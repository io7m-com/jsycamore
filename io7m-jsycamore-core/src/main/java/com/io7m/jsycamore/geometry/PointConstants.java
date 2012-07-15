package com.io7m.jsycamore.geometry;

import javax.annotation.Nonnull;

/**
 * Convenient values in various coordinate systems.
 */

public final class PointConstants
{
  public static final @Nonnull PointReadable<ScreenRelative> SCREEN_ORIGIN;
  public static final @Nonnull PointReadable<WindowRelative> WINDOW_ORIGIN;
  public static final @Nonnull PointReadable<ParentRelative> PARENT_ORIGIN;

  static {
    SCREEN_ORIGIN = new Point<ScreenRelative>(0, 0);
    WINDOW_ORIGIN = new Point<WindowRelative>(0, 0);
    PARENT_ORIGIN = new Point<ParentRelative>(0, 0);
  }
}
