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
import com.io7m.jsycamore.core.components.SyButtonReadableType;
import com.io7m.jsycamore.core.components.SyComponentReadableType;
import com.io7m.jsycamore.core.components.SyPanelReadableType;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
import com.io7m.jsycamore.core.themes.SyThemeButtonType;
import com.io7m.jsycamore.core.themes.SyThemeEmbossType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;

public final class SyComponentRendererAWT implements
  SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage>
{
  private final SyEmbossed embossed;

  private SyComponentRendererAWT()
  {
    this.embossed = new SyEmbossed();
  }

  public static SyComponentRendererType<
    SyComponentRendererAWTContextType, BufferedImage> create()
  {
    return new SyComponentRendererAWT();
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
      viewport.accumulate(object.position(), object.size());

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
        this.renderButton(context, graphics, button);
        return Unit.unit();
      }, (r, panel) -> {
        this.renderPanel(context, graphics, panel);
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

  private void renderPanel(
    final SyComponentRendererAWTContextType context,
    final Graphics2D graphics,
    final SyPanelReadableType panel)
  {

  }

  private void renderButton(
    final SyComponentRendererAWTContextType context,
    final Graphics2D graphics,
    final SyButtonReadableType button)
  {
    final VectorReadable2IType size = button.size();
    final SyThemeButtonType button_theme = context.theme().buttonTheme();

    final int width = size.getXI();
    final int height = size.getYI();
    final int fill_width;
    final int fill_height;
    final int fill_x;
    final int fill_y;
    final int rect_width;
    final int rect_height;

    final Optional<SyThemeOutlineType> outline_opt = button_theme.outline();
    if (outline_opt.isPresent()) {
      fill_x = 1;
      fill_y = 1;
      fill_width = width - 3;
      fill_height = height - 3;
      rect_width = width - 2;
      rect_height = height - 2;
    } else {
      fill_x = 0;
      fill_y = 0;
      fill_width = width;
      fill_height = height;
      rect_width = width;
      rect_height = height;
    }

    if (!button.isEnabled()) {
      this.renderButtonFill(
        graphics,
        fill_width,
        fill_height,
        fill_x,
        fill_y,
        button_theme.embossDisabled(),
        button_theme.colorDisabled());

      if (outline_opt.isPresent()) {
        final SyThemeOutlineType outline = outline_opt.get();
        graphics.setPaint(
          SyComponentRendererAWT.toColor(outline.colorInactive()));
        graphics.drawRect(0, 0, rect_width, rect_height);
      }
      return;
    }

    switch (button.buttonState()) {
      case BUTTON_ACTIVE: {
        this.renderButtonFill(
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
        this.renderButtonFill(
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
        this.renderButtonFill(
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

    if (outline_opt.isPresent()) {
      final SyThemeOutlineType outline = outline_opt.get();
      graphics.setPaint(SyComponentRendererAWT.toColor(outline.colorActive()));
      graphics.drawRect(0, 0, rect_width, rect_height);
    }
  }

  private void renderButtonFill(
    final Graphics2D graphics,
    final int fill_width,
    final int fill_height,
    final int fill_x,
    final int fill_y,
    final Optional<SyThemeEmbossType> emboss_opt,
    final VectorReadable3FType fill_color)
  {
    final Color fill =
      SyComponentRendererAWT.toColor(fill_color);

    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      this.embossed.rectangle(
        graphics,
        fill_x,
        fill_y,
        fill_width,
        fill_height,
        emboss.size(),
        SyComponentRendererAWT.toColor(emboss.colorLeft()),
        SyComponentRendererAWT.toColor(emboss.colorRight()),
        SyComponentRendererAWT.toColor(emboss.colorTop()),
        SyComponentRendererAWT.toColor(emboss.colorBottom()),
        Optional.of(fill));
    } else {
      graphics.setPaint(fill);
      graphics.fillRect(fill_x, fill_y, fill_width, fill_height);
    }
  }

  private static Color toColor(
    final VectorReadable3FType color)
  {
    final float r = Math.min(1.0f, Math.max(0.0f, color.getXF()));
    final float g = Math.min(1.0f, Math.max(0.0f, color.getYF()));
    final float b = Math.min(1.0f, Math.max(0.0f, color.getZF()));
    return new Color(r, g, b);
  }
}
