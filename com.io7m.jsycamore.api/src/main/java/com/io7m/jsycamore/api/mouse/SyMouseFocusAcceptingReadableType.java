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


package com.io7m.jsycamore.api.mouse;

import com.io7m.jsycamore.api.components.SyComponentQuery;

/**
 * The type of components that can accept mouse focus/queries.
 */

public interface SyMouseFocusAcceptingReadableType
{
  /**
   * A component that is <i>mouse query accepting</i> can be returned from a
   * component query of type {@link SyComponentQuery#FIND_FOR_MOUSE_CURSOR}.
   * Intuitively, only components that are <i>mouse query accepting</i> will be
   * considered when tracking which component is under the mouse cursor.
   *
   * @return {@code true} if this component accepts mouse queries
   */

  boolean isMouseQueryAccepting();
}
