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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceType;

public record SyConstraints(
  int sizeMinimumX,
  int sizeMinimumY,
  int sizeMaximumX,
  int sizeMaximumY)
{
  public SyConstraints
  {
    Preconditions.checkPreconditionV(
      0 <= sizeMinimumX,
      "0 <= Minimum X %d",
      sizeMinimumX
    );
    Preconditions.checkPreconditionV(
      0 <= sizeMinimumY,
      "0 <= Minimum Y %d",
      sizeMinimumY
    );
    Preconditions.checkPreconditionV(
      sizeMinimumX <= sizeMaximumX,
      "Minimum X %d <= Maximum X %d",
      sizeMinimumX,
      sizeMaximumX
    );
    Preconditions.checkPreconditionV(
      sizeMinimumY <= sizeMaximumY,
      "Minimum Y %d <= Maximum Y %d",
      sizeMinimumY,
      sizeMaximumY
    );
  }

  private static int clamp(
    final int min,
    final int max,
    final int x)
  {
    return Math.max(min, Math.min(max, x));
  }

  public <T extends SySpaceType> PAreaSizeI<T> sizeMinimum()
  {
    return PAreaSizeI.of(this.sizeMinimumX, this.sizeMinimumY);
  }

  public <T extends SySpaceType> PAreaSizeI<T> sizeMaximum()
  {
    return PAreaSizeI.of(this.sizeMaximumX, this.sizeMaximumY);
  }

  public <T extends SySpaceType> PAreaSizeI<T> sizeWithin(
    final int sizeX,
    final int sizeY)
  {
    return PAreaSizeI.of(
      clamp(this.sizeMinimumX, this.sizeMaximumX, sizeX),
      clamp(this.sizeMinimumY, this.sizeMaximumY, sizeY)
    );
  }

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
}
