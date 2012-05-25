package com.io7m.jsycamore;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;

/**
 * Type representing an active GUI.
 */

public final class GUI
{
  /**
   * Mapping between mouse button indices and up/down states.
   */

  private static enum MouseButtonState
  {
    MOUSE_STATE_UP,
    MOUSE_STATE_DOWN
  }

  private static class MouseState
  {
    @Nonnull MouseButtonState            state;
    @CheckForNull Component              component_clicked_last;
    final @Nonnull Point<ScreenRelative> position_clicked_last;

    MouseState()
    {
      this.component_clicked_last = null;
      this.position_clicked_last = new Point<ScreenRelative>();
      this.state = MouseButtonState.MOUSE_STATE_UP;
    }
  }

  private final @Nonnull GUIContext                   context;
  private final @Nonnull Set<Window>                  windows_closed;
  private final @Nonnull Set<Window>                  windows_open;
  private final @Nonnull LinkedList<Window>           windows_open_order;
  private final @Nonnull HashMap<Integer, MouseState> mouse_button_states;
  private @CheckForNull Component                     component_over;

  public GUI(
    final @Nonnull Point<ScreenRelative> viewport_position,
    final @Nonnull VectorReadable2I viewport_size,
    final @Nonnull GLInterface gl,
    final @Nonnull FilesystemAPI fs,
    final @Nonnull Log log)
    throws ConstraintError,
      GUIException
  {
    try {
      final Log slog = new Log(log, "jsycamore");

      this.context =
        new GUIContext(gl, fs, slog, viewport_position, viewport_size);
      this.windows_open = new TreeSet<Window>();
      this.windows_closed = new TreeSet<Window>();
      this.windows_open_order = new LinkedList<Window>();
      this.mouse_button_states = new HashMap<Integer, GUI.MouseState>();
      this.component_over = null;
    } catch (final GLCompileException e) {
      throw new GUIException(e);
    } catch (final GLException e) {
      throw new GUIException(e);
    } catch (final FontFormatException e) {
      throw new GUIException(e);
    } catch (final IOException e) {
      throw new GUIException(e);
    } catch (final FilesystemError e) {
      throw new GUIException(e);
    }
  }

  /**
   * Retrieve the context associated with this GUI.
   */

  public @Nonnull GUIContext getContext()
  {
    return this.context;
  }

  private boolean mouseAnyButtonsAreDown()
  {
    for (final MouseState state : this.mouse_button_states.values()) {
      if (state.state == MouseButtonState.MOUSE_STATE_DOWN) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return the previous state of the current mouse button.
   * 
   * @param button
   *          The index of the given mouse button, 0 denotes the first button.
   * @return The button state.
   */

  private @Nonnull MouseState mouseCheckState(
    final @Nonnull Integer button)
  {
    final MouseState state = this.mouse_button_states.get(button);
    if (state == null) {
      final MouseState mouse_state = new MouseState();
      this.mouse_button_states.put(button, mouse_state);
      return mouse_state;
    }

    return state;
  }

  /**
   * Inform the GUI that the mouse button <code>button</code> has been pressed
   * and the cursor is at the screen-relative position <code>position<code>.
   * 
   * @param position
   *          The screen-relative cursor position.
   * @param button
   *          The index of the button.
   * @return The component under the cursor, if any.
   * @throws ConstraintError
   *           Iff any of the following conditions hold: <ul>
   *           <li><code>position == null</code></li> <li>
   *           <code>0 &lt;= button &lt;= Integer.MAX_VALUE</code></li> </ul>
   * @throws GUIException
   */

  public @CheckForNull Component mouseIsDown(
    final @Nonnull Point<ScreenRelative> position,
    final int button)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(position, "Position");
    Constraints.constrainRange(
      button,
      0,
      Integer.MAX_VALUE,
      "Mouse button index");

    final Log log = this.context.contextGetLog();
    log.debug("mouse-is-down: " + position + " " + button);

    final MouseState state = this.mouseCheckState(Integer.valueOf(button));
    final Window window = this.windowForPosition(position);

    if (window == null) {
      return null;
    }

    this.moveWindowToFront(window);

    /*
     * If the mouse was previously up, then the mouse is now being clicked.
     * Keep a reference to the clicked component, and send it a
     * "mouse clicked" event.
     */

    if (state.state == MouseButtonState.MOUSE_STATE_UP) {
      state.state = MouseButtonState.MOUSE_STATE_DOWN;
      state.component_clicked_last =
        window.windowGetComponentByScreenRelativePosition(position);
      state.position_clicked_last.setXI(position.getXI());
      state.position_clicked_last.setYI(position.getYI());

      if (state.component_clicked_last != null) {
        state.component_clicked_last.componentEventOnMouseClicked(
          this.context,
          position,
          button,
          state.component_clicked_last);
      }
      return state.component_clicked_last;
    }

    return null;
  }

  /**
   * Inform the GUI that the mouse button <code>button</code> has been
   * released and the cursor is at the screen-relative position
   * <code>position<code>.
   * 
   * @param position
   *          The screen-relative cursor position.
   * @param button
   *          The index of the button.
   * @return The component under the cursor, if any.
   * @throws ConstraintError
   *           Iff any of the following conditions hold: <ul>
   *           <li><code>position == null</code></li> <li>
   *           <code>0 &lt;= button &lt;= Integer.MAX_VALUE</code></li> </ul>
   * @throws GUIException
   */

  public @CheckForNull Component mouseIsUp(
    final @Nonnull Point<ScreenRelative> position,
    final int button)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(position, "Mouse position");
    Constraints.constrainRange(
      button,
      0,
      Integer.MAX_VALUE,
      "Mouse button index");

    final Log log = this.context.contextGetLog();
    log.debug("mouse-is-up: " + position + " " + button);

    final MouseState state = this.mouseCheckState(Integer.valueOf(button));

    /*
     * If the mouse button was previously down, then the mouse button is now
     * being released. The component that was originally clicked receives a
     * "mouse released" event. The mouse is assumed to have moved on "release"
     * in order to deliver an "over" event, if any, to any relevant component.
     */

    if (state.state == MouseButtonState.MOUSE_STATE_DOWN) {
      state.state = MouseButtonState.MOUSE_STATE_UP;
      assert (state.component_clicked_last != null);
      state.component_clicked_last.componentEventOnMouseReleased(
        this.context,
        position,
        button,
        state.component_clicked_last);
      this.mouseMoved(position);
      return state.component_clicked_last;
    }

    return null;
  }

