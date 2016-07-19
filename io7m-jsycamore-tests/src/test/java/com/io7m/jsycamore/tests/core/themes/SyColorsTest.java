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

import com.io7m.jequality.AlmostEqualFloat;
import com.io7m.jsycamore.core.themes.SyColors;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM3F;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import org.junit.Assert;
import org.junit.Test;

public final class SyColorsTest
{
  @Test
  public void testIdentityRGBtoHSV()
  {
    final AlmostEqualFloat.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      new VectorM3FGenerator(),
      new AbstractCharacteristic<VectorM3F>()
      {
        @Override
        protected void doSpecify(final VectorM3F rgb)
          throws Throwable
        {
          final VectorM3F rgb_orig = new VectorM3F(rgb);

          final VectorM3F hsv = new VectorM3F();
          SyColors.convertRGBtoHSV(rgb, hsv);
          Assert.assertEquals(rgb_orig, rgb);

          final VectorM3F rgb_out = new VectorM3F();
          SyColors.convertHSVtoRGB(hsv, rgb_out);

          System.out.println("rgb:     " + rgb);
          System.out.println("hsv:     " + hsv);
          System.out.println("rgb_out: " + rgb_out);

          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getXF(), rgb_out.getXF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getYF(), rgb_out.getYF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getZF(), rgb_out.getZF()));
        }
      });
  }

  @Test
  public void testIdentityRGBtoHSVNew()
  {
    final AlmostEqualFloat.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      new VectorM3FGenerator(),
      new AbstractCharacteristic<VectorM3F>()
      {
        @Override
        protected void doSpecify(final VectorM3F rgb)
          throws Throwable
        {
          final VectorM3F rgb_orig = new VectorM3F(rgb);

          final VectorI3F hsv = SyColors.convertRGBtoHSVNew(rgb);
          Assert.assertEquals(rgb_orig, rgb);

          final VectorI3F rgb_out = SyColors.convertHSVtoRGBNew(hsv);
          System.out.println("rgb:     " + rgb);
          System.out.println("hsv:     " + hsv);
          System.out.println("rgb_out: " + rgb_out);

          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getXF(), rgb_out.getXF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getYF(), rgb_out.getYF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getZF(), rgb_out.getZF()));
        }
      });
  }

  private static AlmostEqualFloat.ContextRelative createContextRelative()
  {
    final AlmostEqualFloat.ContextRelative c =
      new AlmostEqualFloat.ContextRelative();
    c.setMaxAbsoluteDifference(0.01f);
    return c;
  }

  @Test
  public void testIdentityHSVtoRGB()
  {
    final AlmostEqualFloat.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      new VectorM3FGenerator(),
      new AbstractCharacteristic<VectorM3F>()
      {
        @Override
        protected void doSpecify(final VectorM3F raw)
          throws Throwable
        {
          final VectorM3F hsv =
            new VectorM3F(raw.getXF() * 360.0f, raw.getYF(), raw.getZF());
          final VectorM3F hsv_orig =
            new VectorM3F(hsv);

          final VectorM3F rgb = new VectorM3F();
          SyColors.convertHSVtoRGB(hsv, rgb);

          Assert.assertEquals(hsv_orig, hsv);

          final VectorM3F hsv_out = new VectorM3F();
          SyColors.convertRGBtoHSV(rgb, hsv_out);

          System.out.println("hsv:     " + hsv);
          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, hsv.getXF(), hsv_out.getXF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, hsv.getYF(), hsv_out.getYF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, hsv.getZF(), hsv_out.getZF()));
        }
      });
  }

  @Test
  public void testIdentityHSVtoRGBNew()
  {
    final AlmostEqualFloat.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      new VectorM3FGenerator(),
      new AbstractCharacteristic<VectorM3F>()
      {
        @Override
        protected void doSpecify(final VectorM3F raw)
          throws Throwable
        {
          final VectorM3F hsv =
            new VectorM3F(raw.getXF() * 360.0f, raw.getYF(), raw.getZF());
          final VectorM3F hsv_orig =
            new VectorM3F(hsv);

          final VectorI3F rgb = SyColors.convertHSVtoRGBNew(hsv);
          Assert.assertEquals(hsv_orig, hsv);

          final VectorI3F hsv_out = SyColors.convertRGBtoHSVNew(rgb);
          System.out.println("hsv:     " + hsv);
          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, hsv.getXF(), hsv_out.getXF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, hsv.getYF(), hsv_out.getYF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, hsv.getZF(), hsv_out.getZF()));
        }
      });
  }

  @Test
  public void testNoSaturationHSVtoRGB()
  {
    final AlmostEqualFloat.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      new VectorM3FGenerator(),
      new AbstractCharacteristic<VectorM3F>()
      {
        @Override
        protected void doSpecify(final VectorM3F raw)
          throws Throwable
        {
          final VectorM3F hsv =
            new VectorM3F(raw.getXF() * 360.0f, 0.0f, raw.getZF());
          final VectorM3F hsv_orig =
            new VectorM3F(hsv);

          final VectorM3F rgb = new VectorM3F();
          SyColors.convertHSVtoRGB(hsv, rgb);

          Assert.assertEquals(hsv_orig, hsv);

          final VectorM3F hsv_out = new VectorM3F();
          SyColors.convertRGBtoHSV(rgb, hsv_out);

          System.out.println("hsv:     " + hsv);
          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, -1.0f, hsv_out.getXF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, 0.0f, hsv_out.getYF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getXF(), hsv_out.getZF()));
        }
      });
  }

  @Test
  public void testNoSaturationRGBtoHSV()
  {
    final AlmostEqualFloat.ContextRelative c =
      SyColorsTest.createContextRelative();

    QuickCheck.forAllVerbose(
      new VectorM3FGenerator(),
      new AbstractCharacteristic<VectorM3F>()
      {
        @Override
        protected void doSpecify(final VectorM3F raw)
          throws Throwable
        {
          final VectorM3F rgb =
            new VectorM3F(raw.getXF(), raw.getXF(), raw.getXF());

          final VectorM3F hsv_out = new VectorM3F();
          SyColors.convertRGBtoHSV(rgb, hsv_out);

          System.out.println("rgb:     " + rgb);
          System.out.println("hsv_out: " + hsv_out);

          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, -1.0f, hsv_out.getXF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, 0.0f, hsv_out.getYF()));
          Assert.assertTrue(
            AlmostEqualFloat.almostEqual(c, rgb.getXF(), hsv_out.getZF()));
        }
      });
  }
}
