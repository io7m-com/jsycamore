/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.colors;

import com.io7m.jcolorspace.core.ColorSpaceTagHSVType;
import com.io7m.jcolorspace.core.ColorSpaceTagLinearRGBType;
import com.io7m.jcolorspace.core.HSV;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;

import static com.io7m.jcolorspace.core.HSV.hue;
import static com.io7m.jcolorspace.core.HSV.saturation;
import static com.io7m.jcolorspace.core.HSV.value;

/**
 * Functions for generating and processing colors.
 */

public final class SyColors
{
  private SyColors()
  {

  }

  /**
   * @return An opaque white color
   */

  public static PVector4D<SySpaceRGBAPreType> whiteOpaque()
  {
    return PVector4D.of(1.0, 1.0, 1.0, 1.0);
  }

  /**
   * Produce a 10% lighter version of the given color.
   *
   * @param color The color
   *
   * @return A lighter color
   */

  public static PVector4D<SySpaceRGBAPreType> lighter(
    final PVector4D<SySpaceRGBAPreType> color)
  {
    return PVector4D.of(
      Math.clamp(color.x() + 0.1, 0.0, 1.0),
      Math.clamp(color.y() + 0.1, 0.0, 1.0),
      Math.clamp(color.z() + 0.1, 0.0, 1.0),
      color.w()
    );
  }

  /**
   * Produce a 10% darker version of the given color.
   *
   * @param color The color
   *
   * @return A darker color
   */

  public static PVector4D<SySpaceRGBAPreType> darker(
    final PVector4D<SySpaceRGBAPreType> color)
  {
    return PVector4D.of(
      Math.clamp(color.x() - 0.1, 0.0, 1.0),
      Math.clamp(color.y() - 0.1, 0.0, 1.0),
      Math.clamp(color.z() - 0.1, 0.0, 1.0),
      color.w()
    );
  }

  /**
   * Produce a 10% desaturated version of the given color.
   *
   * @param color The color
   *
   * @return A darker color
   */

  public static PVector4D<SySpaceRGBAPreType> desaturated(
    final PVector4D<SySpaceRGBAPreType> color)
  {
    final var hsv =
      HSV.toHSV((PVector4D<ColorSpaceTagLinearRGBType>) (Object) color);

    final var hsvMod =
      PVector4D.<ColorSpaceTagHSVType>of(
        hue(hsv),
        saturation(hsv) * 0.5,
        value(hsv),
        hsv.w()
      );

    return (PVector4D<SySpaceRGBAPreType>) (Object) HSV.toRGB(hsvMod);
  }

  /**
   * Decode a color from the given RGBA values. Each component must be in the
   * range {@code [0, 255]}.
   *
   * @param r The red component
   * @param g The green component
   * @param b The blue component
   * @param a The alpha component
   *
   * @return A color
   */

  public static PVector4D<SySpaceRGBAPreType> fromHex4(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    return PVector4D.of(
      Math.clamp((double) r / 255.0, 0.0, 1.0),
      Math.clamp((double) g / 255.0, 0.0, 1.0),
      Math.clamp((double) b / 255.0, 0.0, 1.0),
      Math.clamp((double) a / 255.0, 0.0, 1.0)
    );
  }

  /**
   * Invert the values of the R, G, and B components.
   *
   * @param c The color
   *
   * @return A color
   */

  public static PVector4D<SySpaceRGBAPreType> inverted(
    final PVector4D<SySpaceRGBAPreType> c)
  {
    return PVector4D.of(
      Math.clamp(1.0 - c.x(), 0.0, 1.0),
      Math.clamp(1.0 - c.y(), 0.0, 1.0),
      Math.clamp(1.0 - c.z(), 0.0, 1.0),
      Math.clamp(c.w(), 0.0, 1.0)
    );
  }
}
