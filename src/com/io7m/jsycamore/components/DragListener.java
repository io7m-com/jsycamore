package com.io7m.jsycamore.components;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;

public interface DragListener
{
  void dragListenerOnDrag(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> start,
    final @Nonnull PointReadable<ScreenRelative> current,
    final @Nonnull Component initial)
    throws ConstraintError,
      GUIException;

  void dragListenerOnRelease(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> start,
    final @Nonnull PointReadable<ScreenRelative> current,
    final @Nonnull Component initial,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException;

  void dragListenerOnStart(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> start,
    final @Nonnull Component initial)
    throws ConstraintError,
      GUIException;
}
