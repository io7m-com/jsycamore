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


package com.io7m.jsycamore.api.rendering;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Objects;

/**
 * The embossed parts of an embossed rectangle.
 *
 * @param fillNorth  The north paint
 * @param fillEast   The east paint
 * @param fillSouth  The south paint
 * @param fillWest   The west paint
 * @param shapeEast  The east shape
 * @param shapeNorth The north shape
 * @param shapeSouth The south shape
 * @param shapeWest  The west shape
 * @param <T>        The coordinate space
 */

public record SyEmbossedRectangle<T extends SySpaceType>(
  SyPaintFillType fillNorth,
  SyShapePolygon<T> shapeNorth,
  SyPaintFillType fillEast,
  SyShapePolygon<T> shapeEast,
  SyPaintFillType fillSouth,
  SyShapePolygon<T> shapeSouth,
  SyPaintFillType fillWest,
  SyShapePolygon<T> shapeWest)
{
  /**
   * The embossed parts of an embossed rectangle.
   *
   * @param fillNorth  The north paint
   * @param fillEast   The east paint
   * @param fillSouth  The south paint
   * @param fillWest   The west paint
   * @param shapeEast  The east shape
   * @param shapeNorth The north shape
   * @param shapeSouth The south shape
   * @param shapeWest  The west shape
   */

  public SyEmbossedRectangle
  {
    Objects.requireNonNull(fillNorth, "fillNorth");
    Objects.requireNonNull(shapeNorth, "shapeNorth");
    Objects.requireNonNull(fillEast, "fillEast");
    Objects.requireNonNull(shapeEast, "shapeEast");
    Objects.requireNonNull(fillSouth, "fillSouth");
    Objects.requireNonNull(shapeSouth, "shapeSouth");
    Objects.requireNonNull(fillWest, "fillWest");
    Objects.requireNonNull(shapeWest, "shapeWest");
  }

  /**
   * Emboss a rectangle.
   *
   * @param area            The rectangle area
   * @param fillNorth       The north paint
   * @param fillEast        The east paint
   * @param fillSouth       The south paint
   * @param fillWest        The west paint
   * @param edgeOffset      The edge offset for the embossed parts
   * @param embossThickness The thickness of the embossed parts
   * @param <T>             The coordinate space
   *
   * @return An embossed rectangle
   */

  public static <T extends SySpaceType> SyEmbossedRectangle<T> emboss(
    final PAreaI<T> area,
    final SyPaintFillType fillNorth,
    final SyPaintFillType fillEast,
    final SyPaintFillType fillSouth,
    final SyPaintFillType fillWest,
    final int edgeOffset,
    final int embossThickness)
  {
    Objects.requireNonNull(area, "area");
    Objects.requireNonNull(fillNorth, "fillNorth");
    Objects.requireNonNull(fillEast, "fillEast");
    Objects.requireNonNull(fillSouth, "fillSouth");
    Objects.requireNonNull(fillWest, "fillWest");

    final SyShapePolygon<T> embossW;
    {
      final var xLeft = edgeOffset;
      final var xLeftInner = xLeft + embossThickness;
      final var yTop = edgeOffset;
      final var yTopInner = yTop + embossThickness;
      final var yBottom = area.maximumY() - edgeOffset;
      final var yBottomInner = yBottom - embossThickness;
      embossW = SyShapePolygon.of(
        PVector2I.of(xLeft, yTop),
        PVector2I.of(xLeftInner, yTopInner),
        PVector2I.of(xLeftInner, yBottomInner),
        PVector2I.of(xLeft, yBottom)
      );
    }

    final SyShapePolygon<T> embossS;
    {
      final var xLeft = 0;
      final var xLeftInner = xLeft + embossThickness;
      final var xRight = area.maximumX();
      final var xRightInner = xRight - embossThickness;
      final var yBottom = area.maximumY();
      final var yBottomInner = yBottom - embossThickness;
      embossS = SyShapePolygon.of(
        PVector2I.of(xLeft, yBottom),
        PVector2I.of(xLeftInner, yBottomInner),
        PVector2I.of(xRightInner, yBottomInner),
        PVector2I.of(xRight, yBottom)
      );
    }

    final SyShapePolygon<T> embossN;
    {
      final var xLeft = edgeOffset;
      final var xLeftInner = xLeft + embossThickness;
      final var xRight = area.maximumX();
      final var xRightInner = xRight - embossThickness;
      final var yTop = edgeOffset;
      final var yTopInner = yTop + embossThickness;
      embossN = SyShapePolygon.of(
        PVector2I.of(xLeft, yTop),
        PVector2I.of(xLeftInner, yTopInner),
        PVector2I.of(xRightInner, yTopInner),
        PVector2I.of(xRight, yTop)
      );
    }

    final SyShapePolygon<T> embossE;
    {
      final var xRight = area.maximumX();
      final var xRightInner = xRight - embossThickness;
      final var yTop = edgeOffset;
      final var yTopInner = yTop + embossThickness;
      final var yBottom = area.maximumY();
      final var yBottomInner = yBottom - embossThickness;

      embossE = SyShapePolygon.of(
        PVector2I.of(xRight, yTop),
        PVector2I.of(xRightInner, yTopInner),
        PVector2I.of(xRightInner, yBottomInner),
        PVector2I.of(xRight, yBottom)
      );
    }

    return new SyEmbossedRectangle<>(
      fillNorth,
      embossN,
      fillEast,
      embossE,
      fillSouth,
      embossS,
      fillWest,
      embossW
    );
  }
}
