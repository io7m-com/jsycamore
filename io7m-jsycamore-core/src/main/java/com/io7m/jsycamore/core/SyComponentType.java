/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jsycamore.core;

import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

/**
 * The type of components.
 */

public interface SyComponentType
{
  /**
   * @return This component's lightWidth resize behavior.
   */

  SyParentResizeBehavior resizeBehaviorWidth();

  /**
   * @return This component's height resize behavior.
   */

  SyParentResizeBehavior resizeBehaviourHeight();

  /**
   * @return The position of the component
   */

  PVectorReadable2IType<SySpaceParentRelativeType> position();

  /**
   * @return The size of the component
   */

  VectorReadable2IType size();

  /**
   * The parent of this component was resized.
   *
   * @param graph   The graph to which this component belongs
   * @param delta_x The change in the lightWidth
   * @param delta_y The change in the height
   */

  void onParentResized(
    SyGraph<SyComponentType, SyComponentLink> graph,
    int delta_x,
    int delta_y);
}
