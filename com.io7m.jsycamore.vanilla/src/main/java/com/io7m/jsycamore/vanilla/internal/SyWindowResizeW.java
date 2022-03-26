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
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.List;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_W;

/**
 * A west resize button.
 */

public final class SyWindowResizeW extends SyWindowComponent
{
  private PVector2I<SySpaceViewportType> windowStartPosition;
  private PAreaSizeI<SySpaceViewportType> windowStartSize;

  SyWindowResizeW()
  {
    super(WINDOW_RESIZE_W);
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
    if (event instanceof SyMouseEventOnOver) {
      this.setMouseOver(true);
      return true;
    }

    if (event instanceof SyMouseEventOnNoLongerOver) {
      this.setMouseOver(false);
      return true;
    }

    if (event instanceof SyMouseEventOnPressed onPressed) {
      return switch (onPressed.button()) {
        case MOUSE_BUTTON_LEFT -> {
          final var window =
            this.window().orElseThrow(UnreachableCodeException::new);

          this.windowStartPosition = window.position().get();
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

      final var newPosition =
        PVector2I.<SySpaceViewportType>of(
          this.windowStartPosition.x() + deltaX,
          this.windowStartPosition.y()
        );

      final var newSize =
        PAreaSizeI.<SySpaceViewportType>of(
          this.windowStartSize.sizeX() - deltaX,
          this.windowStartSize.sizeY()
        );

      final var window =
        this.window().orElseThrow(UnreachableCodeException::new);

      window.setSize(newSize);
      window.setPosition(newPosition);
      return true;
    }

    return false;
  }

  @Override
  public List<SyThemeClassNameType> themeClassesInPreferenceOrder()
  {
    return List.of(SyThemeClassNameStandard.WINDOW_RESIZE_W, BUTTON);
  }
}
