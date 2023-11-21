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

import java.util.Objects;

/**
 * A rectangle.
 *
 * @param area The area
 * @param <T>  The coordinate space type
 */

public record SyShapeRectangle<T extends SySpaceType>(PAreaI<T> area)
  implements SyShapeType<T>
{
  /**
   * A rectangle.
   *
   * @param area The area
   */

  public SyShapeRectangle
  {
    Objects.requireNonNull(area, "area");
  }

  @Override
  public PAreaI<T> boundingArea()
  {
    return this.area;
  }
}
