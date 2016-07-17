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
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.core.images.SyImageCacheResolverType;
import com.io7m.jsycamore.core.images.SyImageCacheType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import com.io7m.jsycamore.core.renderer.SyComponentRendererAWT;
import com.io7m.jsycamore.core.renderer.SyComponentRendererAWTContextType;
import com.io7m.jsycamore.core.renderer.SyComponentRendererType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererAWT;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeWindow;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jsycamore.core.themes.provided.SyThemeStride;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class WindowDump
{
  private WindowDump()
  {

  }

  public static void main(final String[] args)
    throws IOException
  {
    final SyTheme theme_base = SyThemeBee.builder().build();

    final SyGUIType gui =
      SyGUI.createWithTheme("main", theme_base);
    final SyWindowType w =
      gui.windowCreate(320, 240, "Main");

    {
      final SyImageCacheResolverType resolver = i -> {
        final InputStream stream =
          WindowDump.class.getResourceAsStream(i.name());
        if (stream == null) {
          throw new FileNotFoundException(i.name());
        }
        return stream;
      };

      final SyImageCacheLoaderType<BufferedImage> loader = (i, is) -> {
        final BufferedImage image = ImageIO.read(is);
        if (image == null) {
          throw new IOException("Could not parse image " + i.name());
        }
        return image;
      };

      final ExecutorService io_executor = Executors.newSingleThreadExecutor();

      final SyImageSpecification image_spec = SyImageSpecification.of(
        "circle-x-8x.png",
        64,
        64,
        SyImageFormat.IMAGE_FORMAT_GREY_8,
        SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

      final BufferedImage image_default =
        loader.load(image_spec, resolver.resolve(image_spec));

      final SyImageCacheType<BufferedImage> cache =
        SyBufferedImageCacheCaffeine.create(
        resolver,
        loader,
        io_executor,
        image_default,
        image_default,
        1_000_000L);

      final BufferedImage image =
        new BufferedImage(
          w.box().width(),
          w.box().height(),
          BufferedImage.TYPE_4BYTE_ABGR);

      final SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage> component_renderer =
        SyComponentRendererAWT.create(cache, gui.textMeasurement());
      final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer =
        SyWindowRendererAWT.create(gui.textMeasurement(), component_renderer);

      window_renderer.render(image, w);

      ImageIO.write(image, "PNG", new File("/tmp/window.png"));
    }
  }
}
