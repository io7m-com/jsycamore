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

package com.io7m.jsycamore.core.themes;

/**
 * The vertical placement of window titlebars.
 */

public enum SyThemeWindowTitlebarVerticalPlacement
{
  /**
   * The titlebar will be placed at the top of the window, inside the frame.
   */

  PLACEMENT_TOP_INSIDE_FRAME,

  /**
   * The titlebar will be placed at the top of the window, overlapping the
   * frame.
   */

  PLACEMENT_TOP_OVERLAP_FRAME,

  /**
   * The titlebar will be placed at the top of the window, above the frame.
   */

  PLACEMENT_TOP_ABOVE_FRAME
}
