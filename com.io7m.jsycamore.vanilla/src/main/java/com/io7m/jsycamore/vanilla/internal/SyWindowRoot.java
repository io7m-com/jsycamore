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

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.CONTAINER;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_CLOSE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_MAXIMIZE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_CONTENT_AREA;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_ROOT;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_TITLE;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.values;

/**
 * The window root component.
 */

public final class SyWindowRoot extends SyWindowComponent
{
  private final EnumMap<SyWindowDecorationComponent, SyWindowComponent> windowComponents;
  private final Map<SyWindowDecorationComponent, SyComponentType> windowComponentsView;

  SyWindowRoot(final SyScreenType screen)
  {
    super(screen, WINDOW_ROOT, List.of());

    this.windowComponents =
      new EnumMap<>(SyWindowDecorationComponent.class);
    this.windowComponentsView =
      (Map<SyWindowDecorationComponent, SyComponentType>) (Object) this.windowComponents;

    for (final var semantic : values()) {
      if (semantic == WINDOW_ROOT) {
        continue;
      }

      final SyWindowComponent component = switch (semantic) {
        case WINDOW_RESIZE_E -> new SyWindowResizeE(screen);
        case WINDOW_RESIZE_SE -> new SyWindowResizeSE(screen);
        case WINDOW_ROOT -> throw new UnreachableCodeException();
        case WINDOW_CONTENT_AREA -> new SyWindowContentArea(screen);
        case WINDOW_RESIZE_NW -> new SyWindowResizeNW(screen);
        case WINDOW_RESIZE_N -> new SyWindowResizeN(screen);
        case WINDOW_RESIZE_NE -> new SyWindowResizeNE(screen);
        case WINDOW_RESIZE_S -> new SyWindowResizeS(screen);
        case WINDOW_RESIZE_SW -> new SyWindowResizeSW(screen);
        case WINDOW_RESIZE_W -> new SyWindowResizeW(screen);
        case WINDOW_BUTTON_CLOSE -> new SyWindowButtonClose(screen);
        case WINDOW_BUTTON_MAXIMIZE -> new SyWindowButtonMaximize(screen);
        case WINDOW_BUTTON_MENU -> new SyWindowButtonMenu(screen);
        case WINDOW_TITLE -> new SyWindowTitle(screen);
      };

      this.windowComponents.put(semantic, component);
      this.node().childAdd(component.node());
    }

    this.setMouseQueryAccepting(false);
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    this.setSize(constraints.sizeMaximum());

    /*
     * If the window we're attached to has decorations turned off, simply
     * resize the content area to cover the entire window and don't bother
     * to lay out any other components.
     */

    if (!this.isWindowDecorated()) {
      final var content =
        this.windowComponents.get(WINDOW_CONTENT_AREA);

      content.layout(layoutContext, constraints);
      content.setPosition(PVectors2I.zero());
      return constraints.sizeMaximum();
    }

    /*
     * Sort the child nodes so that they appear in the correct Z order
     * according to the theme.
     */

    final var theme =
      layoutContext.themeCurrent();

    this.node()
      .childrenSort((o1, o2) -> {
        final var wc1 = (SyWindowComponent) o1;
        final var wc2 = (SyWindowComponent) o2;
        final var x1 =
          theme.zOrderForWindowDecorationComponent(wc1.semantic());
        final var x2 =
          theme.zOrderForWindowDecorationComponent(wc2.semantic());
        return Integer.compare(x1, x2);
      });

    this.setSize(constraints.sizeMaximum());
    this.setPosition(PVectors2I.zero());

    /*
     * Now position and size the components.
     */

    theme.layoutWindowComponents(
      layoutContext,
      constraints,
      this.windowComponentsView
    );

    /*
     * Now tell the components to lay out their descendants.
     */

    for (final var component : this.windowComponentsView.values()) {
      final var size =
        component.size().get();
      final var componentConstraints =
        new SyConstraints(0, 0, size.sizeX(), size.sizeY());

      component.layout(layoutContext, componentConstraints);
    }

    return this.size().get();
  }

  /**
   * @return The content area
   */

  public SyComponentType contentArea()
  {
    return this.windowComponents.get(WINDOW_CONTENT_AREA);
  }

  SyWindowTitle title()
  {
    return (SyWindowTitle) this.windowComponents.get(WINDOW_TITLE);
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SyThemeClassNameStandard.WINDOW_ROOT, CONTAINER);
  }

  /**
   * @return The window's close button
   */

  public SyComponentType closeButton()
  {
    return this.windowComponents.get(WINDOW_BUTTON_CLOSE);
  }

  /**
   * @return The window's menu button
   */

  public SyComponentType menuButton()
  {
    return this.windowComponents.get(SyWindowDecorationComponent.WINDOW_BUTTON_MENU);
  }

  /**
   * @return The window's maximize button
   */

  public SyComponentType maximizeButton()
  {
    return this.windowComponents.get(WINDOW_BUTTON_MAXIMIZE);
  }
}
