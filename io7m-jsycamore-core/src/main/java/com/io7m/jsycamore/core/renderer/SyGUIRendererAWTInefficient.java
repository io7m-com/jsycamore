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

package com.io7m.jsycamore.core.renderer;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A very inefficient GUI renderer using AWT.
 */

public final class SyGUIRendererAWTInefficient
  implements SyGUIRendererType<BufferedImage, BufferedImage>
{
  private final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer;

  private SyGUIRendererAWTInefficient(
    final SyWindowRendererType<BufferedImage, BufferedImage> in_window_renderer)
  {
    this.window_renderer = NullCheck.notNull(in_window_renderer);
  }

  /**
   * Create a new GUI renderer.
   *
   * @param in_window_renderer A window renderer
   *
   * @return A new renderer
   */

  public static SyGUIRendererType<BufferedImage, BufferedImage> create(
    final SyWindowRendererType<BufferedImage, BufferedImage> in_window_renderer)
  {
    return new SyGUIRendererAWTInefficient(in_window_renderer);
  }

  @Override
  public BufferedImage render(
    final BufferedImage input,
    final SyGUIType ui)
  {
    NullCheck.notNull(input);
    NullCheck.notNull(ui);

    final Graphics2D graphics = input.createGraphics();

    final List<SyWindowType> windows = ui.windowsOpenOrdered();
    final int window_count = windows.size();
    for (int index = window_count - 1; index >= 0; --index) {
      final SyWindowType window = windows.get(index);
      final VectorReadable2IType bounds = window.bounds();
      final BufferedImage bi =
        new BufferedImage(bounds.getXI(), bounds.getYI(), input.getType());

      this.window_renderer.render(bi, window);
      final AffineTransform transform = graphics.getTransform();
      try {
        final PVectorReadable2IType<SySpaceViewportType> position =
          window.position();
        graphics.translate(position.getXI(), position.getYI());
        graphics.drawImage(bi, 0, 0, null);
      } finally {
        graphics.setTransform(transform);
      }
    }

    return input;
  }
}
