/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.theme.primal.internal;

import com.io7m.jsycamore.api.colors.SyColors;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jsycamore.api.themes.SyThemeValuesType;
import com.io7m.jsycamore.theme.spi.SyThemeValues;

import static com.io7m.jsycamore.api.colors.SyColors.darker;
import static com.io7m.jsycamore.api.colors.SyColors.lighter;
import static com.io7m.jsycamore.api.text.SyFontStyle.BOLD;
import static com.io7m.jsycamore.api.text.SyFontStyle.REGULAR;

/**
 * The value for the Primal theme.
 */

public final class SyPrimalValues
{
  /**
   * A value name.
   */
  public static final String BUTTON_EMBOSS_THICKNESS = "button_emboss_thickness";
  /**
   * A value name.
   */
  public static final String BUTTON_PRESSED = "button_pressed";
  /**
   * A value name.
   */
  public static final String EMBOSS_EAST = "emboss_east";
  /**
   * A value name.
   */
  public static final String EMBOSS_INACTIVE_EAST = "emboss_inactive_east";
  /**
   * A value name.
   */
  public static final String EMBOSS_INACTIVE_NORTH = "emboss_inactive_north";
  /**
   * A value name.
   */
  public static final String EMBOSS_INACTIVE_SOUTH = "emboss_inactive_south";
  /**
   * A value name.
   */
  public static final String EMBOSS_INACTIVE_WEST = "emboss_inactive_west";
  /**
   * A value name.
   */
  public static final String EMBOSS_NORTH = "emboss_north";
  /**
   * A value name.
   */
  public static final String EMBOSS_SOUTH = "emboss_south";
  /**
   * A value name.
   */
  public static final String EMBOSS_WEST = "emboss_west";
  /**
   * A value name.
   */
  public static final String PRIMARY_BACKGROUND = "primary_background";
  /**
   * A value name.
   */
  public static final String PRIMARY_EDGE = "primary_edge";
  /**
   * A value name.
   */
  public static final String PRIMARY_FOREGROUND = "primary_foreground";
  /**
   * A value name.
   */
  public static final String PRIMARY_INACTIVE = "primary_inactive";
  /**
   * A value name.
   */
  public static final String PRIMARY_INACTIVE_EDGE = "primary_inactive_edge";
  /**
   * A value name.
   */
  public static final String PRIMARY_OVER = "primary_over";
  /**
   * A value name.
   */
  public static final String TEXT_FONT = "text_font";
  /**
   * A value name.
   */
  public static final String UNMATCHED = "unmatched";
  /**
   * A value name.
   */
  public static final String WINDOW_BUTTON_EMBOSS_THICKNESS = "window_button_emboss_thickness";
  /**
   * A value name.
   */
  public static final String WINDOW_TITLE_TEXT_FONT = "window_title_text_font";

  private SyPrimalValues()
  {

  }

  /**
   * @return A set of theme values
   */

  public static SyThemeValuesType create()
  {
    try {
      final var builder = SyThemeValues.builder();

      builder.createConstantColor4D(
        PRIMARY_BACKGROUND,
        "The background color for components.",
        SyColors.fromHex4(0x31, 0x50, 0x7D, 0xff)
      );

      builder.createConstantFont(
        TEXT_FONT,
        "The primary text font.",
        new SyFontDescription("Dialog", REGULAR, 12)
      );

      builder.createConstantFont(
        WINDOW_TITLE_TEXT_FONT,
        "The window title text font.",
        new SyFontDescription("Dialog", BOLD, 10)
      );

      builder.createConstantColor4D(
        PRIMARY_FOREGROUND,
        "The foreground color for components.",
        SyColors.whiteOpaque()
      );

      builder.createFunctionColor4D(
        PRIMARY_EDGE,
        "The edge color for components.",
        PRIMARY_BACKGROUND,
        SyColors::darker
      );

      builder.createFunctionColor4D(
        PRIMARY_INACTIVE,
        "The background color for inactive components.",
        PRIMARY_BACKGROUND,
        SyColors::desaturated
      );

      builder.createFunctionColor4D(
        PRIMARY_OVER,
        "The color for components that are highlighted by the mouse cursor.",
        PRIMARY_BACKGROUND,
        SyColors::lighter
      );

      builder.createFunctionColor4D(
        PRIMARY_INACTIVE_EDGE,
        "The edge color for inactive components.",
        PRIMARY_BACKGROUND,
        SyColors::desaturated
      );

      builder.createFunctionColor4D(
        EMBOSS_NORTH,
        "The emboss north color for components.",
        PRIMARY_BACKGROUND,
        c -> lighter(lighter(c))
      );

      builder.createFunctionColor4D(
        EMBOSS_EAST,
        "The emboss east color for components.",
        PRIMARY_BACKGROUND,
        SyColors::lighter
      );

      builder.createFunctionColor4D(
        EMBOSS_SOUTH,
        "The emboss south color for components.",
        PRIMARY_BACKGROUND,
        c -> darker(darker(c))
      );

      builder.createFunctionColor4D(
        EMBOSS_WEST,
        "The emboss west color for components.",
        PRIMARY_BACKGROUND,
        SyColors::darker
      );

      builder.createFunctionColor4D(
        EMBOSS_INACTIVE_NORTH,
        "The emboss north color for inactive components.",
        EMBOSS_NORTH,
        SyColors::desaturated
      );

      builder.createFunctionColor4D(
        EMBOSS_INACTIVE_SOUTH,
        "The emboss south color for inactive components.",
        EMBOSS_SOUTH,
        SyColors::desaturated
      );

      builder.createFunctionColor4D(
        EMBOSS_INACTIVE_WEST,
        "The emboss west color for inactive components.",
        EMBOSS_WEST,
        SyColors::desaturated
      );

      builder.createFunctionColor4D(
        EMBOSS_INACTIVE_EAST,
        "The emboss east color for inactive components.",
        EMBOSS_EAST,
        SyColors::desaturated
      );

      builder.createFunctionColor4D(
        BUTTON_PRESSED,
        "The color for pressed buttons.",
        PRIMARY_BACKGROUND,
        SyColors::darker
      );

      builder.createConstantInteger(
        BUTTON_EMBOSS_THICKNESS,
        "The emboss thickness for buttons.",
        2
      );

      builder.createConstantInteger(
        WINDOW_BUTTON_EMBOSS_THICKNESS,
        "The emboss thickness for window buttons.",
        1
      );

      builder.createConstantColor4D(
        UNMATCHED,
        "The color for unmatched components.",
        SyColors.fromHex4(0xff, 0x00, 0xff, 0xff)
      );

      return builder.create();
    } catch (final SyThemeValueException e) {
      throw new IllegalStateException(e);
    }
  }
}
