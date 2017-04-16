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
import com.io7m.jsycamore.core.themes.SyThemeColor;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeImage;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;
import com.io7m.jsycamore.core.themes.SyThemeMeter;
import com.io7m.jsycamore.core.themes.SyThemeMeterOriented;
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
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * A 1990s style consumer computer theme.
 */

public final class SyThemeFenestra
{
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
    NullCheck.notNull(spec, "Specification");

    final SyTheme.Builder theme = SyTheme.builder();

    final Vector3D background = spec.backgroundColor();
    final Vector3D background_lighter =
      Vectors3D.scale(background, spec.colorLightFactor());
    final Vector3D background_lighter_lighter =
      Vectors3D.scale(background_lighter, spec.colorLightFactor());
    final Vector3D background_darker =
      Vectors3D.scale(background, spec.colorDarkFactor());

    final Vector3D title_color_active_lighter =
      Vectors3D.scale(spec.titlebarColorActive(), spec.colorLightFactor());
    final Vector3D title_color_active_darker =
      Vectors3D.scale(spec.titlebarColorActive(), spec.colorDarkFactor());

    final Vector3D frame_color_lighter =
      Vectors3D.scale(spec.frameColor(), spec.colorLightFactor());
    final Vector3D frame_color_darker =
      Vectors3D.scale(spec.frameColor(), spec.colorDarkFactor());

    final Vector3D title_color_inactive_base =
      spec.titlebarColorInactive();
    final Vector3D color_inactive_lighter =
      Vectors3D.scale(title_color_inactive_base, spec.colorLightFactor());
    final Vector3D color_inactive_darker =
      Vectors3D.scale(title_color_inactive_base, spec.colorDarkFactor());

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

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();

    theme_titlebar_b.setPanelTheme(theme_titlebar_panel_b.build());
    theme_titlebar_b.setButtonPadding(SyThemePadding.of(0, 3, 0, 0));
    theme_titlebar_b.setButtonHeight(14);
    theme_titlebar_b.setButtonWidth(16);
    theme_titlebar_b.setButtonTheme(SyThemeFenestra.createThemeTitlebarButton(
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
        "/com/io7m/jsycamore/core/themes/provided/fenestra-close.png",
        16,
        14,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        "/com/io7m/jsycamore/core/themes/provided/fenestra-maximize.png",
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
    theme_frame_b.setBottomHeight(4);
    theme_frame_b.setTopHeight(18 + (4 * 2));
    theme_frame_b.setLeftWidth(4);
    theme_frame_b.setRightWidth(4);
    theme_frame_b.setColorActive(spec.frameColor());
    theme_frame_b.setColorInactive(spec.frameColor());

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
        SyThemeFenestra::arrangeWindowComponents));

    theme.setButtonRepeatingTheme(SyThemeFenestra.createThemeButtonRepeating(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      1,
      true));

    theme.setButtonCheckboxTheme(SyThemeFenestra.createThemeButtonCheckbox(
      spec,
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      1,
      true));

    theme.setMeterTheme(SyThemeFenestra.createThemeMeter(spec));

    theme.setPanelTheme(
      SyThemeFenestra.createThemePanel(background, background_darker));
    theme.setLabelTheme(
      SyThemeFenestra.createThemeLabel(
        spec.foregroundColorActive(),
        spec.foregroundColorInactive()));
    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeMeterType createThemeMeter(
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

  private static SyThemeButtonCheckboxType createThemeButtonCheckbox(
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
        "/com/io7m/jsycamore/core/themes/provided/fenestra-check.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));

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
    final SyBoxType<SySpaceParentRelativeType> box_titlebar =
      SyBoxes.create(
        (frame_left / 2) + emboss_size,
        pad_top,
        window_box.maximumX() - (frame_right + 2),
        titlebar_theme.height());

    final SyBoxType<SySpaceParentRelativeType> box_frame =
      SyBoxes.create(0, 0, window_box.width(), window_box.height());

    final SyBoxType<SySpaceParentRelativeType> box_frame_inner =
      SyBox.of(
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

  private static SyThemeLabelType createThemeLabel(
    final Vector3D foreground_active,
    final Vector3D foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Sans-plain-11");
    return b.build();
  }

  private static SyThemePanelType createThemePanel(
    final Vector3D background,
    final Vector3D background_darker)
  {
    final SyThemePanel.Builder theme_panel_b =
      SyThemePanel.builder();
    theme_panel_b.setFillActive(SyThemeColor.of(background));
    theme_panel_b.setFillInactive(SyThemeColor.of(background));
    return theme_panel_b.build();
  }

  private static SyThemeButtonRepeatingType createThemeButtonRepeating(
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

  private static SyThemeButtonRepeatingType createThemeTitlebarButton(
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
    return SyThemeFenestra.builderFrom(
      SyThemeFenestraSpecification.builder().build());
  }
}
