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
import com.io7m.jsycamore.core.themes.SyColors;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButton;
import com.io7m.jsycamore.core.themes.SyThemeButtonType;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeImage;
import com.io7m.jsycamore.core.themes.SyThemeImageType;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;
import com.io7m.jsycamore.core.themes.SyThemeOutline;
import com.io7m.jsycamore.core.themes.SyThemePadding;
import com.io7m.jsycamore.core.themes.SyThemePanel;
import com.io7m.jsycamore.core.themes.SyThemePanelType;
import com.io7m.jsycamore.core.themes.SyThemeTitlebarElement;
import com.io7m.jsycamore.core.themes.SyThemeTitlebars;
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

/**
 * A 1990s style multimedia theme.
 */

public final class SyThemeBee
{
  private SyThemeBee()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Create a theme based on the given input values.
   *
   * @param spec The theme-specific input values
   *
   * @return A new theme
   */

  public static SyTheme.Builder builderFrom(
    final SyThemeBeeSpecificationType spec)
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


    final VectorI3F title_color_active_lighter =
      SyColors.rotate(spec.titlebarColorActive(), spec.colorLightDegrees());
    final VectorI3F title_color_active_darker =
      SyColors.rotate(spec.titlebarColorActive(), spec.colorDarkDegrees());

    final VectorI3F frame_color_lighter =
      VectorI3F.scale(spec.frameColor(), spec.colorLightFactor());
    final VectorI3F frame_color_darker =
      VectorI3F.scale(spec.frameColor(), spec.colorDarkFactor());

    final VectorI3F title_color_inactive_base =
      spec.titlebarColorInactive();
    final VectorI3F color_inactive_lighter =
      SyColors.rotate(title_color_inactive_base, spec.colorLightDegrees());
    final VectorI3F color_inactive_darker =
      SyColors.rotate(title_color_inactive_base, spec.colorDarkDegrees());

    final VectorI3F text_color_active =
      spec.titlebarTextColorActive();
    final VectorI3F text_color_inactive =
      spec.titlebarTextColorInactive();

    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(title_color_active_lighter);
    theme_titlebar_emboss_active_b.setColorLeft(title_color_active_lighter);
    theme_titlebar_emboss_active_b.setColorRight(title_color_active_darker);
    theme_titlebar_emboss_active_b.setColorBottom(title_color_active_darker);

    final SyThemeEmboss theme_titlebar_emboss_active =
      theme_titlebar_emboss_active_b.build();

    final SyThemeEmboss.Builder theme_titlebar_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_inactive_b.setSize(1);
    theme_titlebar_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_titlebar_emboss_inactive_b.setColorBottom(color_inactive_darker);

    final SyThemeEmboss theme_titlebar_emboss_inactive =
      theme_titlebar_emboss_inactive_b.build();

    /*
     * Title bar button theme.
     */

    final SyThemeButton.Builder theme_titlebar_button_b =
      SyThemeButton.builder();
    theme_titlebar_button_b.setColorPressed(spec.titlebarColorActive());
    theme_titlebar_button_b.setEmbossPressed(theme_titlebar_emboss_active);
    theme_titlebar_button_b.setColorOver(spec.titlebarColorActive());
    theme_titlebar_button_b.setEmbossOver(theme_titlebar_emboss_active);
    theme_titlebar_button_b.setColorActive(spec.titlebarColorActive());
    theme_titlebar_button_b.setEmbossActive(theme_titlebar_emboss_active);
    theme_titlebar_button_b.setColorDisabled(spec.titlebarColorActive());
    theme_titlebar_button_b.setEmbossDisabled(theme_titlebar_emboss_active);

    /*
     * Titlebar panel theme.
     */

    final SyThemePanel.Builder theme_titlebar_panel_b =
      SyThemePanel.builder();
    theme_titlebar_panel_b.setColorActive(spec.titlebarColorActive());
    theme_titlebar_panel_b.setColorInactive(title_color_inactive_base);
    theme_titlebar_panel_b.setEmbossActive(theme_titlebar_emboss_active);
    theme_titlebar_panel_b.setEmbossInactive(theme_titlebar_emboss_inactive);
    theme_titlebar_panel_b.setOutline(SyThemeOutline.of(
      true, true, true, false,
      new VectorI3F(0.0f, 0.0f, 0.0f),
      new VectorI3F(0.3f, 0.3f, 0.3f),
      true));

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();

    theme_titlebar_b.setPanelTheme(theme_titlebar_panel_b.build());
    theme_titlebar_b.setButtonPadding(SyThemePadding.of(3, 3, 0, 0));
    theme_titlebar_b.setButtonHeight(13);
    theme_titlebar_b.setButtonWidth(13);
    theme_titlebar_b.setButtonTheme(theme_titlebar_button_b.build());
    theme_titlebar_b.setButtonAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setElementOrder(SyThemeBee::elementOrder);
    theme_titlebar_b.setHeight(19);
    theme_titlebar_b.setShowIcon(false);

    /*
     * Titlebar text.
     */

