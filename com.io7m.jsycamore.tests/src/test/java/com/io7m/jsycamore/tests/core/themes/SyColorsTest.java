/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jsycamore.tests.core.themes;

import com.io7m.jequality.AlmostEqualDouble;
import com.io7m.jsycamore.api.themes.SyColors;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.generators.Vector3DGenerator;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public final class SyColorsTest
{
  private static AlmostEqualDouble.ContextRelative createContextRelative()
  {
    final AlmostEqualDouble.ContextRelative c =
      new AlmostEqualDouble.ContextRelative();
    c.setMaxAbsoluteDifference(0.01);
    return c;
  }

  @Test
  public void testIdentityRGBtoHSV()
  {
    final AlmostEqualDouble.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      Vector3DGenerator.createNormal(),
      new AbstractCharacteristic<Vector3D>()
      {
        @Override
        protected void doSpecify(final Vector3D rgb)
          throws Throwable
        {
          final Vector3D hsv = SyColors.convertRGBtoHSV(rgb);
          final Vector3D rgb_out = SyColors.convertHSVtoRGB(hsv);

          System.out.println("rgb:     " + rgb);
          System.out.println("hsv:     " + hsv);
          System.out.println("rgb_out: " + rgb_out);

          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, rgb.x(), rgb_out.x()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, rgb.y(), rgb_out.y()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, rgb.z(), rgb_out.z()));
        }
      });
  }

  @Test
  @Ignore("Test is currently broken for unknown reasons.")
  public void testIdentityHSVtoRGB()
  {
    final AlmostEqualDouble.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      Vector3DGenerator.createNormal(),
      new AbstractCharacteristic<Vector3D>()
      {
        @Override
        protected void doSpecify(final Vector3D raw)
          throws Throwable
        {
          final Vector3D hsv =
            Vector3D.of(raw.x() * 360.0, raw.y(), raw.z());
          final Vector3D rgb =
            SyColors.convertHSVtoRGB(hsv);
          final Vector3D hsv_out =
            SyColors.convertRGBtoHSV(rgb);

          System.out.println("hsv:     " + hsv);
          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, hsv.x(), hsv_out.x()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, hsv.y(), hsv_out.y()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, hsv.z(), hsv_out.z()));
        }
      });
  }

  @Test
  public void testNoSaturationHSVtoRGB()
  {
    final AlmostEqualDouble.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      Vector3DGenerator.createNormal(),
      new AbstractCharacteristic<Vector3D>()
      {
        @Override
        protected void doSpecify(final Vector3D raw)
          throws Throwable
        {
          final Vector3D hsv =
            Vector3D.of(raw.x() * 360.0, 0.0, raw.z());
          final Vector3D rgb =
            SyColors.convertHSVtoRGB(hsv);
          final Vector3D hsv_out =
            SyColors.convertRGBtoHSV(rgb);

          System.out.println("hsv:     " + hsv);
          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, -1.0, hsv_out.x()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, 0.0, hsv_out.y()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, rgb.x(), hsv_out.z()));
        }
      });
  }

  @Test
  public void testNoSaturationRGBtoHSV()
  {
    final AlmostEqualDouble.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      Vector3DGenerator.createNormal(),
      new AbstractCharacteristic<Vector3D>()
      {
        @Override
        protected void doSpecify(final Vector3D raw)
          throws Throwable
        {
          final Vector3D rgb =
            Vector3D.of(raw.x(), raw.x(), raw.x());
          final Vector3D hsv_out =
            SyColors.convertRGBtoHSV(rgb);

          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, -1.0, hsv_out.x()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, 0.0, hsv_out.y()));
          Assert.assertTrue(
            AlmostEqualDouble.almostEqual(c, rgb.x(), hsv_out.z()));
        }
      });
  }
}
