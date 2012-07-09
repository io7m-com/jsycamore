package com.io7m.jsycamore.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jsycamore.Scissor;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScissorRelative;
import com.io7m.jtensors.VectorM2I;

public class ScissorTest
{
  @Test public void testString()
  {
    final Scissor s0 =
      new Scissor(new Point<ScissorRelative>(32, 64), new VectorM2I(128, 256));
    final Scissor s1 =
      new Scissor(new Point<ScissorRelative>(33, 65), new VectorM2I(129, 257));

    Assert.assertFalse(s0.toString().equals(s1.toString()));
  }
}
