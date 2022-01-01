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

package com.io7m.jsycamore.tests.core;

import com.io7m.jranges.RangeCheckException;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.components.SyLabelReadableType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowContentPaneType;
import com.io7m.jsycamore.api.windows.SyWindowFrameType;
import com.io7m.jsycamore.api.windows.SyWindowTitleBarType;
import com.io7m.jsycamore.themes.fenestra.SyThemeFenestra;
import com.io7m.jsycamore.themes.motive.SyThemeMotive;
import com.io7m.jsycamore.themes.stride.SyThemeStride;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyGUIContract
{
  protected abstract SyGUIType create(String name);

  protected abstract SyGUIType createWithTheme(
    String name,
    SyTheme theme);

  @Test
  public final void testCreate()
  {
    final var g = this.create("main");
    assertEquals("main", g.name());
    assertEquals(SyThemeMotive.builder().build(), g.theme());
    assertTrue(g.windowsOpenOrdered().isEmpty());
  }

  @Test
  public final void testCreateWithTheme()
  {
    final var theme = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", theme);
    assertEquals("main", g.name());
    assertEquals(theme, g.theme());
    assertTrue(g.windowsOpenOrdered().isEmpty());
  }

  @Test
  public final void testCreateWindowNegativeWidth()
  {
    final var g = this.create("main");

    assertThrows(RangeCheckException.class, () -> {
      g.windowCreate(-1, 200, "title");
    });
  }

  @Test
  public final void testCreateWindowNegativeHeight()
  {
    final var g = this.create("main");
    assertThrows(RangeCheckException.class, () -> {
      g.windowCreate(100, -1, "title");
    });
  }

  @Test
  public final void testWindowFocusedWrongGUI()
  {
    final var g0 = this.create("main");
    final var g1 = this.create("other");
    final var w0 =
      g0.windowCreate(100, 100, "title");

    assertThrows(IllegalArgumentException.class, () -> {
      g1.windowIsFocused(w0);
    });
  }

  @Test
  public final void testCreateWindow()
  {
    final var g = this.create("main");

    final var w = g.windowCreate(640, 480, "Window 0");
    assertEquals(g.theme(), w.theme());
    assertEquals("Window 0", w.titleBar().text());

    final var windows = g.windowsOpenOrdered();
    assertEquals(1L, (long) windows.size());
    assertEquals(w, windows.get(0));
    assertTrue(g.windowIsFocused(w));
    assertTrue(w.isFocused());

    final var window_box = w.box();

    assertEquals(0L, (long) window_box.minimumX());
    assertEquals(0L, (long) window_box.minimumY());
    assertEquals(640L, (long) window_box.sizeX());
    assertEquals(480L, (long) window_box.sizeY());
  }

  @Test
  public final void testWindowFocus()
  {
    final var g = this.create("main");
    final var w0 = g.windowCreate(640, 480, "Window 0");
    final var w1 = g.windowCreate(640, 480, "Window 1");

    assertFalse(w0.isFocused());
    assertTrue(w1.isFocused());
    assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w1));
    assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w0));

    g.windowFocus(w0);

    assertTrue(w0.isFocused());
    assertFalse(w1.isFocused());
    assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w0));
    assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w1));

    g.windowFocus(w1);

    assertFalse(w0.isFocused());
    assertTrue(w1.isFocused());
    assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w1));
    assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w0));
  }

  @Test
  public final void testWindowThemeChange()
  {
    final var theme = SyThemeFenestra.builder().build();
    final SyThemeType theme_default = SyThemeMotive.builder().build();

    final var g = this.create("main");
    final var w0 = g.windowCreate(640, 480, "Main 0");

    assertEquals(theme_default, g.theme());
    assertEquals(theme_default, w0.theme());

    g.setTheme(theme);

    assertEquals(theme, g.theme());
    assertEquals(theme, w0.theme());
  }

  @Test
  public final void testWindowThemeChangeOpenClosed()
  {
    final var theme = SyThemeFenestra.builder().build();
    final SyThemeType theme_default = SyThemeMotive.builder().build();

    final var g = this.create("main");
    final var w0 = g.windowCreate(640, 480, "Main 0");

    assertEquals(theme_default, g.theme());
    assertEquals(theme_default, w0.theme());

    g.windowClose(w0);
    g.setTheme(theme);

    assertEquals(theme, g.theme());
    assertEquals(theme, w0.theme());
  }

  @Test
  public final void testWindowOpenCloseMulti()
  {
    final var g = this.create("main");
    final var w0 = g.windowCreate(640, 480, "Main 0");
    final var w1 = g.windowCreate(640, 480, "Main 1");

    assertTrue(w0.isOpen());
    assertFalse(w0.isFocused());
    assertTrue(w1.isOpen());
    assertTrue(w1.isFocused());

    g.windowClose(w1);

    assertTrue(w0.isOpen());
    assertTrue(w0.isFocused());
    assertFalse(w1.isOpen());
    assertFalse(w1.isFocused());

    g.windowOpen(w1);

    assertTrue(w0.isOpen());
    assertFalse(w0.isFocused());
    assertTrue(w1.isOpen());
    assertTrue(w1.isFocused());

    g.windowClose(w0);

    assertFalse(w0.isOpen());
    assertFalse(w0.isFocused());
    assertTrue(w1.isOpen());
    assertTrue(w1.isFocused());

    g.windowClose(w1);

    assertFalse(w0.isOpen());
    assertFalse(w0.isFocused());
    assertFalse(w1.isOpen());
    assertFalse(w1.isFocused());

    g.windowOpen(w0);

    assertTrue(w0.isOpen());
    assertTrue(w0.isFocused());
    assertFalse(w1.isOpen());
    assertFalse(w1.isFocused());

    g.windowOpen(w1);

    assertTrue(w0.isOpen());
    assertFalse(w0.isFocused());
    assertTrue(w1.isOpen());
    assertTrue(w1.isFocused());
  }

  @Test
  public final void testWindowFocusMulti()
  {
    final var g =
      this.createWithTheme("main", SyThemeStride.builder().build());

    final var w0 = g.windowCreate(320, 240, "Window 0");
    final var w1 = g.windowCreate(320, 240, "Window 1");
    w1.setBox(PAreasI.create(320, 0, 320, 240));

    assertFalse(w0.isFocused());
    assertTrue(w1.isFocused());

    {
      g.onMouseDown(
        PVector2I.of(2, 2), SyMouseButton.MOUSE_BUTTON_LEFT);
      assertTrue(w0.isFocused());
      assertFalse(w1.isFocused());
    }

    {
      g.onMouseDown(
        PVector2I.of(320 + 2, 2), SyMouseButton.MOUSE_BUTTON_LEFT);
      assertTrue(w1.isFocused());
      assertFalse(w0.isFocused());
    }
  }

  @Test
  public final void testWindowMouseOverFrame()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseMoved(PVector2I.of(2, 2));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(cc instanceof SyWindowFrameType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(640 + 2, 480 + 2));
      assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverContentPane()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseMoved(PVector2I.of(32, 32));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(cc instanceof SyWindowContentPaneType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(640 + 32, 480 + 32));
      assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverMulti()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseMoved(PVector2I.of(320, 10));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(cc instanceof SyLabelReadableType);
      assertTrue(cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(325, 10));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(cc instanceof SyLabelReadableType);
      assertTrue(cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(320, 100));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(cc instanceof SyWindowContentPaneType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(325, 100));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(cc instanceof SyWindowContentPaneType);
    }
  }

  @Test
  public final void testWindowMouseOverTitlebar()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseMoved(PVector2I.of(320, 10));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(
        cc instanceof SyLabelReadableType);
      assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(640 + 10, 480 + 10));
      assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverTitlebarOffset()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");
    w0.setBox(PAreasI.moveAbsolute(w0.box(), 32, 32));

    {
      final var c =
        g.onMouseMoved(PVector2I.of(32 + 320 + 10, 32 + 10));
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(
        cc instanceof SyLabelReadableType);
      assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final var c =
        g.onMouseMoved(PVector2I.of(32 + 640 + 10, 32 + 480 + 10));
      assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverTitlebarDragLeftButton()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseDown(
          PVector2I.of(320, 10),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(
        cc instanceof SyLabelReadableType);
      assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    g.onMouseMoved(PVector2I.of(320 + 15, 20));

    {
      final var c =
        g.onMouseUp(
          PVector2I.of(320 + 15, 20),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(
        cc instanceof SyLabelReadableType);
      assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    final var box = w0.box();
    assertEquals(15L, (long) box.minimumX());
    assertEquals(10L, (long) box.minimumY());
    assertEquals(640L, (long) box.sizeX());
    assertEquals(480L, (long) box.sizeY());
  }

  @Test
  public final void testWindowClickNothing()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseDown(
          PVector2I.of(800, 600),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      assertFalse(c.isPresent());
    }

    {
      final var c =
        g.onMouseUp(
          PVector2I.of(800, 600),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverTitlebarDragRightButton()
  {
    final var t = SyThemeMotive.builder().build();
    final var g = this.createWithTheme("main", t);
    final var w0 = g.windowCreate(640, 480, "Window 0");

    {
      final var c =
        g.onMouseDown(
          PVector2I.of(320, 10),
          SyMouseButton.MOUSE_BUTTON_RIGHT);
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(
        cc instanceof SyLabelReadableType);
      assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    g.onMouseMoved(PVector2I.of(320 + 15, 20));

    {
      final var c =
        g.onMouseUp(
          PVector2I.of(320 + 15, 20),
          SyMouseButton.MOUSE_BUTTON_RIGHT);
      assertTrue(c.isPresent());
      final var cc = c.get();
      assertTrue(
        cc instanceof SyLabelReadableType);
      assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    g.onMouseMoved(PVector2I.of(800, 600));

    final var box = w0.box();
    assertEquals(0L, (long) box.minimumX());
    assertEquals(0L, (long) box.minimumY());
    assertEquals(640L, (long) box.sizeX());
    assertEquals(480L, (long) box.sizeY());
  }
}
