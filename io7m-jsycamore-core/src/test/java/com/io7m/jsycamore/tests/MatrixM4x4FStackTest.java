package com.io7m.jsycamore.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.ApproximatelyEqualFloat;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.MatrixM4x4FStack;
import com.io7m.jtensors.MatrixM4x4F;

public final class MatrixM4x4FStackTest
{
  @SuppressWarnings("static-method") @Test public void testInitial()
    throws ConstraintError
  {
    final MatrixM4x4F m1 = new MatrixM4x4F();
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.push(m1);

    final MatrixM4x4F k = stack.peek();
    for (int row = 0; row < 4; ++row) {
      for (int col = 0; col < 4; ++col) {
        Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
          k.get(row, col),
          m1.get(row, col)));
      }
    }
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPeekEmpty()
      throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.peek();
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPopEmpty()
      throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.pop();
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPushCopyEmpty()
      throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.pushCopy();
  }

  @SuppressWarnings("static-method") @Test public
    void
    testPushCopyPopIdentity()
      throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    Assert.assertEquals(0, stack.size());
    stack.push(new MatrixM4x4F());
    Assert.assertEquals(1, stack.size());
    stack.pushCopy();
    Assert.assertEquals(2, stack.size());
    stack.pop();
    Assert.assertEquals(1, stack.size());
    stack.pop();
    Assert.assertEquals(0, stack.size());
  }

  @SuppressWarnings("static-method") @Test public void testPushPopIdentity()
    throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    Assert.assertEquals(0, stack.size());
    stack.push(new MatrixM4x4F());
    Assert.assertEquals(1, stack.size());
    stack.pop();
    Assert.assertEquals(0, stack.size());
  }
}
