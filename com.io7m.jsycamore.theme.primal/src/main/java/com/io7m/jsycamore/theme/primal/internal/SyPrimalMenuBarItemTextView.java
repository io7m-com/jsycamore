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
import com.io7m.jsycamore.api.menus.SyMenuBarItemType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Objects;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_BACKGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_FOREGROUND;

/**
 * A theme component for menu text views.
 */

public final class SyPrimalMenuBarItemTextView extends SyPrimalAbstract
{
  /**
   * A theme component for menu text views.
   *
   * @param inTheme The theme
   */

  public SyPrimalMenuBarItemTextView(
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

    if (component instanceof final SyTextViewReadableType textView) {
      final var theme = this.theme();
      final var values = theme.values();

      final var parentNodeOpt =
        component.nodeReadable().parentReadable();

      if (parentNodeOpt.isEmpty()) {
        return SyRenderNodeNoop.noop();
      }

      final var parentComponent =
        parentNodeOpt.get().value();

      if (parentComponent instanceof final SyMenuBarItemType barItem) {
        try {
          final var size =
            PAreasI.<SySpaceComponentRelativeType>size(
              PAreasI.cast(textView.boundingArea()));

          final var textFont =
            this.font(context, component);
          final var text =
            textView.text().get();

          return switch (barItem.selected()) {
            case MENU_SELECTED -> {
              yield new SyRenderNodeText(
                "MenuBarItemTextSelected",
                PVectors2I.zero(),
                size,
                values.fillFlat(PRIMARY_BACKGROUND),
                textFont,
                text
              );
            }
            case MENU_NOT_SELECTED -> {
              yield new SyRenderNodeText(
                "MenuBarItemTextNotSelected",
                PVectors2I.zero(),
                size,
                values.fillFlat(PRIMARY_FOREGROUND),
                textFont,
                text
              );
            }
          };
        } catch (final SyThemeValueException e) {
          throw new IllegalStateException(e);
        }
      }
    }

    return SyRenderNodeNoop.noop();
  }
}
