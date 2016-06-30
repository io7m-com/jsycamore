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
   * @return A read-only list of the currently open windows in order from
   * nearest to furthest
   */

  List<SyWindowType> windowsOpenOrdered();

  /**
   * @param w A window
   *
   * @return {@code true} iff {@code w} is currently focused
   *
   * @throws IllegalArgumentException Iff {@code w} does not belong to this GUI
   */

  boolean windowIsFocused(SyWindowType w);

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

  SyThemeType theme();

  /**
   * @return The text measurement interface used by the GUI
   */

  SyTextMeasurementType textMeasurement();

  /**
   * @return The name of the GUI instance
   */

  String name();
}
