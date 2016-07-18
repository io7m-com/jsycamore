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
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.SyWindowFrameType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.themes.SyThemeEmbossType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameType;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.core.themes.SyThemeWindowType;
import net.jcip.annotations.NotThreadSafe;
import org.valid4j.Assertive;

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
  private final SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage> component_renderer;

  private SyWindowRendererAWT(
    final SyTextMeasurementType in_measurement,
    final SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage> in_component_renderer)
  {
    this.measurement = NullCheck.notNull(in_measurement);
    this.component_renderer = NullCheck.notNull(in_component_renderer);
    this.embossed = new SyEmbossed();
  }

  /**
   * @param in_measurement        A text measurement interface
   * @param in_component_renderer A component renderer
   *
   * @return A new renderer
   */

  public static SyWindowRendererType<BufferedImage, BufferedImage> create(
    final SyTextMeasurementType in_measurement,
    final SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage> in_component_renderer)
  {
    return new SyWindowRendererAWT(in_measurement, in_component_renderer);
  }

  @Override
  public BufferedImage render(
    final BufferedImage input,
    final SyWindowType window)
  {
    NullCheck.notNull(input);
    NullCheck.notNull(window);

    final SyComponentRendererAWTContext context =
      SyComponentRendererAWTContext.of(window.viewportAccumulator(), input);

    final Graphics2D graphics = input.createGraphics();
    try {
      graphics.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setRenderingHint(
        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      final SyBoxType<SySpaceViewportType> window_box = window.box();
      graphics.setClip(0, 0, window_box.width(), window_box.height());
      this.renderFrame(graphics, window);
      this.component_renderer.render(context, window.contentPane());
      this.component_renderer.render(context, window.titlebar());

      return input;
    } finally {
      graphics.dispose();
    }
  }

  private void renderFrame(
    final Graphics2D graphics,
    final SyWindowReadableType window)
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
    final SyWindowReadableType window)
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
        frame_theme.outline(),
        emboss,
        SyDrawing.toColor(emboss.colorTop()),
        SyDrawing.toColor(emboss.colorLeft()),
        SyDrawing.toColor(emboss.colorRight()),
        SyDrawing.toColor(emboss.colorBottom()),
        Optional.of(SyDrawing.toColor(frame_theme.colorInactive())),
        false);
    } else {
      this.renderFrameUnembossedActual(
        graphics,
        frame_theme,
        window,
        frame_theme.outline(),
        SyDrawing.toColor(frame_theme.colorInactive()),
        false);
    }
  }

  private void renderFrameActive(
    final Graphics2D graphics,
    final SyWindowReadableType window)
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
        frame_theme.outline(),
        emboss,
        SyDrawing.toColor(emboss.colorTop()),
        SyDrawing.toColor(emboss.colorLeft()),
        SyDrawing.toColor(emboss.colorRight()),
        SyDrawing.toColor(emboss.colorBottom()),
        Optional.of(SyDrawing.toColor(frame_theme.colorActive())),
        true);
    } else {
      this.renderFrameUnembossedActual(
        graphics,
        frame_theme,
        window,
        frame_theme.outline(),
        SyDrawing.toColor(frame_theme.colorActive()),
        true);
    }
  }

  private void renderFrameEmbossedActual(
    final Graphics2D graphics,
    final SyThemeWindowTitleBarType titlebar_theme,
    final SyThemeWindowFrameType frame_theme,
    final SyWindowReadableType window,
    final Optional<SyThemeOutlineType> outline_opt,
    final SyThemeEmbossType emboss,
    final Paint e_top,
    final Paint e_left,
    final Paint e_right,
    final Paint e_bottom,
    final Optional<Paint> eo_fill,
    final boolean active)
  {
    final int left_width = frame_theme.leftWidth();
    final int right_width = frame_theme.rightWidth();
    final int top_height = frame_theme.topHeight();
    final int bottom_height = frame_theme.bottomHeight();

    final SyWindowFrameType frame = window.frame();
    final SyBoxType<SySpaceParentRelativeType> frame_box = frame.box();

    final int frame_x;
    final int frame_y;
    final int frame_width;
    final int frame_height;

    if (outline_opt.isPresent()) {
      SyDrawing.drawOutline(graphics, outline_opt.get(), frame_box, active);
      frame_x = frame_box.minimumX() + 1;
      frame_y = frame_box.minimumY() + 1;
      frame_width = frame_box.width() - 2;
      frame_height = frame_box.height() - 2;
    } else {
      frame_x = frame_box.minimumX();
      frame_y = frame_box.minimumY();
      frame_width = frame_box.width();
      frame_height = frame_box.height();
    }

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
        Assertive.require(left_width > 0, "L piece left_width must be > 0");
        Assertive.require(top_height > 0, "L piece top_height must be > 0");

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
        Assertive.require(right_width > 0, "L piece right_width must be > 0");
        Assertive.require(top_height > 0, "L piece top_height must be > 0");

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
        Assertive.require(
          left_width > 0, "L piece left_width must be > 0");
        Assertive.require(
          bottom_height > 0, "L piece bottom_height must be > 0");

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
        Assertive.require(
          right_width > 0, "L piece right_width must be > 0");
        Assertive.require(
          bottom_height > 0, "L piece bottom_height must be > 0");

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
    final SyThemeWindowFrameType frame_theme,
    final SyWindowReadableType window,
    final Optional<SyThemeOutlineType> outline_opt,
    final Paint fill,
    final boolean active)
  {
    final int left_width = frame_theme.leftWidth();
    final int right_width = frame_theme.rightWidth();
    final int top_height = frame_theme.topHeight();
    final int bottom_height = frame_theme.bottomHeight();

    final SyWindowFrameType frame = window.frame();
    final SyBoxType<SySpaceParentRelativeType> frame_box = frame.box();

    if (outline_opt.isPresent()) {
      SyDrawing.drawOutline(
        graphics, outline_opt.get(), frame_box, active);
    }

    final int frame_x = frame_box.minimumX();
    final int frame_y = frame_box.minimumY();
    final int frame_width = frame_box.width();
    final int frame_height = frame_box.height();

    graphics.clipRect(frame_x, frame_y, frame_width, frame_height);
    graphics.translate(frame_x, frame_y);

    final int bottom_y = frame_height - bottom_height;

    /**
     * Left frame.
     */

    if (left_width > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(0, 0, left_width, frame_height);
    }

    /**
     * Right frame.
     */

    if (right_width > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(
        frame_width - right_width, 0, right_width, frame_height);
    }

    /**
     * Top frame.
     */

    if (top_height > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(0, 0, frame_width, top_height);
    }

    /**
     * Bottom frame.
     */

    if (bottom_height > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(0, bottom_y, frame_width, bottom_height);
    }
  }
}
