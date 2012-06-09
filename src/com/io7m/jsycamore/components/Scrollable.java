package com.io7m.jsycamore.components;

import java.util.Set;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Triangle;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.ParentRelative;
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

      @Override public void dragListenerOnDrag(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull PointReadable<ScreenRelative> current,
        final @Nonnull Component initial)
        throws ConstraintError,
          GUIException
      {
        // Unused.
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
        // Unused.
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
        // TODO Auto-generated method stub

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

    int getScrollbarWidth()
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

      @Override public void dragListenerOnDrag(
        final @Nonnull GUIContext context,
        final @Nonnull PointReadable<ScreenRelative> start,
        final @Nonnull PointReadable<ScreenRelative> current,
        final @Nonnull Component initial)
        throws ConstraintError,
          GUIException
      {
        // Unused.
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
        // Unused.
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
        // TODO Auto-generated method stub

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
            fill_color = theme.getUnfocusedComponentDisabledBackgroundColor();
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

    public int getScrollbarHeight()
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

  private int                                child_minimum_x;
  private int                                child_maximum_x;
  private int                                child_minimum_y;
  private int                                child_maximum_y;

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
    this.reconfigure(context);
  }

  private void reconfigure(
    final @Nonnull GUIContext context)
    throws ConstraintError
  {
    final Set<Component> children = this.content.componentGetChildren();

    this.child_minimum_x = Integer.MAX_VALUE;
    this.child_maximum_x = Integer.MIN_VALUE;
    this.child_minimum_y = Integer.MAX_VALUE;
    this.child_maximum_y = Integer.MIN_VALUE;

    for (final Component child : children) {
      final PointReadable<ParentRelative> pos =
        child.componentGetPositionParentRelative();

      this.child_minimum_x = Math.min(this.child_minimum_x, pos.getXI());
      this.child_maximum_x =
        Math.max(
          this.child_maximum_x,
          pos.getXI() + child.componentGetWidth());
      this.child_minimum_y = Math.min(this.child_minimum_y, pos.getYI());
      this.child_maximum_y =
        Math.max(
          this.child_maximum_y,
          pos.getYI() + child.componentGetHeight());
    }

    assert this.child_maximum_x >= this.child_minimum_x;
    assert this.child_maximum_y >= this.child_minimum_y;

    final int child_x_span = this.child_maximum_x - this.child_minimum_x;
    final int child_y_span = this.child_maximum_y - this.child_minimum_y;
    final int bar_x_span = this.scroll_h.getScrollbarWidth();
    final int bar_y_span = this.scroll_v.getScrollbarHeight();
    final int pane_x_span = this.content.componentGetWidth();
    final int pane_y_span = this.content.componentGetHeight();

    final boolean x_scroll =
      (this.child_minimum_x < 0) || (this.child_maximum_x > pane_x_span);
    final boolean y_scroll =
      (this.child_minimum_y < 0) || (this.child_maximum_y > pane_y_span);

    if (x_scroll) {
      final double x_span_prop = (double) pane_x_span / (double) child_x_span;
      final int thumb_width = (int) (bar_x_span * x_span_prop);
      this.scroll_h.setThumbWidth(context, thumb_width);
      this.scroll_h.componentSetEnabled(true);
    } else {
      this.scroll_h.setThumbWidth(context, this.scroll_h.getScrollbarWidth());
      this.scroll_h.componentSetEnabled(false);
    }

    if (y_scroll) {
      final double y_span_prop = (double) pane_y_span / (double) child_y_span;
      final int thumb_height = (int) (bar_y_span * y_span_prop);
      this.scroll_v.setThumbHeight(context, thumb_height);
      this.scroll_v.componentSetEnabled(true);
    } else {
      this.scroll_v.setThumbHeight(
        context,
        this.scroll_v.getScrollbarHeight());
      this.scroll_v.componentSetEnabled(false);
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
    builder.append("[Scrollable ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
