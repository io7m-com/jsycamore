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
import com.io7m.jtensors.VectorReadable2I;

public final class ContentPane extends Component
{
  public ContentPane(
    final @Nonnull Component parent,
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
          theme.getFocusedWindowPaneBackgroundColor());
      } else {
        draw.renderRectangleFill(
          context,
          size,
          theme.getUnfocusedWindowPaneBackgroundColor());
      }
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  @Override public void resourceDelete(
    final GLInterface gl)
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
    builder.append("[Container ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
