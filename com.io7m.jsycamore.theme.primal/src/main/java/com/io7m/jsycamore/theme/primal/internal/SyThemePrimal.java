/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.theme.primal.internal;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeComponentType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.themes.SyThemeValuesType;
import com.io7m.jsycamore.api.themes.SyThemeableReadableType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.CONTAINER;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.TEXT_VIEW;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.WINDOW_TITLE_TEXT;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_CLOSE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_MAXIMIZE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_MENU;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_CONTENT_AREA;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_E;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_N;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_NE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_NW;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_S;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_SE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_SW;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_W;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_TITLE;

/**
 * An instance of the Primal theme.
 */

public final class SyThemePrimal implements SyThemeType
{
  private static final int BORDER_THICKNESS = 1;
  private static final int RESIZE_BAR_THICKNESS = 8;
  private static final int RESIZE_BOX_SIZE = 8;
  private static final int BUTTON_SIZE = 24;
  private static final int TITLE_HEIGHT = 24;
  private static final int RESIZE_BOX_SIZE_DOUBLE =
    RESIZE_BOX_SIZE + RESIZE_BOX_SIZE;

  private final SyPrimalUnmatched unmatched;
  private final EnumMap<SyThemeClassNameStandard, SyThemeComponentType> standards;
  private final SyThemeValuesType values;

  /**
   * An instance of the Primal theme.
   */

