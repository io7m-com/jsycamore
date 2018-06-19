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

package com.io7m.jsycamore.api.components;

import com.io7m.jorchard.core.JOTreeNode;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.SyParentResizeBehavior;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowRelativeType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * The default abstract implementation of the {@link SyComponentType} interface.
 */

@NotThreadSafe
public abstract class SyComponentAbstract implements SyComponentType
{
  private static final Logger LOG;
  private static final Logger LOG_RESIZE;

  static {
    LOG = LoggerFactory.getLogger(SyComponentAbstract.class);
    LOG_RESIZE = LoggerFactory.getLogger(LOG.getName() + ".resize");
  }

  private final JOTreeNodeType<SyComponentType> node;
  private PAreaI<SySpaceParentRelativeType> box;
  private Optional<SyWindowType> window;
  private SyParentResizeBehavior resize_width;
  private SyParentResizeBehavior resize_height;
  private boolean selectable = true;
  private SyVisibility visibility;
  private SyActive active;

  protected SyComponentAbstract(
    final BooleanSupplier in_detach_check)
  {
    Objects.requireNonNull(in_detach_check, "Detach check");

    this.resize_width = SyParentResizeBehavior.BEHAVIOR_FIXED;
    this.resize_height = SyParentResizeBehavior.BEHAVIOR_FIXED;
    this.visibility = SyVisibility.VISIBILITY_VISIBLE;
    this.active = SyActive.ACTIVE;
    this.window = Optional.empty();
    this.box = PAreaI.of(0, 0, 0, 0);
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
    this.resize_width = Objects.requireNonNull(b, "Behavior");
  }

  @Override
  public final void setResizeBehaviorHeight(final SyParentResizeBehavior b)
  {
    this.resize_height = Objects.requireNonNull(b, "Behavior");
  }

  @Override
  public final boolean isVisible()
  {
    /*
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
  public final SyActive activity()
  {
    return this.active;
  }

  @Override
  public final boolean isActive()
  {
    /*
     * If a component is set to inactive, it is unconditionally inactive.
     * Otherwise, it is active if its parent is active. If there is no
     * parent, the component is active.
     */

    switch (this.active) {
      case INACTIVE: {
        return false;
      }

      case ACTIVE: {
        final Optional<JOTreeNodeType<SyComponentType>> parent_opt =
          this.node.parent();
        if (parent_opt.isPresent()) {
          final SyComponentType parent = parent_opt.get().value();
          return parent.isActive();
        }
        return true;
      }
    }

    throw new UnreachableCodeException();
  }

  @Override
  public final void setActive(final SyActive e)
  {
    this.active = Objects.requireNonNull(e, "Activity");
  }

  @Override
  public final SyVisibility visibility()
  {
    return this.visibility;
  }

  @Override
  public final void setVisibility(final SyVisibility v)
  {
    this.visibility = Objects.requireNonNull(v, "Visibility");
  }

  @Override
  public final JOTreeNodeReadableType<SyComponentReadableType> nodeReadable()
  {
    return SyComponentAbstract.castNode(this.node);
  }

  /**
   * Indicate whether or not this component should be selectable. This is used to implement
   * pseudo-components such as the root window component: The component is visible but should not be
   * selectable by users.
   *
   * @param s {@code true} iff the component is selectable
   */

  protected final void setSelectable(final boolean s)
  {
    this.selectable = s;
  }

  protected final void setWindow(final Optional<SyWindowType> in_window)
  {
    this.window = Objects.requireNonNull(in_window, "Window");
  }

