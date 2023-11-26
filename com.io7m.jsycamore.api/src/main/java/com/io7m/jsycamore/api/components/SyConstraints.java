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

package com.io7m.jsycamore.api.components;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceType;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;

/**
 * A set of size constraints.
 *
 * @param sizeMinimumX The minimum allowed size on the X axis
 * @param sizeMaximumX The maximum allowed size on the X axis
 * @param sizeMinimumY The minimum allowed size on the Y axis
 * @param sizeMaximumY The maximum allowed size on the Y axis
 */

public record SyConstraints(
  int sizeMinimumX,
  int sizeMinimumY,
  int sizeMaximumX,
  int sizeMaximumY)
{
  private static final SyConstraints ZERO =
    new SyConstraints(0, 0, 0, 0);

  /**
   * A set of size constraints.
   *
   * @param sizeMinimumX The minimum allowed size on the X axis
   * @param sizeMaximumX The maximum allowed size on the X axis
   * @param sizeMinimumY The minimum allowed size on the Y axis
   * @param sizeMaximumY The maximum allowed size on the Y axis
   */

  public SyConstraints(
    final int sizeMinimumX,
    final int sizeMinimumY,
    final int sizeMaximumX,
    final int sizeMaximumY)
  {
    this.sizeMinimumX =
      Math.clamp(sizeMinimumX, 0, MAX_VALUE);
    this.sizeMaximumX =
      Math.clamp(sizeMaximumX, this.sizeMinimumX, MAX_VALUE);
    this.sizeMinimumY =
      Math.clamp(sizeMinimumY, 0, MAX_VALUE);
    this.sizeMaximumY =
      Math.clamp(sizeMaximumY, this.sizeMinimumY, MAX_VALUE);
  }

  /**
   * @return Constraints that force a zero size
   */

  public static SyConstraints zero()
  {
    return ZERO;
  }

  /**
   * @param <T> The coordinate space type
   *
   * @return The minimum area size that can fit in these constraints
   */

  public <T extends SySpaceType> PAreaSizeI<T> sizeMinimum()
  {
    return PAreaSizeI.of(this.sizeMinimumX, this.sizeMinimumY);
  }

  /**
   * @param <T> The coordinate space type
   *
   * @return The maximum area size that can fit in these constraints
   */

  public <T extends SySpaceType> PAreaSizeI<T> sizeMaximum()
  {
    return PAreaSizeI.of(this.sizeMaximumX, this.sizeMaximumY);
  }

  /**
   * Produce an area size based on the given size values, clamped such that they
   * will fit within these constraints.
   *
   * @param <T>   The coordinate space type
   * @param sizeX The size on the X axis
   * @param sizeY The size on the Y axis
   *
   * @return A clamped area size
   */

  public <T extends SySpaceType> PAreaSizeI<T> sizeWithin(
    final int sizeX,
    final int sizeY)
  {
    return PAreaSizeI.of(
      Math.clamp(sizeX, this.sizeMinimumX, this.sizeMaximumX),
      Math.clamp(sizeY, this.sizeMinimumY, this.sizeMaximumY)
    );
  }

  /**
   * Produce an area size based on the given size values, clamped such that they
   * will fit within these constraints, ignoring the minimum constraint but
   * obeying the maximum constraint.
   *
   * @param <T>   The coordinate space type
   * @param sizeX The size on the X axis
   * @param sizeY The size on the Y axis
   *
   * @return A clamped area size
   */

  public <T extends SySpaceType> PAreaSizeI<T> sizeNotExceeding(
    final int sizeX,
    final int sizeY)
  {
    return PAreaSizeI.of(
      min(this.sizeMaximumX, sizeX),
      min(this.sizeMaximumY, sizeY)
    );
  }

  /**
   * @param size The area size
   *
   * @return {@code true} if the given size fits within these constraints
   */

  public boolean isSatisfiedBy(
    final PAreaSizeI<?> size)
  {
    final var xOk =
      size.sizeX() >= this.sizeMinimumX
      && size.sizeX() <= this.sizeMaximumX;
    final var yOk =
      size.sizeY() >= this.sizeMinimumY
      && size.sizeY() <= this.sizeMaximumY;
    return xOk && yOk;
  }

  /**
   * @return These constraints without the minimum sizes
   */

  public SyConstraints withoutMinimum()
  {
    return new SyConstraints(
      0,
      0,
      this.sizeMaximumX,
      this.sizeMaximumY
    );
  }

  /**
   * Derive a new set of constraints that obey the current constraints whilst
   * attempting to constrain to the given size.
   *
   * @param size The size
   *
   * @return A new set of constraints
   */

  public SyConstraints deriveLimitedBy(
    final PAreaSizeI<?> size)
  {
    return new SyConstraints(
      this.sizeMinimumX(),
      this.sizeMinimumY(),
      min(this.sizeMaximumX(), size.sizeX()),
      min(this.sizeMaximumY(), size.sizeY())
    );
  }

  /**
   * Derive a new set of constraints where the new maximum width has the given
   * size subtracted from it.
   *
   * @param size The size to subtract
   *
   * @return A new set of constraints
   */

  public SyConstraints deriveSubtractMaximumWidth(
    final int size)
  {
    return new SyConstraints(
      this.sizeMinimumX,
      this.sizeMinimumY,
      this.sizeMaximumX - size,
      this.sizeMaximumY
    );
  }

  /**
   * Derive a new set of constraints where the new maximum height has the given
   * size subtracted from it.
   *
   * @param size The size to subtract
   *
   * @return A new set of constraints
   */

  public SyConstraints deriveSubtractMaximumHeight(
    final int size)
  {
    return new SyConstraints(
      this.sizeMinimumX,
      this.sizeMinimumY,
      this.sizeMaximumX,
      this.sizeMaximumY - size
    );
  }

  /**
   * Derive a new set of constraints where the new maximum width and height
   * values have the given sizes subtracted from them.
   *
   * @param sizeX The X size to subtract
   * @param sizeY The Y size to subtract
   *
   * @return A new set of constraints
   */

  public SyConstraints deriveSubtractMaximumSizes(
    final int sizeX,
    final int sizeY)
  {
    return new SyConstraints(
      this.sizeMinimumX,
      this.sizeMinimumY,
      this.sizeMaximumX - sizeX,
      this.sizeMaximumY - sizeY
    );
  }
}
