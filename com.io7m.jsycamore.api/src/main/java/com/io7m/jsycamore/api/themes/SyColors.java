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

package com.io7m.jsycamore.api.themes;

import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Objects;

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

  public static Vector3D rotate(
    final Vector3D rgb,
    final double degrees)
  {
    Objects.requireNonNull(rgb, "RGB");
    final Vector3D hsv = SyColors.convertRGBtoHSV(rgb);
    final Vector3D hsv_rot = Vector3D.of(
      (hsv.x() + degrees) % 360.0, hsv.y(), hsv.z());
    return SyColors.convertHSVtoRGB(hsv_rot);
  }

  /**
   * Convert the color {@code input}, which is assumed to represent an RGB color, to HSV format,
   * writing the result to {@code output}.
   *
   * @param input The input RGB color
   *
   * @return An HSV color
   */

  public static Vector3D convertRGBtoHSV(
    final Vector3D input)
  {
    Objects.requireNonNull(input, "input");

    final double r = input.x();
    final double g = input.y();
    final double b = input.z();

    double h;
    final double min = Math.min(Math.min(r, g), b);
    final double max = Math.max(Math.max(r, g), b);
    final double delta = max - min;
    final double s;

    if (delta != 0.0) {
      s = delta / max;
    } else {
      s = 0.0;
      h = -1.0;
      return Vector3D.of(h, s, max);
    }

    if (r == max) {
      h = (g - b) / delta;
    } else if (g == max) {
      h = 2.0 + (b - r) / delta;
    } else {
      h = 4.0 + (r - g) / delta;
    }

    h *= 60.0;
    if (h < 0.0) {
      h += 360.0;
    }

    return Vector3D.of(h, s, max);
  }

  /**
   * Convert the color {@code input}, which is assumed to represent an HSV color, to RGB format,
   * writing the result to {@code output}.
   *
   * @param input The input HSV color
   *
   * @return An RGB color
   */

  public static Vector3D convertHSVtoRGB(
    final Vector3D input)
  {
    Objects.requireNonNull(input, "input");

    double h = input.x();
    final double s = input.y();
    final double v = input.z();

    if (s == 0.0) {
      return Vector3D.of(v, v, v);
    }

    h /= 60.0;
    final int i = (int) Math.floor(h);
    final double f = h - (double) i;
    final double p = v * (1.0 - s);
    final double q = v * (1.0 - s * f);
    final double t = v * (1.0 - s * (1.0 - f));

    switch (i) {
      case 0:
        return Vector3D.of(v, t, p);
      case 1:
        return Vector3D.of(q, v, p);
      case 2:
        return Vector3D.of(p, v, t);
      case 3:
        return Vector3D.of(p, q, v);
      case 4:
        return Vector3D.of(t, p, v);
      default:
        return Vector3D.of(v, p, q);
    }
  }
}
