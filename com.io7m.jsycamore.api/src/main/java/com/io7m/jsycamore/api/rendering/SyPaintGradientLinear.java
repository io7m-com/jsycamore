/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.rendering;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;

import java.util.List;
import java.util.Objects;

/**
 * <p>The specification of a gradient.</p>
 *
 * <p>A gradient is specified as a pair of points inside the unit square with
 * the top left corner at {@code (0,0)} and the bottom right at {@code (1,1)},
 * and a list of colors with associated distribution values.</p>
 *
 * <p>Distribution values denote how colors are distributed across the unit
 * square. There must be the same number of distribution values as colors. With
 * three colors {@code (a,b,c)}, and the distribution values {@code
 * (0.0,0.5,1.0)}, the color {@code a} will be blended into {@code b} and {@code
 * b} into {@code c} with {@code b} positioned exactly in the middle of {@code
 * a} and {@code c}. Typically, the list of distribution values will sum to
 * {@code 1.0}, but this is not a hard requirement.</p>
 *
 * @param point0       The start point
 * @param point1       The end point
 * @param colors       The color points
 * @param distribution The point distribution
 */

public record SyPaintGradientLinear(
  Vector2D point0,
  Vector2D point1,
  List<PVector4D<SySpaceRGBAPreType>> colors,
  List<Double> distribution
) implements SyPaintFillType, SyPaintEdgeType
{
  /**
   * <p>The specification of a gradient.</p>
   *
   * @param point0       The start point
   * @param point1       The end point
   * @param colors       The color points
   * @param distribution The point distribution
   */

  public SyPaintGradientLinear
  {
    Objects.requireNonNull(point0, "point0");
    Objects.requireNonNull(point1, "point1");
    Objects.requireNonNull(colors, "colors");
    Objects.requireNonNull(distribution, "distribution");

    final var cSize = colors.size();

    Preconditions.checkPreconditionV(
      cSize > 0,
      "Number of colors (%d) must be > 0",
      cSize);

    final var dSize = distribution.size();
    Preconditions.checkPreconditionV(
      dSize == cSize,
      "Number of colors (%d) must match the number of distribution values (%d)",
      cSize,
      dSize);

    double dist = 0.0;
    for (final Double distCurrent : distribution) {
      final double current = distCurrent.doubleValue();
      Preconditions.checkPreconditionV(
        current >= dist,
        "Distribution values must be given in increasing order (%f >= %f)",
        Double.valueOf(current),
        Double.valueOf(dist));
      dist = current;
    }
  }
}
