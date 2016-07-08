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

package com.io7m.jsycamore.core.components;

import com.io7m.jnull.NullCheck;
import com.io7m.jorchard.core.JOTreeNode;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import net.jcip.annotations.NotThreadSafe;
import org.valid4j.Assertive;

import java.util.Collection;
import java.util.Optional;

/**
 * The default abstract implementation of the {@link SyComponentType}
 * interface.
 */

@NotThreadSafe
public abstract class SyComponentAbstract implements SyComponentType
{
  private final JOTreeNodeType<SyComponentType> node;
  private Optional<SyWindowType> window;
  private SyParentResizeBehavior resize_width;
  private SyParentResizeBehavior resize_height;
  private PVectorM2I<SySpaceParentRelativeType> position;
  private VectorM2I size;
  private boolean selectable = true;

  protected SyComponentAbstract()
  {
    this.resize_width = SyParentResizeBehavior.BEHAVIOR_FIXED;
    this.resize_height = SyParentResizeBehavior.BEHAVIOR_FIXED;
    this.window = Optional.empty();
    this.position = new PVectorM2I<>();
    this.size = new VectorM2I();
    this.node = JOTreeNode.create(this);
  }

  @SuppressWarnings("unchecked")
  private static <TR, T extends TR> JOTreeNodeType<TR> castNode(
    final JOTreeNodeType<T> o)
  {
    return (JOTreeNodeType<TR>) o;
  }

  @SuppressWarnings("unchecked")
  private static <TR, T extends TR> Optional<TR> cast(
    final Optional<T> o)
  {
    return (Optional<TR>) o;
  }

  @Override
  public final JOTreeNodeReadableType<SyComponentReadableType> nodeReadable()
  {
    return SyComponentAbstract.castNode(this.node);
  }

  protected final void setSelectable(final boolean s)
  {
    this.selectable = s;
  }

  protected final void setWindow(final Optional<SyWindowType> in_window)
  {
    this.window = NullCheck.notNull(in_window);
  }

  @Override
  public final Optional<SyComponentReadableType> componentReadableForWindowRelative(
    final PVectorReadable2IType<SySpaceWindowRelativeType> w_position,
    final SyWindowViewportAccumulatorType context)
  {
    return this.componentForWindowRelative(w_position, context).map(x -> x);
  }

  @Override
  public final Optional<SyComponentType> componentForWindowRelative(
    final PVectorReadable2IType<SySpaceWindowRelativeType> w_position,
    final SyWindowViewportAccumulatorType context)
  {
    try {
      context.accumulate(this.position, this.size);

      final int min_x = context.minimumX();
      final int min_y = context.minimumY();
      final int max_x = context.maximumX();
      final int max_y = context.maximumY();
      final int target_x = w_position.getXI();
      final int target_y = w_position.getYI();

      if (target_x >= min_x && target_x <= max_x) {
        if (target_y >= min_y && target_y <= max_y) {
          final Collection<JOTreeNodeType<SyComponentType>> children =
            this.node.children();

          for (final JOTreeNodeType<SyComponentType> child_node : children) {
            final SyComponentType child = child_node.value();
            final Optional<SyComponentType> child_sub =
              child.componentForWindowRelative(w_position, context);
            if (child_sub.isPresent()) {
              return child_sub;
            }
          }

          if (this.selectable) {
            return Optional.of(this);
          }
        }
      }

      return Optional.empty();
    } finally {
      context.restore();
    }
  }

  @Override
  public final Optional<SyWindowType> window()
  {
    final Optional<JOTreeNodeType<SyComponentType>> parent_opt =
      this.node.parent();
    if (!parent_opt.isPresent()) {
      return this.window;
    }
    final JOTreeNodeType<SyComponentType> parent_node = parent_opt.get();
    return parent_node.value().window();
  }

  @Override
  public void onMouseHeld(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {

  }

  @Override
  public void onMousePressed(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {

  }

  @Override
  public void onMouseReleased(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {

  }

  @Override
  public void onMouseNoLongerOver()
  {

  }

  @Override
  public void onMouseOver(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {

  }

  @Override
  public final Optional<SyWindowReadableType> windowReadable()
  {
    return SyComponentAbstract.cast(this.window());
  }

  @Override
  public final void setPosition(
    final int x,
    final int y)
  {
    this.position.set2I(x, y);
  }

  @Override
  public final void setBounds(
    final int width,
    final int height)
  {
    this.size.set2I(width, height);
  }

  @Override
  public final JOTreeNodeType<SyComponentType> node()
  {
    return this.node;
  }

  @Override
  public final void onParentResized(
    final int delta_x,
    final int delta_y)
  {
    Assertive.require(delta_x > 0);
    Assertive.require(delta_y > 0);

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
      final Collection<JOTreeNodeType<SyComponentType>> children = this.node.children();
      for (final JOTreeNodeType<SyComponentType> child_node : children) {
        final SyComponentType child = child_node.value();
        child.onParentResized(diff_x, diff_y);
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
