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

import com.io7m.jattribute.core.AttributeReadableType;

/**
 * Read-only access to scrollbars.
 */

public interface SyScrollBarReadableType
  extends SyComponentReadableType
{
  /**
   * @return The scrollbar thumb
   */

  SyComponentReadableType thumb();

  /**
   * @return The scrollbar track
   */

  SyComponentReadableType track();

  /**
   * @return The scroll amount shown in the range {@code [0, 1]}
   */

  double scrollAmountShown();

  /**
   * @return The scroll position in the range {@code [0, 1]}
   */

  double scrollPosition();

  /**
   * @return The scroll position snapping value in the range {@code [0, 1]}
   */

  double scrollPositionSnapping();

  /**
   * Based on the current snapping setting and scrollbar size, determine a
   * reasonable value to use to increment or decrement the current scroll
   * position. This is used to implement up/down/left/right arrow buttons
   * on scrollbars.
   *
   * @return The size of a single scroll increment in the range {@code [0, 1]}
   */

  double scrollIncrementSize();
}
