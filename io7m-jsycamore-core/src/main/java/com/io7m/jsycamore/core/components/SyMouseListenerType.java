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
 * Interface describing an object that can receive mouse events.
 *
 * @param <T> The type of object that received the initial event (not
 *            necessarily the same type of object as the one receiving the event
 *            now).
 */

public interface SyMouseListenerType<T>
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
   *
   * @return {@code true} if this component has consumed the message.
   */

  boolean mouseHeld(
    PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    SyMouseButton button,
    T actual);

  /**
   * Notify the component that the mouse button {@code button} has just been
   * pressed.
   *
   * @param mouse_position The position of the mouse cursor now
   * @param button         The mouse button
   * @param actual         The component that is under the mouse
   *
   * @return {@code true} if this component has consumed the message.
   */

  boolean mousePressed(
    PVectorReadable2IType<SySpaceViewportType> mouse_position,
    SyMouseButton button,
    T actual);

  /**
   * Notify the component that the mouse button {@code button} has just been
   * released.
   *
   * @param mouse_position The position of the mouse cursor now
   * @param button         The mouse button
   * @param actual         The component that is under the mouse
   *
   * @return {@code true} if this component has consumed the message.
   */

  boolean mouseReleased(
    PVectorReadable2IType<SySpaceViewportType> mouse_position,
    SyMouseButton button,
    T actual);

  /**
   * Notify the component that the mouse cursor is no longer over this
   * component.
   *
   * @return {@code true} if this component has consumed the message.
   */

  boolean mouseNoLongerOver();

  /**
   * Notify the component that the mouse cursor has just moved over this
   * component.
   *
   * @param mouse_position The position of the mouse cursor now
   * @param actual         The component that is under the mouse
   *
   * @return {@code true} if this component has consumed the message.
   */

  boolean mouseOver(
    PVectorReadable2IType<SySpaceViewportType> mouse_position,
    T actual);
}
