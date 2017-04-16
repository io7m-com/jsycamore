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

package com.io7m.jsycamore.tests.core.components;

import com.io7m.jfunctional.Unit;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SyOrientation;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyMeterType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.support.DoubleGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SyMeterContract extends SyComponentContract
{
  @Override
  protected abstract SyMeterType create();

  @Test
  public void testWindowlessTheme()
  {
    final SyMeterType c = this.create();
    Assert.assertFalse(c.window().isPresent());
    Assert.assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final SyMeterType meter = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    meter.matchComponent(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, panel) -> {
        throw new UnreachableCodeException();
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, image) -> {
        throw new UnreachableCodeException();
      },
      (x, b_meter) -> {
        called.set(true);
        return Unit.unit();
      });

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final SyMeterType meter = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    meter.matchComponentReadable(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, panel) -> {
        throw new UnreachableCodeException();
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, image) -> {
        throw new UnreachableCodeException();
      },
      (x, b_meter) -> {
        called.set(true);
        return Unit.unit();
      });

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testIdentities()
  {
    final SyMeterType meter = this.create();

    Assert.assertEquals(0.0, meter.value(), 0.0);
    Assert.assertEquals(
      SyOrientation.ORIENTATION_HORIZONTAL, meter.orientation());
  }

  @Test
  public final void testPressLeftAttachedHorizontal()
  {
    final SyGUIType gui = this.gui();
    final SyWindowType window = gui.windowCreate(320, 240, "Window 0");

    final SyMeterType meter = this.create();
    meter.setBox(SyBoxes.create(0, 0, 64, 32));
    window.contentPane().node().childAdd(meter.node());

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(0.0, meter.value(), 0.0);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(32, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(0.406, meter.value(), 0.001);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(64, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(0.906, meter.value(), 0.001);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(1.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testHeldLeftAttachedHorizontal()
  {
    final SyGUIType gui = this.gui();
    final SyWindowType window = gui.windowCreate(320, 240, "Window 0");

    final SyMeterType meter = this.create();
    meter.setBox(SyBoxes.create(0, 0, 64, 32));
    window.contentPane().node().childAdd(meter.node());

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(0.0, meter.value(), 0.0);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(32, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(0.406, meter.value(), 0.001);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(64, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(0.906, meter.value(), 0.001);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(1.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testPressLeftAttachedVertical()
  {
    final SyGUIType gui = this.gui();
    final SyWindowType window = gui.windowCreate(320, 240, "Window 0");

    final SyMeterType meter = this.create();
    meter.setBox(SyBoxes.create(0, 0, 32, 64));
    meter.setOrientation(SyOrientation.ORIENTATION_VERTICAL);
    window.contentPane().node().childAdd(meter.node());

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(-3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(0, 32),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(0.843, meter.value(), 0.001);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(0, 64),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(0.343, meter.value(), 0.001);
    }

    {
      final boolean pressed_event = meter.mousePressed(
        PVector2I.of(0, 3000),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(0.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testPressLeftHeldVertical()
  {
    final SyGUIType gui = this.gui();
    final SyWindowType window = gui.windowCreate(320, 240, "Window 0");

    final SyMeterType meter = this.create();
    meter.setBox(SyBoxes.create(0, 0, 32, 64));
    meter.setOrientation(SyOrientation.ORIENTATION_VERTICAL);
    window.contentPane().node().childAdd(meter.node());

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(-3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 32),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(0.843, meter.value(), 0.001);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 64),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(0.343, meter.value(), 0.001);
    }

    {
      final boolean held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 3000),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      Assert.assertTrue(held_event);
      Assert.assertEquals(0.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testValue()
  {
    final SyMeterType meter = this.create();

    QuickCheck.forAllVerbose(
      new DoubleGenerator(0.0, 1.0),
      new AbstractCharacteristic<Double>()
      {
        @Override
        protected void doSpecify(final Double any)
          throws Throwable
        {
          meter.setValue(any.doubleValue());
          Assert.assertEquals(any.doubleValue(), meter.value(), 0.0);
        }
      });
  }

  @Test
  public final void testValueRange()
  {
    final SyMeterType meter = this.create();

    QuickCheck.forAllVerbose(
      new DoubleGenerator(-100.0, 100.0),
      new AbstractCharacteristic<Double>()
      {
        @Override
        protected void doSpecify(final Double any)
          throws Throwable
        {
          meter.setValue(any.doubleValue());

          if (any.doubleValue() >= 1.0) {
            Assert.assertEquals(1.0, meter.value(), 0.0);
          } else if (any.doubleValue() <= 0.0) {
            Assert.assertEquals(0.0, meter.value(), 0.0);
          } else {
            Assert.assertEquals(any.doubleValue(), meter.value(), 0.0);
          }
        }
      });
  }
}
