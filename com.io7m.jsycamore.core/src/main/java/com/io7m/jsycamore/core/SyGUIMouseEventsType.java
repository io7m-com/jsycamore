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

import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Optional;

/**
 * The type of mouse event interface exposed by GUIs.
 */

public interface SyGUIMouseEventsType
{
  /**
   * Notify the GUI that the mouse cursor has moved.
   *
   * @param position The current mouse position
   *
   * @return The component under the mouse cursor, if any
   */

  Optional<SyComponentType> onMouseMoved(
    PVector2I<SySpaceViewportType> position);

  /**
   * Notify the GUI that a mouse button has been pressed.
   *
   * @param position The current mouse position
   * @param button   The mouse button
   *
   * @return The component under the mouse cursor, if any
   */

  Optional<SyComponentType> onMouseDown(
    PVector2I<SySpaceViewportType> position,
    SyMouseButton button);

  /**
   * Notify the GUI that a mouse button has been released.
   *
   * @param position The current mouse position
   * @param button   The mouse button
   *
   * @return The component under the mouse cursor, if any
   */

  Optional<SyComponentType> onMouseUp(
    PVector2I<SySpaceViewportType> position,
    SyMouseButton button);
}
