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

package com.io7m.jsycamore.awt;

import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.renderer.SyGUIRendererType;
import com.io7m.jsycamore.api.renderer.SyWindowRendererType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.windows.SyWindowType;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A very inefficient GUI renderer using AWT.
 */

public final class SyAWTGUIRendererInefficient
  implements SyGUIRendererType<BufferedImage, BufferedImage>
{
  private final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer;

  private SyAWTGUIRendererInefficient(
    final SyWindowRendererType<BufferedImage, BufferedImage> in_window_renderer)
  {
    this.window_renderer =
      NullCheck.notNull(in_window_renderer, "Window renderer");
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
    return new SyAWTGUIRendererInefficient(in_window_renderer);
  }

  @Override
  public BufferedImage render(
    final BufferedImage input,
    final SyGUIType ui)
  {
    NullCheck.notNull(input, "Input image");
    NullCheck.notNull(ui, "GUI");

    final Graphics2D graphics = input.createGraphics();

    final List<SyWindowType> windows = ui.windowsOpenOrdered();
    final int window_count = windows.size();
    for (int index = window_count - 1; index >= 0; --index) {
      final SyWindowType window = windows.get(index);
      final PAreaI<SySpaceViewportType> box = window.box();
      final BufferedImage bi =
        new BufferedImage(box.width(), box.height(), input.getType());

      this.window_renderer.render(bi, window);
      final AffineTransform transform = graphics.getTransform();
      try {
        graphics.translate(box.minimumX(), box.minimumY());
        graphics.drawImage(bi, 0, 0, null);
      } finally {
        graphics.setTransform(transform);
      }
    }

    return input;
  }
}
