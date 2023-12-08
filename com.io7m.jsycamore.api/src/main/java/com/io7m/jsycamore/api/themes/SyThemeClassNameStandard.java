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

  BUTTON(
    "Button",
    "A generic button."),

  /**
   * A checkbox class.
   */

  CHECKBOX(
    "Checkbox",
    "A checkbox."),

  /**
   * A generic container class.
   */

  CONTAINER(
    "Container",
    "A generic container."),

  /**
   * A grid view class.
   */

  GRID_VIEW(
    "GridView",
    "A grid view."),

  /**
   * An image view class.
   */

  IMAGE_VIEW(
    "ImageView",
    "An image view."),

  /**
   * A list view class.
   */

  LIST_VIEW(
    "ListView",
    "A list view."),

  /**
   * A menu bar class.
   */

  MENU_BAR(
    "MenuBar",
    "A menu bar."),

  /**
   * A menu bar item.
   */

  MENU_BAR_ITEM(
    "MenuBarItem",
    "A menu bar item."),

  /**
   * The text in a menu bar item.
   */

  MENU_BAR_ITEM_TEXT(
    "MenuBarItemText",
    "The text in a menu bar item."),

  /**
   * A menu item class.
   */

  MENU_ITEM(
    "MenuItem",
    "A menu item."),

  /**
   * A menu item class.
   */

  MENU_ITEM_ATOM(
    "MenuItemAtom",
    "A menu atom item."),

  /**
   * A menu item class.
   */

  MENU_ITEM_SEPARATOR(
    "MenuItemSeparator",
    "A menu separator item."),

  /**
   * A menu item class.
   */

  MENU_ITEM_SUBMENU(
    "MenuItemSubmenu",
    "A menu submenu item."),

  /**
   * A menu class.
   */

  MENU(
    "Menu",
    "A menu."),

  /**
   * A menu item text class.
   */

  MENU_ITEM_TEXT(
    "MenuItemText",
    "A menu item text."),

  /**
   * A generic meter class.
   */

  METER(
    "Meter",
    "A generic meter."),

  /**
   * A horizontal scrollbar class.
   */

  SCROLLBAR_HORIZONTAL(
    "ScrollbarHorizontal",
    "A horizontal scrollbar."),

  /**
   * A horizontal scrollbar track class.
   */

  SCROLLBAR_HORIZONTAL_TRACK(
    "ScrollbarHorizontalTrack",
    "A horizontal scrollbar track."),

  /**
   * A scrollbar button class.
   */

  SCROLLBAR_HORIZONTAL_BUTTON_LEFT(
    "ScrollbarHorizontalButtonLeft",
    "A horizontal scrollbar left button."),

  /**
   * A scrollbar button icon class.
   */

  SCROLLBAR_HORIZONTAL_BUTTON_LEFT_ICON(
    "ScrollbarHorizontalButtonLeftIcon",
    "A horizontal scrollbar left button icon."),

  /**
   * A scrollbar button class.
   */

  SCROLLBAR_HORIZONTAL_BUTTON_RIGHT(
    "ScrollbarHorizontalButtonRight",
    "A horizontal scrollbar right button."),

  /**
   * A scrollbar button icon class.
   */

  SCROLLBAR_HORIZONTAL_BUTTON_RIGHT_ICON(
    "ScrollbarHorizontalButtonRightIcon",
    "A horizontal scrollbar right button icon."),

  /**
   * A scrollbar button class.
   */

  SCROLLBAR_HORIZONTAL_BUTTON_THUMB(
    "ScrollbarHorizontalButtonThumb",
    "A horizontal scrollbar thumb button."),

  /**
   * A scrollbar button icon class.
   */

  SCROLLBAR_HORIZONTAL_BUTTON_THUMB_ICON(
    "ScrollbarHorizontalButtonThumbIcon",
    "A horizontal scrollbar thumb button icon."),

  /**
   * A horizontal scrollbar class.
   */

  SCROLLBAR_VERTICAL(
    "ScrollbarVertical",
    "A vertical scrollbar."),

  /**
   * A horizontal scrollbar track class.
   */

  SCROLLBAR_VERTICAL_TRACK(
    "ScrollbarVerticalTrack",
    "A vertical scrollbar track."),

  /**
   * A scrollbar button class.
   */

  SCROLLBAR_VERTICAL_BUTTON_UP(
    "ScrollbarVerticalButtonUp",
    "A vertical scrollbar up button."),

  /**
   * A scrollbar button icon class.
   */

  SCROLLBAR_VERTICAL_BUTTON_UP_ICON(
    "ScrollbarVerticalButtonUpIcon",
    "A vertical scrollbar up button icon."),

  /**
   * A scrollbar button class.
   */

  SCROLLBAR_VERTICAL_BUTTON_DOWN(
    "ScrollbarVerticalButtonDown",
    "A vertical scrollbar down button."),

  /**
   * A scrollbar button icon class.
   */

  SCROLLBAR_VERTICAL_BUTTON_DOWN_ICON(
    "ScrollbarVerticalButtonDownIcon",
    "A vertical scrollbar down button icon."),

  /**
   * A scrollbar button class.
   */

  SCROLLBAR_VERTICAL_BUTTON_THUMB(
    "ScrollbarVerticalButtonThumb",
    "A vertical scrollbar thumb button."),

  /**
   * A scrollbar button icon class.
   */

  SCROLLBAR_VERTICAL_BUTTON_THUMB_ICON(
    "ScrollbarVerticalButtonThumbIcon",
    "A vertical scrollbar thumb button icon."),

  /**
   * A scrollpane class.
   */

  SCROLLPANE(
    "ScrollPane",
    "A scroll pane."),

  /**
   * A scrollpane content area class.
   */

  SCROLLPANE_CONTENT_AREA(
    "ScrollPaneContentArea",
    "A scroll pane content area."),

  /**
   * A scrollpane content area viewport class.
   */

  SCROLLPANE_CONTENT_AREA_VIEWPORT(
    "ScrollPaneContentAreaViewport",
    "A scroll pane viewport."),

  /**
   * A text area class.
   */

  TEXT_AREA(
    "TextArea",
    "A text area."),

  /**
   * A text field class.
   */

  TEXT_FIELD(
    "TextField",
    "A text field."),

  /**
   * A text view class.
   */

  TEXT_VIEW(
    "TextView",
    "A text view."),

  /**
   * A multi-line text view  class.
   */

  TEXT_MULTILINE_VIEW(
    "TextMultilineView",
    "A multi-line text view."),

  /**
   * The close button for a window.
   */

  WINDOW_BUTTON_CLOSE(
    "WindowButtonClose",
    "The close button for a window."),

  /**
   * The close button icon for a window.
   */

  WINDOW_BUTTON_CLOSE_ICON(
    "WindowButtonCloseIcon",
    "The close button icon for a window."),

  /**
   * The maximize button for a window.
   */

  WINDOW_BUTTON_MAXIMIZE(
    "WindowButtonMaximize",
    "The maximize button for a window."),

  /**
   * The menu button for a window.
   */

  WINDOW_BUTTON_MENU(
    "WindowButtonMenu",
    "The menu button for a window."),

  /**
   * The content area for a window.
   */

  WINDOW_CONTENT_AREA(
    "WindowContentArea",
    "The content area for a window."),

  /**
   * The east resize area for a window.
   */

  WINDOW_RESIZE_E(
    "WindowResizeE",
    "The east resize area for a window."),

  /**
   * The north resize area for a window.
   */

  WINDOW_RESIZE_N(
    "WindowResizeN",
    "The north resize area for a window."),

  /**
   * The north-east resize area for a window.
   */

  WINDOW_RESIZE_NE(
    "WindowResizeNE",
    "The north-east resize area for a window."),

  /**
   * The north-west resize area for a window.
   */

  WINDOW_RESIZE_NW(
    "WindowResizeNW",
    "The north-west resize area for a window."),

  /**
   * The south resize area for a window.
   */

  WINDOW_RESIZE_S(
    "WindowResizeS",
    "The south resize area for a window."),

  /**
   * The south-east resize area for a window.
   */

  WINDOW_RESIZE_SE(
    "WindowResizeSE",
    "The south-east resize area for a window."),

  /**
   * The south-west resize area for a window.
   */

  WINDOW_RESIZE_SW(
    "WindowResizeSW",
    "The south-west resize area for a window."),

  /**
   * The west resize area for a window.
   */

  WINDOW_RESIZE_W(
    "WindowResizeW",
    "The west resize area for a window."),

  /**
   * The root component for a window.
   */

  WINDOW_ROOT(
    "WindowRoot",
    "The root component for a window."),

  /**
   * The title component for a window.
   */

  WINDOW_TITLE(
    "WindowTitle",
    "The title component for a window."),

  /**
   * The title text component for a window.
   */

  WINDOW_TITLE_TEXT(
    "WindowTitleText",
    "The title text component for a window.");

  private final String className;
  private final String description;

  SyThemeClassNameStandard(
    final String inClassName,
    final String inDescription)
  {
    this.className =
      Objects.requireNonNull(inClassName, "className");
    this.description =
      Objects.requireNonNull(inDescription, "inDescription");
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

  @Override
  public String description()
  {
    return this.description;
  }
}
