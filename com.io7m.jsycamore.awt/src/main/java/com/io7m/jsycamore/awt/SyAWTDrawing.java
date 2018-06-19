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

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.themes.SyThemeColorType;
import com.io7m.jsycamore.api.themes.SyThemeFillType;
import com.io7m.jsycamore.api.themes.SyThemeGradientLinearType;
import com.io7m.jsycamore.api.themes.SyThemeOutline;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.util.List;
import java.util.Objects;

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
    final Vector3D color)
  {
    Objects.requireNonNull(color, "color");

    final double r = Math.min(1.0, Math.max(0.0, color.x()));
    final double g = Math.min(1.0, Math.max(0.0, color.y()));
    final double b = Math.min(1.0, Math.max(0.0, color.z()));
    return new Color((float) r, (float) g, (float) b);
  }

  /**
   * Draw an outline for the given box.
   *
   * @param graphics A graphics context
   * @param outline  The outline
   * @param box      The box
   * @param active   {@code true} iff the outline should be considered to belong to an <i>active</i>
   *                 object
   */

  public static void drawOutline(
    final Graphics2D graphics,
    final SyThemeOutline outline,
    final PAreaI<?> box,
    final boolean active)
  {
    Objects.requireNonNull(graphics, "graphics");
    Objects.requireNonNull(outline, "outline");
    Objects.requireNonNull(box, "box");

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
    final PAreaI<?> in_box,
    final SyThemeFillType fill)
  {
    Objects.requireNonNull(in_box, "Box");
    return Objects.requireNonNull(fill, "Fill").matchFill(
      in_box,
      SyAWTDrawing::toPaintGradient,
      SyAWTDrawing::toPaintColor);
  }

  private static Paint toPaintColor(
    final PAreaI<?> in_box,
    final SyThemeColorType color)
  {
    return SyAWTDrawing.toColor(color.color());
  }

  private static Paint toPaintGradient(
    final PAreaI<?> in_box,
    final SyThemeGradientLinearType gradient)
  {
    final int size = gradient.colors().size();
    final float[] fractions = new float[size];
    final Color[] colors = new Color[size];

    final List<Double> g_distributions = gradient.distributions();
    final List<Vector3D> g_colors = gradient.colors();
    for (int index = 0; index < size; ++index) {
      fractions[index] = g_distributions.get(index).floatValue();
      colors[index] = SyAWTDrawing.toColor(g_colors.get(index));
    }

    final double x = (double) in_box.minimumX();
    final double y = (double) in_box.minimumY();
    final double w = (double) in_box.width();
    final double h = (double) in_box.height();

    final Vector2D p0 = gradient.point0();
    final Vector2D p1 = gradient.point1();

    final double x0 = x + (p0.x() * w);
    final double y0 = y + (p0.y() * h);
    final double x1 = x + (p1.x() * w);
    final double y1 = y + (p1.y() * h);

    return new LinearGradientPaint(
      (float) x0, (float) y0,
      (float) x1, (float) y1,
      fractions, colors);
  }
}
