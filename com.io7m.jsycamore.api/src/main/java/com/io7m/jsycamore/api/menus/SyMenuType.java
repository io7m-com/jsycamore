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


package com.io7m.jsycamore.api.menus;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.text.SyText;

import java.util.List;

/**
 * A menu.
 *
 * A menu typically presents a vertical list of menu items, each of which can be
 * selected by clicking. A menu is either <i>expanded</i> or <i>not
 * expanded</i>. Only one menu can be expanded within a screen at any given
 * time. Clicking outside of the menu typically causes the expanded menu to be
 * no longer expanded.
 *
 * Menus form a tree structure, with exactly one root.
 */

public interface SyMenuType extends SyComponentType, SyMenuReadableType
{
  /**
   * @return The node for this menu
   */

  JOTreeNodeType<SyMenuType> menuNode();

  /**
   * Add a new separator menu item.
   *
   * @return The item
   */

  SyMenuItemSeparatorType addSeparator();

  /**
   * Add a new atom menu item
   *
   * @param text   The item text
   * @param action The item action
   *
   * @return The item
   */

  SyMenuItemAtomType addAtom(
    SyText text,
    Runnable action);

  /**
   * Add a new submenu menu item
   *
   * @param text The item text
   * @param menu The menu
   *
   * @return The item
   */

  SyMenuItemSubmenuType addSubmenu(
    SyText text,
    SyMenuType menu);

  @Override
  AttributeType<Boolean> expanded();

  /**
   * @return An immutable snapshot of the items within the menu
   */

  List<SyMenuItemType> items();
}
