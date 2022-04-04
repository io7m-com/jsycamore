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
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuItemAtomType;
import com.io7m.jsycamore.api.menus.SyMenuItemSeparatorType;
import com.io7m.jsycamore.api.menus.SyMenuItemSubmenuType;
import com.io7m.jsycamore.api.menus.SyMenuItemType;
import com.io7m.jsycamore.api.menus.SyMenuReadableType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.internal.SyMenuItemAtom;
import com.io7m.jsycamore.components.standard.internal.SyMenuItemSeparator;
import com.io7m.jsycamore.components.standard.internal.SyMenuItemSubmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.components.SyResizeBehaviour.FILL_SPACE;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A menu.
 */

public final class SyMenu extends SyComponentAbstract implements SyMenuType
{
  private final SyPackVertical align;
  private final AttributeType<Boolean> expanded;
  private final JOTreeNodeType<SyMenuType> menuNode;
  private List<SyMenuItemType> items;

  /**
   * A menu.
   *
   * @param themeClasses The extra theme classes, if any
   */

  public SyMenu(
    final List<SyThemeClassNameType> themeClasses)
  {
    super(themeClasses);

    this.items = List.of();

    final var attributes = SyComponentAttributes.get();
    this.expanded = attributes.create(false);

    this.align = new SyPackVertical();
    this.align.childSizeXBehaviour().set(FILL_SPACE);
    this.childAdd(this.align);

    this.menuNode = JOTreeNode.create(this);
  }

  /**
   * A menu.
   */

  @ConvenienceConstructor
  public SyMenu()
  {
    this(List.of());
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

    final var menuItems = this.itemsNow();
    for (final var menuItem : menuItems) {
      final var itemSize =
        menuItem.minimumSizeRequired(layoutContext);

      itemTotalY += itemSize.sizeY();
      itemMaxWidth = Math.max(itemMaxWidth, itemSize.sizeX());
    }

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
    final String text,
    final Runnable action)
  {
    final var v =
      this.addMenuItem(new SyMenuItemAtom(this, text, action));
    this.align.childAdd(v);
    return v;
  }

  @Override
  public SyMenuItemSubmenuType addSubmenu(
    final String text,
    final SyMenuType menu)
  {
    this.menuNode.childAdd(menu.menuNode());

    final var v =
      this.addMenuItem(new SyMenuItemSubmenu(this, menu, text));
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
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  private List<SyMenuItemType> itemsNow()
  {
    return this.items;
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
