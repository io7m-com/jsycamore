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

package com.io7m.jsycamore.awt;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyImageReadableType;
import com.io7m.jsycamore.api.components.SyLabelReadableType;
import com.io7m.jsycamore.api.components.SyMeterReadableType;
import com.io7m.jsycamore.api.components.SyPanelReadableType;
import com.io7m.jsycamore.api.components.SyWindowViewportAccumulatorType;
import com.io7m.jsycamore.api.images.SyImageCacheType;
import com.io7m.jsycamore.api.images.SyImageReferenceType;
import com.io7m.jsycamore.api.renderer.SyComponentRendererType;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyTextMeasurementType;
import com.io7m.jsycamore.api.themes.SyThemeButtonType;
import com.io7m.jsycamore.api.themes.SyThemeEmboss;
import com.io7m.jsycamore.api.themes.SyThemeFillType;
import com.io7m.jsycamore.api.themes.SyThemeImage;
import com.io7m.jsycamore.api.themes.SyThemeLabel;
import com.io7m.jsycamore.api.themes.SyThemeLabelType;
import com.io7m.jsycamore.api.themes.SyThemeMeter;
import com.io7m.jsycamore.api.themes.SyThemeMeterOriented;
import com.io7m.jsycamore.api.themes.SyThemeMeterOrientedType;
import com.io7m.jsycamore.api.themes.SyThemeMeterType;
import com.io7m.jsycamore.api.themes.SyThemeOutline;
import com.io7m.jsycamore.api.themes.SyThemePanel;
import com.io7m.jsycamore.api.themes.SyThemePanelType;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * An AWT-based component renderer.
 */

