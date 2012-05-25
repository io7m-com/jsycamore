package com.io7m.jsycamore.geometry;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Conversion functions between coordinate systems.
 */

public final class PointConversion
{
  /**
   * Convert the scissor-relative point <code>point</code> to screen-relative
   * coordinates, assuming a screen of size <code>size</code>.
   * 
   * @param size
   *          The size of the screen.
   * @param point
   *          The scissor-relative point.
   * @throws ConstraintError
   *           Iff <code>size == null || point == null</code>.
   */

  public static @Nonnull Point<ScreenRelative> scissorToScreen(
    final @Nonnull VectorReadable2I size,
    final @Nonnull PointReadable<ScissorRelative> point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(size, "Screen size");
    Constraints.constrainNotNull(point, "Point");

    final Point<ScreenRelative> p = new Point<ScreenRelative>();
    p.setXI(point.getXI());
    p.setYI(size.getYI() - point.getYI());
    return p;
  }

  /**
   * Convert the screen-relative point <code>point</code> to scissor-relative
   * coordinates, assuming a screen of size <code>size</code>.
   * 
   * @param size
   *          The size of the screen.
   * @param point
   *          The screen-relative point.
   * @throws ConstraintError
   *           Iff <code>size == null || point == null</code>.
   */

  public static @Nonnull Point<ScissorRelative> screenToScissor(
    final @Nonnull VectorReadable2I size,
    final @Nonnull PointReadable<ScreenRelative> point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(size, "Screen size");
    Constraints.constrainNotNull(point, "Point");

    final Point<ScissorRelative> p = new Point<ScissorRelative>();
    p.setXI(point.getXI());
    p.setYI(size.getYI() - point.getYI());
    return p;
  }

  /**
   * Convert the screen-relative point <code>point</code> to window-relative
   * coordinates, relative to <code>base</code>.
   * 
   * @param base
   *          The screen-relative base position of the window.
   * @param point
   *          The screen-relative point.
   * @throws ConstraintError
   *           Iff <code>base == null || point == null</code>.
   */

  public static @Nonnull Point<WindowRelative> screenToWindow(
    final @Nonnull PointReadable<ScreenRelative> base,
    final @Nonnull PointReadable<ScreenRelative> point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(base, "Window");
    Constraints.constrainNotNull(point, "Point");

    final Point<WindowRelative> wr = new Point<WindowRelative>();
    wr.setXI(point.getXI() - base.getXI());
    wr.setYI(point.getYI() - base.getYI());
    return wr;
  }

  /**
   * Convert the window-relative point <code>point</code> to scissor-relative
   * coordinates, assuming a window of size <code>size</code>.
   * 
   * @param size
   *          The size of the screen.
   * @param point
   *          The screen-relative point.
   * @throws ConstraintError
   *           Iff <code>size == null || point == null</code>.
   */

  public static @Nonnull Point<ScissorRelative> windowToScissor(
    final @Nonnull VectorReadable2I size,
    final @Nonnull PointReadable<WindowRelative> point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(size, "Window size");
    Constraints.constrainNotNull(point, "Point");

    final Point<ScissorRelative> p = new Point<ScissorRelative>();
    p.setXI(point.getXI());
    p.setYI(size.getYI() - point.getYI());
    return p;
  }

  /**
   * Convert the window-relative point <code>point</code> to screen-relative
   * coordinates, assuming a window at <code>base</code>.
   * 
   * @param base
   *          The screen-relative base position of the window.
   * @param point
   *          The window-relative point.
   * @throws ConstraintError
   *           Iff <code>base == null || point == null</code>.
   */

  public static @Nonnull Point<ScreenRelative> windowToScreen(
    final @Nonnull PointReadable<ScreenRelative> base,
    final @Nonnull PointReadable<WindowRelative> point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(base, "Window");
    Constraints.constrainNotNull(point, "Point");

    final Point<ScreenRelative> sr = new Point<ScreenRelative>();
    sr.setXI(point.getXI() + base.getXI());
    sr.setYI(point.getYI() + base.getYI());
    return sr;
  }

  private PointConversion()
  {
    throw new AssertionError("unreachable code: report this bug!");
  }
}
