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

package com.io7m.jsycamore.tests.core.boxes;

import com.io7m.jsycamore.core.SySpaceType;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.boxes.SyHorizontalSplitType;
import com.io7m.jsycamore.core.boxes.SyVerticalSplitType;
import net.java.quickcheck.Generator;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.PrimitiveGenerators;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public final class SyBoxesTest
{
  private static int absoluteDifference(
    final int m,
    final int n)
  {
    return Math.abs(Math.subtractExact(m, n));
  }

  private static void show(final SyBoxType<?>... boxes)
    throws IOException
  {
    final BufferedImage image =
      new BufferedImage(800, 800, BufferedImage.TYPE_4BYTE_ABGR);

    final Graphics2D graphics = image.createGraphics();
    final AffineTransform transform = graphics.getTransform();

    final Random random = new Random(0L);
    for (int index = 0; index < boxes.length; ++index) {
      final float r = random.nextFloat();
      final float g = random.nextFloat();
      final float b = random.nextFloat();
      graphics.setTransform(transform);
      graphics.translate(399, 399);
      graphics.setPaint(new Color(r, g, b));
      final SyBoxType<?> box = boxes[index];
      graphics.fillRect(
        box.minimumX(),
        box.minimumY(),
        box.width(),
        box.height());
    }

    graphics.setTransform(transform);
    graphics.setPaint(Color.BLACK);
    graphics.drawRect(0, 0, 799, 799);

    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        final JLabel icon = new JLabel(new ImageIcon(image));
        final JFrame frame = new JFrame("Image");
        frame.setPreferredSize(new Dimension(800, 800));
        frame.getContentPane().add(icon);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
      }
    });

    System.in.read();
  }

  @Test
  public void testEqualsHashcode()
    throws Exception
  {
    QuickCheck.forAllVerbose(
      new SyBoxGenerator<SySpaceType>(),
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> box)
          throws Throwable
        {
          final SyBoxType<SySpaceType> other =
            SyBox.of(
              box.minimumX(),
              box.maximumX(),
              box.minimumY(),
              box.maximumY());
          Assert.assertEquals(box, box);
          Assert.assertEquals(box, other);
          Assert.assertEquals(other, box);
          Assert.assertEquals((long) box.hashCode(), (long) other.hashCode());

          Assert.assertNotEquals(box, null);
          Assert.assertNotEquals(box, Integer.valueOf(23));
        }
      });
  }

  @Test
  public void testShowAll()
    throws Exception
  {
    final SyBoxGenerator<SySpaceType> box_gen = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      box_gen,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> any)
          throws Throwable
        {
          Assert.assertEquals(SyBoxes.show(any), SyBoxes.show(any));

          final SyBoxType<SySpaceType> next = box_gen.next();
          if (!Objects.equals(next, any)) {
            Assert.assertNotEquals(SyBoxes.show(any), SyBoxes.show(next));
          } else {
            Assert.assertEquals(SyBoxes.show(any), SyBoxes.show(next));
          }
        }
      });
  }

  @Test
  public void testZero()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 0, 0, 0);
    Assert.assertEquals(0L, (long) box.width());
    Assert.assertEquals(0L, (long) box.height());
  }

  @Test
  public void testHollowOut()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 99, 0, 99);
    final SyBoxType<SySpaceType> inner = SyBoxes.hollowOut(
      outer,
      10,
      20,
      30,
      40);

    Assert.assertEquals(10L, (long) inner.minimumX());
    Assert.assertEquals(99L - 20L, (long) inner.maximumX());
    Assert.assertEquals(30L, (long) inner.minimumY());
    Assert.assertEquals(99L - 40L, (long) inner.maximumY());
  }

  @Test
  public void testHollowOutSelfAll()
    throws Exception
  {
    QuickCheck.forAllVerbose(
      new SyBoxGenerator<SySpaceType>(),
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> any)
          throws Throwable
        {
          Assert.assertEquals(any, SyBoxes.hollowOut(any, 0, 0, 0, 0));
        }
      });
  }

  @Test
  public void testHollowOutEvenlyAll()
    throws Exception
  {
    final Generator<Integer> int_gen =
      PrimitiveGenerators.integers(0, 100);

    QuickCheck.forAllVerbose(
      new SyBoxGenerator<SySpaceType>(),
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> any)
          throws Throwable
        {
          final int offset = int_gen.next().intValue();
          final SyBoxType<SySpaceType> hollow0 =
            SyBoxes.hollowOut(any, offset, offset, offset, offset);
          final SyBoxType<SySpaceType> hollow1 =
            SyBoxes.hollowOutEvenly(any, offset);
          Assert.assertEquals(hollow0, hollow1);
        }
      });
  }

  @Test
  public void testHollowOutTooLargeLeft()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 100, 0, 100);
    final SyBoxType<SySpaceType> inner = SyBoxes.hollowOut(
      outer,
      120,
      20,
      30,
      40);

    Assert.assertEquals(100L, (long) inner.minimumX());
    Assert.assertEquals(100L, (long) inner.maximumX());
    Assert.assertEquals(30L, (long) inner.minimumY());
    Assert.assertEquals(100L - 40L, (long) inner.maximumY());
  }

  @Test
  public void testHollowOutTooLargeRight()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 100, 0, 100);
    final SyBoxType<SySpaceType> inner = SyBoxes.hollowOut(
      outer,
      10,
      120,
      30,
      40);

    Assert.assertEquals(10L, (long) inner.minimumX());
    Assert.assertEquals(10L, (long) inner.maximumX());
    Assert.assertEquals(30L, (long) inner.minimumY());
    Assert.assertEquals(100L - 40L, (long) inner.maximumY());
  }

  @Test
  public void testHollowOutTooLargeTop()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 100, 0, 100);
    final SyBoxType<SySpaceType> inner = SyBoxes.hollowOut(
      outer,
      10,
      20,
      120,
      40);

    Assert.assertEquals(10L, (long) inner.minimumX());
    Assert.assertEquals(100L - 20L, (long) inner.maximumX());
    Assert.assertEquals(100L, (long) inner.minimumY());
    Assert.assertEquals(100L, (long) inner.maximumY());
  }

  @Test
  public void testHollowOutTooLargeBottom()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 100, 0, 100);
    final SyBoxType<SySpaceType> inner = SyBoxes.hollowOut(
      outer,
      10,
      20,
      30,
      120);

    Assert.assertEquals(10L, (long) inner.minimumX());
    Assert.assertEquals(100L - 20L, (long) inner.maximumX());
    Assert.assertEquals(30L, (long) inner.minimumY());
    Assert.assertEquals(30L, (long) inner.maximumY());
  }

  @Test
  public void testAlignHorizontallyCenterSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 100, 0, 100);
    final SyBoxType<SySpaceType> inner = SyBox.of(0, 10, 0, 10);

    Assert.assertEquals(100L, (long) outer.width());
    Assert.assertEquals(100L, (long) outer.height());

    Assert.assertEquals(10L, (long) inner.width());
    Assert.assertEquals(10L, (long) inner.height());

    final SyBoxType<SySpaceType> aligned = SyBoxes.alignHorizontallyCenter(
      outer,
      inner);

    Assert.assertEquals(10L, (long) aligned.width());
    Assert.assertEquals(10L, (long) aligned.height());
    Assert.assertEquals(45L, (long) aligned.minimumX());
    Assert.assertEquals(55L, (long) aligned.maximumX());
  }

  @Test
  public void testAlignHorizontallyLeftAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignHorizontallyLeft(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.minimumY(),
            (long) aligned.minimumY());
          Assert.assertEquals(
            (long) inner.maximumY(),
            (long) aligned.maximumY());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.minimumX(),
            (long) outer.minimumX());
        }
      });
  }

  @Test
  public void testAlignHorizontallyLeftOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignHorizontallyLeftOffset(outer, inner, offset);

          Assert.assertEquals(
            (long) inner.minimumY(),
            (long) aligned.minimumY());
          Assert.assertEquals(
            (long) inner.maximumY(),
            (long) aligned.maximumY());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.minimumX(),
            (long) outer.minimumX() + (long) offset);
        }
      });
  }

  @Test
  public void testAlignHorizontallyRightAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignHorizontallyRight(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.minimumY(),
            (long) aligned.minimumY());
          Assert.assertEquals(
            (long) inner.maximumY(),
            (long) aligned.maximumY());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.maximumX(),
            (long) outer.maximumX());
        }
      });
  }

  @Test
  public void testAlignHorizontallyRightOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignHorizontallyRightOffset(outer, inner, offset);

          Assert.assertEquals(
            (long) inner.minimumY(),
            (long) aligned.minimumY());
          Assert.assertEquals(
            (long) inner.maximumY(),
            (long) aligned.maximumY());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.maximumX(),
            (long) outer.maximumX() - (long) offset);
        }
      });
  }

  @Test
  public void testAlignHorizontallyCenterAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignHorizontallyCenter(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.minimumY(),
            (long) aligned.minimumY());
          Assert.assertEquals(
            (long) inner.maximumY(),
            (long) aligned.maximumY());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          final int diff_left =
            SyBoxesTest.absoluteDifference(
              outer.minimumX(),
              aligned.minimumX());
          final int diff_right =
            SyBoxesTest.absoluteDifference(
              outer.maximumX(),
              aligned.maximumX());
          Assert.assertTrue(
            SyBoxesTest.absoluteDifference(diff_right, diff_left) <= 1);
        }
      });
  }

  @Test
  public void testAlignVerticallyCenterSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(0, 100, 0, 100);
    final SyBoxType<SySpaceType> inner = SyBox.of(0, 10, 0, 10);

    Assert.assertEquals(100L, (long) outer.width());
    Assert.assertEquals(100L, (long) outer.height());

    Assert.assertEquals(10L, (long) inner.width());
    Assert.assertEquals(10L, (long) inner.height());

    final SyBoxType<SySpaceType> aligned =
      SyBoxes.alignVerticallyCenter(outer, inner);

    Assert.assertEquals(10L, (long) aligned.width());
    Assert.assertEquals(10L, (long) aligned.height());
    Assert.assertEquals(0L, (long) aligned.minimumX());
    Assert.assertEquals(10L, (long) aligned.maximumX());
    Assert.assertEquals(45L, (long) aligned.minimumY());
    Assert.assertEquals(55L, (long) aligned.maximumY());
  }

  @Test
  public void testAlignVerticallyCenterSpecific2()
    throws Exception
  {
    final SyBoxType<SySpaceType> outer = SyBox.of(6, 634, 0, 16);
    final SyBoxType<SySpaceType> inner = SyBox.of(3, 13, 6, 16);

    Assert.assertEquals(628L, (long) outer.width());
    Assert.assertEquals(16L, (long) outer.height());
    Assert.assertEquals(10L, (long) inner.width());
    Assert.assertEquals(10L, (long) inner.height());

    final SyBoxType<SySpaceType> aligned =
      SyBoxes.alignVerticallyCenter(outer, inner);

    Assert.assertEquals(10L, (long) aligned.width());
    Assert.assertEquals(10L, (long) aligned.height());

    Assert.assertEquals(3L, (long) aligned.minimumX());
    Assert.assertEquals(13L, (long) aligned.maximumX());
    Assert.assertEquals(3L, (long) aligned.minimumY());
    Assert.assertEquals(13L, (long) aligned.maximumY());
  }

  @Test
  public void testAlignVerticallyCenterAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignVerticallyCenter(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) inner.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          final int diff_top =
            SyBoxesTest.absoluteDifference(
              outer.minimumY(),
              aligned.minimumY());
          final int diff_bottom =
            SyBoxesTest.absoluteDifference(
              outer.maximumY(),
              aligned.maximumY());
          final int diff_diff =
            SyBoxesTest.absoluteDifference(diff_bottom, diff_top);
          Assert.assertTrue(diff_diff <= 1);
        }
      });
  }

  @Test
  public void testAlignVerticallyTopAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignVerticallyTop(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) inner.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.minimumY(),
            (long) outer.minimumY());
        }
      });
  }

  @Test
  public void testAlignVerticallyTopOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignVerticallyTopOffset(outer, inner, offset);

          Assert.assertEquals(
            (long) inner.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) inner.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.minimumY(),
            (long) outer.minimumY() + (long) offset);
        }
      });
  }

  @Test
  public void testAlignVerticallyBottomAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignVerticallyBottom(outer, inner);

          Assert.assertEquals(
            (long) inner.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) inner.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.maximumY(),
            (long) outer.maximumY());
        }
      });
  }

  @Test
  public void testAlignVerticallyBottomOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset = int_gen.next().intValue();
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignVerticallyBottomOffset(outer, inner, offset);

          Assert.assertEquals(
            (long) inner.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) inner.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) aligned.maximumY(),
            (long) outer.maximumY() - (long) offset);
        }
      });
  }

  @Test
  public void testAlignCenterAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignCenter(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          final int diff_left =
            SyBoxesTest.absoluteDifference(
              outer.minimumX(),
              aligned.minimumX());
          final int diff_right =
            SyBoxesTest.absoluteDifference(
              outer.maximumX(),
              aligned.maximumX());
          final int diff_top =
            SyBoxesTest.absoluteDifference(
              outer.minimumY(),
              aligned.minimumY());
          final int diff_bottom =
            SyBoxesTest.absoluteDifference(
              outer.maximumY(),
              aligned.maximumY());

          Assert.assertTrue(
            SyBoxesTest.absoluteDifference(diff_top, diff_bottom) <= 1);
          Assert.assertTrue(
            SyBoxesTest.absoluteDifference(diff_right, diff_left) <= 1);
        }
      });
  }

  @Test
  public void testAlignTopLeftAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignTopLeft(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) aligned.minimumY());
        }
      });
  }

  @Test
  public void testAlignTopLeftOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset_top = int_gen.next().intValue();
          final int offset_left = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignTopLeftOffset(outer, inner, offset_left, offset_top);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.minimumX() + (long) offset_left,
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) outer.minimumY() + (long) offset_top,
            (long) aligned.minimumY());
        }
      });
  }

  @Test
  public void testAlignBottomLeftAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignBottomLeft(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) aligned.maximumY());
        }
      });
  }

  @Test
  public void testAlignBottomLeftOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset_bottom = int_gen.next().intValue();
          final int offset_left = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignBottomLeftOffset(
              outer,
              inner,
              offset_left,
              offset_bottom);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.minimumX() + (long) offset_left,
            (long) aligned.minimumX());
          Assert.assertEquals(
            (long) outer.maximumY() - (long) offset_bottom,
            (long) aligned.maximumY());
        }
      });
  }

  @Test
  public void testAlignBottomRightAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignBottomRight(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) aligned.maximumY());
        }
      });
  }

  @Test
  public void testAlignBottomRightOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset_bottom = int_gen.next().intValue();
          final int offset_right = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignBottomRightOffset(
              outer,
              inner,
              offset_right,
              offset_bottom);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.maximumX() - (long) offset_right,
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) outer.maximumY() - (long) offset_bottom,
            (long) aligned.maximumY());
        }
      });
  }

  @Test
  public void testAlignTopRightAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned = SyBoxes.alignTopRight(
            outer,
            inner);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) aligned.minimumY());
        }
      });
  }

  @Test
  public void testAlignTopRightOffsetAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int offset_top = int_gen.next().intValue();
          final int offset_right = int_gen.next().intValue();

          final SyBoxType<SySpaceType> inner = generator.next();
          final SyBoxType<SySpaceType> aligned =
            SyBoxes.alignTopRightOffset(
              outer,
              inner,
              offset_right,
              offset_top);

          Assert.assertEquals(
            (long) inner.width(),
            (long) aligned.width());
          Assert.assertEquals(
            (long) inner.height(),
            (long) aligned.height());

          Assert.assertEquals(
            (long) outer.maximumX() - (long) offset_right,
            (long) aligned.maximumX());
          Assert.assertEquals(
            (long) outer.minimumY() + (long) offset_top,
            (long) aligned.minimumY());
        }
      });
  }

  @Test
  public void testSetSizeFromTopLeftAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int expected_width = int_gen.next().intValue();
          final int expected_height = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.setSizeFromTopLeft(
              outer,
              expected_width,
              expected_height);

          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) resized.maximumX());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) resized.maximumY());
          Assert.assertEquals((long) expected_width, (long) resized.width());
          Assert.assertEquals((long) expected_height, (long) resized.height());
        }
      });
  }

  @Test
  public void testSetSizeFromTopRightAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int expected_width = int_gen.next().intValue();
          final int expected_height = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.setSizeFromTopRight(
              outer,
              expected_width,
              expected_height);

          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) resized.minimumX());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) resized.maximumY());
          Assert.assertEquals((long) expected_width, (long) resized.width());
          Assert.assertEquals((long) expected_height, (long) resized.height());
        }
      });
  }

  @Test
  public void testSetSizeFromBottomLeftAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int expected_width = int_gen.next().intValue();
          final int expected_height = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.setSizeFromBottomLeft(
              outer,
              expected_width,
              expected_height);

          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) resized.maximumX());
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) resized.minimumY());
          Assert.assertEquals((long) expected_width, (long) resized.width());
          Assert.assertEquals((long) expected_height, (long) resized.height());
        }
      });
  }

  @Test
  public void testSetSizeFromBottomRightAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int expected_width = int_gen.next().intValue();
          final int expected_height = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.setSizeFromBottomRight(
              outer,
              expected_width,
              expected_height);

          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) resized.minimumX());
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) resized.minimumY());
          Assert.assertEquals((long) expected_width, (long) resized.width());
          Assert.assertEquals((long) expected_height, (long) resized.height());
        }
      });
  }

  @Test
  public void testSetSizeFromCenterAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int expected_width = int_gen.next().intValue();
          final int expected_height = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.setSizeFromCenter(outer, expected_width, expected_height);

          Assert.assertEquals((long) expected_width, (long) resized.width());
          Assert.assertEquals((long) expected_height, (long) resized.height());

          final int diff_left =
            SyBoxesTest.absoluteDifference(
              outer.minimumX(),
              resized.minimumX());
          final int diff_right =
            SyBoxesTest.absoluteDifference(
              outer.maximumX(),
              resized.maximumX());
          final int diff_top =
            SyBoxesTest.absoluteDifference(
              outer.minimumY(),
              resized.minimumY());
          final int diff_bottom =
            SyBoxesTest.absoluteDifference(
              outer.maximumY(),
              resized.maximumY());

          Assert.assertTrue(
            SyBoxesTest.absoluteDifference(diff_top, diff_bottom) <= 1);
          Assert.assertTrue(
            SyBoxesTest.absoluteDifference(diff_right, diff_left) <= 1);
        }
      });
  }

  @Test
  public void testOverlapsFalseSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);

    final SyBoxType<SySpaceType> left = SyBox.of(-10, -1, 0, 10);
    final SyBoxType<SySpaceType> right = SyBox.of(11, 20, 0, 10);
    final SyBoxType<SySpaceType> top = SyBox.of(0, 10, -10, -1);
    final SyBoxType<SySpaceType> bottom = SyBox.of(0, 10, 11, 20);

    Assert.assertFalse(SyBoxes.overlaps(box, left));
    Assert.assertFalse(SyBoxes.overlaps(box, right));
    Assert.assertFalse(SyBoxes.overlaps(box, top));
    Assert.assertFalse(SyBoxes.overlaps(box, bottom));

    Assert.assertFalse(SyBoxes.overlaps(left, box));
    Assert.assertFalse(SyBoxes.overlaps(right, box));
    Assert.assertFalse(SyBoxes.overlaps(top, box));
    Assert.assertFalse(SyBoxes.overlaps(bottom, box));
  }

  @Test
  public void testOverlapsTrueSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);

    final SyBoxType<SySpaceType> left = SyBox.of(-10, 1, 0, 10);
    final SyBoxType<SySpaceType> right = SyBox.of(9, 20, 0, 10);
    final SyBoxType<SySpaceType> top = SyBox.of(0, 10, -10, 1);
    final SyBoxType<SySpaceType> bottom = SyBox.of(0, 10, 9, 20);

    Assert.assertTrue(SyBoxes.overlaps(box, left));
    Assert.assertTrue(SyBoxes.overlaps(box, right));
    Assert.assertTrue(SyBoxes.overlaps(box, top));
    Assert.assertTrue(SyBoxes.overlaps(box, bottom));

    Assert.assertTrue(SyBoxes.overlaps(left, box));
    Assert.assertTrue(SyBoxes.overlaps(right, box));
    Assert.assertTrue(SyBoxes.overlaps(top, box));
    Assert.assertTrue(SyBoxes.overlaps(bottom, box));
  }

  @Test
  public void testOverlapsSelfAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          if (outer.height() > 0 && outer.width() > 0) {
            Assert.assertTrue(SyBoxes.overlaps(outer, outer));
          } else {
            Assert.assertFalse(SyBoxes.overlaps(outer, outer));
          }
        }
      });
  }

  @Test
  public void testContainsSelfAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          Assert.assertTrue(SyBoxes.contains(outer, outer));
        }
      });
  }

  @Test
  public void testContainsFalseSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);

    final SyBoxType<SySpaceType> left = SyBox.of(-10, -1, 0, 10);
    final SyBoxType<SySpaceType> right = SyBox.of(11, 20, 0, 10);
    final SyBoxType<SySpaceType> top = SyBox.of(0, 10, -10, -1);
    final SyBoxType<SySpaceType> bottom = SyBox.of(0, 10, 11, 20);

    Assert.assertFalse(SyBoxes.contains(box, left));
    Assert.assertFalse(SyBoxes.contains(box, right));
    Assert.assertFalse(SyBoxes.contains(box, top));
    Assert.assertFalse(SyBoxes.contains(box, bottom));

    Assert.assertFalse(SyBoxes.contains(left, box));
    Assert.assertFalse(SyBoxes.contains(right, box));
    Assert.assertFalse(SyBoxes.contains(top, box));
    Assert.assertFalse(SyBoxes.contains(bottom, box));
  }

  @Test
  public void testContainsTrueAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 100);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int left_offset = int_gen.next().intValue();
          final int right_offset = int_gen.next().intValue();
          final int top_offset = int_gen.next().intValue();
          final int bottom_offset = int_gen.next().intValue();
          final SyBoxType<SySpaceType> inner =
            SyBoxes.hollowOut(
              outer, left_offset, right_offset, top_offset, bottom_offset);

          Assert.assertTrue(SyBoxes.contains(outer, inner));
          Assert.assertFalse(SyBoxes.contains(inner, outer));
        }
      });
  }

  @Test
  public void testCouldFitInsideSelfAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          Assert.assertTrue(SyBoxes.couldFitInside(outer, outer));
        }
      });
  }

  @Test
  public void testCouldFitInsideAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(0, 100);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> inner = SyBoxes.hollowOutEvenly(
            outer,
            int_gen.next().intValue());
          Assert.assertTrue(SyBoxes.couldFitInside(inner, outer));
        }
      });
  }

  @Test
  public void testContainsPointAll()
    throws Exception
  {
    final Random random = new Random(0L);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int w = Math.max(1, outer.width());
          final int h = Math.max(1, outer.height());
          final int px = random.nextInt(w);
          final int py = random.nextInt(h);
          final int ppx = outer.minimumX() + px;
          final int ppy = outer.minimumY() + py;

          System.out.printf(
            "%d %d\n",
            Integer.valueOf(ppx),
            Integer.valueOf(ppy));

          if (outer.width() > 0 && outer.height() > 0) {
            Assert.assertTrue(SyBoxes.containsPoint(outer, ppx, ppy));
          } else {
            Assert.assertFalse(SyBoxes.containsPoint(outer, ppx, ppy));
          }
        }
      });
  }

  @Test
  public void testContainsZeroWidth()
    throws Exception
  {
    final Random random = new Random(0L);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int w = Math.max(1, outer.width());
          final int h = Math.max(1, outer.height());
          final int px = random.nextInt(w);
          final int py = random.nextInt(h);
          final int ppx = outer.minimumX() + px;
          final int ppy = outer.minimumY() + py;

          final SyBoxType<SySpaceType> scaled = SyBoxes.create(
            outer.minimumX(),
            outer.minimumY(),
            0,
            outer.height());

          System.out.printf(
            "%d %d\n",
            Integer.valueOf(ppx),
            Integer.valueOf(ppy));

          Assert.assertFalse(SyBoxes.containsPoint(scaled, ppx, ppy));
        }
      });
  }

  @Test
  public void testContainsZeroHeight()
    throws Exception
  {
    final Random random = new Random(0L);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int w = Math.max(1, outer.width());
          final int h = Math.max(1, outer.height());
          final int px = random.nextInt(w);
          final int py = random.nextInt(h);
          final int ppx = outer.minimumX() + px;
          final int ppy = outer.minimumY() + py;

          final SyBoxType<SySpaceType> scaled = SyBoxes.create(
            outer.minimumX(),
            outer.minimumY(),
            outer.width(),
            0);

          System.out.printf(
            "%d %d\n",
            Integer.valueOf(ppx),
            Integer.valueOf(ppy));

          Assert.assertFalse(SyBoxes.containsPoint(scaled, ppx, ppy));
        }
      });
  }

  @Test
  public void testFitBetweenHorizontalAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> left = generator.next();
          final SyBoxType<SySpaceType> right = generator.next();

          final int leftmost = Math.min(
            Math.min(left.minimumX(), left.maximumX()),
            Math.min(right.minimumX(), right.maximumX()));
          final int rightmost = Math.max(
            Math.max(left.minimumX(), left.maximumX()),
            Math.max(right.minimumX(), right.maximumX()));

          final SyBoxType<SySpaceType> fitted =
            SyBoxes.fitBetweenHorizontal(outer, left, right);

          Assert.assertTrue(fitted.minimumX() >= leftmost);
          Assert.assertTrue(fitted.maximumX() <= rightmost);
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) fitted.minimumY());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) fitted.maximumY());
        }
      });
  }

  @Test
  public void testFitBetweenVerticalAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> top = generator.next();
          final SyBoxType<SySpaceType> bottom = generator.next();

          final int topmost = Math.min(
            Math.min(top.minimumY(), top.maximumY()),
            Math.min(bottom.minimumY(), bottom.maximumY()));
          final int bottommost = Math.max(
            Math.max(top.minimumY(), top.maximumY()),
            Math.max(bottom.minimumY(), bottom.maximumY()));

          final SyBoxType<SySpaceType> fitted =
            SyBoxes.fitBetweenVertical(outer, top, bottom);

          Assert.assertTrue(fitted.minimumY() >= topmost);
          Assert.assertTrue(fitted.maximumY() <= bottommost);
          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) fitted.minimumX());
          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) fitted.maximumX());
        }
      });
  }

  @Test
  public void testSplitAlongHorizontalAll()
    throws Exception
  {
    final Random random = new Random(0L);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int height = random.nextInt(outer.height() + 1);
          final SyHorizontalSplitType<SySpaceType> pair =
            SyBoxes.splitAlongHorizontal(outer, height);

          final SyBoxType<SySpaceType> lower = pair.lower();
          final SyBoxType<SySpaceType> upper = pair.upper();

          System.out.println("height: " + height);
          System.out.println("lower:  " + lower);
          System.out.println("upper:  " + upper);

          Assert.assertTrue(lower.height() <= outer.height());
          Assert.assertTrue(upper.height() <= outer.height());
          Assert.assertEquals(
            (long) outer.height(),
            (long) (lower.height() + upper.height()));
          Assert.assertEquals((long) outer.width(), (long) lower.width());
          Assert.assertEquals((long) outer.width(), (long) upper.width());

          if (outer.width() > 0 && outer.height() > 0) {
            final boolean lower_ok = lower.width() > 0 && lower.height() > 0;
            final boolean upper_ok = upper.width() > 0 && upper.height() > 0;
            if (lower_ok) {
              Assert.assertTrue(SyBoxes.overlaps(outer, lower));
              Assert.assertTrue(SyBoxes.contains(outer, lower));
              if (upper_ok) {
                Assert.assertFalse(SyBoxes.overlaps(lower, upper));
              }
            }
            if (upper_ok) {
              Assert.assertTrue(SyBoxes.overlaps(outer, upper));
              Assert.assertTrue(SyBoxes.contains(outer, upper));
              if (lower_ok) {
                Assert.assertFalse(SyBoxes.overlaps(lower, upper));
              }
            }
          }
        }
      });
  }

  @Test
  public void testSplitAlongHorizontalSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);
    final SyHorizontalSplitType<SySpaceType> pair =
      SyBoxes.splitAlongHorizontal(box, 5);

    final SyBoxType<SySpaceType> lower = pair.lower();
    final SyBoxType<SySpaceType> upper = pair.upper();

    Assert.assertEquals(0L, (long) lower.minimumX());
    Assert.assertEquals(10L, (long) lower.maximumX());
    Assert.assertEquals(0L, (long) upper.minimumX());
    Assert.assertEquals(10L, (long) upper.maximumX());

    Assert.assertEquals(5L, (long) lower.minimumY());
    Assert.assertEquals(10L, (long) lower.maximumY());
    Assert.assertEquals(0L, (long) upper.minimumY());
    Assert.assertEquals(5L, (long) upper.maximumY());

    Assert.assertEquals(5L, (long) lower.height());
    Assert.assertEquals(5L, (long) upper.height());
  }

  @Test
  public void testSplitAlongHorizontalTiny()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);
    final SyHorizontalSplitType<SySpaceType> pair =
      SyBoxes.splitAlongHorizontal(box, 20);

    final SyBoxType<SySpaceType> lower = pair.lower();
    final SyBoxType<SySpaceType> upper = pair.upper();

    Assert.assertEquals(0L, (long) lower.minimumX());
    Assert.assertEquals(10L, (long) lower.maximumX());
    Assert.assertEquals(0L, (long) upper.minimumX());
    Assert.assertEquals(10L, (long) upper.maximumX());

    Assert.assertEquals(10L, (long) lower.minimumY());
    Assert.assertEquals(10L, (long) lower.maximumY());
    Assert.assertEquals(0L, (long) upper.minimumY());
    Assert.assertEquals(10L, (long) upper.maximumY());

    Assert.assertEquals(0L, (long) lower.height());
    Assert.assertEquals(10L, (long) upper.height());
  }

  @Test
  public void testSplitAlongVerticalSpecific()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);
    final SyVerticalSplitType<SySpaceType> pair =
      SyBoxes.splitAlongVertical(box, 5);

    final SyBoxType<SySpaceType> left = pair.left();
    final SyBoxType<SySpaceType> right = pair.right();

    Assert.assertEquals(0L, (long) left.minimumY());
    Assert.assertEquals(10L, (long) left.maximumY());
    Assert.assertEquals(0L, (long) right.minimumY());
    Assert.assertEquals(10L, (long) right.maximumY());

    Assert.assertEquals(0L, (long) left.minimumX());
    Assert.assertEquals(5L, (long) left.maximumX());
    Assert.assertEquals(5L, (long) right.minimumX());
    Assert.assertEquals(10L, (long) right.maximumX());

    Assert.assertEquals(5L, (long) left.width());
    Assert.assertEquals(5L, (long) right.width());
  }

  @Test
  public void testSplitAlongVerticalTiny()
    throws Exception
  {
    final SyBoxType<SySpaceType> box = SyBox.of(0, 10, 0, 10);
    final SyVerticalSplitType<SySpaceType> pair =
      SyBoxes.splitAlongVertical(box, 20);

    final SyBoxType<SySpaceType> left = pair.left();
    final SyBoxType<SySpaceType> right = pair.right();

    Assert.assertEquals(0L, (long) left.minimumY());
    Assert.assertEquals(10L, (long) left.maximumY());
    Assert.assertEquals(0L, (long) right.minimumY());
    Assert.assertEquals(10L, (long) right.maximumY());

    Assert.assertEquals(0L, (long) left.minimumX());
    Assert.assertEquals(10L, (long) left.maximumX());
    Assert.assertEquals(10L, (long) right.minimumX());
    Assert.assertEquals(10L, (long) right.maximumX());

    Assert.assertEquals(10L, (long) left.width());
    Assert.assertEquals(0L, (long) right.width());
  }

  @Test
  public void testMoveRelative()
    throws Exception
  {
    final Generator<Integer> int_gen =
      PrimitiveGenerators.integers(-1_000_000, 1_000_000);

    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x = int_gen.next().intValue();
          final int y = int_gen.next().intValue();

          final SyBoxType<SySpaceType> moved = SyBoxes.moveRelative(
            outer,
            x,
            y);

          Assert.assertEquals((long) outer.width(), (long) moved.width());
          Assert.assertEquals((long) outer.height(), (long) moved.height());
          Assert.assertEquals(
            (long) Math.abs(x),
            (long) absoluteDifference(outer.minimumX(), moved.minimumX()));
          Assert.assertEquals(
            (long) Math.abs(y),
            (long) absoluteDifference(outer.minimumY(), moved.minimumY()));
        }
      });
  }

  @Test
  public void testMoveAbsolute()
    throws Exception
  {
    final Generator<Integer> int_gen =
      PrimitiveGenerators.integers(-1_000_000, 1_000_000);

    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x = int_gen.next().intValue();
          final int y = int_gen.next().intValue();

          final SyBoxType<SySpaceType> moved = SyBoxes.moveAbsolute(
            outer,
            x,
            y);

          Assert.assertEquals((long) outer.width(), (long) moved.width());
          Assert.assertEquals((long) outer.height(), (long) moved.height());
          Assert.assertEquals((long) x, (long) moved.minimumX());
          Assert.assertEquals((long) y, (long) moved.minimumY());
        }
      });
  }

  @Test
  public void testCreateAll()
    throws Exception
  {
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final SyBoxType<SySpaceType> c =
            SyBoxes.create(
              outer.minimumX(),
              outer.minimumY(),
              outer.width(),
              outer.height());

          Assert.assertEquals(outer, c);
        }
      });
  }

  @Test
  public void testSplitAlongVerticalAll()
    throws Exception
  {
    final Random random = new Random(0L);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int width = random.nextInt(outer.width() + 1);
          final SyVerticalSplitType<SySpaceType> pair =
            SyBoxes.splitAlongVertical(outer, width);

          final SyBoxType<SySpaceType> left = pair.left();
          final SyBoxType<SySpaceType> right = pair.right();

          System.out.println("height: " + width);
          System.out.println("left:   " + left);
          System.out.println("right:  " + right);

          Assert.assertTrue(left.width() <= outer.width());
          Assert.assertTrue(right.width() <= outer.width());
          Assert.assertEquals(
            (long) outer.width(),
            (long) (left.width() + right.width()));
          Assert.assertEquals((long) outer.height(), (long) left.height());
          Assert.assertEquals((long) outer.height(), (long) right.height());

          if (outer.width() > 0 && outer.height() > 0) {
            final boolean lower_ok = left.width() > 0 && left.height() > 0;
            final boolean upper_ok = right.width() > 0 && right.height() > 0;
            if (lower_ok) {
              Assert.assertTrue(SyBoxes.overlaps(outer, left));
              Assert.assertTrue(SyBoxes.contains(outer, left));
            }
            if (upper_ok) {
              Assert.assertTrue(SyBoxes.overlaps(outer, right));
              Assert.assertTrue(SyBoxes.contains(outer, right));
            }
          }
        }
      });
  }

  @Test
  public void testScaleFromTopLeftAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x_diff = int_gen.next().intValue();
          final int y_diff = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.scaleFromTopLeft(outer, x_diff, y_diff);

          Assert.assertEquals(
            (long) Math.max(0, outer.width() + x_diff),
            (long) resized.width());
          Assert.assertEquals(
            (long) Math.max(0, outer.height() + y_diff),
            (long) resized.height());

          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) resized.maximumX());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) resized.maximumY());
        }
      });
  }

  @Test
  public void testScaleFromTopRightAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x_diff = int_gen.next().intValue();
          final int y_diff = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.scaleFromTopRight(outer, x_diff, y_diff);

          Assert.assertEquals(
            (long) Math.max(0, outer.width() + x_diff),
            (long) resized.width());
          Assert.assertEquals(
            (long) Math.max(0, outer.height() + y_diff),
            (long) resized.height());

          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) resized.minimumX());
          Assert.assertEquals(
            (long) outer.maximumY(),
            (long) resized.maximumY());
        }
      });
  }

  @Test
  public void testScaleFromBottomRightAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x_diff = int_gen.next().intValue();
          final int y_diff = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.scaleFromBottomRight(outer, x_diff, y_diff);

          Assert.assertEquals(
            (long) Math.max(0, outer.width() + x_diff),
            (long) resized.width());
          Assert.assertEquals(
            (long) Math.max(0, outer.height() + y_diff),
            (long) resized.height());

          Assert.assertEquals(
            (long) outer.minimumX(),
            (long) resized.minimumX());
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) resized.minimumY());
        }
      });
  }

  @Test
  public void testScaleFromBottomLeftAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x_diff = int_gen.next().intValue();
          final int y_diff = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.scaleFromBottomLeft(outer, x_diff, y_diff);

          Assert.assertEquals(
            (long) Math.max(0, outer.width() + x_diff),
            (long) resized.width());
          Assert.assertEquals(
            (long) Math.max(0, outer.height() + y_diff),
            (long) resized.height());

          Assert.assertEquals(
            (long) outer.maximumX(),
            (long) resized.maximumX());
          Assert.assertEquals(
            (long) outer.minimumY(),
            (long) resized.minimumY());
        }
      });
  }

  @Test
  public void testScaleFromCenterAll()
    throws Exception
  {
    final Generator<Integer> int_gen = PrimitiveGenerators.integers(-400, 400);
    final Generator<SyBoxType<SySpaceType>> generator = new SyBoxGenerator<>();
    QuickCheck.forAllVerbose(
      generator,
      new AbstractCharacteristic<SyBoxType<SySpaceType>>()
      {
        @Override
        protected void doSpecify(final SyBoxType<SySpaceType> outer)
          throws Throwable
        {
          final int x_diff = int_gen.next().intValue();
          final int y_diff = int_gen.next().intValue();

          final SyBoxType<SySpaceType> resized =
            SyBoxes.scaleFromCenter(outer, x_diff, y_diff);

          Assert.assertEquals(
            (long) Math.max(0, outer.width() + x_diff),
            (long) resized.width());
          Assert.assertEquals(
            (long) Math.max(0, outer.height() + y_diff),
            (long) resized.height());
        }
      });
  }
}
