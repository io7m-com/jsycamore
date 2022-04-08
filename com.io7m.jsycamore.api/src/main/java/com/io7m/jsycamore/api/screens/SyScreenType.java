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

package com.io7m.jsycamore.api.screens;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.sized.SySizedType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow.Publisher;

/**
 * The type of user interfaces.
 */

public interface SyScreenType
  extends SyScreenMouseEventsType,
  SyScreenKeyEventsType,
  SySizedType<SySpaceViewportType>,
  AutoCloseable
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

  List<SyWindowType> windowsVisibleOrdered();

  /**
   * @param window A window
   *
   * @return {@code true} iff {@code w} is currently isFocused
   */

  boolean windowIsFocused(SyWindowType window);

  /**
   * @param window A window
   *
   * @return {@code true} iff {@code w} is currently open
   */

  boolean windowIsVisible(SyWindowType window);

  /**
   * Make the window visible. The window, once visible, has focus. If the window
   * is already visible, the window gains focus.
   *
   * @param window A window
   */

  void windowShow(SyWindowType window);

  /**
   * Hide the window. If the window is already invisible, this has no effect.
   *
   * @param window A window
   */

  void windowHide(SyWindowType window);

  /**
   * Close/destroy the window.
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
   * @return A reference to the window used for menus
   */

  SyWindowType windowMenu();

  /**
   * @return The current theme used by the screen
   */

  SyThemeType theme();

  /**
   * @return The component that is currently underneath the mouse pointer, if
   * any
   */

  Optional<SyComponentType> componentOver();

  /**
   * @return The stream of events for the screen
   */

  Publisher<SyEventType> events();

  /**
   * Update the screen, executing a layout pass and updating any animating
   * elements.
   */

  void update();

  /**
   * @return An attribute that exposes the most recently published mouse
   * position
   */

  AttributeReadableType<PVector2I<SySpaceViewportType>> mousePosition();

  /**
   * Open a global menu. If a menu is already open, it is closed as if {@link
   * #menuClose()} had been called, and then the given menu is opened instead.
   * The menu will be positioned onscreen according to the value of {@link
   * SyMenuType#expandPosition()} at the time of this call.
   *
   * @param menu The menu
   */

  void menuOpen(SyMenuType menu);

  /**
   * Close any menu that may be open. If no menu is open, the operation is a
   * no-op.
   */

  void menuClose();

  @Override
  void close()
    throws RuntimeException;
}
