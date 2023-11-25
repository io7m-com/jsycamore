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

import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.screens.SyScreenMouseEventsType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.awt.internal.SyAWTMouseAdapter;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_LEFT;
import static java.awt.event.MouseEvent.*;
import static org.mockito.Mockito.times;

public final class SyAWTMouseAdapterTest
{
  private SyScreenMouseEventsType screen;
  private Component component;

  @BeforeEach
  public void setup()
  {
    this.screen =
      Mockito.mock(SyScreenMouseEventsType.class);
    this.component =
      Mockito.mock(Component.class);

    Mockito.when(this.component.getLocationOnScreen())
      .thenReturn(new Point(0, 0));
  }

  /**
   * Test that mouse events are delivered.
   */

  @Test
  public void testMouseMovedEvents()
  {
    final var adapter = new SyAWTMouseAdapter(this.screen);
    adapter.mouseMoved(this.mouseEventOf(23, 25, BUTTON1));

    Mockito.verify(this.screen, times(1))
      .mouseMoved(PVector2I.of(23, 25));
    Mockito.verify(this.screen, times(0))
      .mouseUp(anyPoint(), Mockito.any());
    Mockito.verify(this.screen, times(0))
      .mouseDown(anyPoint(), Mockito.any());
  }

  /**
   * Test that mouse down events are delivered.
   */

  @Test
  public void testMouseDownEvents()
  {
    final var adapter = new SyAWTMouseAdapter(this.screen);
    adapter.mousePressed(this.mouseEventOf(23, 25, BUTTON1));

    Mockito.verify(this.screen, times(1))
      .mouseDown(PVector2I.of(23, 25), MOUSE_BUTTON_LEFT);
    Mockito.verify(this.screen, times(0))
      .mouseMoved(anyPoint());
    Mockito.verify(this.screen, times(0))
      .mouseUp(anyPoint(), Mockito.any());
  }

  /**
   * Test that mouse up events are delivered.
   */

  @Test
  public void testMouseUpEvents()
  {
    final var adapter = new SyAWTMouseAdapter(this.screen);
    adapter.mouseReleased(this.mouseEventOf(23, 25, BUTTON1));

    Mockito.verify(this.screen, times(1))
      .mouseUp(PVector2I.of(23, 25), MOUSE_BUTTON_LEFT);
    Mockito.verify(this.screen, times(0))
      .mouseMoved(anyPoint());
    Mockito.verify(this.screen, times(0))
      .mouseDown(anyPoint(), Mockito.any());
  }

  /**
   * Test that mouse drag events are delivered.
   */

  @Test
  public void testMouseDraggedEvents()
  {
    final var adapter = new SyAWTMouseAdapter(this.screen);
    adapter.mouseDragged(this.mouseEventOf(23, 25, BUTTON1));

    Mockito.verify(this.screen, times(1))
      .mouseMoved(PVector2I.of(23, 25));
    Mockito.verify(this.screen, times(0))
      .mouseUp(anyPoint(), Mockito.any());
    Mockito.verify(this.screen, times(0))
      .mouseDown(anyPoint(), Mockito.any());
  }

  private static PVector2I<SySpaceViewportType> anyPoint()
  {
    return Mockito.any();
  }

  private MouseEvent mouseEventOf(
    final int x,
    final int y,
    final int button)
  {
    return new MouseEvent(
      this.component,
      0,
      0L,
      0,
      x,
      y,
      0,
      false,
      button
    );
  }
}