  /**
   * Inform the GUI that the mouse has moved and the cursor is at the
   * screen-relative position <code>position<code>.
   * 
   * @param position
   *          The screen-relative cursor position.
   * 
   * @return The component under the cursor, if any.
   * @throws ConstraintError
   *           Iff any of the following conditions hold: <ul>
   *           <li><code>position == null</code></li> </ul>
   * @throws GUIException
   */

  public @CheckForNull Component mouseMoved(
    final @Nonnull Point<ScreenRelative> position)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(position, "Mouse position");

    /*
     * If the mouse button is down, the selected component is delivered a
     * "mouse held" event. Otherwise, the component under the cursor is
     * delivered a "mouse over" event.
     */

    if (this.mouseAnyButtonsAreDown()) {
      for (final Entry<Integer, MouseState> entry : this.mouse_button_states
        .entrySet()) {
        final MouseState state = entry.getValue();
        if (state.state == MouseButtonState.MOUSE_STATE_DOWN) {
          assert (state.component_clicked_last != null);
          state.component_clicked_last.componentEventOnMouseHeld(
            this.context,
            state.position_clicked_last,
            position,
            entry.getKey().intValue(),
            state.component_clicked_last);
        }
      }
    } else {
      final Window window = this.windowForPosition(position);
      if (window != null) {
        final Component over =
          window.windowGetComponentByScreenRelativePosition(position);

        if ((this.component_over != null) && (over != this.component_over)) {
          this.component_over.componentEventOnMouseNoLongerOver(
            this.context,
            position);
        }

        if (over != null) {
          this.component_over = over;
          over.componentEventOnMouseOver(this.context, position, over);
        }
      }
    }

