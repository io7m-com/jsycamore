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
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jsycamore.core.SyWindowTitlebarType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.provided.SyThemeDefault;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorI2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Optional;

public abstract class SyGUIContract
{
  protected abstract SyGUIType create(String name);

  protected abstract SyGUIType createWithTheme(String name, SyThemeType theme);

  @Rule public ExpectedException expected = ExpectedException.none();

  @Test
  public final void testCreate()
  {
    final SyGUIType g = this.create("main");
    Assert.assertEquals("main", g.name());
    Assert.assertEquals(SyThemeDefault.get(), g.theme());
    Assert.assertTrue(g.windowsOpenOrdered().isEmpty());
  }

  @Test
  public final void testCreateWithTheme()
  {
    final SyTheme theme = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", theme);
    Assert.assertEquals("main", g.name());
    Assert.assertEquals(theme, g.theme());
    Assert.assertTrue(g.windowsOpenOrdered().isEmpty());
  }

  @Test
  public final void testCreateWindowNegativeWidth()
  {
    final SyGUIType g = this.create("main");
    this.expected.expect(RangeCheckException.class);
    g.windowCreate(-1, 200, "title");
  }

  @Test
  public final void testCreateWindowNegativeHeight()
  {
    final SyGUIType g = this.create("main");
    this.expected.expect(RangeCheckException.class);
    g.windowCreate(100, -1, "title");
  }

  @Test
  public final void testWindowFocusedWrongGUI()
  {
    final SyGUIType g0 = this.create("main");
    final SyGUIType g1 = this.create("other");

    final SyWindowType w0 = g0.windowCreate(100, 100, "title");
    this.expected.expect(IllegalArgumentException.class);
    g1.windowIsFocused(w0);
  }

  @Test
  public final void testCreateWindow()
  {
    final SyGUIType g = this.create("main");

    final SyWindowType w = g.windowCreate(640, 480, "Window 0");
    Assert.assertEquals(g.theme(), w.theme());
    Assert.assertEquals("Window 0", w.titlebar().text());

    final List<SyWindowType> windows = g.windowsOpenOrdered();
    Assert.assertEquals(1L, (long) windows.size());
    Assert.assertEquals(w, windows.get(0));
    Assert.assertTrue(g.windowIsFocused(w));
    Assert.assertTrue(w.focused());

    final PVectorReadable2IType<SySpaceViewportType> pos = w.position();
    Assert.assertEquals(0L, (long) pos.getXI());
    Assert.assertEquals(0L, (long) pos.getYI());

    final VectorReadable2IType bounds = w.bounds();
    Assert.assertEquals(640L, (long) bounds.getXI());
    Assert.assertEquals(480L, (long) bounds.getYI());

    final PVectorReadable2IType<SySpaceParentRelativeType> frame_pos =
      w.frame().position();
    Assert.assertTrue(frame_pos.getXI() >= pos.getXI());
    Assert.assertTrue(frame_pos.getYI() >= pos.getYI());

    final VectorReadable2IType frame_bounds = w.frame().size();
    Assert.assertTrue(frame_bounds.getXI() <= bounds.getXI());
    Assert.assertTrue(frame_bounds.getYI() <= bounds.getYI());

    final SyWindowTitlebarType titlebar = w.titlebar();
    final PVectorReadable2IType<SySpaceParentRelativeType> titlebar_pos =
      titlebar.position();
    Assert.assertTrue(titlebar_pos.getXI() >= pos.getXI());
    Assert.assertTrue(titlebar_pos.getYI() >= pos.getYI());

    final VectorReadable2IType titlebar_size = titlebar.size();
    Assert.assertTrue(titlebar_size.getXI() <= bounds.getXI());
    Assert.assertTrue(titlebar_size.getYI() <= bounds.getYI());
  }

  @Test
  public final void testWindowFocus()
  {
    final SyGUIType g = this.create("main");
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");
    final SyWindowType w1 = g.windowCreate(640, 480, "Window 1");

    Assert.assertFalse(w0.focused());
    Assert.assertTrue(w1.focused());
    Assert.assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w1));
    Assert.assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w0));

    g.windowFocus(w0);

    Assert.assertTrue(w0.focused());
    Assert.assertFalse(w1.focused());
    Assert.assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w0));
    Assert.assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w1));

    g.windowFocus(w1);

    Assert.assertFalse(w0.focused());
    Assert.assertTrue(w1.focused());
    Assert.assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w1));
    Assert.assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w0));
  }

  @Test public final void testWindowMouseOverTitlebar()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(new PVectorI2I<>(10, 10));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowTitlebarType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(new PVectorI2I<>(640 + 10, 480 + 10));
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test public final void testWindowMouseOverTitlebarDragLeftButton()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseDown(new PVectorI2I<>(10, 10), SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowTitlebarType);
    }

    g.onMouseMoved(new PVectorI2I<>(15, 20));

    {
      final Optional<SyComponentType> c =
        g.onMouseUp(new PVectorI2I<>(15, 20), SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowTitlebarType);
    }

    final PVectorReadable2IType<SySpaceViewportType> pos = w0.position();
    Assert.assertEquals(5L, (long) pos.getXI());
    Assert.assertEquals(10L, (long) pos.getYI());

    final VectorReadable2IType size = w0.bounds();
    Assert.assertEquals(640L, (long) size.getXI());
    Assert.assertEquals(480L, (long) size.getYI());
  }

  @Test public final void testWindowClickNothing()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseDown(new PVectorI2I<>(800, 600), SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertFalse(c.isPresent());
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseUp(new PVectorI2I<>(800, 600), SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test public final void testWindowMouseOverTitlebarDragRightButton()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseDown(new PVectorI2I<>(10, 10), SyMouseButton.MOUSE_BUTTON_RIGHT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowTitlebarType);
    }

    g.onMouseMoved(new PVectorI2I<>(15, 20));

    {
      final Optional<SyComponentType> c =
        g.onMouseUp(new PVectorI2I<>(15, 20), SyMouseButton.MOUSE_BUTTON_RIGHT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowTitlebarType);
    }

    g.onMouseMoved(new PVectorI2I<>(800, 600));

    final PVectorReadable2IType<SySpaceViewportType> pos = w0.position();
    Assert.assertEquals(0L, (long) pos.getXI());
    Assert.assertEquals(0L, (long) pos.getYI());

    final VectorReadable2IType size = w0.bounds();
    Assert.assertEquals(640L, (long) size.getXI());
    Assert.assertEquals(480L, (long) size.getYI());
  }
}
