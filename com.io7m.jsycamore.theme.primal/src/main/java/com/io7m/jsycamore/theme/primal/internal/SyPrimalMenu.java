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
import com.io7m.jsycamore.api.rendering.SyEmbossedRectangle;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_EAST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_NORTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_SOUTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_WEST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_BACKGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_EDGE;

/**
 * A theme component for menu views.
 */

public final class SyPrimalMenu extends SyPrimalAbstract
{
  /**
   * A theme component for menu views.
   *
   * @param inTheme The theme
   */

  public SyPrimalMenu(
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
    final var rectangle =
      new SyShapeRectangle<SySpaceComponentRelativeType>(
        PAreasI.cast(PAreasI.moveToOrigin(area))
      );

    try {
      return this.emboss(rectangle);
    } catch (SyThemeValueException e) {
      throw new IllegalStateException(e);
    }
  }

  private SyRenderNodeType emboss(
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    final var values = this.theme().values();

    final var embossed =
      SyEmbossedRectangle.emboss(
        rectangle.area(),
        values.fillFlat(EMBOSS_NORTH),
        values.fillFlat(EMBOSS_EAST),
        values.fillFlat(EMBOSS_SOUTH),
        values.fillFlat(EMBOSS_WEST),
        1,
        1
      );

    final SyRenderNodeShape embossW =
      new SyRenderNodeShape(
        Optional.empty(),
        Optional.of(embossed.fillWest()),
        embossed.shapeWest()
      );

    final SyRenderNodeShape embossS =
      new SyRenderNodeShape(
        Optional.empty(),
        Optional.of(embossed.fillSouth()),
        embossed.shapeSouth()
      );

    final SyRenderNodeShape embossN =
      new SyRenderNodeShape(
        Optional.empty(),
        Optional.of(embossed.fillNorth()),
        embossed.shapeNorth()
      );

    final SyRenderNodeShape embossE =
      new SyRenderNodeShape(
        Optional.empty(),
        Optional.of(embossed.fillEast()),
        embossed.shapeEast()
      );

    final var mainFill =
      new SyRenderNodeShape(
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_BACKGROUND)),
        rectangle
      );

    final var mainEdge =
      new SyRenderNodeShape(
        Optional.of(values.edgeFlat(PRIMARY_EDGE)),
        Optional.empty(),
        rectangle
      );

    return SyRenderNodeComposite.composite(
      mainFill, embossN, embossE, embossS, embossW, mainEdge
    );
  }
}
