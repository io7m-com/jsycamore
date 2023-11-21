/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.themes;

import java.util.Objects;

/**
 * A list of standard class names that are expected to be recognized by themes.
 */

public enum SyThemeClassNameStandard
  implements SyThemeClassNameType
{
  /**
   * A generic button class.
   */

  BUTTON("Button"),

  /**
   * A checkbox class.
   */

  CHECKBOX("Checkbox"),

  /**
   * A generic container class.
   */

  CONTAINER("Container"),

  /**
   * A grid view class.
   */

  GRID_VIEW("GridView"),

  /**
   * An image view class.
   */

  IMAGE_VIEW("ImageView"),

  /**
   * A list view class.
   */

  LIST_VIEW("ListView"),

  /**
   * A menu bar class.
   */

  MENU_BAR("MenuBar"),

  /**
   * A menu bar item.
   */

  MENU_BAR_ITEM("MenuBarItem"),

  /**
   * The text in a menu bar item.
   */

  MENU_BAR_ITEM_TEXT("MenuBarItemText"),

  /**
   * A menu item class.
   */

  MENU_ITEM("MenuItem"),

  /**
   * A menu item class.
   */

  MENU_ITEM_ATOM("MenuItemAtom"),

  /**
   * A menu item class.
   */

  MENU_ITEM_SEPARATOR("MenuItemSeparator"),

  /**
   * A menu item class.
   */

  MENU_ITEM_SUBMENU("MenuItemSubmenu"),

  /**
   * A menu class.
   */

  MENU("Menu"),

  /**
   * A menu item text class.
   */

  MENU_ITEM_TEXT("MenuItemText"),

  /**
   * A generic meter class.
   */

  METER("Meter"),

  /**
   * A scrollbar class.
   */

  SCROLLBAR("Scrollbar"),

  /**
   * A text area class.
   */

  TEXT_AREA("TextArea"),

  /**
   * A text field class.
   */

  TEXT_FIELD("TextField"),

  /**
   * A text view class.
   */

  TEXT_VIEW("TextView"),

  /**
   * The close button for a window.
   */

  WINDOW_BUTTON_CLOSE("WindowButtonClose"),

  /**
   * The close button icon for a window.
   */

  WINDOW_BUTTON_CLOSE_ICON("WindowButtonCloseIcon"),

  /**
   * The maximize button for a window.
   */

  WINDOW_BUTTON_MAXIMIZE("WindowButtonMaximize"),

  /**
   * The menu button for a window.
   */

  WINDOW_BUTTON_MENU("WindowButtonMenu"),

  /**
   * The content area for a window.
   */

  WINDOW_CONTENT_AREA("WindowContentArea"),

  /**
   * The east resize area for a window.
   */

  WINDOW_RESIZE_E("WindowResizeE"),

  /**
   * The north resize area for a window.
   */

  WINDOW_RESIZE_N("WindowResizeN"),

  /**
   * The north-east resize area for a window.
   */

  WINDOW_RESIZE_NE("WindowResizeNE"),

  /**
   * The north-west resize area for a window.
   */

  WINDOW_RESIZE_NW("WindowResizeNW"),

  /**
   * The south resize area for a window.
   */

  WINDOW_RESIZE_S("WindowResizeS"),

  /**
   * The south-east resize area for a window.
   */

  WINDOW_RESIZE_SE("WindowResizeSE"),

  /**
   * The south-west resize area for a window.
   */

  WINDOW_RESIZE_SW("WindowResizeSW"),

  /**
   * The west resize area for a window.
   */

  WINDOW_RESIZE_W("WindowResizeW"),

  /**
   * The root component for a window.
   */

  WINDOW_ROOT("WindowRoot"),

  /**
   * The title component for a window.
   */

  WINDOW_TITLE("WindowTitle"),

  /**
   * The title text component for a window.
   */

  WINDOW_TITLE_TEXT("WindowTitleText");

  private final String className;

  SyThemeClassNameStandard(
    final String inClassName)
  {
    this.className = Objects.requireNonNull(inClassName, "className");
  }

  @Override
  public String toString()
  {
    return this.className;
  }

  @Override
  public String className()
  {
    return this.className;
  }
}
