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

package com.io7m.jsycamore.api.windows;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceType;

import java.util.ArrayDeque;
import java.util.Objects;

/**
 * The default implementation of the viewport accumulator interface.
 */

public final class SyWindowViewportAccumulator
  implements SyWindowViewportAccumulatorType
{
  private final ArrayDeque<PAreaI<SySpaceType>> saved;
  private PAreaI<SySpaceType> current;
  private int baseWidth;
  private int baseHeight;

  private SyWindowViewportAccumulator()
  {
    this.saved = new ArrayDeque<>(16);
    this.current = PAreasI.create(0, 0, 0, 0);
  }

  /**
   * Create a new accumulator.
   *
   * @return A new accumulator
   */

  public static SyWindowViewportAccumulatorType create()
  {
    return new SyWindowViewportAccumulator();
  }

  @Override
  public String toString()
  {
    final var sb =
      new StringBuilder("[SyWindowViewportAccumulator ");
    sb.append(this.current.minimumX());
    sb.append(' ');
    sb.append(this.current.minimumY());
    sb.append(' ');
    sb.append(this.current.maximumX());
    sb.append(' ');
    sb.append(this.current.maximumY());
    sb.append(']');
    return sb.toString();
  }

  @Override
  public void reset(
    final int width,
    final int height)
  {
    Preconditions.checkPreconditionI(
      width, width >= 0, i -> "Width must be >= 0");
    Preconditions.checkPreconditionI(
      height, height >= 0, i -> "Height must be >= 0");

    this.saved.clear();
    this.baseWidth = width;
    this.baseHeight = height;
    this.current = PAreasI.create(0, 0, width, height);
  }

  @Override
  public void accumulate(
    final PAreaI<SySpaceParentRelativeType> box)
  {
    Objects.requireNonNull(box, "Box");

    this.saved.push(this.current);

    final var originalX0 =
      this.current.minimumX();
    final var originalY0 =
      this.current.minimumY();

    final var moveX = box.minimumX();
    final var moveY = box.minimumY();

    final var newX0 =
      Math.addExact(originalX0, moveX);
    final var newY0 =
      Math.addExact(originalY0, moveY);

    final var sizeX = box.sizeX();
    final var sizeY = box.sizeY();
    final var newX1 = Math.addExact(newX0, sizeX);
    final var newY1 = Math.addExact(newY0, sizeY);

    this.current = PAreaI.of(newX0, newX1, newY0, newY1);
  }

  @Override
  public int minimumX()
  {
    return this.current.minimumX();
  }

  @Override
  public int minimumY()
  {
    return this.current.minimumY();
  }

  @Override
  public int maximumX()
  {
    return this.current.maximumX();
  }

  @Override
  public int maximumY()
  {
    return this.current.maximumY();
  }

  @Override
  public void restore()
  {
    if (!this.saved.isEmpty()) {
      this.current = this.saved.pop();
    } else {
      this.current = PAreasI.create(0, 0, this.baseWidth, this.baseHeight);
    }
  }
}
