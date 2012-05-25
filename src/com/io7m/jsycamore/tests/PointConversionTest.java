package com.io7m.jsycamore.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConversion;
import com.io7m.jsycamore.geometry.ScissorRelative;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jsycamore.geometry.WindowRelative;
import com.io7m.jtensors.VectorM2I;

public class PointConversionTest
{
  @Test public void testAbsoluteToWindow()
    throws ConstraintError
  {
    final Point<ScreenRelative> p0 = new Point<ScreenRelative>(64, 64);
    final Point<ScreenRelative> p1 = new Point<ScreenRelative>(66, 66);
    final Point<WindowRelative> w = PointConversion.screenToWindow(p0, p1);

    Assert.assertEquals(2, w.getXI());
    Assert.assertEquals(2, w.getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testAbsoluteToWindowNull0()
      throws ConstraintError
  {
    final Point<ScreenRelative> p1 = new Point<ScreenRelative>(66, 66);
    PointConversion.screenToWindow(null, p1);
  }

  @Test(expected = ConstraintError.class) public
    void
    testAbsoluteToWindowNull1()
      throws ConstraintError
  {
    final Point<ScreenRelative> p0 = new Point<ScreenRelative>(64, 64);
    PointConversion.screenToWindow(p0, null);
  }

  @Test public void testScreenScissorIdentity()
    throws ConstraintError
  {
    final Point<ScreenRelative> point = new Point<ScreenRelative>(64, 64);
    final VectorM2I size = new VectorM2I(640, 480);

    final Point<ScissorRelative> sci =
      PointConversion.screenToScissor(size, point);

    Assert.assertEquals(64, sci.getXI());
    Assert.assertEquals(point.getXI(), sci.getXI());
    Assert.assertEquals(416, sci.getYI());

    final Point<ScreenRelative> scr =
      PointConversion.scissorToScreen(size, sci);

    Assert.assertEquals(64, scr.getXI());
    Assert.assertEquals(sci.getXI(), scr.getXI());
    Assert.assertEquals(64, scr.getYI());
  }

  @Test(expected = ConstraintError.class) public
    void
    testScreenToScissorNull0()
      throws ConstraintError
  {
    final Point<ScreenRelative> p0 = new Point<ScreenRelative>(64, 64);
    PointConversion.screenToScissor(null, p0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testScreenToScissorNull1()
      throws ConstraintError
  {
    final Point<ScreenRelative> p0 = new Point<ScreenRelative>(64, 64);
    PointConversion.screenToScissor(p0, null);
  }
}
