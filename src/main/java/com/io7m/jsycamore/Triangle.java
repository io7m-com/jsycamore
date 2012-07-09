package com.io7m.jsycamore;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Simple type representing an arbitrary 2D triangle.
 */

@Immutable public final class Triangle
{
  private final @Nonnull VectorReadable2I point0;
  private final @Nonnull VectorReadable2I point1;
  private final @Nonnull VectorReadable2I point2;

  public Triangle(
    final @Nonnull VectorReadable2I point0,
    final @Nonnull VectorReadable2I point1,
    final @Nonnull VectorReadable2I point2)
  {
    this.point0 = new VectorI2I(point0.getXI(), point0.getYI());
    this.point1 = new VectorI2I(point1.getXI(), point1.getYI());
    this.point2 = new VectorI2I(point2.getXI(), point2.getYI());
  }

  public VectorReadable2I getPoint0()
  {
    return this.point0;
  }

  public VectorReadable2I getPoint1()
  {
    return this.point1;
  }

  public VectorReadable2I getPoint2()
  {
    return this.point2;
  }
}
