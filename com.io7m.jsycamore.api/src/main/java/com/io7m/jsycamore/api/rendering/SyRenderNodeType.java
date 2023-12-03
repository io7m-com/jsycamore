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
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

/**
 * The type of render nodes.
 */

public sealed interface SyRenderNodeType
  permits SyRenderNodeComposite,
  SyRenderNodePrimitiveType
{
  /**
   * @return A name for the node, for debugging purposes
   */

  String name();

  /**
   * @return The position of the node relative to the component
   */

  PVector2I<SySpaceComponentRelativeType> position();

  /**
   * @return The size of the node
   */

  PAreaSizeI<SySpaceComponentRelativeType> size();

  /**
   * @return The bounding area of the node
   */

  default PAreaI<SySpaceComponentRelativeType> boundingArea()
  {
    return PAreasI.create(
      this.position().x(),
      this.position().y(),
      this.size().sizeX(),
      this.size().sizeY()
    );
  }
}
