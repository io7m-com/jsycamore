package com.io7m.jsycamore;

import java.util.Stack;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;

public final class MatrixM4x4FStack
{
  private final @Nonnull Stack<MatrixM4x4F> items;

  public MatrixM4x4FStack()
  {
    this.items = new Stack<MatrixM4x4F>();
  }

  public @Nonnull MatrixM4x4F peek()
    throws ConstraintError
  {
    Constraints.constrainRange(this.items.size(), 1, Integer.MAX_VALUE);
    return this.items.peek();
  }

  public @Nonnull MatrixM4x4F pop()
    throws ConstraintError
  {
    Constraints.constrainRange(this.items.size(), 1, Integer.MAX_VALUE);

    final MatrixM4x4F m = this.items.pop();
    return m;
  }

  public void push(
    final @Nonnull MatrixM4x4F m)
  {
    this.items.push(new MatrixM4x4F(m));
  }

  public void pushCopy()
    throws ConstraintError
  {
    this.items.push(new MatrixM4x4F(this.peek()));
  }

  public int size()
  {
    return this.items.size();
  }
}
