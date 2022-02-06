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

package com.io7m.jsycamore.components.standard;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jattribute.core.Attributes;
import com.io7m.jorchard.core.JOTreeNode;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyActive;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyVisibility;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowType;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.osgi.annotation.versioning.ProviderType;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.io7m.jsycamore.api.components.SyActive.ACTIVE;
import static com.io7m.jsycamore.api.components.SyVisibility.VISIBILITY_VISIBLE;

/**
 * A convenient abstract implementation of a component, to make it easier
 * to implement new components.
 */

@ProviderType
public abstract class SyComponentAbstract implements SyComponentType
{
  private final JOTreeNodeType<SyComponentType> node;
  private final AttributeType<SyActive> activity;
  private final AttributeType<SyVisibility> visibility;
  private final AttributeType<PVector2I<SySpaceParentRelativeType>> position;
  private final AttributeType<PAreaSizeI<SySpaceParentRelativeType>> size;

  protected SyComponentAbstract()
  {
    final var attributes =
      SyComponentAttributes.get();

    this.visibility =
      attributes.create(VISIBILITY_VISIBLE);
    this.activity =
      attributes.create(ACTIVE);
    this.size =
      attributes.create(PAreaSizeI.of(0, 0));
    this.position =
      attributes.create(PVector2I.of(0, 0));
    this.node =
      JOTreeNode.create(this);
  }

  @SuppressWarnings("unchecked")
  private static <TR, T extends TR> JOTreeNodeType<TR> castNode(
    final JOTreeNodeType<T> o)
  {
    return (JOTreeNodeType<TR>) o;
  }

  private static boolean isOverlappingComponent(
    final int viewportMinX,
    final int viewportMinY,
    final int viewportMaxX,
    final int viewportMaxY,
    final int targetX,
    final int targetY)
  {
    if (targetX >= viewportMinX && targetX <= viewportMaxX) {
      return targetY >= viewportMinY && targetY <= viewportMaxY;
    }
    return false;
  }

  @Override
  public final AttributeType<SyActive> activity()
  {
    return this.activity;
  }

  @Override
  public final AttributeType<SyVisibility> visibility()
  {
    return this.visibility;
  }

  @Override
  public final Optional<SyWindowType> window()
  {
    final var parentOpt =
      this.node.parent();
    if (parentOpt.isEmpty()) {
      return Optional.empty();
    }
    return parentOpt.get().value().window();
  }

  @Override
  public final AttributeType<PVector2I<SySpaceParentRelativeType>> position()
  {
    return this.position;
  }

  @Override
  public final AttributeType<PAreaSizeI<SySpaceParentRelativeType>> size()
  {
    return this.size;
  }

  @Override
  public final JOTreeNodeReadableType<SyComponentReadableType> nodeReadable()
  {
    return castNode(this.node);
  }

  @Override
  public final Optional<SyWindowReadableType> windowReadable()
  {
    return this.window().map(Function.identity());
  }

  @Override
  public final boolean eventSend(
    final SyEventType event)
  {
    /*
     * Only deliver the event to this component if it is active.
     */

    boolean consumed = true;
    if (this.isActive()) {
      consumed = this.onEvent(event);
    }

    /*
     * If this component didn't consume the event, try to deliver it to
     * the parent component instead.
     */

    if (!consumed) {
      final var parentOpt = this.node.parent();
      if (parentOpt.isPresent()) {
        final SyComponentType parent = parentOpt.get().value();
        return parent.eventSend(event);
      }
    }

    return consumed;
  }

  /**
   * Receive an event on this particular component. If this method returns
   * {@code false}, the event is passed to the parent of this component.
   *
   * @param event The event
   *
   * @return {@code true} if the event has been consumed
   */

  protected abstract boolean onEvent(SyEventType event);

  @Override
  public final Optional<SyComponentType> componentForWindowRelative(
    final PVector2I<SySpaceWindowType> windowPosition,
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
      context.accumulate(this.boundingArea());

      final var minimumX = context.minimumX();
      final var minimumY = context.minimumY();
      final var maximumX = context.maximumX();
      final var maximumY = context.maximumY();
      final var targetX = windowPosition.x();
      final var targetY = windowPosition.y();

      if (isOverlappingComponent(
        minimumX,
        minimumY,
        maximumX,
        maximumY,
        targetX,
        targetY)) {

        final var children = this.node.children();
        for (final var childNode : children) {
          final var child =
            childNode.value();
          final var childOpt =
            child.componentForWindowRelative(windowPosition, context);
          if (childOpt.isPresent()) {
            return childOpt;
          }
        }

        return Optional.of(this);
      }

      return Optional.empty();
    } finally {
      context.restore();
    }
  }

  @Override
  public final JOTreeNodeType<SyComponentType> node()
  {
    return this.node;
  }

  @Override
  public final String toString()
  {
    return String.format(
      "[%s 0x%s]",
      this.getClass().getSimpleName(),
      Integer.toUnsignedString(this.hashCode(), 16)
    );
  }
}
