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

package com.io7m.jsycamore.components.standard;

import com.io7m.jsycamore.api.components.SyButtonType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import org.osgi.annotation.versioning.ProviderType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.api.components.SyComponentQuery.FIND_FOR_MOUSE_CURSOR;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A convenient abstract implementation of a button, to make it easier to
 * implement new buttons.
 */

@ProviderType
public abstract class SyButtonAbstract
  extends SyComponentAbstract implements SyButtonType
{
  private boolean pressed;
  private Runnable listener;

  protected SyButtonAbstract(
    final List<SyThemeClassNameType> themeClassesExtra)
  {
    super(themeClassesExtra);
    this.listener = () -> {
    };
  }

  @Override
  public final void removeOnClickListener()
  {
    this.listener = () -> {
    };
  }

  @Override
  public final void setOnClickListener(
    final Runnable runnable)
  {
    this.listener = Objects.requireNonNull(runnable, "runnable");
  }

  @Override
  protected final SyEventConsumed onEvent(
    final SyEventType event)
  {
    if (event instanceof SyMouseEventType mouseEvent) {
      return this.onMouseEvent(mouseEvent);
    }
    return this.onOtherEvent(event);
  }

  @Override
  public final boolean isPressed()
  {
    return this.pressed;
  }

  /**
   * Receive an event on this particular component. If this method returns
   * {@code false}, the event is passed to the parent of this component.
   *
   * @param event The event
   *
   * @return {@code true} if the event has been consumed
   */

  protected abstract SyEventConsumed onOtherEvent(SyEventType event);

  private SyEventConsumed onMouseEvent(
    final SyMouseEventType event)
  {
    /*
     * If the mouse is pressed, start tracking the "pressed" state.
     */

    if (event instanceof SyMouseEventOnPressed onPressed) {
      this.setMouseOver(true);
      return switch (onPressed.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.pressed = true;
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_MIDDLE,
          MOUSE_BUTTON_RIGHT -> {
          yield EVENT_NOT_CONSUMED;
        }
      };
    }

    /*
     * Buttons should only be triggered if the mouse button is released when
     * the cursor is still over the button.
     */

    if (event instanceof SyMouseEventOnHeld onHeld) {
      return switch (onHeld.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.window()
            .flatMap(window -> {
              return window.componentForViewportPosition(
                onHeld.mousePositionNow(), FIND_FOR_MOUSE_CURSOR);
            })
            .flatMap(component -> {
              this.setMouseOver(Objects.equals(component, this));
              return Optional.of(component);
            })
            .orElseGet(() -> {
              this.setMouseOver(false);
              return null;
            });
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
          yield EVENT_NOT_CONSUMED;
        }
      };
    }

    if (event instanceof SyMouseEventOnReleased onReleased) {
      return switch (onReleased.button()) {
        case MOUSE_BUTTON_LEFT -> {

          /*
           * Only trigger the button if the cursor is still over the button
           * when the mouse button is released.
           */

          if (this.pressed && this.isMouseOver()) {
            try {
              this.onClicked();
              this.listener.run();
            } finally {

              /*
               * Both "pressed" and "over" are cancelled here because if the
               * button is moved for any reason by the result of pressing the
               * button, there will not be a "mouse no longer" over event
               * delivered to the button.
               */

              this.pressed = false;
              this.setMouseOver(false);
            }
          }

          this.pressed = false;
          yield EVENT_CONSUMED;
        }

        case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
          yield EVENT_NOT_CONSUMED;
        }
      };
    }

    return this.onOtherEvent(event);
  }

  protected abstract void onClicked();
}
