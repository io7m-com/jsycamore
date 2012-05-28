package com.io7m.jsycamore.windows;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.Component.ParentResizeBehavior;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.components.Container;
import com.io7m.jsycamore.components.Label;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;

public final class StandardWindow extends Window
{
  private final class CloseBox extends Component
  {
    private boolean pressed;
    private boolean over;

    @SuppressWarnings("unused") public CloseBox(
      final @Nonnull GUIContext context,
      final @Nonnull Component parent,
      final @Nonnull PointReadable<ParentRelative> position)
      throws ConstraintError
    {
      super(parent, position, StandardWindow.CLOSE_BOX_SIZE);
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
              draw.renderLine(
                context,
                StandardWindow.CLOSE_BOX_X_TOP_LEFT,
                StandardWindow.CLOSE_BOX_X_BOTTOM_RIGHT,
                theme.getFocusedComponentEdgeColor());
              draw.renderLine(
                context,
                StandardWindow.CLOSE_BOX_X_BOTTOM_LEFT,
                StandardWindow.CLOSE_BOX_X_TOP_RIGHT,
                theme.getFocusedComponentEdgeColor());
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
              draw.renderLine(
                context,
                StandardWindow.CLOSE_BOX_X_TOP_LEFT,
                StandardWindow.CLOSE_BOX_X_BOTTOM_RIGHT,
                theme.getFocusedComponentEdgeColor());
              draw.renderLine(
                context,
                StandardWindow.CLOSE_BOX_X_BOTTOM_LEFT,
                StandardWindow.CLOSE_BOX_X_TOP_RIGHT,
                theme.getFocusedComponentEdgeColor());
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
            draw.renderLine(
              context,
              StandardWindow.CLOSE_BOX_X_TOP_LEFT,
              StandardWindow.CLOSE_BOX_X_BOTTOM_RIGHT,
              theme.getFocusedComponentEdgeColor());
            draw.renderLine(
              context,
              StandardWindow.CLOSE_BOX_X_BOTTOM_LEFT,
              StandardWindow.CLOSE_BOX_X_TOP_RIGHT,
              theme.getFocusedComponentEdgeColor());
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
          draw.renderLine(
            context,
            StandardWindow.CLOSE_BOX_X_TOP_LEFT,
            StandardWindow.CLOSE_BOX_X_BOTTOM_RIGHT,
            theme.getUnfocusedComponentEdgeColor());
          draw.renderLine(
            context,
            StandardWindow.CLOSE_BOX_X_BOTTOM_LEFT,
            StandardWindow.CLOSE_BOX_X_TOP_RIGHT,
            theme.getUnfocusedComponentEdgeColor());
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

  private final class ResizeBox extends Component
  {
    private final @Nonnull VectorM2I window_delta = new VectorM2I();
    private final @Nonnull VectorM2I window_start = new VectorM2I();
    private boolean                  over;
    private boolean                  pressed;

    @SuppressWarnings("unused") public ResizeBox(
      final @Nonnull GUIContext context,
      final @Nonnull Component parent,
      final @Nonnull PointReadable<ParentRelative> position)
      throws ConstraintError
    {
      super(parent, position, StandardWindow.RESIZE_BOX_OUTER_SIZE);
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

        VectorReadable3F edge_color = null;
        VectorReadable3F fill_color = null;

        if (window.windowIsFocused()) {
          if (this.over) {
            if (this.pressed) {
              edge_color = theme.getFocusedComponentActiveEdgeColor();
              fill_color = theme.getFocusedComponentActiveBackgroundColor();
            } else {
              edge_color = theme.getFocusedComponentOverEdgeColor();
              fill_color = theme.getFocusedComponentOverBackgroundColor();
            }
          } else {
            fill_color = theme.getFocusedComponentBackgroundColor();
            edge_color = theme.getFocusedComponentEdgeColor();
          }
        } else {
          fill_color = theme.getUnfocusedComponentBackgroundColor();
          edge_color = theme.getUnfocusedComponentEdgeColor();
        }

        draw.renderRectangleFill(context, size, fill_color);
        draw.renderRectangleEdge(context, size, 1, edge_color);

        context.contextPushMatrixModelview();
        try {
          final MatrixM4x4F mv = context.contextGetMatrixModelview();
          final int x =
            (size.getXI() - StandardWindow.RESIZE_BOX_INNER_SIZE.x) / 2;
          final int y =
            (size.getYI() - StandardWindow.RESIZE_BOX_INNER_SIZE.y) / 2;
          MatrixM4x4F.translateByVector2FInPlace(mv, new VectorI2F(x, y));

          draw.renderRectangleEdge(
            context,
            StandardWindow.RESIZE_BOX_INNER_SIZE,
            1,
            edge_color);
        } finally {
          context.contextPopMatrixModelview();
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
            + (mouse_position_current.getXI() - mouse_position_initial
              .getXI());
        this.window_delta.y =
          this.window_start.y
            + (mouse_position_current.getYI() - mouse_position_initial
              .getYI());

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

  private final class ScrollBarHorizontal extends Component
  {
    public ScrollBarHorizontal(
      final @Nonnull Component component,
      final @Nonnull PointReadable<ParentRelative> position,
      final @Nonnull VectorReadable2I size)
      throws ConstraintError
    {
      super(component, position, size);
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
          draw.renderRectangleFill(
            context,
            size,
            theme.getFocusedComponentBackgroundColor());
          draw.renderRectangleEdge(
            context,
            size,
            theme.getWindowEdgeWidth(),
            theme.getFocusedComponentEdgeColor());
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
  }

  private final class ScrollBarVertical extends Component
  {
    public ScrollBarVertical(
      final @Nonnull Component component,
      final @Nonnull PointReadable<ParentRelative> position,
      final @Nonnull VectorReadable2I size)
      throws ConstraintError
    {
      super(component, position, size);
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
          draw.renderRectangleFill(
            context,
            size,
            theme.getFocusedComponentBackgroundColor());
          draw.renderRectangleEdge(
            context,
            size,
            theme.getWindowEdgeWidth(),
            theme.getFocusedComponentEdgeColor());
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
  }

  final class Titlebar extends Component
  {
    private final @Nonnull Point<ScreenRelative> window_delta;
    private final @Nonnull Point<ScreenRelative> window_start;
    private final @CheckForNull CloseBox         close_box;
    private final @Nonnull Label                 label;

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
        new Label(
          context,
          parent,
          StandardWindow.TITLEBAR_LABEL_OFFSET,
          size,
          title);
      assert this.label != null;

      if (can_close) {
        this.close_box =
          new CloseBox(context, this, PointConstants.PARENT_ORIGIN);
        this.close_box
          .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
        this.close_box
          .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
        ComponentAlignment.setPositionContainerCenter(this.close_box);
        ComponentAlignment.setPositionContainerRight(this.close_box, 2);
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

        this.label.labelSetColor3f(
          color.getXF(),
          color.getYF(),
          color.getZF());

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

        this.window_delta
          .setXI(this.window_start.getXI()
            + (mouse_position_current.getXI() - mouse_position_initial
              .getXI()));
        this.window_delta
          .setYI(this.window_start.getYI()
            + (mouse_position_current.getYI() - mouse_position_initial
              .getYI()));

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

  static final @Nonnull VectorI2I                     CLOSE_BOX_SIZE;
  static final @Nonnull VectorI2I                     CLOSE_BOX_X_TOP_LEFT;
  static final @Nonnull VectorI2I                     CLOSE_BOX_X_BOTTOM_RIGHT;
  static final @Nonnull VectorI2I                     CLOSE_BOX_X_BOTTOM_LEFT;
  static final @Nonnull VectorI2I                     CLOSE_BOX_X_TOP_RIGHT;
  static final @Nonnull VectorI2I                     RESIZE_BOX_OUTER_SIZE;
  static final @Nonnull VectorI2I                     RESIZE_BOX_INNER_SIZE;
  static final @Nonnull PointReadable<ParentRelative> TITLEBAR_LABEL_OFFSET;
  static final int                                    V_SCROLLBAR_WIDTH;
  static final int                                    H_SCROLLBAR_HEIGHT;

  static {
    CLOSE_BOX_SIZE = new VectorI2I(12, 12);
    CLOSE_BOX_X_TOP_LEFT = new VectorI2I(3, 3);
    CLOSE_BOX_X_BOTTOM_RIGHT = new VectorI2I(9, 9);
    CLOSE_BOX_X_BOTTOM_LEFT = new VectorI2I(3, 9);
    CLOSE_BOX_X_TOP_RIGHT = new VectorI2I(9, 3);
    RESIZE_BOX_INNER_SIZE = new VectorI2I(8, 8);
    RESIZE_BOX_OUTER_SIZE = new VectorI2I(14, 14);
    TITLEBAR_LABEL_OFFSET = new Point<ParentRelative>(4, 0);
    V_SCROLLBAR_WIDTH = StandardWindow.RESIZE_BOX_OUTER_SIZE.x;
    H_SCROLLBAR_HEIGHT = StandardWindow.RESIZE_BOX_OUTER_SIZE.y;
  }

  private final @Nonnull ContentPane                  content_pane;
  private final @Nonnull Container                    main_pane;
  private final @Nonnull Titlebar                     titlebar;
  private final @CheckForNull ResizeBox               resize_box;
  private final @CheckForNull ScrollBarHorizontal     scrollbar_h;
  private final @CheckForNull ScrollBarVertical       scrollbar_v;

  public StandardWindow(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull WindowParameters parameters)
    throws ConstraintError,
      GUIException
  {
    super(context, position, size);

    Constraints.constrainNotNull(parameters, "Parameters");

    this.windowSetMinimumWidth(parameters.getMinimumWidth());
    this.windowSetMinimumHeight(parameters.getMinimumHeight());

    final Component root = this.windowGetRootPane();
    final VectorReadable2I root_size = root.componentGetSize();

    {
      final VectorM2I tbar_size = new VectorM2I();
      tbar_size.x = root_size.getXI();
      tbar_size.y = 16;

      final Point<ParentRelative> tbar_posi = new Point<ParentRelative>();
      tbar_posi.setXI(0);
      tbar_posi.setYI(-1);

      this.titlebar =
        new Titlebar(
          context,
          root,
          tbar_posi,
          tbar_size,
          parameters.getTitle(),
          parameters.getCanClose());
      this.titlebar
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    }

    {
      final VectorM2I pane_size = new VectorM2I();
      pane_size.x = root_size.getXI();
      pane_size.y = root_size.getYI();
      pane_size.y -= this.titlebar.componentGetHeight();

      this.main_pane =
        new Container(root, PointConstants.PARENT_ORIGIN, pane_size);
      this.main_pane
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      this.main_pane
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);

      ComponentAlignment.setPositionRelativeBelowSameX(
        this.main_pane,
        0,
        this.titlebar);
    }

    {
      final VectorM2I pane_size =
        new VectorM2I(this.main_pane.componentGetSize());

      if (parameters.getCanResize()) {
        pane_size.x -= StandardWindow.V_SCROLLBAR_WIDTH;
        pane_size.y -= StandardWindow.H_SCROLLBAR_HEIGHT;
      }

      this.content_pane =
        new ContentPane(
          this.main_pane,
          PointConstants.PARENT_ORIGIN,
          pane_size);

      this.content_pane
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      this.content_pane
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    }

    if (parameters.getCanResize()) {
      this.resize_box =
        new ResizeBox(context, this.main_pane, PointConstants.PARENT_ORIGIN);
      this.resize_box
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.resize_box
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      ComponentAlignment.setPositionContainerBottomRight(this.resize_box, 0);

      /*
       * Initialize horizontal scrollbar.
       */

      final VectorM2I scroll_h_size = new VectorM2I();
      scroll_h_size.x = root.componentGetWidth();
      scroll_h_size.x -= this.resize_box.componentGetWidth();
      scroll_h_size.y = StandardWindow.H_SCROLLBAR_HEIGHT;

      this.scrollbar_h =
        new ScrollBarHorizontal(
          this.main_pane,
          PointConstants.PARENT_ORIGIN,
          scroll_h_size);
      this.scrollbar_h
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      this.scrollbar_h
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);

      ComponentAlignment.setPositionContainerBottomLeft(this.scrollbar_h, 0);

      /*
       * Initialize vertical scrollbar.
       */

      final VectorM2I scroll_v_size = new VectorM2I();
      scroll_v_size.x = StandardWindow.V_SCROLLBAR_WIDTH;
      scroll_v_size.y = this.main_pane.componentGetHeight();
      scroll_v_size.y -= this.resize_box.componentGetHeight();

      this.scrollbar_v =
        new ScrollBarVertical(
          this.main_pane,
          PointConstants.PARENT_ORIGIN,
          scroll_v_size);
      this.scrollbar_v
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.scrollbar_v
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);

      ComponentAlignment.setPositionContainerTopRight(this.scrollbar_v, 0);

    } else {
      this.resize_box = null;
      this.scrollbar_h = null;
      this.scrollbar_v = null;
    }
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[StandardWindow ");
    builder.append(this.windowGetID());
    builder.append("]");
    return builder.toString();
  }

  @Override public @Nonnull ContentPane windowGetContentPane()
  {
    return this.content_pane;
  }
}
