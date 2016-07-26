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

package com.io7m.jsycamore.core.themes;

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorWritable3FType;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Functions for color conversion.
 */

public final class SyColors
{
  private SyColors()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Rotate the given RGB color {@code degrees} around the HSV spectrum.
   *
   * @param rgb     The RGB color
   * @param degrees The number of degrees
   *
   * @return A rotated color
   */

  public static VectorI3F rotate(
    final VectorReadable3FType rgb,
    final float degrees)
  {
    NullCheck.notNull(rgb, "RGB");
    final VectorI3F hsv = SyColors.convertRGBtoHSVNew(rgb);
    final VectorI3F hsv_rot = new VectorI3F(
      (hsv.getXF() + degrees) % 360.0f,
      hsv.getYF(),
      hsv.getZF());
    return SyColors.convertHSVtoRGBNew(hsv_rot);
  }

  /**
   * Convert the color {@code input}, which is assumed to represent an RGB
   * color, to HSV format.
   *
   * @param input The input RGB color
   *
   * @return The output HSV color
   */

  public static VectorI3F convertRGBtoHSVNew(
    final VectorReadable3FType input)
  {
    NullCheck.notNull(input, "input");
    final VectorM3F v = new VectorM3F();
    SyColors.convertRGBtoHSV(input, v);
    return new VectorI3F(v);
  }

  /**
   * Convert the color {@code input}, which is assumed to represent an HSV
   * color, to RGB format.
   *
   * @param input The input HSV color
   *
   * @return The output RGB color
   */

  public static VectorI3F convertHSVtoRGBNew(
    final VectorReadable3FType input)
  {
    NullCheck.notNull(input, "input");
    final VectorM3F v = new VectorM3F();
    SyColors.convertHSVtoRGB(input, v);
    return new VectorI3F(v);
  }

  /**
   * Convert the color {@code input}, which is assumed to represent an RGB
   * color, to HSV format, writing the result to {@code output}.
   *
   * @param input  The input RGB color
   * @param output The output HSV color
   */

  public static void convertRGBtoHSV(
    final VectorReadable3FType input,
    final VectorWritable3FType output)
  {
    NullCheck.notNull(input, "input");
    NullCheck.notNull(output, "output");

    final float r = input.getXF();
    final float g = input.getYF();
    final float b = input.getZF();

    float h;
    final float min = Math.min(Math.min(r, g), b);
    final float max = Math.max(Math.max(r, g), b);
    final float delta = max - min;
    final float s;

    if (delta != 0.0F) {
      s = delta / max;
    } else {
      s = 0.0F;
      h = -1.0F;
      output.set3F(h, s, max);
      return;
    }

    if (r == max) {
      h = (g - b) / delta;
    } else if (g == max) {
      h = 2.0F + (b - r) / delta;
    } else {
      h = 4.0F + (r - g) / delta;
    }

    h *= 60.0F;
    if (h < 0.0F) {
      h += 360.0F;
    }

    output.set3F(h, s, max);
  }

  /**
   * Convert the color {@code input}, which is assumed to represent an HSV
   * color, to RGB format, writing the result to {@code output}.
   *
   * @param input  The input HSV color
   * @param output The output RGB color
   */

  public static void convertHSVtoRGB(
    final VectorReadable3FType input,
    final VectorWritable3FType output)
  {
    NullCheck.notNull(input, "input");
    NullCheck.notNull(output, "output");

    float h = input.getXF();
    final float s = input.getYF();
    final float v = input.getZF();

    if (s == 0.0F) {
      output.set3F(v, v, v);
      return;
    }

    h /= 60.0F;
    final int i = (int) Math.floor((double) h);
    final float f = h - (float) i;
    final float p = v * (1.0F - s);
    final float q = v * (1.0F - s * f);
    final float t = v * (1.0F - s * (1.0F - f));

    switch (i) {
      case 0:
        output.set3F(v, t, p);
        break;
      case 1:
        output.set3F(q, v, p);
        break;
      case 2:
        output.set3F(p, v, t);
        break;
      case 3:
        output.set3F(p, q, v);
        break;
      case 4:
        output.set3F(t, p, v);
        break;
      default:
        output.set3F(v, p, q);
        break;
    }
  }
}
