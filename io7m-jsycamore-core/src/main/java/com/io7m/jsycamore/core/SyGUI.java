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

package com.io7m.jsycamore.core;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.provided.SyThemeDefault;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorWritable2IType;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The default implementation of the {@link SyGUIType} interface.
 */

@NotThreadSafe
public final class SyGUI implements SyGUIType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyGUI.class);
  }

  private final SyTextMeasurementType text_measurement;
  private final String name;
  private final Map<SyMouseButton, MouseState> mouse_button_states;
  private final Set<SyWindowType> windows_closed;
  private final Set<SyWindowType> windows_open;
  private final List<SyWindowType> windows_open_order;
  private final List<SyWindowType> windows_open_order_view;
  private Optional<SyComponentType> component_over;
  private SyThemeType theme;

  private SyGUI(
    final String in_name,
    final SyTextMeasurementType in_text_measurement,
    final SyThemeType in_theme)
  {
    this.name = NullCheck.notNull(in_name);
    this.text_measurement = NullCheck.notNull(in_text_measurement);
    this.theme = NullCheck.notNull(in_theme);

    this.component_over = Optional.empty();
    this.mouse_button_states = new EnumMap<>(SyMouseButton.class);
    this.windows_closed = new HashSet<>(32);
    this.windows_open = new HashSet<>(32);
    this.windows_open_order = new LinkedList<>();
    this.windows_open_order_view =
      Collections.unmodifiableList(this.windows_open_order);
  }

  /**
   * Create a new GUI.
   *
   * @param in_measure A text measurement interface
   * @param in_name    The GUI name, for debugging purposes
   *
   * @return A new GUI
   */

  public static SyGUIType create(
    final SyTextMeasurementType in_measure,
    final String in_name)
  {
    return SyGUI.createWithTheme(in_measure, in_name, SyThemeDefault.get());
  }

  /**
   * Create a new GUI.
   *
   * @param in_measure A text measurement interface
   * @param in_name    The GUI name, for debugging purposes
   * @param in_theme   The default theme
   *
   * @return A new GUI
   */

  public static SyGUIType createWithTheme(
    final SyTextMeasurementType in_measure,
    final String in_name,
    final SyThemeType in_theme)
  {
    return new SyGUI(in_name, in_measure, in_theme);
  }

  @Override
  public SyWindowType windowCreate(
    final int width,
    final int height,
    final String title)
  {
    NullCheck.notNull(title);

    final SyWindowType w = new Window(width, height, title);
    if (!this.windows_open_order.isEmpty()) {
      final SyWindowType current = this.windows_open_order.get(0);
      current.onWindowLosesFocus();
    }

    this.windows_open.add(w);
    this.windows_open_order.add(0, w);
    w.onWindowGainsFocus();
    return w;
  }

  @Override
  public List<SyWindowType> windowsOpenOrdered()
  {
    return this.windows_open_order_view;
  }

  private void checkGUI(final SyGUIElementType e)
  {
    NullCheck.notNull(e);
    if (!Objects.equals(e.gui(), this)) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Attempted to use a GUI element in the wrong GUI context.");
      sb.append(System.lineSeparator());
      sb.append("  Element:     ");
      sb.append(e);
      sb.append(System.lineSeparator());
      sb.append("  Element GUI: ");
      sb.append(e.gui());
      sb.append(System.lineSeparator());
      sb.append("  Current GUI: ");
      sb.append(this);
      sb.append(System.lineSeparator());
      throw new IllegalArgumentException(sb.toString());
    }
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[SyGUI ");
    sb.append(this.name);
    sb.append("]");
    return sb.toString();
  }

  @Override
  public boolean windowIsFocused(final SyWindowType w)
  {
    this.checkGUI(w);
    return !this.windows_open_order.isEmpty()
      && Objects.equals(this.windows_open_order.get(0), w);
  }

  @Override
  public void windowFocus(final SyWindowType w)
  {
    this.checkGUI(w);
    this.windowFocusActual(w);
  }

  @Override
  public SyThemeType theme()
  {
    return this.theme;
  }

  @Override
  public SyTextMeasurementType textMeasurement()
  {
    return this.text_measurement;
  }

  @Override
  public String name()
  {
    return this.name;
  }

  @Override
  public void setTheme(final SyThemeType in_theme)
  {
    this.theme = NullCheck.notNull(in_theme);

    for (final SyWindowType w : this.windows_open) {
      w.onWindowGUIThemeChanged();
    }

    for (final SyWindowType w : this.windows_closed) {
      w.onWindowGUIThemeChanged();
    }
  }

  private boolean mouseAnyButtonsAreDown()
  {
    final Set<Map.Entry<SyMouseButton, MouseState>> entries =
      this.mouse_button_states.entrySet();
    final Iterator<Map.Entry<SyMouseButton, MouseState>> iter =
      entries.iterator();

    while (iter.hasNext()) {
      final Map.Entry<SyMouseButton, MouseState> entry = iter.next();
      final MouseState state = entry.getValue();
      if (MouseButtonState.MOUSE_STATE_DOWN == state.state) {
        return true;
      }
    }

    return false;
  }

  @Override
  public Optional<SyComponentType> onMouseMoved(
    final PVectorReadable2IType<SySpaceViewportType> position)
  {
    NullCheck.notNull(position);

    /*
     * If the mouse button is down, the selected component is delivered a
     * "mouse held" event. Otherwise, the component under the cursor is
     * delivered a "mouse over" event.
     */

    if (this.mouseAnyButtonsAreDown()) {
      final Set<Map.Entry<SyMouseButton, MouseState>> entries =
        this.mouse_button_states.entrySet();
      final Iterator<Map.Entry<SyMouseButton, MouseState>> iter =
        entries.iterator();

      while (iter.hasNext()) {
        final Map.Entry<SyMouseButton, MouseState> entry = iter.next();
        final MouseState state = entry.getValue();

        if (state.state == MouseButtonState.MOUSE_STATE_DOWN) {
          if (state.component_clicked_last.isPresent()) {
            final SyComponentType component = state.component_clicked_last.get();
            SyGUI.LOG.trace("onMouseHeld: {}", component);
            component.onMouseHeld(
              state.position_clicked_last,
              position,
              entry.getKey(),
              component);
          }
        }
      }
    } else {

      final Optional<SyComponentType> component_opt =
        this.componentForPosition(position);

      if (component_opt.isPresent()) {
        final SyComponentType current = component_opt.get();
        if (this.component_over.isPresent()) {
          final SyComponentType previous = this.component_over.get();
          if (!Objects.equals(previous, current)) {
            SyGUI.LOG.trace("onMouseNoLongerOver: {}", previous);
            previous.onMouseNoLongerOver();
            this.component_over = Optional.empty();
          }
        }

        SyGUI.LOG.trace("onMouseOver: {}", current);
        this.component_over = component_opt;
        current.onMouseOver(position, current);
      } else {
        if (this.component_over.isPresent()) {
          final SyComponentType previous = this.component_over.get();
          SyGUI.LOG.trace("onMouseNoLongerOver: {}", previous);
          previous.onMouseNoLongerOver();
          this.component_over = Optional.empty();
        }
      }
    }

    return this.component_over;
  }

  @Override
  public Optional<SyComponentType> onMouseDown(
    final PVectorReadable2IType<SySpaceViewportType> position,
    final SyMouseButton button)
  {
    NullCheck.notNull(position);
    NullCheck.notNull(button);

    /**
     * Find out which component the mouse cursor is over, if any.
     */

    final Optional<SyComponentType> component_opt =
      this.componentForPosition(position);

    if (!component_opt.isPresent()) {
      return Optional.empty();
    }

    /**
     * Focus the window.
     */

    final SyComponentType component = component_opt.get();
    final Optional<SyWindowType> window_opt = component.window();
    Assertive.require(
      window_opt.isPresent(),
      "Component must be attached to window to receive events");
    final SyWindowType window = window_opt.get();
    this.windowFocusActual(window);

    /*
     * If the mouse was previously up, then the mouse is now being clicked.
     * Keep a reference to the clicked component, and send it a
     * "mouse clicked" event.
     */

    final MouseState state = this.mouseGetState(button);
    switch (state.state) {
      case MOUSE_STATE_UP: {

        /**
         * Deliver a "mouse was pressed" event to the component.
         */

        state.state = MouseButtonState.MOUSE_STATE_DOWN;
        state.component_clicked_last = component_opt;
        PVectorM2I.copy(position, state.position_clicked_last);

        SyGUI.LOG.trace("onMousePressed: {}", component);
        component.onMousePressed(position, button, component);
        return state.component_clicked_last;
      }

      case MOUSE_STATE_DOWN: {
        SyGUI.LOG.error("mouse button {} is already down", button);
        break;
      }
    }

    return Optional.empty();
  }

  private MouseState mouseGetState(final SyMouseButton button)
  {

    final MouseState state = this.mouse_button_states.get(button);
    if (state == null) {
      final MouseState mouse_state = new MouseState();
      this.mouse_button_states.put(button, mouse_state);
      return mouse_state;
    }

    return state;
  }

  private void windowFocusActual(final SyWindowType window)
  {
    SyGUI.LOG.debug("windowFocusActual: {}", window);

    Assertive.require(
      this.windows_open.contains(window),
      "The window must be open to receive focus");
    Assertive.require(
      !this.windows_closed.contains(window),
      "The window must not be both open and closed");

    final int index = this.windows_open_order.indexOf(window);
    Assertive.require(
      index >= 0,
      "The window must be present in the ordered window list");

    this.windows_open_order.remove(index);
    this.windows_open_order.add(0, window);
  }

  private Optional<SyComponentType> componentForPosition(
    final PVectorReadable2IType<SySpaceViewportType> position)
  {
    NullCheck.notNull(position);

    final Iterator<SyWindowType> window_iter =
      this.windows_open_order.iterator();

    while (window_iter.hasNext()) {
      final SyWindowType window = window_iter.next();
      final Optional<SyComponentType> component =
        window.componentForViewportPosition(position);

      if (SyGUI.LOG.isTraceEnabled()) {
        SyGUI.LOG.trace(
          "componentForPosition: {} {} {}",
          window, position, component);
      }

      if (component.isPresent()) {
        return component;
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<SyComponentType> onMouseUp(
    final PVectorReadable2IType<SySpaceViewportType> position,
    final SyMouseButton button)
  {
    NullCheck.notNull(position);
    NullCheck.notNull(button);

    final MouseState state = this.mouseGetState(button);

    /*
     * If the mouse button was previously down, then the mouse button is now
     * being released. The component that was originally clicked receives a
     * "mouse released" event. The mouse is assumed to have moved on "release"
     * in order to deliver an "over" event, if any, to any relevant component.
     */

    switch (state.state) {
      case MOUSE_STATE_UP: {
        SyGUI.LOG.error("mouse button {} is already up", button);
        break;
      }
      case MOUSE_STATE_DOWN: {
        state.state = MouseButtonState.MOUSE_STATE_UP;

        if (state.component_clicked_last.isPresent()) {
          final SyComponentType component = state.component_clicked_last.get();

          final Optional<SyWindowType> window_opt = component.window();
          if (window_opt.isPresent()) {
            final SyWindowType window = window_opt.get();
            final PVectorWritable2IType<SySpaceWindowRelativeType> w_position =
              new PVectorM2I<>();
            window.transformViewportRelative(position, w_position);
            SyGUI.LOG.trace("onMouseReleased: {}", component);
            component.onMouseReleased(position, button, component);
          } else {
            SyGUI.LOG.error("onMouseReleased: {} has no window", component);
          }
        }

        this.onMouseMoved(position);
        return state.component_clicked_last;
      }
    }

    return Optional.empty();
  }

  private enum MouseButtonState
  {
    MOUSE_STATE_UP,
    MOUSE_STATE_DOWN
  }

  private static final class MouseState
  {
    private final PVectorM2I<SySpaceViewportType> position_clicked_last;
    private MouseButtonState state;
    private Optional<SyComponentType> component_clicked_last;

    MouseState()
    {
      this.component_clicked_last = Optional.empty();
      this.position_clicked_last = new PVectorM2I<>();
      this.state = MouseButtonState.MOUSE_STATE_UP;
    }
  }

  private final class Window extends SyWindowAbstract
  {
    Window(
      final int width,
      final int height,
      final String in_text)
    {
      super(SyGUI.this, width, height, in_text);
    }
  }
}
