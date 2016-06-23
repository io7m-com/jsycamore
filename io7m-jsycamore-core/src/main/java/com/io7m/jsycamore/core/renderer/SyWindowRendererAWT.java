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
import com.io7m.jsycamore.core.SyThemeEmbossType;
import com.io7m.jsycamore.core.SyThemeType;
import com.io7m.jsycamore.core.SyThemeWindowMarginType;
import com.io7m.jsycamore.core.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.core.SyThemeWindowType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.junreachable.UnimplementedCodeException;
import org.valid4j.Assertive;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * A simple software renderer that renders a window to an image.
 */

public final class SyWindowRendererAWT implements
  SyWindowRendererType<BufferedImage, BufferedImage>
{
  private final RoundRectangle2D.Double draw_rect;
  private final Polygon poly;
  private final SyEmbossed embossed;
  private final WeakHashMap<String, Font> font_cache;

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
    this.embossed = new SyEmbossed();
    this.font_cache = new WeakHashMap<>();
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

    this.renderMargin(input, graphics, window);
    this.renderTitlebar(input, graphics, window);
    return input;
  }

  private void renderTitlebar(
    final BufferedImage input,
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();

    try {
      if (window.active()) {
        this.renderTitlebarActive(input, graphics, window);
      } else {
        this.renderTitlebarInactive(input, graphics, window);
      }
    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
    }
  }

  private void renderTitlebarInactive(
    final BufferedImage input,
    final Graphics2D graphics,
    final SyWindowType window)
  {
    throw new UnimplementedCodeException();
  }

  private void renderTitlebarActive(
    final BufferedImage input,
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowTitleBarType titlebar = window_theme.titleBar();
    final SyThemeWindowMarginType margin = window_theme.margin();
    final VectorReadable2IType window_size = window.size();

    final int x = margin.leftWidth();
    final int y = margin.topHeight();
    final int w = window_size.getXI() - (x + margin.rightWidth());
    final int h = titlebar.height();
    graphics.clipRect(x, y, w, h);
    graphics.translate(x, y);

    /**
     * Render actual bar.
     */

    final Color fill = SyWindowRendererAWT.toColor(titlebar.colorActive());
    final Optional<Paint> fill_opt = Optional.of(fill);
    final Optional<SyThemeEmbossType> emboss_opt = titlebar.embossActive();
    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      final Paint left = SyWindowRendererAWT.toColor(emboss.colorLeft());
      final Color right = SyWindowRendererAWT.toColor(emboss.colorRight());
      final Color bottom = SyWindowRendererAWT.toColor(emboss.colorBottom());
      final Color top = SyWindowRendererAWT.toColor(emboss.colorTop());

      this.embossed.rectangle(
        graphics,
        0,
        0,
        w,
        h,
        emboss.size(),
        left,
        right,
        top,
        bottom,
        fill_opt);
    } else {
      graphics.setPaint(fill);
      graphics.fillRect(0, 0, w, h);
    }

    /**
     * Render window text.
     */

    {
      graphics.setFont(
        this.font_cache.computeIfAbsent(titlebar.textFont(), Font::decode));

      final FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
      final int text_width = metrics.stringWidth(window.text());

      int text_x = 0;
      final int text_y = (titlebar.height() / 4) * 3;
      switch (titlebar.textAlignment()) {
        case ALIGN_LEFT: {
          text_x = metrics.stringWidth(" ");
          break;
        }
        case ALIGN_RIGHT: {
          text_x = w - (text_width + metrics.stringWidth(" "));
          break;
        }
        case ALIGN_CENTER: {
          text_x = (w / 2) - (text_width / 2);
          break;
        }
      }

      graphics.setPaint(Color.BLACK);
      graphics.drawString(window.text(), text_x, text_y);
    }
  }

  private void renderMargin(
    final BufferedImage input,
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();

    try {
      if (window.active()) {
        this.renderMarginActive(input, graphics, window);
      } else {
        this.renderMarginInactive(input, graphics, window);
      }
    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
    }
  }

  private void renderMarginInactive(
    final BufferedImage input,
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowMarginType margin_theme = window_theme.margin();
    final VectorReadable2IType window_size = window.size();

    final Optional<SyThemeEmbossType> emboss_opt = margin_theme.embossInactive();
    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      this.renderMarginInactiveEmbossed(
        graphics, margin_theme, window_size, emboss,
        SyWindowRendererAWT.toColor(margin_theme.colorInactive()));
    } else {
      throw new UnimplementedCodeException();
    }
  }

  private void renderMarginInactiveEmbossed(
    final Graphics2D graphics,
    final SyThemeWindowMarginType margin_theme,
    final VectorReadable2IType window_size,
    final SyThemeEmbossType emboss,
    final Color color)
  {
    throw new UnimplementedCodeException();
  }

  private void renderMarginActive(
    final BufferedImage input,
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowMarginType margin_theme = window_theme.margin();
    final VectorReadable2IType window_size = window.size();

    final Optional<SyThemeEmbossType> emboss_opt = margin_theme.embossActive();
    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      this.renderMarginActiveEmbossed(
        graphics, window_theme.titleBar(), margin_theme, window_size, emboss,
        SyWindowRendererAWT.toColor(margin_theme.colorActive()));
    } else {
      throw new UnimplementedCodeException();
    }
  }

  private void renderMarginActiveEmbossed(
    final Graphics2D graphics,
    final SyThemeWindowTitleBarType titlebar_theme,
    final SyThemeWindowMarginType margin_theme,
    final VectorReadable2IType window_size,
    final SyThemeEmbossType emboss,
    final Paint e_fill)
  {
    final Optional<Paint> eo_fill =
      Optional.of(e_fill);
    final Color e_top =
      SyWindowRendererAWT.toColor(emboss.colorTop());
    final Color e_left =
      SyWindowRendererAWT.toColor(emboss.colorLeft());
    final Color e_right =
      SyWindowRendererAWT.toColor(emboss.colorRight());
    final Color e_bottom =
      SyWindowRendererAWT.toColor(emboss.colorBottom());

    final int left_width = margin_theme.leftWidth();
    final int right_width = margin_theme.rightWidth();
    final int top_height = margin_theme.topHeight();
    final int bottom_height = margin_theme.bottomHeight();

    int top_left_len = Math.max(left_width, top_height);
    int top_right_len = Math.max(right_width, top_height);
    int bottom_right_len = Math.max(right_width, bottom_height);
    int bottom_left_len = Math.max(left_width, bottom_height);

    final int window_width = window_size.getXI();
    final int window_height = window_size.getYI();

    int top_width = window_width;
    int bottom_width = window_width;
    int left_height = window_height;
    int right_height = window_height;

    int bottom_x = 0;
    final int bottom_y = window_height - bottom_height;
    int left_y = 0;
    int right_y = 0;
    int top_x = 0;

    final int cap_length = top_height + titlebar_theme.height() - top_left_len;
    final int emboss_size = emboss.size();

    boolean top_left_caps = false;
    switch (margin_theme.topLeftStyle()) {
      case MARGIN_CORNER_NONE: {
        break;
      }
      case MARGIN_CORNER_L_PIECE: {
        top_left_caps = true;
        top_left_len += cap_length;
        left_y = top_left_len;
        left_height -= top_left_len;
        top_x = top_left_len;
        top_width -= top_left_len;
        break;
      }
      case MARGIN_CORNER_BOX: {
        break;
      }
    }

    boolean top_right_caps = false;
    switch (margin_theme.topRightStyle()) {
      case MARGIN_CORNER_NONE: {
        break;
      }
      case MARGIN_CORNER_L_PIECE: {
        top_right_caps = true;
        top_right_len += cap_length;
        right_height -= top_right_len;
        right_y += top_right_len;
        top_width -= top_right_len;
        break;
      }
      case MARGIN_CORNER_BOX: {
        break;
      }
    }

    boolean bottom_left_caps = false;
    switch (margin_theme.bottomLeftStyle()) {
      case MARGIN_CORNER_NONE: {
        break;
      }
      case MARGIN_CORNER_L_PIECE: {
        bottom_left_caps = true;
        bottom_left_len += cap_length;
        left_height -= bottom_left_len;
        bottom_x += bottom_left_len;
        bottom_width -= bottom_left_len;
        break;
      }
      case MARGIN_CORNER_BOX: {
        break;
      }
    }

    boolean bottom_right_caps = false;
    switch (margin_theme.bottomRightStyle()) {
      case MARGIN_CORNER_NONE: {
        break;
      }
      case MARGIN_CORNER_L_PIECE: {
        bottom_right_caps = true;
        bottom_right_len += cap_length;
        right_height -= bottom_right_len;
        bottom_width -= bottom_right_len;
        break;
      }
      case MARGIN_CORNER_BOX: {
        break;
      }
    }

    /**
     * Left margin.
     */

    this.embossed.rectangle(
      graphics,
      0,
      left_y,
      left_width,
      left_height,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill);

    /**
     * Right margin.
     */

    this.embossed.rectangle(
      graphics,
      window_width - right_width,
      right_y,
      right_width,
      right_height,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill);

    /**
     * Top margin.
     */

    this.embossed.rectangle(
      graphics,
      top_x,
      0,
      top_width,
      top_height,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill);

    /**
     * Bottom margin.
     */

    this.embossed.rectangle(
      graphics,
      bottom_x,
      bottom_y,
      bottom_width,
      bottom_height,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill);

    /**
     * Corners.
     */

    this.embossed.drawEmbossedL(
      graphics,
      SyEmbossed.LShape.L_SHAPE_NW,
      0,
      0,
      left_width,
      top_height,
      top_left_len,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill,
      top_left_caps);

    this.embossed.drawEmbossedL(
      graphics,
      SyEmbossed.LShape.L_SHAPE_NE,
      window_width - top_right_len,
      0,
      right_width,
      top_height,
      top_right_len,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill,
      top_right_caps);

    this.embossed.drawEmbossedL(
      graphics,
      SyEmbossed.LShape.L_SHAPE_SW,
      0,
      window_height - bottom_left_len,
      left_width,
      bottom_height,
      bottom_left_len,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill,
      bottom_left_caps);

    this.embossed.drawEmbossedL(
      graphics,
      SyEmbossed.LShape.L_SHAPE_SE,
      window_width - bottom_right_len,
      window_height - bottom_right_len,
      right_width,
      bottom_height,
      bottom_right_len,
      emboss_size,
      e_left,
      e_right,
      e_top,
      e_bottom,
      eo_fill,
      bottom_right_caps);
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
