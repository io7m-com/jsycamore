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

/**
 * Enumerations identifying the components that can be attached directly to a
 * window.
 */

public enum SyWindowDecorationComponent
{
  /**
   * The window closed button.
   */
  WINDOW_BUTTON_CLOSE,
  /**
   * The window maximize button.
   */
  WINDOW_BUTTON_MAXIMIZE,
  /**
   * The window menu button.
   */
  WINDOW_BUTTON_MENU,
  /**
   * The window's content area.
   */
  WINDOW_CONTENT_AREA,
  /**
   * The eastern resize box.
   */
  WINDOW_RESIZE_E,
  /**
   * The northern resize box.
   */
  WINDOW_RESIZE_N,
  /**
   * The northeastern resize box.
   */
  WINDOW_RESIZE_NE,
  /**
   * The northwestern resize box.
   */
  WINDOW_RESIZE_NW,
  /**
   * The southern resize box.
   */
  WINDOW_RESIZE_S,
  /**
   * The southeastern resize box.
   */
  WINDOW_RESIZE_SE,
  /**
   * The southwestern resize box.
   */
  WINDOW_RESIZE_SW,
  /**
   * The western resize box.
   */
  WINDOW_RESIZE_W,
  /**
   * The root.
   */
  WINDOW_ROOT,
  /**
   * The title bar.
   */
  WINDOW_TITLE;

  /**
   * Some window components are decorations. Decorations are made invisible when
   * windows are set to undecorated mode.
   *
   * @return {@code true} if the component is a "decoration"
   */

  public boolean isDecoration()
  {
    return switch (this) {
      case WINDOW_BUTTON_CLOSE,
        WINDOW_RESIZE_E,
        WINDOW_TITLE,
        WINDOW_RESIZE_W,
        WINDOW_RESIZE_SW,
        WINDOW_RESIZE_SE,
        WINDOW_RESIZE_S,
        WINDOW_RESIZE_NW,
        WINDOW_RESIZE_NE,
        WINDOW_RESIZE_N,
        WINDOW_BUTTON_MENU,
        WINDOW_BUTTON_MAXIMIZE -> true;
      case WINDOW_CONTENT_AREA,
        WINDOW_ROOT -> false;
    };
  }
}
