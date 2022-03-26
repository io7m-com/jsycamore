/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.theme.primal;

import com.io7m.jsycamore.api.colors.SyColors;
import com.io7m.jsycamore.api.themes.SyThemeFactoryType;
import com.io7m.jsycamore.api.themes.SyThemeParameterType.SyParameterColorRGBA;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.theme.primal.internal.SyThemePrimal;

import static com.io7m.jsycamore.api.colors.SyColors.darker;
import static com.io7m.jsycamore.api.colors.SyColors.lighter;
import static com.io7m.jsycamore.api.themes.SyThemeParameterType.SyParameterString;

/**
 * The Primal theme.
 */

public final class SyThemePrimalFactory implements SyThemeFactoryType
{
  /**
   * The default background fill for components.
   */

  public static final SyParameterColorRGBA BACKGROUND_FILL =
    new SyParameterColorRGBA(
      "background_fill",
      "The default background fill for components.",
      SyColors.fromHex4(0x31, 0x50, 0x7D, 0xff)
    );

  /**
   * The default text color for components.
   */

  public static final SyParameterColorRGBA TEXT_COLOR =
    new SyParameterColorRGBA(
      "text_color",
      "The default text color for components.",
      SyColors.whiteOpaque()
    );

  /**
   * The default edge color for components.
   */

  public static final SyParameterColorRGBA EDGE =
    new SyParameterColorRGBA(
      "edge",
      "The default edge color for components.",
      lighter(lighter(lighter(BACKGROUND_FILL.value())))
    );

  /**
   * The default background fill for components when the mouse cursor is over
   * the component.
   */

  public static final SyParameterColorRGBA BACKGROUND_OVER_FILL =
    new SyParameterColorRGBA(
      "background_over_fill",
      "The default background fill for components when the mouse cursor is over the component.",
      lighter(BACKGROUND_FILL.value())
    );

  /**
   * The default background fill for buttons when the button is pressed.
   */

  public static final SyParameterColorRGBA BUTTON_PRESSED_FILL =
    new SyParameterColorRGBA(
      "button_pressed_fill",
      "The default background fill for buttons when the button is pressed.",
      darker(BACKGROUND_FILL.value())
    );

  /**
   * The default background fill for disabled components.
   */

  public static final SyParameterColorRGBA BACKGROUND_DISABLED_FILL =
    new SyParameterColorRGBA(
      "background_disabled_fill",
      "The default background fill for disabled components.",
      SyColors.fromHex4(0x50, 0x50, 0x50, 0xff)
    );

  /**
   * The default background fill for unmatched components.
   */

  public static final SyParameterColorRGBA UNMATCHED_FILL =
    new SyParameterColorRGBA(
      "unmatched_fill",
      "The default background fill for unmatched components.",
      BACKGROUND_FILL.value()
    );

  /**
   * The default text font.
   */

  public static final SyParameterString TEXT_FONT =
    new SyParameterString(
      "text_font",
      "The default text font.",
      "SansSerif 12"
    );

  /**
   * The "Primal" theme.
   */

  public SyThemePrimalFactory()
  {

  }

  @Override
  public SyThemeType create()
  {
    return new SyThemePrimal();
  }
}
