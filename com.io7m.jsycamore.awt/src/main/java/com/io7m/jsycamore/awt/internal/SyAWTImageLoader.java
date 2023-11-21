/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.awt.internal;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CompletionException;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;

/**
 * An image loader.
 *
 * This implementation automatically resizes loaded images according to their
 * requested size, and caches the resized images in a fixed-size cache.
 */

public final class SyAWTImageLoader
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyAWTImageLoader.class);

  private final BufferedImage failed;
  private final AsyncLoadingCache<SyAWTImageRequest, BufferedImage> cache;
  private final BufferedImage loading;

  /**
   * Construct an image loader.
   */

  public SyAWTImageLoader()
  {
    this.cache =
      Caffeine.newBuilder()
        .maximumWeight(16_000_000L)
        .weigher((Weigher<SyAWTImageRequest, BufferedImage>) (key, value) -> imageBytes(
          value))
        .buildAsync(SyAWTImageLoader::loadImage);

    this.failed = new BufferedImage(4, 4, TYPE_4BYTE_ABGR_PRE);

    {
      final var graphics = this.failed.createGraphics();
      try {
        graphics.setPaint(Color.RED);
        graphics.fillRect(0, 0, 4, 4);
      } finally {
        graphics.dispose();
      }
    }

    this.loading = new BufferedImage(4, 4, TYPE_4BYTE_ABGR_PRE);

    {
      final var graphics = this.failed.createGraphics();
      try {
        graphics.clearRect(0, 0, 4, 4);
      } finally {
        graphics.dispose();
      }
    }
  }

  private static int imageBytes(
    final BufferedImage image)
  {
    return image.getWidth() * image.getHeight() * 4;
  }

  private static BufferedImage loadImage(
    final SyAWTImageRequest request)
    throws IOException
  {
    try {
      final var url = request.source().toURL();
      final var image = ImageIO.read(url);
      final var requestWidth = request.width();
      final var requestHeight = request.height();
      final var originalWidth = image.getWidth();
      final var originalHeight = image.getHeight();

      image.getColorModel()
        .coerceData(image.getRaster(), true);

      final var matchesWidth = originalWidth == requestWidth;
      final var matchesHeight = originalHeight == requestHeight;

      if (!matchesWidth || !matchesHeight) {
        final var result =
          new BufferedImage(requestWidth, requestHeight, TYPE_4BYTE_ABGR_PRE);

        final var graphics = result.createGraphics();
        try {
          graphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
          graphics.drawImage(
            image,
            0,
            0,
            requestWidth,
            requestHeight,
            0,
            0,
            originalWidth,
            originalHeight,
            null
          );
          return result;
        } finally {
          graphics.dispose();
        }
      }

      return image;
    } catch (final IOException e) {
      LOG.error("unable to load image {}: ", request.source(), e);
      throw e;
    }
  }

  /**
   * Load the given image request. If the request cannot be fulfilled, an
   * "error" image will be returned.
   *
   * @param request The image request
   *
   * @return An image
   */

  public BufferedImage load(
    final SyAWTImageRequest request)
  {
    try {
      final var future = this.cache.get(request);
      if (future.isCompletedExceptionally()) {
        return this.failed;
      }
      return future.getNow(this.loading);
    } catch (final CompletionException e) {
      return this.failed;
    }
  }
}
