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
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButtonCheckbox;
import com.io7m.jsycamore.core.themes.SyThemeButtonCheckboxType;
import com.io7m.jsycamore.core.themes.SyThemeButtonRepeating;
import com.io7m.jsycamore.core.themes.SyThemeButtonRepeatingType;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeImage;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;
import com.io7m.jsycamore.core.themes.SyThemeMeter;
import com.io7m.jsycamore.core.themes.SyThemeMeterType;
import com.io7m.jsycamore.core.themes.SyThemeOutline;
import com.io7m.jsycamore.core.themes.SyThemeOutlines;
import com.io7m.jsycamore.core.themes.SyThemePadding;
import com.io7m.jsycamore.core.themes.SyThemePanel;
import com.io7m.jsycamore.core.themes.SyThemePanelType;
import com.io7m.jsycamore.core.themes.SyThemeTitleBarElement;
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
import com.io7m.jtensors.VectorI4F;
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
    final VectorI3F background_darker_darker =
      VectorI3F.scale(background_darker, spec.colorDarkFactor());

    final VectorI3F color_active_base = spec.colorActive();
    final VectorI3F color_active_lighter =
      VectorI3F.scale(color_active_base, spec.colorLightFactor());
    final VectorI3F color_active_darker =
      VectorI3F.scale(color_active_base, spec.colorDarkFactor());

    final float average =
      (color_active_base.getXF()
        + color_active_base.getYF()
        + color_active_base.getZF()) / 3.0f;

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

    final SyThemePanel.Builder theme_titlebar_panel_b = SyThemePanel.builder();
    theme_titlebar_panel_b.setColorActive(color_active_base);
    theme_titlebar_panel_b.setColorInactive(color_inactive_base);
    theme_titlebar_panel_b.setEmbossActive(theme_titlebar_emboss_active_b.build());
    theme_titlebar_panel_b.setEmbossInactive(theme_titlebar_emboss_inactive_b.build());

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setPanelTheme(theme_titlebar_panel_b.build());

    final SyThemeLabel.Builder theme_titlebar_text_b = SyThemeLabel.builder();
    theme_titlebar_text_b.setTextFont("Monospaced 10");
    theme_titlebar_text_b.setTextColorActive(text_color_active);
    theme_titlebar_text_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setTextAlignment(SyAlignmentHorizontal.ALIGN_CENTER);
    theme_titlebar_b.setTextPadding(SyThemePadding.of(0, 0, 0, 0));
    theme_titlebar_b.setTextTheme(theme_titlebar_text_b.build());

    theme_titlebar_b.setPanelTheme(theme_titlebar_panel_b.build());
    theme_titlebar_b.setButtonPadding(SyThemePadding.of(0, 0, 0, 0));
    theme_titlebar_b.setButtonHeight(16);
    theme_titlebar_b.setButtonWidth(16);
    theme_titlebar_b.setButtonTheme(SyThemeMotive.createThemeTitlebarButton(
      color_active_base,
      color_active_lighter,
      color_active_darker,
      color_inactive_base,
      color_inactive_lighter,
      color_inactive_darker,
      1,
      false));
    theme_titlebar_b.setButtonAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setElementOrder(SyThemeMotive::elementOrder);
    theme_titlebar_b.setHeight(16);
    theme_titlebar_b.setIconPresent(false);
    theme_titlebar_b.setIconHeight(0);
    theme_titlebar_b.setIconWidth(0);
    theme_titlebar_b.setIconTheme(SyThemeImage.builder().build());
    theme_titlebar_b.setIconAlignment(SyAlignmentVertical.ALIGN_CENTER);

    theme_titlebar_b.setButtonCloseIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/motive-close.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/motive-maximize.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

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
    theme_frame_b.setColorActive(color_active_base);
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
      new VectorI3F(0.3f, 0.3f, 0.3f),
      true));

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_frame_b.build(),
        SyThemeMotive::arrangeWindowComponents));

    theme.setPanelTheme(SyThemeMotive.createThemePanel(
      background,
      background_lighter,
      background_darker));

    theme.setButtonRepeatingTheme(SyThemeMotive.createThemeButtonRepeating(
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      2,
      true));

    theme.setButtonCheckboxTheme(SyThemeMotive.createThemeButtonCheckbox(
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      2,
      true));

    theme.setMeterTheme(SyThemeMotive.createThemeMeter(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      background_darker_darker
    ));

    theme.setLabelTheme(SyThemeMotive.createThemeLabel(
      spec.foregroundColorActive(),
      spec.foregroundColorInactive()));

    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeMeterType createThemeMeter(
    final SyThemeMotiveSpecificationType spec,
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_lighter_lighter,
    final VectorI3F background_darker,
    final VectorI3F background_darker_darker)
  {
    final SyThemeMeter.Builder b = SyThemeMeter.builder();

    b.setColorContainerActive(background_darker_darker);
    b.setEmbossContainerActive(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    b.setColorContainerInactive(background_darker_darker);
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    b.setColorFillActive(spec.foregroundColorActive());
    b.setEmbossFillActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    b.setColorFillInactive(spec.foregroundColorInactive());
    b.setEmbossFillInactive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    return b.build();
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
     * area inside which is placed the titleBar and content.
     */

    final SyThemeWindowFrameType frame_theme = theme_window.frame();

    final SyBoxType<SySpaceParentRelativeType> box_frame = window_box;

    final SyBoxType<SySpaceParentRelativeType> box_frame_inner_initial =
      SyBoxes.hollowOut(
        box_frame,
        frame_theme.leftWidth(),
        frame_theme.rightWidth(),
        frame_theme.topHeight(),
        frame_theme.bottomHeight());

    final SyBoxType<SySpaceParentRelativeType> box_frame_inner =
      SyThemeOutlines.scaleForOutlineOptional(
        box_frame_inner_initial,
        frame_theme.outline());

    /**
     * Calculate a titleBar that appears at the top of the inside of the
     * frame.
     */

    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final SyBoxType<SySpaceParentRelativeType> box_titlebar =
      SyBoxes.create(
        box_frame_inner.minimumX(),
        box_frame_inner.minimumY(),
        box_frame_inner.width(),
        titlebar_theme.height());

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

  private static SyThemePanelType createThemePanel(
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_darker)
  {
    final SyThemePanel.Builder b = SyThemePanel.builder();
    b.setColorActive(background);
    b.setColorInactive(background);
    b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1));
    b.setEmbossInactive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1));
    return b.build();
  }

  private static SyThemeLabelType createThemeLabel(
    final VectorI3F foreground_active,
    final VectorI3F foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Monospaced-plain-10");
    return b.build();
  }

  private static SyThemeButtonRepeatingType createThemeButtonRepeating(
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_lighter_lighter,
    final VectorI3F background_darker,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    if (outline) {
      theme_button_b.setOutline(Optional.of(SyThemeOutline.of(
        true, true, true, true,
        background_darker,
        background_darker,
        true)));
    }

    theme_button_b.setColorActive(background);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      emboss
    ));

    theme_button_b.setColorInactive(background);

    theme_button_b.setColorOver(background_lighter);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter_lighter,
      background,
      background_lighter_lighter,
      background,
      emboss
    ));

    theme_button_b.setColorPressed(background);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    return theme_button_b.build();
  }

  private static SyThemeButtonCheckboxType createThemeButtonCheckbox(
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_lighter_lighter,
    final VectorI3F background_darker,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonCheckbox.Builder theme_button_b =
      SyThemeButtonCheckbox.builder();

    if (outline) {
      theme_button_b.setOutline(Optional.of(SyThemeOutline.of(
        true, true, true, true,
        background_darker,
        background_darker,
        true)));
    }

    theme_button_b.setColorActive(background);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      emboss
    ));

    theme_button_b.setColorInactive(background);

    theme_button_b.setColorOver(background_lighter);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter_lighter,
      background,
      background_lighter_lighter,
      background,
      emboss
    ));

    theme_button_b.setColorPressed(background);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    theme_button_b.setCheckedIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/motive-check.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

    return theme_button_b.build();
  }

  private static SyThemeButtonRepeatingType createThemeTitlebarButton(
    final VectorI3F color_active,
    final VectorI3F color_active_lighter,
    final VectorI3F color_active_darker,
    final VectorI3F color_inactive,
    final VectorI3F color_inactive_lighter,
    final VectorI3F color_inactive_darker,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    theme_button_b.setColorActive(color_active);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      color_active_lighter,
      color_active_darker,
      color_active_lighter,
      color_active_darker,
      emboss
    ));

    theme_button_b.setColorInactive(color_inactive);
    theme_button_b.setEmbossInactive(SyThemeEmboss.of(
      color_inactive_lighter,
      color_inactive_darker,
      color_inactive_lighter,
      color_inactive_darker,
      emboss
    ));

    theme_button_b.setColorOver(color_active);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      color_active_lighter,
      color_active_darker,
      color_active_lighter,
      color_active_darker,
      emboss
    ));

    theme_button_b.setColorPressed(color_active_darker);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      color_active,
      color_active_darker,
      color_active,
      color_active_darker,
      emboss
    ));

    return theme_button_b.build();
  }

  private static int elementOrder(
    final SyThemeTitleBarElement e0,
    final SyThemeTitleBarElement e1)
  {
    NullCheck.notNull(e0, "Left");
    NullCheck.notNull(e1, "Right");

    switch (e0) {
      case ELEMENT_CLOSE_BUTTON: {
        switch (e1) {
          case ELEMENT_CLOSE_BUTTON:
            return 0;
          case ELEMENT_MAXIMIZE_BUTTON:
            return -1;
          case ELEMENT_TITLE:
            return -1;
          case ELEMENT_ICON:
            return 1;
        }
        throw new UnreachableCodeException();
      }

      case ELEMENT_MAXIMIZE_BUTTON: {
        switch (e1) {
          case ELEMENT_CLOSE_BUTTON:
            return 1;
          case ELEMENT_MAXIMIZE_BUTTON:
            return 0;
          case ELEMENT_TITLE:
            return 1;
          case ELEMENT_ICON:
            return 1;
        }
        throw new UnreachableCodeException();
      }

      case ELEMENT_TITLE: {
        switch (e1) {
          case ELEMENT_CLOSE_BUTTON:
            return 1;
          case ELEMENT_MAXIMIZE_BUTTON:
            return -1;
          case ELEMENT_TITLE:
            return 0;
          case ELEMENT_ICON:
            return 1;
        }
        throw new UnreachableCodeException();
      }

      case ELEMENT_ICON: {
        switch (e1) {
          case ELEMENT_CLOSE_BUTTON:
            return -1;
          case ELEMENT_MAXIMIZE_BUTTON:
            return -1;
          case ELEMENT_TITLE:
            return -1;
          case ELEMENT_ICON:
            return 0;
        }
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
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
