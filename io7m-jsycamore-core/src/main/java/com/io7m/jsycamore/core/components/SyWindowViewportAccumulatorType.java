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

package com.io7m.jsycamore.core.components;

import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.boxes.SyBoxType;

/**
 * <p>The type of accumulators that calculate <i>viewports</i> for sets of
 * components.</p>
 *
 * <p>Whilst traversing a tree of components, a given component can only be
 * rendered within the bounds of its parent component. The positions of
 * components are given relative to their parents. Therefore, it's necessary to
 * calculate the effective viewport for a component based on the values of its
 * ancestors when performing bounds checks.</p>
 *
 * <p>The viewport accumulator can be thought of as an implicit stack of
 * viewports where each new position and size has the effect of trimming off a
 * portion of the current viewport.</p>
 */

public interface SyWindowViewportAccumulatorType
{
  /**
   * <p>Reset the accumulator to the given base size. This is typically the
   * width and height of a containing window. The width and height must be
   * greater than or equal to {@code 0}.</p>
   *
   * <p>This will clear the internal accumulator stack.</p>
   *
   * @param width  The width
   * @param height The height
   */

  void reset(
    int width,
    int height);

  /**
   * <p>Calculate a new viewport based on the given box.</p> <p>The calculated
   * viewport will always be less than or equal to the previous viewport in
   * size.</p>
   *
   * @param box The box
   */

  void accumulate(
    SyBoxType<SySpaceParentRelativeType> box);

  /**
   * @return The leftmost edge of the effective viewport
   */

  int minimumX();

  /**
   * @return The top edge of the effective viewport
   */

  int minimumY();

  /**
   * @return The rightmost edge of the effective viewport
   */

  int maximumX();

  /**
   * @return The bottomost edge of the effective viewport
   */

  int maximumY();

  /**
   * <p>Restore the viewport that was calculated before the most recent call to
   * {@link #accumulate(SyBoxType)}.</p>
   *
   * <p>If more {@code restore} calls have been made than {@code accumulate}
   * calls, the viewport position is reset to {@code (0, 0)} and the size is
   * reset to the most recent values given to {@link #reset(int, int)} (or
   * {@code (0,0)} if none exist).</p>
   */

  void restore();
}
