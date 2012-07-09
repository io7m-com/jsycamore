package com.io7m.jsycamore.components;

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
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;

public final class ContainerThemed extends AbstractContainer
{
  private boolean          edge       = true;
  private boolean          fill       = false;
  private final int        edge_width = 1;
  private VectorReadable3F edge_color;
  private VectorReadable3F fill_color;

  public ContainerThemed(
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(parent, position, size);
  }

  public ContainerThemed(
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(position, size);
  }

  public ContainerThemed(
    final @Nonnull Window parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(parent, position, size);
  }

  @Override public void componentRenderPostDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException
  {
    try {
      final DrawPrimitives draw = context.contextGetDrawPrimitives();
      final VectorReadable2I size = this.componentGetSize();

      if (this.edge) {
        draw.renderRectangleEdge(
          context,
          size,
          this.edge_width,
          this.edge_color);
      }
    } catch (final GLException e) {
      throw new GUIException(e);
    }
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
        this.fill_color = theme.getFocusedComponentBackgroundColor();
        this.edge_color = theme.getFocusedComponentEdgeColor();
      } else {
        this.fill_color = theme.getUnfocusedComponentBackgroundColor();
        this.edge_color = theme.getUnfocusedComponentEdgeColor();
      }

      assert this.fill_color != null;
      assert this.edge_color != null;

      if (this.fill) {
        draw.renderRectangleFill(context, size, this.fill_color);
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
    // Unused.
  }

  @Override public boolean resourceIsDeleted()
  {
    return true;
  }

  public void setDrawEdge(
    final boolean on)
  {
    this.edge = on;
  }

  public void setDrawFill(
    final boolean on)
  {
    this.fill = on;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Container ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
