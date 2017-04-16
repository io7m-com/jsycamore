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
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.components.SyButtonCheckboxType;
import com.io7m.jsycamore.core.components.SyButtonListenerType;
import com.io7m.jsycamore.core.components.SyButtonState;
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButtonCheckbox;
import com.io7m.jsycamore.core.themes.SyThemeColor;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SyButtonCheckboxContract extends SyComponentContract
{
  @Override
  protected abstract SyButtonCheckboxType create();

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
      },
      (x, meter) -> {
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
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testSetCheckedNotAttached()
  {
    final SyButtonCheckboxType button = this.create();

    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());

    button.setChecked(true);
    Assert.assertTrue(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());

    button.setChecked(false);
    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());
  }

  @Test
  public final void testSetCheckedAttached()
  {
    final SyTheme theme = SyThemeMotive.builder().build();
    Assert.assertTrue(theme.buttonCheckboxTheme().checkedIcon().isPresent());

    final SyGUIType gui = this.gui();
    gui.setTheme(theme);

    final SyWindowType window = gui.windowCreate(320, 240, "Window 0");

    final SyButtonCheckboxType button = this.create();
    window.contentPane().node().childAdd(button.node());

    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());

    button.setChecked(true);
    Assert.assertTrue(button.isChecked());
    Assert.assertEquals(1L, (long) button.node().children().size());

    button.setChecked(false);
    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());
  }

  @Test
  public final void testThemeChangedChecked()
  {
    final SyTheme theme_with_icons = SyThemeMotive.builder().build();
    Assert.assertTrue(
      theme_with_icons.buttonCheckboxTheme().checkedIcon().isPresent());

    final SyTheme theme_without_icons =
      theme_with_icons.withButtonCheckboxTheme(
        SyThemeButtonCheckbox.of(
          SyThemeColor.of(Vectors3D.zero()),
          Optional.empty(),
          SyThemeColor.of(Vectors3D.zero()),
          Optional.empty(),
          SyThemeColor.of(Vectors3D.zero()),
          Optional.empty(),
          SyThemeColor.of(Vectors3D.zero()),
          Optional.empty(),
          Optional.empty()
        ));

    Assert.assertFalse(
      theme_without_icons.buttonCheckboxTheme().checkedIcon().isPresent());

    final SyGUIType gui = this.gui();
    gui.setTheme(theme_without_icons);

    final SyWindowType window = gui.windowCreate(320, 240, "Window 0");

    final SyButtonCheckboxType button = this.create();
    window.contentPane().node().childAdd(button.node());

    button.setChecked(true);
    Assert.assertTrue(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());

    gui.setTheme(theme_with_icons);

    Assert.assertTrue(button.isChecked());
    Assert.assertEquals(1L, (long) button.node().children().size());

    gui.setTheme(theme_without_icons);

    Assert.assertTrue(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());
  }

  @Test
  public final void testOver()
  {
    final SyButtonType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    {
      final boolean over_event =
        button.mouseOver(PVector2I.of(0, 0), button);
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
  public final void testPressLeftNotAttached()
  {
    final AtomicInteger pressed = new AtomicInteger(0);
    final SyButtonCheckboxType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(0L, (long) button.node().children().size());
    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final SyButtonListenerType listener = b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(pressed_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_PRESSED, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(released_event);
      Assert.assertTrue(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    Assert.assertEquals(1L, (long) pressed.get());

    button.buttonRemoveListener(listener);

    {
      final boolean pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(pressed_event);
      Assert.assertTrue(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_PRESSED, button.buttonState());
    }

    Assert.assertEquals(1L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(released_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    Assert.assertEquals(1L, (long) pressed.get());
  }

  @Test
  public final void testPressRight()
  {
    final AtomicInteger pressed = new AtomicInteger(0);
    final SyButtonCheckboxType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final SyButtonListenerType listener = b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(pressed_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(released_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    button.buttonRemoveListener(listener);

    {
      final boolean pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(pressed_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      Assert.assertFalse(released_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(0L, (long) button.node().children().size());
      Assert.assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());
  }

  @Test
  public final void testNoOver()
  {
    final SyButtonType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

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
    final SyButtonCheckboxType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    Assert.assertFalse(button.isChecked());
    Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final SyButtonListenerType listener = b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    Assert.assertEquals(0L, (long) pressed.get());

    {
      final boolean released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      Assert.assertTrue(released_event);
      Assert.assertFalse(button.isChecked());
      Assert.assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    Assert.assertEquals(0L, (long) pressed.get());
  }
}
