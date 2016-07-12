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

package com.io7m.jsycamore.tests.core;

import com.io7m.jsycamore.caffeine.SyBufferedImageCacheCaffeine;
import com.io7m.jsycamore.core.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.core.images.SyImageCacheResolverType;
import com.io7m.jsycamore.core.images.SyImageCacheType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageReferenceType;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class CacheDemo
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(CacheDemo.class);
  }

  private CacheDemo()
  {

  }

  public static void main(final String[] args)
    throws InterruptedException, ExecutionException, IOException
  {
    final ExecutorService exec = Executors.newFixedThreadPool(1);

    final SyImageCacheResolverType resolver =
      i -> new FileInputStream("/tmp/window_new.png");

    final SyImageCacheLoaderType<BufferedImage> loader = (i, is) -> {
      final BufferedImage image = ImageIO.read(is);
      if (image == null) {
        throw new IOException("Unable to parse image");
      }

      LOG.debug(
        "size: {}: {}", i.name(),
        Integer.valueOf(image.getData().getDataBuffer().getSize()));
      return image;
    };

    final BufferedImage in_default = new BufferedImage(
      2, 2, BufferedImage.TYPE_USHORT_565_RGB);
    final BufferedImage in_error = new BufferedImage(
      2, 2, BufferedImage.TYPE_USHORT_565_RGB);

    final SyImageCacheType<BufferedImage> cache =
      SyBufferedImageCacheCaffeine.create(
        resolver, loader, exec, in_default, in_error, 200_000L);

    Assertive.require(cache.maximumSize() == 200_000L);
    Assertive.require(cache.size() == 0L);

    final Random r = new Random(0L);

    {
      final byte[] buf = new byte[16];
      r.nextBytes(buf);
      final String name = new String(buf, StandardCharsets.US_ASCII);

      final SyImageSpecification.Builder spec_b = SyImageSpecification.builder();
      spec_b.setFormat(SyImageFormat.IMAGE_FORMAT_RGB_565);
      spec_b.setHeight(512);
      spec_b.setWidth(512);
      spec_b.setName(name);
      spec_b.setScaleInterpolation(
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST);

      final SyImageSpecification spec = spec_b.build();
      final SyImageReferenceType<BufferedImage> i = cache.get(spec);
      System.out.println(i.value());

      final BufferedImage image = i.future().get();
      System.out.println(i.value());
      System.out.println(cache.size());
      System.out.println(cache.count());
      System.out.println("---");

      ImageIO.write(image, "PNG", new File("/tmp/out.png"));
    }

    exec.shutdown();
    exec.awaitTermination(1L, TimeUnit.SECONDS);
  }
}
