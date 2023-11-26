/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

/**
 * Read-only access to a scroll pane.
 */

public interface SyScrollPaneReadableType
  extends SyComponentReadableType
{
  /**
   * @return A readable reference to the internal content region
   */

  SyComponentReadableType contentArea();

  /**
   * @return A readable reference to the horizontal scroll bar
   */

  SyScrollBarHorizontalReadableType scrollBarHorizontal();

  /**
   * @return A readable reference to the vertical scroll bar
   */

  SyScrollBarVerticalReadableType scrollBarVertical();

  /**
   * @return The default listener executed when clicking on a scrollbar button
   */

  Runnable onScrollClickLeftListener();

  /**
   * @return The default listener executed when clicking on a scrollbar button
   */

  Runnable onScrollClickRightListener();

  /**
   * @return The default listener executed when clicking on a scrollbar button
   */

  Runnable onScrollClickUpListener();

  /**
   * @return The default listener executed when clicking on a scrollbar button
   */

  Runnable onScrollClickDownListener();
}
