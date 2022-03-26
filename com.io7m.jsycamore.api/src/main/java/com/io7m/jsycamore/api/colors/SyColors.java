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

import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;

/**
 * Functions for generating and processing colors.
 */

public final class SyColors
{
  private SyColors()
  {

  }

  private static double clamp(
    final double x,
    final double min,
    final double max)
  {
    return Math.min(Math.max(x, min), max);
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
      clamp(color.x() + 0.1, 0.0, 1.0),
      clamp(color.y() + 0.1, 0.0, 1.0),
      clamp(color.z() + 0.1, 0.0, 1.0),
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
      clamp(color.x() - 0.1, 0.0, 1.0),
      clamp(color.y() - 0.1, 0.0, 1.0),
      clamp(color.z() - 0.1, 0.0, 1.0),
      color.w()
    );
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
      clamp((double) r / 255.0, 0.0, 1.0),
      clamp((double) g / 255.0, 0.0, 1.0),
      clamp((double) b / 255.0, 0.0, 1.0),
      clamp((double) a / 255.0, 0.0, 1.0)
    );
  }
}