    final SyThemeLabel.Builder theme_titlebar_text_b = SyThemeLabel.builder();
    theme_titlebar_text_b.setTextFont("Sans-bold-10");
    theme_titlebar_text_b.setTextColorActive(text_color_active);
    theme_titlebar_text_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setTextAlignment(SyAlignmentHorizontal.ALIGN_CENTER);
    theme_titlebar_b.setTextPadding(SyThemePadding.of(8, 8, 0, 0));
    theme_titlebar_b.setTextTheme(theme_titlebar_text_b.build());

    final SyThemeEmboss.Builder theme_frame_emboss_active_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(frame_color_lighter);
    theme_frame_emboss_active_b.setColorLeft(frame_color_lighter);
    theme_frame_emboss_active_b.setColorRight(frame_color_darker);
    theme_frame_emboss_active_b.setColorBottom(frame_color_darker);

    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(frame_color_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(frame_color_lighter);
    theme_frame_emboss_inactive_b.setColorRight(frame_color_darker);
    theme_frame_emboss_inactive_b.setColorBottom(frame_color_darker);

    final SyThemeWindowFrame.Builder theme_frame_b =
      SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(3);
    theme_frame_b.setTopHeight(3);
    theme_frame_b.setLeftWidth(3);
    theme_frame_b.setRightWidth(3);
    theme_frame_b.setColorActive(spec.frameColor());
    theme_frame_b.setColorInactive(spec.frameColor());
    theme_frame_b.setOutline(SyThemeOutline.of(
      true, true, true, true,
      new VectorI3F(0.0f, 0.0f, 0.0f),
      new VectorI3F(0.3f, 0.3f, 0.3f),
      true));

    theme_frame_b.setTopLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setTopRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setBottomLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setBottomRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);

    theme_frame_b.setEmbossActive(theme_frame_emboss_active_b.build());
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive_b.build());

    final SyThemeOutline.Builder theme_window_outline = SyThemeOutline.builder();
    theme_window_outline.setColorActive(new VectorI3F(0.0f, 0.0f, 0.0f));
    theme_window_outline.setColorInactive(new VectorI3F(0.3f, 0.3f, 0.3f));

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_frame_b.build(),
        SyThemeBee::arrangeWindowComponents));

    theme.setButtonTheme(SyThemeBee.createThemeButton(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker));

    theme.setPanelTheme(SyThemeBee.createThemePanel(background));
    theme.setLabelTheme(SyThemeBee.createThemeLabel(spec.foregroundColor()));
    theme.setImageTheme(SyThemeBee.createThemeImage(background_darker));
    return theme;
  }

  private static int elementOrder(
    final SyThemeTitlebarElement e0,
    final SyThemeTitlebarElement e1)
  {
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
        break;
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
        break;
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
        break;
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
        break;
      }
    }

    throw new UnreachableCodeException();
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
    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final int titlebar_width = SyThemeTitlebars.minimumWidthRequired(
      measurement,
      window_box,
      titlebar_theme,
      window.titlebar().text(),
      window.isCloseable(),
      window.isMaximizable());

    final SyBoxType<SySpaceParentRelativeType> box_titlebar =
      SyBoxes.create(0, 0, titlebar_width, titlebar_theme.height());

    final SyBox<SySpaceParentRelativeType> box_frame =
      SyBox.of(
        0,
        window_box.maximumX(),
        box_titlebar.maximumY() - 2,
        window_box.maximumY());

    final SyThemeWindowFrameType frame_theme = theme_window.frame();
    final SyBoxType<SySpaceParentRelativeType> box_frame_inner =
      SyBoxes.hollowOut(
        box_frame,
        frame_theme.leftWidth() + 1,
        frame_theme.rightWidth() + 1,
        frame_theme.topHeight() + 1,
        frame_theme.bottomHeight() + 1);

    return SyThemeWindowArrangement.of(
      box_frame,
      box_frame_inner,
      box_titlebar,
      box_frame_inner);
  }

  private static SyThemeImageType createThemeImage(
    final VectorI3F color)
  {
    final SyThemeImage.Builder b = SyThemeImage.builder();
    b.setOutline(SyThemeOutline.of(true, true, true, true, color, color, true));
    return b.build();
  }

  private static SyThemePanelType createThemePanel(
    final VectorI3F background)
  {
    final SyThemePanel.Builder b = SyThemePanel.builder();
    b.setColorActive(background);
    b.setColorInactive(background);
    return b.build();
  }

  private static SyThemeLabelType createThemeLabel(final VectorI3F foreground)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground);
    b.setTextColorInactive(foreground);
    return b.build();
  }

  private static SyThemeButtonType createThemeButton(
    final SyThemeBeeSpecificationType spec,
    final VectorI3F background,
    final VectorI3F background_lighter,
    final VectorI3F background_lighter_lighter,
    final VectorI3F background_darker)
  {
    final SyThemeButton.Builder theme_button_b =
      SyThemeButton.builder();

    theme_button_b.setOutline(SyThemeOutline.of(
      true, true, true, true,
      spec.foregroundColor(),
      background_darker,
      true));

    theme_button_b.setColorActive(background);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    theme_button_b.setColorDisabled(background);

    theme_button_b.setColorOver(background_lighter);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter_lighter,
      background,
      background_lighter_lighter,
      background,
      1
    ));

    theme_button_b.setColorPressed(background);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    return theme_button_b.build();
  }

  /**
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return SyThemeBee.builderFrom(
      SyThemeBeeSpecification.builder().build());
  }
}
