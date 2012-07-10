package com.io7m.jsycamore;

import java.util.Stack;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;

/**
 * A stack of matrices.
 * 
 * This is analogous to the old fixed-function OpenGL matrix stack. Programs
 * should set an initial matrix (usually the identity matrix) with
 * {@link MatrixM4x4FStack#push(MatrixM4x4F)}, and then use pairs of
 * {@link MatrixM4x4FStack#pushCopy()} and {@link MatrixM4x4FStack#pop()}
 * calls to mark transformations.
 */

public final class MatrixM4x4FStack
{
  private final @Nonnull Stack<MatrixM4x4F> items;

  public MatrixM4x4FStack()
  {
    this.items = new Stack<MatrixM4x4F>();
  }

  /**
   * Peek at the matrix on the top of the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   */

  public @Nonnull MatrixM4x4F peek()
    throws ConstraintError
  {
    Constraints.constrainRange(this.items.size(), 1, Integer.MAX_VALUE);
    return this.items.peek();
  }

  /**
   * Pop a matrix from the top of the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   */

  public @Nonnull MatrixM4x4F pop()
    throws ConstraintError
  {
    Constraints.constrainRange(this.items.size(), 1, Integer.MAX_VALUE);

    final MatrixM4x4F m = this.items.pop();
    return m;
  }

  /**
   * Push <code>m</code> onto the stack.
   * 
   * @throws ConstraintError
   *           Iff <code>m == null</code>.
   */

  public void push(
    final @Nonnull MatrixM4x4F m)
    throws ConstraintError
  {
    Constraints.constrainNotNull(m, "Matrix");
    this.items.push(new MatrixM4x4F(m));
  }

  /**
   * Duplicate the element at the top of the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   */

  public void pushCopy()
    throws ConstraintError
  {
    this.items.push(new MatrixM4x4F(this.peek()));
  }

  /**
   * Retrieve the size of the stack.
   */

  public int size()
  {
    return this.items.size();
  }
}
