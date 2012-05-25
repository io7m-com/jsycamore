package com.io7m.jsycamore.windows;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;

public final class ResizeBox extends Component
{
  private final @Nonnull VectorM2I        window_delta = new VectorM2I();
  private final @Nonnull VectorM2I        window_start = new VectorM2I();
  private static final @Nonnull VectorI2I DEFAULT_SIZE = new VectorI2I(8, 8);
  private boolean                         over;
  private boolean                         pressed;

  @SuppressWarnings("unused") public ResizeBox(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position)
    throws ConstraintError
  {
    super(parent, position, ResizeBox.DEFAULT_SIZE);
    this.over = false;
    this.pressed = false;
  }

  @Override public void componentRenderPostDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException
  {
    // Unused.
  }

  @Override public void componentRenderPreDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException
  {
    try {
      final DrawPrimitives draw = context.contextGetDrawPrimitives();
      final Theme theme = context.contextGetTheme();
      final VectorReadable2I size = this.componentGetSize();
      final Window window = this.componentGetWindow();
      assert window != null;

      if (window.windowIsFocused()) {
        if (this.over) {
          if (this.pressed) {
            draw.renderRectangleFill(
              context,
              size,
              theme.getFocusedComponentActiveBackgroundColor());
            draw.renderRectangleEdge(
              context,
              size,
              theme.getWindowEdgeWidth(),
              theme.getFocusedComponentActiveEdgeColor());
          } else {
            draw.renderRectangleFill(
              context,
              size,
              theme.getFocusedComponentOverBackgroundColor());
            draw.renderRectangleEdge(
              context,
              size,
              theme.getWindowEdgeWidth(),
              theme.getFocusedComponentOverEdgeColor());
          }
        } else {
          draw.renderRectangleFill(
            context,
            size,
            theme.getFocusedComponentBackgroundColor());
          draw.renderRectangleEdge(
            context,
            size,
            theme.getWindowEdgeWidth(),
            theme.getFocusedComponentEdgeColor());
        }
      } else {
        draw.renderRectangleFill(
          context,
          size,
          theme.getUnfocusedComponentBackgroundColor());
        draw.renderRectangleEdge(
          context,
          size,
          theme.getWindowEdgeWidth(),
          theme.getUnfocusedComponentEdgeColor());
      }
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  @Override public boolean mouseListenerOnMouseClicked(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    if (button == 0) {
      final Log log = context.contextGetComponentLog();
      log.debug("clicked");

      final Window window = this.componentGetWindow();
      assert window != null;

      VectorM2I.copy(window.windowGetSize(), this.window_start);
      VectorM2I.copy(VectorI2I.ZERO, this.window_delta);

      this.pressed = true;
      return true;
    }
    return false;
  }

  @Override public boolean mouseListenerOnMouseHeld(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position_initial,
    final @Nonnull PointReadable<ScreenRelative> mouse_position_current,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    if (button == 0) {
      final Log log = context.contextGetComponentLog();
      log.debug("held initial " + mouse_position_initial);
      log.debug("held current " + mouse_position_current);

      this.window_delta.x =
        this.window_start.x
          + (mouse_position_current.getXI() - mouse_position_initial.getXI());
      this.window_delta.y =
        this.window_start.y
          + (mouse_position_current.getYI() - mouse_position_initial.getYI());

      final Window window = this.componentGetWindow();
      assert window != null;
      window.windowSetSize(context, this.window_delta);
      return true;
    }
    return false;
  }

  @Override public boolean mouseListenerOnMouseNoLongerOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position)
    throws ConstraintError,
      GUIException
  {
    this.over = false;
    return false;
  }

  @Override public boolean mouseListenerOnMouseOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    this.over = true;
    return true;
  }

  @Override public boolean mouseListenerOnMouseReleased(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    if (button == 0) {
      final Log log = context.contextGetComponentLog();
      log.debug("released");
      this.pressed = false;
      return true;
    }
    return false;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    // Unused.
  }

  @Override public boolean resourceIsDeleted()
  {
    return true;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ResizeBox ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