public final class SyAWTComponentRenderer implements
  SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage>
{
  private final SyAWTEmbossed embossed;
  private final SyTextMeasurementType measurement;
  private final SyAWTFontCacheType font_cache;
  private final SyImageCacheType<BufferedImage> image_cache;

  private SyAWTComponentRenderer(
    final SyImageCacheType<BufferedImage> in_image_cache,
    final SyAWTFontCacheType in_font_cache,
    final SyTextMeasurementType in_measurement)
  {
    this.image_cache =
      Objects.requireNonNull(in_image_cache, "Image cache");
    this.font_cache =
      Objects.requireNonNull(in_font_cache, "Font cache");
    this.measurement =
      Objects.requireNonNull(in_measurement, "Text measurement");
    this.embossed = new SyAWTEmbossed();
  }

  /**
   * Construct a new renderer.
   *
   * @param in_image_cache An image cache
   * @param in_measurement A text measurement interface
   * @param in_font_cache  A font cache
   *
   * @return A new renderer
   */

  public static SyComponentRendererType<
    SyAWTComponentRendererContextType, BufferedImage> create(
    final SyImageCacheType<BufferedImage> in_image_cache,
    final SyTextMeasurementType in_measurement,
    final SyAWTFontCacheType in_font_cache)
  {
    return new SyAWTComponentRenderer(
      in_image_cache, in_font_cache, in_measurement);
  }

  private static IllegalStateException notAttached(
    final SyComponentReadableType c)
  {
    Preconditions.checkPrecondition(
      !c.windowReadable().isPresent(),
      "Component is not attached to a window");

    final StringBuilder sb = new StringBuilder(128);
    sb.append("Cannot render a component that is not attached to a window.");
    final String separator = System.lineSeparator();
    sb.append(separator);
    sb.append("  Component: ");
    sb.append(c);
    sb.append(separator);
    return new IllegalStateException(sb.toString());
  }

  @Override
  public BufferedImage render(
    final SyAWTComponentRendererContextType context,
    final SyComponentReadableType object)
  {
    Objects.requireNonNull(context, "Renderer context");
    Objects.requireNonNull(object, "Component");

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
    final SyAWTComponentRendererContextType context,
    final Graphics2D graphics,
    final AffineTransform initial_transform,
    final Shape initial_clip,
    final SyComponentReadableType object)
  {
    /*
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
        return Void.class;
      }, (r, panel) -> {
        this.renderPanel(graphics, panel);
        return Void.class;
      }, (r, label) -> {
        this.renderLabel(graphics, label);
        return Void.class;
      }, (r, image) -> {
        this.renderImage(graphics, image);
        return Void.class;
      }, (r, meter) -> {
        this.renderMeter(graphics, meter);
        return Void.class;
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

  private void renderMeter(
    final Graphics2D graphics,
    final SyMeterReadableType meter)
  {
    final Optional<SyThemeMeter> theme_opt = meter.theme();
    if (!theme_opt.isPresent()) {
      throw notAttached(meter);
    }

    final SyThemeMeterType theme = theme_opt.get();
    final PAreaI<SySpaceParentRelativeType> box = meter.box();
    final PAreaI<SySpaceComponentRelativeType> box_origin =
      PAreasI.cast(PAreasI.moveToOrigin(box));

    switch (meter.orientation()) {
      case ORIENTATION_HORIZONTAL: {
        final int width = (int) (meter.value() * (double) box.sizeX());
        final PAreaI<SySpaceComponentRelativeType> box_indicator =
          PAreasI.create(0, 0, width, box.sizeY());
        final SyThemeMeterOrientedType t = theme.horizontal();
        final Optional<SyThemeOutline> outline = t.outline();

        this.renderThemedBox(
          graphics,
          outline,
          () -> meter.isActive() ? t.fillContainerActive() : t.fillContainerInactive(),
          () -> meter.isActive() ? t.embossContainerActive() : t.embossContainerInactive(),
          box_origin);

        this.renderThemedBox(
          graphics,
          outline,
          () -> meter.isActive() ? t.fillIndicatorActive() : t.fillIndicatorInactive(),
          () -> meter.isActive() ? t.embossIndicatorActive() : t.embossIndicatorInactive(),
          box_indicator);

        outline.ifPresent(
          theme_outlint -> SyAWTDrawing.drawOutline(
            graphics, theme_outlint, box_origin, meter.isActive()));
        break;
      }
      case ORIENTATION_VERTICAL: {
        final int height = (int) (meter.value() * (double) box.sizeY());
        final PAreaI<SySpaceComponentRelativeType> box_indicator =
          PAreasI.create(0, box.sizeY() - height, box.sizeX(), height);

        final SyThemeMeterOriented t = theme.vertical();
        final Optional<SyThemeOutline> outline = t.outline();

        this.renderThemedBox(
          graphics,
          outline,
          () -> meter.isActive() ? t.fillContainerActive() : t.fillContainerInactive(),
          () -> meter.isActive() ? t.embossContainerActive() : t.embossContainerInactive(),
          box_origin);

        this.renderThemedBox(
          graphics,
          outline,
          () -> meter.isActive() ? t.fillIndicatorActive() : t.fillIndicatorInactive(),
          () -> meter.isActive() ? t.embossIndicatorActive() : t.embossIndicatorInactive(),
          box_indicator);

        if (outline.isPresent()) {
          SyAWTDrawing.drawOutline(
            graphics, outline.get(), box_origin, meter.isActive());
        }
        break;
      }
    }
  }

  private void renderImage(
    final Graphics2D graphics,
    final SyImageReadableType image)
  {
    final Optional<SyThemeImage> theme_opt = image.theme();
    if (!theme_opt.isPresent()) {
      throw notAttached(image);
    }

    final PAreaI<SySpaceParentRelativeType> box = image.box();

    final SyImageReferenceType<BufferedImage> ref =
      this.image_cache.get(image.image());

    final BufferedImage actual = ref.value();
    final int area_width = box.sizeX();
    final int area_height = box.sizeY();

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
  }

  private void renderLabel(
    final Graphics2D graphics,
    final SyLabelReadableType label)
  {
    final Optional<SyThemeLabel> theme_opt = label.theme();
    if (!theme_opt.isPresent()) {
      throw notAttached(label);
    }

    final SyThemeLabelType theme = theme_opt.get();
    final PAreaI<SySpaceParentRelativeType> box = label.box();

    if (label.isActive()) {
      graphics.setPaint(SyAWTDrawing.toColor(theme.textColorActive()));
    } else {
      graphics.setPaint(SyAWTDrawing.toColor(theme.textColorInactive()));
    }

    final String font = theme.textFont();
    graphics.setFont(this.font_cache.decodeFont(font));

    SyAWTTextRenderer.renderText(
      this.measurement,
      graphics,
      font,
      box.sizeX(),
      box.sizeY(),
      label.textAlignmentHorizontal(),
      label.textAlignmentVertical(),
      label.text());
  }

  private void renderPanel(
    final Graphics2D graphics,
    final SyPanelReadableType panel)
  {
    final Optional<SyThemePanel> theme_opt = panel.theme();
    if (!theme_opt.isPresent()) {
      throw notAttached(panel);
    }

    if (panel.isPanelTransparent()) {
      return;
    }

    final SyThemePanelType theme = theme_opt.get();
    final PAreaI<SySpaceParentRelativeType> box = panel.box();
    final PAreaI<SySpaceComponentRelativeType> box_origin =
      PAreasI.cast(PAreasI.moveToOrigin(box));
    final Optional<SyThemeOutline> outline = theme.outline();

    this.renderThemedBox(
      graphics,
      outline,
      () -> panel.isActive() ? theme.fillActive() : theme.fillInactive(),
      () -> panel.isActive() ? theme.embossActive() : theme.embossInactive(),
      box_origin
    );

    if (outline.isPresent()) {
      SyAWTDrawing.drawOutline(
        graphics, outline.get(), PAreasI.moveToOrigin(box), panel.isActive());
    }
  }

  private void renderThemedBox(
    final Graphics2D graphics,
    final Optional<SyThemeOutline> outline,
    final Supplier<SyThemeFillType> fill,
    final Supplier<Optional<SyThemeEmboss>> emboss,
    final PAreaI<SySpaceComponentRelativeType> box)
  {
    final PAreaI<SySpaceComponentRelativeType> box_fill;
    if (outline.isPresent()) {
      box_fill = PAreasI.hollowOutEvenly(box, 1);
    } else {
      box_fill = box;
    }

    this.renderOptionallyEmbossedFill(
      graphics, box_fill, emboss.get(), fill.get());
  }

  private void renderButton(
    final Graphics2D graphics,
    final SyButtonReadableType button)
  {
    final Optional<SyThemeButtonType> theme_opt = button.theme();
    if (!theme_opt.isPresent()) {
      throw notAttached(button);
    }

    final SyThemeButtonType theme = theme_opt.get();
    final PAreaI<SySpaceParentRelativeType> box = button.box();
    final PAreaI<SySpaceParentRelativeType> box_origin =
      PAreasI.moveToOrigin(box);

    final PAreaI<SySpaceParentRelativeType> box_fill;
    final Optional<SyThemeOutline> outline = theme.outline();
    if (outline.isPresent()) {
      box_fill = PAreasI.hollowOutEvenly(box_origin, 1);
    } else {
      box_fill = box_origin;
    }

    if (!button.isActive()) {
      this.renderOptionallyEmbossedFill(
        graphics,
        box_fill,
        theme.embossInactive(),
        theme.fillInactive());
    } else {
      switch (button.buttonState()) {
        case BUTTON_ACTIVE: {
          this.renderOptionallyEmbossedFill(
            graphics,
            box_fill,
            theme.embossActive(),
            theme.fillActive());
          break;
        }
        case BUTTON_OVER: {
          this.renderOptionallyEmbossedFill(
            graphics,
            box_fill,
            theme.embossOver(),
            theme.fillOver());
          break;
        }
        case BUTTON_PRESSED: {
          this.renderOptionallyEmbossedFill(
            graphics,
            box_fill,
            theme.embossPressed(),
            theme.fillPressed());
          break;
        }
      }
    }

    if (outline.isPresent()) {
      SyAWTDrawing.drawOutline(
        graphics, outline.get(), box_origin, button.isActive());
    }
  }

  private void renderOptionallyEmbossedFill(
    final Graphics2D graphics,
    final PAreaI<?> box,
    final Optional<SyThemeEmboss> emboss_opt,
    final SyThemeFillType fill)
  {
    final Paint fill_paint = SyAWTDrawing.toPaint(box, fill);

    if (emboss_opt.isPresent()) {
      final SyThemeEmboss emboss = emboss_opt.get();
      this.embossed.rectangle(
        graphics,
        SyAWTEmbossedRectangle.builder()
          .setBox(box)
          .setEmbossSize(emboss.size())
          .setPaintLeft(SyAWTDrawing.toColor(emboss.colorLeft()))
          .setPaintRight(SyAWTDrawing.toColor(emboss.colorRight()))
          .setPaintTop(SyAWTDrawing.toColor(emboss.colorTop()))
          .setPaintBottom(SyAWTDrawing.toColor(emboss.colorBottom()))
          .setPaintFill(fill_paint)
          .build());
    } else {
      graphics.setPaint(fill_paint);
      graphics.fillRect(
        box.minimumX(),
        box.minimumY(),
        box.sizeX(),
        box.sizeY());
    }
  }
}
