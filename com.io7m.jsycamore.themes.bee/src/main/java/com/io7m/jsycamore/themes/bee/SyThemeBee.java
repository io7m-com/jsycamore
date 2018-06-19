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

package com.io7m.jsycamore.themes.bee;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyTextMeasurementType;
import com.io7m.jsycamore.api.themes.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.themes.SyAlignmentVertical;
import com.io7m.jsycamore.api.themes.SyColors;
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
import com.io7m.jsycamore.api.themes.SyThemeTitleBars;
import com.io7m.jsycamore.api.themes.SyThemeWindow;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangement;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangementType;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.io7m.jsycamore.api.images.SyImageFormat.IMAGE_FORMAT_RGBA_8888;
import static com.io7m.jsycamore.api.images.SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST;
import static com.io7m.jsycamore.api.themes.SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE;
import static com.io7m.jsycamore.api.themes.SyThemeWindowFrameCorner.FRAME_CORNER_NONE;

/**
 * A 1990s style multimedia theme.
 */

public final class SyThemeBee
{
  private static final URI ICON_CLOSE;
  private static final URI ICON_MAXIMIZE;

  static {
    try {
      ICON_CLOSE =
        SyThemeBee.class.getResource(
          "/com/io7m/jsycamore/themes/bee/bee-close.png").toURI();
      ICON_MAXIMIZE =
        SyThemeBee.class.getResource(
          "/com/io7m/jsycamore/themes/bee/bee-maximize.png").toURI();
    } catch (final URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

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
    final SyThemeBeeSpecification spec)
  {
    Objects.requireNonNull(spec, "Specification");

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

    final SyThemeEmboss theme_titlebar_emboss_active =
      titleBarEmboss(title_color_active_lighter, title_color_active_darker);
    final SyThemeEmboss theme_titlebar_emboss_pressed =
      titleBarEmboss(title_color_active_darker, title_color_active_lighter);
    final SyThemeEmboss theme_titlebar_emboss_inactive =
      titleBarEmboss(color_inactive_lighter, color_inactive_darker);

    /*
     * Title bar button theme.
     */

    final SyThemeButtonRepeating theme_titlebar_button =
      titleBarButtonTheme(
        spec,
        theme_titlebar_emboss_active,
        theme_titlebar_emboss_pressed,
        theme_titlebar_emboss_inactive);

    /*
     * Titlebar panel theme.
     */

    final SyThemePanel theme_titlebar_panel =
      titleBarPanelTheme(
        spec,
        title_color_inactive_base,
        theme_titlebar_emboss_active,
        theme_titlebar_emboss_inactive);

    final SyThemeWindowTitleBar theme_title_bar =
      themeTitleBar(
        text_color_active,
        text_color_inactive,
        theme_titlebar_button,
        theme_titlebar_panel);

    final SyThemeEmboss theme_frame_emboss_active =
      titleBarFrameEmbossActive(background_lighter, background_darker);
    final SyThemeEmboss theme_frame_emboss_inactive =
      titleBarFrameEmbossInactive(background_lighter, background_darker);

    final SyThemeWindowFrame theme_window_frame =
      themeWindowFrame(spec, theme_frame_emboss_active, theme_frame_emboss_inactive);

    final SyThemeOutline.Builder theme_window_outline = SyThemeOutline.builder();
    theme_window_outline.setColorActive(
      Vector3D.of(0.0, 0.0, 0.0));
    theme_window_outline.setColorInactive(
      Vectors3D.scale(spec.backgroundColor(), 0.5));

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_title_bar,
        theme_window_frame,
        SyThemeBee::arrangeWindowComponents));

    theme.setButtonRepeatingTheme(createThemeButtonRepeating(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker));

    theme.setButtonCheckboxTheme(createThemeButtonCheckbox(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker));

