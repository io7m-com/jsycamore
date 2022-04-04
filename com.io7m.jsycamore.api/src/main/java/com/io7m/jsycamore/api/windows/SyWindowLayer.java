/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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
 * A window layer.
 */

public enum SyWindowLayer
{
  /**
   * The window is an "underlay" window and will always be rendered underneath
   * windows with categories {@link #WINDOW_LAYER_NORMAL} and {@link
   * #WINDOW_LAYER_OVERLAY}.
   */

  WINDOW_LAYER_UNDERLAY,

  /**
   * The window is a normal window and will always be rendered underneath
   * windows with layer {@link #WINDOW_LAYER_OVERLAY} and over the top of
   * windows with layer {@link #WINDOW_LAYER_UNDERLAY}.
   */

  WINDOW_LAYER_NORMAL,

  /**
   * The window is an "overlay" window and will always be rendered over the top
   * of windows with categories {@link #WINDOW_LAYER_NORMAL} and {@link
   * #WINDOW_LAYER_UNDERLAY}.
   */

  WINDOW_LAYER_OVERLAY
}
