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

public final class Container extends Component
{
  private boolean edge = false;

  public Container(
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(parent, position, size);
  }

  public Container(
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
      if (this.edge) {
        final Theme theme = context.contextGetTheme();
        final DrawPrimitives draw = context.contextGetDrawPrimitives();

        if (this.componentIsFocused()) {
          draw.renderRectangleEdge(
            context,
            this.componentGetSize(),
            1,
            theme.getFocusedComponentEdgeColor());
        }
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

  public void setDrawEdge(
    final boolean on)
  {
    this.edge = on;
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
