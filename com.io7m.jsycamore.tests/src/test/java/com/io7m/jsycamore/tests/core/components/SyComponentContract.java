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
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.SyParentResizeBehavior;
import com.io7m.jsycamore.api.components.SyActive;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyVisibility;
import com.io7m.jsycamore.api.components.SyWindowViewportAccumulator;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyComponentContract
{
  protected abstract SyGUIType gui();

  protected abstract SyComponentType create();

  @Test
  public void testComponentActive()
  {
    final var c0 = this.create();
    assertEquals(SyActive.ACTIVE, c0.activity());
    assertTrue(c0.isActive());

    c0.setActive(SyActive.INACTIVE);
    assertEquals(SyActive.INACTIVE, c0.activity());
    assertFalse(c0.isActive());

    c0.setActive(SyActive.ACTIVE);
    assertEquals(SyActive.ACTIVE, c0.activity());
    assertTrue(c0.isActive());
  }

  @Test
  public void testComponentActiveInherited()
  {
    final var c0 = this.create();
    final var c1 = this.create();

    c0.node().childAdd(c1.node());

    assertTrue(c0.isActive());
    assertTrue(c1.isActive());

    c0.setActive(SyActive.INACTIVE);

    assertFalse(c0.isActive());
    assertFalse(c1.isActive());

    c0.setActive(SyActive.ACTIVE);

    assertTrue(c0.isActive());
    assertTrue(c1.isActive());

    c1.setActive(SyActive.INACTIVE);

    assertTrue(c0.isActive());
    assertFalse(c1.isActive());

    c1.setActive(SyActive.ACTIVE);

    assertTrue(c0.isActive());
    assertTrue(c1.isActive());
  }

  @Test
  public void testComponentVisible()
  {
    final var c0 = this.create();
    assertEquals(SyVisibility.VISIBILITY_VISIBLE, c0.visibility());
    assertTrue(c0.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);
    assertEquals(SyVisibility.VISIBILITY_INVISIBLE, c0.visibility());
    assertFalse(c0.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_VISIBLE);
    assertEquals(SyVisibility.VISIBILITY_VISIBLE, c0.visibility());
    assertTrue(c0.isVisible());
  }

  @Test
  public void testComponentVisibleInherited()
  {
    final var c0 = this.create();
    final var c1 = this.create();

    c0.node().childAdd(c1.node());

    assertTrue(c0.isVisible());
    assertTrue(c1.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);

    assertFalse(c0.isVisible());
    assertFalse(c1.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_VISIBLE);

    assertTrue(c0.isVisible());
    assertTrue(c1.isVisible());

    c1.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);

    assertTrue(c0.isVisible());
    assertFalse(c1.isVisible());

    c1.setVisibility(SyVisibility.VISIBILITY_VISIBLE);

    assertTrue(c0.isVisible());
    assertTrue(c1.isVisible());
  }

  @Test
  public void testComponentForWindowRelative()
  {
    final var c0 = this.create();
    c0.setBox(PAreasI.create(0, 0, 64, 64));

    final var c = SyWindowViewportAccumulator.create();
    c.reset(64, 64);

    {
      final var cc =
        c0.componentForWindowRelative(PVector2I.of(0, 0), c);
      assertEquals(Optional.of(c0), cc);
    }

    {
      final var cc =
        c0.componentForWindowRelative(PVector2I.of(65, 65), c);
      assertEquals(Optional.empty(), cc);
    }
  }

  @Test
  public void testComponentForWindowRelativeInvisible()
  {
    final var c0 = this.create();
    c0.setBox(PAreasI.create(0, 0, 64, 64));
    c0.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);

    final var c = SyWindowViewportAccumulator.create();
    c.reset(64, 64);

    {
      final var cc =
        c0.componentForWindowRelative(PVector2I.of(0, 0), c);
      assertEquals(Optional.empty(), cc);
    }

    {
      final var cc =
        c0.componentForWindowRelative(PVector2I.of(65, 65), c);
      assertEquals(Optional.empty(), cc);
    }
  }

  @Test
  public void testComponentWindowless()
  {
    final var c0 = this.create();
    assertEquals(Optional.empty(), c0.window());
    assertEquals(Optional.empty(), c0.windowReadable());
  }

  @Test
  public void testComponentParentResizeFixed()
  {
    final var c0 = this.create();
    c0.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c0.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorHeight());
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorWidth());

    final var c1 = this.create();
    c1.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c1.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c1.resizeBehaviorHeight());
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c1.resizeBehaviorWidth());

    c0.setBox(PAreasI.create(0, 0, 32, 32));
    assertEquals(0L, (long) c0.box().minimumX());
    assertEquals(0L, (long) c0.box().minimumY());
    assertEquals(32L, (long) c0.box().sizeX());
    assertEquals(32L, (long) c0.box().sizeY());

    c1.setBox(PAreasI.create(0, 0, 16, 16));
    assertEquals(0L, (long) c1.box().minimumX());
    assertEquals(0L, (long) c1.box().minimumY());
    assertEquals(16L, (long) c1.box().sizeX());
    assertEquals(16L, (long) c1.box().sizeY());

    c0.node().childAdd(c1.node());

    c0.setBox(PAreasI.create(0, 0, 64, 64));
    assertEquals(0L, (long) c0.box().minimumX());
    assertEquals(0L, (long) c0.box().minimumY());
    assertEquals(64L, (long) c0.box().sizeX());
    assertEquals(64L, (long) c0.box().sizeY());

    assertEquals(0L, (long) c1.box().minimumX());
    assertEquals(0L, (long) c1.box().minimumY());
    assertEquals(16L, (long) c1.box().sizeX());
    assertEquals(16L, (long) c1.box().sizeY());
  }

  @Test
  public void testComponentParentResizeResize()
  {
    final var c0 = this.create();
    c0.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c0.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorHeight());
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorWidth());

    final var c1 = this.create();
    c1.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
    c1.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_RESIZE,
      c1.resizeBehaviorHeight());
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_RESIZE,
      c1.resizeBehaviorWidth());

    c0.setBox(PAreasI.create(0, 0, 32, 32));
    assertEquals(0L, (long) c0.box().minimumX());
    assertEquals(0L, (long) c0.box().minimumY());
    assertEquals(32L, (long) c0.box().sizeX());
    assertEquals(32L, (long) c0.box().sizeY());

    c1.setBox(PAreasI.create(0, 0, 16, 16));
    assertEquals(0L, (long) c1.box().minimumX());
    assertEquals(0L, (long) c1.box().minimumY());
    assertEquals(16L, (long) c1.box().sizeX());
    assertEquals(16L, (long) c1.box().sizeY());

    c0.node().childAdd(c1.node());

    c0.setBox(PAreasI.create(0, 0, 64, 64));
    assertEquals(0L, (long) c0.box().minimumX());
    assertEquals(0L, (long) c0.box().minimumY());
    assertEquals(64L, (long) c0.box().sizeX());
    assertEquals(64L, (long) c0.box().sizeY());

    assertEquals(0L, (long) c1.box().minimumX());
    assertEquals(0L, (long) c1.box().minimumY());
    assertEquals(16L + 32L, (long) c1.box().sizeX());
    assertEquals(16L + 32L, (long) c1.box().sizeY());
  }

  @Test
  public void testComponentParentResizeMove()
  {
    final var c0 = this.create();
    c0.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c0.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorHeight());
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorWidth());

    final var c1 = this.create();
    c1.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_MOVE);
    c1.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_MOVE);
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_MOVE,
      c1.resizeBehaviorHeight());
    assertEquals(
      SyParentResizeBehavior.BEHAVIOR_MOVE,
      c1.resizeBehaviorWidth());

    c0.setBox(PAreasI.create(0, 0, 32, 32));
    assertEquals(0L, (long) c0.box().minimumX());
    assertEquals(0L, (long) c0.box().minimumY());
    assertEquals(32L, (long) c0.box().sizeX());
    assertEquals(32L, (long) c0.box().sizeY());

    c1.setBox(PAreasI.create(0, 0, 16, 16));
    assertEquals(0L, (long) c1.box().minimumX());
    assertEquals(0L, (long) c1.box().minimumY());
    assertEquals(16L, (long) c1.box().sizeX());
    assertEquals(16L, (long) c1.box().sizeY());

    c0.node().childAdd(c1.node());

    c0.setBox(PAreasI.create(0, 0, 64, 64));
    assertEquals(0L, (long) c0.box().minimumX());
    assertEquals(0L, (long) c0.box().minimumY());
    assertEquals(64L, (long) c0.box().sizeX());
    assertEquals(64L, (long) c0.box().sizeY());

    assertEquals(32L, (long) c1.box().minimumX());
    assertEquals(32L, (long) c1.box().minimumY());
    assertEquals(16L, (long) c1.box().sizeX());
    assertEquals(16L, (long) c1.box().sizeY());
  }
}
