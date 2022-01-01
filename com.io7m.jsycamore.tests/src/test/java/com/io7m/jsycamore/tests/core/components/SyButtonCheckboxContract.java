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
import com.io7m.jsycamore.api.components.SyButtonCheckboxType;
import com.io7m.jsycamore.api.components.SyButtonListenerType;
import com.io7m.jsycamore.api.components.SyButtonState;
import com.io7m.jsycamore.api.components.SyButtonType;
import com.io7m.jsycamore.api.themes.SyThemeButtonCheckbox;
import com.io7m.jsycamore.api.themes.SyThemeColor;
import com.io7m.jsycamore.themes.motive.SyThemeMotive;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyButtonCheckboxContract extends SyComponentContract
{
  @Override
  protected abstract SyButtonCheckboxType create();

  @Test
  public void testWindowlessTheme()
  {
    final SyButtonType c = this.create();
    assertFalse(c.window().isPresent());
    assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final SyButtonType button = this.create();
    final var called = new AtomicBoolean(false);

    button.matchComponent(
      this,
      (x, b_button) -> {
        called.set(true);
        return Void.class;
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

    assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final SyButtonType button = this.create();
    final var called = new AtomicBoolean(false);

    button.matchComponentReadable(
      this,
      (x, b_button) -> {
        called.set(true);
        return Void.class;
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

    assertTrue(called.get());
  }

  @Test
  public final void testSetCheckedNotAttached()
  {
    final var button = this.create();

    assertFalse(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());

    button.setChecked(true);
    assertTrue(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());

    button.setChecked(false);
    assertFalse(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());
  }

  @Test
  public final void testSetCheckedAttached()
  {
    final var theme = SyThemeMotive.builder().build();
    assertTrue(theme.buttonCheckboxTheme().checkedIcon().isPresent());

    final var gui = this.gui();
    gui.setTheme(theme);

    final var window =
      gui.windowCreate(320, 240, "Window 0");

    final var button = this.create();
    window.contentPane().node().childAdd(button.node());

    assertFalse(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());

    button.setChecked(true);
    assertTrue(button.isChecked());
    assertEquals(1L, (long) button.node().children().size());

    button.setChecked(false);
    assertFalse(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());
  }

  @Test
  public final void testThemeChangedChecked()
  {
    final var theme_with_icons = SyThemeMotive.builder().build();
    assertTrue(
      theme_with_icons.buttonCheckboxTheme().checkedIcon().isPresent());

    final var theme_without_icons =
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

    assertFalse(
      theme_without_icons.buttonCheckboxTheme().checkedIcon().isPresent());

    final var gui = this.gui();
    gui.setTheme(theme_without_icons);

    final var window = gui.windowCreate(320, 240, "Window 0");

    final var button = this.create();
    window.contentPane().node().childAdd(button.node());

    button.setChecked(true);
    assertTrue(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());

    gui.setTheme(theme_with_icons);

    assertTrue(button.isChecked());
    assertEquals(1L, (long) button.node().children().size());

    gui.setTheme(theme_without_icons);

    assertTrue(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());
  }

  @Test
  public final void testOver()
  {
    final SyButtonType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    {
      final var over_event =
        button.mouseOver(PVector2I.of(0, 0), button);
      assertTrue(over_event);
      assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    {
      final var over_event = button.mouseNoLongerOver();
      assertTrue(over_event);
      assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }
  }

  @Test
  public final void testPressLeftNotAttached()
  {
    final var pressed = new AtomicInteger(0);
    final var button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    assertFalse(button.isChecked());
    assertEquals(0L, (long) button.node().children().size());
    assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final var listener = (SyButtonListenerType) b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    assertEquals(0L, (long) pressed.get());

    {
      final var pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      assertTrue(pressed_event);
      assertFalse(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_PRESSED, button.buttonState());
    }

    assertEquals(0L, (long) pressed.get());

    {
      final var released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      assertTrue(released_event);
      assertTrue(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    assertEquals(1L, (long) pressed.get());

    button.buttonRemoveListener(listener);

    {
      final var pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      assertTrue(pressed_event);
      assertTrue(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_PRESSED, button.buttonState());
    }

    assertEquals(1L, (long) pressed.get());

    {
      final var released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      assertTrue(released_event);
      assertFalse(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    assertEquals(1L, (long) pressed.get());
  }

  @Test
  public final void testPressRight()
  {
    final var pressed = new AtomicInteger(0);
    final var button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    assertFalse(button.isChecked());
    assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final var listener = (SyButtonListenerType) b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    assertEquals(0L, (long) pressed.get());

    {
      final var pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      assertFalse(pressed_event);
      assertFalse(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    assertEquals(0L, (long) pressed.get());

    {
      final var released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      assertFalse(released_event);
      assertFalse(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    assertEquals(0L, (long) pressed.get());

    button.buttonRemoveListener(listener);

    {
      final var pressed_event = button.mousePressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      assertFalse(pressed_event);
      assertFalse(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    assertEquals(0L, (long) pressed.get());

    {
      final var released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_RIGHT,
        button);
      assertFalse(released_event);
      assertFalse(button.isChecked());
      assertEquals(0L, (long) button.node().children().size());
      assertEquals(SyButtonState.BUTTON_OVER, button.buttonState());
    }

    assertEquals(0L, (long) pressed.get());
  }

  @Test
  public final void testNoOver()
  {
    final SyButtonType button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    {
      final var over_event = button.mouseNoLongerOver();
      assertTrue(over_event);
      assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }
  }

  @Test
  public final void testReleaseWithoutPress()
  {
    final var pressed = new AtomicInteger(0);
    final var button = this.create();
    button.setBox(PAreasI.create(0, 0, 32, 32));

    assertFalse(button.isChecked());
    assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());

    final var listener = (SyButtonListenerType) b -> pressed.incrementAndGet();
    button.buttonAddListener(listener);

    assertEquals(0L, (long) pressed.get());

    {
      final var released_event = button.mouseReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        button);
      assertTrue(released_event);
      assertFalse(button.isChecked());
      assertEquals(SyButtonState.BUTTON_ACTIVE, button.buttonState());
    }

    assertEquals(0L, (long) pressed.get());
  }
}
