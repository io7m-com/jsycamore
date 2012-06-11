package com.io7m.jsycamore.components;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;

public abstract class AbstractDragButton extends Component implements
  ButtonListener,
  DragListener
{
  private boolean                              over;
  private boolean                              pressed;
  private final int                            edge_width = 1;
  private @CheckForNull VectorReadable3F       edge_color;
  private @CheckForNull VectorReadable3F       fill_color;
  private @CheckForNull ButtonListener         listener;
  private final @Nonnull Point<ScreenRelative> drag_start;
  private final @Nonnull Point<ScreenRelative> drag_delta_initial;
  private final @Nonnull Point<ScreenRelative> drag_delta_previous;
  private final @Nonnull Point<ScreenRelative> mouse_previous;
  private final @Nonnull Point<ParentRelative> position_initial;

  protected AbstractDragButton(
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(parent, position, size);
    this.drag_start = new Point<ScreenRelative>();
    this.drag_delta_initial = new Point<ScreenRelative>();
    this.drag_delta_previous = new Point<ScreenRelative>();
    this.mouse_previous = new Point<ScreenRelative>();
    this.position_initial = new Point<ParentRelative>(position);
  }

  protected AbstractDragButton(
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(position, size);
    this.drag_start = new Point<ScreenRelative>();
    this.drag_delta_initial = new Point<ScreenRelative>();
    this.drag_delta_previous = new Point<ScreenRelative>();
    this.mouse_previous = new Point<ScreenRelative>();
    this.position_initial = new Point<ParentRelative>(position);
  }

  public final @Nonnull VectorReadable3F buttonGetCurrentEdgeColor()
  {
    return this.edge_color;
  }

  public final int buttonGetCurrentEdgeWidth()
  {
    return this.edge_width;
  }

  public final @Nonnull VectorReadable3F buttonGetCurrentFillColor()
  {
    return this.fill_color;
  }

  public abstract void buttonRenderPost(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException;

  public abstract void buttonRenderPre(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException;

  @Override public final void componentRenderPostDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException
  {
    // Unused.
  }

  @Override public final void componentRenderPreDescendants(
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
        if (this.componentIsEnabled()) {
          if (this.over) {
            if (this.pressed) {
              this.edge_color = theme.getFocusedComponentActiveEdgeColor();
              this.fill_color =
                theme.getFocusedComponentActiveBackgroundColor();
            } else {
              this.edge_color = theme.getFocusedComponentOverEdgeColor();
              this.fill_color =
                theme.getFocusedComponentOverBackgroundColor();
            }
          } else {
            this.fill_color = theme.getFocusedComponentBackgroundColor();
            this.edge_color = theme.getFocusedComponentEdgeColor();
          }
        } else {
          this.fill_color =
            theme.getFocusedComponentDisabledBackgroundColor();
          this.edge_color = theme.getFocusedComponentDisabledEdgeColor();
        }
      } else {
        if (this.componentIsEnabled()) {
          this.fill_color = theme.getUnfocusedComponentBackgroundColor();
          this.edge_color = theme.getUnfocusedComponentEdgeColor();
        } else {
          this.fill_color =
            theme.getUnfocusedComponentDisabledBackgroundColor();
          this.edge_color = theme.getUnfocusedComponentDisabledEdgeColor();
        }
      }

      assert this.fill_color != null;
      assert this.edge_color != null;

      this.buttonRenderPre(context);

      draw.renderRectangleFill(context, size, this.fill_color);
      draw.renderRectangleEdge(
        context,
        size,
        this.edge_width,
        this.edge_color);

      this.buttonRenderPost(context);

    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  private final void dragBuffersInitialize(
    final @Nonnull PointReadable<ScreenRelative> mouse_position)
  {
    this.position_initial.setXI(this
      .componentGetPositionParentRelative()
      .getXI());
    this.position_initial.setYI(this
      .componentGetPositionParentRelative()
      .getYI());
    this.drag_start.setXI(mouse_position.getXI());
    this.drag_start.setYI(mouse_position.getYI());
  }

  private final void dragBuffersUpdate(
    final @Nonnull PointReadable<ScreenRelative> mouse_position)
  {
    this.drag_delta_previous.setXI(mouse_position.getXI()
      - this.mouse_previous.getXI());
    this.drag_delta_previous.setYI(mouse_position.getYI()
      - this.mouse_previous.getYI());

    this.mouse_previous.setXI(mouse_position.getXI());
    this.mouse_previous.setYI(mouse_position.getYI());

    this.drag_delta_initial.setXI(mouse_position.getXI()
      - this.drag_start.getXI());
    this.drag_delta_initial.setYI(mouse_position.getYI()
      - this.drag_start.getYI());
  }

  public final @Nonnull Point<ParentRelative> dragGetComponentInitial()
  {
    return this.position_initial;
  }

  public final @Nonnull
    PointReadable<ScreenRelative>
    dragGetDeltaFromInitial()
  {
    return this.drag_delta_initial;
  }

  public final @Nonnull
    PointReadable<ScreenRelative>
    dragGetDeltaFromPrevious()
  {
    return this.drag_delta_previous;
  }

  public final @Nonnull
    PointReadable<ScreenRelative>
    dragGetInitialPosition()
  {
    return this.drag_start;
  }

  @Override public final boolean mouseListenerOnMouseClicked(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    if (button == 0) {
      this.pressed = true;
      this.dragBuffersInitialize(mouse_position);
      this.dragBuffersUpdate(mouse_position);
      this.dragListenerOnStart(context, mouse_position, this);
    }
    return true;
  }

  @Override public final boolean mouseListenerOnMouseHeld(
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

      this.dragBuffersUpdate(mouse_position_current);
      this.dragListenerOnDrag(
        context,
        mouse_position_initial,
        mouse_position_current,
        this);
      return true;
    }
    return false;
  }

  @Override public final boolean mouseListenerOnMouseNoLongerOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position)
    throws ConstraintError,
      GUIException
  {
    this.over = false;
    this.pressed = false;
    return false;
  }

  @Override public final boolean mouseListenerOnMouseOver(
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

  @Override public final boolean mouseListenerOnMouseReleased(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    if (button == 0) {
      if (this.pressed) {
        try {
          this.dragBuffersUpdate(mouse_position);

          if (this.over) {
            this.buttonListenerOnClick(this);
            if (this.listener != null) {
              this.listener.buttonListenerOnClick(this);
            }
          }
          this.dragListenerOnRelease(
            context,
            this.drag_start,
            mouse_position,
            actual,
            this);
        } finally {
          this.position_initial.setXI(this
            .componentGetPositionParentRelative()
            .getXI());
          this.position_initial.setYI(this
            .componentGetPositionParentRelative()
            .getYI());
          this.pressed = false;
          this.over = false;
        }
      }

      this.pressed = false;
      return true;
    }
    return false;
  }

  public final void setButtonListener(
    final @Nonnull ButtonListener listener)
    throws ConstraintError
  {
    this.listener = Constraints.constrainNotNull(listener, "Listener");
  }
}
