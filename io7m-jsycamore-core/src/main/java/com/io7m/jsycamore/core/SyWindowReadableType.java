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

import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorWritable2IType;

import java.util.Optional;

/**
 * A read-only interface to windows.
 */

public interface SyWindowReadableType extends SyGUIElementType
{
  /**
   * @return {@code true} iff the window should have a close box
   */

  boolean isCloseable();

  /**
   * @return {@code true} iff the window should have a maximize box
   */

  boolean isMaximizable();

  /**
   * @return The readable box representing the window's position and bounds
   */

  SyBoxType<SySpaceViewportType> box();

  /**
   * @return Read-only access to the content pane
   */

  SyWindowContentPaneReadableType contentPane();

  /**
   * @return The current theme
   */

  SyThemeType theme();

  /**
   * @return The window frame
   */

  SyWindowFrameType frame();

  /**
   * @return The window title bar
   */

  SyWindowTitleBarType titleBar();

  /**
   * @return {@code true} iff the window currently has focus
   */

  boolean isFocused();

  /**
   * @return {@code true} iff the window is currently open
   */

  boolean isOpen();

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

  Optional<SyComponentType> componentForWindowPosition(
    PVectorReadable2IType<SySpaceWindowRelativeType> w_position);

  /**
   * @param position A viewport-relative position
   *
   * @return The topmost component that contains {@code position}
   */

  Optional<SyComponentType> componentForViewportPosition(
    PVectorReadable2IType<SySpaceViewportType> position);
}
