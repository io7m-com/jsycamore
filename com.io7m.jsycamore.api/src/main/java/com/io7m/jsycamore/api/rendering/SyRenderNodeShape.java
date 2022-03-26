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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;

import java.util.Objects;
import java.util.Optional;

/**
 * A render node consisting of a shape.
 *
 * @param edgePaint The edge paint
 * @param fillPaint The fill paint
 * @param shape     The shape
 */

public record SyRenderNodeShape(
  Optional<SyPaintEdgeType> edgePaint,
  Optional<SyPaintFillType> fillPaint,
  SyShapeType<SySpaceComponentRelativeType> shape)
  implements SyRenderNodeType
{
  /**
   * A render node consisting of a shape.
   *
   * @param edgePaint The edge paint
   * @param fillPaint The fill paint
   * @param shape     The shape
   */

  public SyRenderNodeShape
  {
    Objects.requireNonNull(edgePaint, "edgePaint");
    Objects.requireNonNull(fillPaint, "fillPaint");
    Objects.requireNonNull(shape, "shape");
  }

  @Override
  public PAreaSizeI<SySpaceComponentRelativeType> size()
  {
    return this.shape.size();
  }
}
