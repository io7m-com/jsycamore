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

package com.io7m.jsycamore.themes.fenestra;

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
import com.io7m.jsycamore.api.themes.SyThemeImage;
import com.io7m.jsycamore.api.themes.SyThemeLabel;
import com.io7m.jsycamore.api.themes.SyThemeMeter;
import com.io7m.jsycamore.api.themes.SyThemeMeterOriented;
import com.io7m.jsycamore.api.themes.SyThemeOutline;
import com.io7m.jsycamore.api.themes.SyThemePadding;
import com.io7m.jsycamore.api.themes.SyThemePanel;
import com.io7m.jsycamore.api.themes.SyThemeTitleBarElement;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.themes.SyThemeWindow;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangement;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangementType;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.api.themes.SyThemeWindowFrameType;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.api.themes.SyThemeWindowType;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.io7m.jsycamore.api.themes.SyThemeWindowFrameCorner.FRAME_CORNER_NONE;

/**
 * A 1990s style consumer computer theme.
 */

public final class SyThemeFenestra
{
  private static final URI ICON_CLOSE;
  private static final URI ICON_MAXIMIZE;
  private static final URI ICON_CHECK;

  static {
    try {
      ICON_CLOSE =
        SyThemeFenestra.class.getResource(
          "/com/io7m/jsycamore/themes/fenestra/fenestra-close.png").toURI();
      ICON_MAXIMIZE =
        SyThemeFenestra.class.getResource(
          "/com/io7m/jsycamore/themes/fenestra/fenestra-maximize.png").toURI();
      ICON_CHECK =
        SyThemeFenestra.class.getResource(
          "/com/io7m/jsycamore/themes/fenestra/fenestra-check.png").toURI();
    } catch (final URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private SyThemeFenestra()
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
    final SyThemeFenestraSpecificationType spec)
  {
    Objects.requireNonNull(spec, "Specification");

    final SyTheme.Builder theme = SyTheme.builder();

    final Vector3D background = spec.backgroundColor();
    final Vector3D background_lighter =
      Vectors3D.scale(background, spec.colorLightFactor());
    final Vector3D background_lighter_lighter =
      Vectors3D.scale(background_lighter, spec.colorLightFactor());
    final Vector3D background_darker =
      Vectors3D.scale(background, spec.colorDarkFactor());

    final Vector3D frame_color_lighter =
      Vectors3D.scale(spec.frameColor(), spec.colorLightFactor());
    final Vector3D frame_color_darker =
      Vectors3D.scale(spec.frameColor(), spec.colorDarkFactor());

    final Vector3D title_color_inactive_base =
      spec.titlebarColorInactive();

    final Vector3D text_color_active =
      spec.titlebarTextColorActive();
    final Vector3D text_color_inactive =
      spec.titlebarTextColorInactive();

    final SyThemePanel theme_titlebar_panel =
      themeTitleBarPanel(spec, title_color_inactive_base);

    final SyThemeWindowTitleBar theme_titlebar =
      themeTitleBar(
        spec,
        background,
        background_lighter,
        background_lighter_lighter,
        background_darker,
        text_color_active,
        text_color_inactive,
        theme_titlebar_panel);

    final SyThemeEmboss theme_frame_emboss_active =
      themeFrameEmbossActive(frame_color_lighter, frame_color_darker);
    final SyThemeEmboss theme_frame_emboss_inactive =
      themeFrameEmbossInactive(frame_color_lighter, frame_color_darker);
    final SyThemeWindowFrame theme_frame =
      themeWindowFrame(spec, theme_frame_emboss_active, theme_frame_emboss_inactive);

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar,
        theme_frame,
        SyThemeFenestra::arrangeWindowComponents));

    theme.setButtonRepeatingTheme(
      createThemeButtonRepeating(
        spec,
        background,
        background_lighter,
        background_lighter_lighter,
        background_darker,
        1,
        true));

    theme.setButtonCheckboxTheme(createThemeButtonCheckbox(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      1,
      true));

