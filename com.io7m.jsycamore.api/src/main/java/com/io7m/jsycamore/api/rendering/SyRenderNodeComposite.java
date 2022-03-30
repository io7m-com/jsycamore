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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;

import java.util.List;
import java.util.Objects;

/**
 * A render node consisting of a composition of render nodes.
 *
 * @param nodes The nodes
 */

public record SyRenderNodeComposite(
  List<SyRenderNodeType> nodes)
  implements SyRenderNodeType
{
  /**
   * A render node consisting of a composition of render nodes.
   *
   * @param nodes The nodes
   */

  public SyRenderNodeComposite
  {
    Objects.requireNonNull(nodes, "nodes");
  }

  /**
   * Create a composite node from the list of nodes.
   *
   * @param nodes The nodes
   *
   * @return A composite node
   */

  public static SyRenderNodeComposite composite(
    final SyRenderNodeType... nodes)
  {
    return new SyRenderNodeComposite(List.of(nodes));
  }

  @Override
  public PAreaSizeI<SySpaceComponentRelativeType> size()
  {
    var area =
      PAreasI.<SySpaceComponentRelativeType>create(
        0, 0, 0, 0);

    for (final var node : this.nodes) {
      final var extraArea =
        PAreasI.<SySpaceComponentRelativeType>create(
          0, 0, node.size().sizeX(), node.size().sizeY());
      area = PAreasI.containing(area, extraArea);
    }

    return PAreasI.size(area);
  }
}
