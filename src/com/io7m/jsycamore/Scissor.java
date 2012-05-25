package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScissorRelative;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;

public class Scissor
{
  private final @Nonnull Point<ScissorRelative> position;
  private final @Nonnull VectorM2I              size;

  public Scissor(
    final @Nonnull Point<ScissorRelative> position,
    final @Nonnull VectorReadable2I size)
  {
    this.position = new Point<ScissorRelative>(position);
    this.size = new VectorM2I(size);
  }

  public @Nonnull Scissor clipAgainst(
    final @Nonnull Scissor other)
  {
    final int ox0 = other.getLowerX();
    final int oy0 = other.getLowerY();
    final int ox1 = other.getUpperX();
    final int oy1 = other.getUpperY();

    final int tx0 = this.getLowerX();
    final int ty0 = this.getLowerY();
    final int tx1 = this.getUpperX();
    final int ty1 = this.getUpperY();

    final int mx0 = Math.max(ox0, tx0);
    final int my0 = Math.max(oy0, ty0);
    final int mx1 = Math.min(ox1, tx1);
    final int my1 = Math.min(oy1, ty1);

    final int w = mx1 - mx0;
    final int h = my1 - my0;

    final Point<ScissorRelative> p = new Point<ScissorRelative>(mx0, my0);
    final VectorI2I s = new VectorI2I(w, h);

    return new Scissor(p, s);
  }

  public int getLowerX()
  {
    return this.position.getXI();
  }

  public int getLowerY()
  {
    return this.position.getYI();
  }

  public @Nonnull PointReadable<ScissorRelative> getPosition()
  {
    return this.position;
  }

  public @Nonnull VectorReadable2I getSize()
  {
    return this.size;
  }

  public int getUpperX()
  {
    return this.position.getXI() + this.size.x;
  }

  public int getUpperY()
  {
    return this.position.getYI() + this.size.y;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Scissor ");
    builder.append(this.position);
    builder.append(" ");
    builder.append(this.size);
    builder.append("]");
    return builder.toString();
  }
}