  public SyThemePrimal()
  {
    this.values =
      SyPrimalValues.create();
    this.unmatched =
      new SyPrimalUnmatched(this);
    this.standards =
      new EnumMap<>(SyThemeClassNameStandard.class);

    final var windowButton =
      new SyPrimalWindowButton(this);
    final var button =
      new SyPrimalButton(this);
    final var imageView =
      new SyPrimalImageView(this);

    for (final var className : SyThemeClassNameStandard.values()) {
      switch (className) {
        case WINDOW_BUTTON_CLOSE,
          WINDOW_BUTTON_MAXIMIZE,
          WINDOW_BUTTON_MENU,
          WINDOW_RESIZE_E,
          WINDOW_RESIZE_N,
          WINDOW_RESIZE_NE,
          WINDOW_RESIZE_NW,
          WINDOW_RESIZE_S,
          WINDOW_RESIZE_SE,
          WINDOW_RESIZE_SW,
          WINDOW_RESIZE_W,
          WINDOW_TITLE -> {
          this.standards.put(className, windowButton);
        }
        case WINDOW_CONTENT_AREA, WINDOW_ROOT -> {
          this.standards.put(className, new SyPrimalWindowContentArea(this));
        }
        case BUTTON -> {
          this.standards.put(BUTTON, button);
        }
        case TEXT_VIEW -> {
          this.standards.put(TEXT_VIEW, new SyPrimalTextView(this));
        }
        case CONTAINER -> {
          this.standards.put(CONTAINER, new SyPrimalContainer(this));
        }
        case WINDOW_BUTTON_CLOSE_ICON, IMAGE_VIEW -> {
          this.standards.put(className, imageView);
        }
        case MENU_BAR -> {
          this.standards.put(className, new SyPrimalMenuBar(this));
        }
        case MENU_BAR_ITEM -> {
          this.standards.put(className, new SyPrimalMenuBarItem(this));
        }
        case MENU_BAR_ITEM_TEXT -> {
          this.standards.put(className, new SyPrimalMenuBarItemTextView(this));
        }
        case MENU_ITEM_TEXT -> {
          this.standards.put(className, new SyPrimalMenuItemTextView(this));
        }
        case MENU_ITEM -> {
          this.standards.put(className, new SyPrimalMenuItem(this));
        }
        case MENU_ITEM_ATOM -> {

        }
        case MENU_ITEM_SEPARATOR -> {
          this.standards.put(className, new SyPrimalMenuItemSeparator(this));
        }
        case MENU_ITEM_SUBMENU -> {

        }
        case MENU -> {
          this.standards.put(className, new SyPrimalMenu(this));
        }

        case SCROLLBAR_HORIZONTAL -> {
          this.standards.put(className, new SyPrimalScrollbarH(this));
        }
        case SCROLLBAR_HORIZONTAL_TRACK -> {
          this.standards.put(className, new SyPrimalScrollbarHTrack(this));
        }
        case SCROLLBAR_HORIZONTAL_BUTTON_LEFT -> {
          this.standards.put(className, new SyPrimalScrollbarHButton(this, button));
        }
        case SCROLLBAR_HORIZONTAL_BUTTON_LEFT_ICON -> {
          this.standards.put(className, new SyPrimalScrollbarHButtonIcon(this, imageView));
        }
        case SCROLLBAR_HORIZONTAL_BUTTON_RIGHT -> {
          this.standards.put(className, new SyPrimalScrollbarHButton(this, button));
        }
        case SCROLLBAR_HORIZONTAL_BUTTON_RIGHT_ICON -> {
          this.standards.put(className, new SyPrimalScrollbarHButtonIcon(this, imageView));
        }
        case SCROLLBAR_HORIZONTAL_BUTTON_THUMB -> {
          this.standards.put(className, new SyPrimalScrollbarHButton(this, button));
        }
        case SCROLLBAR_HORIZONTAL_BUTTON_THUMB_ICON -> {
          this.standards.put(className, new SyPrimalScrollbarHButtonIcon(this, imageView));
        }

        case SCROLLBAR_VERTICAL -> {
          this.standards.put(className, new SyPrimalScrollbarV(this));
        }
        case SCROLLBAR_VERTICAL_TRACK -> {
          this.standards.put(className, new SyPrimalScrollbarVTrack(this));
        }
        case SCROLLBAR_VERTICAL_BUTTON_UP -> {
          this.standards.put(className, new SyPrimalScrollbarVButton(this, button));
        }
        case SCROLLBAR_VERTICAL_BUTTON_UP_ICON -> {
          this.standards.put(className, new SyPrimalScrollbarVButtonIcon(this, imageView));
        }
        case SCROLLBAR_VERTICAL_BUTTON_DOWN -> {
          this.standards.put(className, new SyPrimalScrollbarVButton(this, button));
        }
        case SCROLLBAR_VERTICAL_BUTTON_DOWN_ICON -> {
          this.standards.put(className, new SyPrimalScrollbarVButtonIcon(this, imageView));
        }
        case SCROLLBAR_VERTICAL_BUTTON_THUMB -> {
          this.standards.put(className, new SyPrimalScrollbarVButton(this, button));
        }
        case SCROLLBAR_VERTICAL_BUTTON_THUMB_ICON -> {
          this.standards.put(className, new SyPrimalScrollbarVButtonIcon(this, imageView));
        }

        case TEXT_AREA -> {
          this.standards.put(className, new SyPrimalTextArea(this));
        }

        case CHECKBOX,
          GRID_VIEW,
          LIST_VIEW,
          METER,
          TEXT_FIELD -> {

        }
        case WINDOW_TITLE_TEXT -> {
          this.standards.put(
            WINDOW_TITLE_TEXT,
            new SyPrimalTitleTextView(this));
        }
      }
    }
  }

  private static int verticalResizeBoxSizeY(
    final int windowSizeY)
  {
    return windowSizeY - (RESIZE_BOX_SIZE + BORDER_THICKNESS + RESIZE_BOX_SIZE);
  }

  @Override
  public SyThemeValuesType values()
  {
    return this.values;
  }

