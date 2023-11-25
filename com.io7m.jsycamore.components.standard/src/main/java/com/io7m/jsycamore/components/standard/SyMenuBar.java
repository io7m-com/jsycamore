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

import com.io7m.jattribute.core.AttributeSubscriptionType;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuBarItemType;
import com.io7m.jsycamore.api.menus.SyMenuBarType;
import com.io7m.jsycamore.api.menus.SyMenuSelected;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.menus.SyMenuSelected.MENU_NOT_SELECTED;
import static com.io7m.jsycamore.api.menus.SyMenuSelected.MENU_SELECTED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.MENU_BAR_ITEM_TEXT;

/**
 * A menu bar.
 */

public final class SyMenuBar
  extends SyComponentAbstract
  implements SyMenuBarType
{
  private final SyPackHorizontal align;
  private final HashMap<SyMenuType, AttributeSubscriptionType> menuSubscriptions;
  private SyMenuBarItem menuSelected;
  private List<SyMenuBarItemType> menuBarItems;

  /**
   * A menu bar.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyMenuBar(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);

    this.menuBarItems = List.of();
    this.menuSelected = null;
    this.menuSubscriptions = new HashMap<>(4);

    this.align = new SyPackHorizontal();
    this.align.setMouseQueryAccepting(false);

    this.childAdd(this.align);
  }

  /**
   * A menu bar.
   */

  @ConvenienceConstructor
  public SyMenuBar()
  {
    this(List.of());
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  private boolean menuIsSelected(
    final SyMenuBarItem menu)
  {
    return Objects.equals(this.menuSelected, menu);
  }

  private boolean menuAnySelected()
  {
    return this.menuSelected != null;
  }

  private void menuChangeSelection(
    final SyMenuBarItem menu)
  {
    this.menuSelected = menu;

    final var windowOpt = this.window();
    if (windowOpt.isEmpty()) {
      return;
    }
    final var window = windowOpt.get();

    /*
     * Open the new menu directly below the current menu bar item.
     */

    final var positionNow =
      menu.position().get();
    final var sizeNow =
      menu.size().get();

    final var positionOffset =
      PVector2I.<SySpaceParentRelativeType>of(
        positionNow.x(),
        positionNow.y() + sizeNow.sizeY()
      );

    final PVector2I<SySpaceViewportType> viewportPosition =
      this.viewportPositionOf(positionOffset);

    menu.menu.setPosition(
      PVector2I.of(viewportPosition.x(), viewportPosition.y())
    );
    window.screen().menuOpen(menu.menu());
  }

  @Override
  public SyMenuBarItemType addMenu(
    final String name,
    final SyMenuType menu)
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(menu, "menu");

    final var menuItem =
      new SyMenuBarItem(this, menu, name);

    final var newMenus =
      new ArrayList<SyMenuBarItemType>(this.menuBarItems.size() + 1);
    newMenus.addAll(this.menuBarItems);
    newMenus.add(menuItem);
    this.menuBarItems = List.copyOf(newMenus);
    this.menuSubscriptions.put(
      menu,
      menu.expanded().subscribe((expandedThen, expandedNow) -> {
        this.onMenuExpansionChanged(menuItem, expandedThen, expandedNow);
      })
    );

    this.align.childAdd(menuItem);
    return menuItem;
  }

  private void onMenuExpansionChanged(
    final SyMenuBarItem menu,
    final Boolean expandedThen,
    final Boolean expandedNow)
  {
    if (expandedThen && !expandedNow) {
      this.menuSelected = null;
      return;
    }
    if (expandedNow) {
      this.menuSelected = menu;
    }
  }

  private static final class SyMenuBarItem
    extends SyComponentAbstract
    implements SyMenuBarItemType
  {
    private final SyMenuBar menuBar;
    private final SyMenuType menu;
    private final SyTextView text;

    SyMenuBarItem(
      final SyMenuBar inMenuBar,
      final SyMenuType inMenu,
      final String inName)
    {
      super(List.of());

      this.menuBar =
        Objects.requireNonNull(inMenuBar, "inMenuBar");
      this.menu =
        Objects.requireNonNull(inMenu, "inMenu");

      this.text = new SyTextView(List.of(MENU_BAR_ITEM_TEXT));
      this.text.setText(inName);
      this.text.setMouseQueryAccepting(false);

      this.childAdd(this.text);
    }

    @Override
    public PAreaSizeI<SySpaceParentRelativeType> layout(
      final SyLayoutContextType layoutContext,
      final SyConstraints constraints)
    {
      final var textSize =
        this.text.layout(layoutContext, constraints);

      final var textX = 8;
      final var textY = (constraints.sizeMaximumY() / 2) - (textSize.sizeY() / 2);
      this.text.setPosition(PVector2I.of(textX, textY));

      final var newSize =
        PAreaSizeI.<SySpaceParentRelativeType>of(
          textSize.sizeX() + 16,
          constraints.sizeMaximumY()
        );
      this.setSize(newSize);
      return newSize;
    }

    @Override
    protected SyEventConsumed onEvent(
      final SyEventType event)
    {
      return switch (event) {
        case final SyMouseEventOnPressed e -> {
          this.menuBar.menuChangeSelection(this);
          yield EVENT_CONSUMED;
        }
        case final SyMouseEventOnOver e -> {
          if (this.menuBar.menuAnySelected()) {
            if (!this.menuBar.menuIsSelected(this)) {
              this.menuBar.menuChangeSelection(this);
              yield EVENT_CONSUMED;
            }
          }
          yield EVENT_NOT_CONSUMED;
        }
        default -> {
          yield EVENT_NOT_CONSUMED;
        }
      };
    }

    @Override
    public SyMenuSelected selected()
    {
      if (this.menuBar.menuIsSelected(this)) {
        return MENU_SELECTED;
      }
      return MENU_NOT_SELECTED;
    }

    @Override
    public AttributeType<String> name()
    {
      return this.text.text();
    }

    @Override
    public SyMenuType menu()
    {
      return this.menu;
    }
  }
}
