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

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceType;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxMutable;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import net.jcip.annotations.NotThreadSafe;
import org.valid4j.Assertive;

import java.util.ArrayDeque;

/**
 * The default implementation of the {@link SyWindowViewportAccumulatorType}
 * interface.
 */

@NotThreadSafe
public final class SyWindowViewportAccumulator implements
  SyWindowViewportAccumulatorType
{
  private final ArrayDeque<SyBox<SySpaceType>> saved;
  private final SyBoxMutable<SySpaceType> current;
  private int base_width;
  private int base_height;

  private SyWindowViewportAccumulator()
  {
    this.saved = new ArrayDeque<>(16);
    this.current = SyBoxMutable.create(0, 0, 0, 0);
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

  private static int clamp(
    final int value,
    final int min,
    final int max)
  {
    final int low = Math.max(value, min);
    return Math.min(max, low);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb =
      new StringBuilder("[SyWindowViewportAccumulator ");
    sb.append(this.current.minimumX());
    sb.append(" ");
    sb.append(this.current.minimumY());
    sb.append(" ");
    sb.append(this.current.maximumX());
    sb.append(" ");
    sb.append(this.current.maximumY());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public void reset(
    final int width,
    final int height)
  {
    Assertive.require(width >= 0, "Width must be >= 0");
    Assertive.require(height >= 0, "Height must be >= 0");

    this.saved.clear();
    this.base_width = width;
    this.base_height = height;

    this.current.from(SyBoxes.create(0, 0, width, height));
  }

  @Override
  public void accumulate(
    final SyBoxType<SySpaceParentRelativeType> box)
  {
    NullCheck.notNull(box);

    this.saved.push(SyBox.copyOf(this.current));

    final int original_x0 = this.current.minimumX();
    final int original_y0 = this.current.minimumY();
    final int original_x1 = this.current.maximumX();
    final int original_y1 = this.current.maximumY();

    final int move_x = box.minimumX();
    final int move_y = box.minimumY();

    final int new_x0 =
      Math.addExact(original_x0, move_x);
    final int new_y0 =
      Math.addExact(original_y0, move_y);
    final int mx0 =
      SyWindowViewportAccumulator.clamp(new_x0, original_x0, original_x1);
    final int my0 =
      SyWindowViewportAccumulator.clamp(new_y0, original_y0, original_y1);

    final int size_x = box.width();
    final int size_y = box.height();
    final int new_x1 = Math.addExact(new_x0, size_x);
    final int new_y1 = Math.addExact(new_y0, size_y);
    final int mx1 =
      SyWindowViewportAccumulator.clamp(new_x1, mx0, original_x1);
    final int my1 =
      SyWindowViewportAccumulator.clamp(new_y1, my0, original_y1);

    Assertive.require(mx0 >= original_x0, "mx0 must be >= original_x0");
    Assertive.require(my0 >= original_y0, "my0 must be >= original_y0");
    Assertive.require(mx1 <= original_x1, "mx1 must be >= original_x1");
    Assertive.require(my1 <= original_y1, "my1 must be >= original_y1");
    Assertive.require(mx0 <= mx1, "mx0 must be <= mx1");
    Assertive.require(my0 <= my1, "my0 must be <= my1");

    this.current.setMinimumX(mx0);
    this.current.setMaximumX(mx1);
    this.current.setMinimumY(my0);
    this.current.setMaximumY(my1);
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
      final SyBox<SySpaceType> previous = this.saved.pop();
      this.current.from(previous);
    } else {
      this.current.from(
        SyBoxes.create(0, 0, this.base_width, this.base_height));
    }
  }
}
