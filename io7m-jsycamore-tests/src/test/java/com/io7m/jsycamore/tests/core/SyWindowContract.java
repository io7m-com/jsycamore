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

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jsycamore.core.SyWindowTitlebarType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeWindow;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarVerticalPlacement;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarWidthBehavior;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorI2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public abstract class SyWindowContract
{
  protected abstract SyWindowType create(int width, int height, String title);

  @Test
  public final void testCreate()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final VectorReadable2IType bounds = w.bounds();
    Assert.assertEquals(640L, (long) bounds.getXI());
    Assert.assertEquals(480L, (long) bounds.getYI());
    final PVectorReadable2IType<SySpaceViewportType> position = w.position();
    Assert.assertEquals(0L, (long) position.getXI());
    Assert.assertEquals(0L, (long) position.getYI());

    final SyWindowTitlebarType titlebar = w.titlebar();
    Assert.assertEquals("Main 0", titlebar.text());

    Assert.assertTrue(w.focused());
  }

  @Test
  public final void testBounds()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final VectorReadable2IType bounds = w.bounds();
    final PVectorReadable2IType<SySpaceViewportType> position = w.position();
    Assert.assertEquals(640L, (long) bounds.getXI());
    Assert.assertEquals(480L, (long) bounds.getYI());
    Assert.assertEquals(0L, (long) position.getXI());
    Assert.assertEquals(0L, (long) position.getYI());

    w.setBounds(800, 600);
    Assert.assertEquals(800L, (long) bounds.getXI());
    Assert.assertEquals(600L, (long) bounds.getYI());
    Assert.assertEquals(0L, (long) position.getXI());
    Assert.assertEquals(0L, (long) position.getYI());

    final SyWindowTitlebarType titlebar = w.titlebar();
    Assert.assertEquals("Main 0", titlebar.text());

    final VectorReadable2IType titlebar_size =
      titlebar.size();
    final PVectorReadable2IType<SySpaceParentRelativeType> frame_pos =
      w.frame().position();
    final VectorReadable2IType frame_size =
      w.frame().size();

    Assert.assertTrue(titlebar_size.getXI() <= bounds.getXI());
    Assert.assertTrue(titlebar_size.getYI() <= bounds.getYI());

    Assert.assertTrue(frame_pos.getXI() >= 0);
    Assert.assertTrue(frame_pos.getYI() >= 0);
    Assert.assertTrue(frame_size.getXI() <= bounds.getXI());
    Assert.assertTrue(frame_size.getYI() <= bounds.getYI());
  }

  @Test
  public final void testBoundsTitleAboveFrame()
  {
    final SyTheme theme = SyThemeMotive.builder().build();

    final SyThemeWindowTitleBar.Builder theme_win_title =
      SyThemeWindowTitleBar.builder();
    theme_win_title.from(theme.windowTheme().titleBar());
    theme_win_title.setVerticalPlacement(
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME);

    final SyThemeWindow.Builder theme_win = SyThemeWindow.builder();
    theme_win.from(theme.windowTheme());
    theme_win.setTitleBar(theme_win_title.build());

    final SyTheme.Builder theme_mod_b = SyThemeMotive.builder();
    theme_mod_b.setWindowTheme(theme_win.build());
    final SyTheme theme_mod = theme_mod_b.build();

    final SyWindowType w = this.create(640, 480, "Main 0");
    final VectorReadable2IType bounds = w.bounds();
    final PVectorReadable2IType<SySpaceViewportType> position = w.position();
    Assert.assertEquals(640L, (long) bounds.getXI());
    Assert.assertEquals(480L, (long) bounds.getYI());
    Assert.assertEquals(0L, (long) position.getXI());
    Assert.assertEquals(0L, (long) position.getYI());

    w.setBounds(800, 600);
    Assert.assertEquals(800L, (long) bounds.getXI());
    Assert.assertEquals(600L, (long) bounds.getYI());
    Assert.assertEquals(0L, (long) position.getXI());
    Assert.assertEquals(0L, (long) position.getYI());

    final SyWindowTitlebarType titlebar = w.titlebar();
    Assert.assertEquals("Main 0", titlebar.text());

    final VectorReadable2IType titlebar_size =
      titlebar.size();
    final PVectorReadable2IType<SySpaceParentRelativeType> frame_pos =
      w.frame().position();
    final VectorReadable2IType frame_size =
      w.frame().size();

    Assert.assertTrue(titlebar_size.getXI() <= bounds.getXI());
    Assert.assertTrue(titlebar_size.getYI() <= bounds.getYI());

    Assert.assertTrue(frame_pos.getXI() >= 0);
    Assert.assertTrue(frame_pos.getYI() >= 0);
    Assert.assertTrue(frame_size.getXI() <= bounds.getXI());
    Assert.assertTrue(frame_size.getYI() <= bounds.getYI());
  }

  @Test
  public final void testSetTitle()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");

    final SyWindowTitlebarType titlebar = w.titlebar();
    Assert.assertEquals("Main 0", titlebar.text());

    titlebar.setText("Main 1");
    Assert.assertEquals("Main 1", titlebar.text());
  }
}
