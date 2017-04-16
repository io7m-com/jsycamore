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
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
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
 * A 1990s style object-based workstation theme.
 */

public final class SyThemeStride
{
  private SyThemeStride()
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
    final SyThemeStrideSpecificationType spec)
  {
    NullCheck.notNull(spec, "Specification");

    final SyTheme.Builder theme = SyTheme.builder();

    final Vector3D title_color_active_lighter =
      Vectors3D.scale(spec.titlebarColorActive(), 3.0f);
    final Vector3D title_color_active_darker =
      Vectors3D.scale(spec.titlebarColorActive(), 2.0f);

    final Vector3D frame_color_lighter =
      Vectors3D.scale(spec.frameColor(), 1.2f);
    final Vector3D frame_color_darker =
      Vectors3D.scale(spec.frameColor(), 0.8f);

    final Vector3D title_color_inactive_base =
      spec.titlebarColorInactive();
    final Vector3D color_inactive_lighter =
      Vectors3D.scale(title_color_inactive_base, 1.2f);
    final Vector3D color_inactive_darker =
      Vectors3D.scale(title_color_inactive_base, 0.8f);

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

    final SyThemeEmboss.Builder theme_titlebar_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_inactive_b.setSize(1);
    theme_titlebar_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_titlebar_emboss_inactive_b.setColorBottom(color_inactive_darker);

    final SyThemePanel.Builder theme_titlebar_panel_b =
      SyThemePanel.builder();
    theme_titlebar_panel_b.setFillActive(
      SyThemeColor.of(spec.titlebarColorActive()));
    theme_titlebar_panel_b.setFillInactive(
      SyThemeColor.of(title_color_inactive_base));
    theme_titlebar_panel_b.setEmbossActive(theme_titlebar_emboss_active_b.build());
    theme_titlebar_panel_b.setEmbossInactive(theme_titlebar_emboss_inactive_b.build());
    theme_titlebar_panel_b.setOutline(SyThemeOutline.of(
      true, true, true, false,
      Vector3D.of(0.0, 0.0, 0.0),
      Vector3D.of(0.3, 0.3, 0.3),
      true));

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();

    theme_titlebar_b.setPanelTheme(theme_titlebar_panel_b.build());
    theme_titlebar_b.setPanelTheme(theme_titlebar_panel_b.build());
    theme_titlebar_b.setButtonPadding(SyThemePadding.of(3, 3, 0, 0));
    theme_titlebar_b.setButtonHeight(15);
    theme_titlebar_b.setButtonWidth(15);
    theme_titlebar_b.setButtonTheme(
      SyThemeStride.createThemeTitlebarButton(spec, 1, true));
    theme_titlebar_b.setButtonAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setElementOrder(SyThemeStride::elementOrder);
    theme_titlebar_b.setHeight(21);
    theme_titlebar_b.setIconPresent(false);
    theme_titlebar_b.setIconHeight(0);
    theme_titlebar_b.setIconWidth(0);
    theme_titlebar_b.setIconTheme(SyThemeImage.builder().build());
    theme_titlebar_b.setIconAlignment(SyAlignmentVertical.ALIGN_CENTER);

    theme_titlebar_b.setButtonCloseIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/stride-close.png",
        15,
        15,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/stride-maximize.png",
        15,
        15,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

    final SyThemeLabel.Builder theme_titlebar_text_b = SyThemeLabel.builder();
    theme_titlebar_text_b.setTextFont("Sans-bold-11");
    theme_titlebar_text_b.setTextColorActive(text_color_active);
    theme_titlebar_text_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setTextAlignment(SyAlignmentHorizontal.ALIGN_CENTER);
    theme_titlebar_b.setTextPadding(SyThemePadding.of(0, 0, 0, 0));
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
    theme_frame_b.setBottomHeight(5);
    theme_frame_b.setTopHeight(0);
    theme_frame_b.setLeftWidth(0);
    theme_frame_b.setRightWidth(0);
    theme_frame_b.setColorActive(spec.frameColor());
    theme_frame_b.setColorInactive(spec.frameColor());
    theme_frame_b.setOutline(SyThemeOutline.of(
      true, true, true, true,
      Vector3D.of(0.0, 0.0, 0.0),
      Vector3D.of(0.3, 0.3, 0.3),
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

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_frame_b.build(),
        SyThemeStride::arrangeWindowComponents));

    theme.setButtonRepeatingTheme(
      SyThemeStride.createThemeButtonRepeating(spec, 1, true));
    theme.setButtonCheckboxTheme(
      SyThemeStride.createThemeButtonCheckbox(spec, 1, true));

    theme.setMeterTheme(SyThemeStride.createThemeMeter(spec));

    theme.setPanelTheme(
      SyThemeStride.createThemePanel(spec));
    theme.setLabelTheme(
      SyThemeStride.createThemeLabel(
        spec.foregroundColorActive(),
        spec.foregroundColorInactive()));
    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeMeterOrientedType createThemeMeterHorizontal(
    final SyThemeStrideSpecificationType spec)
  {
    final SyThemeMeterOriented.Builder b =
      SyThemeMeterOriented.builder();

    b.setFillContainerActive(
      SyThemeColor.of(Vectors3D.scale(spec.backgroundColor(), 0.9)));
    b.setEmbossContainerActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      1
    ));

    b.setFillContainerInactive(
      SyThemeColor.of(Vectors3D.scale(spec.backgroundColor(), 0.9)));
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      1
    ));

    final SyThemeGradientLinear gradient;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(0.0, 1.0));

      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.0));
      gb.addDistributions(0.0f);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.5));
      gb.addDistributions(0.5f);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.0));
      gb.addDistributions(1.0f);

      gradient = gb.build();
    }

    b.setFillIndicatorActive(gradient);
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      1
    ));

    b.setFillIndicatorInactive(gradient);
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      1
    ));

    return b.build();
  }

  private static SyThemeMeterOrientedType createThemeMeterVertical(
    final SyThemeStrideSpecificationType spec)
  {
    final SyThemeMeterOriented.Builder b =
      SyThemeMeterOriented.builder();

    b.setFillContainerActive(
      SyThemeColor.of(Vectors3D.scale(spec.backgroundColor(), 0.9)));
    b.setEmbossContainerActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      1
    ));

    b.setFillContainerInactive(
      SyThemeColor.of(Vectors3D.scale(spec.backgroundColor(), 0.9)));
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      1
    ));

    final SyThemeGradientLinear gradient;
    {
      final SyThemeGradientLinear.Builder gb = SyThemeGradientLinear.builder();
      gb.setPoint0(Vector2D.of(0.0, 0.0));
      gb.setPoint1(Vector2D.of(1.0, 0.0));

      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.0));
      gb.addDistributions(0.0f);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.5));
      gb.addDistributions(0.5f);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.0));
      gb.addDistributions(1.0f);

      gradient = gb.build();
    }

    b.setFillIndicatorActive(gradient);
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      1
    ));

    b.setFillIndicatorInactive(gradient);
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.6),
      1
    ));

    return b.build();
  }

  private static SyThemeMeterType createThemeMeter(
    final SyThemeStrideSpecificationType spec)
  {
    return SyThemeMeter.of(
      SyThemeStride.createThemeMeterHorizontal(spec),
      SyThemeStride.createThemeMeterVertical(spec));
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
            return 1;
          case ELEMENT_TITLE:
            return 1;
          case ELEMENT_ICON:
            return 1;
        }
        throw new UnreachableCodeException();
      }
      case ELEMENT_MAXIMIZE_BUTTON: {
        switch (e1) {
          case ELEMENT_CLOSE_BUTTON:
            return -1;
          case ELEMENT_MAXIMIZE_BUTTON:
            return 0;
          case ELEMENT_TITLE:
            return -1;
          case ELEMENT_ICON:
            return 1;

        }
        throw new UnreachableCodeException();
      }
      case ELEMENT_TITLE: {
        switch (e1) {
          case ELEMENT_CLOSE_BUTTON:
            return -1;
          case ELEMENT_MAXIMIZE_BUTTON:
            return 1;
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
    final PAreaI<SySpaceParentRelativeType> window_box)
  {
    NullCheck.notNull(measurement, "Measurement");
    NullCheck.notNull(window, "Window");
    NullCheck.notNull(window_box, "Window box");

    final SyThemeType theme = window.theme();
    final SyThemeWindowType theme_window = theme.windowTheme();
    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final PAreaI<SySpaceParentRelativeType> box_titlebar =
      PAreasI.create(0, 0, window_box.width(), titlebar_theme.height());

    final PAreaI<SySpaceParentRelativeType> box_frame =
      PAreaI.of(
        0,
        window_box.maximumX(),
        box_titlebar.maximumY() - 1,
        window_box.maximumY());

    final SyThemeWindowFrameType frame_theme = theme_window.frame();
    final PAreaI<SySpaceParentRelativeType> box_frame_inner =
      PAreasI.hollowOut(
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

  private static SyThemeLabelType createThemeLabel(
    final Vector3D foreground_active,
    final Vector3D foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Sans 10");
    return b.build();
  }

  private static SyThemePanelType createThemePanel(
    final SyThemeStrideSpecificationType spec)
  {
    final SyThemePanel.Builder theme_panel_b =
      SyThemePanel.builder();

    theme_panel_b.setOutline(
      SyThemeOutline.of(
        true,
        true,
        true,
        true,
        Vectors3D.scale(spec.backgroundColor(), 0.5),
        Vectors3D.scale(spec.backgroundColor(), 0.5),
        true));

    theme_panel_b.setFillActive(SyThemeColor.of(spec.backgroundColor()));
    theme_panel_b.setFillInactive(SyThemeColor.of(spec.backgroundColor()));
    return theme_panel_b.build();
  }

  private static SyThemeButtonRepeatingType createThemeButtonRepeating(
    final SyThemeStrideSpecificationType spec,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    if (outline) {
      theme_button_b.setOutline(SyThemeOutline.of(
        true, true, true, true,
        spec.foregroundColorActive(),
        Vectors3D.scale(spec.backgroundColor(), 0.5),
        true));
    }

    theme_button_b.setFillActive(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(spec.backgroundColor()));

    theme_button_b.setFillOver(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      emboss
    ));

    return theme_button_b.build();
  }

  private static SyThemeButtonCheckboxType createThemeButtonCheckbox(
    final SyThemeStrideSpecificationType spec,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonCheckbox.Builder theme_button_b =
      SyThemeButtonCheckbox.builder();

    if (outline) {
      theme_button_b.setOutline(SyThemeOutline.of(
        true, true, true, true,
        spec.foregroundColorActive(),
        Vectors3D.scale(spec.backgroundColor(), 0.5),
        true));
    }

    theme_button_b.setFillActive(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(spec.backgroundColor()));

    theme_button_b.setFillOver(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      emboss
    ));

    theme_button_b.setCheckedIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/stride-close.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

    return theme_button_b.build();
  }

  private static SyThemeButtonRepeatingType createThemeTitlebarButton(
    final SyThemeStrideSpecificationType spec,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    if (outline) {
      theme_button_b.setOutline(SyThemeOutline.of(
        true,
        true,
        true,
        true,
        spec.foregroundColorActive(),
        Vectors3D.scale(spec.backgroundColor(), 0.5),
        true));
    }

    theme_button_b.setFillActive(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillOver(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(spec.backgroundColor()));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      Vectors3D.scale(spec.backgroundColor(), 0.8),
      Vectors3D.scale(spec.backgroundColor(), 1.2),
      emboss
    ));

    return theme_button_b.build();
  }

  /**
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return SyThemeStride.builderFrom(
      SyThemeStrideSpecification.builder().build());
  }
}
