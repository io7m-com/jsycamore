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
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.List;
import java.util.Objects;

/**
 * A render node consisting of a composition of render nodes.
 *
 * @param name  The node name, for debugging purposes
 * @param nodes The nodes
 */

public record SyRenderNodeComposite(
  String name,
  List<SyRenderNodeType> nodes)
  implements SyRenderNodeType
{
  /**
   * A render node consisting of a composition of render nodes.
   *
   * @param name  The node name, for debugging purposes
   * @param nodes The nodes
   */

  public SyRenderNodeComposite
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(nodes, "nodes");
  }

  /**
   * Create a composite node from the list of nodes.
   *
   * @param name  The node name, for debugging purposes
   * @param nodes The nodes
   *
   * @return A composite node
   */

  public static SyRenderNodeComposite composite(
    final String name,
    final SyRenderNodeType... nodes)
  {
    return new SyRenderNodeComposite(name, List.of(nodes));
  }

  @Override
  public PVector2I<SySpaceComponentRelativeType> position()
  {
    return PVectors2I.zero();
  }

  @Override
  public PAreaSizeI<SySpaceComponentRelativeType> size()
  {
    var sizeX = 0;
    var sizeY = 0;
    for (final var node : this.nodes) {
      sizeX = Math.max(sizeX, node.size().sizeX());
      sizeY = Math.max(sizeY, node.size().sizeY());
    }
    return PAreaSizeI.of(sizeX, sizeY);
  }
}