    theme.setMeterTheme(createThemeMeter(spec));
    theme.setPanelTheme(createThemePanel(background));
    theme.setLabelTheme(createThemeLabel(
      spec.foregroundColorActive(),
      spec.foregroundColorInactive()));
    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeWindowTitleBar themeTitleBar(
    final Vector3D text_color_active,
    final Vector3D text_color_inactive,
    final SyThemeButtonRepeating theme_titlebar_button,
    final SyThemePanel theme_titlebar_panel)
  {
    final SyThemeWindowTitleBar.Builder theme_titlebar_b = SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setPanelTheme(theme_titlebar_panel);
    theme_titlebar_b.setButtonPadding(SyThemePadding.of(3, 3, 0, 0));
    theme_titlebar_b.setButtonHeight(13);
    theme_titlebar_b.setButtonWidth(13);
    theme_titlebar_b.setButtonTheme(theme_titlebar_button);
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
        ICON_CLOSE,
        13,
        13,
        IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        ICON_MAXIMIZE,
        13,
        13,
        IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SCALE_INTERPOLATION_NEAREST));

    final SyThemeLabel theme_titlebar_text =
      titleBarText(text_color_active, text_color_inactive);
    theme_titlebar_b.setTextAlignment(SyAlignmentHorizontal.ALIGN_CENTER);
    theme_titlebar_b.setTextPadding(SyThemePadding.of(8, 8, 0, 0));
    theme_titlebar_b.setTextTheme(theme_titlebar_text);
    return theme_titlebar_b.build();
  }

  private static SyThemeWindowFrame themeWindowFrame(
    final SyThemeBeeSpecification spec,
    final SyThemeEmboss theme_frame_emboss_active,
    final SyThemeEmboss theme_frame_emboss_inactive)
  {
    final SyThemeWindowFrame.Builder theme_frame_b = SyThemeWindowFrame.builder();
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

    theme_frame_b.setTopLeftStyle(FRAME_CORNER_NONE);
    theme_frame_b.setTopRightStyle(FRAME_CORNER_NONE);
    theme_frame_b.setBottomLeftStyle(FRAME_CORNER_NONE);
    theme_frame_b.setBottomRightStyle(FRAME_CORNER_L_PIECE);
    theme_frame_b.setEmbossActive(theme_frame_emboss_active);
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive);
    return theme_frame_b.build();
  }

  private static SyThemeEmboss titleBarFrameEmbossInactive(
    final Vector3D background_lighter,
    final Vector3D background_darker)
  {
    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b = SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(background_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(background_lighter);
    theme_frame_emboss_inactive_b.setColorRight(background_darker);
    theme_frame_emboss_inactive_b.setColorBottom(background_darker);
    return theme_frame_emboss_inactive_b.build();
  }

  private static SyThemeEmboss titleBarFrameEmbossActive(
    final Vector3D background_lighter,
    final Vector3D background_darker)
  {
    final SyThemeEmboss.Builder theme_frame_emboss_active_b = SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(background_lighter);
    theme_frame_emboss_active_b.setColorLeft(background_lighter);
    theme_frame_emboss_active_b.setColorRight(background_darker);
    theme_frame_emboss_active_b.setColorBottom(background_darker);
    return theme_frame_emboss_active_b.build();
  }

  private static SyThemeLabel titleBarText(
    final Vector3D text_color_active,
    final Vector3D text_color_inactive)
  {
    final SyThemeLabel.Builder theme_titlebar_text_b = SyThemeLabel.builder();
    theme_titlebar_text_b.setTextFont("Sans-bold-10");
    theme_titlebar_text_b.setTextColorActive(text_color_active);
    theme_titlebar_text_b.setTextColorInactive(text_color_inactive);
    return theme_titlebar_text_b.build();
  }

  private static SyThemePanel titleBarPanelTheme(
    final SyThemeBeeSpecification spec,
    final Vector3D title_color_inactive_base,
    final SyThemeEmboss theme_titlebar_emboss_active,
    final SyThemeEmboss theme_titlebar_emboss_inactive)
  {
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
    return theme_titlebar_panel_b.build();
  }

  private static SyThemeButtonRepeating titleBarButtonTheme(
    final SyThemeBeeSpecification spec,
    final SyThemeEmboss theme_titlebar_emboss_active,
    final SyThemeEmboss theme_titlebar_emboss_pressed,
    final SyThemeEmboss theme_titlebar_emboss_inactive)
  {
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
    return theme_titlebar_button_b.build();
  }

  private static SyThemeEmboss titleBarEmboss(
    final Vector3D title_color_active_lighter,
    final Vector3D title_color_active_darker)
  {
    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b = SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(title_color_active_lighter);
    theme_titlebar_emboss_active_b.setColorLeft(title_color_active_lighter);
    theme_titlebar_emboss_active_b.setColorRight(title_color_active_darker);
    theme_titlebar_emboss_active_b.setColorBottom(title_color_active_darker);
    return theme_titlebar_emboss_active_b.build();
  }

  private static SyThemeMeter createThemeMeter(
    final SyThemeBeeSpecification spec)
  {
    final SyThemeMeterOriented h = createThemeMeterHorizontal(spec);
    final SyThemeMeterOriented v = createThemeMeterVertical(spec);
    return SyThemeMeter.of(h, v);
  }

  private static SyThemeMeterOriented createThemeMeterHorizontal(
    final SyThemeBeeSpecification spec)
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

    b.setFillIndicatorActive(createThemeMeterHorizontalActiveGradient(spec));
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      1
    ));

    b.setFillIndicatorInactive(createThemeMeterHorizontalInactiveGradient(spec));
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.8),
      Vectors3D.scale(spec.colorPrimaryActive(), 0.5),
      1
    ));

    return b.build();
  }

  private static SyThemeGradientLinear createThemeMeterHorizontalActiveGradient(
    final SyThemeBeeSpecification spec)
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
    return gb.build();
  }

  private static SyThemeGradientLinear createThemeMeterHorizontalInactiveGradient(
    final SyThemeBeeSpecification spec)
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
    return gb.build();
  }

  private static SyThemeMeterOriented createThemeMeterVertical(
    final SyThemeBeeSpecification spec)
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

  // Cyclomatic complexity is an unfortunate but inevitable side effect of working with enums
  // CHECKSTYLE:OFF
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
  // CHECKSTYLE:ON

  /**
   * Arrange components in a manner suitable for this theme.
   *
   * @param measurement A text measurement interface
   * @param window      The window
   * @param window_box  The box covering the window
   *
   * @return A set of boxes for the components
   */

  private static SyThemeWindowArrangementType arrangeWindowComponents(
    final SyTextMeasurementType measurement,
    final SyWindowReadableType window,
    final PAreaI<SySpaceParentRelativeType> window_box)
  {
    Objects.requireNonNull(measurement, "Text measurement");
    Objects.requireNonNull(window, "Window");
    Objects.requireNonNull(window_box, "Box");

    final SyTheme theme = window.theme();
    final SyThemeWindow theme_window = theme.windowTheme();
    final SyThemeWindowTitleBar titlebar_theme = theme_window.titleBar();

    final int titlebar_width = SyThemeTitleBars.minimumWidthRequired(
      measurement,
      window_box,
      titlebar_theme,
      window.titleBar().text(),
      window.isCloseable(),
      window.isMaximizable());

    final PAreaI<SySpaceParentRelativeType> box_titlebar =
      PAreasI.create(0, 0, titlebar_width, titlebar_theme.height());

    final PAreaI<SySpaceParentRelativeType> box_frame =
      PAreaI.of(
        0,
        window_box.maximumX(),
        box_titlebar.maximumY() - 2,
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

  private static SyThemePanel createThemePanel(
    final Vector3D background)
  {
    final SyThemePanel.Builder b = SyThemePanel.builder();
    b.setFillActive(SyThemeColor.of(background));
    b.setFillInactive(SyThemeColor.of(background));
    return b.build();
  }

  private static SyThemeLabel createThemeLabel(
    final Vector3D foreground_active,
    final Vector3D foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Sans-plain-12");
    return b.build();
  }

  private static SyThemeButtonRepeating createThemeButtonRepeating(
    final SyThemeBeeSpecification spec,
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

    theme_button_b.setFillActive(createThemeButtonRepeatingActiveGradient(background));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));

    theme_button_b.setFillOver(createThemeButtonRepeatingOverGradient(background));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    final SyThemeGradientLinear gradient_pressed = createThemeButtonRepeatingPressedGradient(
      background);

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

  private static SyThemeGradientLinear createThemeButtonRepeatingPressedGradient(
    final Vector3D background)
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
    return gb.build();
  }

  private static SyThemeGradientLinear createThemeButtonRepeatingOverGradient(
    final Vector3D background)
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
    return gb.build();
  }

  private static SyThemeGradientLinear createThemeButtonRepeatingActiveGradient(
    final Vector3D background)
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
    return gb.build();
  }

  private static SyThemeButtonCheckbox createThemeButtonCheckbox(
    final SyThemeBeeSpecification spec,
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

    theme_button_b.setFillActive(createThemeButtonCheckboxActiveGradient(background));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));

    theme_button_b.setFillOver(createThemeButtonCheckboxOverGradient(background));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    theme_button_b.setFillPressed(createThemeButtonCheckboxPressedGradient(background));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    theme_button_b.setCheckedIcon(
      SyImageSpecification.of(
        ICON_CLOSE,
        16,
        16,
        IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SCALE_INTERPOLATION_NEAREST));

    return theme_button_b.build();
  }

  private static SyThemeGradientLinear createThemeButtonCheckboxPressedGradient(
    final Vector3D background)
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
    return gb.build();
  }

  private static SyThemeGradientLinear createThemeButtonCheckboxOverGradient(
    final Vector3D background)
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
    return gb.build();
  }

  private static SyThemeGradientLinear createThemeButtonCheckboxActiveGradient(
    final Vector3D background)
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
    return gb.build();
  }

  /**
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return builderFrom(
      SyThemeBeeSpecification.builder().build());
  }
}
