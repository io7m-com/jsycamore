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

package com.io7m.jsycamore.core.boxes;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SySpaceType;
import com.io7m.junreachable.UnreachableCodeException;
import org.valid4j.Assertive;

/**
 * Functions to arrange boxes.
 */

public final class SyBoxes
{
  private SyBoxes()
  {
    throw new UnreachableCodeException();
  }

  private static int clamp(
    final int x,
    final int minimum,
    final int maximum)
  {
    Assertive.require(maximum >= minimum, "Maximum >= minimum");
    return Math.max(Math.min(x, maximum), minimum);
  }

  /**
   * Brand a given box as belonging to a different coordinate space. Mixing up
   * coordinate spaces is a common source of difficult-to-locate bugs. Use at
   * your own risk.
   *
   * @param box A box
   * @param <S> The starting coordinate space
   * @param <T> The resulting coordinate space
   *
   * @return {@code box}
   */

  @SuppressWarnings("unchecked")
  public static <S extends SySpaceType, T extends SySpaceType> SyBoxType<T> cast(
    final SyBoxType<S> box)
  {
    NullCheck.notNull(box);
    return (SyBoxType<T>) box;
  }

  /**
   * Move the given box by {@code (x, y)}.
   *
   * @param box The box
   * @param x   The amount to move on the X axis
   * @param y   The amount to move on the Y axis
   * @param <S> The coordinate space of the box
   *
   * @return A moved box
   */

  public static <S extends SySpaceType> SyBoxType<S> moveRelative(
    final SyBoxType<S> box,
    final int x,
    final int y)
  {
    final int x_min = Math.addExact(box.minimumX(), x);
    final int x_max = Math.addExact(box.maximumX(), x);
    final int y_min = Math.addExact(box.minimumY(), y);
    final int y_max = Math.addExact(box.maximumY(), y);
    return SyBox.of(x_min, x_max, y_min, y_max);
  }

  /**
   * Move the given box to {@code (x, y)}.
   *
   * @param box The box
   * @param x   The position to which to move on the X axis
   * @param y   The position to which to move on the Y axis
   * @param <S> The coordinate space of the box
   *
   * @return A moved box
   */

  public static <S extends SySpaceType> SyBoxType<S> moveAbsolute(
    final SyBoxType<S> box,
    final int x,
    final int y)
  {
    return SyBoxes.create(x, y, box.width(), box.height());
  }

  /**
   * Move the given box to {@code (0, 0)}.
   *
   * @param box The box
   * @param <S> The coordinate space of the box
   *
   * @return A moved box
   */

  public static <S extends SySpaceType> SyBoxType<S> moveToOrigin(
    final SyBoxType<S> box)
  {
    return SyBoxes.create(0, 0, box.width(), box.height());
  }

  /**
   * Create a box of width {@code width} and height {@code height}, placing the
   * top left corner at {@code (x, y)}.
   *
   * @param x      The X value of the top left corner
   * @param y      The Y value of the bottom left corner
   * @param width  The width of the box
   * @param height The height of the box
   * @param <S>    The coordinate space of the box
   *
   * @return A box
   */

  public static <S extends SySpaceType> SyBoxType<S> create(
    final int x,
    final int y,
    final int width,
    final int height)
  {
    return SyBox.of(
      x,
      Math.addExact(x, width),
      y,
      Math.addExact(y, height));
  }

