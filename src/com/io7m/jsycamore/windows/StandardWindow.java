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
import com.io7m.jsycamore.components.AbstractButton;
import com.io7m.jsycamore.components.AbstractDragButton;
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
  private static final class CloseBox extends AbstractButton
  {
    @SuppressWarnings("unused") public CloseBox(
      final @Nonnull GUIContext context,
      final @Nonnull Component parent,
      final @Nonnull PointReadable<ParentRelative> position)
      throws ConstraintError
    {
      super(parent, position, StandardWindow.CLOSE_BOX_SIZE);
    }

    @Override public void buttonListenerOnClick(
      final @Nonnull Component button)
    {
      final Window window = this.componentGetWindow();
      if (window != null) {
        window.windowSetWantClose();
      }
    }

    @Override public void buttonRenderPost(
      final GUIContext context)
      throws ConstraintError,
        GUIException
    {
      try {
        final DrawPrimitives draw = context.contextGetDrawPrimitives();

        draw.renderLine(
          context,
          StandardWindow.CLOSE_BOX_X_TOP_LEFT,
          StandardWindow.CLOSE_BOX_X_BOTTOM_RIGHT,
          this.buttonGetCurrentEdgeColor());
        draw.renderLine(
          context,
          StandardWindow.CLOSE_BOX_X_BOTTOM_LEFT,
          StandardWindow.CLOSE_BOX_X_TOP_RIGHT,
          this.buttonGetCurrentEdgeColor());

      } catch (final GLException e) {
        throw new GUIException(e);
      }
    }

    @Override public void buttonRenderPre(
      final GUIContext context)
      throws ConstraintError,
        GUIException
    {
      // Unused.
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

  private static final class ResizeBox extends AbstractDragButton
  {
    private final VectorM2I size_initial = new VectorM2I();
    private final VectorM2I size_new     = new VectorM2I();

    @SuppressWarnings("unused") public ResizeBox(
      final @Nonnull GUIContext context,
      final @Nonnull Component parent,
      final @Nonnull PointReadable<ParentRelative> position)
      throws ConstraintError
    {
      super(parent, position, StandardWindow.RESIZE_BOX_OUTER_SIZE);
    }

    @Override public void buttonListenerOnClick(
      final Component button)
    {
      // Unused.
    }

    @Override public void buttonRenderPost(
      final @Nonnull GUIContext context)
      throws ConstraintError,
        GUIException
    {
      try {
        final DrawPrimitives draw = context.contextGetDrawPrimitives();
        final VectorReadable2I size = this.componentGetSize();

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
            this.buttonGetCurrentEdgeWidth(),
            this.buttonGetCurrentEdgeColor());
        } finally {
          context.contextPopMatrixModelview();
        }

      } catch (final GLException e) {
        throw new GUIException(e);
      }
    }

    @Override public void buttonRenderPre(
      final GUIContext context)
    {
      // Unused.
    }

    @Override public void dragListenerOnDrag(
      final @Nonnull GUIContext context,
      final @Nonnull PointReadable<ScreenRelative> start,
      final @Nonnull PointReadable<ScreenRelative> current,
      final @Nonnull Component initial)
      throws ConstraintError,
        GUIException
    {
      final PointReadable<ScreenRelative> delta = this.dragGetDelta();

      final Log log = context.contextGetComponentLog();
      log.debug("drag drag initial: " + this.dragGetComponentInitial());
      log.debug("drag drag delta:   " + delta);

      final Window window = this.componentGetWindow();
      if (window != null) {
        this.size_new.x = this.size_initial.x + delta.getXI();
        this.size_new.y = this.size_initial.y + delta.getYI();
        window.windowSetSize(context, this.size_new);
      }
    }

    @Override public void dragListenerOnRelease(
      final @Nonnull GUIContext context,
      final @Nonnull PointReadable<ScreenRelative> start,
      final @Nonnull PointReadable<ScreenRelative> current,
      final @Nonnull Component initial,
      final @Nonnull Component actual)
      throws GUIException,
        ConstraintError
    {
      final PointReadable<ScreenRelative> delta = this.dragGetDelta();

      final Log log = context.contextGetComponentLog();
      log.debug("drag release initial: " + this.dragGetComponentInitial());
      log.debug("drag release delta:   " + delta);

      final Window window = this.componentGetWindow();
      if (window != null) {
        this.size_new.x = this.size_initial.x + delta.getXI();
        this.size_new.y = this.size_initial.y + delta.getYI();
        window.windowSetSize(context, this.size_new);
      }
    }

    @Override public void dragListenerOnStart(
      final @Nonnull GUIContext context,
      final @Nonnull PointReadable<ScreenRelative> start,
      final @Nonnull Component initial)
      throws ConstraintError
    {
      // Unused.
      final Log log = context.contextGetComponentLog();
      log.debug("drag start initial: " + this.dragGetComponentInitial());
      log.debug("drag start delta:   " + this.dragGetDelta());

      final Window window = this.componentGetWindow();
      if (window != null) {
        final VectorReadable2I size = window.windowGetSize();
        VectorM2I.copy(size, this.size_initial);
      }
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

  private static final class ScrollBarHorizontal extends Component
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

        VectorReadable3F fill_color = null;
        VectorReadable3F edge_color = null;
        final int edge_width = 1;

        if (window.windowIsFocused()) {
          fill_color = theme.getFocusedComponentBackgroundColor();
          edge_color = theme.getFocusedComponentEdgeColor();
        } else {
          fill_color = theme.getUnfocusedComponentBackgroundColor();
          edge_color = theme.getUnfocusedComponentEdgeColor();
        }

        assert fill_color != null;
        assert edge_color != null;
        draw.renderRectangleFill(context, size, fill_color);
        draw.renderRectangleEdge(context, size, edge_width, edge_color);

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

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[ScrollBarHorizontal ");
      builder.append(this.componentGetID());
      builder.append("]");
      return builder.toString();
    }
  }

  private static final class ScrollBarVertical extends Component
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

        VectorReadable3F fill_color = null;
        VectorReadable3F edge_color = null;
        final int edge_width = 1;

        if (window.windowIsFocused()) {
          fill_color = theme.getFocusedComponentBackgroundColor();
          edge_color = theme.getFocusedComponentEdgeColor();
        } else {
          fill_color = theme.getUnfocusedComponentBackgroundColor();
          edge_color = theme.getUnfocusedComponentEdgeColor();
        }

        assert fill_color != null;
        assert edge_color != null;
        draw.renderRectangleFill(context, size, fill_color);
        draw.renderRectangleEdge(context, size, edge_width, edge_color);

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

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[ScrollBarVertical ");
      builder.append(this.componentGetID());
      builder.append("]");
      return builder.toString();
    }
  }

  private static final class Titlebar extends Component
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

        VectorReadable3F text_color = null;
        VectorReadable3F edge_color = null;
        VectorReadable3F fill_color = null;
        final int edge_width = 1;

        if (window.windowIsFocused()) {
          text_color = theme.getFocusedWindowTitlebarTextColor();
          edge_color = theme.getFocusedWindowEdgeColor();
          fill_color = theme.getFocusedWindowTitlebarBackgroundColor();
        } else {
          text_color = theme.getUnfocusedWindowTitlebarTextColor();
          edge_color = theme.getUnfocusedWindowEdgeColor();
          fill_color = theme.getUnfocusedWindowTitlebarBackgroundColor();
        }

        assert text_color != null;
        assert edge_color != null;
        assert fill_color != null;

        this.label.labelSetColor3f(
          text_color.getXF(),
          text_color.getYF(),
          text_color.getZF());
        draw.renderRectangleFill(context, size, fill_color);
        draw.renderRectangleEdge(context, size, edge_width, edge_color);

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
    this.windowSetMinimumWidth(context, parameters.getMinimumWidth());
    this.windowSetMinimumHeight(context, parameters.getMinimumHeight());

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

    /*
     * Initialize main pane (the pane that holds everything but the titlebar).
     */

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

    /*
     * Initialize scrollbars and resize box, if necessary.
     */

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
