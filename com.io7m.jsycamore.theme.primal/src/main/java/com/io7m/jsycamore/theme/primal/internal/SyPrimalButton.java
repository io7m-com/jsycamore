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
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.rendering.SyEmbossedRectangle;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.BUTTON_EMBOSS_THICKNESS;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.BUTTON_PRESSED;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_EAST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_INACTIVE_EAST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_INACTIVE_NORTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_INACTIVE_SOUTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_INACTIVE_WEST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_NORTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_SOUTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_WEST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_BACKGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_EDGE;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_INACTIVE;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_OVER;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.UNMATCHED;

/**
 * A theme component for buttons.
 */

public final class SyPrimalButton extends SyPrimalAbstract
{
  /**
   * A theme component for buttons.
   *
   * @param inTheme The theme
   */

  public SyPrimalButton(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
  }

  private SyRenderNodeType renderForActiveOverButton(
    final SyButtonReadableType button,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    final var values = this.theme().values();
    final var area = rectangle.area();

    if (button.isPressed()) {
      final var embossed =
        SyEmbossedRectangle.emboss(
          area,
          values.fillFlat(EMBOSS_SOUTH),
          values.fillFlat(EMBOSS_WEST),
          values.fillFlat(EMBOSS_NORTH),
          values.fillFlat(EMBOSS_EAST),
          1,
          values.integer(BUTTON_EMBOSS_THICKNESS)
        );

      final SyRenderNodeShape embossW =
        new SyRenderNodeShape(
          "ButtonPressedEmbossW",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillWest()),
          embossed.shapeWest()
        );

      final SyRenderNodeShape embossS =
        new SyRenderNodeShape(
          "ButtonPressedEmbossS",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillSouth()),
          embossed.shapeSouth()
        );