  /**
   * Align the box {@code inner} horizontally in the center of {@code outer}.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignHorizontallyCenter(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int outer_width = outer.width();
    final int inner_width = inner.width();
    final int xm0 = Math.addExact(outer.minimumX(), outer_width / 2);
    final int xm1 = Math.subtractExact(xm0, inner_width / 2);
    return SyBoxes.create(xm1, inner.minimumY(), inner_width, inner.height());
  }

  /**
   * Equivalent to calling {@link #alignHorizontallyLeftOffset(SyBoxType,
   * SyBoxType, int)} with a zero offset.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignHorizontallyLeft(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignHorizontallyLeftOffset(outer, inner, 0);
  }

  /**
   * Align the box {@code inner} horizontally against the inside left edge of
   * {@code outer}. The box will be at least {@code offset} units from the left
   * edge.
   *
   * @param outer  The outer box
   * @param inner  The inner box
   * @param offset The offset from the edge
   * @param <S>    The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignHorizontallyLeftOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int x_min = Math.addExact(outer.minimumX(), offset);
    final int x_max = Math.addExact(x_min, inner.width());
    return SyBox.of(x_min, x_max, inner.minimumY(), inner.maximumY());
  }

  /**
   * Equivalent to calling {@link #alignHorizontallyRightOffset(SyBoxType,
   * SyBoxType, int)} with a zero offset.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignHorizontallyRight(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignHorizontallyRightOffset(outer, inner, 0);
  }

  /**
   * Align the box {@code inner} horizontally against the inside right edge of
   * {@code outer}. The box will be at least {@code offset} units from the right
   * edge.
   *
   * @param outer  The outer box
   * @param inner  The inner box
   * @param offset The offset from the edge
   * @param <S>    The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignHorizontallyRightOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int x_max = Math.subtractExact(outer.maximumX(), offset);
    final int x_min = Math.subtractExact(x_max, inner.width());
    return SyBox.of(x_min, x_max, inner.minimumY(), inner.maximumY());
  }

  /**
   * Equivalent to calling {@link #alignVerticallyTopOffset(SyBoxType,
   * SyBoxType, int)} with a zero offset.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignVerticallyTop(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignVerticallyTopOffset(outer, inner, 0);
  }

  /**
   * Align the box {@code inner} vertically against the inside top edge of
   * {@code outer}. The box will be at least {@code offset} units from the top
   * edge.
   *
   * @param outer  The outer box
   * @param inner  The inner box
   * @param offset The offset from the edge
   * @param <S>    The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignVerticallyTopOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int y_min = Math.addExact(outer.minimumY(), offset);
    final int y_max = Math.addExact(y_min, inner.height());
    return SyBox.of(inner.minimumX(), inner.maximumX(), y_min, y_max);
  }

  /**
   * Equivalent to calling {@link #alignVerticallyBottomOffset(SyBoxType,
   * SyBoxType, int)} with a zero offset.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignVerticallyBottom(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignVerticallyBottomOffset(outer, inner, 0);
  }

  /**
   * Align the box {@code inner} vertically against the inside bottom edge of
   * {@code outer}. The box will be at least {@code offset} units from the
   * bottom edge.
   *
   * @param outer  The outer box
   * @param inner  The inner box
   * @param offset The offset from the edge
   * @param <S>    The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignVerticallyBottomOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int y_max = Math.subtractExact(outer.maximumY(), offset);
    final int y_min = Math.subtractExact(y_max, inner.height());
    return SyBox.of(inner.minimumX(), inner.maximumX(), y_min, y_max);
  }

  /**
   * Align the box {@code inner} vertically in the center of {@code outer}.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignVerticallyCenter(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int outer_height = outer.height();
    final int inner_height = inner.height();

    final int ym0 = Math.addExact(outer.minimumY(), outer_height / 2);
    final int ym1 = Math.subtractExact(ym0, inner_height / 2);
    return SyBoxes.create(inner.minimumX(), ym1, inner.width(), inner_height);
  }

  /**
   * Equivalent to calling {@link #alignTopLeftOffset(SyBoxType, SyBoxType, int,
   * int)} with zero offsets.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignTopLeft(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignTopLeftOffset(outer, inner, 0, 0);
  }

  /**
   * Align the box {@code inner} such that the top edge is at least {@code
   * offset_top} from the inside top edge of {@code outer} and the left edge is
   * at least {@code offset_left} from the inside left edge of {@code outer}.
   *
   * @param outer       The outer box
   * @param inner       The inner box
   * @param offset_left The offset from the left edge
   * @param offset_top  The offset from the top edge
   * @param <S>         The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignTopLeftOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset_left,
    final int offset_top)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int x_min = Math.addExact(outer.minimumX(), offset_left);
    final int y_min = Math.addExact(outer.minimumY(), offset_top);
    final int y_max = Math.addExact(y_min, inner.height());
    final int x_max = Math.addExact(x_min, inner.width());
    return SyBox.of(x_min, x_max, y_min, y_max);
  }

  /**
   * Equivalent to calling {@link #alignTopRightOffset(SyBoxType, SyBoxType,
   * int, int)} with zero offsets.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignTopRight(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignTopRightOffset(outer, inner, 0, 0);
  }

  /**
   * Align the box {@code inner} such that the top edge is at least {@code
   * offset_top} from the inside top edge of {@code outer} and the right edge is
   * at least {@code offset_right} from the inside right edge of {@code outer}.
   *
   * @param outer        The outer box
   * @param inner        The inner box
   * @param offset_right The offset from the right edge
   * @param offset_top   The offset from the top edge
   * @param <S>          The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignTopRightOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset_right,
    final int offset_top)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int x_max = Math.subtractExact(outer.maximumX(), offset_right);
    final int y_min = Math.addExact(outer.minimumY(), offset_top);
    final int y_max = Math.addExact(y_min, inner.height());
    final int x_min = Math.subtractExact(x_max, inner.width());
    return SyBox.of(x_min, x_max, y_min, y_max);
  }

  /**
   * Equivalent to calling {@link #alignBottomLeftOffset(SyBoxType, SyBoxType,
   * int, int)} with zero offsets.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignBottomLeft(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignBottomLeftOffset(outer, inner, 0, 0);
  }

  /**
   * Align the box {@code inner} such that the bottom edge is at least {@code
   * offset_bottom} from the inside bottom edge of {@code outer} and the left
   * edge is at least {@code offset_left} from the inside left edge of {@code
   * outer}.
   *
   * @param outer         The outer box
   * @param inner         The inner box
   * @param offset_left   The offset from the left edge
   * @param offset_bottom The offset from the bottom edge
   * @param <S>           The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignBottomLeftOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset_left,
    final int offset_bottom)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int x_min = Math.addExact(outer.minimumX(), offset_left);
    final int y_max = Math.subtractExact(outer.maximumY(), offset_bottom);
    final int y_min = Math.subtractExact(y_max, inner.height());
    final int x_max = Math.addExact(x_min, inner.width());
    return SyBox.of(x_min, x_max, y_min, y_max);
  }

  /**
   * Equivalent to calling {@link #alignBottomRightOffset(SyBoxType, SyBoxType,
   * int, int)} with zero offsets.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignBottomRight(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    return SyBoxes.alignBottomRightOffset(outer, inner, 0, 0);
  }

  /**
   * Align the box {@code inner} such that the bottom edge is at least {@code
   * offset_bottom} from the inside bottom edge of {@code outer} and the right
   * edge is at least {@code offset_right} from the inside right edge of {@code
   * outer}.
   *
   * @param outer         The outer box
   * @param inner         The inner box
   * @param offset_right  The offset from the right edge
   * @param offset_bottom The offset from the bottom edge
   * @param <S>           The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignBottomRightOffset(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner,
    final int offset_right,
    final int offset_bottom)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    final int x_max = Math.subtractExact(outer.maximumX(), offset_right);
    final int y_max = Math.subtractExact(outer.maximumY(), offset_bottom);
    final int y_min = Math.subtractExact(y_max, inner.height());
    final int x_min = Math.subtractExact(x_max, inner.width());
    return SyBox.of(x_min, x_max, y_min, y_max);
  }

  /**
   * Align the box {@code inner} such that the center of the box is equal to the
   * center of {@code outer}.
   *
   * @param outer The outer box
   * @param inner The inner box
   * @param <S>   The coordinate space of the boxes
   *
   * @return An aligned box
   */

