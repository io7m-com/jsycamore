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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.menus.SyMenuItemType;
import com.io7m.jsycamore.api.components.SyTextViewReadableType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;

import java.util.Objects;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_BACKGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_FOREGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_FOREGROUND_INACTIVE;

/**
 * A theme component for menu text views.
 */

public final class SyPrimalMenuItemTextView extends SyPrimalAbstract
{
  /**
   * A theme component for menu text views.
   *
   * @param inTheme The theme
   */

  public SyPrimalMenuItemTextView(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
  }

  @Override
  public SyRenderNodeType render(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    final var area =
      component.boundingArea();

    if (component instanceof SyTextViewReadableType textView) {
      final var theme = this.theme();
      final var values = theme.values();

      final var menuItemOpt =
        textView.ancestorMatchingReadable(c -> c instanceof SyMenuItemType)
          .map(SyMenuItemType.class::cast);

      if (menuItemOpt.isEmpty()) {
        return SyRenderNodeNoop.noop();
      }

      final var menuItem = menuItemOpt.get();

      try {
        final var textFont =
          this.font(context, component);
        final var text =
          textView.text().get();
        final var textSize =
          PAreaSizeI.<SySpaceComponentRelativeType>of(area.sizeX(), area.sizeY());

        if (!menuItem.isActive()) {
          return new SyRenderNodeText(
            values.fillFlat(PRIMARY_FOREGROUND_INACTIVE),
            textSize,
            textFont,
            text
          );
        }

        if (menuItem.isMouseOver()) {
          return new SyRenderNodeText(
            values.fillFlat(PRIMARY_BACKGROUND),
            textSize,
            textFont,
            text
          );
        }

        return new SyRenderNodeText(
          values.fillFlat(PRIMARY_FOREGROUND),
          textSize,
          textFont,
          text
        );
      } catch (final SyThemeValueException e) {
        throw new IllegalStateException(e);
      }
    }

    return SyRenderNodeNoop.noop();
  }
}
