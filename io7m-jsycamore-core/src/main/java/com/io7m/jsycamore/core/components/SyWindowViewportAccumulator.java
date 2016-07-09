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
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
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
  private final ArrayDeque<Item> saved;
  private final Item current;
  private int base_width;
  private int base_height;

  private SyWindowViewportAccumulator()
  {
    this.saved = new ArrayDeque<>(16);
    this.current = new Item(0, 0, 0, 0);
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
    sb.append(this.current.x_min);
    sb.append(" ");
    sb.append(this.current.y_min);
    sb.append(" ");
    sb.append(this.current.x_max);
    sb.append(" ");
    sb.append(this.current.y_max);
    sb.append("]");
    return sb.toString();
  }

  @Override
  public void reset(
    final int width,
    final int height)
  {
    Assertive.require(width >= 0);
    Assertive.require(height >= 0);

    this.saved.clear();
    this.base_width = width;
    this.base_height = height;
    this.current.x_max = width;
    this.current.y_max = height;
  }

  @Override
  public void accumulate(
    final PVectorReadable2IType<SySpaceParentRelativeType> in_position,
    final VectorReadable2IType in_size)
  {
    NullCheck.notNull(in_position);
    NullCheck.notNull(in_size);

    this.saved.push(new Item(
      this.current.x_min,
      this.current.x_max,
      this.current.y_min,
      this.current.y_max));

    final int original_x0 = this.current.x_min;
    final int original_y0 = this.current.y_min;
    final int original_x1 = this.current.x_max;
    final int original_y1 = this.current.y_max;

    final int move_x = in_position.getXI();
    final int move_y = in_position.getYI();

    final int new_x0 =
      Math.addExact(original_x0, move_x);
    final int new_y0 =
      Math.addExact(original_y0, move_y);
    final int mx0 =
      SyWindowViewportAccumulator.clamp(new_x0, original_x0, original_x1);
    final int my0 =
      SyWindowViewportAccumulator.clamp(new_y0, original_y0, original_y1);

    final int size_x = in_size.getXI();
    final int size_y = in_size.getYI();
    final int new_x1 = Math.addExact(new_x0, size_x);
    final int new_y1 = Math.addExact(new_y0, size_y);
    final int mx1 =
      SyWindowViewportAccumulator.clamp(new_x1, mx0, original_x1);
    final int my1 =
      SyWindowViewportAccumulator.clamp(new_y1, my0, original_y1);

    Assertive.require(mx0 >= original_x0);
    Assertive.require(my0 >= original_y0);
    Assertive.require(mx1 <= original_x1);
    Assertive.require(my1 <= original_y1);
    Assertive.require(mx0 <= mx1);
    Assertive.require(my0 <= my1);

    this.current.x_min = mx0;
    this.current.y_min = my0;
    this.current.x_max = mx1;
    this.current.y_max = my1;
  }

  @Override
  public int minimumX()
  {
    return this.current.x_min;
  }

  @Override
  public int minimumY()
  {
    return this.current.y_min;
  }

  @Override
  public int maximumX()
  {
    return this.current.x_max;
  }

  @Override
  public int maximumY()
  {
    return this.current.y_max;
  }

  @Override
  public void restore()
  {
    if (!this.saved.isEmpty()) {
      final Item previous = this.saved.pop();
      this.current.x_max = previous.x_max;
      this.current.x_min = previous.x_min;
      this.current.y_min = previous.y_min;
      this.current.y_max = previous.y_max;
    } else {
      this.current.x_max = this.base_width;
      this.current.x_min = 0;
      this.current.y_min = 0;
      this.current.y_max = this.base_height;
    }
  }

  private static final class Item
  {
    private int x_min;
    private int x_max;
    private int y_min;
    private int y_max;

    Item(
      final int in_x_min,
      final int in_x_max,
      final int in_y_min,
      final int in_y_max)
    {
      this.x_min = in_x_min;
      this.x_max = in_x_max;
      this.y_min = in_y_min;
      this.y_max = in_y_max;
    }
  }
}
