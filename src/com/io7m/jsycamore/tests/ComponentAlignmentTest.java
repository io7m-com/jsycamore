package com.io7m.jsycamore.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jtensors.VectorI2I;

public class ComponentAlignmentTest
{
  @Test public void testContainerBottom()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerBottom(c0, 16);

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test public void testContainerBottomCenter()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerBottomCenter(c0, 16);

    Assert.assertEquals(64 - (4 / 2), c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerBottomCenterNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerBottomCenter(null, 16);
  }

  @Test public void testContainerBottomLeft()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerBottomLeft(c0, 16);

    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerBottomLeftNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerBottomLeft(null, 16);
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerBottomNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerBottom(null, 16);
  }

  @Test public void testContainerBottomRight()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerBottomRight(c0, 16);

    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerBottomRightNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerBottomRight(null, 16);
  }

  @Test public void testContainerLeft()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerLeft(c0, 16);

    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public void testContainerLeftNull()
    throws ConstraintError
  {
    ComponentAlignment.setPositionContainerLeft(null, 16);
  }

  @Test public void testContainerRight()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerRight(c0, 16);

    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerRightNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerRight(null, 16);
  }

  @Test public void testContainerTop()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerTop(c0, 16);

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getYI());
  }

  @Test public void testContainerTopCenter()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerTopCenter(c0, 16);

    Assert.assertEquals(64 - (4 / 2), c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerTopCenterNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerTopCenter(null, 16);
  }

  @Test public void testContainerTopLeft()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerTopLeft(c0, 16);

    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerTopLeftNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerTopLeft(null, 16);
  }

  @Test(expected = ConstraintError.class) public void testContainerTopNull()
    throws ConstraintError
  {
    ComponentAlignment.setPositionContainerTop(null, 16);
  }

  @Test public void testContainerTopRight()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        PointConstants.PARENT_ORIGIN,
        new VectorI2I(4, 4));

    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(0, c0.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionContainerTopRight(c0, 16);

    Assert.assertEquals(128 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(16, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testContainerTopRightNull()
      throws ConstraintError
  {
    ComponentAlignment.setPositionContainerTopRight(null, 16);
  }

  @Test public void testRelativeAbove()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeAbove(c0, 16, c1);

    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeAboveNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeAbove(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeAboveNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeAbove(c0, 16, null);
  }

  @Test public void testRelativeAboveSameX()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(32, 32),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeAboveSameX(c0, 16, c1);

    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64 - 16 - 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeAboveSameXNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeAboveSameX(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeAboveSameXNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeAboveSameX(c0, 16, null);
  }

  @Test public void testRelativeBelow()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeBelow(c0, 16, c1);

    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64 + 16 + 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeBelowNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeBelow(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeBelowNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeBelow(c0, 16, null);
  }

  @Test public void testRelativeBelowSameX()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(32, 32),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeBelowSameX(c0, 16, c1);

    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64 + 16 + 4, c0
      .componentGetPositionParentRelative()
      .getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeBelowSameXNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeBelowSameX(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeBelowSameXNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeBelowSameX(c0, 16, null);
  }

  @Test public void testRelativeLeftOf()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(32, 32),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeLeftOf(c0, 16, c1);

    Assert.assertEquals(64 - 4 - 16, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeLeftOfNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeLeftOf(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeLeftOfNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeLeftOf(c0, 16, null);
  }

  @Test public void testRelativeLeftOfSameY()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(32, 32),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeLeftOfSameY(c0, 16, c1);

    Assert.assertEquals(64 - 4 - 16, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeLeftOfSameYNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeLeftOfSameY(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeLeftOfSameYNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeLeftOfSameY(c0, 16, null);
  }

  @Test public void testRelativeRightOf()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(32, 32),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeRightOf(c0, 16, c1);

    Assert.assertEquals(64 + 4 + 16, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeRightOfNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeRightOf(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeRightOfNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeRightOf(c0, 16, null);
  }

  @Test public void testRelativeRightOfSameY()
    throws ConstraintError
  {
    final TestComponent container =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));
    final TestComponent c0 =
      new TestComponent(
        container,
        new Point<ParentRelative>(32, 32),
        new VectorI2I(4, 4));
    final TestComponent c1 =
      new TestComponent(
        container,
        new Point<ParentRelative>(64, 64),
        new VectorI2I(4, 4));

    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(32, c0.componentGetPositionParentRelative().getYI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getXI());
    Assert.assertEquals(64, c1.componentGetPositionParentRelative().getYI());

    ComponentAlignment.setPositionRelativeRightOfSameY(c0, 16, c1);

    Assert.assertEquals(64 + 4 + 16, c0
      .componentGetPositionParentRelative()
      .getXI());
    Assert.assertEquals(64, c0.componentGetPositionParentRelative().getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeRightOfSameYNull0()
      throws ConstraintError
  {
    ComponentAlignment.setPositionRelativeRightOfSameY(null, 16, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testRelativeRightOfSameYNull1()
      throws ConstraintError
  {
    final TestComponent c0 =
      new TestComponent(PointConstants.PARENT_ORIGIN, new VectorI2I(128, 128));

    ComponentAlignment.setPositionRelativeRightOfSameY(c0, 16, null);
  }
}
