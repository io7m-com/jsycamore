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
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Color;
import java.awt.Graphics2D;

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
    final SyBoxType<SySpaceParentRelativeType> box,
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
}
