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
import com.io7m.jsycamore.api.components.SyTextViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuItemSubmenuType;
import com.io7m.jsycamore.api.menus.SyMenuItemType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.components.standard.SyAlign;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyImageView;
import com.io7m.jsycamore.components.standard.SySpace;
import com.io7m.jsycamore.components.standard.forms.SyFormColumnsConfiguration;
import com.io7m.jsycamore.components.standard.forms.SyFormRow;
import com.io7m.jsycamore.components.standard.text.SyTextView;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.text.SyTextDirection.TEXT_DIRECTION_LEFT_TO_RIGHT;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.MENU_ITEM_TEXT;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_LEFT;
import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

/**
 * A menu item that opens a submenu.
 */

public final class SyMenuItemSubmenu
  extends SyComponentAbstract implements SyMenuItemSubmenuType
{
  private final SyAlign iconAlign;
  private final SyAlign iconSubmenuAlign;
  private final SyAlign textAlign;
  private final SyFormColumnsConfiguration columns;
  private final SyFormRow row;
  private final SyImageView icon;
  private final SyTextViewType iconSubmenu;
  private final SyMenuType menuOpen;
  private final SyMenuType menuOwner;
  private final SyTextViewType text;

  /**
   * A menu item that opens a submenu.
   *
   * @param inMenuOwner The owning menu
   * @param inColumns   The columns configuration
   * @param inMenuOpen  The menu the submenu will open
   * @param inText      The menu item text
   */

  public SyMenuItemSubmenu(
    final SyMenuType inMenuOwner,
    final SyFormColumnsConfiguration inColumns,
    final SyMenuType inMenuOpen,
    final SyText inText)
  {
    super(inMenuOwner.screen(), List.of());

    this.menuOwner =
      Objects.requireNonNull(inMenuOwner, "inMenuOwner");
    this.columns =
      Objects.requireNonNull(inColumns, "inColumns");
    this.menuOpen =
      Objects.requireNonNull(inMenuOpen, "inMenuOpen");

    this.row =
      new SyFormRow(inMenuOwner.screen(), this.columns);

    this.iconSubmenu =
      SyTextView.textView(inMenuOwner.screen(), List.of(MENU_ITEM_TEXT));
    this.iconSubmenu.setMouseQueryAccepting(false);
    this.iconSubmenu.text()
      .set(new SyText("→", TEXT_DIRECTION_LEFT_TO_RIGHT));

    this.iconSubmenuAlign = new SyAlign(inMenuOwner.screen());
    this.iconSubmenuAlign.alignmentHorizontal().set(ALIGN_HORIZONTAL_CENTER);
    this.iconSubmenuAlign.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    this.iconSubmenuAlign.childAdd(this.iconSubmenu);

    this.icon = new SyImageView(inMenuOwner.screen());
    this.icon.setMouseQueryAccepting(false);
    this.icon.sizeUpperLimit().set(PAreaSizeI.of(16, 16));

    this.iconAlign = new SyAlign(inMenuOwner.screen());
    this.iconAlign.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    this.iconAlign.alignmentHorizontal().set(ALIGN_HORIZONTAL_CENTER);
    this.iconAlign.childAdd(this.icon);

    this.text =
      SyTextView.textView(inMenuOwner.screen(), List.of(MENU_ITEM_TEXT));
    this.text.text().set(inText);

    this.textAlign = new SyAlign(inMenuOwner.screen());
    this.textAlign.alignmentHorizontal().set(ALIGN_HORIZONTAL_LEFT);
    this.textAlign.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    this.textAlign.childAdd(this.text);

    this.row.childAdd(this.iconAlign);
    this.row.childAdd(this.textAlign);
    this.row.childAdd(this.iconSubmenuAlign);
    this.row.childAdd(new SySpace(inMenuOwner.screen()));

    this.childAdd(this.row);
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
      this.text.minimumSizeRequired(layoutContext);

    /*
     * Then, set this layout to the height of the text plus a margin.
     */

    final var sizeLimitY = textSize.sizeY() + 8;
    this.row.sizeUpperLimit().set(PAreaSizeI.of(Integer.MAX_VALUE, sizeLimitY));
    this.columns.evaluateSizes(constraints.sizeMaximumX());

    final var newSize =
      this.row.layout(
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
  public boolean isMouseOverMenuDescendant()
  {
    if (this.isMouseOver()) {
      return true;
    }

    return this.submenu()
      .items()
      .stream()
      .anyMatch(SyMenuItemType::isMouseOverMenuDescendant);
  }

  @Override
  public SyMenuType submenu()
  {
    return this.menuOpen;
  }

  @Override
  public AttributeType<SyText> text()
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
      .map(SyScreenType::menuService)
      .ifPresent(screen -> screen.menuOpen(this.menuOpen));
  }
}
