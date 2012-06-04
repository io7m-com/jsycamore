package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable2I;

public interface ResizeListener<T>
{
  void resizeListenerOnResize(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size_original,
    final @Nonnull VectorReadable2I size_end,
    final @Nonnull VectorReadable2I size_delta,
    final @Nonnull T actual)
    throws ConstraintError;
}
