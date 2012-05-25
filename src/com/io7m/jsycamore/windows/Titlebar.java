package com.io7m.jsycamore.windows;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.components.Label;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;

public final class Titlebar extends Component
{
  private final @Nonnull Point<ScreenRelative>                window_delta;
  private final @Nonnull Point<ScreenRelative>                window_start;
  private final @CheckForNull CloseBox                        close_box;
  private final @Nonnull Label                                label;
  private static final @Nonnull PointReadable<ParentRelative> LABEL_DEFAULT_OFFSET;

  static {
    LABEL_DEFAULT_OFFSET = new Point<ParentRelative>(4, 1);
  }

  public Titlebar(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String title,
    final boolean can_close)
    throws ConstraintError,
      GUIException
  {
    super(parent, position, size);

    this.window_delta = new Point<ScreenRelative>();
    this.window_start = new Point<ScreenRelative>();

    this.label =
      new Label(context, parent, Titlebar.LABEL_DEFAULT_OFFSET, size, title);
    assert this.label != null;

    if (can_close) {
      this.close_box =
        new CloseBox(context, this, PointConstants.PARENT_ORIGIN);
      this.close_box
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.close_box
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      ComponentAlignment.setPositionContainerTopRight(this.close_box, 4);
    } else {
      this.close_box = null;
    }
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

      VectorReadable3F color = null;
      if (window.windowIsFocused()) {
        color = theme.getFocusedWindowTitlebarTextColor();
      } else {
        color = theme.getUnfocusedWindowTitlebarTextColor();
      }

      this.label.labelSetColor3f(color.getXF(), color.getYF(), color.getZF());

      if (window.windowIsFocused()) {
        draw.renderRectangleFill(
          context,
          size,
          theme.getFocusedWindowTitlebarBackgroundColor());
        draw.renderRectangleEdge(
          context,
          size,
          theme.getWindowEdgeWidth(),
          theme.getFocusedWindowEdgeColor());
      } else {
        draw.renderRectangleFill(
          context,
          size,
          theme.getUnfocusedWindowTitlebarBackgroundColor());
        draw.renderRectangleEdge(
          context,
          size,
          theme.getWindowEdgeWidth(),
          theme.getUnfocusedWindowEdgeColor());
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
      final Point<ScreenRelative> window_pos = window.windowGetPosition();

      this.window_start.setXI(window_pos.getXI());
      this.window_start.setYI(window_pos.getYI());
      this.window_delta.setXI(0);
      this.window_delta.setYI(0);

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

      this.window_delta.setXI(this.window_start.getXI()
        + (mouse_position_current.getXI() - mouse_position_initial.getXI()));
      this.window_delta.setYI(this.window_start.getYI()
        + (mouse_position_current.getYI() - mouse_position_initial.getYI()));

      final Window window = this.componentGetWindow();
      assert window != null;
      window.windowSetPosition(this.window_delta);

      return true;
    }
    return false;
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
    builder.append("[Titlebar ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