      final SyRenderNodeShape embossN =
        new SyRenderNodeShape(
          "ButtonPressedEmbossN",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillNorth()),
          embossed.shapeNorth()
        );

      final SyRenderNodeShape embossE =
        new SyRenderNodeShape(
          "ButtonPressedEmbossE",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillEast()),
          embossed.shapeEast()
        );

      final var mainFill =
        new SyRenderNodeShape(
          "ButtonPressedMainFill",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(values.fillFlat(BUTTON_PRESSED)),
          rectangle
        );

      final var mainEdge =
        new SyRenderNodeShape(
          "ButtonPressedMainEdge",
          PVectors2I.zero(),
          Optional.of(values.edgeFlat(PRIMARY_EDGE)),
          Optional.empty(),
          rectangle
        );

      return SyRenderNodeComposite.composite(
        "ButtonPressedComposite",
        mainFill, embossN, embossE, embossS, embossW, mainEdge
      );
    }

    final var embossed =
      SyEmbossedRectangle.emboss(
        area,
        values.fillFlat(EMBOSS_NORTH),
        values.fillFlat(EMBOSS_EAST),
        values.fillFlat(EMBOSS_SOUTH),
        values.fillFlat(EMBOSS_WEST),
        1,
        values.integer(BUTTON_EMBOSS_THICKNESS)
      );

    final SyRenderNodeShape embossW =
      new SyRenderNodeShape(
        "ButtonOverEmbossW",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillWest()),
        embossed.shapeWest()
      );

    final SyRenderNodeShape embossS =
      new SyRenderNodeShape(
        "ButtonOverEmbossS",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillSouth()),
        embossed.shapeSouth()
      );

    final SyRenderNodeShape embossN =
      new SyRenderNodeShape(
        "ButtonOverEmbossN",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillNorth()),
        embossed.shapeNorth()
      );

    final SyRenderNodeShape embossE =
      new SyRenderNodeShape(
        "ButtonOverEmbossE",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillEast()),
        embossed.shapeEast()
      );

    final var mainFill =
      new SyRenderNodeShape(
        "ButtonOverMainFill",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_OVER)),
        rectangle
      );

    final var mainEdge =
      new SyRenderNodeShape(
        "ButtonOverMainEdge",
        PVectors2I.zero(),
        Optional.of(values.edgeFlat(PRIMARY_EDGE)),
        Optional.empty(),
        rectangle
      );

    return SyRenderNodeComposite.composite(
      "ButtonOverComposite",
      mainFill, embossN, embossE, embossS, embossW, mainEdge
    );
  }

  private SyRenderNodeType renderForActiveNotOverButton(
    final SyButtonReadableType button,
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
        values.integer(BUTTON_EMBOSS_THICKNESS)
      );

    final SyRenderNodeShape embossW =
      new SyRenderNodeShape(
        "ButtonEmbossW",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillWest()),
        embossed.shapeWest()
      );

    final SyRenderNodeShape embossS =
      new SyRenderNodeShape(
        "ButtonEmbossS",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillSouth()),
        embossed.shapeSouth()
      );

    final SyRenderNodeShape embossN =
      new SyRenderNodeShape(
        "ButtonEmbossN",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillNorth()),
        embossed.shapeNorth()
      );

    final SyRenderNodeShape embossE =
      new SyRenderNodeShape(
        "ButtonEmbossE",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillEast()),
        embossed.shapeEast()
      );

    final var mainFill =
      new SyRenderNodeShape(
        "ButtonMainFill",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_BACKGROUND)),
        rectangle
      );

    final var mainEdge =
      new SyRenderNodeShape(
        "ButtonMainEdge",
        PVectors2I.zero(),
        Optional.of(values.edgeFlat(PRIMARY_EDGE)),
        Optional.empty(),
        rectangle
      );

    return SyRenderNodeComposite.composite(
      "ButtonMainComposite",
      mainFill, embossN, embossE, embossS, embossW, mainEdge
    );
  }

  private SyRenderNodeType renderForActiveNotOver(
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    if (component instanceof final SyButtonReadableType button) {
      return this.renderForActiveNotOverButton(button, rectangle);
    }

    final var values = this.theme().values();
    return new SyRenderNodeShape(
      "ButtonUnmatched",
      PVectors2I.zero(),
      Optional.of(values.edgeFlat(PRIMARY_EDGE)),
      Optional.of(values.fillFlat(UNMATCHED)),
      rectangle
    );
  }

  private SyRenderNodeType renderForActiveOver(
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    if (component instanceof final SyButtonReadableType button) {
      return this.renderForActiveOverButton(button, rectangle);
    }

    final var values = this.theme().values();
    return new SyRenderNodeShape(
      "ButtonUnmatched",
      PVectors2I.zero(),
      Optional.of(values.edgeFlat(PRIMARY_EDGE)),
      Optional.of(values.fillFlat(UNMATCHED)),
      rectangle
    );
  }

  private SyRenderNodeType renderForActive(
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    if (component.isMouseOver()) {
      return this.renderForActiveOver(component, rectangle);
    }
    return this.renderForActiveNotOver(component, rectangle);
  }

  @Override
  public SyRenderNodeType render(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    try {
      final var area =
        component.boundingArea();
      final var rectangle =
        new SyShapeRectangle<SySpaceComponentRelativeType>(
          PAreasI.cast(PAreasI.moveToOrigin(area))
        );

      if (component.isActive()) {
        return this.renderForActive(component, rectangle);
      }

      return this.renderNotActive(rectangle);
    } catch (final SyThemeValueException e) {
      throw new IllegalStateException(e);
    }
  }

  private SyRenderNodeType renderNotActive(
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    final var values = this.theme().values();

    final var embossed =
      SyEmbossedRectangle.emboss(
        rectangle.area(),
        values.fillFlat(EMBOSS_INACTIVE_NORTH),
        values.fillFlat(EMBOSS_INACTIVE_EAST),
        values.fillFlat(EMBOSS_INACTIVE_SOUTH),
        values.fillFlat(EMBOSS_INACTIVE_WEST),
        1,
        values.integer(BUTTON_EMBOSS_THICKNESS)
      );

    final SyRenderNodeShape embossW =
      new SyRenderNodeShape(
        "ButtonInactiveEmbossW",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillWest()),
        embossed.shapeWest()
      );

    final SyRenderNodeShape embossS =
      new SyRenderNodeShape(
        "ButtonInactiveEmbossS",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillSouth()),
        embossed.shapeSouth()
      );

    final SyRenderNodeShape embossN =
      new SyRenderNodeShape(
        "ButtonInactiveEmbossN",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillNorth()),
        embossed.shapeNorth()
      );

    final SyRenderNodeShape embossE =
      new SyRenderNodeShape(
        "ButtonInactiveEmbossE",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillEast()),
        embossed.shapeEast()
      );

    final var mainFill =
      new SyRenderNodeShape(
        "ButtonInactiveMainFill",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_INACTIVE)),
        rectangle
      );

    final var mainEdge =
      new SyRenderNodeShape(
        "ButtonInactiveMainEdge",
        PVectors2I.zero(),
        Optional.of(values.edgeFlat(PRIMARY_EDGE)),
        Optional.empty(),
        rectangle
      );

    return SyRenderNodeComposite.composite(
      "ButtonInactiveComposite",
      mainFill, embossN, embossE, embossS, embossW, mainEdge
    );
  }
}
