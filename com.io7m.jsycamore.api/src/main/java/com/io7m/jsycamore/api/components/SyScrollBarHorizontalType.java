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

import java.util.function.Consumer;

/**
 * Write access to scrollbars.
 */

public interface SyScrollBarHorizontalType
  extends SyScrollBarHorizontalReadableType, SyScrollBarType
{
  /**
   * Set a listener that will be executed when the left scroll button is clicked.
   *
   * @param runnable The listener
   */

  void setOnClickLeftListener(Runnable runnable);

  /**
   * Remove any listeners that are executed when the left button is clicked.
   *
   * @see #setOnClickLeftListener(Runnable)
   */

  void removeOnClickLeftListener();

  /**
   * Set a listener that will be executed when the right scroll button is clicked.
   *
   * @param runnable The listener
   */

  void setOnClickRightListener(Runnable runnable);

  /**
   * Remove any listeners that are executed when the right button is clicked.
   *
   * @see #setOnClickRightListener(Runnable)
   */

  void removeOnClickRightListener();

  /**
   * Set a listener that will be executed when the left scroll button is clicked.
   *
   * @param listener The listener
   */

  void setOnThumbDragListener(Consumer<SyScrollBarDrag> listener);

  /**
   * Remove any listeners that are executed when the left button is clicked.
   *
   * @see #setOnThumbDragListener(Consumer)
   */

  void removeOnThumbDragListener();
}
