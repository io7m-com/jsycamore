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
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeEmboss;
import com.io7m.jsycamore.api.themes.SyThemeOutline;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBar;

import java.util.Objects;
import java.util.Optional;

/**
 * Functions for embossing window frames.
 */

public final class SyAWTWindowFrameEmbossing
{
  private SyAWTWindowFrameEmbossing()
  {

  }

  /**
   * Offset and shrink a box based on the presence of an outline. If an outline is not present, the
   * original box is returned.
   */

  private static PAreaI<SySpaceParentRelativeType> adjustFrameBoxForOutline(
    final PAreaI<SySpaceParentRelativeType> frame_box,
    final Optional<SyThemeOutline> outline_opt)
  {
    return outline_opt.map(
      outline -> PAreasI.<SySpaceParentRelativeType>create(
        frame_box.minimumX() + 1,
        frame_box.minimumY() + 1,
        frame_box.sizeX() - 2,
        frame_box.sizeY() - 2))
      .orElse(frame_box);
  }

  /**
   * Calculate all of the parameters needed to render embossed frames.
   *
   * @param titlebar_theme The titlebar theme
   * @param frame_theme    The window frame theme
   * @param emboss         The embossing settings
   * @param outline_opt    The outline for the window, if any
   * @param frame_box      The box for the window
   *
   * @return The parameters needed to render embossed frames.
   */

  // The cyclomatic complexity of this method is too high, but can't be reduced without
  // introducing somewhat worse complexity and additional objects.
  // CHECKSTYLE:OFF
  public static SyAWTWindowRendererEmbossedFrameParameters
  renderFrameEmbossedActualFrameCalculateParameters(
    final SyThemeWindowTitleBar titlebar_theme,
    final SyThemeWindowFrame frame_theme,
    final SyThemeEmboss emboss,
    final Optional<SyThemeOutline> outline_opt,
    final PAreaI<SySpaceParentRelativeType> frame_box)
  {
    Objects.requireNonNull(titlebar_theme, "titlebar_theme");
    Objects.requireNonNull(frame_theme, "frame_theme");
    Objects.requireNonNull(emboss, "emboss");
    Objects.requireNonNull(outline_opt, "outline_opt");
    Objects.requireNonNull(frame_box, "frame_box");

    final PAreaI<SySpaceParentRelativeType> box_adjusted =
      adjustFrameBoxForOutline(frame_box, outline_opt);

    final int frame_top_height = frame_theme.topHeight();
    final int frame_right_width = frame_theme.rightWidth();
    final int frame_left_width = frame_theme.leftWidth();
    final int frame_bottom_height = frame_theme.bottomHeight();

    int bottom_left_len = Math.max(frame_left_width, frame_bottom_height);
    int bottom_right_len = Math.max(frame_right_width, frame_bottom_height);
    int top_left_len = Math.max(frame_left_width, frame_top_height);
    int top_right_len = Math.max(frame_right_width, frame_top_height);

    boolean bottom_right_caps = false;
    boolean top_right_caps = false;
    boolean top_left_caps = false;
    boolean bottom_left_caps = false;

    int bottom_x = 0;
    final int bottom_y = box_adjusted.sizeY() - frame_bottom_height;
    int right_y = 0;
    int top_x = 0;
    int left_y = 0;

    int top_width = box_adjusted.sizeX();
    int bottom_width = box_adjusted.sizeX();
    int left_height = box_adjusted.sizeY();
    int right_height = box_adjusted.sizeY();

    final int cap_length = frame_top_height + titlebar_theme.height() - top_left_len;

    switch (frame_theme.topLeftStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Preconditions.checkPreconditionI(
          frame_left_width,
          frame_left_width > 0,
          i -> "L piece left_width must be > 0");
        Preconditions.checkPreconditionI(
          frame_top_height,
          frame_top_height > 0,
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

    switch (frame_theme.topRightStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Preconditions.checkPreconditionI(
          frame_right_width,
          frame_right_width > 0,
          i -> "L piece right_width must be > 0");
        Preconditions.checkPreconditionI(
          frame_top_height,
          frame_top_height > 0,
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

    switch (frame_theme.bottomLeftStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Preconditions.checkPreconditionI(
          frame_left_width,
          frame_left_width > 0,
          i -> "L piece left_width must be > 0");
        Preconditions.checkPreconditionI(
          frame_bottom_height,
          frame_bottom_height > 0,
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

    switch (frame_theme.bottomRightStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        Preconditions.checkPreconditionI(
          frame_right_width,
          frame_right_width > 0,
          i -> "L piece right_width must be > 0");
        Preconditions.checkPreconditionI(
          frame_bottom_height,
          frame_bottom_height > 0,
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

    return SyAWTWindowRendererEmbossedFrameParameters.builder()
      .setBottomHeight(frame_bottom_height)
      .setBottomLeftCaps(bottom_left_caps)
      .setBottomLeftArmLength(bottom_left_len)
      .setBottomRightCaps(bottom_right_caps)
      .setBottomRightArmLength(bottom_right_len)
      .setBottomWidth(bottom_width)
      .setBottomX(bottom_x)
      .setBottomY(bottom_y)
      .setEmbossSize(emboss.size())
      .setFrameBoxAdjustedForOutline(box_adjusted)
      .setLeftHeight(left_height)
      .setLeftWidth(frame_left_width)
      .setLeftY(left_y)
      .setRightHeight(right_height)
      .setRightWidth(frame_right_width)
      .setRightY(right_y)
      .setTopHeight(frame_top_height)
      .setTopLeftCaps(top_left_caps)
      .setTopLeftArmLength(top_left_len)
      .setTopRightCaps(top_right_caps)
      .setTopRightArmLength(top_right_len)
      .setTopWidth(top_width)
      .setTopX(top_x)
      .build();
  }
  // CHECKSTYLE:ON
}