  @Override
  public void layoutWindowComponents(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints,
    final Map<SyWindowDecorationComponent, SyComponentType> components)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "windowConstraints");
    Objects.requireNonNull(components, "windowComponents");

    final var windowSize =
      constraints.<SySpaceParentRelativeType>sizeMaximum();

    final var windowSizeX = windowSize.sizeX();
    final var windowSizeY = windowSize.sizeY();

    /*
     * The content area is positioned under the title bar, and is sized such
     * that it fits between the resize bars.
     */

    {
      final var c = components.get(WINDOW_CONTENT_AREA);

      final var x = RESIZE_BOX_SIZE;
      final var y = RESIZE_BOX_SIZE + TITLE_HEIGHT;
      final var sizeX = windowSizeX - (RESIZE_BOX_SIZE_DOUBLE);
      final var sizeY = windowSizeY - (RESIZE_BOX_SIZE_DOUBLE + TITLE_HEIGHT);

      final var size =
        PAreaSizeI.<SySpaceParentRelativeType>of(sizeX, sizeY);

      c.setPosition(PVector2I.of(x, y));
      c.setSize(size);
    }

    layoutResizeButtons(constraints, components, windowSizeX, windowSizeY);
    layoutButtons(constraints, components, windowSizeX);
  }

  private static void layoutResizeButtons(
    final SyConstraints constraints,
    final Map<SyWindowDecorationComponent, SyComponentType> components,
    final int windowSizeX,
    final int windowSizeY)
  {
    layoutResizeButtonsNS(constraints, components, windowSizeX, windowSizeY);
    layoutResizeButtonsWE(constraints, components, windowSizeX, windowSizeY);
    layoutResizeButtonsCorners(constraints, components, windowSizeX, windowSizeY);
  }

  private static void layoutResizeButtonsCorners(
    final SyConstraints constraints,
    final Map<SyWindowDecorationComponent, SyComponentType> components,
    final int windowSizeX,
    final int windowSizeY)
  {
    /*
     * The NE/SE/SW/NW resize boxes are square and are positioned in the
     * corners.
     */

    final var resizeBoxSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(
        RESIZE_BOX_SIZE,
        RESIZE_BOX_SIZE);

    {
      final var c = components.get(WINDOW_RESIZE_NE);

      final var x = windowSizeX - (RESIZE_BOX_SIZE + BORDER_THICKNESS);
      final var y = 0;

      c.setSize(resizeBoxSize);
      c.setPosition(PVector2I.of(x, y));
    }

    {
      final var c = components.get(WINDOW_RESIZE_SE);

      final var x = windowSizeX - (RESIZE_BOX_SIZE + BORDER_THICKNESS);
      final var y = windowSizeY - (RESIZE_BOX_SIZE + BORDER_THICKNESS);

      c.setSize(resizeBoxSize);
      c.setPosition(PVector2I.of(x, y));
    }

    {
      final var c = components.get(WINDOW_RESIZE_SW);

      final var x = 0;
      final var y = windowSizeY - (RESIZE_BOX_SIZE + BORDER_THICKNESS);

      c.setSize(resizeBoxSize);
      c.setPosition(PVector2I.of(x, y));
    }

    {
      final var c = components.get(WINDOW_RESIZE_NW);

      final var x = 0;
      final var y = 0;

      c.setSize(resizeBoxSize);
      c.setPosition(PVector2I.of(x, y));
    }
  }

  private static void layoutResizeButtonsWE(
    final SyConstraints constraints,
    final Map<SyWindowDecorationComponent, SyComponentType> components,
    final int windowSizeX,
    final int windowSizeY)
  {
    /*
     * The W/E resize bars are vertical and cover the height of the window,
     * between the corner resize boxes.
     */

    {
      final var c = components.get(WINDOW_RESIZE_W);

      final var x = 0;
      final var y = RESIZE_BOX_SIZE;
      final var sizeX = RESIZE_BAR_THICKNESS;
      final int sizeY = verticalResizeBoxSizeY(windowSizeY);

      final var size =
        constraints.<SySpaceParentRelativeType>sizeWithin(sizeX, sizeY);

      c.setSize(size);
      c.setPosition(PVector2I.of(x, y));
    }

    {
      final var c = components.get(WINDOW_RESIZE_E);

      final var x = windowSizeX - (RESIZE_BAR_THICKNESS + BORDER_THICKNESS);
      final var y = RESIZE_BOX_SIZE;
      final var sizeX = RESIZE_BAR_THICKNESS;
      final int sizeY = verticalResizeBoxSizeY(windowSizeY);

      final var size =
        constraints.<SySpaceParentRelativeType>sizeWithin(sizeX, sizeY);

      c.setSize(size);
      c.setPosition(PVector2I.of(x, y));
    }
  }

  private static void layoutResizeButtonsNS(
    final SyConstraints constraints,
    final Map<SyWindowDecorationComponent, SyComponentType> components,
    final int windowSizeX,
    final int windowSizeY)
  {
    /*
     * The N/S resize bars are horizontal and cover the width of the window,
     * between the corner resize boxes.
     */

    {
      final var c = components.get(WINDOW_RESIZE_N);

      final var x = RESIZE_BOX_SIZE;
      final var y = 0;
      final var sizeX = windowSizeX - RESIZE_BOX_SIZE_DOUBLE;
      final var sizeY = RESIZE_BAR_THICKNESS;

      c.setSize(constraints.sizeWithin(sizeX, sizeY));
      c.setPosition(PVector2I.of(x, y));
    }

    {
      final var c = components.get(WINDOW_RESIZE_S);

      final var x = RESIZE_BOX_SIZE;
      final var y = windowSizeY - (RESIZE_BOX_SIZE + BORDER_THICKNESS);
      final var sizeX = windowSizeX - RESIZE_BOX_SIZE_DOUBLE;
      final var sizeY = RESIZE_BAR_THICKNESS;

      final var size =
        constraints.<SySpaceParentRelativeType>sizeWithin(sizeX, sizeY);

      c.setSize(size);
      c.setPosition(PVector2I.of(x, y));
    }
  }

  private static void layoutButtons(
    final SyConstraints constraints,
    final Map<SyWindowDecorationComponent, SyComponentType> components,
    final int windowSizeX)
  {
    /*
     * The window controls are positioned horizontally below the N resize
     * box.
     *
     * The close button is placed first, then the title, then the menu button,
     * and then finally the maximize button.
     */

    final var buttonSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(
        BUTTON_SIZE,
        BUTTON_SIZE);

    {
      final var close = components.get(WINDOW_BUTTON_CLOSE);
      final var title = components.get(WINDOW_TITLE);
      final var menu = components.get(WINDOW_BUTTON_MENU);
      final var max = components.get(WINDOW_BUTTON_MAXIMIZE);

      final var totalSizeX = windowSizeX - RESIZE_BOX_SIZE_DOUBLE;

      close.setSize(buttonSize);
      menu.setSize(buttonSize);
      max.setSize(buttonSize);

      final var startX = RESIZE_BOX_SIZE;
      final var y = RESIZE_BOX_SIZE;

      close.setPosition(PVector2I.of(startX, y));

      var titleX = startX;
      var titleW = totalSizeX;
      final var maxX = (startX + totalSizeX) - buttonSize.sizeX();
      var menuX = maxX - buttonSize.sizeX();

      if (close.isVisible()) {
        titleX += buttonSize.sizeX();
        titleW -= buttonSize.sizeX();
      }

      if (menu.isVisible()) {
        titleW -= buttonSize.sizeX();
      }

      if (max.isVisible()) {
        titleW -= buttonSize.sizeX();
      } else {
        menuX = maxX;
      }

      title.setSize(PAreaSizeI.of(titleW, TITLE_HEIGHT));
      title.setPosition(PVector2I.of(titleX, y));
      menu.setPosition(PVector2I.of(menuX, y));
      max.setPosition(PVector2I.of(maxX, y));
    }
  }

  @Override
  public int zOrderForWindowDecorationComponent(
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_RESIZE_NW,
        WINDOW_RESIZE_W,
        WINDOW_RESIZE_SW,
        WINDOW_RESIZE_SE,
        WINDOW_RESIZE_S,
        WINDOW_RESIZE_NE,
        WINDOW_RESIZE_N,
        WINDOW_RESIZE_E -> 3;

      case WINDOW_BUTTON_CLOSE,
        WINDOW_CONTENT_AREA,
        WINDOW_BUTTON_MENU,
        WINDOW_BUTTON_MAXIMIZE -> 2;

      case WINDOW_ROOT -> 0;

      case WINDOW_TITLE -> BORDER_THICKNESS;
    };
  }

  @Override
  public SyThemeComponentType findForComponent(
    final SyThemeableReadableType component)
  {
    for (final var className : component.themeClassesInPreferenceOrder()) {
      if (className instanceof final SyThemeClassNameStandard standard) {
        if (this.standards.containsKey(standard)) {
          return this.standards.get(standard);
        }
      }
    }
    return this.unmatched;
  }
}
