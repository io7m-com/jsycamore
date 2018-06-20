/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jsycamore.api.components;

import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * An abstract implementation of the {@link SyButtonType} interface.
 */

@NotThreadSafe
public abstract class SyButtonRepeatingAbstract extends SyComponentAbstract implements
  SyButtonType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyButtonRepeatingAbstract.class);
  }

  private final List<SyButtonListenerType> listeners;
  private boolean pressed;
  private boolean over;

  protected SyButtonRepeatingAbstract(final BooleanSupplier in_detach_check)
  {
    super(in_detach_check);
    this.listeners = new ArrayList<>(4);
  }

  protected final boolean isPressed()
  {
    return this.pressed;
  }

  protected final boolean isOver()
  {
    return this.over;
  }

  @Override
  public final void buttonAddListener(final SyButtonListenerType r)
  {
    this.listeners.add(Objects.requireNonNull(r, "Button listener"));
  }

  @Override
  public final void buttonRemoveListener(final SyButtonListenerType r)
  {
    this.listeners.remove(Objects.requireNonNull(r, "Button listener"));
  }

  @Override
  public final SyButtonState buttonState()
  {
    if (this.pressed) {
      return SyButtonState.BUTTON_PRESSED;
    }

    if (this.over) {
      return SyButtonState.BUTTON_OVER;
    }

    return SyButtonState.BUTTON_ACTIVE;
  }

  @Override
  public final boolean mouseHeld(
    final PVector2I<SySpaceViewportType> mouse_position_first,
    final PVector2I<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    Objects.requireNonNull(mouse_position_first, "Mouse first position");
    Objects.requireNonNull(mouse_position_now, "Mouse current position");
    Objects.requireNonNull(button, "Mouse button");
    Objects.requireNonNull(actual, "Component");

    if (LOG.isTraceEnabled()) {
      LOG.trace(
        "mouseHeld: {} {} {} {}",
        mouse_position_first,
        mouse_position_now,
        button,
        actual);
    }

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.window()
          .flatMap(window -> window.componentForViewportPosition(
            mouse_position_now))
          .flatMap(component -> {
            this.over = Objects.equals(component, this);
            return Optional.empty();
          });
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        break;
      }
    }

    return false;
  }

  @Override
  public final boolean mousePressed(
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    Objects.requireNonNull(mouse_position, "Mouse position");
    Objects.requireNonNull(button, "Mouse button");
    Objects.requireNonNull(actual, "Component");

    if (LOG.isTraceEnabled()) {
      LOG.trace(
        "mousePressed: {} {} {}", mouse_position, button, actual);
    }

    this.over = true;
    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.pressed = true;
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  @Override
  public final boolean mouseReleased(
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    Objects.requireNonNull(mouse_position, "Mouse position");
    Objects.requireNonNull(button, "Mouse button");
    Objects.requireNonNull(actual, "Component");

    if (LOG.isTraceEnabled()) {
      LOG.trace("mouseReleased: {} {} {}", mouse_position, button, actual);
    }

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        if (this.pressed && this.over) {
          try {
            this.clicked();
          } finally {

            /*
             * Both "pressed" and "over" are cancelled here because if the
             * button is moved for any reason by the result of pressing the
             * button, there will not be a "mouse no longer" over event
             * delivered to the button. A concrete example of this is when
             * the button represents a window close box: The window will be
             * closed and therefore the button will not receive any subsequent
             * mouse events after the button is pressed.
             */

            this.pressed = false;
            this.over = false;
          }
        }

        this.pressed = false;
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  protected void buttonOnClick()
  {

  }

  private void clicked()
  {
    try {
      this.buttonOnClick();
    } catch (final Throwable e) {
      SyErrors.ignoreNonErrors(LOG, e);
    }

    for (final SyButtonListenerType x : this.listeners) {
      try {
        x.buttonClicked(this);
      } catch (final Throwable e) {
        SyErrors.ignoreNonErrors(LOG, e);
      }
    }
  }

  @Override
  public final boolean mouseNoLongerOver()
  {
    LOG.trace("mouseNoLongerOver");
    this.over = false;
    this.pressed = false;
    return true;
  }

  @Override
  public final boolean mouseOver(
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {
    Objects.requireNonNull(mouse_position, "Mouse position");
    Objects.requireNonNull(actual, "Component");

    if (LOG.isTraceEnabled()) {
      LOG.trace("mouseOver: {} {}", mouse_position, actual);
    }

    this.over = true;
    this.pressed = false;
    return true;
  }
}
