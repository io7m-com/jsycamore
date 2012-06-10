package com.io7m.jsycamore.components;

import java.util.Set;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.ComponentLimits;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Triangle;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;

public final class Scrollable extends Component
{
  private final class ScrollBarHorizontal extends Component
  {
    private final class ButtonLeft extends AbstractRepeatingButton
    {
      public ButtonLeft(
        final @Nonnull ScrollBarHorizontal parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);
      }

      @Override public void buttonListenerOnClick(
        final @Nonnull Component button)
      {
        // Unused.
      }

      @SuppressWarnings("synthetic-access") @Override public
        void
        buttonRenderPost(
          final @Nonnull GUIContext context)
          throws ConstraintError,
            GUIException
      {
        try {
          final DrawPrimitives draw = context.contextGetDrawPrimitives();

          draw.renderTriangleFill(
            context,
            Scrollable.triangle_left0,
            this.buttonGetCurrentEdgeColor());
          draw.renderTriangleFill(
            context,
            Scrollable.triangle_left1,
            this.buttonGetCurrentEdgeColor());

        } catch (final GLException e) {
          throw new GUIException(e);
        }
      }

      @Override public void buttonRenderPre(
        final @Nonnull GUIContext context)
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
        builder.append("[ButtonLeft ");
        builder.append(this.componentGetID());
        builder.append("]");
        return builder.toString();
      }
    }

    private final class ButtonRight extends AbstractRepeatingButton
    {
      public ButtonRight(
        final @Nonnull ScrollBarHorizontal parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);
      }

      @Override public void buttonListenerOnClick(
        final @Nonnull Component button)
        throws ConstraintError
      {
        // final Set<Component> children =
        // Scrollable.this.content.componentGetChildren();
        // final Point<ParentRelative> pos = new Point<ParentRelative>();
        //
        // for (final Component child : children) {
        // final PointReadable<ParentRelative> cpos =
        // child.componentGetPositionParentRelative();
        // pos.setXI(cpos.getXI() - 1);
        // pos.setYI(cpos.getYI());
        // child.componentSetPositionParentRelative(pos);
        // }
      }

      @SuppressWarnings("synthetic-access") @Override public
        void
        buttonRenderPost(
          final @Nonnull GUIContext context)
          throws ConstraintError,
            GUIException
      {
        try {
          final DrawPrimitives draw = context.contextGetDrawPrimitives();

          draw.renderTriangleFill(
            context,
            Scrollable.triangle_right0,
            this.buttonGetCurrentEdgeColor());
          draw.renderTriangleFill(
            context,
            Scrollable.triangle_right1,
            this.buttonGetCurrentEdgeColor());

        } catch (final GLException e) {
          throw new GUIException(e);
        }
      }

      @Override public void buttonRenderPre(
        final @Nonnull GUIContext context)
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
        builder.append("[ButtonRight ");
        builder.append(this.componentGetID());
        builder.append("]");
        return builder.toString();
      }
    }

    private final class Thumb extends AbstractDragButton
    {
      private final class Ridges extends AbstractContainer
      {
        public Ridges(
          final @Nonnull Component parent,
          final @Nonnull PointReadable<ParentRelative> position,
          final @Nonnull VectorReadable2I size)
          throws ConstraintError
        {
          super(parent, position, size);
        }

        @Override public void componentRenderPostDescendants(
          final GUIContext context)
          throws ConstraintError,
            GUIException
        {
          // Unused.
        }

        @SuppressWarnings("synthetic-access") @Override public
          void
          componentRenderPreDescendants(
            final @Nonnull GUIContext context)
            throws ConstraintError,
              GUIException
        {
          try {
            final DrawPrimitives draw = context.contextGetDrawPrimitives();
            final VectorReadable3F color =
              Thumb.this.buttonGetCurrentEdgeColor();

            draw.renderLine(
              context,
              Scrollable.H_THUMB_LINE0_POINT_0,
              Scrollable.H_THUMB_LINE0_POINT_1,
              color);
            draw.renderLine(
              context,
              Scrollable.H_THUMB_LINE1_POINT_0,
              Scrollable.H_THUMB_LINE1_POINT_1,
              color);
            draw.renderLine(
              context,
              Scrollable.H_THUMB_LINE2_POINT_0,
              Scrollable.H_THUMB_LINE2_POINT_1,
              color);
          } catch (final GLException e) {
            throw new GUIException(e);
          }
        }

        @Override public String toString()
        {
          final StringBuilder builder = new StringBuilder();
          builder.append("[Ridges ");
          builder.append(this.componentGetID());
          builder.append("]");
          return builder.toString();
        }
      }

      private final @Nonnull Ridges ridges;

      public Thumb(
        final @Nonnull Component parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);

        this.ridges =
          new Ridges(
            this,
            PointConstants.PARENT_ORIGIN,
            Scrollable.SCROLLBAR_BUTTON_SIZE);
      }

      @Override public void buttonListenerOnClick(
        final @Nonnull Component button)
        throws GUIException,
          ConstraintError
      {
        // Unused.
      }

      @Override public void buttonRenderPost(
        final @Nonnull GUIContext context)
        throws ConstraintError,
          GUIException
      {
        // Unused.
      }

      @Override public void buttonRenderPre(
        final @Nonnull GUIContext context)
        throws ConstraintError,
          GUIException
      {
        ComponentAlignment.setPositionContainerCenter(this.ridges);
      }

      private void doDrag()
        throws ConstraintError
      {
        this.doDragUpdateThumbPosition();
      }

      private void doDragUpdateThumbPosition()
        throws ConstraintError
      {
        final PointReadable<ScreenRelative> delta =
          this.dragGetDeltaFromInitial();
        final Point<ParentRelative> component_start =
          this.dragGetComponentInitial();

        final Point<ParentRelative> new_pos = new Point<ParentRelative>();
        new_pos.setXI(component_start.getXI() + delta.getXI());
        new_pos.setYI(0);

        this.componentSetPositionParentRelative(new_pos);
      }

      @Override public void dragListenerOnDrag(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull PointReadable<ScreenRelative> current,
        final @Nonnull Component initial)
        throws ConstraintError,
          GUIException
      {
        this.doDrag();
      }

      @Override public void dragListenerOnRelease(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull PointReadable<ScreenRelative> current,
        final @Nonnull Component initial,
        final @Nonnull Component actual)
        throws ConstraintError,
          GUIException
      {
        this.doDrag();
      }

      @Override public void dragListenerOnStart(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull Component initial)
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
        builder.append("[Thumb ");
        builder.append(this.componentGetID());
        builder.append("]");
        return builder.toString();
      }
    }

    private final class Trough extends AbstractContainer
    {
      public Trough(
        final @Nonnull Component parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);
      }

      @Override public void componentRenderPostDescendants(
        final GUIContext context)
        throws ConstraintError,
          GUIException
      {
        // Unused.
      }

      @Override public void componentRenderPreDescendants(
        final GUIContext context)
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
            if (this.componentIsEnabled()) {
              fill_color = theme.getFocusedComponentBackgroundColor();
              edge_color = theme.getFocusedComponentEdgeColor();
            } else {
              fill_color = theme.getFocusedComponentDisabledBackgroundColor();
              edge_color = theme.getFocusedComponentDisabledEdgeColor();
            }
          } else {
            if (this.componentIsEnabled()) {
              fill_color = theme.getUnfocusedComponentBackgroundColor();
              edge_color = theme.getUnfocusedComponentEdgeColor();
            } else {
              fill_color =
                theme.getUnfocusedComponentDisabledBackgroundColor();
              edge_color = theme.getUnfocusedComponentDisabledEdgeColor();
            }
          }

          assert fill_color != null;
          assert edge_color != null;
          draw.renderRectangleFill(context, size, fill_color);
          draw.renderRectangleEdge(context, size, edge_width, edge_color);

        } catch (final GLException e) {
          throw new GUIException(e);
        }
      }
    }

    private final @Nonnull ButtonLeft  button_left;
    private final @Nonnull ButtonRight button_right;
    private final @Nonnull Trough      trough;
    private final @Nonnull Thumb       thumb;

    public ScrollBarHorizontal(
      final @Nonnull GUIContext context,
      final @Nonnull Component component,
      final @Nonnull PointReadable<ParentRelative> position,
      final @Nonnull VectorReadable2I size)
      throws ConstraintError
    {
      super(component, position, size);

      this.button_left =
        new ButtonLeft(
          this,
          PointConstants.PARENT_ORIGIN,
          Scrollable.SCROLLBAR_BUTTON_SIZE);

      this.button_right =
        new ButtonRight(
          this,
          PointConstants.PARENT_ORIGIN,
          Scrollable.SCROLLBAR_BUTTON_SIZE);

      final int trough_w =
        this.componentGetWidth()
          - (Scrollable.SCROLLBAR_BUTTON_SIZE.getXI() * 2);
      final int trough_h = Scrollable.SCROLLBAR_BUTTON_SIZE.getYI();
      this.trough =
        new Trough(this, PointConstants.PARENT_ORIGIN, new VectorI2I(
          trough_w,
          trough_h));

      this.thumb =
        new Thumb(
          this.trough,
          PointConstants.PARENT_ORIGIN,
          Scrollable.SCROLLBAR_BUTTON_SIZE);
      this.thumb.componentSetMinimumWidth(
        context,
        Scrollable.SCROLLBAR_BUTTON_SIZE.getXI());
      ComponentLimits.setMaximumXYContainer(ScrollBarHorizontal.this.thumb);

      this.button_right
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.trough
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);

      ComponentAlignment.setPositionContainerTopLeft(this.button_left, 0);
      ComponentAlignment.setPositionRelativeRightOfSameY(
        this.trough,
        0,
        this.button_left);
      ComponentAlignment.setPositionRelativeRightOfSameY(
        this.button_right,
        0,
        this.trough);
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
      // Unused.
    }

    int getTroughWidth()
    {
      return this.trough.componentGetWidth();
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

    void setThumbWidth(
      final @Nonnull GUIContext context,
      final int width)
      throws ConstraintError
    {
      this.thumb.componentSetSize(context, new VectorI2I(
        width,
        Scrollable.SCROLLBAR_BUTTON_SIZE.getYI()));
      ComponentLimits.setMaximumXYContainer(this.thumb);
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

  private final class ScrollBarVertical extends Component
  {
    private final class ButtonDown extends AbstractRepeatingButton
    {
      public ButtonDown(
        final @Nonnull ScrollBarVertical parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);
      }

      @Override public void buttonListenerOnClick(
        final @Nonnull Component button)
      {
        // Unused.
      }

      @SuppressWarnings("synthetic-access") @Override public
        void
        buttonRenderPost(
          final @Nonnull GUIContext context)
          throws ConstraintError,
            GUIException
      {
        try {
          final DrawPrimitives draw = context.contextGetDrawPrimitives();

          draw.renderTriangleFill(
            context,
            Scrollable.triangle_down0,
            this.buttonGetCurrentEdgeColor());
          draw.renderTriangleFill(
            context,
            Scrollable.triangle_down1,
            this.buttonGetCurrentEdgeColor());

        } catch (final GLException e) {
          throw new GUIException(e);
        }
      }

      @Override public void buttonRenderPre(
        final @Nonnull GUIContext context)
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
        builder.append("[ButtonRight ");
        builder.append(this.componentGetID());
        builder.append("]");
        return builder.toString();
      }
    }

    private final class ButtonUp extends AbstractRepeatingButton
    {
      public ButtonUp(
        final @Nonnull ScrollBarVertical parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);
      }

      @Override public void buttonListenerOnClick(
        final @Nonnull Component button)
      {
        // Unused.
      }

      @SuppressWarnings("synthetic-access") @Override public
        void
        buttonRenderPost(
          final @Nonnull GUIContext context)
          throws ConstraintError,
            GUIException
      {
        try {
          final DrawPrimitives draw = context.contextGetDrawPrimitives();

          draw.renderTriangleFill(
            context,
            Scrollable.triangle_up0,
            this.buttonGetCurrentEdgeColor());
          draw.renderTriangleFill(
            context,
            Scrollable.triangle_up1,
            this.buttonGetCurrentEdgeColor());

        } catch (final GLException e) {
          throw new GUIException(e);
        }
      }

      @Override public void buttonRenderPre(
        final @Nonnull GUIContext context)
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
        builder.append("[ButtonUp ");
        builder.append(this.componentGetID());
        builder.append("]");
        return builder.toString();
      }
    }

    private final class Thumb extends AbstractDragButton
    {
      private final class Ridges extends AbstractContainer
      {
        public Ridges(
          final @Nonnull Component parent,
          final @Nonnull PointReadable<ParentRelative> position,
          final @Nonnull VectorReadable2I size)
          throws ConstraintError
        {
          super(parent, position, size);
        }

        @Override public void componentRenderPostDescendants(
          final GUIContext context)
          throws ConstraintError,
            GUIException
        {
          // Unused.
        }

        @SuppressWarnings("synthetic-access") @Override public
          void
          componentRenderPreDescendants(
            final @Nonnull GUIContext context)
            throws ConstraintError,
              GUIException
        {
          try {
            final DrawPrimitives draw = context.contextGetDrawPrimitives();
            final VectorReadable3F color =
              Thumb.this.buttonGetCurrentEdgeColor();

            draw.renderLine(
              context,
              Scrollable.V_THUMB_LINE0_POINT_0,
              Scrollable.V_THUMB_LINE0_POINT_1,
              color);
            draw.renderLine(
              context,
              Scrollable.V_THUMB_LINE1_POINT_0,
              Scrollable.V_THUMB_LINE1_POINT_1,
              color);
            draw.renderLine(
              context,
              Scrollable.V_THUMB_LINE2_POINT_0,
              Scrollable.V_THUMB_LINE2_POINT_1,
              color);
          } catch (final GLException e) {
            throw new GUIException(e);
          }
        }

        @Override public String toString()
        {
          final StringBuilder builder = new StringBuilder();
          builder.append("[Ridges ");
          builder.append(this.componentGetID());
          builder.append("]");
          return builder.toString();
        }
      }

      private final Ridges ridges;

      public Thumb(
        final @Nonnull Component parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);

        this.ridges =
          new Ridges(
            this,
            PointConstants.PARENT_ORIGIN,
            Scrollable.SCROLLBAR_BUTTON_SIZE);
      }

      @Override public void buttonListenerOnClick(
        final @Nonnull Component button)
        throws GUIException,
          ConstraintError
      {
        // Unused.
      }

      @Override public void buttonRenderPost(
        final @Nonnull GUIContext context)
        throws ConstraintError,
          GUIException
      {
        // Unused.
      }

      @Override public void buttonRenderPre(
        final @Nonnull GUIContext context)
        throws ConstraintError,
          GUIException
      {
        ComponentAlignment.setPositionContainerCenter(this.ridges);
      }

      private void doDrag()
        throws ConstraintError
      {
        final Point<ParentRelative> component_start =
          this.dragGetComponentInitial();
        final PointReadable<ScreenRelative> delta =
          this.dragGetDeltaFromInitial();

        final Point<ParentRelative> new_pos = new Point<ParentRelative>();
        new_pos.setXI(0);
        new_pos.setYI(component_start.getYI() + delta.getYI());

        this.componentSetPositionParentRelative(new_pos);
      }

      @Override public void dragListenerOnDrag(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull PointReadable<ScreenRelative> current,
        final @Nonnull Component initial)
        throws ConstraintError,
          GUIException
      {
        this.doDrag();
      }

      @Override public void dragListenerOnRelease(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull PointReadable<ScreenRelative> current,
        final @Nonnull Component initial,
        final @Nonnull Component actual)
        throws ConstraintError,
          GUIException
      {
        this.doDrag();
      }

      @Override public void dragListenerOnStart(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull Component initial)
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
        builder.append("[Thumb ");
        builder.append(this.componentGetID());
        builder.append("]");
        return builder.toString();
      }
    }

    private final class Trough extends AbstractContainer
    {
      public Trough(
        final @Nonnull Component parent,
        final @Nonnull PointReadable<ParentRelative> position,
        final @Nonnull VectorReadable2I size)
        throws ConstraintError
      {
        super(parent, position, size);
      }

      @Override public void componentRenderPostDescendants(
        final GUIContext context)
        throws ConstraintError,
          GUIException
      {
        // Unused.
      }

      @Override public void componentRenderPreDescendants(
        final GUIContext context)
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
            if (this.componentIsEnabled()) {
              fill_color = theme.getFocusedComponentBackgroundColor();
              edge_color = theme.getFocusedComponentEdgeColor();
            } else {
              fill_color = theme.getFocusedComponentDisabledBackgroundColor();
              edge_color = theme.getFocusedComponentDisabledEdgeColor();
            }
          } else {
            if (this.componentIsEnabled()) {
              fill_color = theme.getUnfocusedComponentBackgroundColor();
              edge_color = theme.getUnfocusedComponentEdgeColor();
            } else {
              fill_color =
                theme.getUnfocusedComponentDisabledBackgroundColor();
              edge_color = theme.getUnfocusedComponentDisabledEdgeColor();
            }
          }

          assert fill_color != null;
          assert edge_color != null;
          draw.renderRectangleFill(context, size, fill_color);
          draw.renderRectangleEdge(context, size, edge_width, edge_color);

        } catch (final GLException e) {
          throw new GUIException(e);
        }
      }
    }

    private final @Nonnull ButtonUp   button_up;
    private final @Nonnull ButtonDown button_down;
    private final @Nonnull Trough     trough;
    private final @Nonnull Thumb      thumb;

    public ScrollBarVertical(
      final @Nonnull GUIContext context,
      final @Nonnull Component component,
      final @Nonnull PointReadable<ParentRelative> position,
      final @Nonnull VectorReadable2I size)
      throws ConstraintError
    {
      super(component, position, size);

      this.button_up =
        new ButtonUp(
          this,
          PointConstants.PARENT_ORIGIN,
          Scrollable.SCROLLBAR_BUTTON_SIZE);

      this.button_down =
        new ButtonDown(
          this,
          PointConstants.PARENT_ORIGIN,
          Scrollable.SCROLLBAR_BUTTON_SIZE);

      final int trough_h =
        this.componentGetHeight()
          - (Scrollable.SCROLLBAR_BUTTON_SIZE.getYI() * 2);
      final int trough_w = Scrollable.SCROLLBAR_BUTTON_SIZE.getXI();
      this.trough =
        new Trough(this, PointConstants.PARENT_ORIGIN, new VectorI2I(
          trough_w,
          trough_h));

      this.thumb =
        new Thumb(
          this.trough,
          PointConstants.PARENT_ORIGIN,
          Scrollable.SCROLLBAR_BUTTON_SIZE);
      this.thumb.componentSetMinimumHeight(
        context,
        Scrollable.SCROLLBAR_BUTTON_SIZE.getYI());
      ComponentLimits.setMaximumXYContainer(this.thumb);

      this.button_down
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.trough
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);

      ComponentAlignment.setPositionContainerTopLeft(this.button_up, 0);
      ComponentAlignment.setPositionRelativeBelowSameX(
        this.trough,
        0,
        this.button_up);
      ComponentAlignment.setPositionRelativeBelowSameX(
        this.button_down,
        0,
        this.trough);
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
      // Unused.
    }

    public int getTroughHeight()
    {
      return this.trough.componentGetHeight();
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

    public void setThumbHeight(
      final GUIContext context,
      final int height)
      throws ConstraintError
    {
      this.thumb.componentSetSize(context, new VectorI2I(
        Scrollable.SCROLLBAR_BUTTON_SIZE.getXI(),
        height));
      ComponentLimits.setMaximumXYContainer(this.thumb);
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

  private static final @Nonnull Triangle     triangle_left0;
  private static final @Nonnull Triangle     triangle_left1;
  private static final @Nonnull Triangle     triangle_right0;
  private static final @Nonnull Triangle     triangle_right1;
  private static final @Nonnull Triangle     triangle_down0;
  private static final @Nonnull Triangle     triangle_down1;
  private static final @Nonnull Triangle     triangle_up0;
  private static final @Nonnull Triangle     triangle_up1;
  private static final @Nonnull VectorI2I    H_THUMB_LINE0_POINT_0;
  private static final @Nonnull VectorI2I    H_THUMB_LINE1_POINT_0;
  private static final @Nonnull VectorI2I    H_THUMB_LINE1_POINT_1;
  private static final @Nonnull VectorI2I    H_THUMB_LINE2_POINT_0;
  private static final @Nonnull VectorI2I    H_THUMB_LINE2_POINT_1;
  private static final @Nonnull VectorI2I    H_THUMB_LINE0_POINT_1;
  private static final @Nonnull VectorI2I    V_THUMB_LINE0_POINT_0;
  private static final @Nonnull VectorI2I    V_THUMB_LINE1_POINT_0;
  private static final @Nonnull VectorI2I    V_THUMB_LINE1_POINT_1;
  private static final @Nonnull VectorI2I    V_THUMB_LINE2_POINT_0;
  private static final @Nonnull VectorI2I    V_THUMB_LINE2_POINT_1;
  private static final @Nonnull VectorI2I    V_THUMB_LINE0_POINT_1;

  static {
    {
      H_SCROLLBAR_WIDTH = 14;
      V_SCROLLBAR_HEIGHT = 14;
      SCROLLBAR_BUTTON_SIZE =
        new VectorI2I(
          Scrollable.H_SCROLLBAR_WIDTH,
          Scrollable.V_SCROLLBAR_HEIGHT);
    }

    {
      H_THUMB_LINE0_POINT_0 = new VectorI2I(4, 4);
      H_THUMB_LINE0_POINT_1 =
        new VectorI2I(4, Scrollable.V_SCROLLBAR_HEIGHT - 4);

      H_THUMB_LINE1_POINT_0 =
        new VectorI2I(Scrollable.H_SCROLLBAR_WIDTH / 2, 4);
      H_THUMB_LINE1_POINT_1 =
        new VectorI2I(
          Scrollable.H_SCROLLBAR_WIDTH / 2,
          Scrollable.V_SCROLLBAR_HEIGHT - 4);

      H_THUMB_LINE2_POINT_0 =
        new VectorI2I(Scrollable.H_SCROLLBAR_WIDTH - 4, 4);
      H_THUMB_LINE2_POINT_1 =
        new VectorI2I(
          Scrollable.H_SCROLLBAR_WIDTH - 4,
          Scrollable.V_SCROLLBAR_HEIGHT - 4);
    }

    {
      V_THUMB_LINE0_POINT_0 = new VectorI2I(4, 4);
      V_THUMB_LINE0_POINT_1 =
        new VectorI2I(Scrollable.H_SCROLLBAR_WIDTH - 4, 4);

      V_THUMB_LINE1_POINT_0 =
        new VectorI2I(4, Scrollable.V_SCROLLBAR_HEIGHT / 2);
      V_THUMB_LINE1_POINT_1 =
        new VectorI2I(
          Scrollable.H_SCROLLBAR_WIDTH - 4,
          Scrollable.V_SCROLLBAR_HEIGHT / 2);

      V_THUMB_LINE2_POINT_0 =
        new VectorI2I(4, Scrollable.V_SCROLLBAR_HEIGHT - 4);
      V_THUMB_LINE2_POINT_1 =
        new VectorI2I(
          Scrollable.H_SCROLLBAR_WIDTH - 4,
          Scrollable.V_SCROLLBAR_HEIGHT - 4);
    }

    {
      final int x_off = 5;
      final int y_off = 3;

      final VectorI2I tl0p0 = new VectorI2I(0 + x_off, 4 + y_off);
      final VectorI2I tl0p1 = new VectorI2I(3 + x_off, 4 + y_off);
      final VectorI2I tl0p2 = new VectorI2I(3 + x_off, 0 + y_off);
      triangle_left0 = new Triangle(tl0p0, tl0p1, tl0p2);

      final VectorI2I tl1p0 = new VectorI2I(0 + x_off, 3 + y_off);
      final VectorI2I tl1p1 = new VectorI2I(3 + x_off, 7 + y_off);
      final VectorI2I tl1p2 = new VectorI2I(3 + x_off, 3 + y_off);
      triangle_left1 = new Triangle(tl1p0, tl1p1, tl1p2);
    }

    {
      final int x_off = 6;
      final int y_off = 3;

      final VectorI2I tr0p0 = new VectorI2I(0 + x_off, 0 + y_off);
      final VectorI2I tr0p1 = new VectorI2I(0 + x_off, 4 + y_off);
      final VectorI2I tr0p2 = new VectorI2I(4 + x_off, 4 + y_off);
      triangle_right0 = new Triangle(tr0p0, tr0p1, tr0p2);

      final VectorI2I tr1p0 = new VectorI2I(0 + x_off, 3 + y_off);
      final VectorI2I tr1p1 = new VectorI2I(0 + x_off, 7 + y_off);
      final VectorI2I tr1p2 = new VectorI2I(4 + x_off, 3 + y_off);
      triangle_right1 = new Triangle(tr1p0, tr1p1, tr1p2);
    }

    {
      final int x_off = 3;
      final int y_off = 6;

      final VectorI2I td0p0 = new VectorI2I(0 + x_off, 0 + y_off);
      final VectorI2I td0p1 = new VectorI2I(4 + x_off, 3 + y_off);
      final VectorI2I td0p2 = new VectorI2I(4 + x_off, 0 + y_off);
      triangle_down0 = new Triangle(td0p0, td0p1, td0p2);

      final VectorI2I td1p0 = new VectorI2I(3 + x_off, 0 + y_off);
      final VectorI2I td1p1 = new VectorI2I(3 + x_off, 3 + y_off);
      final VectorI2I td1p2 = new VectorI2I(7 + x_off, 0 + y_off);
      triangle_down1 = new Triangle(td1p0, td1p1, td1p2);
    }

    {
      final int x_off = 3;
      final int y_off = 5;

      final VectorI2I tu0p0 = new VectorI2I(0 + x_off, 3 + y_off);
      final VectorI2I tu0p1 = new VectorI2I(4 + x_off, 3 + y_off);
      final VectorI2I tu0p2 = new VectorI2I(4 + x_off, 0 + y_off);
      triangle_up0 = new Triangle(tu0p0, tu0p1, tu0p2);

      final VectorI2I tu1p0 = new VectorI2I(3 + x_off, 3 + y_off);
      final VectorI2I tu1p1 = new VectorI2I(7 + x_off, 3 + y_off);
      final VectorI2I tu1p2 = new VectorI2I(3 + x_off, 0 + y_off);
      triangle_up1 = new Triangle(tu1p0, tu1p1, tu1p2);
    }
  }

  private final @Nonnull AbstractContainer   content;
  private final @Nonnull ScrollBarHorizontal scroll_h;
  private final @Nonnull ScrollBarVertical   scroll_v;

  private final @Nonnull VectorM2I           minimum_child = new VectorM2I();
  private final @Nonnull VectorM2I           maximum_child = new VectorM2I();
  private final @Nonnull VectorM2I           span_children = new VectorM2I();
  private final @Nonnull VectorM2I           span_trough   = new VectorM2I();
  private final @Nonnull VectorM2I           span_pane     = new VectorM2I();

  static final int                           H_SCROLLBAR_WIDTH;
  static final int                           V_SCROLLBAR_HEIGHT;
  static final @Nonnull VectorReadable2I     SCROLLBAR_BUTTON_SIZE;

  public Scrollable(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull AbstractContainer content)
    throws ConstraintError
  {
    super(parent, content.componentGetPositionParentRelative(), content
      .componentGetSize());

    {
      /*
       * Take ownership of content area. Resize it and position.
       */

      final VectorM2I content_size =
        new VectorM2I(content.componentGetSize());
      content_size.y -= Scrollable.H_SCROLLBAR_WIDTH;
      content_size.x -= Scrollable.V_SCROLLBAR_HEIGHT;

      this.content = content;
      if (this.content.componentHasParent()) {
        this.content.componentDetachFromParent();
      }
      this.content.componentAttachToParent(this);
      this.content
        .componentSetPositionParentRelative(PointConstants.PARENT_ORIGIN);
      this.content.componentSetSize(context, content_size);
      this.content
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      this.content
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    }

    {
      /*
       * Initialize horizontal scrollbar.
       */

      final VectorM2I scroll_h_size = new VectorM2I();
      scroll_h_size.x = this.componentGetWidth();
      scroll_h_size.x -= Scrollable.H_SCROLLBAR_WIDTH;
      scroll_h_size.y = Scrollable.V_SCROLLBAR_HEIGHT;

      this.scroll_h =
        new ScrollBarHorizontal(
          context,
          this,
          PointConstants.PARENT_ORIGIN,
          scroll_h_size);
      this.scroll_h
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      this.scroll_h
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      ComponentAlignment.setPositionRelativeBelowSameX(
        this.scroll_h,
        -1,
        content);
    }

    {
      /*
       * Initialize vertical scrollbar.
       */

      final VectorM2I scroll_v_size = new VectorM2I();
      scroll_v_size.x = Scrollable.H_SCROLLBAR_WIDTH;
      scroll_v_size.y = this.componentGetHeight();
      scroll_v_size.y -= Scrollable.V_SCROLLBAR_HEIGHT;

      this.scroll_v =
        new ScrollBarVertical(
          context,
          this,
          PointConstants.PARENT_ORIGIN,
          scroll_v_size);
      this.scroll_v
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.scroll_v
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      ComponentAlignment.setPositionRelativeRightOfSameY(
        this.scroll_v,
        -1,
        content);
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
    this.reconfigureScrollbars(context);
  }

  /**
   * Determine the current "spans": the difference between the left edge of
   * the leftmost child and the right edge of the rightmost child, the
   * difference between the top edge of the top child and the bottom edge of
   * the bottom child, etc...
   */

  private void reconfigureCalculateSpans()
  {
    final Set<Component> children = this.content.componentGetChildren();

    this.minimum_child.x = Integer.MAX_VALUE;
    this.minimum_child.y = Integer.MAX_VALUE;
    this.maximum_child.x = Integer.MIN_VALUE;
    this.maximum_child.y = Integer.MIN_VALUE;

    for (final Component child : children) {
      final PointReadable<ParentRelative> pos =
        child.componentGetPositionParentRelative();

      this.minimum_child.x = Math.min(this.minimum_child.x, pos.getXI());
      this.maximum_child.x =
        Math.max(
          this.maximum_child.x,
          pos.getXI() + child.componentGetWidth());
      this.minimum_child.y = Math.min(this.minimum_child.y, pos.getYI());
      this.maximum_child.y =
        Math.max(
          this.maximum_child.y,
          pos.getYI() + child.componentGetHeight());
    }

    assert this.maximum_child.x >= this.minimum_child.x;
    assert this.maximum_child.y >= this.minimum_child.y;

    this.span_children.x = this.maximum_child.x - this.minimum_child.x;
    this.span_children.y = this.maximum_child.y - this.minimum_child.y;
    this.span_trough.x = this.scroll_h.getTroughWidth();
    this.span_trough.y = this.scroll_v.getTroughHeight();
    this.span_pane.x = this.content.componentGetWidth();
    this.span_pane.y = this.content.componentGetHeight();
  }

  /**
   * The height of the scrollbar thumb is equal to the proportion of the
   * "child span" to the "pane span", multiplied by the height of the trough.
   * In other words, if 50% of the child span is contained within the current
   * pane span, the thumb height will be 50% of the height of the scrollbar
   * trough.
   */

  private int reconfigureGetThumbHeight()
  {
    final double proportion =
      (double) this.span_pane.y / (double) this.span_children.y;
    return (int) (this.span_trough.y * proportion);
  }

  /**
   * The width of the scrollbar thumb is equal to the proportion of the
   * "child span" to the "pane span", multiplied by the width of the trough.
   * In other words, if 50% of the child span is contained within the current
   * pane span, the thumb width will be 50% of the width of the scrollbar
   * trough.
   */

  private int reconfigureGetThumbWidth()
  {
    final double proportion =
      (double) this.span_pane.x / (double) this.span_children.x;
    return (int) (this.span_trough.x * proportion);
  }

  /**
   * Activate/deactive the horizontal and vertical scrollbars based on whether
   * or not the contents of the managed pane are within view. Resize the
   * scrollbar thumbs based on how much of the pane is visible.
   */

  private void reconfigureScrollbars(
    final @Nonnull GUIContext context)
    throws ConstraintError
  {
    this.reconfigureCalculateSpans();

    if (this.reconfigureShouldScrollX()) {
      final int thumb_width = this.reconfigureGetThumbWidth();
      this.scroll_h.setThumbWidth(context, thumb_width);
      this.scroll_h.componentSetEnabled(true);
    } else {
      this.scroll_h.setThumbWidth(context, this.scroll_h.getTroughWidth());
      this.scroll_h.componentSetEnabled(false);
    }

    if (this.reconfigureShouldScrollY()) {
      final int thumb_height = this.reconfigureGetThumbHeight();
      this.scroll_v.setThumbHeight(context, thumb_height);
      this.scroll_v.componentSetEnabled(true);
    } else {
      this.scroll_v.setThumbHeight(context, this.scroll_v.getTroughHeight());
      this.scroll_v.componentSetEnabled(false);
    }
  }

  /**
   * If the rightmost edge of the rightmost child is greater than the
   * rightmost edge of the current span, or if the leftmost edge of the
   * leftmost child is less than 0, the pane should scroll horizontally.
   */

  private boolean reconfigureShouldScrollX()
  {
    return (this.minimum_child.x < 0)
      || (this.maximum_child.x > this.span_pane.x);
  }

  /**
   * If the bottom edge of the bottom child is greater than the bottom edge of
   * the current span, or if the top edge of the top child is less than 0, the
   * pane should scroll vertically.
   */

  private boolean reconfigureShouldScrollY()
  {
    return (this.minimum_child.y < 0)
      || (this.maximum_child.y > this.span_pane.y);
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
    builder.append("[Scrollable ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
