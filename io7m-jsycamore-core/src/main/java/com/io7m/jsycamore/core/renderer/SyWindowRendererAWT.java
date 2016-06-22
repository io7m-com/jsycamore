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
import com.io7m.jsycamore.core.SyThemeType;
import com.io7m.jsycamore.core.SyThemeWindowType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import org.valid4j.Assertive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * A simple software renderer that renders a window to an image.
 */

public final class SyWindowRendererAWT implements
  SyWindowRendererType<BufferedImage, BufferedImage>
{
  private final RoundRectangle2D.Double draw_rect;
  private final Polygon poly;
  private final SyEmbossed emboss;

  /**
   * @return A new renderer
   */

  public static SyWindowRendererType<BufferedImage, BufferedImage> create()
  {
    return new SyWindowRendererAWT();
  }

  private SyWindowRendererAWT()
  {
    this.draw_rect = new RoundRectangle2D.Double();
    this.poly = new Polygon();
    this.emboss = new SyEmbossed();
  }

  @Override
  public BufferedImage render(
    final BufferedImage input,
    final SyWindowType window)
  {
    NullCheck.notNull(input);
    NullCheck.notNull(window);

    final VectorReadable2IType window_size = window.size();
    Assertive.require(input.getWidth() >= window_size.getXI());
    Assertive.require(input.getHeight() >= window_size.getYI());

    final Graphics2D graphics = input.createGraphics();
    graphics.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setRenderingHint(
      RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();

    final Color window_color =
      SyWindowRendererAWT.toColor(theme.mainBackgroundColor());

    this.draw_rect.setRoundRect(
      0.0,
      0.0,
      (double) window_size.getXI(),
      (double) window_size.getYI(),
      0.0,
      0.0);

    graphics.setClip(0, 0, window_size.getXI(), window_size.getYI());

    this.emboss.rectangle(
      graphics,
      0,
      0,
      window_size.getXI(),
      window_size.getYI(),
      3,
      Color.RED,
      Color.GREEN,
      Color.YELLOW,
      Color.BLUE,
      Optional.empty());

    final SyEmbossed emb = new SyEmbossed();

    int y = 32;

    {
      int x = 32;
      for (final SyEmbossed.LShape v : SyEmbossed.LShape.values()) {
        emb.drawEmbossedL(
          graphics,
          v,
          x,
          y,
          24,
          16,
          64,
          5,
          Color.RED,
          Color.GREEN,
          Color.YELLOW,
          Color.BLUE,
          Optional.of(Color.WHITE),
          false);
        x += 100;
      }
    }

    {
      y += 100;
      int x = 32;
      for (final SyEmbossed.LShape v : SyEmbossed.LShape.values()) {
        emb.drawEmbossedL(
          graphics,
          v,
          x,
          y,
          24,
          16,
          64,
          5,
          Color.RED,
          Color.GREEN,
          Color.YELLOW,
          Color.BLUE,
          Optional.empty(),
          false);
        x += 100;
      }
    }

    {
      y += 100;
      int x = 32;
      for (final SyEmbossed.LShape v : SyEmbossed.LShape.values()) {
        emb.drawEmbossedL(
          graphics,
          v,
          x,
          y,
          24,
          16,
          64,
          5,
          Color.RED,
          Color.GREEN,
          Color.YELLOW,
          Color.BLUE,
          Optional.of(Color.WHITE),
          true);
        x += 100;
      }
    }

    {
      y += 100;
      int x = 32;
      for (final SyEmbossed.LShape v : SyEmbossed.LShape.values()) {
        emb.drawEmbossedL(
          graphics,
          v,
          x,
          y,
          24,
          16,
          64,
          5,
          Color.RED,
          Color.GREEN,
          Color.YELLOW,
          Color.BLUE,
          Optional.empty(),
          true);
        x += 100;
      }
    }

    return input;
  }

  private static Color toColor(
    final VectorReadable3FType color)
  {
    final float r = Math.min(1.0f, Math.max(0.0f, color.getXF()));
    final float g = Math.min(1.0f, Math.max(0.0f, color.getYF()));
    final float b = Math.min(1.0f, Math.max(0.0f, color.getZF()));
    return new Color(r, g, b);
  }
}
