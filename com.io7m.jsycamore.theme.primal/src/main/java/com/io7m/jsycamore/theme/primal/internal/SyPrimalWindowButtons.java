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

import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.rendering.SyEmbossedRectangle;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import com.io7m.junreachable.UnimplementedCodeException;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.BUTTON_PRESSED;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_EAST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_NORTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_SOUTH;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.EMBOSS_WEST;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_BACKGROUND;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_EDGE;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_OVER;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.UNMATCHED;
import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.WINDOW_BUTTON_EMBOSS_THICKNESS;

/**
 * Functions to render window buttons.
 */

public final class SyPrimalWindowButtons
{
  private SyPrimalWindowButtons()
  {

  }

  private static SyRenderNodeType renderForActiveOverButton(
    final SyThemePrimal theme,
    final SyButtonReadableType button,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    final var values = theme.values();
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
          values.integer(WINDOW_BUTTON_EMBOSS_THICKNESS)
        );

      final SyRenderNodeShape embossW =
        new SyRenderNodeShape(
          "WindowButtonPressedEmbossW",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillWest()),
          embossed.shapeWest()
        );

      final SyRenderNodeShape embossS =
        new SyRenderNodeShape(
          "WindowButtonPressedEmbossS",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillSouth()),
          embossed.shapeSouth()
        );

      final SyRenderNodeShape embossN =
        new SyRenderNodeShape(
          "WindowButtonPressedEmbossN",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillNorth()),
          embossed.shapeNorth()
        );

      final SyRenderNodeShape embossE =
        new SyRenderNodeShape(
          "WindowButtonPressedEmbossE",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(embossed.fillEast()),
          embossed.shapeEast()
        );

      final var mainFill =
        new SyRenderNodeShape(
          "WindowButtonPressedFill",
          PVectors2I.zero(),
          Optional.empty(),
          Optional.of(values.fillFlat(BUTTON_PRESSED)),
          rectangle
        );

      final var mainEdge =
        new SyRenderNodeShape(
          "WindowButtonPressedEdge",
          PVectors2I.zero(),
          Optional.of(values.edgeFlat(PRIMARY_EDGE)),
          Optional.empty(),
          rectangle
        );

      return SyRenderNodeComposite.composite(
        "WindowButtonPressedComposite",
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
        values.integer(WINDOW_BUTTON_EMBOSS_THICKNESS)
      );

    final SyRenderNodeShape embossW =
      new SyRenderNodeShape(
        "WindowButtonOverEmbossW",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillWest()),
        embossed.shapeWest()
      );

    final SyRenderNodeShape embossS =
      new SyRenderNodeShape(
        "WindowButtonOverEmbossS",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillSouth()),
        embossed.shapeSouth()
      );

    final SyRenderNodeShape embossN =
      new SyRenderNodeShape(
        "WindowButtonOverEmbossN",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillNorth()),
        embossed.shapeNorth()
      );

    final SyRenderNodeShape embossE =
      new SyRenderNodeShape(
        "WindowButtonOverEmbossE",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillEast()),
        embossed.shapeEast()
      );

    final var mainFill =
      new SyRenderNodeShape(
        "WindowButtonOverFill",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_OVER)),
        rectangle
      );

    final var mainEdge =
      new SyRenderNodeShape(
        "WindowButtonOverEdge",
        PVectors2I.zero(),
        Optional.of(values.edgeFlat(PRIMARY_EDGE)),
        Optional.empty(),
        rectangle
      );

    return SyRenderNodeComposite.composite(
      "WindowButtonOverComposite",
      mainFill, embossN, embossE, embossS, embossW, mainEdge
    );
  }

  private static SyRenderNodeType renderForActiveNotOverButton(
    final SyThemePrimal theme,
    final SyButtonReadableType button,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    final var values = theme.values();

    final var embossed =
      SyEmbossedRectangle.emboss(
        rectangle.area(),
        values.fillFlat(EMBOSS_NORTH),
        values.fillFlat(EMBOSS_EAST),
        values.fillFlat(EMBOSS_SOUTH),
        values.fillFlat(EMBOSS_WEST),
        1,
        values.integer(WINDOW_BUTTON_EMBOSS_THICKNESS)
      );

    final SyRenderNodeShape embossW =
      new SyRenderNodeShape(
        "WindowButtonEmbossW",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillWest()),
        embossed.shapeWest()
      );

    final SyRenderNodeShape embossS =
      new SyRenderNodeShape(
        "WindowButtonEmbossS",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillSouth()),
        embossed.shapeSouth()
      );

    final SyRenderNodeShape embossN =
      new SyRenderNodeShape(
        "WindowButtonEmbossN",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillNorth()),
        embossed.shapeNorth()
      );

    final SyRenderNodeShape embossE =
      new SyRenderNodeShape(
        "WindowButtonEmbossE",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(embossed.fillEast()),
        embossed.shapeEast()
      );

    final var mainFill =
      new SyRenderNodeShape(
        "WindowButtonFill",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_BACKGROUND)),
        rectangle
      );

    final var mainEdge =
      new SyRenderNodeShape(
        "WindowButtonEdge",
        PVectors2I.zero(),
        Optional.of(values.edgeFlat(PRIMARY_EDGE)),
        Optional.empty(),
        rectangle
      );

    return SyRenderNodeComposite.composite(
      "WindowButtonComposite",
      mainFill, embossN, embossE, embossS, embossW, mainEdge
    );
  }

  private static SyRenderNodeType renderForActiveNotOver(
    final SyThemePrimal theme,
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    if (component instanceof final SyButtonReadableType button) {
      return renderForActiveNotOverButton(theme, button, rectangle);
    }

    final var values = theme.values();
    return new SyRenderNodeShape(
      "UNMATCHED",
      PVectors2I.zero(),
      Optional.of(values.edgeFlat(PRIMARY_EDGE)),
      Optional.of(values.fillFlat(UNMATCHED)),
      rectangle
    );
  }

  private static SyRenderNodeType renderForActiveOver(
    final SyThemePrimal theme,
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    if (component instanceof final SyButtonReadableType button) {
      return renderForActiveOverButton(theme, button, rectangle);
    }

    final var values = theme.values();
    return new SyRenderNodeShape(
      "UNMATCHED",
      PVectors2I.zero(),
      Optional.of(values.edgeFlat(PRIMARY_EDGE)),
      Optional.of(values.fillFlat(UNMATCHED)),
      rectangle
    );
  }

  private static SyRenderNodeType renderForActive(
    final SyThemePrimal theme,
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    if (component.isMouseOver()) {
      return renderForActiveOver(theme, component, rectangle);
    }
    return renderForActiveNotOver(theme, component, rectangle);
  }

  private static SyRenderNodeType renderForInactive(
    final SyThemePrimal theme,
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
  {
    throw new UnimplementedCodeException();
  }

  /**
   * Render a window button.
   *
   * @param theme     The theme
   * @param component The component
   * @param rectangle The target rectangle
   *
   * @return A render node
   *
   * @throws SyThemeValueException On errors
   */

  public static SyRenderNodeType render(
    final SyThemePrimal theme,
    final SyComponentReadableType component,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
    throws SyThemeValueException
  {
    Objects.requireNonNull(theme, "theme");
    Objects.requireNonNull(component, "component");
    Objects.requireNonNull(rectangle, "rectangle");

    if (component.isActive()) {
      return renderForActive(theme, component, rectangle);
    }
    return renderForInactive(theme, component, rectangle);
  }
}
