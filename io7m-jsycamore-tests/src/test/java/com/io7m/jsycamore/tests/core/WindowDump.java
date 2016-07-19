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

import com.io7m.jsycamore.awt.SyAWTComponentRenderer;
import com.io7m.jsycamore.awt.SyAWTComponentRendererContextType;
import com.io7m.jsycamore.awt.SyAWTTextMeasurement;
import com.io7m.jsycamore.awt.SyAWTWindowRenderer;
import com.io7m.jsycamore.caffeine.SyBufferedImageCacheCaffeine;
import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyWindowContentPaneType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyPanel;
import com.io7m.jsycamore.core.components.SyPanelType;
import com.io7m.jsycamore.core.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.core.images.SyImageCacheResolverType;
import com.io7m.jsycamore.core.images.SyImageCacheType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import com.io7m.jsycamore.core.renderer.SyComponentRendererType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jsycamore.core.themes.provided.SyThemeFenestra;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jsycamore.core.themes.provided.SyThemeStride;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    final List<SyTheme> themes = new ArrayList<>();
    themes.add(SyThemeFenestra.builder().build());
    themes.add(SyThemeMotive.builder().build());
    themes.add(SyThemeBee.builder().build());
    themes.add(SyThemeStride.builder().build());
    Collections.shuffle(themes, new Random());

    final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> component_renderer;
    final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer;
    final SyAWTTextMeasurement text_measure = SyAWTTextMeasurement.create();

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
        final BufferedImage loaded = ImageIO.read(is);
        if (loaded == null) {
          throw new IOException("Could not parse output_image " + i.name());
        }
        return loaded;
      };

      final ExecutorService io_executor = Executors.newSingleThreadExecutor();

      final SyImageSpecification image_spec = SyImageSpecification.of(
        "/com/io7m/jsycamore/tests/awt/circle-x-8x.png",
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

      component_renderer =
        SyAWTComponentRenderer.create(cache, text_measure, text_measure);
      window_renderer =
        SyAWTWindowRenderer.create(component_renderer);
    }

    final SyGUIType gui = SyGUI.createWithTheme(
      text_measure,
      "main",
      themes.get(0));

    final BufferedImage output_image =
      new BufferedImage(
        16 + 320 + 16 + 320 + 16,
        themes.size() * (16 + 240 + 16),
        BufferedImage.TYPE_4BYTE_ABGR);

    final Graphics2D output = output_image.createGraphics();
    int y = 16;
    for (int index = 0; index < themes.size(); ++index) {
      final SyTheme theme = themes.get(index);
      final SyWindowType w_inactive = WindowDump.createWindow(gui, theme);
      final SyWindowType w_active = WindowDump.createWindow(gui, theme);

      {
        final BufferedImage active_image =
          new BufferedImage(320, 240, BufferedImage.TYPE_4BYTE_ABGR);

        window_renderer.render(active_image, w_active);
        output.drawImage(active_image, 16, y, null);
      }

      {
        final BufferedImage inactive_image =
          new BufferedImage(320, 240, BufferedImage.TYPE_4BYTE_ABGR);

        window_renderer.render(inactive_image, w_inactive);
        output.drawImage(inactive_image, 16 + 320 + 16, y, null);
      }

      y += 240;
      y += 16;
    }

    ImageIO.write(output_image, "PNG", new File("/tmp/window.png"));
  }

  private static SyWindowType createWindow(
    final SyGUIType gui,
    final SyTheme theme)
  {
    final SyWindowType w = gui.windowCreate(320, 240, "Main");

    final SyWindowContentPaneType content = w.contentPane();
    {
      final SyPanelType c = SyPanel.create();
      c.setBox(SyBoxes.create(
        0,
        0,
        content.box().width(),
        content.box().height()));
      content.node().childAdd(c.node());
    }

    w.setTheme(Optional.of(theme));
    return w;
  }
}
