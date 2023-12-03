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

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyTextViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyAlign;
import com.io7m.jsycamore.components.standard.SyAlignmentHorizontal;
import com.io7m.jsycamore.components.standard.SyAlignmentVertical;
import com.io7m.jsycamore.components.standard.SyLayoutMargin;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.WINDOW_TITLE_TEXT;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_TITLE;
import static com.io7m.jsycamore.components.standard.text.SyTextView.textView;

/**
 * A window title component.
 */

public final class SyWindowTitle
  extends SyWindowComponent
  implements SyButtonReadableType
{
  private final SyTextViewType text;
  private final SyAlign align;
  private final SyLayoutMargin margin;
  private PVector2I<SySpaceViewportType> windowStart;
  private boolean pressed;

  SyWindowTitle(final SyScreenType screen)
  {
    super(screen, WINDOW_TITLE, List.of());
    this.windowStart = PVectors2I.zero();

    this.margin = new SyLayoutMargin(screen);
    this.margin.setPaddingAll(3);
    this.margin.setMouseQueryAccepting(false);

    this.align = new SyAlign(screen);
    this.align.alignmentHorizontal()
      .set(SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER);
    this.align.alignmentVertical()
      .set(SyAlignmentVertical.ALIGN_VERTICAL_CENTER);
    this.align.setMouseQueryAccepting(false);

    this.text = textView(screen, List.of(WINDOW_TITLE_TEXT));
    this.text.setMouseQueryAccepting(false);

    this.align.childAdd(this.text);
    this.margin.childAdd(this.align);
    this.childAdd(this.margin);
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    if (event instanceof final SyMouseEventType mouseEvent) {
      return this.onMouseEvent(mouseEvent);
    }

    return EVENT_NOT_CONSUMED;
  }

  private SyEventConsumed onMouseEvent(
    final SyMouseEventType event)
  {
    if (event instanceof final SyMouseEventOnPressed onPressed) {
      return switch (onPressed.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.pressed = true;
          this.windowStart =
            this.window()
              .orElseThrow(UnreachableCodeException::new)
              .position()
              .get();
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> EVENT_NOT_CONSUMED;
      };
    }

    if (event instanceof final SyMouseEventOnReleased onReleased) {
      return switch (onReleased.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.pressed = false;
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> EVENT_NOT_CONSUMED;
      };
    }

    if (event instanceof final SyMouseEventOnHeld onHeld) {
      final var delta =
        PVectors2I.subtract(
          onHeld.mousePositionNow(),
          onHeld.mousePositionFirst()
        );

      final var newPosition =
        PVectors2I.add(this.windowStart, delta);

      this.window()
        .orElseThrow(UnreachableCodeException::new)
        .setPosition(newPosition);

      return EVENT_CONSUMED;
    }

    return EVENT_NOT_CONSUMED;
  }

  @Override
  public boolean isPressed()
  {
    return this.pressed;
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SyThemeClassNameStandard.WINDOW_TITLE, BUTTON);
  }

  AttributeType<SyText> titleText()
  {
    return this.text.text();
  }
}
