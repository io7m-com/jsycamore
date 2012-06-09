package com.io7m.jsycamore.components;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;

public final class ButtonLabelled extends AbstractButton
{
  private final @Nonnull Label label;

  public ButtonLabelled(
    final @Nonnull GUIContext context,
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String text)
    throws ConstraintError,
      GUIException
  {
    super(parent, position, size);

    this.label =
      new Label(context, this, PointConstants.PARENT_ORIGIN, size, text);
    ComponentAlignment.setPositionContainerCenter(this.label);
  }

  public ButtonLabelled(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String text)
    throws ConstraintError,
      GUIException
  {
    super(position, size);

    this.label =
      new Label(context, this, PointConstants.PARENT_ORIGIN, size, text);
    ComponentAlignment.setPositionContainerCenter(this.label);
  }

  @Override public final void buttonListenerOnClick(
    final @Nonnull Component button)
  {
    // Unused.
  }

  @Override public void buttonRenderPost(
    final @Nonnull GUIContext context)
  {
    // Unused.
  }

  @Override public void buttonRenderPre(
    final @Nonnull GUIContext context)
  {
    final VectorReadable3F color = this.buttonGetCurrentEdgeColor();
    this.label.labelSetColor3f(color.getXF(), color.getYF(), color.getZF());
  }

  @Override public void resourceDelete(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.label.resourceDelete(gl);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.label.resourceIsDeleted();
  }

  public void setText(
    final @Nonnull GUIContext context,
    final @Nonnull String text)
    throws GUIException,
      ConstraintError
  {
    this.label.labelSetText(context, text);
    ComponentAlignment.setPositionContainerCenter(this.label);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ButtonLabelled ");
    builder.append(this.label);
    builder.append(" ");
    builder.append(this.componentGetID());
    builder.append("]");
    return builder.toString();
  }
}
