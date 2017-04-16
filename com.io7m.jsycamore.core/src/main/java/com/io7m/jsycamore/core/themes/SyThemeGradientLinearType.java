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
import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.immutables.value.Value;
import org.valid4j.Assertive;

import java.util.List;
import java.util.function.BiFunction;

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
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeGradientLinearType extends SyThemeFillType
{
  /**
   * Check the preconditions for the parameters.
   */

  @Value.Check
  default void checkPreconditions()
  {
    final List<Double> dists = this.distributions();
    final List<Vector3D> cs = this.colors();
    final int c_size = cs.size();
    final int d_size = dists.size();
    final Integer c_size_b = Integer.valueOf(c_size);
    final Integer d_size_b = Integer.valueOf(d_size);

    Assertive.require(
      c_size > 0, "Number of colors (%d) must be >= 0", c_size_b);

    Assertive.require(
      d_size == c_size,
      "Number of colors (%d) must match the number of distribution values (%d)",
      c_size_b,
      d_size_b);

    double dist = 0.0;
    for (int index = 0; index < d_size; ++index) {
      final double current = dists.get(index).doubleValue();
      Assertive.require(
        current >= dist,
        "Distribution values must be given in increasing order (%f >= %f)",
        Double.valueOf(current),
        Double.valueOf(dist));
      dist = current;
    }
  }

  /**
   * @return A list of color distribution values
   */

  @Value.Parameter(order = 0)
  List<Double> distributions();

  /**
   * @return A list of colors
   */

  @Value.Parameter(order = 1)
  List<Vector3D> colors();

  /**
   * @return The top left corner of the gradient box
   */

  @Value.Parameter(order = 2)
  Vector2D point0();

  /**
   * @return The bottom right corner of the gradient box
   */

  @Value.Parameter(order = 3)
  Vector2D point1();

  @Override
  default <A, B> B matchFill(
    final A context,
    final BiFunction<A, SyThemeGradientLinearType, B> on_gradient_linear,
    final BiFunction<A, SyThemeColorType, B> on_color)
  {
    return NullCheck.notNull(on_gradient_linear, "Gradient function")
      .apply(context, this);
  }
}
