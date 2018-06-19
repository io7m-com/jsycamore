/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jsycamore.caffeine;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Weigher;
import com.io7m.jsycamore.api.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.api.images.SyImageCacheResolverType;
import com.io7m.jsycamore.api.images.SyImageCacheType;
import com.io7m.jsycamore.api.images.SyImageReferenceType;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.awt.SyAWTImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link BufferedImage} cache backed by {@code caffeine}.
 */

public final class SyBufferedImageCacheCaffeine implements SyImageCacheType<BufferedImage>
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyBufferedImageCacheCaffeine.class);
  }

  private final AsyncLoadingCache<SyImageSpecification, BufferedImage> cache;
  private final BufferedImage image_default;
  private final BufferedImage image_error;
  private final long size_max;

  private SyBufferedImageCacheCaffeine(
    final AsyncLoadingCache<SyImageSpecification, BufferedImage> in_cache,
    final BufferedImage in_default,
    final BufferedImage in_error,
    final long in_size_max)
  {
    this.cache =
      Objects.requireNonNull(in_cache, "Cache");
    this.image_default =
      Objects.requireNonNull(in_default, "Default image");
    this.image_error =
      Objects.requireNonNull(in_error, "Error image");
    this.size_max = in_size_max;
  }

  /**
   * Create a new cache.
   *
   * @param in_resolver      The image resolver
   * @param in_loader        The image loader
   * @param in_executor      An executor for executing long-running IO operations
   * @param in_image_default The default image used whilst loading
   * @param in_image_error   The image displayed when loading fails
   * @param in_maximum_size  The maximum size of the cache in bytes
   *
   * @return A new cache
   */

  public static SyImageCacheType<BufferedImage> create(
    final SyImageCacheResolverType in_resolver,
    final SyImageCacheLoaderType<BufferedImage> in_loader,
    final Executor in_executor,
    final BufferedImage in_image_default,
    final BufferedImage in_image_error,
    final long in_maximum_size)
  {
    Objects.requireNonNull(in_loader, "Loader");
    Objects.requireNonNull(in_resolver, "Resolver");
    Objects.requireNonNull(in_executor, "Executor");
    Objects.requireNonNull(in_image_default, "Default image");
    Objects.requireNonNull(in_image_error, "Error image");

    final Weigher<SyImageSpecification, BufferedImage> weigher =
      (key, value) -> value.getData().getDataBuffer().getSize();

    final AsyncCacheLoader<? super SyImageSpecification, BufferedImage> loader =
      (AsyncCacheLoader<SyImageSpecification, BufferedImage>) (image_spec, executor) ->
        SyBufferedImageCacheCaffeine.load(
          in_resolver, in_loader, image_spec, executor);

    final RemovalListener<SyImageSpecification, BufferedImage> removal_listener =
      (key, value, cause) -> SyBufferedImageCacheCaffeine.LOG.trace(
        "removal: {} {} {}", key, value, cause);

    final AsyncLoadingCache<SyImageSpecification, BufferedImage> cache =
      Caffeine.newBuilder()
        .maximumWeight(in_maximum_size)
        .expireAfterAccess(60L, TimeUnit.SECONDS)
        .weigher(weigher)
        .executor(in_executor)
        .removalListener(removal_listener)
        .buildAsync(loader);

    return new SyBufferedImageCacheCaffeine(
      cache, in_image_default, in_image_error, in_maximum_size);
  }

  private static CompletableFuture<BufferedImage> load(
    final SyImageCacheResolverType in_resolver,
    final SyImageCacheLoaderType<BufferedImage> in_loader,
    final SyImageSpecification in_image_spec,
    final Executor in_executor)
  {
    return CompletableFuture.supplyAsync(() -> {
      try (final InputStream is = in_resolver.resolve(in_image_spec)) {
        final BufferedImage image = in_loader.load(in_image_spec, is);
        return SyAWTImage.filter(in_image_spec, image);
      } catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }, in_executor);
  }

  @Override
  public SyImageReferenceType<BufferedImage> get(final SyImageSpecification i)
  {
    return new ImageReference(
      this.cache,
      i,
      this.image_default,
      this.image_error,
      this.cache.get(i));
  }

  @Override
  public long maximumSize()
  {
    return this.size_max;
  }

  @Override
  public long size()
  {
    final Policy<SyImageSpecification, BufferedImage> policy =
      this.cache.synchronous().policy();
    final Optional<Policy.Eviction<SyImageSpecification, BufferedImage>> evict =
      policy.eviction();
    return evict.map(Policy.Eviction::weightedSize)
      .orElse(OptionalLong.of(0L)).getAsLong();
  }

  @Override
  public long count()
  {
    return this.cache.synchronous().estimatedSize();
  }

  @Override
  public void close()
    throws IOException
  {

  }

  private static final class ImageReference
    implements SyImageReferenceType<BufferedImage>
  {
    private final SyImageSpecification spec;
    private final CompletableFuture<BufferedImage> future;
    private final BufferedImage image_default;
    private final BufferedImage image_error;
    private final AsyncLoadingCache<SyImageSpecification, BufferedImage> cache;

    ImageReference(
      final AsyncLoadingCache<SyImageSpecification, BufferedImage> in_cache,
      final SyImageSpecification in_spec,
      final BufferedImage in_default,
      final BufferedImage in_error,
      final CompletableFuture<BufferedImage> in_future)
    {
      this.cache =
        Objects.requireNonNull(in_cache, "Cache");
      this.spec =
        Objects.requireNonNull(in_spec, "Image specification");
      this.image_default =
        Objects.requireNonNull(in_default, "Default image");
      this.image_error =
        Objects.requireNonNull(in_error, "Error image");
      this.future =
        Objects.requireNonNull(in_future, "Image future");
    }

    @Override
    public CompletableFuture<BufferedImage> future()
    {
      return this.future;
    }

    @Override
    public SyImageSpecification description()
    {
      return this.spec;
    }

    @Override
    public BufferedImage value()
    {
      if (this.future.isDone()) {
        try {
          return this.future.get();
        } catch (final InterruptedException e) {
          Thread.currentThread().interrupt();
          return this.image_error;
        } catch (final ExecutionException e) {
          final LoadingCache<SyImageSpecification, BufferedImage> scache =
            this.cache.synchronous();
          scache.put(this.spec, this.image_error);
          return this.image_error;
        }
      }
      return this.image_default;
    }
  }
}
