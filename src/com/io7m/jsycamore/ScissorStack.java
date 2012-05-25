package com.io7m.jsycamore;

import java.util.Iterator;
import java.util.Stack;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

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

  public @Nonnull Scissor push(
    final @Nonnull Scissor next)
  {
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

  public int size()
  {
    return this.regions.size();
  }
}
