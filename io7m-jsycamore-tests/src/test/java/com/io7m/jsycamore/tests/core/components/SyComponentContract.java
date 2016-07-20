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

import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyActive;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.components.SyVisibility;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulator;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.parameterized.PVectorI2I;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

public abstract class SyComponentContract
{
  protected abstract SyComponentType create();

  @Rule public ExpectedException expected = ExpectedException.none();

  @Test
  public void testComponentActive()
  {
    final SyComponentType c0 = this.create();
    Assert.assertEquals(SyActive.ACTIVE, c0.activity());
    Assert.assertTrue(c0.isActive());

    c0.setActive(SyActive.INACTIVE);
    Assert.assertEquals(SyActive.INACTIVE, c0.activity());
    Assert.assertFalse(c0.isActive());

    c0.setActive(SyActive.ACTIVE);
    Assert.assertEquals(SyActive.ACTIVE, c0.activity());
    Assert.assertTrue(c0.isActive());
  }

  @Test
  public void testComponentActiveInherited()
  {
    final SyComponentType c0 = this.create();
    final SyComponentType c1 = this.create();

    c0.node().childAdd(c1.node());

    Assert.assertTrue(c0.isActive());
    Assert.assertTrue(c1.isActive());

    c0.setActive(SyActive.INACTIVE);

    Assert.assertFalse(c0.isActive());
    Assert.assertFalse(c1.isActive());

    c0.setActive(SyActive.ACTIVE);

    Assert.assertTrue(c0.isActive());
    Assert.assertTrue(c1.isActive());

    c1.setActive(SyActive.INACTIVE);

    Assert.assertTrue(c0.isActive());
    Assert.assertFalse(c1.isActive());

    c1.setActive(SyActive.ACTIVE);

    Assert.assertTrue(c0.isActive());
    Assert.assertTrue(c1.isActive());
  }

  @Test
  public void testComponentVisible()
  {
    final SyComponentType c0 = this.create();
    Assert.assertEquals(SyVisibility.VISIBILITY_VISIBLE, c0.visibility());
    Assert.assertTrue(c0.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);
    Assert.assertEquals(SyVisibility.VISIBILITY_INVISIBLE, c0.visibility());
    Assert.assertFalse(c0.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_VISIBLE);
    Assert.assertEquals(SyVisibility.VISIBILITY_VISIBLE, c0.visibility());
    Assert.assertTrue(c0.isVisible());
  }

  @Test
  public void testComponentVisibleInherited()
  {
    final SyComponentType c0 = this.create();
    final SyComponentType c1 = this.create();

    c0.node().childAdd(c1.node());

    Assert.assertTrue(c0.isVisible());
    Assert.assertTrue(c1.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);

    Assert.assertFalse(c0.isVisible());
    Assert.assertFalse(c1.isVisible());

    c0.setVisibility(SyVisibility.VISIBILITY_VISIBLE);

    Assert.assertTrue(c0.isVisible());
    Assert.assertTrue(c1.isVisible());

    c1.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);

    Assert.assertTrue(c0.isVisible());
    Assert.assertFalse(c1.isVisible());

    c1.setVisibility(SyVisibility.VISIBILITY_VISIBLE);

