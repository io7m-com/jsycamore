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

package com.io7m.jsycamore.core.renderer;

import com.io7m.jfunctional.Unit;
import com.io7m.jnull.NullCheck;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyButtonReadableType;
import com.io7m.jsycamore.core.components.SyComponentReadableType;
import com.io7m.jsycamore.core.components.SyImageReadableType;
import com.io7m.jsycamore.core.components.SyLabelReadableType;
import com.io7m.jsycamore.core.components.SyPanelReadableType;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
import com.io7m.jsycamore.core.images.SyImageCacheType;
import com.io7m.jsycamore.core.images.SyImageReferenceType;
import com.io7m.jsycamore.core.themes.SyThemeButtonType;
import com.io7m.jsycamore.core.themes.SyThemeEmbossType;
import com.io7m.jsycamore.core.themes.SyThemeImageType;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jsycamore.core.themes.SyThemePanelType;
import com.io7m.jtensors.VectorReadable3FType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;

/**
 * An AWT-based component renderer.
 */

public final class SyComponentRendererAWT implements
  SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage>
{
  private final SyEmbossed embossed;
  private final SyTextMeasurementType measurement;
  private final SyImageCacheType<BufferedImage> cache;

  private SyComponentRendererAWT(
    final SyImageCacheType<BufferedImage> in_cache,
    final SyTextMeasurementType in_measurement)
  {
    this.cache = NullCheck.notNull(in_cache);
    this.measurement = NullCheck.notNull(in_measurement);
    this.embossed = new SyEmbossed();
  }

  /**
   * Construct a new renderer.
   *
   * @param in_cache       An image cache
   * @param in_measurement A text measurement interface
   *
   * @return A new renderer
   */

  public static SyComponentRendererType<
    SyComponentRendererAWTContextType, BufferedImage> create(
    final SyImageCacheType<BufferedImage> in_cache,
    final SyTextMeasurementType in_measurement)
  {
    return new SyComponentRendererAWT(in_cache, in_measurement);
  }

  @Override
  public BufferedImage render(
    final SyComponentRendererAWTContextType context,
    final SyComponentReadableType object)
  {
    NullCheck.notNull(context);
    NullCheck.notNull(object);

    final BufferedImage image = context.image();
    final Graphics2D graphics = image.createGraphics();

    try {
      graphics.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setRenderingHint(
        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      this.renderActual(
        context,
        graphics,
        graphics.getTransform(),
        graphics.getClip(),
        object);
      return image;
    } finally {
      graphics.dispose();
    }
  }

  private void renderActual(
    final SyComponentRendererAWTContextType context,
    final Graphics2D graphics,
    final AffineTransform initial_transform,
    final Shape initial_clip,
    final SyComponentReadableType object)
  {
    /**
     * Do not render invisible components.
     */

    if (!object.isVisible()) {
      return;
    }

    final SyWindowViewportAccumulatorType viewport = context.viewport();

    try {
      viewport.accumulate(object.box());

      final int max_x = viewport.maximumX();
      final int min_x = viewport.minimumX();
      final int max_y = viewport.maximumY();
      final int min_y = viewport.minimumY();
      final int width = max_x - min_x;
      final int height = max_y - min_y;

      graphics.setClip(initial_clip);
      graphics.setTransform(initial_transform);
      graphics.clipRect(min_x, min_y, width, height);
      graphics.translate(min_x, min_y);

      object.matchComponentReadable(this, (r, button) -> {
        this.renderButton(graphics, button);
        return Unit.unit();
      }, (r, panel) -> {
        this.renderPanel(graphics, panel);
        return Unit.unit();
      }, (r, label) -> {
        this.renderLabel(graphics, label);
        return Unit.unit();
      }, (r, image) -> {
        this.renderImage(graphics, image);
        return Unit.unit();
      });

      final JOTreeNodeReadableType<SyComponentReadableType> node =
        object.nodeReadable();
      final Collection<JOTreeNodeReadableType<SyComponentReadableType>> child_nodes =
        node.childrenReadable();

      for (final JOTreeNodeReadableType<SyComponentReadableType> child_node : child_nodes) {
        this.renderActual(
          context,
          graphics,
          initial_transform,
          initial_clip,
          child_node.value());
      }

    } finally {
      viewport.restore();
    }
  }

  private void renderImage(
    final Graphics2D graphics,
    final SyImageReadableType image)
  {
    final SyThemeImageType theme = image.theme();
    final SyBoxType<SySpaceParentRelativeType> box = image.box();

    final SyImageReferenceType<BufferedImage> ref =
      this.cache.get(image.image());

    final BufferedImage actual = ref.value();
    final int area_width = box.width();
    final int area_height = box.height();

    int x = 0;
    switch (image.imageAlignmentHorizontal()) {
      case ALIGN_LEFT: {
        break;
      }
      case ALIGN_RIGHT: {
        x = area_width - actual.getWidth();
        break;
      }
      case ALIGN_CENTER: {
        x = (area_width / 2) - (actual.getWidth() / 2);
        break;
      }
    }

    int y = 0;
    switch (image.imageAlignmentVertical()) {
      case ALIGN_TOP: {
        break;
      }
      case ALIGN_BOTTOM: {
        y = area_height - actual.getHeight();
        break;
      }
      case ALIGN_CENTER: {
        y = (area_height / 2) - (actual.getHeight() / 2);
        break;
      }
    }

    graphics.drawImage(actual, x, y, null);

    final Optional<SyThemeOutlineType> outline_opt = theme.outline();
    if (outline_opt.isPresent()) {
      SyDrawing.drawOutline(
        graphics,
        outline_opt.get(),
        SyBoxes.moveToOrigin(box),
        image.isEnabled());
    }
  }

  private void renderLabel(
    final Graphics2D graphics,
    final SyLabelReadableType label)
  {
    final SyThemeLabelType label_theme = label.theme();
    final SyBoxType<SySpaceParentRelativeType> box = label.box();

    final String font = label_theme.textFont();
    graphics.setPaint(SyDrawing.toColor(label_theme.textColorActive()));
    graphics.setFont(this.measurement.decodeFont(font));

    SyTextRenderer.renderText(
      this.measurement,
      graphics,
      font,
      box.width(),
      box.height(),
      label.textAlignmentHorizontal(),
      label.textAlignmentVertical(),
      label.text());
  }

  private void renderPanel(
    final Graphics2D graphics,
    final SyPanelReadableType panel)
  {
    if (panel.isPanelTransparent()) {
      return;
    }

    final SyThemePanelType panel_theme = panel.theme();
    final SyBoxType<SySpaceParentRelativeType> box = panel.box();

    final int width = box.width();
    final int height = box.height();
    final int fill_width;
    final int fill_height;
    final int fill_x;
    final int fill_y;

    final Optional<SyThemeOutlineType> outline = panel_theme.outline();
    if (outline.isPresent()) {
      fill_x = 1;
      fill_y = 1;
      fill_width = width - 2;
      fill_height = height - 2;
    } else {
      fill_x = 0;
      fill_y = 0;
      fill_width = width;
      fill_height = height;
    }

    final Optional<SyThemeEmbossType> emboss;
    final VectorReadable3FType color;
    if (panel.isEnabled()) {
      emboss = panel_theme.embossActive();
      color = panel_theme.colorActive();
    } else {
      emboss = panel_theme.embossInactive();
      color = panel_theme.colorInactive();
    }

    this.renderOptionallyEmbossedFill(
      graphics,
      fill_width,
      fill_height,
      fill_x,
      fill_y,
      emboss,
      color);

    if (outline.isPresent()) {
      SyDrawing.drawOutline(
        graphics, outline.get(), SyBoxes.moveToOrigin(box), panel.isEnabled());
    }
  }

  private void renderButton(
    final Graphics2D graphics,
    final SyButtonReadableType button)
  {
    final SyThemeButtonType button_theme = button.theme();
    final SyBoxType<SySpaceParentRelativeType> box = button.box();

    final int width = box.width();
    final int height = box.height();

    final int fill_width;
    final int fill_height;
    final int fill_x;
    final int fill_y;

    final Optional<SyThemeOutlineType> outline = button_theme.outline();
    if (outline.isPresent()) {
      fill_x = 1;
      fill_y = 1;
      fill_width = width - 2;
      fill_height = height - 2;
    } else {
      fill_x = 0;
      fill_y = 0;
      fill_width = width;
      fill_height = height;
    }

    if (!button.isEnabled()) {
      this.renderOptionallyEmbossedFill(
        graphics,
        fill_width,
        fill_height,
        fill_x,
        fill_y,
        button_theme.embossDisabled(),
        button_theme.colorDisabled());
    } else {
      switch (button.buttonState()) {
        case BUTTON_ACTIVE: {
          this.renderOptionallyEmbossedFill(
            graphics,
            fill_width,
            fill_height,
            fill_x,
            fill_y,
            button_theme.embossActive(),
            button_theme.colorActive());
          break;
        }
        case BUTTON_OVER: {
          this.renderOptionallyEmbossedFill(
            graphics,
            fill_width,
            fill_height,
            fill_x,
            fill_y,
            button_theme.embossOver(),
            button_theme.colorOver());
          break;
        }
        case BUTTON_PRESSED: {
          this.renderOptionallyEmbossedFill(
            graphics,
            fill_width,
            fill_height,
            fill_x,
            fill_y,
            button_theme.embossPressed(),
            button_theme.colorPressed());
          break;
        }
      }
    }

    if (outline.isPresent()) {
      SyDrawing.drawOutline(
        graphics, outline.get(), SyBoxes.moveToOrigin(box), button.isEnabled());
    }
  }

  private void renderOptionallyEmbossedFill(
    final Graphics2D graphics,
    final int fill_width,
    final int fill_height,
    final int fill_x,
    final int fill_y,
    final Optional<SyThemeEmbossType> emboss_opt,
    final VectorReadable3FType fill_color)
  {
    final Color fill = SyDrawing.toColor(fill_color);

    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      this.embossed.rectangle(
        graphics,
        fill_x,
        fill_y,
        fill_width,
        fill_height,
        emboss.size(),
        SyDrawing.toColor(emboss.colorLeft()),
        SyDrawing.toColor(emboss.colorRight()),
        SyDrawing.toColor(emboss.colorTop()),
        SyDrawing.toColor(emboss.colorBottom()),
        Optional.of(fill));
    } else {
      graphics.setPaint(fill);
      graphics.fillRect(fill_x, fill_y, fill_width, fill_height);
    }
  }
}