  public static <S extends SySpaceType> SyBoxType<S> alignCenter(
    final SyBoxType<S> outer,
    final SyBoxType<S> inner)
  {
    NullCheck.notNull(outer);
    NullCheck.notNull(inner);

    return SyBoxes.alignVerticallyCenter(
      outer, SyBoxes.alignHorizontallyCenter(outer, inner));
  }

  /**
   * Construct a new box that fits inside {@code outer} based on the given
   * offsets from each edge.
   *
   * @param outer         The containing box
   * @param left_offset   The offset from the left edge (must be non-negative)
   * @param right_offset  The offset from the right edge (must be non-negative)
   * @param top_offset    The offset from the top edge (must be non-negative)
   * @param bottom_offset The offset from the bottom edge (must be
   *                      non-negative)
   * @param <S>           The coordinate space of the boxes
   *
   * @return A new box
   */

  public static <S extends SySpaceType> SyBoxType<S> hollowOut(
    final SyBoxType<S> outer,
    final int left_offset,
    final int right_offset,
    final int top_offset,
    final int bottom_offset)
  {
    NullCheck.notNull(outer);
    Assertive.require(left_offset >= 0, "Left offset >= 0");
    Assertive.require(right_offset >= 0, "Right offset >= 0");
    Assertive.require(top_offset >= 0, "Top offset >= 0");
    Assertive.require(bottom_offset >= 0, "Bottom offset >= 0");

    final int x_min =
      SyBoxes.clamp(
        Math.addExact(outer.minimumX(), left_offset),
        outer.minimumX(),
        outer.maximumX());
    final int x_max =
      SyBoxes.clamp(
        Math.subtractExact(outer.maximumX(), right_offset),
        outer.minimumX(),
        outer.maximumX());
    final int y_min =
      SyBoxes.clamp(
        Math.addExact(outer.minimumY(), top_offset),
        outer.minimumY(),
        outer.maximumY());
    final int y_max =
      SyBoxes.clamp(
        Math.subtractExact(outer.maximumY(), bottom_offset),
        outer.minimumY(),
        outer.maximumY());

    final int out_x_max = Math.max(x_min, x_max);
    final int out_y_max = Math.max(y_min, y_max);

    return SyBox.of(x_min, out_x_max, y_min, out_y_max);
  }