    theme.setMeterTheme(createThemeMeter(spec));
    theme.setPanelTheme(createThemePanel(background, background_darker));
    theme.setLabelTheme(createThemeLabel(
      spec.foregroundColorActive(),
      spec.foregroundColorInactive()));
    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeWindowTitleBar themeTitleBar(
    final SyThemeFenestraSpecificationType spec,
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_lighter_lighter,
    final Vector3D background_darker,
    final Vector3D text_color_active,
    final Vector3D text_color_inactive,
    final SyThemePanel theme_titlebar_panel)
  {
    final SyThemeWindowTitleBar.Builder theme_titlebar_b = SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setPanelTheme(theme_titlebar_panel);
    theme_titlebar_b.setButtonPadding(SyThemePadding.of(0, 3, 0, 0));
    theme_titlebar_b.setButtonHeight(14);
    theme_titlebar_b.setButtonWidth(16);
    theme_titlebar_b.setButtonTheme(createThemeTitlebarButton(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      1,
      false));
    theme_titlebar_b.setButtonAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setElementOrder(SyThemeFenestra::elementOrder);
    theme_titlebar_b.setHeight(18);
    theme_titlebar_b.setIconPresent(true);
    theme_titlebar_b.setIconHeight(16);
    theme_titlebar_b.setIconWidth(16);
    theme_titlebar_b.setIconTheme(SyThemeImage.builder().build());
    theme_titlebar_b.setIconAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setButtonCloseIcon(
      SyImageSpecification.of(
        ICON_CLOSE,
        16,
        14,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        ICON_MAXIMIZE,
        16,
        14,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

    final SyThemeLabel.Builder theme_titlebar_text_b = SyThemeLabel.builder();
    theme_titlebar_text_b.setTextFont("Sans-bold-11");
    theme_titlebar_text_b.setTextColorActive(text_color_active);
    theme_titlebar_text_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setTextAlignment(SyAlignmentHorizontal.ALIGN_LEFT);
    theme_titlebar_b.setTextPadding(SyThemePadding.of(4, 0, 0, 0));
    theme_titlebar_b.setTextTheme(theme_titlebar_text_b.build());
    return theme_titlebar_b.build();
  }

  private static SyThemePanel themeTitleBarPanel(
    final SyThemeFenestraSpecificationType spec,
    final Vector3D title_color_inactive_base)
  {
    final SyThemePanel.Builder theme_titlebar_panel_b = SyThemePanel.builder();
    theme_titlebar_panel_b.setFillActive(SyThemeColor.of(spec.titlebarColorActive()));
    theme_titlebar_panel_b.setFillInactive(SyThemeColor.of(title_color_inactive_base));
    return theme_titlebar_panel_b.build();
  }

  private static SyThemeWindowFrame themeWindowFrame(
    final SyThemeFenestraSpecificationType spec,
    final SyThemeEmboss theme_frame_emboss_active,
    final SyThemeEmboss theme_frame_emboss_inactive)
  {
    final SyThemeWindowFrame.Builder theme_frame_b = SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(4);
    theme_frame_b.setTopHeight(18 + (4 * 2));
    theme_frame_b.setLeftWidth(4);
    theme_frame_b.setRightWidth(4);
    theme_frame_b.setColorActive(spec.frameColor());
    theme_frame_b.setColorInactive(spec.frameColor());
    theme_frame_b.setTopLeftStyle(FRAME_CORNER_NONE);
    theme_frame_b.setTopRightStyle(FRAME_CORNER_NONE);
    theme_frame_b.setBottomLeftStyle(FRAME_CORNER_NONE);
    theme_frame_b.setBottomRightStyle(FRAME_CORNER_NONE);
    theme_frame_b.setEmbossActive(theme_frame_emboss_active);
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive);
    return theme_frame_b.build();
  }

  private static SyThemeEmboss themeFrameEmbossInactive(
    final Vector3D frame_color_lighter,
    final Vector3D frame_color_darker)
  {
    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b = SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(frame_color_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(frame_color_lighter);
    theme_frame_emboss_inactive_b.setColorRight(frame_color_darker);
    theme_frame_emboss_inactive_b.setColorBottom(frame_color_darker);
    return theme_frame_emboss_inactive_b.build();
  }

  private static SyThemeEmboss themeFrameEmbossActive(
    final Vector3D frame_color_lighter,
    final Vector3D frame_color_darker)
  {
    final SyThemeEmboss.Builder theme_frame_emboss_active_b = SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(frame_color_lighter);
    theme_frame_emboss_active_b.setColorLeft(frame_color_lighter);
    theme_frame_emboss_active_b.setColorRight(frame_color_darker);
    theme_frame_emboss_active_b.setColorBottom(frame_color_darker);
    return theme_frame_emboss_active_b.build();
  }

  private static SyThemeMeter createThemeMeter(
    final SyThemeFenestraSpecificationType spec)
  {
    final SyThemeMeterOriented.Builder b = SyThemeMeterOriented.builder();

    b.setFillContainerActive(SyThemeColor.of(Vector3D.of(1.0, 1.0, 1.0)));
    b.setEmbossContainerActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.frameColor(), 0.8),
      Vectors3D.scale(spec.frameColor(), 1.8),
      Vectors3D.scale(spec.frameColor(), 0.8),
      Vectors3D.scale(spec.frameColor(), 1.8),
      1
    ));

