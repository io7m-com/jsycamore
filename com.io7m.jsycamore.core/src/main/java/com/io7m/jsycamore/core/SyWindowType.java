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
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
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
   * Set whether or not the window should have a close box.
   *
   * @param c {@code true} iff the window should have a close box
   */

  void setCloseable(boolean c);

  /**
   * Set whether or not the window should have a maximize box.
   *
   * @param c {@code true} iff the window should have a maximize box
   */

  void setMaximizable(boolean c);

  /**
   * @return Writable access to the content pane
   */

  @Override
  SyWindowContentPaneType contentPane();

  /**
   * Set the window's position and bounds.
   *
   * @param box The box representing the new position and bounds
   */

  void setBox(SyBoxType<SySpaceViewportType> box);

  /**
   * Set the theme for the window. If an empty value is specified, the window
   * will be reset to whatever is the default theme for the owning {@link
   * SyGUIType}.
   *
   * @param theme The theme, or an empty value to reset to the GUI default
   */

  void setTheme(Optional<SyThemeType> theme);

  /**
   * @return A viewport accumulator for the window
   */

  SyWindowViewportAccumulatorType viewportAccumulator();


}
