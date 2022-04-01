/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.windows.SyWindowType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A trivial renderer that only renders the bounds of objects.
 */

public final class SyBoundsOnlyRenderer implements SyRendererType
{
  /**
   * A trivial renderer that only renders the bounds of objects.
   */

  public SyBoundsOnlyRenderer()
  {

  }

  @Override
  public void render(
    final Graphics2D g,
    final SyScreenType screen,
    final SyWindowType window)
  {
    final var oldTransform = g.getTransform();
    final var oldClip = g.getClip();

    try {
      final var bounds = window.boundingArea();
      g.setTransform(
        AffineTransform.getTranslateInstance(
          bounds.minimumX(),
          bounds.minimumY())
      );
      g.setPaint(Color.BLUE);
      g.drawRect(0, 0, bounds.sizeX(), bounds.sizeY());
      g.setClip(0, 0, bounds.sizeX(), bounds.sizeY());

      final var root = window.rootNodeReadable();
      this.renderComponent(g, screen, root);

    } finally {
      g.setTransform(oldTransform);
      g.setClip(oldClip);
    }
  }

  private void renderComponent(
    final Graphics2D g,
    final SyScreenType gui,
    final JOTreeNodeReadableType<SyComponentReadableType> node)
  {
    final var component = node.value();
    if (!component.isVisible()) {
      return;
    }

    final var oldTransform = g.getTransform();
    final var oldClip = g.getClip();

    try {
      final var bounds =
        component.boundingArea();

      g.translate(bounds.minimumX(), bounds.minimumY());

      final var color =
        new Color(component.hashCode())
          .brighter()
          .brighter()
          .brighter();

      final var translucent =
        new Color(
          color.getRed(),
          color.getGreen(),
          color.getBlue(),
          255 / 4
        );

      final Optional<SyComponentReadableType> over =
        gui.componentOver().map(Function.identity());

      if (Objects.equals(over, Optional.of(component))) {
        g.setPaint(color);
        g.fillRect(0, 0, bounds.sizeX(), bounds.sizeY());
      } else {
        g.setPaint(translucent);
        g.fillRect(0, 0, bounds.sizeX(), bounds.sizeY());
        g.setPaint(color);
        g.drawRect(0, 0, bounds.sizeX(), bounds.sizeY());
      }

      final var children =
        node.childrenReadable();
      for (final var child : children) {
        this.renderComponent(g, gui, child);
      }
    } finally {
      g.setTransform(oldTransform);
      g.setClip(oldClip);
    }
  }
}
