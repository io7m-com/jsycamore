/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.api.windows;

import com.io7m.jsycamore.api.services.SyServiceType;

import java.util.List;

/**
 * The window service.
 */

public interface SyWindowServiceType extends SyServiceType
{
  /**
   * Create a new window.
   *
   * @param sizeX The window width
   * @param sizeY The window height
   *
   * @return A new window
   */

  default SyWindowType windowCreate(
    final int sizeX,
    final int sizeY)
  {
    return this.windowCreateOnLayer(
      sizeX,
      sizeY,
      SyWindowLayers.layerForNormalWindows()
    );
  }

  /**
   * Create a new window.
   *
   * @param sizeX The window width
   * @param sizeY The window height
   * @param layer The window layer
   *
   * @return A new window
   */

  SyWindowType windowCreateOnLayer(
    int sizeX,
    int sizeY,
    int layer);

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
}
