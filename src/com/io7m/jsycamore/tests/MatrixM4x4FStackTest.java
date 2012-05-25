package com.io7m.jsycamore.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.ApproximatelyEqualFloat;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.MatrixM4x4FStack;
import com.io7m.jtensors.MatrixM4x4F;

public class MatrixM4x4FStackTest
{
  @Test public void testInitial()
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

  @Test(expected = ConstraintError.class) public void testPeekEmpty()
    throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.peek();
  }

  @Test(expected = ConstraintError.class) public void testPopEmpty()
    throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.pop();
  }

  @Test(expected = ConstraintError.class) public void testPushCopyEmpty()
    throws ConstraintError
  {
    final MatrixM4x4FStack stack = new MatrixM4x4FStack();
    stack.pushCopy();
  }

  @Test public void testPushCopyPopIdentity()
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

  @Test public void testPushPopIdentity()
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
