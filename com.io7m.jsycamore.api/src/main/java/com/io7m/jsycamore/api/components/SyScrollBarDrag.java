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

import java.util.Objects;

import static java.lang.StrictMath.clamp;

/**
 * A drag operation being performed on a scrollbar.
 *
 * @param dragKind  The kind of operation
 * @param dragStart The scroll position at the start of the operation
 * @param dragNow   The scroll position now
 */

public record SyScrollBarDrag(
  Kind dragKind,
  double dragStart,
  double dragNow)
{
  /**
   * The kind of drag operation.
   */

  public enum Kind
  {
    /**
     * Dragging just started.
     */

    DRAG_STARTED,

    /**
     * Dragging is being continued.
     */

    DRAG_CONTINUED,

    /**
     * Dragging just ended.
     */

    DRAG_ENDED
  }

  /**
   * A drag operation being performed on a scrollbar.
   *
   * @param dragKind  The kind of operation
   * @param dragStart The scroll position at the start of the operation
   * @param dragNow   The scroll position now
   */

  public SyScrollBarDrag
  {
    Objects.requireNonNull(dragKind, "dragKind");

    dragStart = clamp(dragStart, 0.0, 1.0);
    dragNow = clamp(dragNow, 0.0, 1.0);
  }
}
