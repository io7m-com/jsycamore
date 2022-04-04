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
import com.io7m.jsycamore.api.menus.SyMenuItemAtomType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyImageView;
import com.io7m.jsycamore.components.standard.SyLimit;
import com.io7m.jsycamore.components.standard.SyPackHorizontal;
import com.io7m.jsycamore.components.standard.SySpace;
import com.io7m.jsycamore.components.standard.SyTextView;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.MENU_ITEM_TEXT;
import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

/**
 * A menu atom item.
 */

public final class SyMenuItemAtom
  extends SyComponentAbstract
  implements SyMenuItemAtomType
{
  private final SyMenuType menu;
  private final SyTextView text;
  private final Runnable action;
  private final SyPackHorizontal layout;
  private final SyImageView icon;
  private final SyLimit iconLimit;
  private final SySpace space;

  /**
   * A menu atom item.
   *
   * @param inMenu   The owning menu
   * @param inText   The item text
   * @param inAction The menu action
   */

  public SyMenuItemAtom(
    final SyMenuType inMenu,
    final String inText,
    final Runnable inAction)
  {
    super(List.of());

    this.space = new SySpace();
    this.space.preferredSize().set(PAreaSizeI.of(0, 0));

    this.menu =
      Objects.requireNonNull(inMenu, "inMenu");
    this.action =
      Objects.requireNonNull(inAction, "action");

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
    return this.menu;
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    if (event instanceof SyMouseEventOnReleased released) {
      return switch (released.button()) {
        case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> EVENT_NOT_CONSUMED;
        case MOUSE_BUTTON_LEFT -> {

          try {
            this.action.run();
          } finally {
            this.window()
              .map(SyWindowType::screen)
              .ifPresent(SyScreenType::menuClose);
          }

          yield EVENT_CONSUMED;
        }
      };
    }

    return EVENT_NOT_CONSUMED;
  }

  @Override
  public AttributeType<Optional<URI>> icon()
  {
    return this.icon.imageURI();
  }

  @Override
  public AttributeType<String> text()
  {
    return this.text.text();
  }
}
