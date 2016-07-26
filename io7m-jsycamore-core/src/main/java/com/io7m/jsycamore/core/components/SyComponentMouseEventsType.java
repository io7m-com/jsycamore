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

package com.io7m.jsycamore.core.components;

import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

/**
 * The mouse event interface exposed by components.
 */

public interface SyComponentMouseEventsType
{
  /**
   * Notify the component that the mouse button {@code button} is currently
   * being held.
   *
   * @param mouse_position_first The position of the mouse cursor when the
   *                             button was first pressed
   * @param mouse_position_now   The position of the mouse cursor now
   * @param button               The mouse button
   * @param actual               The component that was initially under the
   *                             mouse
   */

  void onMouseHeld(
    PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    SyMouseButton button,
    SyComponentType actual);

  /**
   * Notify the component that the mouse button {@code button} has just been
   * pressed.
   *
   * @param mouse_position The position of the mouse cursor now
   * @param button         The mouse button
   * @param actual         The component that is under the mouse
   */

  void onMousePressed(
    PVectorReadable2IType<SySpaceViewportType> mouse_position,
    SyMouseButton button,
    SyComponentType actual);

  /**
   * Notify the component that the mouse button {@code button} has just been
   * released.
   *
   * @param mouse_position The position of the mouse cursor now
   * @param button         The mouse button
   * @param actual         The component that is under the mouse
   */

  void onMouseReleased(
    PVectorReadable2IType<SySpaceViewportType> mouse_position,
    SyMouseButton button,
    SyComponentType actual);

  /**
   * Notify the component that the mouse cursor is no longer over this
   * component.
   */

  void onMouseNoLongerOver();

  /**
   * Notify the component that the mouse cursor has just moved over this
   * component.
   *
   * @param mouse_position The position of the mouse cursor now
   * @param actual         The component that is under the mouse
   */

  void onMouseOver(
    PVectorReadable2IType<SySpaceViewportType> mouse_position,
    SyComponentType actual);
}
