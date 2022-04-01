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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.components.SyComponentQuery;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowFocusGained;
import com.io7m.jsycamore.api.windows.SyWindowFocusLost;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.api.components.SyComponentQuery.FIND_FOR_MOUSE_CURSOR;

/**
 * A screen.
 */

public final class SyScreen implements SyScreenType
{
  private final SyThemeType theme;
  private final EnumMap<SyMouseButton, MouseState> mouseButtonStates;
  private final AttributeType<PAreaSizeI<SySpaceViewportType>> viewportSize;
  private SyWindowSet windows;
  private Optional<SyComponentType> componentOver;

  /**
   * A screen.
   *
   * @param inTheme The theme
   * @param inSize  The screen size
   */

  public SyScreen(
    final SyThemeType inTheme,
    final PAreaSizeI<SySpaceViewportType> inSize)
  {
    final var attributes = SyWindowAttributes.get();

    this.theme =
      Objects.requireNonNull(inTheme, "theme");
    this.viewportSize =
      attributes.create(
        Objects.requireNonNull(inSize, "viewportSize"));

    this.mouseButtonStates =
      new EnumMap<>(SyMouseButton.class);
    this.componentOver =
      Optional.empty();
    this.windows =
      SyWindowSet.empty();
  }

  @Override
  public SyThemeType theme()
  {
    return this.theme;
  }

  @Override
  public Optional<SyComponentType> componentOver()
  {
    return this.componentOver;
  }

  @Override
  public SyWindowType windowCreate(
    final int sizeX,
    final int sizeY)
  {
    final var window = new SyWindow(this, PAreaSizeI.of(sizeX, sizeY));
    this.processWindowChange(this.windows.windowOpen(window));
    return window;
  }

  @Override
  public List<SyWindowType> windowsOpenOrderedNow()
  {
    return this.windows.windowsOpenOrdered();
  }

  @Override
  public Optional<SyComponentType> mouseMoved(
    final PVector2I<SySpaceViewportType> position)
  {
    Objects.requireNonNull(position, "Position");

    /*
     * If the mouse button is down, the selected component is delivered a
     * "mouse held" event. Otherwise, the component under the cursor is
     * delivered a "mouse over" event.
     */

    if (this.mouseAnyButtonsAreDown()) {
      final var entries =
        this.mouseButtonStates.entrySet();
      final var iter =
        entries.iterator();

      while (iter.hasNext()) {
        final var entry = iter.next();
        final var state = entry.getValue();

        if (state.state == MouseButtonState.MOUSE_STATE_DOWN) {
          state.componentClickedLast.ifPresent(component -> {
            component.eventSend(new SyMouseEventOnHeld(
              state.positionClickedLast,
              position,
              entry.getKey(),
              component
            ));
          });
        }
      }

      return this.componentOver;
    }

    /*
     * The mouse button is up. Deliver "no longer over" events to the
     * relevant components.
     */

    final var currentOpt =
      this.componentForPosition(position, FIND_FOR_MOUSE_CURSOR);

    /*
     * If the cursor is currently over a component...
     */

    if (currentOpt.isPresent()) {
      final var current = currentOpt.get();

      /*
       * If the cursor was previously over a component, and that component
       * is not the same component as the current one, notify the previous
       * component that the cursor is no longer over it.
       */

      if (this.componentOver.isPresent()) {
        final var previous = this.componentOver.get();
        if (!Objects.equals(previous, current)) {
          this.onMouseMovedNotifyPreviousNoLongerOver();
        }
      }

      /*
       * Tell the current component that the cursor is over it.
       */

      this.componentOver = currentOpt;
      current.eventSend(new SyMouseEventOnOver(position, current));
      return this.componentOver;
    }

    this.onMouseMovedNotifyPreviousNoLongerOver();
    return this.componentOver;
  }

  private void onMouseMovedNotifyPreviousNoLongerOver()
  {
    if (this.componentOver.isPresent()) {
      final var previous = this.componentOver.get();
      previous.eventSend(new SyMouseEventOnNoLongerOver());
      this.componentOver = Optional.empty();
    }
  }

