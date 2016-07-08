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
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jsycamore.core.SyWindowTitlebarType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.components.SyComponentReadableType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.themes.SyThemeEmbossType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameType;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.core.themes.SyThemeWindowType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import net.jcip.annotations.NotThreadSafe;
import org.valid4j.Assertive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * A simple software renderer that renders a window to an image.
 */

@NotThreadSafe
public final class SyWindowRendererAWT implements
  SyWindowRendererType<BufferedImage, BufferedImage>
{
  private final SyEmbossed embossed;
  private final SyTextMeasurementType measurement;

  private SyWindowRendererAWT(
    final SyTextMeasurementType in_measurement)
  {
    this.measurement = NullCheck.notNull(in_measurement);
    this.embossed = new SyEmbossed();
  }

  /**
   * @param in_measurement A text measurement interface
   *
   * @return A new renderer
   */

  public static SyWindowRendererType<BufferedImage, BufferedImage> create(
    final SyTextMeasurementType in_measurement)
  {
    return new SyWindowRendererAWT(in_measurement);
  }

  private static Color toColor(
    final VectorReadable3FType color)
  {
    final float r = Math.min(1.0f, Math.max(0.0f, color.getXF()));
    final float g = Math.min(1.0f, Math.max(0.0f, color.getYF()));
    final float b = Math.min(1.0f, Math.max(0.0f, color.getZF()));
    return new Color(r, g, b);
  }

  @Override
  public BufferedImage render(
    final BufferedImage input,
    final SyWindowType window)
  {
    NullCheck.notNull(input);
    NullCheck.notNull(window);

    final VectorReadable2IType window_size = window.bounds();
    Assertive.require(input.getWidth() >= window_size.getXI());
    Assertive.require(input.getHeight() >= window_size.getYI());

    final Graphics2D graphics = input.createGraphics();
    graphics.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setRenderingHint(
      RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    graphics.setClip(0, 0, window_size.getXI(), window_size.getYI());
    this.renderFrame(graphics, window);
    this.renderTitlebar(graphics, window);
    this.renderContent(graphics, window);
    this.renderOutline(graphics, window);
    return input;
  }

  private void renderContent(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();

    try {
      final SyComponentType content =
        window.contentPane();
      final PVectorReadable2IType<SySpaceParentRelativeType> position =
        content.position();
      final VectorReadable2IType size =
        content.size();

      graphics.clipRect(
        position.getXI(),
        position.getYI(),
        size.getXI(),
        size.getYI());
      graphics.setPaint(Color.MAGENTA);
      graphics.fillRect(
        position.getXI(),
        position.getYI(),
        size.getXI(),
        size.getYI());

    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
    }
  }

  private void renderOutline(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();

    try {
      if (window.focused()) {
        this.renderOutlineActive(graphics, window);
      } else {
        this.renderOutlineInactive(graphics, window);
      }
    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
    }
  }

  private void renderOutlineInactive(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final Optional<SyThemeOutlineType> outline_opt = window_theme.outline();
    if (outline_opt.isPresent()) {
      this.renderOutlineActual(
        graphics, window, outline_opt.get().colorInactive());
    }
  }

  private void renderOutlineActual(
    final Graphics2D graphics,
    final SyWindowReadableType window,
    final VectorReadable3FType color)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowTitleBarType title_theme = window_theme.titleBar();
    final VectorReadable2IType size = window.bounds();

    graphics.setPaint(SyWindowRendererAWT.toColor(color));

    final int max_y = size.getYI() - 1;
    final int max_x = size.getXI() - 1;
    switch (title_theme.verticalPlacement()) {
      case PLACEMENT_TOP_INSIDE_FRAME:
      case PLACEMENT_TOP_OVERLAP_FRAME: {
        graphics.drawRect(0, 0, max_x, max_y);
        break;
      }
      case PLACEMENT_TOP_ABOVE_FRAME: {
        final SyComponentReadableType titlebar = window.titlebar();
        final VectorReadable2IType title_size =
          titlebar.size();
        final PVectorReadable2IType<SySpaceParentRelativeType> title_pos =
          titlebar.position();

        final int title_min_x = title_pos.getXI() - 1;
        final int title_max_x = title_min_x + title_size.getXI() + 1;
        final int title_height = title_size.getYI();
        graphics.drawLine(0, title_height, title_min_x, title_height);
        graphics.drawLine(title_min_x, title_height, title_min_x, 0);
        graphics.drawLine(title_min_x, 0, title_max_x, 0);
        graphics.drawLine(title_max_x, 0, title_max_x, title_height);
        graphics.drawLine(title_max_x, title_height, max_x, title_height);

        graphics.drawLine(max_x, title_height, max_x, max_y);
        graphics.drawLine(0, max_y, max_x, max_y);
        graphics.drawLine(0, title_height, 0, max_y);
        break;
      }
    }
  }

  private void renderOutlineActive(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final Optional<SyThemeOutlineType> outline_opt = window_theme.outline();
    if (outline_opt.isPresent()) {
      this.renderOutlineActual(
        graphics, window, outline_opt.get().colorActive());
    }
  }

  private void renderTitlebar(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();

    try {
      if (window.focused()) {
        this.renderTitlebarActive(graphics, window);
      } else {
        this.renderTitlebarInactive(graphics, window);
      }
    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
    }
  }

  private void renderTitlebarInactive(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowTitleBarType titlebar = window_theme.titleBar();
    this.renderTitleBarActual(
      graphics,
      window,
      titlebar,
      titlebar.colorInactive(),
      titlebar.embossInactive(),
      titlebar.textColorInactive());
  }

  private void renderTitlebarActive(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowTitleBarType titlebar = window_theme.titleBar();
    this.renderTitleBarActual(
      graphics,
      window,
      titlebar,
      titlebar.colorActive(),
      titlebar.embossActive(),
      titlebar.textColorActive());
  }

  private void renderTitleBarActual(
    final Graphics2D graphics,
    final SyWindowReadableType window,
    final SyThemeWindowTitleBarType titlebar_theme,
    final VectorReadable3FType titlebar_color,
    final Optional<SyThemeEmbossType> emboss_opt,
    final VectorReadable3FType text_color)
  {
    final SyWindowTitlebarType titlebar = window.titlebar();
    final PVectorReadable2IType<SySpaceParentRelativeType> bar_pos =
      titlebar.position();
    final VectorReadable2IType bar_size =
      titlebar.size();

    final int x = bar_pos.getXI();
    final int y = bar_pos.getYI();
    final int w = bar_size.getXI();
    final int h = bar_size.getYI();
    graphics.clipRect(x, y, w, h);
    graphics.translate(x, y);

    /**
     * Render actual bar.
     */

    final Color fill = SyWindowRendererAWT.toColor(titlebar_color);
    final Optional<Paint> fill_opt = Optional.of(fill);

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
      final String text_font = titlebar_theme.textFont();
      final String text = titlebar.text();

      final int text_width = this.measurement.measureText(text_font, text);
      final int space_width = this.measurement.measureText(text_font, " ");

      int text_x = 0;
      final int text_y = (titlebar_theme.height() / 4) * 3;
      switch (titlebar_theme.textAlignment()) {
        case ALIGN_LEFT: {
          text_x = space_width;
          break;
        }
        case ALIGN_RIGHT: {
          text_x = w - (text_width + space_width);
          break;
        }
        case ALIGN_CENTER: {
          text_x = (w / 2) - (text_width / 2);
          break;
        }
      }

      final Color text_paint = SyWindowRendererAWT.toColor(text_color);
      graphics.setFont(this.measurement.decodeFont(text_font));
      graphics.setPaint(text_paint);
      graphics.drawString(text, text_x, text_y);
    }
  }

  private void renderFrame(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();

    try {
      if (window.focused()) {
        this.renderFrameActive(graphics, window);
      } else {
        this.renderFrameInactive(graphics, window);
      }
    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
    }
  }

  private void renderFrameInactive(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowFrameType frame_theme = window_theme.frame();

    final Optional<SyThemeEmbossType> emboss_opt = frame_theme.embossInactive();
    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      this.renderFrameEmbossedActual(
        graphics,
        window_theme.titleBar(),
        frame_theme,
        window,
        emboss,
        SyWindowRendererAWT.toColor(emboss.colorTop()),
        SyWindowRendererAWT.toColor(emboss.colorLeft()),
        SyWindowRendererAWT.toColor(emboss.colorRight()),
        SyWindowRendererAWT.toColor(emboss.colorBottom()),
        Optional.of(SyWindowRendererAWT.toColor(frame_theme.colorInactive())));
    } else {
      this.renderFrameUnembossedActual(
        graphics,
        window_theme.titleBar(),
        frame_theme,
        window,
        SyWindowRendererAWT.toColor(frame_theme.colorInactive()));
    }
  }

  private void renderFrameActive(
    final Graphics2D graphics,
    final SyWindowType window)
  {
    final SyThemeType theme = window.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowFrameType frame_theme = window_theme.frame();

    final Optional<SyThemeEmbossType> emboss_opt = frame_theme.embossActive();
    if (emboss_opt.isPresent()) {
      final SyThemeEmbossType emboss = emboss_opt.get();
      this.renderFrameEmbossedActual(
        graphics,
        window_theme.titleBar(),
        frame_theme,
        window,
        emboss,
        SyWindowRendererAWT.toColor(emboss.colorTop()),
        SyWindowRendererAWT.toColor(emboss.colorLeft()),
        SyWindowRendererAWT.toColor(emboss.colorRight()),
        SyWindowRendererAWT.toColor(emboss.colorBottom()),
        Optional.of(SyWindowRendererAWT.toColor(frame_theme.colorActive())));
    } else {
      this.renderFrameUnembossedActual(
        graphics,
        window_theme.titleBar(),
        frame_theme,
        window,
        SyWindowRendererAWT.toColor(frame_theme.colorActive()));
    }
  }

  private void renderFrameEmbossedActual(
    final Graphics2D graphics,
    final SyThemeWindowTitleBarType titlebar_theme,
    final SyThemeWindowFrameType frame_theme,
    final SyWindowReadableType window,
    final SyThemeEmbossType emboss,
    final Paint e_top,
    final Paint e_left,
    final Paint e_right,
    final Paint e_bottom,
    final Optional<Paint> eo_fill)
  {
    final int left_width = frame_theme.leftWidth();
    final int right_width = frame_theme.rightWidth();
    final int top_height = frame_theme.topHeight();
    final int bottom_height = frame_theme.bottomHeight();

    final PVectorReadable2IType<SySpaceWindowRelativeType> frame_position =
      window.framePosition();
    final PVectorReadable2IType<SySpaceWindowRelativeType> frame_size =
      window.frameBounds();

    final int frame_x = frame_position.getXI();
    final int frame_y = frame_position.getYI();
    final int frame_width = frame_size.getXI();
    final int frame_height = frame_size.getYI();
    graphics.clipRect(frame_x, frame_y, frame_width, frame_height);
    graphics.translate(frame_x, frame_y);

    int top_left_len = Math.max(left_width, top_height);
    int top_right_len = Math.max(right_width, top_height);
    int bottom_right_len = Math.max(right_width, bottom_height);
    int bottom_left_len = Math.max(left_width, bottom_height);

    int top_width = frame_width;
    int bottom_width = frame_width;
    int left_height = frame_height;
    int right_height = frame_height;

    int bottom_x = 0;
    final int bottom_y = frame_height - bottom_height;
    int left_y = 0;
    int right_y = 0;
    int top_x = 0;

    final int cap_length = top_height + titlebar_theme.height() - top_left_len;
    final int emboss_size = emboss.size();

    boolean top_left_caps = false;
    switch (frame_theme.topLeftStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Assertive.require(left_width > 0);
        Assertive.require(top_height > 0);

        top_left_caps = true;
        top_left_len += cap_length;
        left_y = top_left_len;
        left_height -= top_left_len;
        top_x = top_left_len;
        top_width -= top_left_len;
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }

    boolean top_right_caps = false;
    switch (frame_theme.topRightStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Assertive.require(right_width > 0);
        Assertive.require(top_height > 0);

        top_right_caps = true;
        top_right_len += cap_length;
        right_height -= top_right_len;
        right_y += top_right_len;
        top_width -= top_right_len;
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }

    boolean bottom_left_caps = false;
    switch (frame_theme.bottomLeftStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Assertive.require(left_width > 0);
        Assertive.require(bottom_height > 0);

        bottom_left_caps = true;
        bottom_left_len += cap_length;
        left_height -= bottom_left_len;
        bottom_x += bottom_left_len;
        bottom_width -= bottom_left_len;
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }

    boolean bottom_right_caps = false;
    switch (frame_theme.bottomRightStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Assertive.require(right_width > 0);
        Assertive.require(bottom_height > 0);

        bottom_right_caps = true;
        bottom_right_len += cap_length;
        right_height -= bottom_right_len;
        bottom_width -= bottom_right_len;
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }

    /**
     * Left frame.
     */

    if (left_width > 0) {
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
    }

    /**
     * Right frame.
     */

    if (right_width > 0) {
      this.embossed.rectangle(
        graphics,
        frame_width - right_width,
        right_y,
        right_width,
        right_height,
        emboss_size,
        e_left,
        e_right,
        e_top,
        e_bottom,
        eo_fill);
    }

    /**
     * Top frame.
     */

    if (top_height > 0) {
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
    }

    /**
     * Bottom frame.
     */

    if (bottom_height > 0) {
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
    }

    /**
     * Corners.
     */

    if (top_height > 0 && left_width > 0) {
      final int thickness_of_horizontal = top_height;
      final int thickness_of_vertical = left_width;
      this.embossed.drawEmbossedL(
        graphics,
        SyEmbossed.LShape.L_SHAPE_NW,
        0,
        0,
        thickness_of_horizontal,
        thickness_of_vertical,
        top_left_len,
        emboss_size,
        e_left,
        e_right,
        e_top,
        e_bottom,
        eo_fill,
        top_left_caps);
    }

    if (top_height > 0 && right_width > 0) {
      final int thickness_of_horizontal = top_height;
      final int thickness_of_vertical = right_width;
      this.embossed.drawEmbossedL(
        graphics,
        SyEmbossed.LShape.L_SHAPE_NE,
        frame_width - top_right_len,
        0,
        thickness_of_horizontal,
        thickness_of_vertical,
        top_right_len,
        emboss_size,
        e_left,
        e_right,
        e_top,
        e_bottom,
        eo_fill,
        top_right_caps);
    }

    if (bottom_height > 0 && left_width > 0) {
      final int thickness_of_horizontal = bottom_height;
      final int thickness_of_vertical = left_width;
      this.embossed.drawEmbossedL(
        graphics,
        SyEmbossed.LShape.L_SHAPE_SW,
        0,
        frame_height - bottom_left_len,
        thickness_of_horizontal,
        thickness_of_vertical,
        bottom_left_len,
        emboss_size,
        e_left,
        e_right,
        e_top,
        e_bottom,
        eo_fill,
        bottom_left_caps);
    }

    if (bottom_height > 0 && right_width > 0) {
      final int thickness_of_horizontal = bottom_height;
      final int thickness_of_vertical = right_width;
      this.embossed.drawEmbossedL(
        graphics,
        SyEmbossed.LShape.L_SHAPE_SE,
        frame_width - bottom_right_len,
        frame_height - bottom_right_len,
        thickness_of_horizontal,
        thickness_of_vertical,
        bottom_right_len,
        emboss_size,
        e_left,
        e_right,
        e_top,
        e_bottom,
        eo_fill,
        bottom_right_caps);
    }
  }

  private void renderFrameUnembossedActual(
    final Graphics2D graphics,
    final SyThemeWindowTitleBarType titlebar_theme,
    final SyThemeWindowFrameType frame_theme,
    final SyWindowReadableType window,
    final Paint fill)
  {
    final int left_width = frame_theme.leftWidth();
    final int right_width = frame_theme.rightWidth();
    final int top_height = frame_theme.topHeight();
    final int bottom_height = frame_theme.bottomHeight();

    final PVectorReadable2IType<SySpaceWindowRelativeType> frame_position =
      window.framePosition();
    final PVectorReadable2IType<SySpaceWindowRelativeType> frame_size =
      window.frameBounds();

    final int frame_x = frame_position.getXI();
    final int frame_y = frame_position.getYI();
    final int frame_width = frame_size.getXI();
    final int frame_height = frame_size.getYI();
    graphics.clipRect(frame_x, frame_y, frame_width, frame_height);
    graphics.translate(frame_x, frame_y);

    final int top_width = frame_width;
    final int bottom_width = frame_width;
    final int left_height = frame_height;
    final int right_height = frame_height;

    final int bottom_x = 0;
    final int bottom_y = frame_height - bottom_height;
    final int left_y = 0;
    final int right_y = 0;
    final int top_x = 0;

    /**
     * Left frame.
     */

    if (left_width > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(0, left_y, left_width, left_height);
    }

    /**
     * Right frame.
     */

    if (right_width > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(
        frame_width - right_width, right_y, right_width, right_height);
    }

    /**
     * Top frame.
     */

    if (top_height > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(top_x, 0, top_width, top_height);
    }

    /**
     * Bottom frame.
     */

    if (bottom_height > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(bottom_x, bottom_y, bottom_width, bottom_height);
    }

    /**
     * Corners.
     */

    if (top_height > 0 && left_width > 0) {
      final int thickness_of_horizontal = top_height;
      final int thickness_of_vertical = left_width;
    }

    if (top_height > 0 && right_width > 0) {
      final int thickness_of_horizontal = top_height;
      final int thickness_of_vertical = right_width;
    }

    if (bottom_height > 0 && left_width > 0) {
      final int thickness_of_horizontal = bottom_height;
      final int thickness_of_vertical = left_width;
    }

    if (bottom_height > 0 && right_width > 0) {
      final int thickness_of_horizontal = bottom_height;
      final int thickness_of_vertical = right_width;
    }
  }
}
