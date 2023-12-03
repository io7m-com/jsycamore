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
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyTextMultiLineViewReadableType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType.SyTextSelectionIsSelected;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType.SyTextSelectionNotSelected;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_FOREGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.TEXT_SELECTION_BACKGROUND;

/**
 * A theme component for multi-line text views.
 */

public final class SyPrimalTextMultilineView extends SyPrimalAbstract
{
  /**
   * A theme component for multi-line text views.
   *
   * @param inTheme The theme
   */

  public SyPrimalTextMultilineView(
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

    final var textSelections =
      context.textSelection();

    if (component instanceof final SyTextMultiLineViewReadableType textView) {
      final var theme = this.theme();
      try {
        final var textColor =
          theme.values().fillFlat(PRIMARY_FOREGROUND);
        final var textSelectedBackground =
          theme.values().fillFlat(TEXT_SELECTION_BACKGROUND);

        final var font =
          this.font(context, component);
        final var texts =
          textView.textsByYOffset();
        final var textViewSize =
          textView.size().get();

        final var nodes =
          new ArrayList<SyRenderNodeType>(texts.size());

        /*
         * Render a background behind all text selection regions.
         */

        switch (textSelections.isComponentSelected(textView)) {
          case final SyTextSelectionIsSelected selected -> {
            final var selection =
              selected.selection();

            for (final var region : selection.regions()) {
              final PVector2I<SySpaceComponentRelativeType> position =
                PVector2I.of(region.minimumX(), region.minimumY());

              nodes.add(
                new SyRenderNodeShape(
                  "TextMultiLineSelectionRegion",
                  position,
                  Optional.empty(),
                  Optional.of(textSelectedBackground),
                  new SyShapeRectangle<>(
                    PAreasI.cast(PAreasI.moveToOrigin(region))
                  )
                )
              );
            }
          }
          case final SyTextSelectionNotSelected not -> {

          }
        }

        /*
         * Create a text node for each line of text.
         */

        for (final var entry : texts.entrySet()) {
          final var yOffset =
            entry.getKey();
          final var line =
            entry.getValue();
          final var lineSize =
            line.textBounds();
          final PVector2I<SySpaceComponentRelativeType> position =
            PVector2I.of(0, yOffset.intValue());

          final PAreaSizeI<SySpaceComponentRelativeType> size =
            PAreaSizeI.<SySpaceComponentRelativeType>of(
              textViewSize.sizeX(),
              lineSize.sizeY()
            );

          nodes.add(
            new SyRenderNodeText(
              "TextMultiLineViewText",
              position,
              size,
              textColor,
              font,
              line.text()
            )
          );
        }

        return new SyRenderNodeComposite("TextMultilineViewComposite", nodes);
      } catch (final SyThemeValueException e) {
        throw new IllegalStateException(e);
      }
    }

    return SyRenderNodeNoop.noop();
  }
}
