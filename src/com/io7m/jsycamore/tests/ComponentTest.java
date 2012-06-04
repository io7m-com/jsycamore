package com.io7m.jsycamore.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.Component.ParentResizeBehavior;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jtensors.VectorI2I;

public final class ComponentTest
{
  @SuppressWarnings("static-method") @Test public void testHeightMaximum()
    throws ConstraintError
  {
    final TestComponent c =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));

    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());

    c.componentSetMaximumHeight(null, 8);

    Assert.assertEquals(Integer.MAX_VALUE, c.componentGetMaximumWidth());
    Assert.assertEquals(8, c.componentGetMaximumHeight());
    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());

    c.componentSetMaximumSize(null, new VectorI2I(5, 9));

    Assert.assertEquals(5, c.componentGetMaximumWidth());
    Assert.assertEquals(9, c.componentGetMaximumHeight());
    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testHeightMaximumBelowMinimum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMinimumHeight(null, 4);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumHeight(null, 3);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testHeightMaximumEqualMinimum()
      throws ConstraintError
  {
    TestComponent c = null;

    c = new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    c.componentSetMinimumHeight(null, 4);
    c.componentSetMaximumHeight(null, 4);
  }

  @SuppressWarnings("static-method") @Test public void testHeightMinimum()
    throws ConstraintError
  {
    final TestComponent c =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));

    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());

    c.componentSetMinimumHeight(null, 8);

    Assert.assertEquals(1, c.componentGetMinimumWidth());
    Assert.assertEquals(8, c.componentGetMinimumHeight());
    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(8, c.componentGetHeight());

    c.componentSetMinimumSize(null, new VectorI2I(5, 8));

    Assert.assertEquals(5, c.componentGetMinimumWidth());
    Assert.assertEquals(8, c.componentGetMinimumHeight());
    Assert.assertEquals(5, c.componentGetWidth());
    Assert.assertEquals(8, c.componentGetHeight());
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testHeightMinimumAboveMaximum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMaximumHeight(null, 4);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumHeight(null, 4);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testHeightMinimumTooLow0()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumHeight(null, 0);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testHeightResizeBehaviourIdentity()
      throws ConstraintError
  {
    final TestComponent c =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));

    for (final ParentResizeBehavior v : Component.ParentResizeBehavior
      .values()) {
      c.componentSetHeightResizeBehavior(v);
      Assert.assertEquals(v, c.componentGetHeightResizeBehavior());
    }
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testInitialNoHeight()
    throws ConstraintError
  {
    new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(1, 0));
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testInitialNoWidth()
    throws ConstraintError
  {
    new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(0, 1));
  }

  @SuppressWarnings("static-method") @Test public void testPositionMaximum()
    throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumPosition(new Point<ParentRelative>(23, 71));

    Assert.assertEquals(23, c.componentGetMaximumX());
    Assert.assertEquals(23, c.componentGetMaximumPosition().getXI());
    Assert.assertEquals(71, c.componentGetMaximumY());
    Assert.assertEquals(71, c.componentGetMaximumPosition().getYI());
  }

  @SuppressWarnings("static-method") @Test public void testPositionMinimum()
    throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumPosition(new Point<ParentRelative>(23, 71));

    Assert.assertEquals(23, c.componentGetMinimumX());
    Assert.assertEquals(23, c.componentGetMinimumPosition().getXI());
    Assert.assertEquals(71, c.componentGetMinimumY());
    Assert.assertEquals(71, c.componentGetMinimumPosition().getYI());
  }

  @SuppressWarnings("static-method") @Test public void testSizeIdentity()
    throws ConstraintError
  {
    final TestComponent c =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));

    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());
  }

  @SuppressWarnings("static-method") @Test public void testWidthMinimum()
    throws ConstraintError
  {
    final TestComponent c =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));

    Assert.assertEquals(3, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());

    c.componentSetMinimumWidth(null, 4);

    Assert.assertEquals(4, c.componentGetMinimumWidth());
    Assert.assertEquals(1, c.componentGetMinimumHeight());
    Assert.assertEquals(4, c.componentGetWidth());
    Assert.assertEquals(7, c.componentGetHeight());

    c.componentSetMinimumSize(null, new VectorI2I(5, 8));

    Assert.assertEquals(5, c.componentGetMinimumWidth());
    Assert.assertEquals(8, c.componentGetMinimumHeight());
    Assert.assertEquals(5, c.componentGetWidth());
    Assert.assertEquals(8, c.componentGetHeight());
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testWidthMinimumAboveMaximum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMaximumWidth(null, 4);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumWidth(null, 4);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testWidthMinimumTooLow0()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumWidth(null, 0);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testWidthResizeBehaviourIdentity()
      throws ConstraintError
  {
    final TestComponent c =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));

    for (final ParentResizeBehavior v : Component.ParentResizeBehavior
      .values()) {
      c.componentSetWidthResizeBehavior(v);
      Assert.assertEquals(v, c.componentGetWidthResizeBehavior());
    }
  }

  @SuppressWarnings("static-method") @Test public void testXMaximum()
    throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumX(23);

    Assert.assertEquals(23, c.componentGetMaximumX());
    Assert.assertEquals(23, c.componentGetMaximumPosition().getXI());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testXMaximumSameMinimum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMinimumX(23);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumX(23);

    Assert.assertEquals(23, c.componentGetMaximumX());
    Assert.assertEquals(23, c.componentGetMaximumPosition().getXI());
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testXMaximumUnderMinimum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMinimumX(23);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumX(22);
  }

  @SuppressWarnings("static-method") @Test public void testXMinimum()
    throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumX(23);

    Assert.assertEquals(23, c.componentGetMinimumX());
    Assert.assertEquals(23, c.componentGetMinimumPosition().getXI());
  }

  @SuppressWarnings("static-method") @Test public void testYMaximum()
    throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumY(23);

    Assert.assertEquals(23, c.componentGetMaximumY());
    Assert.assertEquals(23, c.componentGetMaximumPosition().getYI());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testYMaximumSameMinimum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMinimumY(23);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumY(23);

    Assert.assertEquals(23, c.componentGetMaximumY());
    Assert.assertEquals(23, c.componentGetMaximumPosition().getYI());
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testYMaximumUnderMinimum()
      throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
      c.componentSetMinimumY(23);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMaximumY(22);
  }

  @SuppressWarnings("static-method") @Test public void testYMinimum()
    throws ConstraintError
  {
    TestComponent c = null;

    try {
      c =
        new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(3, 7));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert c != null;
    c.componentSetMinimumY(23);

    Assert.assertEquals(23, c.componentGetMinimumY());
    Assert.assertEquals(23, c.componentGetMinimumPosition().getYI());
  }
}