  /**
   * Equivalent to calling {@link #hollowOut(SyBoxType, int, int, int, int)}
   * with {@code offset} for all offset parameters.
   *
   * @param outer  The containing box
   * @param offset The offset from each edge (must be non-negative)
   * @param <S>    The coordinate space of the boxes
   *
   * @return A new box
   */

  public static <S extends SySpaceType> SyBoxType<S> hollowOutEvenly(
    final SyBoxType<S> outer,
    final int offset)
  {
    return SyBoxes.hollowOut(outer, offset, offset, offset, offset);
  }

  /**
   * <p>Set the width and height of {@code box} to {@code width} and {@code
   * height}, respectively.</p>
   *
   * <p>The box is resized from its own center.</p>
   *
   * @param box    The box
   * @param width  The new width (must be non-negative)
   * @param height The new height (must be non-negative)
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> setSizeFromCenter(
    final SyBoxType<S> box,
    final int width,
    final int height)
  {
    NullCheck.notNull(box);
    Assertive.require(width >= 0, "Width must be >= 0");
    Assertive.require(height >= 0, "Height must be >= 0");

    return SyBoxes.alignCenter(box, SyBox.of(
      box.minimumX(),
      Math.addExact(box.minimumX(), width),
      box.minimumY(),
      Math.addExact(box.minimumY(), height)));
  }

  /**
   * <p>Set the width and height of {@code box} to {@code width} and {@code
   * height}, respectively.</p>
   *
   * <p>The box is resized by moving its top-left corner.</p>
   *
   * @param box    The box
   * @param width  The new width (must be non-negative)
   * @param height The new height (must be non-negative)
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> setSizeFromTopLeft(
    final SyBoxType<S> box,
    final int width,
    final int height)
  {
    NullCheck.notNull(box);
    Assertive.require(width >= 0, "Width must be >= 0");
    Assertive.require(height >= 0, "Height must be >= 0");

    return SyBoxes.alignBottomRight(box, SyBox.of(
      box.minimumX(),
      Math.addExact(box.minimumX(), width),
      box.minimumY(),
      Math.addExact(box.minimumY(), height)));
  }

  /**
   * <p>Set the width and height of {@code box} to {@code width} and {@code
   * height}, respectively.</p>
   *
   * <p>The box is resized by moving its top-right corner.</p>
   *
   * @param box    The box
   * @param width  The new width (must be non-negative)
   * @param height The new height (must be non-negative)
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> setSizeFromTopRight(
    final SyBoxType<S> box,
    final int width,
    final int height)
  {
    NullCheck.notNull(box);
    Assertive.require(width >= 0, "Width must be >= 0");
    Assertive.require(height >= 0, "Height must be >= 0");

    return SyBoxes.alignBottomLeft(box, SyBox.of(
      box.minimumX(),
      Math.addExact(box.minimumX(), width),
      box.minimumY(),
      Math.addExact(box.minimumY(), height)));
  }

  /**
   * <p>Set the width and height of {@code box} to {@code width} and {@code
   * height}, respectively.</p>
   *
   * <p>The box is resized by moving its bottom-right corner.</p>
   *
   * @param box    The box
   * @param width  The new width (must be non-negative)
   * @param height The new height (must be non-negative)
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> setSizeFromBottomRight(
    final SyBoxType<S> box,
    final int width,
    final int height)
  {
    NullCheck.notNull(box);
    Assertive.require(width >= 0, "Width must be >= 0");
    Assertive.require(height >= 0, "Height must be >= 0");

    return SyBoxes.alignTopLeft(box, SyBox.of(
      box.minimumX(),
      Math.addExact(box.minimumX(), width),
      box.minimumY(),
      Math.addExact(box.minimumY(), height)));
  }

  /**
   * <p>Set the width and height of {@code box} to {@code width} and {@code
   * height}, respectively.</p>
   *
   * <p>The box is resized by moving its bottom-left corner.</p>
   *
   * @param box    The box
   * @param width  The new width (must be non-negative)
   * @param height The new height (must be non-negative)
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> setSizeFromBottomLeft(
    final SyBoxType<S> box,
    final int width,
    final int height)
  {
    NullCheck.notNull(box);
    Assertive.require(width >= 0, "Width must be >= 0");
    Assertive.require(height >= 0, "Height must be >= 0");

    return SyBoxes.alignTopRight(box, SyBox.of(
      box.minimumX(),
      Math.addExact(box.minimumX(), width),
      box.minimumY(),
      Math.addExact(box.minimumY(), height)));
  }

  /**
   * <p>Scale {@code box} by adding {@code x_diff} to the width, and {@code
   * y_diff} to the height. The size of the resulting box is clamped so that its
   * width and height are always non-negative.</p>
   *
   * <p>The box is resized by moving its top-left corner.</p>
   *
   * @param box    The box
   * @param x_diff The X difference
   * @param y_diff The Y difference
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleFromTopLeft(
    final SyBoxType<S> box,
    final int x_diff,
    final int y_diff)
  {
    NullCheck.notNull(box);

    final int width = Math.max(0, Math.addExact(box.width(), x_diff));
    final int height = Math.max(0, Math.addExact(box.height(), y_diff));
    return SyBoxes.setSizeFromTopLeft(box, width, height);
  }

  /**
   * <p>Scale {@code box} by adding {@code x_diff} to the width, and {@code
   * y_diff} to the height. The size of the resulting box is clamped so that its
   * width and height are always non-negative.</p>
   *
   * <p>The box is resized by moving its top-right corner.</p>
   *
   * @param box    The box
   * @param x_diff The X difference
   * @param y_diff The Y difference
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleFromTopRight(
    final SyBoxType<S> box,
    final int x_diff,
    final int y_diff)
  {
    NullCheck.notNull(box);

    final int width = Math.max(0, Math.addExact(box.width(), x_diff));
    final int height = Math.max(0, Math.addExact(box.height(), y_diff));
    return SyBoxes.setSizeFromTopRight(box, width, height);
  }

  /**
   * <p>Scale {@code box} by adding {@code x_diff} to the width, and {@code
   * y_diff} to the height. The size of the resulting box is clamped so that its
   * width and height are always non-negative.</p>
   *
   * <p>The box is resized by moving its bottom-left corner.</p>
   *
   * @param box    The box
   * @param x_diff The X difference
   * @param y_diff The Y difference
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleFromBottomLeft(
    final SyBoxType<S> box,
    final int x_diff,
    final int y_diff)
  {
    NullCheck.notNull(box);

    final int width = Math.max(0, Math.addExact(box.width(), x_diff));
    final int height = Math.max(0, Math.addExact(box.height(), y_diff));
    return SyBoxes.setSizeFromBottomLeft(box, width, height);
  }

  /**
   * <p>Scale {@code box} by adding {@code x_diff} to the width, and {@code
   * y_diff} to the height. The size of the resulting box is clamped so that its
   * width and height are always non-negative.</p>
   *
   * <p>The box is resized by moving its bottom-right corner.</p>
   *
   * @param box    The box
   * @param x_diff The X difference
   * @param y_diff The Y difference
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleFromBottomRight(
    final SyBoxType<S> box,
    final int x_diff,
    final int y_diff)
  {
    NullCheck.notNull(box);

    final int width = Math.max(0, Math.addExact(box.width(), x_diff));
    final int height = Math.max(0, Math.addExact(box.height(), y_diff));
    return SyBoxes.setSizeFromBottomRight(box, width, height);
  }

  /**
   * <p>Scale {@code box} by adding {@code x_diff} to the width, and {@code
   * y_diff} to the height. The size of the resulting box is clamped so that its
   * width and height are always non-negative.</p>
   *
   * <p>The box is resized from its own center.</p>
   *
   * @param box    The box
   * @param x_diff The X difference
   * @param y_diff The Y difference
   * @param <S>    The coordinate space of the box
   *
   * @return A resized box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleFromCenter(
    final SyBoxType<S> box,
    final int x_diff,
    final int y_diff)
  {
    NullCheck.notNull(box);

    final int width = Math.max(0, Math.addExact(box.width(), x_diff));
    final int height = Math.max(0, Math.addExact(box.height(), y_diff));
    return SyBoxes.setSizeFromCenter(box, width, height);
  }

  /**
   * <p>Determine whether or not two boxes overlap.</p>
   *
   * <p>Overlapping is reflexive: {@code overlaps(a, a) == true}.</p>
   *
   * <p>Overlapping is symmetric: {@code overlaps(a, b) == contains(b, a)}.</p>
   *
   * <p>Overlapping is not necessarily transitive.</p>
   *
   * @param a   A box
   * @param b   A box
   * @param <S> The coordinate space of the boxes
   *
   * @return {@code true} iff {@code a} overlaps {@code b}
   */

