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
import com.io7m.jsycamore.api.components.SyTextAreaReadableType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_EDGE;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.TEXT_AREA_BACKGROUND;

/**
 * A theme component for text areas.
 */

public final class SyPrimalTextArea extends SyPrimalAbstract
{
  /**
   * A theme component for text areas.
   *
   * @param inTheme The theme
   */

  public SyPrimalTextArea(
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

    if (component instanceof final SyTextAreaReadableType textArea) {
      try {
        final var area =
          component.boundingArea();
        final var rectangle =
          new SyShapeRectangle<SySpaceComponentRelativeType>(
            PAreasI.cast(PAreasI.moveToOrigin(area))
          );

        final var values =
          this.theme().values();

        final var mainFill =
          new SyRenderNodeShape(
            "TextAreaFill",
            PVectors2I.zero(),
            Optional.empty(),
            Optional.of(values.fillFlat(TEXT_AREA_BACKGROUND)),
            rectangle
          );

        final var mainEdge =
          new SyRenderNodeShape(
            "TextAreaEdge",
            PVectors2I.zero(),
            Optional.of(values.edgeFlat(PRIMARY_EDGE)),
            Optional.empty(),
            rectangle
          );

        return SyRenderNodeComposite.composite(
          "TextAreaComposite",
          mainFill,
          mainEdge
        );
      } catch (final SyThemeValueException e) {
        throw new IllegalStateException(e);
      }
    }

    return SyRenderNodeNoop.noop();
  }
}
