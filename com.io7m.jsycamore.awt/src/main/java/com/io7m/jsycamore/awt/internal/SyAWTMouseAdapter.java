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

package com.io7m.jsycamore.awt.internal;

import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.screens.SyScreenMouseEventsType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * An adapter that converts AWT mouse events to {@code jsycamore} mouse calls.
 */

public final class SyAWTMouseAdapter extends MouseAdapter
{
  private final SyScreenMouseEventsType screen;

  /**
   * An adapter that converts AWT mouse events to {@code jsycamore} mouse
   * calls.
   *
   * @param inScreen The receiver of mouse events
   */

  public SyAWTMouseAdapter(
    final SyScreenMouseEventsType inScreen)
  {
    this.screen = Objects.requireNonNull(inScreen, "screen");
  }

  @Override
  public void mousePressed(
    final MouseEvent e)
  {
    this.screen.mouseDown(
      PVector2I.of(e.getX(), e.getY()),
      SyMouseButton.ofIndex(e.getButton() - 1)
    );
  }

  @Override
  public void mouseReleased(
    final MouseEvent e)
  {
    this.screen.mouseUp(
      PVector2I.of(e.getX(), e.getY()),
      SyMouseButton.ofIndex(e.getButton() - 1)
    );
  }

  @Override
  public void mouseDragged(
    final MouseEvent e)
  {
    this.screen.mouseMoved(PVector2I.of(e.getX(), e.getY()));
  }

  @Override
  public void mouseMoved(
    final MouseEvent e)
  {
    final PVector2I<SySpaceViewportType> position =
      PVector2I.of(e.getX(), e.getY());
    this.screen.mouseMoved(position);
  }
}
