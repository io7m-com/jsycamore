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

package com.io7m.jsycamore.core.themes.provided;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButton;
import com.io7m.jsycamore.core.themes.SyThemeButtonType;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeImage;
import com.io7m.jsycamore.core.themes.SyThemeImageType;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;
import com.io7m.jsycamore.core.themes.SyThemeOutline;
import com.io7m.jsycamore.core.themes.SyThemeOutlines;
import com.io7m.jsycamore.core.themes.SyThemePanel;
import com.io7m.jsycamore.core.themes.SyThemePanelType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.SyThemeWindow;
import com.io7m.jsycamore.core.themes.SyThemeWindowArrangement;
import com.io7m.jsycamore.core.themes.SyThemeWindowArrangementType;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameCorner;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameType;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.core.themes.SyThemeWindowType;
import com.io7m.jtensors.VectorI3F;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Optional;

/**
 * A 1980s style workstation theme.
 */

public final class SyThemeMotive
{
  private SyThemeMotive()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Create a theme builder based on the given input values.
   *
   * @param spec The theme-specific input values
   *
   * @return A new theme
   */

  public static SyTheme.Builder builderFrom(
    final SyThemeMotiveSpecificationType spec)
  {
    NullCheck.notNull(spec);

    final SyTheme.Builder theme = SyTheme.builder();

    final VectorI3F background = spec.backgroundColor();
    final VectorI3F background_lighter =
      VectorI3F.scale(background, spec.colorLightFactor());
    final VectorI3F background_lighter_lighter =
      VectorI3F.scale(background_lighter, spec.colorLightFactor());
    final VectorI3F background_darker =
      VectorI3F.scale(background, spec.colorDarkFactor());

    final VectorI3F color_active_lighter =
      VectorI3F.scale(spec.colorActive(), spec.colorLightFactor());
    final VectorI3F color_active_darker =
      VectorI3F.scale(spec.colorActive(), spec.colorDarkFactor());

    final float average =
      (spec.colorActive().getXF()
        + spec.colorActive().getYF()
        + spec.colorActive().getZF()) / 3.0f;

    final VectorI3F color_inactive_base =
      new VectorI3F(average, average, average);
    final VectorI3F color_inactive_lighter =
      VectorI3F.scale(color_inactive_base, spec.colorLightFactor());
    final VectorI3F color_inactive_darker =
      VectorI3F.scale(color_inactive_base, spec.colorDarkFactor());

    final VectorI3F text_color_active =
      new VectorI3F(1.0f, 1.0f, 1.0f);
    final VectorI3F text_color_inactive =
      VectorI3F.scale(text_color_active, 0.6f);

    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(color_active_lighter);
    theme_titlebar_emboss_active_b.setColorLeft(color_active_lighter);
    theme_titlebar_emboss_active_b.setColorRight(color_active_darker);
    theme_titlebar_emboss_active_b.setColorBottom(color_active_darker);

    final SyThemeEmboss.Builder theme_titlebar_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_inactive_b.setSize(1);
    theme_titlebar_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_titlebar_emboss_inactive_b.setColorBottom(color_inactive_darker);

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setTextFont("Monospaced 10");
    theme_titlebar_b.setColorActive(spec.colorActive());
    theme_titlebar_b.setColorInactive(color_inactive_base);
    theme_titlebar_b.setTextColorActive(text_color_active);
    theme_titlebar_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setEmbossActive(
      Optional.of(theme_titlebar_emboss_active_b.build()));
    theme_titlebar_b.setEmbossInactive(
      Optional.of(theme_titlebar_emboss_inactive_b.build()));
    theme_titlebar_b.setTextAlignment(
      SyAlignmentHorizontal.ALIGN_CENTER);

    final SyThemeEmboss.Builder theme_frame_emboss_active_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(color_active_lighter);
    theme_frame_emboss_active_b.setColorLeft(color_active_lighter);
    theme_frame_emboss_active_b.setColorRight(color_active_darker);
    theme_frame_emboss_active_b.setColorBottom(color_active_darker);

    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_frame_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_frame_emboss_inactive_b.setColorBottom(color_inactive_darker);

    final SyThemeWindowFrame.Builder theme_frame_b =
      SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(5);
    theme_frame_b.setTopHeight(5);
    theme_frame_b.setLeftWidth(5);
    theme_frame_b.setRightWidth(5);
    theme_frame_b.setColorActive(spec.colorActive());
    theme_frame_b.setColorInactive(color_inactive_base);

    theme_frame_b.setTopLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);
    theme_frame_b.setTopRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);
    theme_frame_b.setBottomLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);
    theme_frame_b.setBottomRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);

    theme_frame_b.setEmbossActive(theme_frame_emboss_active_b.build());
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive_b.build());
    theme_frame_b.setOutline(SyThemeOutline.of(
      true, true, true, true,
      new VectorI3F(0.0f, 0.0f, 0.0f),
      new VectorI3F(0.3f, 0.3f, 0.3f)));

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_frame_b.build(),
        SyThemeMotive::arrangeWindowComponents));

    theme.setPanelTheme(SyThemeMotive.createThemePanel(
      background,
      background_lighter,
      background_darker));

    theme.setButtonTheme(SyThemeMotive.createThemeButton(
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker));

    theme.setLabelTheme(SyThemeMotive.createThemeLabel(spec.foregroundColor()));
    theme.setImageTheme(SyThemeMotive.createThemeImage(background_darker));
    return theme;
  }

  /**
   * Arrange components in a manner suitable for this theme.
   *
   * @param measurement A text measurement interface
   * @param window      The window
   * @param window_box  The box covering the window
   *
   * @return A set of boxes for the components
   */

  public static SyThemeWindowArrangementType arrangeWindowComponents(
    final SyTextMeasurementType measurement,
    final SyWindowReadableType window,
    final SyBoxType<SySpaceParentRelativeType> window_box)
  {
    NullCheck.notNull(measurement);
    NullCheck.notNull(window);
    NullCheck.notNull(window_box);

    final SyThemeType theme = window.theme();
    final SyThemeWindowType theme_window = theme.windowTheme();

    /**
     * Calculate a frame that borders the entire window, with an exclusion
     * area inside which is placed the titlebar and content.
     */

    final SyThemeWindowFrameType frame_theme = theme_window.frame();

    final SyBoxType<SySpaceParentRelativeType> box_frame =
      SyThemeOutlines.scaleForOutlineOptional(
        window_box, frame_theme.outline());

    final SyBoxType<SySpaceParentRelativeType> box_frame_inner =
      SyBoxes.hollowOut(
        box_frame,
        frame_theme.leftWidth(),
        frame_theme.rightWidth(),
        frame_theme.topHeight(),
        frame_theme.bottomHeight());

    /**
     * Calculate a titlebar that appears at the top of the inside of the
     * frame.
     */

    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final SyBoxType<SySpaceParentRelativeType> box_titlebar_initial =
      SyBoxes.create(
        box_frame_inner.minimumX(),
        box_frame_inner.minimumY(),
        box_frame_inner.width(),
        titlebar_theme.height());

    final SyBoxType<SySpaceParentRelativeType> box_titlebar =
      SyThemeOutlines.scaleForOutlineOptional(
        box_titlebar_initial, titlebar_theme.outline());

    /**
     * The content area is whatever space is left over.
     */

    final SyBox<SySpaceParentRelativeType> box_content = SyBox.of(
      box_frame_inner.minimumX(),
      box_frame_inner.maximumX(),
      box_titlebar.maximumY(),
      box_frame_inner.maximumY());

    return SyThemeWindowArrangement.of(
      box_frame,
      box_frame_inner,
      box_titlebar,
      box_content);
  }

  private static SyThemeImageType createThemeImage(
    final VectorI3F color)
  {
    final SyThemeImage.Builder b = SyThemeImage.builder();
    b.setOutline(SyThemeOutline.of(true, true, true, true, color, color));
    return b.build();
  }

  private static SyThemePanelType createThemePanel(
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_darker)
  {
    final SyThemePanel.Builder b = SyThemePanel.builder();
    b.setColor(background);
    b.setEmboss(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1));
    return b.build();
  }

  private static SyThemeLabelType createThemeLabel(final VectorI3F foreground)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColor(foreground);
    b.setTextFont("Monospaced-plain-10");
    return b.build();
  }

  private static SyThemeButtonType createThemeButton(
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_lighter_lighter,
    final VectorI3F background_darker)
  {
    final SyThemeButton.Builder theme_button_b =
      SyThemeButton.builder();

    theme_button_b.setOutline(Optional.of(SyThemeOutline.of(
      true, true, true, true,
      background_darker,
      background_darker)));

    final int emboss_size = 2;

    theme_button_b.setColorActive(background);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      emboss_size
    ));

    theme_button_b.setColorDisabled(background);

    theme_button_b.setColorOver(background_lighter);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter_lighter,
      background,
      background_lighter_lighter,
      background,
      emboss_size
    ));

    theme_button_b.setColorPressed(background);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss_size
    ));

    return theme_button_b.build();
  }

  /**
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return SyThemeMotive.builderFrom(
      SyThemeMotiveSpecification.builder().build());
  }
}
