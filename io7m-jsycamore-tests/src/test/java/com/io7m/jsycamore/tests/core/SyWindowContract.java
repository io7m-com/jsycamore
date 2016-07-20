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

import com.io7m.jorchard.core.JOTreeExceptionDetachDenied;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowContentPaneType;
import com.io7m.jsycamore.core.SyWindowTitleBarType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jsycamore.core.themes.provided.SyThemeDefault;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

public abstract class SyWindowContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  protected abstract SyWindowType create(
    int width,
    int height,
    String title);

  @Test
  public final void testCreate()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final SyBoxType<SySpaceViewportType> box = w.box();

    Assert.assertEquals(640L, (long) box.width());
    Assert.assertEquals(480L, (long) box.height());
    Assert.assertEquals(0L, (long) box.minimumX());
    Assert.assertEquals(0L, (long) box.minimumY());

    final SyWindowTitleBarType titlebar = w.titleBar();
    Assert.assertEquals("Main 0", titlebar.text());

    Assert.assertTrue(w.focused());
  }

  @Test
  public final void testCloseable()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");

    Assert.assertTrue(w.isCloseable());
    w.setCloseable(true);
    Assert.assertTrue(w.isCloseable());
    w.setCloseable(false);
    Assert.assertFalse(w.isCloseable());
    w.setCloseable(true);
    Assert.assertTrue(w.isCloseable());
  }

  @Test
  public final void testMaximizable()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");

    Assert.assertTrue(w.isMaximizable());
    w.setMaximizable(true);
    Assert.assertTrue(w.isMaximizable());
    w.setMaximizable(false);
    Assert.assertFalse(w.isMaximizable());
    w.setMaximizable(true);
    Assert.assertTrue(w.isMaximizable());
  }

  @Test
  public final void testTheme()
  {
    final SyThemeType theme_default = SyThemeDefault.get();
    final SyThemeType theme_other = SyThemeBee.builder().build();
    final SyWindowType w = this.create(640, 480, "Main 0");

    Assert.assertEquals(theme_default, w.theme());
    w.setTheme(Optional.of(theme_other));
    Assert.assertEquals(theme_other, w.theme());
    w.setTheme(Optional.empty());
    Assert.assertEquals(theme_default, w.theme());
  }

  @Test
  public final void testSetTitle()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");

    final SyWindowTitleBarType titlebar = w.titleBar();
    Assert.assertEquals("Main 0", titlebar.text());

    titlebar.setText("Main 1");
    Assert.assertEquals("Main 1", titlebar.text());
  }

  @Test
  public final void testDetachContentPaneDenied()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final SyWindowContentPaneType content = w.contentPane();

    this.expected.expect(JOTreeExceptionDetachDenied.class);
    content.node().detach();
  }

  @Test
  public final void testDetachTitlebarDenied()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final SyWindowTitleBarType titlebar = w.titleBar();

    final SyWindowContentPaneType content = w.contentPane();
    final JOTreeNodeType<SyComponentType> c_node = content.node();
    final JOTreeNodeType<SyComponentType> root = c_node.parent().get();

    JOTreeNodeType<SyComponentType> titlebar_node = null;
    for (final JOTreeNodeType<SyComponentType> n : root.children()) {
      if (Objects.equals(n.value(), titlebar)) {
        titlebar_node = n;
      }
    }

    Assert.assertTrue(titlebar_node != null);
    this.expected.expect(JOTreeExceptionDetachDenied.class);
    titlebar_node.detach();
  }

  @Test
  public final void testDetachEverythingDenied()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");

    final SyWindowContentPaneType content = w.contentPane();
    final JOTreeNodeType<SyComponentType> c_node = content.node();
    final JOTreeNodeType<SyComponentType> root = c_node.parent().get();
    Assert.assertTrue(root.isRoot());

    final ArrayList<JOTreeNodeType<SyComponentType>> all = new ArrayList<>(100);
    root.forEachBreadthFirst(all, (input, depth, node) -> {
      input.add((JOTreeNodeType<SyComponentType>) node);
    });

    {
      final Iterator<JOTreeNodeType<SyComponentType>> iter = all.iterator();
      while (iter.hasNext()) {
        final JOTreeNodeType<SyComponentType> node = iter.next();
        Assert.assertFalse(node.isDetachAllowed());
        if (node.isRoot()) {
          iter.remove();
        }
      }
    }

    long caught = 0L;
    for (final JOTreeNodeType<SyComponentType> node : all) {
      try {
        System.out.println("Trying detach of " + node.value());
        node.detach();
      } catch (final JOTreeExceptionDetachDenied e) {
        ++caught;
      }
    }

    Assert.assertEquals((long) all.size(), caught);
  }

  @Test
  public final void testOnFocus()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final SyWindowTitleBarType tb = w.titleBar();

    Assert.assertTrue(tb.isActive());
    Assert.assertTrue(tb.isVisible());

    w.onWindowLosesFocus();
    Assert.assertFalse(tb.isActive());
    Assert.assertTrue(tb.isVisible());

    w.onWindowGainsFocus();
    Assert.assertTrue(tb.isActive());
    Assert.assertTrue(tb.isVisible());
  }
}