    return this.component_over;
  }

  private void moveWindowToFront(
    final @Nonnull Window window)
  {
    this.windows_open_order.remove(window);
    this.windows_open_order.add(window);
    this.windowsUpdateFocus();
  }

  /**
   * Inform the GUI that it is now time to render.
   * 
   * @throws GUIException
   * @throws ConstraintError
   */

  public void render()
    throws GUIException,
      ConstraintError
  {
    try {
      final GLInterface g = this.context.contextGetGL();
      final VectorReadable2I vpp = this.context.contextGetViewportPosition();
      final VectorReadable2I vps = this.context.contextGetViewportSize();

      final MatrixM4x4F mv = this.context.contextGetMatrixModelview();
      MatrixM4x4F.setIdentity(mv);

      final MatrixM4x4F mp = this.context.contextGetMatrixProjection();
      MatrixM4x4F.setIdentity(mp);
      ProjectionMatrix.makeOrthographic(
        mp,
        0,
        vps.getXI(),
        vps.getYI(),
        0,
        1,
        100);

      g.viewportSet(vpp, vps);

      this.renderHandleClosingWindows();
      this.renderHandleOpeningWindows();
      this.windowsUpdateFocus();
      this.renderUpdateOpenWindows();

      MatrixM4x4F.setIdentity(mv);
      MatrixM4x4F.setIdentity(mp);
      ProjectionMatrix.makeOrthographic(
        mp,
        0,
        vps.getXI(),
        vps.getYI(),
        0,
        1,
        100);

      g.viewportSet(vpp, vps);

      this.renderOpenWindows();
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  private void renderHandleClosingWindows()
  {
    final Log log = this.context.contextGetRendererLog();
    log.debug("closing windows");

    final Iterator<Window> iter = this.windows_open.iterator();
    while (iter.hasNext()) {
      final Window w = iter.next();
      if (w.windowGetState() == WindowState.WINDOW_WANT_CLOSE) {
        this.windows_closed.add(w);
        iter.remove();
        this.windows_open_order.remove(w);
        w.windowSetState(WindowState.WINDOW_CLOSED);
      }
    }
  }

  private void renderHandleOpeningWindows()
  {
    final Log log = this.context.contextGetRendererLog();
    log.debug("opening windows");

    final Iterator<Window> iter = this.windows_closed.iterator();
    while (iter.hasNext()) {
      final Window w = iter.next();
      if (w.windowGetState() == WindowState.WINDOW_WANT_OPEN) {
        this.windows_open.add(w);
        this.windows_open_order.add(w);
        iter.remove();
        w.windowSetState(WindowState.WINDOW_OPEN);
      }
    }
  }

  private void renderOpenWindows()
    throws GUIException,
      ConstraintError
  {
    final Log log = this.context.contextGetRendererLog();
    log.debug("rendering windows");

    for (final Window w : this.windows_open_order) {
      w.windowRenderActual(this.context);
    }
  }

  private void renderUpdateOpenWindows()
    throws GUIException,
      ConstraintError
  {
    final Log log = this.context.contextGetRendererLog();
    log.debug("updating windows");

    for (final Window w : this.windows_open) {
      w.windowRenderContents(this.context);
    }
  }

  /**
   * Add the window <code>window</code> to the list of currently open windows
   * in the GUI.
   * 
   * @param window
   *          The window.
   * @throws ConstraintError
   *           Iff <code>window == null</code>.
   */

  public void windowAdd(
    final @Nonnull Window window)
    throws ConstraintError
  {
    Constraints.constrainNotNull(window, "Window");
    this.windows_open.add(window);
    this.windows_open_order.add(window);
    this.windowsUpdateFocus();
  }

  private @CheckForNull Window windowForPosition(
    final @Nonnull Point<ScreenRelative> position)
    throws ConstraintError
  {
    final Iterator<Window> iter =
      this.windows_open_order.descendingIterator();

    while (iter.hasNext()) {
      final Window w = iter.next();
      if (w.windowContainsScreenRelativePosition(position)) {
        return w;
      }
    }

    return null;
  }

  /**
   * Remove the window with ID <code>window_id</code> from the set of windows
   * in the GUI, if any.
   * 
   * @see Window#windowGetID()
   * @param window_id
   *          The ID of the window.
   * @throws ConstraintError
   *           Iff <code>window_id == null</code>.
   */

  public void windowRemove(
    final @Nonnull Long window_id)
    throws ConstraintError
  {
    Constraints.constrainNotNull(window_id, "Window ID");

    Window found = null;
    for (final Window w : this.windows_open) {
      if (w.windowGetID().equals(window_id)) {
        found = w;
        break;
      }
    }

    if (found != null) {
      this.windows_open.remove(found);
      this.windows_open_order.remove(found);
    }

    this.windowsUpdateFocus();
  }

  private void windowsUpdateFocus()
  {
    for (final Window w : this.windows_open_order) {
      w.windowSetState(WindowState.WINDOW_OPEN);
    }

    try {
      final Window w = this.windows_open_order.getLast();
      w.windowSetState(WindowState.WINDOW_FOCUSED);
    } catch (final NoSuchElementException e) {
      // Unused.
    }
  }
}
