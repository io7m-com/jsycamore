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

package com.io7m.jsycamore.themes.motive;

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
import com.io7m.jsycamore.api.themes.SyThemeOutlines;
import com.io7m.jsycamore.api.themes.SyThemePadding;
import com.io7m.jsycamore.api.themes.SyThemePanel;
import com.io7m.jsycamore.api.themes.SyThemeTitleBarElement;
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
import java.util.Optional;

import static com.io7m.jsycamore.api.themes.SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE;

/**
 * A 1980s style workstation theme.
 */

public final class SyThemeMotive
{
  private static final URI ICON_CLOSE;
  private static final URI ICON_MAXIMIZE;
  private static final URI ICON_CHECK;

  static {
    try {
      ICON_CLOSE =
        SyThemeMotive.class.getResource(
          "/com/io7m/jsycamore/themes/motive/motive-close.png").toURI();
      ICON_MAXIMIZE =
        SyThemeMotive.class.getResource(
          "/com/io7m/jsycamore/themes/motive/motive-maximize.png").toURI();
      ICON_CHECK =
        SyThemeMotive.class.getResource(
          "/com/io7m/jsycamore/themes/motive/motive-check.png").toURI();
    } catch (final URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

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
    final SyThemeMotiveSpecification spec)
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
    final Vector3D background_darker_darker =
      Vectors3D.scale(background_darker, spec.colorDarkFactor());

    final Vector3D color_active_base = spec.colorActive();
    final Vector3D color_active_lighter =
      Vectors3D.scale(color_active_base, spec.colorLightFactor());
    final Vector3D color_active_darker =
      Vectors3D.scale(color_active_base, spec.colorDarkFactor());

    final double average =
      (color_active_base.x() + color_active_base.y() + color_active_base.z()) / 3.0;

    final Vector3D color_inactive_base =
      Vector3D.of(average, average, average);
    final Vector3D color_inactive_lighter =
      Vectors3D.scale(color_inactive_base, spec.colorLightFactor());
    final Vector3D color_inactive_darker =
      Vectors3D.scale(color_inactive_base, spec.colorDarkFactor());

    final Vector3D text_color_active =
      Vector3D.of(1.0, 1.0, 1.0);
    final Vector3D text_color_inactive =
      Vectors3D.scale(text_color_active, 0.6);

    final SyThemeEmboss theme_titlebar_emboss_active =
      titleBarEmbossActive(color_active_lighter, color_active_darker);
    final SyThemeEmboss theme_titlebar_emboss_inactive =
      titleBarEmbossInactive(color_inactive_lighter, color_inactive_darker);
    final SyThemePanel theme_titlebar_panel =
      titleBarPanel(
        color_active_base,
        color_inactive_base,
        theme_titlebar_emboss_active,
        theme_titlebar_emboss_inactive);

    final SyThemeWindowTitleBar theme_titlebar =
      themeTitleBar(
        color_active_base,
        color_active_lighter,
        color_active_darker,
        color_inactive_base,
        color_inactive_lighter,
        color_inactive_darker,
        text_color_active,
        text_color_inactive,
        theme_titlebar_panel);

    final SyThemeEmboss theme_frame_emboss_active =
      themeFrameEmbossActive(color_active_lighter, color_active_darker);
    final SyThemeEmboss theme_frame_emboss_inactive =
      themeFrameEmbossInactive(color_inactive_lighter, color_inactive_darker);

    final SyThemeWindowFrame theme_frame =
      themeWindowFrame(
        color_active_base,
        color_inactive_base,
        theme_frame_emboss_active,
        theme_frame_emboss_inactive);

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar,
        theme_frame,
        SyThemeMotive::arrangeWindowComponents));

    theme.setPanelTheme(createThemePanel(
      background,
      background_lighter,
      background_darker));

    theme.setButtonRepeatingTheme(createThemeButtonRepeating(
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      2,
      true));

    theme.setButtonCheckboxTheme(createThemeButtonCheckbox(
      background,
      background_lighter,
      background_lighter_lighter,
      background_darker,
      2,
      true));

    theme.setMeterTheme(createThemeMeter(
      spec,
      background_lighter,
      background_darker,
      background_darker_darker
    ));

    theme.setLabelTheme(createThemeLabel(
      spec.foregroundColorActive(),
      spec.foregroundColorInactive()));

    theme.setImageTheme(SyThemeImage.builder().build());
    return theme;
  }

  private static SyThemeWindowFrame themeWindowFrame(
    final Vector3D color_active_base,
    final Vector3D color_inactive_base,
    final SyThemeEmboss theme_frame_emboss_active,
    final SyThemeEmboss theme_frame_emboss_inactive)
  {
    final SyThemeWindowFrame.Builder theme_frame_b = SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(5);
    theme_frame_b.setTopHeight(5);
    theme_frame_b.setLeftWidth(5);
    theme_frame_b.setRightWidth(5);
    theme_frame_b.setColorActive(color_active_base);
    theme_frame_b.setColorInactive(color_inactive_base);
    theme_frame_b.setTopLeftStyle(FRAME_CORNER_L_PIECE);
    theme_frame_b.setTopRightStyle(FRAME_CORNER_L_PIECE);
    theme_frame_b.setBottomLeftStyle(FRAME_CORNER_L_PIECE);
    theme_frame_b.setBottomRightStyle(FRAME_CORNER_L_PIECE);
    theme_frame_b.setEmbossActive(theme_frame_emboss_active);
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive);
    theme_frame_b.setOutline(SyThemeOutline.of(
      true, true, true, true,
      Vector3D.of(0.0, 0.0, 0.0),
      Vector3D.of(0.3, 0.3, 0.3),
      true));
    return theme_frame_b.build();
  }

  private static SyThemeWindowTitleBar themeTitleBar(
    final Vector3D color_active_base,
    final Vector3D color_active_lighter,
    final Vector3D color_active_darker,
    final Vector3D color_inactive_base,
    final Vector3D color_inactive_lighter,
    final Vector3D color_inactive_darker,
    final Vector3D text_color_active,
    final Vector3D text_color_inactive,
    final SyThemePanel theme_titlebar_panel)
  {
    final SyThemeWindowTitleBar.Builder theme_titlebar_b = SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setPanelTheme(theme_titlebar_panel);

    final SyThemeLabel.Builder theme_titlebar_text_b = SyThemeLabel.builder();
    theme_titlebar_text_b.setTextFont("Monospaced 10");
    theme_titlebar_text_b.setTextColorActive(text_color_active);
    theme_titlebar_text_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setTextAlignment(SyAlignmentHorizontal.ALIGN_CENTER);
    final SyThemePadding zero_padding = SyThemePadding.of(0, 0, 0, 0);
    theme_titlebar_b.setTextPadding(zero_padding);
    theme_titlebar_b.setTextTheme(theme_titlebar_text_b.build());

    theme_titlebar_b.setPanelTheme(theme_titlebar_panel);
    theme_titlebar_b.setButtonPadding(zero_padding);
    theme_titlebar_b.setButtonHeight(16);
    theme_titlebar_b.setButtonWidth(16);
    theme_titlebar_b.setButtonTheme(createThemeTitlebarButton(
      color_active_base,
      color_active_lighter,
      color_active_darker,
      color_inactive_base,
      color_inactive_lighter,
      color_inactive_darker,
      1
    ));
    theme_titlebar_b.setButtonAlignment(SyAlignmentVertical.ALIGN_CENTER);
    theme_titlebar_b.setElementOrder(SyThemeMotive::elementOrder);
    theme_titlebar_b.setHeight(16);
    theme_titlebar_b.setIconPresent(false);
    theme_titlebar_b.setIconHeight(0);
    theme_titlebar_b.setIconWidth(0);
    theme_titlebar_b.setIconTheme(SyThemeImage.builder().build());
    theme_titlebar_b.setIconAlignment(SyAlignmentVertical.ALIGN_CENTER);

    final Vector4D white = Vector4D.of(1.0, 1.0, 1.0, 1.0);
    theme_titlebar_b.setButtonCloseIcon(
      SyImageSpecification.of(
        ICON_CLOSE,
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        white,
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    theme_titlebar_b.setButtonMaximizeIcon(
      SyImageSpecification.of(
        ICON_MAXIMIZE,
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        white,
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST));
    return theme_titlebar_b.build();
  }

  private static SyThemeEmboss themeFrameEmbossActive(
    final Vector3D color_active_lighter,
    final Vector3D color_active_darker)
  {
    final SyThemeEmboss.Builder theme_frame_emboss_active_b = SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(color_active_lighter);
    theme_frame_emboss_active_b.setColorLeft(color_active_lighter);
    theme_frame_emboss_active_b.setColorRight(color_active_darker);
    theme_frame_emboss_active_b.setColorBottom(color_active_darker);
    return theme_frame_emboss_active_b.build();
  }

  private static SyThemeEmboss themeFrameEmbossInactive(
    final Vector3D color_inactive_lighter,
    final Vector3D color_inactive_darker)
  {
    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b = SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_frame_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_frame_emboss_inactive_b.setColorBottom(color_inactive_darker);
    return theme_frame_emboss_inactive_b.build();
  }

  private static SyThemePanel titleBarPanel(
    final Vector3D color_active_base,
    final Vector3D color_inactive_base,
    final SyThemeEmboss theme_titlebar_emboss_active,
    final SyThemeEmboss theme_titlebar_emboss_inactive)
  {
    final SyThemePanel.Builder theme_titlebar_panel_b = SyThemePanel.builder();
    theme_titlebar_panel_b.setFillActive(SyThemeColor.of(color_active_base));
    theme_titlebar_panel_b.setFillInactive(SyThemeColor.of(color_inactive_base));
    theme_titlebar_panel_b.setEmbossActive(theme_titlebar_emboss_active);
    theme_titlebar_panel_b.setEmbossInactive(theme_titlebar_emboss_inactive);
    return theme_titlebar_panel_b.build();
  }

  private static SyThemeEmboss titleBarEmbossInactive(
    final Vector3D color_inactive_lighter,
    final Vector3D color_inactive_darker)
  {
    final SyThemeEmboss.Builder theme_titlebar_emboss_inactive_b = SyThemeEmboss.builder();
    theme_titlebar_emboss_inactive_b.setSize(1);
    theme_titlebar_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_titlebar_emboss_inactive_b.setColorBottom(color_inactive_darker);
    return theme_titlebar_emboss_inactive_b.build();
  }

  private static SyThemeEmboss titleBarEmbossActive(
    final Vector3D color_active_lighter,
    final Vector3D color_active_darker)
  {
    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b = SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(color_active_lighter);
    theme_titlebar_emboss_active_b.setColorLeft(color_active_lighter);
    theme_titlebar_emboss_active_b.setColorRight(color_active_darker);
    theme_titlebar_emboss_active_b.setColorBottom(color_active_darker);
    return theme_titlebar_emboss_active_b.build();
  }

  private static SyThemeMeter createThemeMeter(
    final SyThemeMotiveSpecification spec,
    final Vector3D background_lighter,
    final Vector3D background_darker,
    final Vector3D background_darker_darker)
  {
    final SyThemeMeterOriented.Builder b = SyThemeMeterOriented.builder();

    b.setFillContainerActive(SyThemeColor.of(background_darker_darker));
    b.setEmbossContainerActive(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    b.setFillContainerInactive(SyThemeColor.of(background_darker_darker));
    b.setEmbossContainerInactive(SyThemeEmboss.of(
      background_darker,
      background_lighter,
      background_darker,
      background_lighter,
      1
    ));

    b.setFillIndicatorActive(SyThemeColor.of(spec.foregroundColorActive()));
    b.setEmbossIndicatorActive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    b.setFillIndicatorInactive(SyThemeColor.of(spec.foregroundColorInactive()));
    b.setEmbossIndicatorInactive(SyThemeEmboss.of(
      background_lighter,
      background_darker,
      background_lighter,
      background_darker,
      1
    ));

    final SyThemeMeterOriented meter = b.build();
    return SyThemeMeter.of(meter, meter);
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

  private static SyThemeWindowArrangementType arrangeWindowComponents(
    final SyTextMeasurementType measurement,
    final SyWindowReadableType window,
    final PAreaI<SySpaceParentRelativeType> window_box)
  {
    Objects.requireNonNull(measurement, "Text measurement");
    Objects.requireNonNull(window, "Window");
    Objects.requireNonNull(window_box, "Box");

    final SyTheme theme = window.theme();
    final SyThemeWindowType theme_window = theme.windowTheme();

    /*
     * Calculate a frame that borders the entire window, with an exclusion
     * area inside which is placed the titleBar and content.
     */

    final SyThemeWindowFrameType frame_theme = theme_window.frame();

    final PAreaI<SySpaceParentRelativeType> box_frame = window_box;
    final PAreaI<SySpaceParentRelativeType> box_frame_inner_initial =
      PAreasI.hollowOut(
        box_frame,
        frame_theme.leftWidth(),
        frame_theme.rightWidth(),
        frame_theme.topHeight(),
        frame_theme.bottomHeight());

    final PAreaI<SySpaceParentRelativeType> box_frame_inner =
      SyThemeOutlines.scaleForOutlineOptional(
        box_frame_inner_initial,
        frame_theme.outline());

    /*
     * Calculate a titleBar that appears at the top of the inside of the
     * frame.
     */

    final SyThemeWindowTitleBarType titlebar_theme = theme_window.titleBar();

    final PAreaI<SySpaceParentRelativeType> box_titlebar =
      PAreasI.create(
        box_frame_inner.minimumX(),
        box_frame_inner.minimumY(),
        box_frame_inner.sizeX(),
        titlebar_theme.height());

    /*
     * The content area is whatever space is left over.
     */

    final PAreaI<SySpaceParentRelativeType> box_content = PAreaI.of(
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

  private static SyThemePanel createThemePanel(
    final Vector3D background,
    final Vector3D background_lighter,
    final Vector3D background_darker)
  {
    final SyThemePanel.Builder b = SyThemePanel.builder();
    b.setFillActive(SyThemeColor.of(background));
    b.setFillInactive(SyThemeColor.of(background));
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

  private static SyThemeLabel createThemeLabel(
    final Vector3D foreground_active,
    final Vector3D foreground_inactive)
  {
    final SyThemeLabel.Builder b = SyThemeLabel.builder();
    b.setTextColorActive(foreground_active);
    b.setTextColorInactive(foreground_inactive);
    b.setTextFont("Monospaced-plain-10");
    return b.build();
  }

  private static SyThemeButtonRepeating createThemeButtonRepeating(
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
      theme_button_b.setOutline(Optional.of(SyThemeOutline.of(
        true, true, true, true,
        background_darker,
        background_darker,
        true)));
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

  private static SyThemeButtonCheckbox createThemeButtonCheckbox(
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
      theme_button_b.setOutline(Optional.of(SyThemeOutline.of(
        true, true, true, true,
        background_darker,
        background_darker,
        true)));
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

  private static SyThemeButtonRepeating createThemeTitlebarButton(
    final Vector3D color_active,
    final Vector3D color_active_lighter,
    final Vector3D color_active_darker,
    final Vector3D color_inactive,
    final Vector3D color_inactive_lighter,
    final Vector3D color_inactive_darker,
    final int emboss)
  {
    final SyThemeButtonRepeating.Builder theme_button_b =
      SyThemeButtonRepeating.builder();

    theme_button_b.setFillActive(SyThemeColor.of(color_active));
    theme_button_b.setEmbossActive(SyThemeEmboss.of(
      color_active_lighter,
      color_active_darker,
      color_active_lighter,
      color_active_darker,
      emboss
    ));

    theme_button_b.setFillInactive(SyThemeColor.of(color_inactive));
    theme_button_b.setEmbossInactive(SyThemeEmboss.of(
      color_inactive_lighter,
      color_inactive_darker,
      color_inactive_lighter,
      color_inactive_darker,
      emboss
    ));

    theme_button_b.setFillOver(SyThemeColor.of(color_active));
    theme_button_b.setEmbossOver(SyThemeEmboss.of(
      color_active_lighter,
      color_active_darker,
      color_active_lighter,
      color_active_darker,
      emboss
    ));

    theme_button_b.setFillPressed(SyThemeColor.of(color_active_darker));
    theme_button_b.setEmbossPressed(SyThemeEmboss.of(
      color_active,
      color_active_darker,
      color_active,
      color_active_darker,
      emboss
    ));

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
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return builderFrom(SyThemeMotiveSpecification.builder().build());
  }
}
