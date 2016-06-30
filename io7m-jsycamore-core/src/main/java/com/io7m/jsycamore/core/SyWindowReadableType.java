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

import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorWritable2IType;

import java.util.Optional;

/**
 * A read-only interface to windows.
 */

public interface SyWindowReadableType extends SyGUIElementType
{
  /**
   * Retrieve the positionParentRelative of the window. This is the very top
   * left corner of the window's bounding box.
   *
   * @return The positionParentRelative of the window
   */

  PVectorReadable2IType<SySpaceViewportType> position();

  /**
   * @return The tree of components owned by the window
   */

  SyUnmodifiableGraph<SyComponentReadableType, SyComponentLinkReadableType> components();

  /**
   * Retrieve the size of the bounds of the window. This is the absolute maximum
   * bounding box size and is therefore useful for allocating images that will
   * contain the rendered window.
   *
   * @return The bounds of the window
   */

  VectorReadable2IType bounds();

  /**
   * @return The current theme
   */

  SyThemeType theme();

  /**
   * @return The window-relative position of the frame
   */

  PVectorReadable2IType<SySpaceWindowRelativeType> framePosition();

  /**
   * @return The size of the frame
   */

  PVectorReadable2IType<SySpaceWindowRelativeType> frameBounds();

  /**
   * @return The window titlebar
   */

  SyWindowTitlebarType titlebar();

  /**
   * @return {@code true} iff the window currently has focus
   */

  boolean focused();

  /**
   * @param position A viewport-relative position
   *
   * @return {@code true} iff the window contains {@code position}
   */

  boolean containsViewportRelative(
    PVectorReadable2IType<SySpaceViewportType> position);

  /**
   * Transform a viewport-relative position to window-relative form.
   *
   * @param v_position A viewport-relative position
   * @param w_position The output vector
   */

  void transformViewportRelative(
    PVectorReadable2IType<SySpaceViewportType> v_position,
    PVectorWritable2IType<SySpaceWindowRelativeType> w_position);

  /**
   * @param w_position A window-relative position
   *
   * @return The topmost component that contains {@code w_position}
   */

  Optional<SyComponentType> componentForPosition(
    PVectorReadable2IType<SySpaceWindowRelativeType> w_position);
}
