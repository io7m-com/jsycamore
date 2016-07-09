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
import com.io7m.jsycamore.core.themes.SyThemeType;

import java.util.Optional;

/**
 * The type of windows.
 */

public interface SyWindowType extends SyWindowEventsType,
  SyWindowThemeEventsType,
  SyWindowReadableType
{
  /**
   * Set the size of the window.
   *
   * @param width  The lightWidth
   * @param height The height
   */

  void setBounds(
    int width,
    int height);

  /**
   * Set the position of the window, in viewport-relative coordinates.
   *
   * @param x The {@code x} value
   * @param y The {@code y} value
   */

  void setPosition(
    int x,
    int y);

  /**
   * @return Writable access to the content pane
   */

  @Override
  SyComponentType contentPane();

  /**
   * Set the theme for the window. If an empty value is specified, the window
   * will be reset to whatever is the default theme for the owning {@link
   * SyGUIType}.
   *
   * @param theme The theme, or an empty value to reset to the GUI default
   */

  void setTheme(Optional<SyThemeType> theme);
}
