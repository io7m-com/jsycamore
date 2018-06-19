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

import com.io7m.jsycamore.api.text.SyTextMeasurementType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.windows.SyWindowType;

import java.util.List;

/**
 * The type of GUIs.
 */

public interface SyGUIType extends SyGUIMouseEventsType
{
  /**
   * Create a new window.
   *
   * @param width  The window width
   * @param height The window height
   * @param title  The window title
   *
   * @return A new window
   */

  SyWindowType windowCreate(
    int width,
    int height,
    String title);

  /**
   * @return A read-only list of the currently open windows in order from nearest to furthest
   */

  List<SyWindowType> windowsOpenOrdered();

  /**
   * @param w A window
   *
   * @return {@code true} iff {@code w} is currently isFocused
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  boolean windowIsFocused(SyWindowType w);

  /**
   * @param w A window
   *
   * @return {@code true} iff {@code w} is currently open
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  boolean windowIsOpen(SyWindowType w);

  /**
   * Open the window. The window, once opened, has focus. If the window is already open, the window
   * gains focus.
   *
   * @param w A window
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  void windowOpen(SyWindowType w);

  /**
   * Close the window. If the window is already closed, this has no effect.
   *
   * @param w A window
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  void windowClose(SyWindowType w);

  /**
   * Bring {@code w} to the front.
   *
   * @param w A window
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  void windowFocus(SyWindowType w);

  /**
   * @return The default theme for the GUI
   */

  SyTheme theme();

  /**
   * @return The text measurement interface used by the GUI
   */

  SyTextMeasurementType textMeasurement();

  /**
   * @return The name of the GUI instance
   */

  String name();

  /**
   * Set the theme for the GUI. This will reload the theme for all windows that are using the GUI
   * theme.
   *
   * @param theme The theme
   */

  void setTheme(SyTheme theme);
}
