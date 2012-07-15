package com.io7m.jsycamore.tests;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jtensors.VectorReadable2I;

public final class TestComponent extends Component
{
  protected TestComponent(
    final @Nonnull Component component,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(component, position, size);
  }

  protected TestComponent(
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    super(position, size);
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

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    // Unused.
  }

  @Override public boolean resourceIsDeleted()
  {
    return false;
  }
}
