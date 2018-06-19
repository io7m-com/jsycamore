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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.renderer.SyComponentRendererType;
import com.io7m.jsycamore.api.renderer.SyWindowRendererType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeEmboss;
import com.io7m.jsycamore.api.themes.SyThemeOutline;
import com.io7m.jsycamore.api.themes.SyThemeWindow;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.api.windows.SyWindowFrameType;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import net.jcip.annotations.NotThreadSafe;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Optional;

/**
 * A simple software renderer that renders a window to an image.
 */

@NotThreadSafe
public final class SyAWTWindowRenderer implements
  SyWindowRendererType<BufferedImage, BufferedImage>
{
  private final SyAWTEmbossed embossed;
  private final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> component_renderer;

  private SyAWTWindowRenderer(
    final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> in_component_renderer)
  {
    this.component_renderer =
      Objects.requireNonNull(in_component_renderer, "Component renderer");
    this.embossed = new SyAWTEmbossed();
  }

  /**
   * @param in_component_renderer A component renderer
   *
   * @return A new renderer
   */

  public static SyWindowRendererType<BufferedImage, BufferedImage> create(
    final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> in_component_renderer)
  {
    return new SyAWTWindowRenderer(in_component_renderer);
  }

  @Override
  public BufferedImage render(
    final BufferedImage input,
    final SyWindowType window)
  {
    Objects.requireNonNull(input, "Input image");
    Objects.requireNonNull(window, "Window");

    final SyAWTComponentRendererContext context =
      SyAWTComponentRendererContext.of(window.viewportAccumulator(), input);

    final Graphics2D graphics = input.createGraphics();
    try {
      graphics.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setRenderingHint(
        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      final PAreaI<SySpaceViewportType> window_box = window.box();
      graphics.setClip(0, 0, window_box.sizeX(), window_box.sizeY());
      this.renderFrame(graphics, window);
      this.component_renderer.render(context, window.contentPane());
      this.component_renderer.render(context, window.titleBar());

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
      if (window.isFocused()) {
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
    final SyTheme theme = window.theme();
    final SyThemeWindow window_theme = theme.windowTheme();
    final SyThemeWindowFrame frame_theme = window_theme.frame();

    final Optional<SyThemeEmboss> emboss_opt = frame_theme.embossInactive();
    if (emboss_opt.isPresent()) {
      final SyThemeEmboss emboss = emboss_opt.get();
      this.renderFrameEmbossedActual(
        graphics,
        window_theme.titleBar(),
        frame_theme,
        window,
        frame_theme.outline(),
        emboss,
        SyAWTDrawing.toColor(emboss.colorTop()),
        SyAWTDrawing.toColor(emboss.colorLeft()),
        SyAWTDrawing.toColor(emboss.colorRight()),
        SyAWTDrawing.toColor(emboss.colorBottom()),
        Optional.of(SyAWTDrawing.toColor(frame_theme.colorInactive())),
        false);
    } else {
      this.renderFrameUnembossedActual(
        graphics,
        frame_theme,
        window,
        frame_theme.outline(),
        SyAWTDrawing.toColor(frame_theme.colorInactive()),
        false);
    }
  }

  private void renderFrameActive(
    final Graphics2D graphics,
    final SyWindowReadableType window)
  {
    final SyTheme theme = window.theme();
    final SyThemeWindow window_theme = theme.windowTheme();
    final SyThemeWindowFrame frame_theme = window_theme.frame();
    final Optional<SyThemeEmboss> emboss_opt = frame_theme.embossActive();

    if (emboss_opt.isPresent()) {
      final SyThemeEmboss emboss = emboss_opt.get();
      this.renderFrameEmbossedActual(
        graphics,
        window_theme.titleBar(),
        frame_theme,
        window,
        frame_theme.outline(),
        emboss,
        SyAWTDrawing.toColor(emboss.colorTop()),
        SyAWTDrawing.toColor(emboss.colorLeft()),
        SyAWTDrawing.toColor(emboss.colorRight()),
        SyAWTDrawing.toColor(emboss.colorBottom()),
        Optional.of(SyAWTDrawing.toColor(frame_theme.colorActive())),
        true);
    } else {
      this.renderFrameUnembossedActual(
        graphics,
        frame_theme,
        window,
        frame_theme.outline(),
        SyAWTDrawing.toColor(frame_theme.colorActive()),
        true);
    }
  }

  private void renderFrameEmbossedActual(
    final Graphics2D graphics,
    final SyThemeWindowTitleBar titlebar_theme,
    final SyThemeWindowFrame frame_theme,
    final SyWindowReadableType window,
    final Optional<SyThemeOutline> outline_opt,
    final SyThemeEmboss emboss,
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
    final PAreaI<SySpaceParentRelativeType> frame_box = frame.box();

    final int frame_x;
    final int frame_y;
    final int frame_width;
    final int frame_height;

    if (outline_opt.isPresent()) {
      SyAWTDrawing.drawOutline(graphics, outline_opt.get(), frame_box, active);
      frame_x = frame_box.minimumX() + 1;
      frame_y = frame_box.minimumY() + 1;
      frame_width = frame_box.sizeX() - 2;
      frame_height = frame_box.sizeY() - 2;
    } else {
      frame_x = frame_box.minimumX();
      frame_y = frame_box.minimumY();
      frame_width = frame_box.sizeX();
      frame_height = frame_box.sizeY();
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
        Preconditions.checkPreconditionI(
          left_width,
          left_width > 0,
          i -> "L piece left_width must be > 0");
        Preconditions.checkPreconditionI(
          top_height,
          top_height > 0,
          i -> "L piece top_height must be > 0");

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
        Preconditions.checkPreconditionI(
          right_width,
          right_width > 0,
          i -> "L piece right_width must be > 0");
        Preconditions.checkPreconditionI(
          top_height,
          top_height > 0,
          i -> "L piece top_height must be > 0");

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
        Preconditions.checkPreconditionI(
          left_width,
          left_width > 0,
          i -> "L piece left_width must be > 0");
        Preconditions.checkPreconditionI(
          bottom_height,
          bottom_height > 0,
          i -> "L piece bottom_height must be > 0");

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
        Preconditions.checkPreconditionI(
          right_width,
          right_width > 0,
          i -> "L piece right_width must be > 0");
        Preconditions.checkPreconditionI(
          bottom_height,
          bottom_height > 0,
          i -> "L piece bottom_height must be > 0");

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
      final PAreaI<?> box =
        PAreasI.create(0, left_y, left_width, left_height);
      this.embossed.rectangle(
        graphics, box, emboss_size, e_left, e_right, e_top, e_bottom, eo_fill);
    }

    /**
     * Right frame.
     */

    if (right_width > 0) {
      final PAreaI<?> box =
        PAreasI.create(
          frame_width - right_width, right_y, right_width, right_height);
      this.embossed.rectangle(
        graphics, box, emboss_size, e_left, e_right, e_top, e_bottom, eo_fill);
    }

    /**
     * Top frame.
     */

    if (top_height > 0) {
      final PAreaI<?> box =
        PAreasI.create(top_x, 0, top_width, top_height);
      this.embossed.rectangle(
        graphics, box, emboss_size, e_left, e_right, e_top, e_bottom, eo_fill);
    }

    /**
     * Bottom frame.
     */

    if (bottom_height > 0) {
      final PAreaI<?> box =
        PAreasI.create(bottom_x, bottom_y, bottom_width, bottom_height);
      this.embossed.rectangle(
        graphics, box, emboss_size, e_left, e_right, e_top, e_bottom, eo_fill);
    }

    /**
     * Corners.
     */

    if (top_height > 0 && left_width > 0) {
      final int thickness_of_horizontal = top_height;
      final int thickness_of_vertical = left_width;
      this.embossed.drawEmbossedL(
        graphics,
        SyAWTEmbossed.LShape.L_SHAPE_NW,
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
        SyAWTEmbossed.LShape.L_SHAPE_NE,
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
        SyAWTEmbossed.LShape.L_SHAPE_SW,
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
        SyAWTEmbossed.LShape.L_SHAPE_SE,
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
    final SyThemeWindowFrame frame_theme,
    final SyWindowReadableType window,
    final Optional<SyThemeOutline> outline_opt,
    final Paint fill,
    final boolean active)
  {
    final int left_width = frame_theme.leftWidth();
    final int right_width = frame_theme.rightWidth();
    final int top_height = frame_theme.topHeight();
    final int bottom_height = frame_theme.bottomHeight();

    final SyWindowFrameType frame = window.frame();
    final PAreaI<SySpaceParentRelativeType> frame_box = frame.box();

    if (outline_opt.isPresent()) {
      SyAWTDrawing.drawOutline(
        graphics, outline_opt.get(), frame_box, active);
    }

    final int frame_x = frame_box.minimumX();
    final int frame_y = frame_box.minimumY();
    final int frame_width = frame_box.sizeX();
    final int frame_height = frame_box.sizeY();

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
