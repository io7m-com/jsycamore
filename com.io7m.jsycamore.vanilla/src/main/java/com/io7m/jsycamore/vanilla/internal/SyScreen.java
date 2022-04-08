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
import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentQuery;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.keyboard.SyKeyCode;
import com.io7m.jsycamore.api.keyboard.SyKeyModifier;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuClosed;
import com.io7m.jsycamore.api.menus.SyMenuHostType;
import com.io7m.jsycamore.api.menus.SyMenuItemType;
import com.io7m.jsycamore.api.menus.SyMenuOpened;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowMaximized;
import com.io7m.jsycamore.api.windows.SyWindowSet;
import com.io7m.jsycamore.api.windows.SyWindowSetChanged;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowUnmaximized;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jsycamore.components.standard.SyLayoutManual;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.io7m.jsycamore.api.components.SyComponentQuery.FIND_FOR_MOUSE_CURSOR;
import static com.io7m.jsycamore.api.windows.SyWindowDeletionPolicy.WINDOW_MAY_BE_DELETED;
import static com.io7m.jsycamore.api.windows.SyWindowDeletionPolicy.WINDOW_MAY_NOT_BE_DELETED;
import static com.io7m.jsycamore.api.windows.SyWindowLayer.WINDOW_LAYER_NORMAL;
import static com.io7m.jsycamore.api.windows.SyWindowLayer.WINDOW_LAYER_OVERLAY;

/**
 * A screen.
 */

