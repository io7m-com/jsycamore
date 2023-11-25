/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.api.layout;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import static java.lang.StrictMath.round;

/**
 * Snapping functions.
 */

public final class SySnapping
{
  private SySnapping()
  {

  }

  /**
   * Snap a real number {@code x} to a snapping value. The value of {@code f}
   * is a fractional value in the range {@code [0, 1]}.
   *
   * @param x The number to be snapped
   * @param f The snapping value
   *
   * @return The snapped value
   */

  public static double snapDouble(
    final double x,
    final double f)
  {
    if (f == 0.0) {
      return x;
    }

    final var p = 1.0 / f;
    var r = x * p;
    r = (double) round(r);
    return r / p;
  }

  /**
   * Snap the given vector. Each element of the vector will be snapped to the
   * nearest multiple of {@code snapping}.
   *
   * @param v        The vector
   * @param snapping The snapping value
   * @param <T>      The vector space
   *
   * @return The vector
   */

  public static <T extends SySpaceType> PVector2I<T> snapVector(
    final PVector2I<T> v,
    final int snapping)
  {
    if (snapping > 1) {
      return PVector2I.of(
        (v.x() / snapping) * snapping,
        (v.y() / snapping) * snapping
      );
    }
    return v;
  }

  /**
   * Snap the given size. Each element of the size will be snapped to the
   * nearest multiple of {@code snapping}.
   *
   * @param v        The vector
   * @param snapping The snapping value
   * @param <T>      The vector space
   *
   * @return The vector
   */

  public static <T extends SySpaceType> PAreaSizeI<T> snapSize(
    final PAreaSizeI<T> v,
    final int snapping)
  {
    if (snapping > 1) {
      return PAreaSizeI.of(
        (v.sizeX() / snapping) * snapping,
        (v.sizeY() / snapping) * snapping
      );
    }
    return v;
  }
}
