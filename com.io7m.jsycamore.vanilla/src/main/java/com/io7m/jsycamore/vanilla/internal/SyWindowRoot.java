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
import com.io7m.jsycamore.api.SyThemeType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.EnumMap;

import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_CONTENT_AREA;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_ROOT;

public final class SyWindowRoot extends SyWindowComponent
{
  private final EnumMap<SyWindowDecorationComponent, SyWindowComponent> windowComponents;

  SyWindowRoot()
  {
    super(WINDOW_ROOT);

    this.windowComponents = new EnumMap<>(SyWindowDecorationComponent.class);
    for (final var semantic : SyWindowDecorationComponent.values()) {
      if (semantic == WINDOW_ROOT) {
        continue;
      }

      final SyWindowComponent component = switch (semantic) {
        case WINDOW_RESIZE_E -> new SyWindowResizeE();
        case WINDOW_RESIZE_SE -> new SyWindowResizeSE();
        case WINDOW_ROOT -> throw new UnreachableCodeException();
        case WINDOW_CONTENT_AREA -> new SyWindowContentArea();
        case WINDOW_RESIZE_NW -> new SyWindowResizeNW();
        case WINDOW_RESIZE_N -> new SyWindowResizeN();
        case WINDOW_RESIZE_NE -> new SyWindowResizeNE();
        case WINDOW_RESIZE_S -> new SyWindowResizeS();
        case WINDOW_RESIZE_SW -> new SyWindowResizeSW();
        case WINDOW_RESIZE_W -> new SyWindowResizeW();
        case WINDOW_BUTTON_CLOSE -> new SyWindowButtonClose();
        case WINDOW_BUTTON_MAXIMIZE -> new SyWindowButtonMaximize();
        case WINDOW_BUTTON_MENU -> new SyWindowButtonMenu();
        case WINDOW_TITLE -> new SyWindowTitle();
      };

      this.windowComponents.put(semantic, component);
      this.node().childAdd(component.node());
    }
  }

  @Override
  protected boolean onEvent(
    final SyEventType event)
  {
    return false;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyThemeType theme,
    final SyConstraints constraints)
  {
    this.setSize(constraints.sizeMaximum());

    /*
     * If the window we're attached to has decorations turned off, simply
     * resize the content area to cover the entire window and don't bother
     * to lay out any other components.
     */

    if (!this.isWindowDecorated()) {
      final var content =
        this.windowComponents.get(WINDOW_CONTENT_AREA);

      content.setSize(constraints.sizeMaximum());
      content.setPosition(PVectors2I.zero());
      return this.size().get();
    }

    /*
     * Sort the child nodes so that they appear in the correct Z order
     * according to the theme.
     */

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

    for (final var entry : this.windowComponents.entrySet()) {
      final var semantic =
        entry.getKey();
      final var component =
        entry.getValue();
      final var componentSize =
        component.layout(theme, constraints);

      component.position().set(
        theme.positionForWindowDecorationComponent(
          this.size().get(),
          componentSize,
          semantic)
      );
    }

    return this.size().get();
  }
}
