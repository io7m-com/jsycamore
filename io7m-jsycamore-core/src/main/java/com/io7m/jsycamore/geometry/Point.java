package com.io7m.jsycamore.geometry;

import javax.annotation.Nonnull;

import com.io7m.jtensors.VectorM2I;

/**
 * <p>
 * Type representing a pair of <code>(x,y)</code> coordinates in the
 * coordinate system <code>A</code>.
 * </p>
 * 
 * <p>
 * The class is indexed by the type <code>A</code> in order to make points in
 * different coordinate systems type-incompatible. The purpose of this is to
 * allow the type system to prevent the accidental mixing of, for example,
 * screen-relative and window-relative coordinates.
 * </p>
 * 
 * @param <A>
 *          Type representing the coordinate system of the point. Likely one
 *          of <code>{ScreenRelative | WindowRelative | ParentRelative}</code>
 *          .
 */

public final class Point<A> implements PointReadable<A>
{
  private final @Nonnull VectorM2I vector;

  public Point()
  {
    this.vector = new VectorM2I();
  }

  public Point(
    final int x,
    final int y)
  {
    this.vector = new VectorM2I(x, y);
  }

  public Point(
    final @Nonnull PointReadable<A> source)
  {
    this.vector = new VectorM2I(source);
  }

  @Override public int getXI()
  {
    return this.vector.x;
  }

  @Override public int getYI()
  {
    return this.vector.y;
  }

  /**
   * Set the X coordinate to <code>x</code>.
   */

  public void setXI(
    final int x)
  {
    this.vector.x = x;
  }

  /**
   * Set the Y coordinate to <code>y</code>.
   */

  public void setYI(
    final int y)
  {
    this.vector.y = y;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Point ");
    builder.append(this.vector.x);
    builder.append(" ");
    builder.append(this.vector.y);
    builder.append("]");
    return builder.toString();
  }
}
