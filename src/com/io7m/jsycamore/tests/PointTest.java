package com.io7m.jsycamore.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;

public final class PointTest
{
  @SuppressWarnings("static-method") @Test public void testSetIdentity()
  {
    final Point<ScreenRelative> p = new Point<ScreenRelative>();

    p.setXI(23);
    Assert.assertEquals(23, p.getXI());
    Assert.assertEquals(0, p.getYI());

    p.setYI(71);
    Assert.assertEquals(23, p.getXI());
    Assert.assertEquals(71, p.getYI());
  }

  @SuppressWarnings("static-method") @Test public void testString()
  {
    final Point<ScreenRelative> p = new Point<ScreenRelative>();

    p.setXI(23);
    p.setYI(71);

    Assert.assertEquals("[Point 23 71]", p.toString());
  }

  @SuppressWarnings("static-method") @Test public void testZero()
  {
    final Point<ScreenRelative> p = new Point<ScreenRelative>();
    Assert.assertEquals(0, p.getXI());
    Assert.assertEquals(0, p.getYI());
  }

  @SuppressWarnings("static-method") @Test public void testZeroCopy()
  {
    final Point<ScreenRelative> p0 = new Point<ScreenRelative>(23, 23);
    Assert.assertEquals(23, p0.getXI());
    Assert.assertEquals(23, p0.getYI());

    final Point<ScreenRelative> p1 = new Point<ScreenRelative>(p0);
    Assert.assertEquals(23, p1.getXI());
    Assert.assertEquals(23, p1.getYI());

    Assert.assertNotSame(p1, p0);
  }
}
