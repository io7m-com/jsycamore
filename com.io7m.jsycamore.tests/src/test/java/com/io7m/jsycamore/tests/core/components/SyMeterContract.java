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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.components.SyMeterType;
import com.io7m.jsycamore.api.themes.SyOrientation;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.support.DoubleGenerator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyMeterContract extends SyComponentContract
{
  @Override
  protected abstract SyMeterType create();

  @Test
  public void testWindowlessTheme()
  {
    final var c = this.create();
    assertFalse(c.window().isPresent());
    assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final var meter = this.create();
    final var called = new AtomicBoolean(false);

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
        return Void.class;
      });

    assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final var meter = this.create();
    final var called = new AtomicBoolean(false);

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
        return Void.class;
      });

    assertTrue(called.get());
  }

  @Test
  public final void testIdentities()
  {
    final var meter = this.create();

    assertEquals(0.0, meter.value(), 0.0);
    assertEquals(
      SyOrientation.ORIENTATION_HORIZONTAL, meter.orientation());
  }

  @Test
  public final void testPressLeftAttachedHorizontal()
  {
    final var gui = this.gui();
    final var window = gui.windowCreate(320, 240, "Window 0");

    final var meter = this.create();
    meter.setBox(PAreasI.create(0, 0, 64, 32));
    window.contentPane().node().childAdd(meter.node());

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(0.0, meter.value(), 0.0);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(32, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(0.406, meter.value(), 0.001);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(64, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(0.906, meter.value(), 0.001);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(1.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testHeldLeftAttachedHorizontal()
  {
    final var gui = this.gui();
    final var window = gui.windowCreate(320, 240, "Window 0");

    final var meter = this.create();
    meter.setBox(PAreasI.create(0, 0, 64, 32));
    window.contentPane().node().childAdd(meter.node());

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(0.0, meter.value(), 0.0);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(32, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(0.406, meter.value(), 0.001);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(64, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(0.906, meter.value(), 0.001);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(1.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testPressLeftAttachedVertical()
  {
    final var gui = this.gui();
    final var window = gui.windowCreate(320, 240, "Window 0");

    final var meter = this.create();
    meter.setBox(PAreasI.create(0, 0, 32, 64));
    meter.setOrientation(SyOrientation.ORIENTATION_VERTICAL);
    window.contentPane().node().childAdd(meter.node());

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(-3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(0, 32),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(0.843, meter.value(), 0.001);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(0, 64),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(0.343, meter.value(), 0.001);
    }

    {
      final var pressed_event = meter.mousePressed(
        PVector2I.of(0, 3000),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(pressed_event);
      assertEquals(0.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testPressLeftHeldVertical()
  {
    final var gui = this.gui();
    final var window = gui.windowCreate(320, 240, "Window 0");

    final var meter = this.create();
    meter.setBox(PAreasI.create(0, 0, 32, 64));
    meter.setOrientation(SyOrientation.ORIENTATION_VERTICAL);
    window.contentPane().node().childAdd(meter.node());

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(-3000, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(1.0, meter.value(), 0.0);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 32),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(0.843, meter.value(), 0.001);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 64),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(0.343, meter.value(), 0.001);
    }

    {
      final var held_event = meter.mouseHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 3000),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        meter);
      assertTrue(held_event);
      assertEquals(0.0, meter.value(), 0.0);
    }
  }

  @Test
  public final void testValue()
  {
    final var meter = this.create();

    QuickCheck.forAllVerbose(
      new DoubleGenerator(0.0, 1.0),
      new AbstractCharacteristic<Double>()
      {
        @Override
        protected void doSpecify(final Double any)
          throws Throwable
        {
          meter.setValue(any.doubleValue());
          assertEquals(any.doubleValue(), meter.value(), 0.0);
        }
      });
  }

  @Test
  public final void testValueRange()
  {
    final var meter = this.create();

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
            assertEquals(1.0, meter.value(), 0.0);
          } else if (any.doubleValue() <= 0.0) {
            assertEquals(0.0, meter.value(), 0.0);
          } else {
            assertEquals(any.doubleValue(), meter.value(), 0.0);
          }
        }
      });
  }
}