  public static <S extends SySpaceType> boolean overlaps(
    final SyBoxType<S> a,
    final SyBoxType<S> b)
  {
    NullCheck.notNull(a);
    NullCheck.notNull(b);

    if (a.minimumX() >= b.maximumX() || a.maximumX() < b.minimumX()) {
      return false;
    }
    if (a.minimumY() >= b.maximumY() || a.maximumY() < b.minimumY()) {
      return false;
    }
    return true;
  }

  /**
   * <p>Determine whether or not one box could fit inside another.</p>
   *
   * <p>Fitting is reflexive: {@code couldFitInside(a, a) == true}.</p>
   *
   * <p>Fitting is transitive: {@code couldFitInside(a, b) → couldFitInside(b,
   * c) → couldFitInside(a, c)}.</p>
   *
   * <p>Fitting is not necessarily symmetric.</p>
   *
   * @param a   A box
   * @param b   A box
   * @param <S> The coordinate space of the boxes
   *
   * @return {@code true} iff {@code a} could fit inside {@code b}
   */

  public static <S extends SySpaceType> boolean couldFitInside(
    final SyBoxType<S> a,
    final SyBoxType<S> b)
  {
    NullCheck.notNull(a);
    NullCheck.notNull(b);
    final boolean width_ok = a.width() <= b.width();
    final boolean height_ok = a.height() <= b.height();
    return width_ok && height_ok;
  }

