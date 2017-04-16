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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.awt.SyAWTComponentRenderer;
import com.io7m.jsycamore.awt.SyAWTComponentRendererContextType;
import com.io7m.jsycamore.awt.SyAWTTextMeasurement;
import com.io7m.jsycamore.awt.SyAWTWindowRenderer;
import com.io7m.jsycamore.caffeine.SyBufferedImageCacheCaffeine;
import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.SyWindowContentPaneType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.components.SyActive;
import com.io7m.jsycamore.core.components.SyButtonCheckbox;
import com.io7m.jsycamore.core.components.SyButtonCheckboxType;
import com.io7m.jsycamore.core.components.SyButtonRepeating;
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.components.SyImage;
import com.io7m.jsycamore.core.components.SyImageType;
import com.io7m.jsycamore.core.components.SyLabel;
import com.io7m.jsycamore.core.components.SyLabelType;
import com.io7m.jsycamore.core.components.SyMeter;
import com.io7m.jsycamore.core.components.SyMeterType;
import com.io7m.jsycamore.core.components.SyPanel;
import com.io7m.jsycamore.core.components.SyPanelType;
import com.io7m.jsycamore.core.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.core.images.SyImageCacheResolverType;
import com.io7m.jsycamore.core.images.SyImageCacheType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageReferenceType;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import com.io7m.jsycamore.core.renderer.SyComponentRendererType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jsycamore.core.themes.provided.SyThemeFenestra;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jsycamore.core.themes.provided.SyThemeStride;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class WindowDump
{
  private WindowDump()
  {

  }

  public static void main(final String[] args)
    throws Exception
  {
    final List<SyTheme> themes = new ArrayList<>();
    themes.add(SyThemeFenestra.builder().build());
    themes.add(SyThemeMotive.builder().build());
    themes.add(SyThemeBee.builder().build());
    themes.add(SyThemeStride.builder().build());
    // Collections.shuffle(themes, new Random());

    final ExecutorService io_executor = Executors.newFixedThreadPool(4);
    final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> component_renderer;
    final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer;
    final SyAWTTextMeasurement text_measure = SyAWTTextMeasurement.create();
    final SyImageCacheType<BufferedImage> image_cache;

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

      final SyImageSpecification image_spec = SyImageSpecification.of(
        "/com/io7m/jsycamore/tests/awt/circle-x-8x.png",
        64,
        64,
        SyImageFormat.IMAGE_FORMAT_GREY_8,
        Vector4D.of(1.0f, 1.0f, 1.0f, 1.0f),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

      final BufferedImage image_default =
        loader.load(image_spec, resolver.resolve(image_spec));

      image_cache =
        SyBufferedImageCacheCaffeine.create(
          resolver,
          loader,
          io_executor,
          image_default,
          image_default,
          1_000_000L);

      component_renderer =
        SyAWTComponentRenderer.create(image_cache, text_measure, text_measure);
      window_renderer =
        SyAWTWindowRenderer.create(component_renderer);
    }

    final SyGUIType gui =
      SyGUI.createWithTheme(text_measure, "main", themes.get(0));

    final BufferedImage output_image =
      new BufferedImage(
        16 + 320 + 16 + 320 + 16,
        themes.size() * (16 + 240 + 16),
        BufferedImage.TYPE_4BYTE_ABGR);

    final SyImageSpecification icon_spec = SyImageSpecification.of(
      "/com/io7m/jsycamore/tests/awt/paper.png",
      16,
      16,
      SyImageFormat.IMAGE_FORMAT_RGBA_8888,
      Vector4D.of(1.0f, 1.0f, 1.0f, 1.0f),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST);

    {
      final SyImageReferenceType<BufferedImage> f = image_cache.get(icon_spec);
      f.future().get(10L, TimeUnit.SECONDS);
    }

    final Graphics2D output = output_image.createGraphics();
    int y = 16;
    for (int index = 0; index < themes.size(); ++index) {
      final SyTheme theme = themes.get(index);
      final SyWindowType w_inactive = WindowDump.createWindow(gui, theme);
      w_inactive.titleBar().setIcon(Optional.of(icon_spec));

      final SyWindowType w_active = WindowDump.createWindow(gui, theme);
      w_active.titleBar().setIcon(Optional.of(icon_spec));

      {
        final BufferedImage active_image =
          new BufferedImage(320, 240, BufferedImage.TYPE_4BYTE_ABGR);

        window_renderer.render(active_image, w_active);
        Thread.sleep(1000L);
        window_renderer.render(active_image, w_active);

        output.drawImage(active_image, 16, y, null);
      }

      {
        final BufferedImage inactive_image =
          new BufferedImage(320, 240, BufferedImage.TYPE_4BYTE_ABGR);

        window_renderer.render(inactive_image, w_inactive);
        Thread.sleep(1000L);
        window_renderer.render(inactive_image, w_inactive);

        output.drawImage(inactive_image, 16 + 320 + 16, y, null);
      }

      y += 240;
      y += 16;
    }

    ImageIO.write(output_image, "PNG", new File("/tmp/window.png"));

    io_executor.shutdown();
    io_executor.awaitTermination(5L, TimeUnit.SECONDS);
  }

  private static SyWindowType createWindow(
    final SyGUIType gui,
    final SyTheme theme)
  {
    final SyWindowType w = gui.windowCreate(320, 240, "Main");
    w.setTheme(Optional.of(theme));

    final SyWindowContentPaneType content = w.contentPane();
    {
      final SyPanelType panel = SyPanel.create();
      panel.setBox(PAreasI.create(
        0,
        0,
        content.box().width(),
        content.box().height()));
      content.node().childAdd(panel.node());

      int y_base = 8;

      {
        final SyButtonType button = SyButtonRepeating.create();
        button.setBox(PAreasI.create(8, y_base, 64, 32));
        button.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        panel.node().childAdd(button.node());

        final SyLabelType label = SyLabel.create();
        label.setBox(PAreasI.create(0, 0, 64, 32));
        label.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        label.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        label.setText("Hello");
        button.node().childAdd(label.node());
      }

      {
        final SyButtonType button = SyButtonRepeating.create();
        button.setBox(PAreasI.create(8 + 64 + 8, y_base, 64, 32));
        button.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setActive(SyActive.INACTIVE);
        panel.node().childAdd(button.node());

        final SyLabelType label = SyLabel.create();
        label.setBox(PAreasI.create(0, 0, 64, 32));
        label.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        label.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        label.setText("Hello");
        button.node().childAdd(label.node());
      }

      y_base += 32 + 8;

      {
        final SyImageSpecification spec = SyImageSpecification.of(
          "/com/io7m/jsycamore/tests/awt/wheat.png",
          64,
          64,
          SyImageFormat.IMAGE_FORMAT_RGB_888,
          Vector4D.of(1.0f, 0.0f, 0.0f, 1.0f),
          SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

        final SyImageType image = SyImage.create(spec);
        image.setBox(PAreasI.create(8, y_base, 64, 64));
        image.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        image.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        panel.node().childAdd(image.node());
      }

      {
        final SyImageSpecification spec = SyImageSpecification.of(
          "/com/io7m/jsycamore/tests/awt/wheat.png",
          64,
          64,
          SyImageFormat.IMAGE_FORMAT_RGB_888,
          Vector4D.of(0.0f, 1.0f, 0.0f, 1.0f),
          SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

        final SyImageType image = SyImage.create(spec);
        image.setBox(PAreasI.create(8 + 64 + 8, y_base, 64, 64));
        image.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        image.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        panel.node().childAdd(image.node());
      }

      {
        final SyImageSpecification spec = SyImageSpecification.of(
          "/com/io7m/jsycamore/tests/awt/wheat.png",
          64,
          64,
          SyImageFormat.IMAGE_FORMAT_RGB_888,
          Vector4D.of(0.0, 0.0, 1.0, 1.0),
          SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

        final SyImageType image = SyImage.create(spec);
        image.setBox(PAreasI.create(8 + 64 + 8 + 64 + 8, y_base, 64, 64));
        image.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        image.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        panel.node().childAdd(image.node());
      }

      y_base += 64 + 8;

      {
        final SyButtonCheckboxType button = SyButtonCheckbox.create();
        button.setBox(PAreasI.create(8, y_base, 16, 16));
        button.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setActive(SyActive.ACTIVE);
        button.setChecked(false);
        panel.node().childAdd(button.node());
      }

      {
        final SyButtonCheckboxType button = SyButtonCheckbox.create();
        button.setBox(PAreasI.create(8 + 16 + 4, y_base, 16, 16));
        button.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setActive(SyActive.ACTIVE);
        button.setChecked(true);
        panel.node().childAdd(button.node());
      }

      {
        final SyButtonCheckboxType button = SyButtonCheckbox.create();
        button.setBox(PAreasI.create(8 + 16 + 4 + 16 + 4, y_base, 16, 16));
        button.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setActive(SyActive.INACTIVE);
        button.setChecked(false);
        panel.node().childAdd(button.node());
      }

      {
        final SyButtonCheckboxType button = SyButtonCheckbox.create();
        button.setBox(PAreasI.create(
          8 + 16 + 4 + 16 + 4 + 16 + 4,
          y_base,
          16,
          16));
        button.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        button.setActive(SyActive.INACTIVE);
        button.setChecked(true);
        panel.node().childAdd(button.node());
      }

      y_base += 16 + 8;

      {
        final SyMeterType meter = SyMeter.create();
        meter.setBox(PAreasI.create(8, y_base, 128, 16));
        meter.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        meter.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        meter.setActive(SyActive.ACTIVE);
        meter.setValue(0.75);
        panel.node().childAdd(meter.node());
      }

      y_base += 16 + 8;

      {
        final SyMeterType meter = SyMeter.create();
        meter.setBox(PAreasI.create(8, y_base, 128, 16));
        meter.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        meter.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        meter.setActive(SyActive.INACTIVE);
        meter.setValue(0.25);
        panel.node().childAdd(meter.node());
      }
    }

    return w;
  }
}
