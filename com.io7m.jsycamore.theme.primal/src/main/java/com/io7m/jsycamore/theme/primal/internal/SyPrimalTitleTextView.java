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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyTextViewReadableType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.text.SyFontException;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Objects;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_FOREGROUND;

/**
 * A theme component for text views.
 */

public final class SyPrimalTitleTextView extends SyPrimalAbstract
{
  /**
   * A theme component for text views.
   *
   * @param inTheme The theme
   */

  public SyPrimalTitleTextView(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
  }

  @Override
  public SyFontType font(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    try {
      return context.fonts()
        .get(this.theme().values().font(SyPrimalValues.WINDOW_TITLE_TEXT_FONT));
    } catch (final SyThemeValueException | SyFontException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public SyRenderNodeType render(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    if (component instanceof final SyTextViewReadableType textView) {
      final var theme = this.theme();
      try {
        final var size =
          PAreasI.<SySpaceComponentRelativeType>size(
            PAreasI.cast(textView.boundingArea()));

        return new SyRenderNodeText(
          "TitleTextViewText",
          PVectors2I.zero(),
          size,
          theme.values().fillFlat(PRIMARY_FOREGROUND),
          this.font(context, component),
          textView.text().get()
        );
      } catch (final SyThemeValueException e) {
        throw new IllegalStateException(e);
      }
    }

    return SyRenderNodeNoop.noop();
  }
}
