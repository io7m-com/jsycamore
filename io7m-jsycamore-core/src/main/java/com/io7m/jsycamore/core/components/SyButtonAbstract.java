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

package com.io7m.jsycamore.core.components;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
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
public abstract class SyButtonAbstract extends SyComponentAbstract implements
  SyButtonType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyButtonAbstract.class);
  }

  private final List<SyButtonListenerType> listeners;
  private boolean pressed;
  private boolean over;

  protected SyButtonAbstract(final BooleanSupplier in_detach_check)
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
    this.listeners.add(NullCheck.notNull(r));
  }

  @Override
  public final void buttonRemoveListener(final SyButtonListenerType r)
  {
    this.listeners.remove(NullCheck.notNull(r));
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
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position_first);
    NullCheck.notNull(mouse_position_now);
    NullCheck.notNull(button);
    NullCheck.notNull(actual);

    SyButtonAbstract.LOG.trace(
      "mouseHeld: {} {} {} {}",
      mouse_position_first,
      mouse_position_now,
      button,
      actual);

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        final Optional<SyWindowType> window_opt = this.window();
        return window_opt
          .flatMap(window -> window.componentForViewportPosition(
            mouse_position_now))
          .flatMap(component -> {
            this.over = Objects.equals(component, this);
            return Optional.empty();
          }).isPresent();
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
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position);
    NullCheck.notNull(button);
    NullCheck.notNull(actual);

    SyButtonAbstract.LOG.trace(
      "mousePressed: {} {} {}", mouse_position, button, actual);

    this.over = true;
    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.pressed = true;
        this.over = true;
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
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position);
    NullCheck.notNull(button);
    NullCheck.notNull(actual);

    SyButtonAbstract.LOG.trace(
      "mouseReleased: {} {} {} {}", mouse_position, button, actual);

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        if (this.pressed && this.over) {
          try {
            this.clicked();
          } finally {
            this.pressed = false;
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
      SyErrors.ignoreNonErrors(SyButtonAbstract.LOG, e);
    }

    for (final SyButtonListenerType x : this.listeners) {
      try {
        x.buttonClicked(this);
      } catch (final Throwable e) {
        SyErrors.ignoreNonErrors(SyButtonAbstract.LOG, e);
      }
    }
  }

  @Override
  public final boolean mouseNoLongerOver()
  {
    SyButtonAbstract.LOG.trace("mouseNoLongerOver");
    this.over = false;
    this.pressed = false;
    return true;
  }

  @Override
  public final boolean mouseOver(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position);
    NullCheck.notNull(actual);

    SyButtonAbstract.LOG.trace("mouseOver: {} {}", mouse_position, actual);
    this.over = true;
    this.pressed = false;
    return true;
  }
}
