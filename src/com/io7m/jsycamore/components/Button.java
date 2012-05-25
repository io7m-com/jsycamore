package com.io7m.jsycamore.components;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorReadable2I;

public final class Button extends Component
{
  private boolean                      over;
  private boolean                      pressed;
  private final @Nonnull Label         label;
  private @CheckForNull ButtonListener listener;

  public Button(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String text)
    throws ConstraintError,
      GUIException
  {
    super(parent, position, size);

    this.label =
      new Label(context, this, PointConstants.PARENT_ORIGIN, size, text);
    ComponentAlignment.setPositionContainerCenter(this.label);
  }

  public Button(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String text)
    throws ConstraintError,
      GUIException
  {
    super(position, size);

    this.label =
      new Label(context, this, PointConstants.PARENT_ORIGIN, size, text);
    ComponentAlignment.setPositionContainerCenter(this.label);
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
    this.pressed = false;
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
    this.pressed = false;
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
        if (this.listener != null) {
          this.listener.onClick(this);
          this.pressed = false;
          this.over = false;
        }
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
    // TODO Auto-generated method stub
  }

  @Override public boolean resourceIsDeleted()
  {
    // TODO Auto-generated method stub
    return false;
  }

  public void setButtonListener(
    final @Nonnull ButtonListener listener)
    throws ConstraintError
  {
    this.listener = Constraints.constrainNotNull(listener, "Listener");
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Button ");
    builder.append(this.label);
    builder.append("]");
    return builder.toString();
  }
}
