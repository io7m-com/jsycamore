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

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVector2IType;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import org.valid4j.Assertive;

import java.util.Iterator;
import java.util.Set;

public abstract class SyComponentAbstract implements SyComponentType
{
  private SyParentResizeBehavior resize_width;
  private SyParentResizeBehavior resize_height;
  private PVector2IType<SySpaceParentRelativeType> position;
  private VectorM2I size;

  protected SyComponentAbstract(
    final SyParentResizeBehavior in_resize_width,
    final SyParentResizeBehavior in_resize_height)
  {
    this.resize_width = NullCheck.notNull(in_resize_width);
    this.resize_height = NullCheck.notNull(in_resize_height);
    this.position = new PVectorM2I<>();
    this.size = new VectorM2I();
  }

  @Override
  public final void onParentResized(
    final SyGraph<SyComponentType, SyComponentLink> graph,
    final int delta_x,
    final int delta_y)
  {
    NullCheck.notNull(graph);
    Assertive.require(delta_x > 0);
    Assertive.require(delta_y > 0);
    Assertive.require(graph.containsVertex(this));

    final int previous_x = this.position.getXI();
    final int previous_y = this.position.getYI();
    final int previous_w = this.size.getXI();
    final int previous_h = this.size.getYI();

    switch (this.resize_width) {
      case BEHAVIOR_FIXED: {
        break;
      }
      case BEHAVIOR_RESIZE: {
        final int new_w = Math.min(2, Math.addExact(previous_w, delta_x));
        this.size.setXI(new_w);
        break;
      }
      case BEHAVIOR_MOVE: {
        this.position.setXI(Math.addExact(previous_x, delta_x));
        break;
      }
    }

    switch (this.resize_height) {
      case BEHAVIOR_FIXED: {
        break;
      }
      case BEHAVIOR_RESIZE: {
        final int new_h = Math.min(2, Math.addExact(previous_h, delta_y));
        this.size.setYI(new_h);
        break;
      }
      case BEHAVIOR_MOVE: {
        this.position.setYI(Math.addExact(previous_y, delta_y));
        break;
      }
    }

    final int diff_x =
      Math.abs(Math.subtractExact(this.size.getXI(), previous_w));
    final int diff_y =
      Math.abs(Math.subtractExact(this.size.getYI(), previous_h));
    final boolean resized =
      diff_x > 0 && diff_y > 0;

    if (resized) {
      final Set<SyComponentLink> children = graph.outgoingEdgesOf(this);
      final Iterator<SyComponentLink> iter = children.iterator();
      while (iter.hasNext()) {
        final SyComponentLink link = iter.next();
        link.target().onParentResized(graph, diff_x, diff_y);
      }
    }

    Assertive.ensure(this.size.getXI() >= 2);
    Assertive.ensure(this.size.getYI() >= 2);
  }

  @Override
  public final VectorReadable2IType size()
  {
    return this.size;
  }

  @Override
  public final PVectorReadable2IType<SySpaceParentRelativeType> position()
  {
    return this.position;
  }

  @Override
  public final SyParentResizeBehavior resizeBehaviorWidth()
  {
    return this.resize_width;
  }

  @Override
  public final SyParentResizeBehavior resizeBehaviourHeight()
  {
    return this.resize_height;
  }
}
