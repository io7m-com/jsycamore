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

public enum SyWindowDecorationComponent
{
  WINDOW_BUTTON_CLOSE,
  WINDOW_BUTTON_MAXIMIZE,
  WINDOW_BUTTON_MENU,
  WINDOW_CONTENT_AREA,
  WINDOW_RESIZE_E,
  WINDOW_RESIZE_N,
  WINDOW_RESIZE_NE,
  WINDOW_RESIZE_NW,
  WINDOW_RESIZE_S,
  WINDOW_RESIZE_SE,
  WINDOW_RESIZE_SW,
  WINDOW_RESIZE_W,
  WINDOW_ROOT,
  WINDOW_TITLE;

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
