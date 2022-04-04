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


package com.io7m.jsycamore.components.standard.internal;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuItemSubmenuType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyImageView;
import com.io7m.jsycamore.components.standard.SyLimit;
import com.io7m.jsycamore.components.standard.SyPackHorizontal;
import com.io7m.jsycamore.components.standard.SySpace;
import com.io7m.jsycamore.components.standard.SyTextView;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.MENU_ITEM_TEXT;
import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

/**
 * A menu item that opens a submenu.
 */

public final class SyMenuItemSubmenu
  extends SyComponentAbstract implements SyMenuItemSubmenuType
{
  private final SyImageView icon;
  private final SyLimit iconLimit;
  private final SyMenuType menuOpen;
  private final SyMenuType menuOwner;
  private final SyPackHorizontal layout;
  private final SySpace space;
  private final SyTextView text;

  /**
   * A menu item that opens a submenu.
   *
   * @param inMenuOwner The owning menu
   * @param inMenuOpen  The menu the submenu will open
   * @param inText      The menu item text
   */

  public SyMenuItemSubmenu(
    final SyMenuType inMenuOwner,
    final SyMenuType inMenuOpen,
    final String inText)
  {
    super(List.of());

    this.menuOwner =
      Objects.requireNonNull(inMenuOwner, "inMenuOwner");
    this.menuOpen =
      Objects.requireNonNull(inMenuOpen, "inMenuOpen");

    this.space = new SySpace();
    this.space.preferredSize().set(PAreaSizeI.of(0, 0));

    this.layout = new SyPackHorizontal();
    this.layout.paddingBetween().set(8);
    this.layout.alignVertical().set(ALIGN_VERTICAL_CENTER);

    this.icon = new SyImageView();
    this.icon.setMouseQueryAccepting(false);

    this.iconLimit = new SyLimit();
    this.iconLimit.limitSizeX().set(16);
    this.iconLimit.limitSizeY().set(16);
    this.iconLimit.childAdd(this.icon);

    this.text = new SyTextView(List.of(MENU_ITEM_TEXT));
    this.text.text().set(inText);

    this.layout.childAdd(this.space);
    this.layout.childAdd(this.iconLimit);
    this.layout.childAdd(this.text);

    this.childAdd(this.layout);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    /*
     * First, measure the text height.
     */

    final var textSize =
      this.text.layout(layoutContext, constraints);

    /*
     * Then, set this layout to the height of the text plus a margin.
     */

    final var sizeLimitY = textSize.sizeY() + 8;
    this.layout.limitSizeY().set(sizeLimitY);

    final var newSize =
      this.layout.layout(
        layoutContext,
        new SyConstraints(
          constraints.sizeMinimumX(),
          sizeLimitY,
          constraints.sizeMaximumX(),
          sizeLimitY
        ));

    this.setSize(newSize);
    return newSize;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> minimumSizeRequired(
    final SyLayoutContextType layoutContext)
  {
    final var textSize =
      this.text.minimumSizeRequired(layoutContext);

    return PAreaSizeI.of(
      16 + 8 + textSize.sizeX() + 32,
      16 + textSize.sizeY()
    );
  }

  @Override
  public SyMenuType menu()
  {
    return this.menuOwner;
  }

  @Override
  public SyMenuType submenu()
  {
    return this.menuOpen;
  }

  @Override
  public AttributeType<String> text()
  {
    return this.text.text();
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    if (event instanceof SyMouseEventOnOver) {
      this.openMenu();
      return EVENT_CONSUMED;
    }

    return EVENT_NOT_CONSUMED;
  }

  private void openMenu()
  {
    /*
     * Open the new menu directly to the right of the current menu item.
     */

    final var positionNow =
      this.position().get();
    final var sizeNow =
      this.size().get();

    final var positionOffset =
      PVector2I.<SySpaceParentRelativeType>of(
        positionNow.x() + sizeNow.sizeX(),
        positionNow.y()
      );

    final PVector2I<SySpaceViewportType> viewportPosition =
      this.viewportPositionOf(positionOffset);

    this.menuOpen.setPosition(
      PVector2I.of(viewportPosition.x(), viewportPosition.y())
    );

    this.window()
      .map(SyWindowType::screen)
      .ifPresent(screen -> screen.menuOpen(this.menuOpen));
  }
}
