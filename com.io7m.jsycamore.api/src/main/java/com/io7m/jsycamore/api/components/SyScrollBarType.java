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

import com.io7m.jsycamore.api.layout.SyLayoutContextType;

import java.util.function.Consumer;

/**
 * Write access to scrollbars.
 */

public interface SyScrollBarType
  extends SyScrollBarReadableType, SyComponentType
{
  /**
   * Allow for hiding the scrollbar if it is disabled. If hiding-if-disabled
   * is enabled, then the scrollbar's size will collapse to zero when
   * {@link #layout(SyLayoutContextType, SyConstraints)} is called and the
   * scrollbar is disabled.
   *
   * @param hideIfDisabled The hide-if-disabled setting
   */

  void setHideIfDisabled(
    SyScrollBarHideIfDisabled hideIfDisabled);

  /**
   * Set the scroll position in the range {@code [0, 1]}.
   *
   * @param position The position
   */

  void setScrollPosition(double position);

  /**
   * Set the scroll position snapping value in the range {@code [0, 1]}. The
   * given fraction is used to determine how many divisions will be used within
   * the scrolling space. For example, a value of {@code 1.0 / 4.0} will yield
   * four possible snapped position values.
   *
   * @param fraction The fraction
   */

  void setScrollPositionSnapping(double fraction);

  /**
   * Scrollbars are typically used to scroll a visible portion of some larger
   * structure. Some implementations might want to scale the scrollbar thumb
   * based on the portion of the visible space that is visible. A value of
   * {@code 0.0} means that an infinitely small piece of the larger structure
   * is visible. A value of {@code 1.0} means that the entirety of the larger
   * structure is visible.
   *
   * @param amount The amount shown
   */

  void setScrollAmountShown(double amount);

  /**
   * Set a listener that will be executed when the thumb is dragged.
   *
   * @param listener The listener
   */

  void setOnThumbDragListener(Consumer<SyScrollBarDrag> listener);

  /**
   * Remove any listeners that are executed when the thumb is dragged.
   *
   * @see #setOnThumbDragListener(Consumer)
   */

  void removeOnThumbDragListener();
}
