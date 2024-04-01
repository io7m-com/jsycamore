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


package com.io7m.jsycamore.components.standard;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jorchard.core.JOTreeNode;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.keyboard.SyKeyboardFocusBehavior;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuItemAtomType;
import com.io7m.jsycamore.api.menus.SyMenuItemSeparatorType;
import com.io7m.jsycamore.api.menus.SyMenuItemSubmenuType;
import com.io7m.jsycamore.api.menus.SyMenuItemType;
import com.io7m.jsycamore.api.menus.SyMenuReadableType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.forms.SyFormColumnsConfiguration;
import com.io7m.jsycamore.components.standard.internal.SyMenuItemAtom;
import com.io7m.jsycamore.components.standard.internal.SyMenuItemSeparator;
import com.io7m.jsycamore.components.standard.internal.SyMenuItemSubmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.components.SyResizeBehaviour.FILL_SPACE;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnSizeType.exact;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnSizeType.flexible;

/**
 * A menu.
 */

public final class SyMenu extends SyComponentAbstract implements SyMenuType
{
  private static final int MENU_ICON_SIZE = 24;
  private static final int MENU_SUBMENU_ICON_SIZE = 12;
  private static final int MENU_SUBMENU_ICON_END_PADDING = 4;

  private final SyPackVertical align;
  private final AttributeType<Boolean> expanded;
  private final JOTreeNodeType<SyMenuType> menuNode;
  private final SyFormColumnsConfiguration columns;
  private List<SyMenuItemType> items;

  /**
   * A menu.
   *
   * @param screen     The screen that owns the menu
   * @param themeClasses The extra theme classes, if any
   */

  public SyMenu(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(
      screen,
      themeClasses,
      SyKeyboardFocusBehavior.IGNORES_FOCUS_AND_STOPS_TRAVERSAL
    );

    this.items = List.of();

    /*
     * Menu items use four columns.
     *
     * 1. The menu icon (maybe invisible)
     * 2. The flexible space for the menu item text
     * 3. An icon used to indicate a submenu
     * 4. Some padding.
     */

    this.columns =
      SyFormColumnsConfiguration.columns(
        exact(MENU_ICON_SIZE),
        flexible(),
        exact(MENU_SUBMENU_ICON_SIZE),
        exact(MENU_SUBMENU_ICON_END_PADDING)
      );

    final var attributes = SyComponentAttributes.get();
    this.expanded = attributes.create(false);

    this.align = new SyPackVertical(screen);
    this.align.childSizeXBehaviour().set(FILL_SPACE);
    this.childAdd(this.align);

    this.menuNode = JOTreeNode.create(this);
  }

  /**
   * A menu.
   *
   * @param inScreen The screen that owns the menu
   */

  @ConvenienceConstructor
  public SyMenu(final SyScreenType inScreen)
  {
    this(inScreen, List.of());
  }

  private static <TR, T extends TR> JOTreeNodeType<TR> castNode(
    final JOTreeNodeType<T> o)
  {
    return (JOTreeNodeType<TR>) o;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    var itemMaxWidth = 0;
    var itemTotalY = 0;

    /*
     * Iterate over all the menu items and determine the total size on the Y
     * axis needed for the menu, and the maximum width needed to display the
     * longest menu item.
     */

    final var menuItems = this.items();
    for (final var menuItem : menuItems) {
      final var itemSize =
        menuItem.minimumSizeRequired(layoutContext);

      itemTotalY += itemSize.sizeY();
      itemMaxWidth = Math.max(itemMaxWidth, itemSize.sizeX());
    }

    /*
     * Add some padding.
     */

    itemMaxWidth += 16;

    final var subConstraints =
      new SyConstraints(
        itemMaxWidth,
        0,
        itemMaxWidth,
        Math.min(constraints.sizeMaximumY(), itemTotalY)
      );

    final var size =
      this.align.layout(layoutContext, subConstraints);

    this.setSize(size);
    return size;
  }

  @Override
  public JOTreeNodeType<SyMenuType> menuNode()
  {
    return this.menuNode;
  }

  @Override
  public SyMenuItemSeparatorType addSeparator()
  {
    final var v =
      this.addMenuItem(new SyMenuItemSeparator(this));
    this.align.childAdd(v);
    return v;
  }

  @Override
  public SyMenuItemAtomType addAtom(
    final SyText text,
    final Runnable action)
  {
    final var v =
      this.addMenuItem(
        new SyMenuItemAtom(this, this.columns, text, action)
      );

    this.align.childAdd(v);
    return v;
  }

  @Override
  public SyMenuItemSubmenuType addSubmenu(
    final SyText text,
    final SyMenuType menu)
  {
    this.menuNode.childAdd(menu.menuNode());

    final var v =
      this.addMenuItem(new SyMenuItemSubmenu(this, this.columns, menu, text));
    this.align.childAdd(v);
    return v;
  }

  @Override
  public JOTreeNodeReadableType<SyMenuReadableType> menuNodeReadable()
  {
    return castNode(this.menuNode);
  }

  @Override
  public AttributeType<Boolean> expanded()
  {
    return this.expanded;
  }

  @Override
  public List<SyMenuItemType> items()
  {
    return List.copyOf(this.items);
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  private <T extends SyMenuItemType> T addMenuItem(
    final T item)
  {
    final var newItems =
      new ArrayList<SyMenuItemType>(this.items.size() + 1);
    newItems.addAll(this.items);
    newItems.add(item);
    this.items = List.copyOf(newItems);
    return item;
  }
}
