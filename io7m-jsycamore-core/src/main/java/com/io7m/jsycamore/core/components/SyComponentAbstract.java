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
import com.io7m.junreachable.UnreachableCodeException;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * The default abstract implementation of the {@link SyComponentType}
 * interface.
 */

@NotThreadSafe
public abstract class SyComponentAbstract implements SyComponentType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyComponentAbstract.class);
  }

  private final JOTreeNodeType<SyComponentType> node;
  private Optional<SyWindowType> window;
  private SyParentResizeBehavior resize_width;
  private SyParentResizeBehavior resize_height;
  private PVectorM2I<SySpaceParentRelativeType> position;
  private VectorM2I size;
  private boolean selectable = true;
  private boolean enabled = true;
  private SyVisibility visibility;

  protected SyComponentAbstract(
    final BooleanSupplier in_detach_check)
  {
    NullCheck.notNull(in_detach_check);

    this.resize_width = SyParentResizeBehavior.BEHAVIOR_FIXED;
    this.resize_height = SyParentResizeBehavior.BEHAVIOR_FIXED;
    this.visibility = SyVisibility.VISIBILITY_VISIBLE;
    this.window = Optional.empty();
    this.position = new PVectorM2I<>();
    this.size = new VectorM2I();
    this.node = JOTreeNode.createWithDetachCheck(this, in_detach_check);
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

  private static boolean isOverlappingComponent(
    final int viewport_min_x,
    final int viewport_min_y,
    final int viewport_max_x,
    final int viewport_max_y,
    final int target_x,
    final int target_y)
  {
    if (target_x >= viewport_min_x && target_x <= viewport_max_x) {
      if (target_y >= viewport_min_y && target_y <= viewport_max_y) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final void setResizeBehaviorWidth(final SyParentResizeBehavior b)
  {
    this.resize_width = NullCheck.notNull(b);
  }

  @Override
  public final void setResizeBehaviorHeight(final SyParentResizeBehavior b)
  {
    this.resize_height = NullCheck.notNull(b);
  }

  @Override
  public final boolean isVisible()
  {
    /**
     * If a component is set to invisible, it is unconditionally invisible.
     * Otherwise, it is visible if its parent is visible. If there is no
     * parent, the component is visible.
     */

    switch (this.visibility) {
      case VISIBILITY_INVISIBLE: {
        return false;
      }
      case VISIBILITY_VISIBLE: {
        final Optional<JOTreeNodeType<SyComponentType>> parent_opt =
          this.node.parent();
        if (parent_opt.isPresent()) {
          final SyComponentType parent = parent_opt.get().value();
          return parent.isVisible();
        }
        return true;
      }
    }

    throw new UnreachableCodeException();
  }

  @Override
  public final SyVisibility visibility()
  {
    return this.visibility;
  }

  @Override
  public final void setVisibility(final SyVisibility v)
  {
    this.visibility = NullCheck.notNull(v);
  }

  @Override
  public final boolean isEnabled()
  {
    return this.enabled;
  }

  @Override
  public final void setEnabled(final boolean e)
  {
    this.enabled = e;
  }

  @Override
  public final JOTreeNodeReadableType<SyComponentReadableType> nodeReadable()
  {
    return SyComponentAbstract.castNode(this.node);
  }

  /**
   * Indicate whether or not this component should be selectable. This is used
   * to implement pseudo-components such as the root window component: The
   * component is visible but should not be selectable by users.
   *
   * @param s {@code true} iff the component is selectable
   */

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

  // CHECKSTYLE:OFF
  protected boolean isOverlappingExcludedArea(
    final int viewport_min_x,
    final int viewport_min_y,
    final int viewport_max_x,
    final int viewport_max_y,
    final int target_x,
    final int target_y)
  {
    return false;
  }
  // CHECKSTYLE:ON

  @Override
  public final Optional<SyComponentType> componentForWindowRelative(
    final PVectorReadable2IType<SySpaceWindowRelativeType> w_position,
    final SyWindowViewportAccumulatorType context)
  {
    /**
     * If this component is invisible, then none of the children are
     * visible either and so there's no point returning them.
     */

    if (!this.isVisible()) {
      return Optional.empty();
    }

    try {
      context.accumulate(this.position, this.size);

      final int min_x = context.minimumX();
      final int min_y = context.minimumY();
      final int max_x = context.maximumX();
      final int max_y = context.maximumY();
      final int target_x = w_position.getXI();
      final int target_y = w_position.getYI();

      if (SyComponentAbstract.isOverlappingComponent(
        min_x, min_y, max_x, max_y, target_x, target_y)) {

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
          if (!this.isOverlappingExcludedArea(
            min_x, min_y, max_x, max_y, target_x, target_y)) {
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
  public final void onMouseHeld(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isEnabled()) {
      consumed = this.mouseHeld(
        mouse_position_first, mouse_position_now, button, actual);
    }

    if (!consumed) {
      final Optional<JOTreeNodeType<SyComponentType>> parent_node =
        this.node.parent();
      if (parent_node.isPresent()) {
        final SyComponentType parent = parent_node.get().value();
        parent.onMouseHeld(
          mouse_position_first, mouse_position_now, button, actual);
      } else {
        SyComponentAbstract.LOG.warn("onMouseHeld: event not consumed");
      }
    }
  }

  @Override
  public final void onMousePressed(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isEnabled()) {
      consumed = this.mousePressed(mouse_position, button, actual);
    }

    if (!consumed) {
      final Optional<JOTreeNodeType<SyComponentType>> parent_node =
        this.node.parent();
      if (parent_node.isPresent()) {
        final SyComponentType parent = parent_node.get().value();
        parent.onMousePressed(mouse_position, button, actual);
      } else {
        SyComponentAbstract.LOG.warn("onMousePressed: event not consumed");
      }
    }
  }

  @Override
  public final void onMouseReleased(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isEnabled()) {
      consumed =
        this.mouseReleased(mouse_position, button, actual);
    }

    if (!consumed) {
      final Optional<JOTreeNodeType<SyComponentType>> parent_node =
        this.node.parent();
      if (parent_node.isPresent()) {
        final SyComponentType parent = parent_node.get().value();
        parent.onMouseReleased(mouse_position, button, actual);
      } else {
        SyComponentAbstract.LOG.warn("onMouseReleased: event not consumed");
      }
    }
  }

  @Override
  public final void onMouseNoLongerOver()
  {
    boolean consumed = true;
    if (this.isEnabled()) {
      consumed = this.mouseNoLongerOver();
    }

    if (!consumed) {
      final Optional<JOTreeNodeType<SyComponentType>> parent_node =
        this.node.parent();
      if (parent_node.isPresent()) {
        final SyComponentType parent = parent_node.get().value();
        parent.onMouseNoLongerOver();
      } else {
        SyComponentAbstract.LOG.warn("onMouseNoLongerOver: event not consumed");
      }
    }
  }

  @Override
  public final void onMouseOver(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isEnabled()) {
      consumed = this.mouseOver(mouse_position, actual);
    }

    if (!consumed) {
      final Optional<JOTreeNodeType<SyComponentType>> parent_node =
        this.node.parent();
      if (parent_node.isPresent()) {
        final SyComponentType parent = parent_node.get().value();
        parent.onMouseOver(mouse_position, actual);
      } else {
        SyComponentAbstract.LOG.warn("onMouseOver: event not consumed");
      }
    }
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
    final int previous_w = this.size.getXI();
    final int previous_h = this.size.getYI();

    this.size.set2I(width, height);

    final int delta_x =
      Math.subtractExact(this.size.getXI(), previous_w);
    final int delta_y =
      Math.subtractExact(this.size.getYI(), previous_h);
    final boolean resized =
      delta_x != 0 || delta_y != 0;

    try {
      if (SyComponentAbstract.LOG.isTraceEnabled()) {
        SyComponentAbstract.LOG.trace(
          "resized: ({}) {} ({}, {})",
          this,
          this.size,
          Integer.valueOf(delta_x),
          Integer.valueOf(delta_y));
      }
      this.resized(delta_x, delta_y);
    } catch (final Throwable e) {
      SyErrors.ignoreNonErrors(SyComponentAbstract.LOG, e);
    }

    if (resized) {
      final Collection<JOTreeNodeType<SyComponentType>> children = this.node.children();
      for (final JOTreeNodeType<SyComponentType> child_node : children) {
        final SyComponentType child = child_node.value();
        child.onParentResized(delta_x, delta_y);
      }
    }
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
    final int previous_x = this.position.getXI();
    final int previous_y = this.position.getYI();
    final int previous_w = this.size.getXI();
    final int previous_h = this.size.getYI();

    switch (this.resize_width) {
      case BEHAVIOR_FIXED: {
        break;
      }
      case BEHAVIOR_RESIZE: {
        final int new_w = Math.max(2, Math.addExact(previous_w, delta_x));
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
        final int new_h = Math.max(2, Math.addExact(previous_h, delta_y));
        this.size.setYI(new_h);
        break;
      }
      case BEHAVIOR_MOVE: {
        this.position.setYI(Math.addExact(previous_y, delta_y));
        break;
      }
    }

    final int diff_x =
      Math.subtractExact(this.size.getXI(), previous_w);
    final int diff_y =
      Math.subtractExact(this.size.getYI(), previous_h);
    final boolean resized =
      diff_x != 0 || diff_y != 0;

    if (resized) {
      try {
        this.resized(delta_x, delta_y);
      } catch (final Throwable e) {
        SyErrors.ignoreNonErrors(SyComponentAbstract.LOG, e);
      }

      final Collection<JOTreeNodeType<SyComponentType>> children = this.node.children();
      for (final JOTreeNodeType<SyComponentType> child_node : children) {
        final SyComponentType child = child_node.value();
        child.onParentResized(diff_x, diff_y);
      }
    }
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
  public final SyParentResizeBehavior resizeBehaviorHeight()
  {
    return this.resize_height;
  }
}