public final class SyScreen implements SyScreenType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyScreen.class);

  private final SyLayoutManual windowMenuOverlayLayout;
  private final AtomicBoolean closed;
  private final AttributeType<PAreaSizeI<SySpaceViewportType>> viewportSize;
  private final AttributeType<PVector2I<SySpaceViewportType>> mousePosition;
  private final EnumMap<SyMouseButton, MouseState> mouseButtonStates;
  private final SubmissionPublisher<SyEventType> events;
  private final SyFontDirectoryType<? extends SyFontType> fonts;
  private final SyLayoutContextType layoutContext;
  private final SyThemeType theme;
  private final SyWindow windowMenuOverlay;
  private final AttributeType<PAreaSizeI<SySpaceViewportType>> sizeUpperLimit;
  private final EnumSet<SyKeyCode> keyStates;
  private final EnumSet<SyKeyModifier> keyModifierStates;
  private MenuTreeOpen menuTreeCurrentlyOpen;
  private Optional<SyComponentType> componentOver;
  private SyWindowSet windows;

  /**
   * A screen.
   *
   * @param inTheme The theme
   * @param inFonts The font directory
   * @param inSize  The screen size
   */

  public SyScreen(
    final SyThemeType inTheme,
    final SyFontDirectoryType<? extends SyFontType> inFonts,
    final PAreaSizeI<SySpaceViewportType> inSize)
  {
    final var attributes = SyComponentAttributes.get();

    this.theme =
      Objects.requireNonNull(inTheme, "theme");
    this.fonts =
      Objects.requireNonNull(inFonts, "inFonts");
    this.viewportSize =
      attributes.create(
        Objects.requireNonNull(inSize, "viewportSize"));
    this.mousePosition =
      attributes.create(PVectors2I.zero());
    this.sizeUpperLimit =
      attributes.create(PAreaSizeI.of(Integer.MAX_VALUE, Integer.MAX_VALUE));

    this.keyStates =
      EnumSet.noneOf(SyKeyCode.class);
    this.keyModifierStates =
      EnumSet.noneOf(SyKeyModifier.class);

    this.mouseButtonStates =
      new EnumMap<>(SyMouseButton.class);
    this.componentOver =
      Optional.empty();

    this.windowMenuOverlayLayout =
      new SyLayoutManual();

    this.windows = SyWindowSet.empty();
    this.windowMenuOverlay =
      new SyWindow(
        this,
        this.windows.windowFreshId(),
        WINDOW_LAYER_OVERLAY,
        WINDOW_MAY_NOT_BE_DELETED,
        inSize
      );
    this.windowMenuOverlay.title()
      .set("Menu Overlay");
    this.windowMenuOverlay.decorated()
      .set(false);
    this.windowMenuOverlay.contentArea()
      .childAdd(this.windowMenuOverlayLayout);

    this.windows =
      this.windows.windowCreate(this.windowMenuOverlay)
        .then(k -> k.windowShow(this.windowMenuOverlay))
        .newSet();

    this.events =
      new SubmissionPublisher<>(Runnable::run, Flow.defaultBufferSize());
    this.closed =
      new AtomicBoolean(false);
    this.layoutContext =
      new SyLayoutContext(this.fonts, this.theme);
  }

  @Override
  public AttributeType<PAreaSizeI<SySpaceViewportType>> sizeUpperLimit()
  {
    return this.sizeUpperLimit;
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
  public Flow.Publisher<SyEventType> events()
  {
    return this.events;
  }

  @Override
  public void update()
  {
    this.windows.windows()
      .values()
      .forEach(window -> window.layout(this.layoutContext));
  }

  @Override
  public AttributeReadableType<PVector2I<SySpaceViewportType>> mousePosition()
  {
    return this.mousePosition;
  }

  @Override
  public void menuOpen(
    final SyMenuType menu)
  {
    Objects.requireNonNull(menu, "menu");

    if (this.menuTreeCurrentlyOpen != null) {
      this.menuClose();
    }

    final var screenSize =
      this.viewportSize.get();

    final var screenConstraints =
      new SyConstraints(
        0,
        0,
        screenSize.sizeX(),
        screenSize.sizeY()
      );

    /*
     * Capture the list of menus that need to be opened. Organize the
     * list such that the root menu is opened first, followed by each of
     * the child submenus.
     */

    final var menusToOpen = new ArrayList<SyMenuType>(4);
    var currentMenu = menu;
    while (currentMenu != null) {
      menusToOpen.add(currentMenu);
      currentMenu = currentMenu.menuNode()
        .parent()
        .map(JOTreeNodeReadableType::value)
        .orElse(null);
    }
    Collections.reverse(menusToOpen);

    /*
     * Open each menu as a series of windows.
     */

    for (final var menuToOpen : menusToOpen) {
      menuToOpen.layout(this.layoutContext, screenConstraints);
      this.windowMenuOverlayLayout.childAdd(menuToOpen);
      menuToOpen.expanded().set(true);
      this.events.submit(new SyMenuOpened(menu));
    }

    this.menuTreeCurrentlyOpen = new MenuTreeOpen(menusToOpen);
  }

  @Override
  public void menuClose()
  {
    try {
      final var menusOpenNow = this.menuTreeCurrentlyOpen;
      if (menusOpenNow != null) {
        for (final var menuOpenNow : menusOpenNow.menusOpen) {
          menuOpenNow.node().detach();
          this.events.submit(new SyMenuClosed(menuOpenNow));
          menuOpenNow.expanded().set(false);
        }
      }
    } finally {
      this.menuTreeCurrentlyOpen = null;
    }
  }

  @Override
  public void close()
    throws RuntimeException
  {
    if (this.closed.compareAndSet(false, true)) {
      this.events.close();
    }
  }

  @Override
  public SyWindowType windowCreate(
    final int sizeX,
    final int sizeY)
  {
    final var window =
      new SyWindow(
        this,
        this.windows.windowFreshId(),
        WINDOW_LAYER_NORMAL,
        WINDOW_MAY_BE_DELETED,
        PAreaSizeI.of(sizeX, sizeY)
      );

    this.processWindowChange(this.windows.windowCreate(window));
    this.processWindowChange(this.windows.windowShow(window));
    return window;
  }

  @Override
  public List<SyWindowType> windowsVisibleOrdered()
  {
    return this.windows.windowsVisibleOrdered();
  }

  @Override
  public Optional<SyComponentType> mouseMoved(
    final PVector2I<SySpaceViewportType> position)
  {
    Objects.requireNonNull(position, "Position");

    this.mousePosition.set(position);

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
      this.windows.windowsVisibleOrdered().iterator();

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

    this.mousePosition.set(position);

    /*
     * Find out which component the mouse cursor is over, if any.
     */

    final var componentOpt =
      this.componentForPosition(position, FIND_FOR_MOUSE_CURSOR);

    if (componentOpt.isEmpty()) {
      this.onClickedNothing();
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

        this.onClickedSomething(component);
        yield state.componentClickedLast;
      }

      case MOUSE_STATE_DOWN -> Optional.empty();
    };
  }

  private void onClickedSomething(
    final SyComponentType component)
  {
    /*
     * If a menu is open, then we need to make some checks to determine if
     * the menu should be closed.
     */

    if (this.menuTreeCurrentlyOpen != null) {

      /*
       * The user clicked on a menu item, or something that hosts a menu
       * such as a menu bar item. This should not cause the menu to be closed;
       * it's up to those components to explicitly decide whether to
       * close the menu.
       */

      if (component instanceof SyMenuItemType) {
        return;
      }
      if (component instanceof SyMenuHostType) {
        return;
      }

      /*
       * The user may have clicked on something that lives inside a menu
       * item. This should not cause the menu to be closed.
       */

      final var menuAncestor =
        component.ancestorMatchingReadable(c -> c instanceof SyMenuItemType);

      if (menuAncestor.isPresent()) {
        return;
      }

      /*
       * The user may have clicked on a descendant of a menu host.
       * This should not cause the menu to be closed.
       */

      final var menuHostAncestor =
        component.ancestorMatchingReadable(c -> c instanceof SyMenuHostType);

      if (menuHostAncestor.isPresent()) {
        return;
      }

      /*
       * The user clicked on something else. Close the menu!
       */

      this.menuClose();
    }
  }

  private void onClickedNothing()
  {
    if (this.menuTreeCurrentlyOpen != null) {
      this.menuClose();
    }
  }

  private void processWindowChange(
    final SyWindowSetChanged change)
  {
    this.windows = change.newSet();

    final var windowMap = this.windows.windows();
    for (final var event : change.changes()) {
      final var window = windowMap.get(event.id());
      if (window != null) {
        window.eventSend(event);
      }
      this.events.submit(event);
    }
  }

  @Override
  public boolean windowIsFocused(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    return Objects.equals(
      this.windows.windowFocused(window.layer()), Optional.of(window));
  }

  @Override
  public boolean windowIsVisible(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    return this.windows.windowIsVisible(window);
  }

  @Override
  public void windowShow(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    this.processWindowChange(this.windows.windowShow(window));
  }

  @Override
  public void windowHide(
    final SyWindowType window)
  {
    Objects.requireNonNull(window, "window");
    this.processWindowChange(this.windows.windowHide(window));
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

    if (window.maximized().get()) {
      this.events.submit(new SyWindowMaximized(window.id()));
    } else {
      this.events.submit(new SyWindowUnmaximized(window.id()));
    }
  }

  @Override
  public SyWindowType windowMenu()
  {
    return this.windowMenuOverlay;
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

    this.mousePosition.set(position);

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

  @Override
  public void keyPressed(
    final SyKeyCode keyCode)
  {
    Objects.requireNonNull(keyCode, "keyCode");

    final var wasPressed = this.keyStates.contains(keyCode);
    this.keyStates.add(keyCode);
  }

  @Override
  public void keyReleased(
    final SyKeyCode keyCode)
  {
    Objects.requireNonNull(keyCode, "keyCode");

    final var wasPressed = this.keyStates.contains(keyCode);
    this.keyStates.remove(keyCode);
  }

  @Override
  public void keyModifierPressed(
    final SyKeyModifier keyModifier)
  {
    Objects.requireNonNull(keyModifier, "keyModifier");

    final var wasPressed = this.keyModifierStates.contains(keyModifier);
    this.keyModifierStates.add(keyModifier);
  }

  @Override
  public void keyModifierReleased(
    final SyKeyModifier keyModifier)
  {
    Objects.requireNonNull(keyModifier, "keyModifier");

    final var wasPressed = this.keyModifierStates.contains(keyModifier);
    this.keyModifierStates.remove(keyModifier);
  }

  private enum MouseButtonState
  {
    MOUSE_STATE_UP,
    MOUSE_STATE_DOWN
  }

  private static final class MenuTreeOpen
  {
    private final List<SyMenuType> menusOpen;

    private MenuTreeOpen(
      final List<SyMenuType> inMenusOpen)
    {
      this.menusOpen =
        Objects.requireNonNull(inMenusOpen, "menusOpen");
    }
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
