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

package com.io7m.jsycamore.api;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

public interface SyBoundedReadableType<T extends SySpaceType>
  extends SySizedReadableType<T>
{
  /**
   * @return The readable area representing the object's position and bounds
   */

  default PAreaI<T> boundingArea()
  {
    final var position =
      this.position().get();
    final var size =
      this.size().get();
    return PAreasI.create(
      position.x(),
      position.y(),
      size.sizeX(),
      size.sizeY());
  }

  AttributeReadableType<PVector2I<T>> position();
}
