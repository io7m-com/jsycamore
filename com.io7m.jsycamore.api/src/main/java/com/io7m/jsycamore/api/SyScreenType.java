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

package com.io7m.jsycamore.api;

import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowType;

import java.util.List;
import java.util.Optional;

/**
 * The type of user interfaces.
 */

public interface SyScreenType
  extends SyScreenMouseEventsType,
  SySizedType<SySpaceViewportType>
{
  /**
   * Create a new window.
   *
   * @param sizeX The window width
   * @param sizeY The window height
   *
   * @return A new window
   */

  SyWindowType windowCreate(
    int sizeX,
    int sizeY);

  /**
   * @return A read-only list of the currently open windows in order from
   * nearest to furthest
   */

  List<SyWindowType> windowsOpenOrderedNow();

  /**
   * @param window A window
   *
   * @return {@code true} iff {@code w} is currently isFocused
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  boolean windowIsFocused(SyWindowType window);

  /**
   * @param window A window
   *
   * @return {@code true} iff {@code w} is currently open
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  boolean windowIsOpen(SyWindowType window);

  /**
   * Open the window. The window, once opened, has focus. If the window is
   * already open, the window gains focus.
   *
   * @param window A window
   */

  void windowOpen(SyWindowType window);

  /**
   * Close the window. If the window is already closed, this has no effect.
   *
   * @param window A window
   */

  void windowClose(SyWindowType window);

  /**
   * Bring {@code w} to the front.
   *
   * @param window A window
   */

  void windowFocus(SyWindowType window);

  /**
   * Maximize the window.
   *
   * @param window A window
   */

  void windowMaximize(SyWindowType window);

  /**
   * @return The current theme used by the screen
   */

  SyThemeType theme();

  /**
   * @return The component that is currently underneath the mouse pointer, if
   * any
   */

  Optional<SyComponentType> componentOver();
}