  /**
   * <p>Determine whether or not one box contains another.</p>
   *
   * <p>Containing is reflexive: {@code contains(a, a) == true}.</p>
   *
   * <p>Containing is transitive: {@code contains(a, b) → contains(b, c) →
   * contains(a, c)}.</p>
   *
   * <p>Containing is not necessarily symmetric.</p>
   *
   * @param a   A box
   * @param b   A box
   * @param <S> The coordinate space of the boxes
   *
   * @return {@code true} iff {@code a} contains {@code b}
   */

  public static <S extends SySpaceType> boolean contains(
    final SyBoxType<S> a,
    final SyBoxType<S> b)
  {
    NullCheck.notNull(a);
    NullCheck.notNull(b);

    final boolean contain_x = b.minimumX() >= a.minimumX() && b.maximumX() <= a.maximumX();
    final boolean contain_y = b.minimumY() >= a.minimumY() && b.maximumY() <= a.maximumY();
    return contain_x && contain_y;
  }

  /**
   * <p>Determine whether or not a box contains a given point.</p>
   *
   * @param a   A box
   * @param x   The X coordinate of the point
   * @param y   The Y coordinate of the point
   * @param <S> The coordinate space of the box
   *
   * @return {@code true} iff {@code a} contains {@code (x, y)}
   */

