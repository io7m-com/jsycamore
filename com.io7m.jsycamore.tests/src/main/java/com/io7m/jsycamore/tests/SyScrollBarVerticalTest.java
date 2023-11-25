/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.tests;

import com.io7m.jsycamore.api.components.SyScrollBarDrag;
import com.io7m.jsycamore.api.components.SyScrollBarVerticalType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.components.standard.SyScrollBarsVertical;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static com.io7m.jsycamore.api.components.SyScrollBarDrag.Kind.DRAG_CONTINUED;
import static com.io7m.jsycamore.api.components.SyScrollBarDrag.Kind.DRAG_ENDED;
import static com.io7m.jsycamore.api.components.SyScrollBarDrag.Kind.DRAG_STARTED;
import static com.io7m.jsycamore.api.components.SyScrollBarPresencePolicy.ALWAYS_ENABLED;
import static com.io7m.jsycamore.api.components.SyScrollBarPresencePolicy.DISABLED_IF_ENTIRE_RANGE_SHOWN;
import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_LEFT;
import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyScrollBarVerticalTest
  extends SyComponentContract<SyScrollBarVerticalType>
{
  private static final PVector2I<SySpaceViewportType> Z =
    PVectors2I.zero();
  private static final PVector2I<SySpaceViewportType> ELSEWHERE =
    PVector2I.of(8192, 8192);

  private int clicks;
  private LinkedList<SyScrollBarDrag> drags;

  @BeforeEach
  public void buttonSetup()
  {
    this.clicks = 0;
    this.drags = new LinkedList<SyScrollBarDrag>();
  }

  @Override
  protected SyScrollBarVerticalType newComponent()
  {
    return SyScrollBarsVertical.create();
  }

  /**
   * Pressing scrollbar buttons works.
   */

  @Test
  public void testClickUp()
  {
    final var c = this.newComponent();
    final var b = c.buttonUp();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnClickUpListener(() -> this.clicks++);

    this.screen().mouseDown(Z, MOUSE_BUTTON_LEFT);
    assertEquals(0, this.clicks);

    this.screen().mouseUp(Z, MOUSE_BUTTON_LEFT);
    assertEquals(1, this.clicks);

    c.removeOnClickUpListener();

    this.screen().mouseDown(Z, MOUSE_BUTTON_LEFT);
    assertEquals(1, this.clicks);

    this.screen().mouseUp(Z, MOUSE_BUTTON_LEFT);
    assertEquals(1, this.clicks);
  }

  /**
   * Pressing scrollbar buttons with the wrong mouse buttons does nothing.
   */

  @Test
  public void testClickUpNotButton()
  {
    final var c = this.newComponent();
    final var b = c.buttonUp();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnClickUpListener(() -> this.clicks++);

    this.screen().mouseDown(Z, MOUSE_BUTTON_RIGHT);
    assertEquals(0, this.clicks);

    this.screen().mouseUp(Z, MOUSE_BUTTON_RIGHT);
    assertEquals(0, this.clicks);
  }

  /**
   * Pressing scrollbar buttons works.
   */

  @Test
  public void testClickDown()
  {
    final var c = this.newComponent();
    final var b = c.buttonDown();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnClickDownListener(() -> this.clicks++);

    final var p =
      c.viewportPositionOf(b.position().get());
    final var q =
      PVector2I.<SySpaceViewportType>of(p.x() + 4, p.y() + 4);

    this.screen().mouseDown(q, MOUSE_BUTTON_LEFT);
    assertEquals(0, this.clicks);

    this.screen().mouseUp(q, MOUSE_BUTTON_LEFT);
    assertEquals(1, this.clicks);

    c.removeOnClickDownListener();

    this.screen().mouseDown(q, MOUSE_BUTTON_LEFT);
    assertEquals(1, this.clicks);

    this.screen().mouseUp(q, MOUSE_BUTTON_LEFT);
    assertEquals(1, this.clicks);
  }

  /**
   * Pressing scrollbar buttons with the wrong mouse buttons does nothing.
   */

  @Test
  public void testClickDownNotButton()
  {
    final var c = this.newComponent();
    final var b = c.buttonDown();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnClickDownListener(() -> this.clicks++);

    final var p =
      c.viewportPositionOf(b.position().get());
    final var q =
      PVector2I.<SySpaceViewportType>of(p.x() + 4, p.y() + 4);

    this.screen().mouseDown(q, MOUSE_BUTTON_RIGHT);
    assertEquals(0, this.clicks);

    this.screen().mouseUp(q, MOUSE_BUTTON_RIGHT);
    assertEquals(0, this.clicks);
  }

  /**
   * Setting the scroll position works.
   */

  @Test
  public void testScrollPosition()
  {
    final var c = this.newComponent();

    c.setScrollPosition(-1.0);
    assertEquals(0.0, c.scrollPosition());
    c.setScrollPosition(0.0);
    assertEquals(0.0, c.scrollPosition());
    c.setScrollPosition(1.0);
    assertEquals(1.0, c.scrollPosition());
    c.setScrollPosition(2.0);
    assertEquals(1.0, c.scrollPosition());
  }

  /**
   * Setting the scroll snapping works.
   */

  @Test
  public void testScrollSnapping()
  {
    final var c = this.newComponent();

    c.setScrollPositionSnapping(-1.0);
    assertEquals(0.0, c.scrollPositionSnapping());
    c.setScrollPositionSnapping(0.0);
    assertEquals(0.0, c.scrollPositionSnapping());
    c.setScrollPositionSnapping(1.0);
    assertEquals(1.0, c.scrollPositionSnapping());
    c.setScrollPositionSnapping(2.0);
    assertEquals(1.0, c.scrollPositionSnapping());
  }

  /**
   * Dragging scrollbar thumbs works.
   */

  @Test
  public void testThumbDrag()
  {
    final var c = this.newComponent();
    final var t = c.thumb();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnThumbDragListener(drag -> this.drags.add(drag));

    final var p0 =
      t.viewportPositionOf(t.position().get());
    final var p1 =
      PVector2I.<SySpaceViewportType>of(p0.x() + 4, p0.y() + 4);
    final var p2 =
      PVector2I.<SySpaceViewportType>of(p1.x(), p1.y() + 128);

    this.screen().mouseDown(p1, MOUSE_BUTTON_LEFT);
    this.screen().mouseMoved(p2);
    this.screen().mouseUp(p2, MOUSE_BUTTON_LEFT);

    {
      final var d = this.drags.remove(0);
      assertEquals(DRAG_STARTED, d.dragKind());
      assertEquals(0.0, d.dragStart());
      assertEquals(0.0, d.dragNow());
    }

    {
      final var d = this.drags.remove(0);
      assertEquals(DRAG_CONTINUED, d.dragKind());
      assertEquals(0.0, d.dragStart());
      assertNotEquals(0.0, d.dragNow());
    }

    {
      final var d = this.drags.remove(0);
      assertEquals(DRAG_ENDED, d.dragKind());
      assertEquals(0.0, d.dragStart());
      assertNotEquals(0.0, d.dragNow());
    }

    c.removeOnThumbDragListener();

    this.screen().mouseDown(p1, MOUSE_BUTTON_LEFT);
    this.screen().mouseMoved(p2);
    this.screen().mouseUp(p2, MOUSE_BUTTON_LEFT);

    assertEquals(0, this.drags.size());
  }

  /**
   * Dragging scrollbar thumbs with the wrong mouse buttons does nothing.
   */

  @Test
  public void testThumbDragNotButton()
  {
    final var c = this.newComponent();
    final var t = c.thumb();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnThumbDragListener(drag -> this.drags.add(drag));

    final var p0 =
      t.viewportPositionOf(t.position().get());
    final var p1 =
      PVector2I.<SySpaceViewportType>of(p0.x() + 4, p0.y() + 4);
    final var p2 =
      PVector2I.<SySpaceViewportType>of(p1.x() + 128, p1.y());

    this.screen().mouseDown(p1, MOUSE_BUTTON_RIGHT);
    this.screen().mouseMoved(p2);
    this.screen().mouseUp(p2, MOUSE_BUTTON_RIGHT);

    assertEquals(0, this.drags.size());
  }

  /**
   * The ALWAYS_ENABLED presence policy works.
   */

  @Test
  public void testPresencePolicyAlwaysActive()
  {
    final var c = this.newComponent();
    c.presencePolicy().set(ALWAYS_ENABLED);

    final var t = c.thumb();
    final var l = c.buttonUp();
    final var r = c.buttonDown();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setScrollAmountShown(0.0);
    assertTrue(t.isActive());
    assertTrue(l.isActive());
    assertTrue(r.isActive());

    c.setScrollAmountShown(1.0);
    assertTrue(t.isActive());
    assertTrue(l.isActive());
    assertTrue(r.isActive());

    c.setScrollAmountShown(0.0);
    assertTrue(t.isActive());
    assertTrue(l.isActive());
    assertTrue(r.isActive());
  }

  /**
   * The DISABLED_IF_ENTIRE_RANGE_SHOWN presence policy works.
   */

  @Test
  public void testPresencePolicyDisabledIfFullyShown()
  {
    final var c = this.newComponent();
    c.presencePolicy().set(DISABLED_IF_ENTIRE_RANGE_SHOWN);

    final var t = c.thumb();
    final var l = c.buttonUp();
    final var r = c.buttonDown();

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setScrollAmountShown(0.0);
    assertTrue(t.isActive());
    assertTrue(l.isActive());
    assertTrue(r.isActive());

    c.setScrollAmountShown(1.0);
    assertFalse(t.isActive());
    assertFalse(l.isActive());
    assertFalse(r.isActive());

    c.setScrollAmountShown(0.0);
    assertTrue(t.isActive());
    assertTrue(l.isActive());
    assertTrue(r.isActive());
  }
}
