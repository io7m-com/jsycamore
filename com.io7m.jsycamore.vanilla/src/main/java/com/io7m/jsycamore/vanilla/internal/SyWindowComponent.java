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

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jorchard.core.JOTreeNode;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.active.SyActive;
import com.io7m.jsycamore.api.components.SyComponentQuery;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.api.visibility.SyVisibility;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.io7m.jsycamore.api.active.SyActive.ACTIVE;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.visibility.SyVisibility.VISIBILITY_VISIBLE;
import static java.lang.Boolean.FALSE;

/**
 * A window decoration component.
 */

public abstract class SyWindowComponent implements SyComponentType
{
  private final SyWindowDecorationComponent semantic;
  private final JOTreeNodeType<SyComponentType> node;
  private final AttributeType<SyActive> activity;
  private final AttributeType<SyVisibility> visibility;
  private final AttributeType<PVector2I<SySpaceParentRelativeType>> position;
  private final AttributeType<PAreaSizeI<SySpaceParentRelativeType>> size;
  private final List<SyThemeClassNameType> themeClassesExtra;
  private Optional<SyWindowType> window;
  private volatile boolean mouseOver;
  private volatile boolean mouseAcceptQuery = true;

  protected SyWindowComponent(
    final SyWindowDecorationComponent inSemantic,
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    this.semantic =
      Objects.requireNonNull(inSemantic, "semantic");
    this.themeClassesExtra =
      Objects.requireNonNull(inThemeClassesExtra, "themeClassesExtra");

    final var attributes =
      SyWindowAttributes.get();

    this.visibility =
      attributes.create(VISIBILITY_VISIBLE);
    this.activity =
      attributes.create(ACTIVE);
    this.size =
      attributes.create(PAreaSizeI.of(0, 0));
    this.position =
      attributes.create(PVector2I.of(0, 0));
    this.window =
      Optional.empty();
    this.node =
      JOTreeNode.createWithDetachCheck(this, () -> false);
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
  public final List<SyThemeClassNameType> themeClassesExtra()
  {
    return this.themeClassesExtra;
  }

  @Override
  public final boolean isMouseQueryAccepting()
  {
    return this.mouseAcceptQuery;
  }

  @Override
  public final void setMouseQueryAccepting(
    final boolean accepting)
  {
    this.mouseAcceptQuery = accepting;
  }

  protected final void setWindow(
    final Optional<SyWindowType> newWindow)
  {
    this.window = Objects.requireNonNull(newWindow, "newWindow");
  }

  @Override
  public final Optional<SyWindowType> window()
  {
    final var parentOpt =
      this.node.parent();
    if (parentOpt.isEmpty()) {
      return this.window;
    }
    return parentOpt.get().value().window();
  }

  final SyWindowDecorationComponent semantic()
  {
    return this.semantic;
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
  public final Optional<SyWindowReadableType> windowReadable()
  {
    return this.window().map(Function.identity());
  }

  @Override
  public final SyEventConsumed eventSend(
    final SyEventType event)
  {
    /*
     * Only deliver the event to this component if it is active.
     */

    SyEventConsumed result = EVENT_CONSUMED;
    if (this.isActive()) {
      result = this.onEvent(event);
    }

    /*
     * If this component didn't consume the event, try to deliver it to
     * the parent component instead.
     */

    if (result == EVENT_CONSUMED) {
      final var parentOpt = this.node.parent();
      if (parentOpt.isPresent()) {
        final SyComponentType parent = parentOpt.get().value();
        return parent.eventSend(event);
      }
    }

    return result;
  }

  protected abstract SyEventConsumed onEvent(SyEventType event);

  @Override
  public final Optional<SyComponentType> componentForWindowRelative(
    final PVector2I<SySpaceWindowType> windowPosition,
    final SyWindowViewportAccumulatorType context,
    final SyComponentQuery query)
  {
    Objects.requireNonNull(windowPosition, "windowPosition");
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(query, "query");

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
            child.componentForWindowRelative(windowPosition, context, query);
          if (childOpt.isPresent()) {
            return childOpt;
          }
        }

        return switch (query) {
          case FIND_SPATIALLY -> Optional.of(this);
          case FIND_FOR_MOUSE_CURSOR -> {
            if (this.isMouseQueryAccepting()) {
              yield Optional.of(this);
            }
            yield Optional.empty();
          }
        };
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

  @Override
  public final boolean isVisible()
  {
    return switch (this.visibility().get()) {
      case VISIBILITY_INVISIBLE -> false;
      case VISIBILITY_VISIBLE -> {

        /*
         * If this component is a decoration, then it should not be visible
         * if the window is not showing decorations.
         */

        if (this.semantic.isDecoration() && !this.isWindowDecorated()) {
          yield false;
        }

        /*
         * Otherwise, this component is visible if the parent is visible.
         */

        final var parentOpt =
          this.nodeReadable().parentReadable();
        if (parentOpt.isPresent()) {
          final var parent = parentOpt.get().value();
          yield parent.isVisible();
        }

        yield true;
      }
    };
  }

  protected final boolean isWindowDecorated()
  {
    return this.windowReadable()
      .map(SyWindowReadableType::decorated)
      .map(AttributeReadableType::get)
      .orElse(FALSE)
      .booleanValue();
  }

  @Override
  public final boolean isMouseOver()
  {
    return this.mouseOver;
  }

  protected final void setMouseOver(
    final boolean over)
  {
    this.mouseOver = over;
  }
}
