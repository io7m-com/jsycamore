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

package com.io7m.jsycamore.themes.stride;

import java.util.Objects;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.images.SyImageFormat;
import com.io7m.jsycamore.api.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyTextMeasurementType;
import com.io7m.jsycamore.api.themes.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.themes.SyAlignmentVertical;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeButtonCheckbox;
import com.io7m.jsycamore.api.themes.SyThemeButtonRepeating;
import com.io7m.jsycamore.api.themes.SyThemeColor;
import com.io7m.jsycamore.api.themes.SyThemeEmboss;
import com.io7m.jsycamore.api.themes.SyThemeGradientLinear;
import com.io7m.jsycamore.api.themes.SyThemeImage;
import com.io7m.jsycamore.api.themes.SyThemeLabel;
import com.io7m.jsycamore.api.themes.SyThemeMeter;
import com.io7m.jsycamore.api.themes.SyThemeMeterOriented;
import com.io7m.jsycamore.api.themes.SyThemeOutline;
import com.io7m.jsycamore.api.themes.SyThemePadding;
import com.io7m.jsycamore.api.themes.SyThemePanel;
import com.io7m.jsycamore.api.themes.SyThemeTitleBarElement;
import com.io7m.jsycamore.api.themes.SyThemeWindow;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangement;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangementType;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrameCorner;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.api.themes.SyThemeWindowType;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A 1990s style object-based workstation theme.
 */

public final class SyThemeStride
{
  private static final URI ICON_CLOSE;
  private static final URI ICON_MAXIMIZE;

  static {
    try {
      ICON_CLOSE =
        SyThemeStride.class.getResource(
          "/com/io7m/jsycamore/themes/stride/stride-close.png").toURI();
      ICON_MAXIMIZE =
        SyThemeStride.class.getResource(
          "/com/io7m/jsycamore/themes/stride/stride-maximize.png").toURI();
    } catch (final URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

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
    final SyThemeStrideSpecification spec)
  {
    Objects.requireNonNull(spec, "Specification");

    final SyTheme.Builder theme = SyTheme.builder();

    final Vector3D title_color_active_lighter =
      Vectors3D.scale(spec.titlebarColorActive(), 3.0);
    final Vector3D title_color_active_darker =
      Vectors3D.scale(spec.titlebarColorActive(), 2.0);

    final Vector3D frame_color_lighter =
      Vectors3D.scale(spec.frameColor(), 1.2);
    final Vector3D frame_color_darker =
      Vectors3D.scale(spec.frameColor(), 0.8);

    final Vector3D title_color_inactive_base =
      spec.titlebarColorInactive();
    final Vector3D color_inactive_lighter =
      Vectors3D.scale(title_color_inactive_base, 1.2);
    final Vector3D color_inactive_darker =
      Vectors3D.scale(title_color_inactive_base, 0.8);

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
        ICON_CLOSE,
        15,
        15,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        ICON_MAXIMIZE,
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

  private static SyThemeMeterOriented createThemeMeterHorizontal(
    final SyThemeStrideSpecification spec)
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
      gb.addDistributions(0.0);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.5));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.0));
      gb.addDistributions(1.0);

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

  private static SyThemeMeterOriented createThemeMeterVertical(
    final SyThemeStrideSpecification spec)
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
      gb.addDistributions(0.0);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.5));
      gb.addDistributions(0.5);
      gb.addColors(Vectors3D.scale(spec.colorPrimaryActive(), 1.0));
      gb.addDistributions(1.0);

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

  private static SyThemeMeter createThemeMeter(
    final SyThemeStrideSpecification spec)
  {
    return SyThemeMeter.of(
      SyThemeStride.createThemeMeterHorizontal(spec),
      SyThemeStride.createThemeMeterVertical(spec));
  }

  private static int elementOrder(
    final SyThemeTitleBarElement e0,
    final SyThemeTitleBarElement e1)
  {
    Objects.requireNonNull(e0, "Left");
    Objects.requireNonNull(e1, "Right");

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
    Objects.requireNonNull(measurement, "Measurement");
    Objects.requireNonNull(window, "Window");
    Objects.requireNonNull(window_box, "Window box");

    final SyTheme theme = window.theme();
    final SyThemeWindowType theme_window = theme.windowTheme();
    final SyThemeWindowTitleBar titlebar_theme = theme_window.titleBar();

    final PAreaI<SySpaceParentRelativeType> box_titlebar =
      PAreasI.create(0, 0, window_box.width(), titlebar_theme.height());

    final PAreaI<SySpaceParentRelativeType> box_frame =
      PAreaI.of(
        0,
        window_box.maximumX(),
        box_titlebar.maximumY() - 1,
        window_box.maximumY());

    final SyThemeWindowFrame frame_theme = theme_window.frame();
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

  private static SyThemeLabel createThemeLabel(
    final Vector3D foreground_active,
    final Vector3D foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Sans 10");
    return b.build();
  }

  private static SyThemePanel createThemePanel(
    final SyThemeStrideSpecification spec)
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

  private static SyThemeButtonRepeating createThemeButtonRepeating(
    final SyThemeStrideSpecification spec,
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

  private static SyThemeButtonCheckbox createThemeButtonCheckbox(
    final SyThemeStrideSpecification spec,
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
        ICON_CLOSE,
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

    return theme_button_b.build();
  }

  private static SyThemeButtonRepeating createThemeTitlebarButton(
    final SyThemeStrideSpecification spec,
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
