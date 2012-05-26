package com.io7m.jsycamore.components;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jpismo.CompiledText;
import com.io7m.jpismo.TextCacheException;
import com.io7m.jpismo.TextRenderer;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorReadable2I;

public final class TextArea extends Component
{
  private final class TextAreaActual extends Component
  {
    private final @Nonnull ArrayList<String> lines;
    private @Nonnull CompiledText            text;

    public TextAreaActual(
      final @Nonnull GUIContext context,
      final @Nonnull Component parent,
      final @Nonnull PointReadable<ParentRelative> position,
      final @Nonnull VectorReadable2I size)
      throws ConstraintError,
        GUIException
    {
      super(parent, position, size);

      try {
        this.lines = new ArrayList<String>();
        this.recompileText(context);
      } catch (final GLException e) {
        throw new GUIException(e);
      } catch (final TextCacheException e) {
        throw new GUIException(e);
      }
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
        final Window window = this.componentGetWindow();

        assert window != null;
        if (window.windowIsFocused()) {
          draw.renderCompiledText(
            context,
            this.text,
            theme.getFocusedTextAreaForegroundColor());
        } else {
          draw.renderCompiledText(
            context,
            this.text,
            theme.getUnfocusedTextAreaForegroundColor());
        }
      } catch (final GLException e) {
        throw new GUIException(e);
      }
    }

    private void recompileText(
      final @Nonnull GUIContext context)
      throws GLException,
        ConstraintError,
        TextCacheException
    {
      final TextRenderer tr = context.contextGetTextRendererMedium();
      this.text = tr.textCompile(this.lines);
      tr.textCacheUpload();
    }

    @Override public void resourceDelete(
      final GLInterface gl)
      throws ConstraintError,
        GLException
    {
      this.text.resourceDelete(gl);
    }

    @Override public boolean resourceIsDeleted()
    {
      return this.text.resourceIsDeleted();
    }

    public void textAreaAddLine(
      final @Nonnull GUIContext context,
      final @Nonnull String line)
      throws ConstraintError,
        GUIException
    {
      Constraints.constrainNotNull(line, "Line");

      try {
        this.lines.add(line);
        this.recompileText(context);
      } catch (final GLException e) {
        throw new GUIException(e);
      } catch (final TextCacheException e) {
        throw new GUIException(e);
      }
    }

    public void textAreaSetText(
      final @Nonnull GUIContext context,
      final @Nonnull String line)
      throws ConstraintError,
        GUIException
    {
      Constraints.constrainNotNull(line, "Line");

      try {
        this.lines.clear();
        this.lines.add(line);
        this.recompileText(context);
      } catch (final GLException e) {
        throw new GUIException(e);
      } catch (final TextCacheException e) {
        throw new GUIException(e);
      }
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[TextAreaActual \"");
      builder.append(this.lines);
      builder.append("\"]");
      return builder.toString();
    }
  }

  private static final Point<ParentRelative> PADDING;

  static {
    PADDING = new Point<ParentRelative>(4, 2);
  }

  private final @Nonnull TextAreaActual      actual;

  public TextArea(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError,
      GUIException
  {
    super(parent, position, size);

    final VectorI2I ta_size =
      new VectorI2I(size.getXI() - TextArea.PADDING.getXI(), size.getYI()
        - TextArea.PADDING.getYI());

    this.actual =
      new TextAreaActual(context, this, TextArea.PADDING, ta_size);
  }

  @Override public void componentRenderPostDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException
  {
    // TODO Auto-generated method stub

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
          theme.getFocusedTextAreaBackgroundColor());
        draw.renderRectangleEdge(
          context,
          size,
          theme.getWindowEdgeWidth(),
          theme.getFocusedComponentEdgeColor());
      } else {
        draw.renderRectangleFill(
          context,
          size,
          theme.getUnfocusedTextAreaBackgroundColor());
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
    this.actual.resourceDelete(gl);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.actual.resourceIsDeleted();
  }

  public void textAreaAddLine(
    final @Nonnull GUIContext context,
    final @Nonnull String line)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(line, "Line");
    this.actual.textAreaAddLine(context, line);
  }

  public void textAreaSetText(
    final @Nonnull GUIContext context,
    final @Nonnull String line)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(line, "Line");
    this.actual.textAreaSetText(context, line);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[TextArea ");
    builder.append(this.actual);
    builder.append("]");
    return builder.toString();
  }
}
