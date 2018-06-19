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

package com.io7m.jsycamore.api;

/**
 * An enumeration of the mouse buttons used by the package.
 */

public enum SyMouseButton
{
  /**
   * The left mouse button.
   */

  MOUSE_BUTTON_LEFT(0),

  /**
   * The middle mouse button.
   */

  MOUSE_BUTTON_MIDDLE(1),

  /**
   * The right mouse button.
   */

  MOUSE_BUTTON_RIGHT(2);

  private final int index;

  SyMouseButton(final int in_index)
  {
    this.index = in_index;
  }

  /**
   * Retrieve a button for the given index. The index must be in the range {@code [0, 2]}.
   *
   * @param index The button index
   *
   * @return The mouse button associated with {@code index}
   */

  public static SyMouseButton ofIndex(final int index)
  {
    switch (index) {
      case 0:
        return SyMouseButton.MOUSE_BUTTON_LEFT;
      case 1:
        return SyMouseButton.MOUSE_BUTTON_MIDDLE;
      case 2:
        return SyMouseButton.MOUSE_BUTTON_RIGHT;
    }

    final StringBuilder sb = new StringBuilder(128);
    sb.append("Invalid mouse button index.");
    sb.append(System.lineSeparator());
    sb.append("  Expected: [0, 2]");
    sb.append(System.lineSeparator());
    sb.append("  Received: ");
    sb.append(index);
    sb.append(System.lineSeparator());
    throw new IllegalArgumentException(sb.toString());
  }

  /**
   * @return The integer index of the mouse button
   */

  public int index()
  {
    return this.index;
  }
}
