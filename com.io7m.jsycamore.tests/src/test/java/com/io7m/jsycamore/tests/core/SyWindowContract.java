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
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyImageType;
import com.io7m.jsycamore.api.images.SyImageFormat;
import com.io7m.jsycamore.api.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.api.windows.SyWindowCloseButtonType;
import com.io7m.jsycamore.api.windows.SyWindowMaximizeButtonType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.themes.bee.SyThemeBee;
import com.io7m.jsycamore.themes.fenestra.SyThemeFenestra;
import com.io7m.jsycamore.themes.motive.SyThemeMotive;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyWindowContract
{
  private static boolean isCloseOrMaximize(
    final JOTreeNodeType<SyComponentType> node)
  {
    if (!node.isRoot()) {
      if (node.value() instanceof SyImageType) {
        final var parent = node.parent().get();
        return parent.value() instanceof SyWindowCloseButtonType
          || parent.value() instanceof SyWindowMaximizeButtonType;
      }
    }
    return false;
  }

  protected abstract SyWindowType create(
    int width,
    int height,
    String title);

  @Test
  public final void testCreate()
  {
    final var w = this.create(640, 480, "Main 0");
    final var box = w.box();

    assertEquals(640L, (long) box.sizeX());
    assertEquals(480L, (long) box.sizeY());
    assertEquals(0L, (long) box.minimumX());
    assertEquals(0L, (long) box.minimumY());

    final var titlebar = w.titleBar();
    assertEquals("Main 0", titlebar.text());

    assertTrue(w.isFocused());
  }

  @Test
  public final void testOpenClose()
  {
    final var w = this.create(640, 480, "Main 0");
    final var g = w.gui();

    assertTrue(w.isOpen());
    assertTrue(w.isFocused());

    g.windowClose(w);

    assertFalse(w.isOpen());
    assertFalse(w.isFocused());

    g.windowOpen(w);

    assertTrue(w.isOpen());
    assertTrue(w.isFocused());
  }

  @Test
  public final void testCloseable()
  {
    final var w = this.create(640, 480, "Main 0");

    assertTrue(w.isCloseable());
    w.setCloseable(true);
    assertTrue(w.isCloseable());
    w.setCloseable(false);
    assertFalse(w.isCloseable());
    w.setCloseable(true);
    assertTrue(w.isCloseable());
  }

  @Test
  public final void testMaximizable()
  {
    final var w = this.create(640, 480, "Main 0");

    assertTrue(w.isMaximizable());
    w.setMaximizable(true);
    assertTrue(w.isMaximizable());
    w.setMaximizable(false);
    assertFalse(w.isMaximizable());
    w.setMaximizable(true);
    assertTrue(w.isMaximizable());
  }

  @Test
  public final void testTheme()
  {
    final var theme_default = SyThemeMotive.builder().build();
    final var theme_other = SyThemeBee.builder().build();
    final var w = this.create(640, 480, "Main 0");

    assertEquals(theme_default, w.theme());
    w.setTheme(Optional.of(theme_other));
    assertEquals(theme_other, w.theme());
    w.setTheme(Optional.empty());
    assertEquals(theme_default, w.theme());
  }

  @Test
  public final void testSetTitle()
  {
    final var w = this.create(640, 480, "Main 0");

    final var titlebar = w.titleBar();
    assertEquals("Main 0", titlebar.text());

    titlebar.setText("Main 1");
    assertEquals("Main 1", titlebar.text());
  }

  @Test
  public final void testDetachContentPaneDenied()
  {
    final var w = this.create(640, 480, "Main 0");
    final var content = w.contentPane();

    assertThrows(JOTreeExceptionDetachDenied.class, () -> {
      content.node().detach();
    });
  }

  @Test
  public final void testDetachTitlebarDenied()
  {
    final var w = this.create(640, 480, "Main 0");
    final var titlebar = w.titleBar();

    final var content = w.contentPane();
    final var c_node = content.node();
    final var root = c_node.parent().get();

    JOTreeNodeType<SyComponentType> titlebar_node = null;
    for (final var n : root.children()) {
      if (Objects.equals(n.value(), titlebar)) {
        titlebar_node = n;
      }
    }

    assertNotNull(titlebar_node);
    final JOTreeNodeType<SyComponentType> titlebarNode = titlebar_node;
    assertThrows(JOTreeExceptionDetachDenied.class, titlebarNode::detach);
  }

  @Test
  public final void testDetachEverythingDenied()
  {
    final var w = this.create(640, 480, "Main 0");

    final var content = w.contentPane();
    final var c_node = content.node();
    final var root = c_node.parent().get();
    assertTrue(root.isRoot());

    final var all = new ArrayList<JOTreeNodeType<SyComponentType>>(100);
    root.forEachBreadthFirst(all, (input, depth, node) -> {
      input.add((JOTreeNodeType<SyComponentType>) node);
    });

    {
      final var iter = all.iterator();
      while (iter.hasNext()) {
        final var node = iter.next();

        /*
         * The images on close and maximize buttons are a special case and
         * are allowed to be removed.
         */

        if (isCloseOrMaximize(node)) {
          continue;
        }

        assertFalse(node.isDetachAllowed());
        if (node.isRoot()) {
          iter.remove();
        }
      }
    }

    var caught = 0L;
    for (final var node : all) {
      try {
        if (isCloseOrMaximize(node)) {
          ++caught;
          continue;
        }

        System.out.println("Trying detach of " + node.value());
        node.detach();
      } catch (final JOTreeExceptionDetachDenied e) {
        ++caught;
      }
    }

    assertEquals((long) all.size(), caught);
  }

  @Test
  public final void testOnFocus()
  {
    final var w = this.create(640, 480, "Main 0");
    final var tb = w.titleBar();

    assertTrue(tb.isActive());
    assertTrue(tb.isVisible());

    w.onWindowLosesFocus();
    assertFalse(tb.isActive());
    assertTrue(tb.isVisible());

    w.onWindowGainsFocus();
    assertTrue(tb.isActive());
    assertTrue(tb.isVisible());
  }

  @Test
  public final void testThemeIconCorrect()
    throws Exception
  {
    final var theme_start = SyThemeMotive.builder().build();
    final var theme_next = SyThemeFenestra.builder().build();

    assertFalse(theme_start.windowTheme().titleBar().iconPresent());
    assertTrue(theme_next.windowTheme().titleBar().iconPresent());

    final var icon_spec = SyImageSpecification.of(
      SyWindowContract.class.getResource("/com/io7m/jsycamore/tests/awt/paper.png").toURI(),
      16,
      16,
      SyImageFormat.IMAGE_FORMAT_RGBA_8888,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST);

    final var w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme_start));

    final var tb = w.titleBar();
    final var icon = tb.iconPanel();

    assertEquals(
      0L,
      (long) icon.nodeReadable().childrenReadable().size());
    assertEquals(0L, (long) icon.box().sizeX());
    assertEquals(0L, (long) icon.box().sizeY());

    tb.setIcon(Optional.of(icon_spec));

    assertEquals(
      1L,
      (long) icon.nodeReadable().childrenReadable().size());
    assertEquals(0L, (long) icon.box().sizeX());
    assertEquals(0L, (long) icon.box().sizeY());

    w.setTheme(Optional.of(theme_next));

    assertEquals(
      1L,
      (long) icon.nodeReadable().childrenReadable().size());
    assertEquals(16L, (long) icon.box().sizeX());
    assertEquals(16L, (long) icon.box().sizeY());

    w.setTheme(Optional.of(theme_start));

    assertEquals(
      1L,
      (long) icon.nodeReadable().childrenReadable().size());
    assertEquals(0L, (long) icon.box().sizeX());
    assertEquals(0L, (long) icon.box().sizeY());

    tb.setIcon(Optional.empty());

    assertEquals(
      0L,
      (long) icon.nodeReadable().childrenReadable().size());
    assertEquals(0L, (long) icon.box().sizeX());
    assertEquals(0L, (long) icon.box().sizeY());
  }
}