    b.setFillContainerInactive(SyThemeColor.of(Vector3D.of(1.0, 1.0, 1.0)));
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.frameColor(), 0.8),
      Vectors3D.scale(spec.frameColor(), 1.8),
      Vectors3D.scale(spec.frameColor(), 0.8),
      Vectors3D.scale(spec.frameColor(), 1.8),
      1
    ));

    b.setFillIndicatorActive(SyThemeColor.of(spec.titlebarColorActive()));
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      Vectors3D.scale(spec.titlebarColorActive(), 1.5),
      Vectors3D.scale(spec.titlebarColorActive(), 0.5),
      Vectors3D.scale(spec.titlebarColorActive(), 1.5),
      Vectors3D.scale(spec.titlebarColorActive(), 0.5),
      1
    ));

    b.setFillIndicatorInactive(SyThemeColor.of(spec.titlebarColorInactive()));
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      Vectors3D.scale(spec.titlebarColorInactive(), 1.5),
      Vectors3D.scale(spec.titlebarColorInactive(), 0.5),
      Vectors3D.scale(spec.titlebarColorInactive(), 1.5),
      Vectors3D.scale(spec.titlebarColorInactive(), 0.5),
      1
    ));

    return SyThemeMeter.of(b.build(), b.build());
  }

  private static SyThemeButtonCheckbox createThemeButtonCheckbox(
    final SyThemeFenestraSpecificationType spec,
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_lighter_lighter,
    final Vector3D background_darker,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonCheckbox.Builder theme_button_b =
      SyThemeButtonCheckbox.builder();

    if (outline) {
      theme_button_b.setOutline(SyThemeOutline.of(
        true, true, true, true,
        spec.foregroundColorActive(),
        background_darker,
        true));
    }

    final Vector3D base = Vector3D.of(1.0, 1.0, 1.0);

    theme_button_b.setFillActive(SyThemeColor.of(base));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));

    theme_button_b.setFillOver(SyThemeColor.of(background_lighter));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(base));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    theme_button_b.setCheckedIcon(
      SyImageSpecification.of(
        ICON_CHECK,
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

    return theme_button_b.build();
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
            return 1;
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
        break;
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

    final SyThemeType theme = window.theme();
    final SyThemeWindowType theme_window = theme.windowTheme();
    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final SyThemeWindowFrameType frame_theme = theme_window.frame();
    final int frame_left = frame_theme.leftWidth();
    final int frame_right = frame_theme.rightWidth();
    final int frame_top = frame_theme.topHeight();
    final int frame_bottom = frame_theme.bottomHeight();

    final int emboss_size =
      (window.isFocused() ? frame_theme.embossActive() : frame_theme.embossInactive())
        .map(emboss -> Integer.valueOf(emboss.size()))
        .orElse(Integer.valueOf(0)).intValue();

    final int pad_top = ((frame_top - titlebar_theme.height()) / 2) - emboss_size;
    final PAreaI<SySpaceParentRelativeType> box_titlebar =
      PAreasI.create(
        (frame_left / 2) + emboss_size,
        pad_top,
        window_box.maximumX() - (frame_right + 2),
        titlebar_theme.height());

    final PAreaI<SySpaceParentRelativeType> box_frame =
      PAreasI.create(0, 0, window_box.sizeX(), window_box.sizeY());

    final PAreaI<SySpaceParentRelativeType> box_frame_inner =
      PAreaI.of(
        box_titlebar.minimumX(),
        box_titlebar.maximumX(),
        box_titlebar.maximumY() + pad_top,
        (box_frame.maximumY() - frame_bottom) + emboss_size);

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
    b.setTextFont("Sans-plain-11");
    return b.build();
  }

  private static SyThemePanel createThemePanel(
    final Vector3D background,
    final Vector3D background_darker)
  {
    final SyThemePanel.Builder theme_panel_b =
      SyThemePanel.builder();
    theme_panel_b.setFillActive(SyThemeColor.of(background));
    theme_panel_b.setFillInactive(SyThemeColor.of(background));
    return theme_panel_b.build();
  }

  private static SyThemeButtonRepeating createThemeButtonRepeating(
    final SyThemeFenestraSpecificationType spec,
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_lighter_lighter,
    final Vector3D background_darker,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    if (outline) {
      theme_button_b.setOutline(SyThemeOutline.of(
        true, true, true, true,
        spec.foregroundColorActive(),
        background_darker,
        true));
    }

    theme_button_b.setFillActive(SyThemeColor.of(background));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));

    theme_button_b.setFillOver(SyThemeColor.of(background_lighter));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter_lighter,
      background,
      background_lighter_lighter,
      background,
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(background));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    return theme_button_b.build();
  }

  private static SyThemeButtonRepeating createThemeTitlebarButton(
    final SyThemeFenestraSpecificationType spec,
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_lighter_lighter,
    final Vector3D background_darker,
    final int emboss,
    final boolean outline)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    if (outline) {
      theme_button_b.setOutline(SyThemeOutline.of(
        true, true, true, true,
        spec.foregroundColorActive(),
        background_darker,
        true));
    }

    theme_button_b.setFillActive(SyThemeColor.of(background));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(background));
    theme_button_b.setEmbossInactive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      emboss
    ));

    theme_button_b.setFillOver(SyThemeColor.of(background_lighter));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      background_lighter_lighter,
      background,
      background_lighter_lighter,
      background,
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(background));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      emboss
    ));

    return theme_button_b.build();
  }


  /**
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return builderFrom(
      SyThemeFenestraSpecification.builder().build());
  }
}