  private Optional<SyComponentType> componentForPosition(
    final PVector2I<SySpaceViewportType> position,
    final SyComponentQuery query)
  {
    Objects.requireNonNull(position, "Position");
    Objects.requireNonNull(query, "query");

    final var windowIterator =
      this.windows.windowsOpenOrdered().iterator();

    while (windowIterator.hasNext()) {
      final var window = windowIterator.next();
      final var component =
        window.componentForViewportPosition(position, query);

      if (component.isPresent()) {
        return component;
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<SyComponentType> mouseDown(
    final PVector2I<SySpaceViewportType> position,
    final SyMouseButton button)
  {
    Objects.requireNonNull(position, "Position");
    Objects.requireNonNull(button, "Button");

    /*
     * Find out which component the mouse cursor is over, if any.
     */

    final var componentOpt =
      this.componentForPosition(position, FIND_FOR_MOUSE_CURSOR);

    if (componentOpt.isEmpty()) {
      return Optional.empty();
    }

    /*
     * Focus the window.
     */

    final var component = componentOpt.get();
    final var windowOpt = component.window();

    Preconditions.checkPrecondition(
      windowOpt.isPresent(),
      "Component must be attached to window to receive events"
    );

    final var window = windowOpt.get();
    this.processWindowChange(this.windows.windowFocus(window));

    /*
     * If the mouse was previously up, then the mouse is now being clicked.
     * Keep a reference to the clicked component, and send it a
     * "mouse clicked" event.
     */

    final var state = this.mouseGetState(button);
    return switch (state.state) {
      case MOUSE_STATE_UP -> {

        /*
         * Deliver a "mouse was pressed" event to the component.
         */

        state.state = MouseButtonState.MOUSE_STATE_DOWN;
        state.componentClickedLast = componentOpt;
        state.positionClickedLast = position;

        component.eventSend(
          new SyMouseEventOnPressed(position, button, component));

        yield state.componentClickedLast;
      }

      case MOUSE_STATE_DOWN -> Optional.empty();
    };
  }

  private void processWindowChange(
    final SyWindowSetChanged change)
  {
    this.windows = change.newSet();
    change.focusLost()
      .ifPresent(w -> w.eventSend(new SyWindowFocusLost()));
    change.focusGained()
      .ifPresent(w -> w.eventSend(new SyWindowFocusGained()));
  }

  @Override
  public boolean windowIsFocused(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    return Objects.equals(this.windows.windowFocused(), Optional.of(window));
  }

  @Override
  public boolean windowIsOpen(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    return this.windows.windowIsOpen(window);
  }

  @Override
  public void windowOpen(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    this.processWindowChange(this.windows.windowOpen(window));
  }

  @Override
  public void windowClose(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    this.processWindowChange(this.windows.windowClose(window));
  }

  @Override
  public void windowFocus(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    this.processWindowChange(this.windows.windowFocus(window));
  }

  @Override
  public void windowMaximize(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    window.setMaximizeToggle(this.viewportSize.get());
  }

  private MouseState mouseGetState(
    final SyMouseButton button)
  {
    final var state = this.mouseButtonStates.get(button);
    if (state == null) {
      final var mouseState = new MouseState();
      this.mouseButtonStates.put(button, mouseState);
      return mouseState;
    }
    return state;
  }

  @Override
  public Optional<SyComponentType> mouseUp(
    final PVector2I<SySpaceViewportType> position,
    final SyMouseButton button)
  {
    Objects.requireNonNull(position, "Position");
    Objects.requireNonNull(button, "Button");

    final var state = this.mouseGetState(button);

    /*
     * If the mouse button was previously down, then the mouse button is now
     * being released. The component that was originally clicked receives a
     * "mouse released" event. The mouse is assumed to have moved on "release"
     * in order to deliver an "over" event, if any, to any relevant component.
     */

    return switch (state.state) {
      case MOUSE_STATE_UP -> {
        yield Optional.empty();
      }

      case MOUSE_STATE_DOWN -> {
        state.state = MouseButtonState.MOUSE_STATE_UP;

        if (state.componentClickedLast.isPresent()) {
          final var component = state.componentClickedLast.get();

          final var windowOpt = component.window();
          if (windowOpt.isPresent()) {
            component.eventSend(
              new SyMouseEventOnReleased(position, button, component)
            );
          }
        }

        this.mouseMoved(position);
        yield state.componentClickedLast;
      }
    };
  }

  private boolean mouseAnyButtonsAreDown()
  {
    final var entries =
      this.mouseButtonStates.entrySet();
    final var iterator =
      entries.iterator();

    while (iterator.hasNext()) {
      final var entry = iterator.next();
      final var state = entry.getValue();
      if (MouseButtonState.MOUSE_STATE_DOWN == state.state) {
        return true;
      }
    }

    return false;
  }

  @Override
  public AttributeType<PAreaSizeI<SySpaceViewportType>> size()
  {
    return this.viewportSize;
  }

  private enum MouseButtonState
  {
    MOUSE_STATE_UP,
    MOUSE_STATE_DOWN
  }

  private static final class MouseState
  {
    private PVector2I<SySpaceViewportType> positionClickedLast;
    private MouseButtonState state;
    private Optional<SyComponentType> componentClickedLast;

    MouseState()
    {
      this.componentClickedLast = Optional.empty();
      this.state = MouseButtonState.MOUSE_STATE_UP;
    }
  }
}
