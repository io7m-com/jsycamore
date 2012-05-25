package com.io7m.jsycamore.windows;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
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
import com.io7m.jtensors.VectorReadable2I;

public final class CloseBox extends Component
{
  private boolean                pressed;
  private boolean                over;
  private static final VectorI2I DEFAULT_SIZE = new VectorI2I(8, 8);

  @SuppressWarnings("unused") public CloseBox(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position)
    throws ConstraintError
  {
    super(parent, position, CloseBox.DEFAULT_SIZE);
    this.pressed = false;
    this.over = false;
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
      this.pressed = true;
    }
    return true;
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
      this.over =
        this.componentContainsScreenRelativePoint(mouse_position_current);
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
      if (this.pressed && this.over) {
        this.componentSetWindowClosing();
      }

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
    builder.append("[CloseBox ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
