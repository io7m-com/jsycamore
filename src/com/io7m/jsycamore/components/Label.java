package com.io7m.jsycamore.components;

import javax.annotation.Nonnull;

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
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorReadable2I;

public final class Label extends Component
{
  private final @Nonnull VectorM4F     color;
  private final @Nonnull StringBuilder text;
  private @Nonnull CompiledText        compiled_text;
  private boolean                      deleted = false;

  public Label(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String text)
    throws ConstraintError,
      GUIException
  {
    super(parent, position, size);

    try {
      this.color = new VectorM4F();
      this.color.x = 1.0f;
      this.color.y = 1.0f;
      this.color.z = 1.0f;
      this.color.w = 1.0f;

      this.text = new StringBuilder(text);
      final TextRenderer tr = context.contextGetTextRendererMedium();
      this.compiled_text = tr.textCompileLine(text);
      tr.textCacheUpload();

      this.componentSetSize(new VectorI2I(
        (int) this.compiled_text.getWidth(),
        (int) this.compiled_text.getHeight()));

    } catch (final GLException e) {
      throw new GUIException(e);
    } catch (final TextCacheException e) {
      throw new GUIException(e);
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
      draw.renderCompiledText(context, this.compiled_text, this.color);
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  public void labelSetAlpha(
    final float a)
  {
    this.color.w = a;
  }

  public void labelSetColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.color.x = r;
    this.color.y = g;
    this.color.z = b;
  }

  @Override public void resourceDelete(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.compiled_text.resourceDelete(gl);
    this.deleted = true;
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.deleted;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Label '");
    builder.append(this.text);
    builder.append("']");
    return builder.toString();
  }
}
