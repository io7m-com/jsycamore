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
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.junreachable.UnreachableCodeException;

import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_SE;

public final class SyWindowResizeSE extends SyWindowComponent
{
  private PAreaSizeI<SySpaceViewportType> windowStartSize;

  SyWindowResizeSE()
  {
    super(WINDOW_RESIZE_SE);
  }

  @Override
  protected boolean onEvent(
    final SyEventType event)
  {
    if (event instanceof SyMouseEventType mouseEvent) {
      return this.onMouseEvent(mouseEvent);
    }
    return false;
  }

  private boolean onMouseEvent(
    final SyMouseEventType event)
  {
    if (event instanceof SyMouseEventOnPressed onPressed) {
      return switch (onPressed.button()) {
        case MOUSE_BUTTON_LEFT -> {
          final var window =
            this.window().orElseThrow(UnreachableCodeException::new);

          this.windowStartSize = window.size().get();
          yield true;
        }
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> false;
      };
    }

    if (event instanceof SyMouseEventOnReleased onReleased) {
      return switch (onReleased.button()) {
        case MOUSE_BUTTON_LEFT -> true;
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> false;
      };
    }

    if (event instanceof SyMouseEventOnHeld onHeld) {
      final var deltaX =
        onHeld.mousePositionNow().x() - onHeld.mousePositionFirst().x();
      final var deltaY =
        onHeld.mousePositionNow().y() - onHeld.mousePositionFirst().y();

      final var newSize =
        PAreaSizeI.<SySpaceViewportType>of(
          this.windowStartSize.sizeX() + deltaX,
          this.windowStartSize.sizeY() + deltaY
        );

      final var window =
        this.window().orElseThrow(UnreachableCodeException::new);

      window.setSize(newSize);
      return true;
    }

    return false;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyThemeType theme,
    final SyConstraints constraints)
  {
    final var newSize =
      theme.sizeForWindowDecorationComponent(constraints, this.semantic());

    this.setSize(newSize);
    return newSize;
  }
}
