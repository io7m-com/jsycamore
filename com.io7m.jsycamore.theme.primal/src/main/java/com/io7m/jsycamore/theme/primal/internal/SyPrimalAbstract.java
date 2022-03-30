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

import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.themes.SyThemeComponentType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;

import java.util.Objects;

/**
 * The base type of theme components in the Primal theme.
 */

public abstract class SyPrimalAbstract implements SyThemeComponentType
{
  private final SyThemePrimal theme;

  /**
   * The base type of theme components in the Primal theme.
   *
   * @param inTheme The theme
   */

  public SyPrimalAbstract(
    final SyThemePrimal inTheme)
  {
    this.theme =
      Objects.requireNonNull(inTheme, "theme");
  }

  /**
   * @return The theme instance
   */

  public final SyThemePrimal theme()
  {
    return this.theme;
  }

  /**
   * Retrieve a font. This method should be overridden to return specific fonts
   * for specific components. By default, this method just returns the default
   * theme font.
   *
   * @param context   The theme context
   * @param component The component
   *
   * @return A font
   */

  @Override
  public SyFontType font(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    try {
      return context.fonts()
        .get(this.theme.values().font(SyPrimalValues.TEXT_FONT));
    } catch (final SyThemeValueException e) {
      throw new IllegalStateException(e);
    }
  }
}
