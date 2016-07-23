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
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyButtonListenerType;
import com.io7m.jsycamore.core.components.SyButtonState;
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.components.SyLabelType;
import com.io7m.jtensors.parameterized.PVectorI2I;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SyButtonRepeatingContract extends SyComponentContract
{
  @Override
  protected abstract SyButtonType create();

  @Test
  public void testWindowlessTheme()
  {
    final SyButtonType c = this.create();
    Assert.assertFalse(c.window().isPresent());
    Assert.assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final SyButtonType button = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    button.matchComponent(
      this,
      (x, b_button) -> {
        called.set(true);
        return Unit.unit();
      },
      (x, panel) -> {
        throw new UnreachableCodeException();
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, image) -> {
        throw new UnreachableCodeException();
      });

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final SyButtonType button = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    button.matchComponentReadable(
      this,
      (x, b_button) -> {
        called.set(true);
        return Unit.unit();
      },
      (x, panel) -> {
        throw new UnreachableCodeException();
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, image) -> {
        throw new UnreachableCodeException();
      });

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testOver()
  {
    final SyButtonType button = this.create();
    button.setBox(SyBoxes.create(0, 0, 32, 32));

    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    {
      final boolean over_event =
        button.mouseOver(new PVectorI2I<>(0, 0), button);
      Assert.assertTrue(over_event);
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    {
      final boolean over_event = button.mouseNoLongerOver();
      Assert.assertTrue(over_event);
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }
  }

  @Test
  public final void testPressLeft()
  {
    final AtomicInteger pressed = new AtomicInteger(0);
    final SyButtonType button = this.create();
    button.setBox(SyBoxes.create(0, 0, 32, 32));

    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final SyButtonListenerType listener = b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean pressed_event = button.mousePressed(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(SyButtonState.BUTTON_PRESSED, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(released_event);
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    Assert.assertEquals(1L, (long) pressed.get());

    button.buttonRemoveListener(listener);

    {
      final boolean pressed_event = button.mousePressed(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(pressed_event);
      Assert.assertEquals(SyButtonState.BUTTON_PRESSED, button.buttonState());
    }

    Assert.assertEquals(1L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(released_event);
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    Assert.assertEquals(1L, (long) pressed.get());
  }

  @Test
  public final void testPressRight()
  {
    final AtomicInteger pressed = new AtomicInteger(0);
    final SyButtonType button = this.create();
    button.setBox(SyBoxes.create(0, 0, 32, 32));

    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final SyButtonListenerType listener = b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean pressed_event = button.mousePressed(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(pressed_event);
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(released_event);
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    button.buttonRemoveListener(listener);

    {
      final boolean pressed_event = button.mousePressed(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(pressed_event);
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(released_event);
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());
  }

  @Test
  public final void testNoOver()
  {
    final SyButtonType button = this.create();
    button.setBox(SyBoxes.create(0, 0, 32, 32));

    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    {
      final boolean over_event = button.mouseNoLongerOver();
      Assert.assertTrue(over_event);
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }
  }

  @Test
  public final void testReleaseWithoutPress()
  {
    final AtomicInteger pressed = new AtomicInteger(0);
    final SyButtonType button = this.create();
    button.setBox(SyBoxes.create(0, 0, 32, 32));

    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final SyButtonListenerType listener = b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        new PVectorI2I<>(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(released_event);
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());
  }
}
