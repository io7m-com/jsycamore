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

package com.io7m.jsycamore.api.components;

import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.api.active.SyActiveReadableType;
import com.io7m.jsycamore.api.bounded.SyBoundedReadableType;
import com.io7m.jsycamore.api.mouse.SyMouseAcceptingReadableType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeableReadableType;
import com.io7m.jsycamore.api.visibility.SyVisibleReadableType;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The type of readable components.
 */

public interface SyComponentReadableType
  extends SyActiveReadableType,
  SyVisibleReadableType,
  SyMouseAcceptingReadableType,
  SyBoundedReadableType<SySpaceParentRelativeType>,
  SyThemeableReadableType
{
  /**
   * @return The window to which this component belongs
   */

  Optional<SyWindowReadableType> windowReadable();

  /**
   * @return The tree node for this component
   */

  JOTreeNodeReadableType<SyComponentReadableType> nodeReadable();

  @Override
  default boolean isVisible()
  {
    /*
     * If a component is set to invisible, it is unconditionally invisible.
     * Otherwise, it is visible if its parent is visible. If there is no
     * parent, the component is visible.
     */

    return switch (this.visibility().get()) {
      case VISIBILITY_INVISIBLE -> false;
      case VISIBILITY_VISIBLE -> {
        final var parentOpt = this.nodeReadable().parentReadable();
        if (parentOpt.isPresent()) {
          final var parent = parentOpt.get().value();
          yield parent.isVisible();
        }
        yield true;
      }
    };
  }

  @Override
  default boolean isActive()
  {
    /*
     * If a component is set to inactive, it is unconditionally inactive.
     * Otherwise, it is active if its parent is active. If there is no
     * parent, the component is active.
     */

    return switch (this.activity().get()) {
      case INACTIVE -> false;
      case ACTIVE -> {
        final var parentOpt = this.nodeReadable().parentReadable();
        if (parentOpt.isPresent()) {
          final var parent = parentOpt.get().value();
          yield parent.isActive();
        }
        yield true;
      }
    };
  }

  /**
   * A convenience method to return the number of children of this component.
   *
   * @return The number of children
   */

  default int childCount()
  {
    return this.nodeReadable().childrenReadable().size();
  }

  /**
   * Find an ancestor component matching the given predicate.
   *
   * @param predicate The predicate
   *
   * @return The ancestor component, if any
   */

  default Optional<SyComponentReadableType> ancestorMatchingReadable(
    final Predicate<SyComponentReadableType> predicate)
  {
    Objects.requireNonNull(predicate, "predicate");

    var parentOpt =
      this.nodeReadable().parentReadable();

    while (parentOpt.isPresent()) {
      final var parent = parentOpt.get();
      if (predicate.test(parent.value())) {
        return Optional.of(parent.value());
      }
      parentOpt = parent.parentReadable();
    }

    return Optional.empty();
  }

  /**
   * Determine the viewport position of the given parent-relative position
   * relative to this component. The method will fail if this component is not
   * attached to a window.
   *
   * @param position The source position
   *
   * @return The viewport position
   */

  default PVector2I<SySpaceViewportType> viewportPositionOf(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var windowOpt = this.windowReadable();
    if (windowOpt.isEmpty()) {
      throw new IllegalStateException("Not attached to a window!");
    }

    final var window = windowOpt.get();
    var x = position.x();
    var y = position.y();

    var parentOpt =
      this.nodeReadable().parentReadable();

    while (parentOpt.isPresent()) {
      final var parentNode =
        parentOpt.get();
      final var parent =
        parentNode.value();
      final var parentPosition =
        parent.position().get();

      x += parentPosition.x();
      y += parentPosition.y();
      parentOpt = parentNode.parentReadable();
    }

    final var windowPosition =
      window.position().get();

    return PVector2I.of(
      windowPosition.x() + x,
      windowPosition.y() + y
    );
  }
}
