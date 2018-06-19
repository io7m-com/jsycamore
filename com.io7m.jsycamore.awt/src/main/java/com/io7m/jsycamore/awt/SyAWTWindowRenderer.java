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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.awt.SyAWTEmbossed.LShape.L_SHAPE_NE;
import static com.io7m.jsycamore.awt.SyAWTEmbossed.LShape.L_SHAPE_NW;
import static com.io7m.jsycamore.awt.SyAWTEmbossed.LShape.L_SHAPE_SE;
import static com.io7m.jsycamore.awt.SyAWTEmbossed.LShape.L_SHAPE_SW;

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

  /**
   * Render all of the visible embossed corners.
   */

  private static void renderFrameEmbossedActualCorners(
    final Graphics2D graphics,
    final Paint e_top,
    final Paint e_left,
    final Paint e_right,
    final Paint e_bottom,
    final Optional<Paint> eo_fill,
    final SyAWTWindowRendererEmbossedFrameParameters parameters,
    final SyAWTEmbossed embossed)
  {
    if (hasVisibleTopLeftCorner(parameters)) {
      embossed.drawEmbossedL(
        graphics,
        SyAWTEmbossedCornerL.builder()
          .setShape(L_SHAPE_NW)
          .setX(0)
          .setY(0)
          .setThicknessOfHorizontal(parameters.topHeight())
          .setThicknessOfVertical(parameters.leftWidth())
          .setArmLength(parameters.topLeftArmLength())
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintBottom(e_bottom)
          .setPaintTop(e_top)
          .setPaintFill(eo_fill)
          .setCaps(parameters.topLeftCaps())
          .build());
    }

    final PAreaI<SySpaceParentRelativeType> frame_box = parameters.frameBoxAdjustedForOutline();

    if (hasVisibleTopRightCorner(parameters)) {
      embossed.drawEmbossedL(
        graphics,
        SyAWTEmbossedCornerL.builder()
          .setShape(L_SHAPE_NE)
          .setX(frame_box.sizeX() - parameters.topRightArmLength())
          .setY(0)
          .setThicknessOfHorizontal(parameters.topHeight())
          .setThicknessOfVertical(parameters.rightWidth())
          .setArmLength(parameters.topRightArmLength())
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintBottom(e_bottom)
          .setPaintTop(e_top)
          .setPaintFill(eo_fill)
          .setCaps(parameters.topRightCaps())
          .build());
    }

    if (hasVisibleBottomLeftCorner(parameters)) {
      embossed.drawEmbossedL(
        graphics,
        SyAWTEmbossedCornerL.builder()
          .setShape(L_SHAPE_SW)
          .setX(0)
          .setY(frame_box.sizeY() - parameters.bottomLeftArmLength())
          .setThicknessOfHorizontal(parameters.bottomHeight())
          .setThicknessOfVertical(parameters.leftWidth())
          .setArmLength(parameters.bottomLeftArmLength())
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintBottom(e_bottom)
          .setPaintTop(e_top)
          .setPaintFill(eo_fill)
          .setCaps(parameters.bottomLeftCaps())
          .build());
    }

    if (hasVisibleBottomRightCorner(parameters)) {
      embossed.drawEmbossedL(
        graphics,
        SyAWTEmbossedCornerL.builder()
          .setShape(L_SHAPE_SE)
          .setX(frame_box.sizeX() - parameters.bottomRightArmLength())
          .setY(frame_box.sizeY() - parameters.bottomRightArmLength())
          .setThicknessOfHorizontal(parameters.bottomHeight())
          .setThicknessOfVertical(parameters.rightWidth())
          .setArmLength(parameters.bottomRightArmLength())
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintBottom(e_bottom)
          .setPaintTop(e_top)
          .setPaintFill(eo_fill)
          .setCaps(parameters.bottomRightCaps())
          .build());
    }
  }

  private static boolean hasVisibleBottomRightCorner(
    final SyAWTWindowRendererEmbossedFrameParameters parameters)
  {
    return parameters.bottomHeight() > 0 && parameters.rightWidth() > 0;
  }

  private static boolean hasVisibleBottomLeftCorner(
    final SyAWTWindowRendererEmbossedFrameParameters parameters)
  {
    return parameters.bottomHeight() > 0 && parameters.leftWidth() > 0;
  }

  private static boolean hasVisibleTopRightCorner(
    final SyAWTWindowRendererEmbossedFrameParameters parameters)
  {
    return parameters.topHeight() > 0 && parameters.rightWidth() > 0;
  }

  private static boolean hasVisibleTopLeftCorner(
    final SyAWTWindowRendererEmbossedFrameParameters parameters)
  {
    return parameters.topHeight() > 0 && parameters.leftWidth() > 0;
  }

  /**
   * Render the top, bottom, left, and right frames that make up an embossed window.
   */

  private static void renderFrameEmbossedActualFrames(
    final Graphics2D graphics,
    final Paint e_top,
    final Paint e_left,
    final Paint e_right,
    final Paint e_bottom,
    final Optional<Paint> eo_fill,
    final SyAWTWindowRendererEmbossedFrameParameters parameters,
    final SyAWTEmbossed embossed)
  {
    /*
     * Left frame.
     */

    if (parameters.leftWidth() > 0) {
      embossed.rectangle(
        graphics,
        SyAWTEmbossedRectangle.builder()
          .setBox(PAreasI.create(
            0,
            parameters.leftY(),
            parameters.leftWidth(),
            parameters.leftHeight()))
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintTop(e_top)
          .setPaintBottom(e_bottom)
          .setPaintFill(eo_fill)
          .build());
    }

    /*
     * Right frame.
     */

    final PAreaI<SySpaceParentRelativeType> frame_box = parameters.frameBoxAdjustedForOutline();
    if (parameters.rightWidth() > 0) {
      embossed.rectangle(
        graphics,
        SyAWTEmbossedRectangle.builder()
          .setBox(PAreasI.create(
            frame_box.sizeX() - parameters.rightWidth(),
            parameters.rightY(),
            parameters.rightWidth(),
            parameters.rightHeight()))
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintTop(e_top)
          .setPaintBottom(e_bottom)
          .setPaintFill(eo_fill)
          .build());
    }

    /*
     * Top frame.
     */

    if (parameters.topHeight() > 0) {
      embossed.rectangle(
        graphics,
        SyAWTEmbossedRectangle.builder()
          .setBox(PAreasI.create(
            parameters.topX(),
            0,
            parameters.topWidth(),
            parameters.topHeight()))
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintTop(e_top)
          .setPaintBottom(e_bottom)
          .setPaintFill(eo_fill)
          .build());
    }

    /*
     * Bottom frame.
     */

    if (parameters.bottomHeight() > 0) {
      embossed.rectangle(
        graphics,
        SyAWTEmbossedRectangle.builder()
          .setBox(PAreasI.create(
            parameters.bottomX(),
            parameters.bottomY(),
            parameters.bottomWidth(),
            parameters.bottomHeight()))
          .setEmbossSize(parameters.embossSize())
          .setPaintLeft(e_left)
          .setPaintRight(e_right)
          .setPaintTop(e_top)
          .setPaintBottom(e_bottom)
          .setPaintFill(eo_fill)
          .build());
    }
  }

  /**
   * Render an unembossed frame.
   */

  private static void renderFrameUnembossedActual(
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

    outline_opt.ifPresent(
      theme_outline -> SyAWTDrawing.drawOutline(graphics, theme_outline, frame_box, active));

    final int frame_x = frame_box.minimumX();
    final int frame_y = frame_box.minimumY();
    final int frame_width = frame_box.sizeX();
    final int frame_height = frame_box.sizeY();

    graphics.clipRect(frame_x, frame_y, frame_width, frame_height);
    graphics.translate(frame_x, frame_y);

    /*
     * Left frame.
     */

    if (left_width > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(0, 0, left_width, frame_height);
    }

    /*
     * Right frame.
     */

    if (right_width > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(frame_width - right_width, 0, right_width, frame_height);
    }

    /*
     * Top frame.
     */

    if (top_height > 0) {
      graphics.setPaint(fill);
      graphics.fillRect(0, 0, frame_width, top_height);
    }

    /*
     * Bottom frame.
     */

    if (bottom_height > 0) {
      graphics.setPaint(fill);
      final int bottom_y = frame_height - bottom_height;
      graphics.fillRect(0, bottom_y, frame_width, bottom_height);
    }
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
        Optional.of(SyAWTDrawing.toColor(frame_theme.colorInactive())),
        false);
    } else {
      renderFrameUnembossedActual(
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
        Optional.of(SyAWTDrawing.toColor(frame_theme.colorActive())),
        true);
    } else {
      renderFrameUnembossedActual(
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
    final Optional<Paint> eo_fill,
    final boolean active)
  {
    final PAreaI<SySpaceParentRelativeType> frame_box = window.frame().box();

    final SyAWTWindowRendererEmbossedFrameParameters parameters =
      SyAWTWindowFrameEmbossing.renderFrameEmbossedActualFrameCalculateParameters(
        titlebar_theme,
        frame_theme,
        emboss,
        outline_opt,
        frame_box);

    outline_opt.ifPresent(
      outline -> SyAWTDrawing.drawOutline(graphics, outline, frame_box, active));

    final PAreaI<SySpaceParentRelativeType> frame_box_adjusted =
      parameters.frameBoxAdjustedForOutline();

    graphics.clipRect(
      frame_box_adjusted.minimumX(),
      frame_box_adjusted.minimumY(),
      frame_box_adjusted.sizeX(),
      frame_box_adjusted.sizeY());

    graphics.translate(
      frame_box_adjusted.minimumX(),
      frame_box_adjusted.minimumY());

    final Color e_top = SyAWTDrawing.toColor(emboss.colorTop());
    final Color e_left = SyAWTDrawing.toColor(emboss.colorLeft());
    final Color e_right = SyAWTDrawing.toColor(emboss.colorRight());
    final Color e_bottom = SyAWTDrawing.toColor(emboss.colorBottom());

    renderFrameEmbossedActualFrames(
      graphics,
      e_top,
      e_left,
      e_right,
      e_bottom,
      eo_fill,
      parameters,
      this.embossed);

    renderFrameEmbossedActualCorners(
      graphics,
      e_top,
      e_left,
      e_right,
      e_bottom,
      eo_fill,
      parameters,
      this.embossed);
  }

}
