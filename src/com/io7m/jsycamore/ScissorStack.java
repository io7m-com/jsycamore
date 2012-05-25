package com.io7m.jsycamore;

import java.util.Iterator;
import java.util.Stack;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A stack of scissor regions. Each region pushed onto the stack is clipped
 * against the preceding region so that the scissoring is "cumulative".
 */

public final class ScissorStack implements Iterable<Scissor>
{
  private final @Nonnull Stack<Scissor> regions;

  public ScissorStack()
  {
    this.regions = new Stack<Scissor>();
  }

  @Override public Iterator<Scissor> iterator()
  {
    return this.regions.iterator();
  }

  /**
   * Peek at the region on top of the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   */

  public @Nonnull Scissor peek()
    throws ConstraintError
  {
    Constraints.constrainRange(
      this.regions.size(),
      1,
      Integer.MAX_VALUE,
      "Stack size");
    return this.regions.peek();
  }

  /**
   * Pop the region at top of the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   */

  public @Nonnull Scissor pop()
    throws ConstraintError
  {
    Constraints.constrainRange(
      this.regions.size(),
      1,
      Integer.MAX_VALUE,
      "Stack size");
    return this.regions.pop();
  }

  /**
   * Clip the given region against the region at the top of the stack (if
   * any), push the clipped region onto the stack, and return the clipped
   * region.
   * 
   * @throws ConstraintError
   *           Iff <code>next == null</code>.
   */

  public @Nonnull Scissor push(
    final @Nonnull Scissor next)
    throws ConstraintError
  {
    Constraints.constrainNotNull(next, "Next region");

    if (this.regions.size() > 0) {
      final Scissor existing = this.regions.peek();
      final Scissor intersection = next.clipAgainst(existing);

      assert intersection.getLowerX() >= existing.getLowerX();
      assert intersection.getLowerY() >= existing.getLowerY();
      assert intersection.getUpperX() <= existing.getUpperX();
      assert intersection.getUpperY() <= existing.getUpperY();

      this.regions.push(intersection);
      return intersection;
    }

    this.regions.push(next);
    return next;
  }

  /**
   * Retrieve the current size of the stack.
   */

  public int size()
  {
    return this.regions.size();
  }
}
