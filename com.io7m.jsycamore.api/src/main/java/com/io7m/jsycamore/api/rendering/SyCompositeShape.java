/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

import java.util.List;
import java.util.Objects;

public record SyCompositeShape<T extends SySpaceType>(
  List<SyShapeType<T>> shapes)
  implements SyShapeType<T>
{
  public SyCompositeShape
  {
    Objects.requireNonNull(shapes, "shapes");
  }

  @Override
  public PAreaI<T> boundingArea()
  {
    if (this.shapes.isEmpty()) {
      return PAreaI.of(0, 0, 0, 0);
    }

    var xMin = Integer.MAX_VALUE;
    var xMax = Integer.MIN_VALUE;
    var yMin = Integer.MAX_VALUE;
    var yMax = Integer.MIN_VALUE;

    for (final var shape : this.shapes) {
      final var area = shape.boundingArea();
      xMin = Math.min(area.minimumX(), xMin);
      xMax = Math.max(area.maximumX(), xMax);
      yMin = Math.min(area.minimumY(), yMin);
      yMax = Math.max(area.maximumY(), yMax);
    }

    return PAreaI.of(xMin, xMax, yMin, yMax);
  }
}