  @Override
  public final Optional<SyComponentReadableType> componentReadableForWindowRelative(
    final PVector2I<SySpaceWindowRelativeType> w_position,
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
  public final PAreaI<SySpaceParentRelativeType> box()
  {
    return this.box;
  }

  @Override
  public final PVector2I<SySpaceComponentRelativeType> transformWindowRelative(
    final PVector2I<SySpaceWindowRelativeType> w_position)
  {
    Objects.requireNonNull(w_position, "Viewport position");

    final PVector2I<SySpaceWindowRelativeType> pos_component =
      this.positionWindowRelative();

    return PVector2I.of(
      Math.subtractExact(w_position.x(), pos_component.x()),
      Math.subtractExact(w_position.y(), pos_component.y()));
  }

  @Override
  public final PVector2I<SySpaceWindowRelativeType> positionWindowRelative()
  {
    final ArrayDeque<SyComponentReadableType> ancestors =
      new ArrayDeque<>();

    JOTreeNodeType<SyComponentType> n = this.node;
    while (true) {
      final Optional<JOTreeNodeType<SyComponentType>> parent_opt = n.parent();
      if (parent_opt.isPresent()) {
        final JOTreeNodeType<SyComponentType> parent = parent_opt.get();
        ancestors.push(parent.value());
        n = parent;
      } else {
        break;
      }
    }

    int x = 0;
    int y = 0;

    while (!ancestors.isEmpty()) {
      final SyComponentReadableType ancestor = ancestors.pop();
      x = Math.addExact(x, ancestor.box().minimumX());
      y = Math.addExact(y, ancestor.box().minimumY());
    }

    x = Math.addExact(x, this.box().minimumX());
    y = Math.addExact(y, this.box().minimumY());
    return PVector2I.of(x, y);
  }

  @Override
  public final Optional<SyComponentType> componentForWindowRelative(
    final PVector2I<SySpaceWindowRelativeType> w_position,
    final SyWindowViewportAccumulatorType context)
  {
    /*
     * If this component is invisible, then none of the children are
     * visible either and so there's no point returning them.
     */

    if (!this.isVisible()) {
      return Optional.empty();
    }

    try {
      context.accumulate(this.box);

      final int min_x = context.minimumX();
      final int min_y = context.minimumY();
      final int max_x = context.maximumX();
      final int max_y = context.maximumY();
      final int target_x = w_position.x();
      final int target_y = w_position.y();

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
  public final void onThemeChanged()
  {
    this.themeHasChanged();

    final Collection<JOTreeNodeType<SyComponentType>> children =
      this.node.children();

    /*
     * Copy the list of children so that if a child detaches, a concurrent
     * modification error is avoided.
     */

    final Iterable<JOTreeNodeType<SyComponentType>> children_copy =
      new ArrayList<>(children);

    for (final JOTreeNodeType<SyComponentType> child_node : children_copy) {
      final SyComponentType child = child_node.value();
      child.onThemeChanged();
    }
  }

  @Override
  public final void onMouseHeld(
    final PVector2I<SySpaceViewportType> mouse_position_first,
    final PVector2I<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isActive()) {
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
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isActive()) {
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
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isActive()) {
      consumed = this.mouseReleased(mouse_position, button, actual);
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
    if (this.isActive()) {
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
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {
    boolean consumed = true;
    if (this.isActive()) {
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
  public final void setBox(final PAreaI<SySpaceParentRelativeType> new_box)
  {
    Objects.requireNonNull(new_box, "Box");

    final int previous_w = this.box.sizeX();
    final int previous_h = this.box.sizeY();

    this.box = new_box;

    final int delta_x =
      Math.subtractExact(this.box.sizeX(), previous_w);
    final int delta_y =
      Math.subtractExact(this.box.sizeY(), previous_h);
    final boolean resized =
      delta_x != 0 || delta_y != 0;

    try {
      if (SyComponentAbstract.LOG_RESIZE.isTraceEnabled()) {
        SyComponentAbstract.LOG_RESIZE.trace(
          "resized: ({}) {} ({}, {})",
          this,
          this.box,
          Integer.valueOf(delta_x),
          Integer.valueOf(delta_y));
      }
      this.resized(delta_x, delta_y);
    } catch (final Throwable e) {
      SyErrors.ignoreNonErrors(SyComponentAbstract.LOG, e);
    }

    if (resized) {
      final Collection<JOTreeNodeType<SyComponentType>> children =
        this.node.children();

      /*
       * Copy the list of children so that if a child detaches, a concurrent
       * modification error is avoided.
       */

      final Iterable<JOTreeNodeType<SyComponentType>> children_copy =
        new ArrayList<>(children);

      for (final JOTreeNodeType<SyComponentType> child_node : children_copy) {
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
    switch (this.resize_width) {
      case BEHAVIOR_FIXED: {
        break;
      }
      case BEHAVIOR_RESIZE: {
        this.setBox(PAreasI.scaleFromMaxYMaxX(this.box, delta_x, 0));
        break;
      }
      case BEHAVIOR_MOVE: {
        this.setBox(PAreasI.moveRelative(this.box, delta_x, 0));
        break;
      }
    }

    switch (this.resize_height) {
      case BEHAVIOR_FIXED: {
        break;
      }
      case BEHAVIOR_RESIZE: {
        this.setBox(PAreasI.scaleFromMaxYMaxX(this.box, 0, delta_y));
        break;
      }
      case BEHAVIOR_MOVE: {
        this.setBox(PAreasI.moveRelative(this.box, 0, delta_y));
        break;
      }
    }
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

  protected final String toNamedString(final String name)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[");
    sb.append(name);
    sb.append(" 0x");
    sb.append(Integer.toHexString(this.hashCode()));
    sb.append(" ");
    PAreasI.showToBuilder(this.box(), sb);
    sb.append("]");
    return sb.toString();
  }

  protected final Optional<SyTheme> windowTheme()
  {
    return this.window().map(SyWindowReadableType::theme);
  }
}