  public static <S extends SySpaceType> boolean containsPoint(
    final SyBoxType<S> a,
    final int x,
    final int y)
  {
    NullCheck.notNull(a);

    final boolean contain_x = x >= a.minimumX() && x < a.maximumX();
    final boolean contain_y = y >= a.minimumY() && y < a.maximumY();
    return contain_x && contain_y;
  }

  /**
   * Attempt to fit {@code fit} between {@code a} and {@code b}, horizontally.
   *
   * @param fit The box to be fitted
   * @param a   A box
   * @param b   A box
   * @param <S> The coordinate space of the boxes
   *
   * @return A fitted box
   */

  public static <S extends SySpaceType> SyBoxType<S> fitBetweenHorizontal(
    final SyBoxType<S> fit,
    final SyBoxType<S> a,
    final SyBoxType<S> b)
  {
    NullCheck.notNull(fit);
    NullCheck.notNull(a);
    NullCheck.notNull(b);

    final int x_min = Math.min(a.maximumX(), b.maximumX());
    final int x_max = Math.max(a.minimumX(), b.minimumX());
    final int out_x_min = Math.min(x_min, x_max);
    final int out_x_max = Math.max(x_min, x_max);
    return SyBox.of(out_x_min, out_x_max, fit.minimumY(), fit.maximumY());
  }

  /**
   * Attempt to fit {@code fit} between {@code a} and {@code b}, vertically.
   *
   * @param fit The box to be fitted
   * @param a   A box
   * @param b   A box
   * @param <S> The coordinate space of the boxes
   *
   * @return A fitted box
   */

