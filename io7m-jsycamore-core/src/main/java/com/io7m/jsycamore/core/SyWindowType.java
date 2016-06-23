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

package com.io7m.jsycamore.core;

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

/**
 * The type of windows.
 */

public interface SyWindowType
{
  /**
   * Retrieve the position of the window. This is the very top left corner
   * of the window's bounding box.
   *
   * @return The position of the window
   */

  PVectorReadable2IType<SySpaceViewportType> position();

  /**
   * @return The tree of components owned by the window
   */

  SyGraph<SyComponentType, SyComponentLink> components();

  /**
   * Retrieve the size of the bounds of the window. This is the absolute
   * maximum bounding box size and is therefore useful for allocating images
   * that will contain the rendered window.
   *
   * @return The bounds of the window
   */

  VectorReadable2IType size();

  /**
   * @return The current theme
   */

  SyThemeType theme();

  /**
   * Set the size of the window.
   * @param width The lightWidth
   * @param height The height
   */

  void setSize(
    int width,
    int height);

  boolean active();

  void setActive(final boolean in_active);

  String text();

  void setText(final String in_text);
}
