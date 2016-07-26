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

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.themes.SyThemeColorType;
import com.io7m.jsycamore.core.themes.SyThemeFillType;
import com.io7m.jsycamore.core.themes.SyThemeGradientLinearType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.util.List;

/**
 * Functions for drawing in AWT contexts.
 */

public final class SyAWTDrawing
{
  private SyAWTDrawing()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Convert the given vector to an AWT color.
   *
   * @param color The color vector
   *
   * @return A color
   */

  public static Color toColor(
    final VectorReadable3FType color)
  {
    NullCheck.notNull(color, "color");

    final float r = Math.min(1.0f, Math.max(0.0f, color.getXF()));
    final float g = Math.min(1.0f, Math.max(0.0f, color.getYF()));
    final float b = Math.min(1.0f, Math.max(0.0f, color.getZF()));
    return new Color(r, g, b);
  }

  /**
   * Draw an outline for the given box.
   *
   * @param graphics A graphics context
   * @param outline  The outline
   * @param box      The box
   * @param active   {@code true} iff the outline should be considered to belong
   *                 to an <i>active</i> object
   */

  public static void drawOutline(
    final Graphics2D graphics,
    final SyThemeOutlineType outline,
    final SyBoxType<?> box,
    final boolean active)
  {
    NullCheck.notNull(graphics, "graphics");
    NullCheck.notNull(outline, "outline");
    NullCheck.notNull(box, "box");

    final int x_min = box.minimumX();
    final int y_min = box.minimumY();
    final int x_max = box.maximumX();
    final int y_max = box.maximumY();

    if (active) {
      graphics.setPaint(SyAWTDrawing.toColor(outline.colorActive()));
    } else {
      graphics.setPaint(SyAWTDrawing.toColor(outline.colorInactive()));
    }

    if (outline.left()) {
      graphics.drawLine(x_min, y_min + 1, x_min, y_max - 2);
    }
    if (outline.right()) {
      graphics.drawLine(x_max - 1, y_min + 1, x_max - 1, y_max - 2);
    }
    if (outline.top()) {
      graphics.drawLine(x_min + 1, y_min, x_max - 2, y_min);
    }
    if (outline.bottom()) {
      graphics.drawLine(x_min + 1, y_max - 1, x_max - 2, y_max - 1);
    }

    if (outline.corners()) {
      if (outline.left() && outline.top()) {
        graphics.drawLine(x_min, y_min, x_min, y_min);
      }
      if (outline.left() && outline.bottom()) {
        graphics.drawLine(x_min, y_max - 1, x_min, y_max - 1);
      }
      if (outline.right() && outline.top()) {
        graphics.drawLine(x_max - 1, y_min, x_max - 1, y_min);
      }
      if (outline.right() && outline.bottom()) {
        graphics.drawLine(x_max - 1, y_max - 1, x_max - 1, y_max - 1);
      }
    }
  }

  /**
   * Produce an AWT paint for the given fill type.
   *
   * @param in_box The box that will be filled
   * @param fill   The fill type
   *
   * @return An AWT paint
   */

  public static Paint toPaint(
    final SyBoxType<?> in_box,
    final SyThemeFillType fill)
  {
    NullCheck.notNull(in_box, "Box");
    return NullCheck.notNull(fill, "Fill").matchFill(
      in_box,
      SyAWTDrawing::toPaintGradient,
      SyAWTDrawing::toPaintColor);
  }

  private static Paint toPaintColor(
    final SyBoxType<?> in_box,
    final SyThemeColorType color)
  {
    return SyAWTDrawing.toColor(color.color());
  }

  private static Paint toPaintGradient(
    final SyBoxType<?> in_box,
    final SyThemeGradientLinearType gradient)
  {
    final int size = gradient.colors().size();
    final float[] fractions = new float[size];
    final Color[] colors = new Color[size];

    final List<Float> g_distributions = gradient.distributions();
    final List<VectorI3F> g_colors = gradient.colors();
    for (int index = 0; index < size; ++index) {
      fractions[index] = g_distributions.get(index).floatValue();
      colors[index] = SyAWTDrawing.toColor(g_colors.get(index));
    }

    final float x = (float) in_box.minimumX();
    final float y = (float) in_box.minimumY();
    final float w = (float) in_box.width();
    final float h = (float) in_box.height();

    final VectorI2F p0 = gradient.point0();
    final VectorI2F p1 = gradient.point1();

    final float x0 = x + (p0.getXF() * w);
    final float y0 = y + (p0.getYF() * h);
    final float x1 = x + (p1.getXF() * w);
    final float y1 = y + (p1.getYF() * h);

    return new LinearGradientPaint(x0, y0, x1, y1, fractions, colors);
  }
}