  public static <S extends SySpaceType> SyBoxType<S> fitBetweenVertical(
    final SyBoxType<S> fit,
    final SyBoxType<S> a,
    final SyBoxType<S> b)
  {
    NullCheck.notNull(fit);
    NullCheck.notNull(a);
    NullCheck.notNull(b);

    final int y_min = Math.min(a.maximumY(), b.maximumY());
    final int y_max = Math.max(a.minimumY(), b.minimumY());
    final int out_y_min = Math.min(y_min, y_max);
    final int out_y_max = Math.max(y_min, y_max);
    return SyBox.of(fit.minimumX(), fit.maximumX(), out_y_min, out_y_max);
  }

  /**
   * Split {@code box} along a horizontal line placed at {@code height} units
   * from its own top edge.
   *
   * @param box    The box to be split
   * @param height The relative Y coordinate of the splitting edge
   * @param <S>    The coordinate space of the boxes
   *
   * @return A pair of boxes
   */

  public static <S extends SySpaceType> SyHorizontalSplitType<S> splitAlongHorizontal(
    final SyBoxType<S> box,
    final int height)
  {
    NullCheck.notNull(box);

    final int clamped_height = Math.min(box.height(), height);
    final int upper_y_min = box.minimumY();
    final int upper_y_max = Math.addExact(box.minimumY(), clamped_height);
    final int lower_y_min = upper_y_max;
    final int lower_y_max = box.maximumY();

    final SyBoxType<S> lower = SyBox.of(
      box.minimumX(), box.maximumX(), lower_y_min, lower_y_max);
    final SyBoxType<S> upper = SyBox.of(
      box.minimumX(), box.maximumX(), upper_y_min, upper_y_max);

    return SyHorizontalSplit.of(upper, lower);
  }

  /**
   * Split {@code box} along a vertical line placed at {@code width} units from
   * its own left edge.
   *
   * @param box   The box to be split
   * @param width The relative X coordinate of the splitting edge
   * @param <S>   The coordinate space of the boxes
   *
   * @return A pair of boxes
   */

  public static <S extends SySpaceType> SyVerticalSplitType<S> splitAlongVertical(
    final SyBoxType<S> box,
    final int width)
  {
    NullCheck.notNull(box);

    final int clamped_width = Math.min(box.width(), width);
    final int left_x_min = box.minimumX();
    final int left_x_max = Math.addExact(box.minimumX(), clamped_width);
    final int right_x_min = left_x_max;
    final int right_x_max = box.maximumX();

    final SyBoxType<S> left = SyBox.of(
      left_x_min, left_x_max, box.minimumY(), box.maximumY());
    final SyBoxType<S> right = SyBox.of(
      right_x_min, right_x_max, box.minimumY(), box.maximumY());

    return SyVerticalSplit.of(left, right);
  }

  /**
   * @param box The box
   * @param <S> The coordinate space of the box
   *
   * @return A terse string describing the position and size of the box
   */

  public static <S extends SySpaceType> String show(
    final SyBoxType<S> box)
  {
    NullCheck.notNull(box);

    final StringBuilder sb = new StringBuilder(128);
    return SyBoxes.showToBuilder(box, sb);
  }

  /**
   * @param box The box
   * @param sb  A string builder
   * @param <S> The coordinate space of the box
   *
   * @return A terse string describing the position and size of the box
   */

  public static <S extends SySpaceType> String showToBuilder(
    final SyBoxType<S> box,
    final StringBuilder sb)
  {
    NullCheck.notNull(box);
    NullCheck.notNull(sb);

    sb.append(box.width());
    sb.append("x");
    sb.append(box.height());
    sb.append(" ");
    sb.append(box.minimumX());
    sb.append("+");
    sb.append(box.minimumY());
    return sb.toString();
  }
}