    Assert.assertTrue(c0.isVisible());
    Assert.assertTrue(c1.isVisible());
  }

  @Test
  public void testComponentForWindowRelative()
  {
    final SyComponentType c0 = this.create();
    c0.setBox(SyBoxes.create(0, 0, 64, 64));

    final SyWindowViewportAccumulatorType c = SyWindowViewportAccumulator.create();
    c.reset(64, 64);

    {
      final Optional<SyComponentType> cc =
        c0.componentForWindowRelative(new PVectorI2I<>(0, 0), c);
      Assert.assertEquals(Optional.of(c0), cc);
    }

    {
      final Optional<SyComponentType> cc =
        c0.componentForWindowRelative(new PVectorI2I<>(65, 65), c);
      Assert.assertEquals(Optional.empty(), cc);
    }
  }

  @Test
  public void testComponentForWindowRelativeInvisible()
  {
    final SyComponentType c0 = this.create();
    c0.setBox(SyBoxes.create(0, 0, 64, 64));
    c0.setVisibility(SyVisibility.VISIBILITY_INVISIBLE);

    final SyWindowViewportAccumulatorType c = SyWindowViewportAccumulator.create();
    c.reset(64, 64);

    {
      final Optional<SyComponentType> cc =
        c0.componentForWindowRelative(new PVectorI2I<>(0, 0), c);
      Assert.assertEquals(Optional.empty(), cc);
    }

    {
      final Optional<SyComponentType> cc =
        c0.componentForWindowRelative(new PVectorI2I<>(65, 65), c);
      Assert.assertEquals(Optional.empty(), cc);
    }
  }

  @Test
  public void testComponentWindowless()
  {
    final SyComponentType c0 = this.create();
    Assert.assertEquals(Optional.empty(), c0.window());
    Assert.assertEquals(Optional.empty(), c0.windowReadable());
  }

  @Test
  public void testComponentParentResizeFixed()
  {
    final SyComponentType c0 = this.create();
    c0.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c0.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorHeight());
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorWidth());

    final SyComponentType c1 = this.create();
    c1.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c1.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c1.resizeBehaviorHeight());
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c1.resizeBehaviorWidth());

    c0.setBox(SyBoxes.create(0, 0, 32, 32));
    Assert.assertEquals(0L, (long) c0.box().minimumX());
    Assert.assertEquals(0L, (long) c0.box().minimumY());
    Assert.assertEquals(32L, (long) c0.box().width());
    Assert.assertEquals(32L, (long) c0.box().height());

    c1.setBox(SyBoxes.create(0, 0, 16, 16));
    Assert.assertEquals(0L, (long) c1.box().minimumX());
    Assert.assertEquals(0L, (long) c1.box().minimumY());
    Assert.assertEquals(16L, (long) c1.box().width());
    Assert.assertEquals(16L, (long) c1.box().height());

    c0.node().childAdd(c1.node());

    c0.setBox(SyBoxes.create(0, 0, 64, 64));
    Assert.assertEquals(0L, (long) c0.box().minimumX());
    Assert.assertEquals(0L, (long) c0.box().minimumY());
    Assert.assertEquals(64L, (long) c0.box().width());
    Assert.assertEquals(64L, (long) c0.box().height());

    Assert.assertEquals(0L, (long) c1.box().minimumX());
    Assert.assertEquals(0L, (long) c1.box().minimumY());
    Assert.assertEquals(16L, (long) c1.box().width());
    Assert.assertEquals(16L, (long) c1.box().height());
  }

  @Test
  public void testComponentParentResizeResize()
  {
    final SyComponentType c0 = this.create();
    c0.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c0.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorHeight());
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorWidth());

    final SyComponentType c1 = this.create();
    c1.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
    c1.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_RESIZE,
      c1.resizeBehaviorHeight());
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_RESIZE,
      c1.resizeBehaviorWidth());

    c0.setBox(SyBoxes.create(0, 0, 32, 32));
    Assert.assertEquals(0L, (long) c0.box().minimumX());
    Assert.assertEquals(0L, (long) c0.box().minimumY());
    Assert.assertEquals(32L, (long) c0.box().width());
    Assert.assertEquals(32L, (long) c0.box().height());

    c1.setBox(SyBoxes.create(0, 0, 16, 16));
    Assert.assertEquals(0L, (long) c1.box().minimumX());
    Assert.assertEquals(0L, (long) c1.box().minimumY());
    Assert.assertEquals(16L, (long) c1.box().width());
    Assert.assertEquals(16L, (long) c1.box().height());

    c0.node().childAdd(c1.node());

    c0.setBox(SyBoxes.create(0, 0, 64, 64));
    Assert.assertEquals(0L, (long) c0.box().minimumX());
    Assert.assertEquals(0L, (long) c0.box().minimumY());
    Assert.assertEquals(64L, (long) c0.box().width());
    Assert.assertEquals(64L, (long) c0.box().height());

    Assert.assertEquals(0L, (long) c1.box().minimumX());
    Assert.assertEquals(0L, (long) c1.box().minimumY());
    Assert.assertEquals(16L + 32L, (long) c1.box().width());
    Assert.assertEquals(16L + 32L, (long) c1.box().height());
  }

  @Test
  public void testComponentParentResizeMove()
  {
    final SyComponentType c0 = this.create();
    c0.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_FIXED);
    c0.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_FIXED);
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorHeight());
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_FIXED,
      c0.resizeBehaviorWidth());

    final SyComponentType c1 = this.create();
    c1.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_MOVE);
    c1.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_MOVE);
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_MOVE,
      c1.resizeBehaviorHeight());
    Assert.assertEquals(
      SyParentResizeBehavior.BEHAVIOR_MOVE,
      c1.resizeBehaviorWidth());

    c0.setBox(SyBoxes.create(0, 0, 32, 32));
    Assert.assertEquals(0L, (long) c0.box().minimumX());
    Assert.assertEquals(0L, (long) c0.box().minimumY());
    Assert.assertEquals(32L, (long) c0.box().width());
    Assert.assertEquals(32L, (long) c0.box().height());

    c1.setBox(SyBoxes.create(0, 0, 16, 16));
    Assert.assertEquals(0L, (long) c1.box().minimumX());
    Assert.assertEquals(0L, (long) c1.box().minimumY());
    Assert.assertEquals(16L, (long) c1.box().width());
    Assert.assertEquals(16L, (long) c1.box().height());

    c0.node().childAdd(c1.node());

    c0.setBox(SyBoxes.create(0, 0, 64, 64));
    Assert.assertEquals(0L, (long) c0.box().minimumX());
    Assert.assertEquals(0L, (long) c0.box().minimumY());
    Assert.assertEquals(64L, (long) c0.box().width());
    Assert.assertEquals(64L, (long) c0.box().height());

    Assert.assertEquals(32L, (long) c1.box().minimumX());
    Assert.assertEquals(32L, (long) c1.box().minimumY());
    Assert.assertEquals(16L, (long) c1.box().width());
    Assert.assertEquals(16L, (long) c1.box().height());
  }
}
