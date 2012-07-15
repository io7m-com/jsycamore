package com.io7m.jsycamore.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.Scissor;
import com.io7m.jsycamore.ScissorStack;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScissorRelative;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorReadable2I;

public final class ScissorStackTest
{
  @SuppressWarnings("static-method") @Test public void testInitial()
    throws ConstraintError
  {
    final ScissorStack stack = new ScissorStack();
    final Scissor s0 =
      new Scissor(new Point<ScissorRelative>(0, 480), new VectorI2I(640, 480));

    stack.push(s0);

    final Scissor k0 = stack.peek();
    final PointReadable<ScissorRelative> s0_pos = s0.getPosition();
    final PointReadable<ScissorRelative> k0_pos = k0.getPosition();
    final VectorReadable2I s0_siz = s0.getSize();
    final VectorReadable2I k0_siz = k0.getSize();

    Assert.assertEquals(s0_siz.getXI(), k0_siz.getXI());
    Assert.assertEquals(s0_siz.getYI(), k0_siz.getYI());
    Assert.assertEquals(s0_pos.getXI(), k0_pos.getXI());
    Assert.assertEquals(s0_pos.getYI(), k0_pos.getYI());
    Assert.assertEquals(1, stack.size());
  }

  @SuppressWarnings("static-method") @Test public void testIteration()
    throws ConstraintError
  {
    final ScissorStack stack = new ScissorStack();
    final Scissor s0 =
      new Scissor(new Point<ScissorRelative>(0, 0), new VectorI2I(8, 8));
    final Scissor s1 =
      new Scissor(new Point<ScissorRelative>(1, 1), new VectorI2I(8, 8));
    final Scissor s2 =
      new Scissor(new Point<ScissorRelative>(2, 2), new VectorI2I(8, 8));
    final Scissor s3 =
      new Scissor(new Point<ScissorRelative>(3, 3), new VectorI2I(8, 8));

    stack.push(s0);
    stack.push(s1);
    stack.push(s2);
    stack.push(s3);

    int index = 0;
    for (final Scissor s : stack) {
      Assert.assertEquals(index, s.getLowerX());
      Assert.assertEquals(index, s.getLowerY());
      ++index;
    }
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPeekEmpty()
      throws ConstraintError
  {
    final ScissorStack stack = new ScissorStack();
    stack.peek();
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPopEmpty()
      throws ConstraintError
  {
    final ScissorStack stack = new ScissorStack();
    stack.pop();
  }

  @SuppressWarnings("static-method") @Test public void testPushNarrowing()
    throws ConstraintError
  {
    final ScissorStack stack = new ScissorStack();
    final Scissor s0 =
      new Scissor(new Point<ScissorRelative>(0, 480), new VectorI2I(640, 480));
    final Scissor s1 =
      new Scissor(new Point<ScissorRelative>(100, 500), new VectorI2I(
        320,
        480));

    stack.push(s0);
    final Scissor k0 = stack.peek();
    final PointReadable<ScissorRelative> k0_pos = k0.getPosition();
    final VectorReadable2I k0_siz = k0.getSize();

    Assert.assertEquals(0, k0_pos.getXI());
    Assert.assertEquals(480, k0_pos.getYI());
    Assert.assertEquals(640, k0_siz.getXI());
    Assert.assertEquals(480, k0_siz.getYI());

    stack.push(s1);
    final Scissor k1 = stack.peek();
    final PointReadable<ScissorRelative> k1_pos = k1.getPosition();
    final VectorReadable2I k1_siz = k1.getSize();

    Assert.assertEquals(100, k1_pos.getXI());
    Assert.assertEquals(500, k1_pos.getYI());
    Assert.assertEquals(320, k1_siz.getXI());
    Assert.assertEquals(460, k1_siz.getYI());
  }

  @SuppressWarnings("static-method") @Test public void testPushPopIdentity()
    throws ConstraintError
  {
    final ScissorStack stack = new ScissorStack();

    Assert.assertEquals(0, stack.size());
    stack.push(new Scissor(new Point<ScissorRelative>(0, 480), new VectorI2I(
      640,
      480)));
    Assert.assertEquals(1, stack.size());
    stack.pop();
    Assert.assertEquals(0, stack.size());
  }
}
