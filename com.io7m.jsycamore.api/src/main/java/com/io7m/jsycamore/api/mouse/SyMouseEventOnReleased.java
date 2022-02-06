/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Objects;

/**
 * Notify the component that the mouse button {@code button} has just been
 * released.
 *
 * @param mousePosition The position of the mouse cursor now
 * @param button        The mouse button
 * @param actual        The component that is under the mouse
 */

public record SyMouseEventOnReleased(
  PVector2I<SySpaceViewportType> mousePosition,
  SyMouseButton button,
  SyComponentType actual)
  implements SyMouseEventType
{
  /**
   * Notify the component that the mouse button {@code button} has just been
   * released.
   *
   * @param mousePosition The position of the mouse cursor now
   * @param button        The mouse button
   * @param actual        The component that is under the mouse
   */

  public SyMouseEventOnReleased
  {
    Objects.requireNonNull(mousePosition, "mousePosition");
    Objects.requireNonNull(button, "button");
    Objects.requireNonNull(actual, "actual");
  }
}
