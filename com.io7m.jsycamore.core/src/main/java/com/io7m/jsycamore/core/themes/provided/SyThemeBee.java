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
import com.io7m.jsycamore.core.themes.SyColors;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButtonCheckbox;
import com.io7m.jsycamore.core.themes.SyThemeButtonCheckboxType;
import com.io7m.jsycamore.core.themes.SyThemeButtonRepeating;
import com.io7m.jsycamore.core.themes.SyThemeButtonRepeatingType;
import com.io7m.jsycamore.core.themes.SyThemeColor;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeGradientLinear;
import com.io7m.jsycamore.core.themes.SyThemeImage;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;
import com.io7m.jsycamore.core.themes.SyThemeMeter;
import com.io7m.jsycamore.core.themes.SyThemeMeterOriented;
import com.io7m.jsycamore.core.themes.SyThemeMeterOrientedType;
import com.io7m.jsycamore.core.themes.SyThemeMeterType;
import com.io7m.jsycamore.core.themes.SyThemeOutline;
import com.io7m.jsycamore.core.themes.SyThemePadding;
import com.io7m.jsycamore.core.themes.SyThemePanel;
import com.io7m.jsycamore.core.themes.SyThemePanelType;
import com.io7m.jsycamore.core.themes.SyThemeTitleBarElement;
import com.io7m.jsycamore.core.themes.SyThemeTitleBars;
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
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
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
    NullCheck.notNull(spec, "Specification");

    final SyTheme.Builder theme = SyTheme.builder();

    final double degrees_light = 10.0;
    final double degrees_dark = -10.0;
    final double factor_light = 1.2;
    final double factor_dark = 0.8;

    final Vector3D background =
      spec.backgroundColor();
    final Vector3D background_lighter =
      Vectors3D.scale(background, factor_light);
    final Vector3D background_lighter_lighter =
      Vectors3D.scale(background_lighter, factor_light);
    final Vector3D background_darker =
      Vectors3D.scale(background, factor_dark);

    final Vector3D title_color_active_lighter =
      SyColors.rotate(spec.titlebarColorActive(), degrees_light);
    final Vector3D title_color_active_darker =
      SyColors.rotate(spec.titlebarColorActive(), degrees_dark);

    final Vector3D title_color_inactive_base =
      spec.titlebarColorInactive();
    final Vector3D color_inactive_lighter =
      SyColors.rotate(title_color_inactive_base, degrees_light);
    final Vector3D color_inactive_darker =
      SyColors.rotate(title_color_inactive_base, degrees_dark);

    final Vector3D text_color_active =
      spec.titlebarTextColorActive();
    final Vector3D text_color_inactive =
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

    final SyThemeEmboss.Builder theme_titlebar_emboss_pressed_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_pressed_b.setSize(1);
    theme_titlebar_emboss_pressed_b.setColorTop(title_color_active_darker);
    theme_titlebar_emboss_pressed_b.setColorLeft(title_color_active_darker);
    theme_titlebar_emboss_pressed_b.setColorRight(title_color_active_lighter);
    theme_titlebar_emboss_pressed_b.setColorBottom(title_color_active_lighter);
    final SyThemeEmboss theme_titlebar_emboss_pressed =
      theme_titlebar_emboss_pressed_b.build();

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

    final SyThemeButtonRepeating.Builder theme_titlebar_button_b =
      SyThemeButtonRepeating.builder();
    theme_titlebar_button_b.setFillPressed(
      SyThemeColor.of(spec.titlebarColorActive()));
    theme_titlebar_button_b.setEmbossPressed(theme_titlebar_emboss_pressed);
    theme_titlebar_button_b.setFillOver(
      SyThemeColor.of(spec.titlebarColorActive()));
    theme_titlebar_button_b.setEmbossOver(theme_titlebar_emboss_active);
    theme_titlebar_button_b.setFillActive(
      SyThemeColor.of(spec.titlebarColorActive()));
    theme_titlebar_button_b.setEmbossActive(theme_titlebar_emboss_active);
    theme_titlebar_button_b.setFillInactive(
      SyThemeColor.of(spec.titlebarColorInactive()));
    theme_titlebar_button_b.setEmbossInactive(theme_titlebar_emboss_inactive);

    /*
     * Titlebar panel theme.
     */

    final SyThemePanel.Builder theme_titlebar_panel_b =
      SyThemePanel.builder();
    theme_titlebar_panel_b.setFillActive(
      SyThemeColor.of(spec.titlebarColorActive()));
    theme_titlebar_panel_b.setFillInactive(
      SyThemeColor.of(title_color_inactive_base));
    theme_titlebar_panel_b.setEmbossActive(theme_titlebar_emboss_active);
    theme_titlebar_panel_b.setEmbossInactive(theme_titlebar_emboss_inactive);
    theme_titlebar_panel_b.setOutline(SyThemeOutline.of(
      true, true, true, false,
      Vector3D.of(0.0, 0.0, 0.0),
      Vectors3D.scale(spec.backgroundColor(), 0.5),
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
    theme_titlebar_b.setIconPresent(false);
    theme_titlebar_b.setIconHeight(0);
    theme_titlebar_b.setIconWidth(0);
    theme_titlebar_b.setIconTheme(SyThemeImage.builder().build());
    theme_titlebar_b.setIconAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setButtonCloseIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/bee-close.png",
        13,
        13,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/bee-maximize.png",
        13,
        13,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

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
    theme_frame_emboss_active_b.setColorTop(background_lighter);
    theme_frame_emboss_active_b.setColorLeft(background_lighter);
    theme_frame_emboss_active_b.setColorRight(background_darker);
    theme_frame_emboss_active_b.setColorBottom(background_darker);

    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(background_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(background_lighter);
    theme_frame_emboss_inactive_b.setColorRight(background_darker);
    theme_frame_emboss_inactive_b.setColorBottom(background_darker);

    final SyThemeWindowFrame.Builder theme_frame_b =
      SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(3);
    theme_frame_b.setTopHeight(3);
    theme_frame_b.setLeftWidth(3);
    theme_frame_b.setRightWidth(3);
    theme_frame_b.setColorActive(spec.backgroundColor());
    theme_frame_b.setColorInactive(spec.backgroundColor());
    theme_frame_b.setOutline(SyThemeOutline.of(
      true,
      true,
      true,
      true,
      Vector3D.of(0.0, 0.0, 0.0),
      Vectors3D.scale(spec.backgroundColor(), 0.5),
      true));

    theme_frame_b.setTopLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setTopRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setBottomLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setBottomRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);

    theme_frame_b.setEmbossActive(theme_frame_emboss_active_b.build());
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive_b.build());

    final SyThemeOutline.Builder theme_window_outline = SyThemeOutline.builder();
    theme_window_outline.setColorActive(
      Vector3D.of(0.0, 0.0, 0.0));
    theme_window_outline.setColorInactive(
      Vectors3D.scale(spec.backgroundColor(), 0.5));

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_frame_b.build(),
        SyThemeBee::arrangeWindowComponents));

    theme.setButtonRepeatingTheme(SyThemeBee.createThemeButtonRepeating(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker));

    theme.setButtonCheckboxTheme(SyThemeBee.createThemeButtonCheckbox(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker));

    theme.setMeterTheme(SyThemeBee.createThemeMeter(spec));

    theme.setPanelTheme(SyThemeBee.createThemePanel(background));
    theme.setLabelTheme(SyThemeBee.createThemeLabel(
      spec.foregroundColorActive(),
      spec.foregroundColorInactive()));
    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeMeterType createThemeMeter(
    final SyThemeBeeSpecificationType spec)
  {
    final SyThemeMeterOrientedType h =
      SyThemeBee.createThemeMeterHorizontal(spec);
    final SyThemeMeterOrientedType v =
      SyThemeBee.createThemeMeterVertical(spec);
    return SyThemeMeter.of(h, v);
  }

  private static SyThemeMeterOrientedType createThemeMeterHorizontal(
    final SyThemeBeeSpecificationType spec)
  {
    final SyThemeMeterOriented.Builder b =
      SyThemeMeterOriented.builder();

    b.setFillContainerActive(
      SyThemeColor.of(Vector3D.of(1.0, 1.0, 1.0)));
    b.setEmbossContainerActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      1
    ));

    b.setFillContainerInactive(
      SyThemeColor.of(Vector3D.of(1.0, 1.0, 1.0)));
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      1
    ));

    final SyThemeGradientLinear gradient;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.1));
      gb.addDistributions(0.0);
      gb.addColors(spec.colorPrimaryActive());
      gb.addDistributions(0.4999);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 0.9));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 0.8));
      gb.addDistributions(1.0);

      gradient = gb.build();
    }

    b.setFillIndicatorActive(gradient);
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      1
    ));

    b.setFillIndicatorInactive(gradient);
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      1
    ));

    return b.build();
  }

  private static SyThemeMeterOrientedType createThemeMeterVertical(
    final SyThemeBeeSpecificationType spec)
  {
    final SyThemeMeterOriented.Builder b =
      SyThemeMeterOriented.builder();

    b.setFillContainerActive(
      SyThemeColor.of(Vector3D.of(1.0, 1.0, 1.0)));
    b.setEmbossContainerActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      1
    ));

    b.setFillContainerInactive(
      SyThemeColor.of(Vector3D.of(1.0, 1.0, 1.0)));
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.8),
      1
    ));

    final SyThemeGradientLinear gradient;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(1.0, 0.0));

      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.1));
      gb.addDistributions(0.0);
      gb.addColors(spec.colorPrimaryActive());
      gb.addDistributions(0.4999);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 0.9));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 0.8));
      gb.addDistributions(1.0);

      gradient = gb.build();
    }

    b.setFillIndicatorActive(gradient);
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      1
    ));

    b.setFillIndicatorInactive(gradient);
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      1
    ));

    return b.build();
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
    NullCheck.notNull(measurement, "Text measurement");
    NullCheck.notNull(window, "Window");
    NullCheck.notNull(window_box, "Box");

    final SyThemeType theme = window.theme();
    final SyThemeWindowType theme_window = theme.windowTheme();
    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final int titlebar_width = SyThemeTitleBars.minimumWidthRequired(
      measurement,
      window_box,
      titlebar_theme,
      window.titleBar().text(),
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

  private static SyThemePanelType createThemePanel(
    final Vector3D background)
  {
    final SyThemePanel.Builder b = SyThemePanel.builder();
    b.setFillActive(SyThemeColor.of(background));
    b.setFillInactive(SyThemeColor.of(background));
    return b.build();
  }

  private static SyThemeLabelType createThemeLabel(
    final Vector3D foreground_active,
    final Vector3D foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Sans-plain-12");
    return b.build();
  }

  private static SyThemeButtonRepeatingType createThemeButtonRepeating(
    final SyThemeBeeSpecificationType spec,
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_lighter_lighter,
    final Vector3D background_darker)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    theme_button_b.setOutline(SyThemeOutline.of(
      true,
      true,
      true,
      true,
      spec.foregroundColorActive(),
      background_darker,
      true));

    final SyThemeGradientLinear gradient_active;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(background, 1.02));
      gb.addDistributions(0.0);
      gb.addColors(background);
      gb.addDistributions(0.4999);
      gb.addColors(Vectors3D.scale(background, 0.98));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(background, 0.9));
      gb.addDistributions(1.0);

      gradient_active = gb.build();
    }

    theme_button_b.setFillActive(gradient_active);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));

    final SyThemeGradientLinear gradient_over;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(background, 1.03));
      gb.addDistributions(0.0);
      gb.addColors(background);
      gb.addDistributions(0.4999);
      gb.addColors(Vectors3D.scale(background, 0.99));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(background, 0.91));
      gb.addDistributions(1.0);

      gradient_over = gb.build();
    }

    theme_button_b.setFillOver(gradient_over);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    final SyThemeGradientLinear gradient_pressed;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(background, 0.9));
      gb.addDistributions(0.0);
      gb.addColors(Vectors3D.scale(background, 0.98));
      gb.addDistributions(0.4999);
      gb.addColors(background);
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(background, 1.02));
      gb.addDistributions(1.0);

      gradient_pressed = gb.build();
    }

    theme_button_b.setFillPressed(gradient_pressed);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    return theme_button_b.build();
  }

  private static SyThemeButtonCheckboxType createThemeButtonCheckbox(
    final SyThemeBeeSpecificationType spec,
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_lighter_lighter,
    final Vector3D background_darker)
  {
    final SyThemeButtonCheckbox.Builder theme_button_b =
      SyThemeButtonCheckbox.builder();

    theme_button_b.setOutline(SyThemeOutline.of(
      true,
      true,
      true,
      true,
      spec.foregroundColorActive(),
      background_darker,
      true));

    final SyThemeGradientLinear gradient_active;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(background, 1.02));
      gb.addDistributions(0.0);
      gb.addColors(background);
      gb.addDistributions(0.4999);
      gb.addColors(Vectors3D.scale(background, 0.98));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(background, 0.9));
      gb.addDistributions(1.0);

      gradient_active = gb.build();
    }

    theme_button_b.setFillActive(gradient_active);
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));

    final SyThemeGradientLinear gradient_over;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(background, 1.03));
      gb.addDistributions(0.0);
      gb.addColors(background);
      gb.addDistributions(0.4999);
      gb.addColors(Vectors3D.scale(background, 0.99));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(background, 0.91));
      gb.addDistributions(1.0);

      gradient_over = gb.build();
    }

    theme_button_b.setFillOver(gradient_over);
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    final SyThemeGradientLinear gradient_pressed;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(background, 0.9));
      gb.addDistributions(0.0);
      gb.addColors(Vectors3D.scale(background, 0.98));
      gb.addDistributions(0.4999);
      gb.addColors(background);
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(background, 1.02));
      gb.addDistributions(1.0);

      gradient_pressed = gb.build();
    }

    theme_button_b.setFillPressed(gradient_pressed);
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    theme_button_b.setCheckedIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/bee-close.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

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
