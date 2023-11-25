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

import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.active.SyActiveType;
import com.io7m.jsycamore.api.bounded.SyBoundedType;
import com.io7m.jsycamore.api.events.SyEventReceiverType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.mouse.SyMouseFocusAcceptingType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowType;
import com.io7m.jsycamore.api.themes.SyThemeableType;
import com.io7m.jsycamore.api.visibility.SyVisibleType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.Math.min;

/**
 * The type of components.
 */

public interface SyComponentType
  extends SyComponentReadableType,
  SyVisibleType,
  SyActiveType,
  SyEventReceiverType,
  SyMouseFocusAcceptingType,
  SyBoundedType<SySpaceParentRelativeType>,
  SyThemeableType
{
  /**
   * @return The tree node for this component
   */

  JOTreeNodeType<SyComponentType> node();

  /**
   * @return The window to which the component is attached, if any
   */

  Optional<SyWindowType> window();

  /**
   * Specify a size for this component in response to a set of size constraints.
   * This default implementation simply sets the size of the component to the
   * maximum size allowed by the constraints.
   *
   * @param layoutContext The current layout context
   * @param constraints   The size constraints
   *
   * @return A size for this component
   */

  default PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    final var sizeLimit =
      this.sizeUpperLimit().get();

    final var limitedConstraints =
      new SyConstraints(
        constraints.sizeMinimumX(),
        constraints.sizeMinimumY(),
        min(constraints.sizeMaximumX(), sizeLimit.sizeX()),
        min(constraints.sizeMaximumY(), sizeLimit.sizeY())
      );

    final var childNodes = this.node().children();
    for (final var childNode : childNodes) {
      childNode.value().layout(layoutContext, limitedConstraints);
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      limitedConstraints.sizeMaximum();
    this.setSize(newSize);
    return newSize;
  }

  /**
   * Find the component under the given window-relative position. The method can
   * return this component if applicable, but should return a child component if
   * one overlaps the given position.
   *
   * @param windowPosition The window position
   * @param context        The viewport accumulator
   * @param query          The type of query
   *
   * @return The component, if any
   */

  Optional<SyComponentType> componentForWindowRelative(
    PVector2I<SySpaceWindowType> windowPosition,
    SyWindowViewportAccumulatorType context,
    SyComponentQuery query);

  /**
   * A convenience function to remove all child components of this component.
   */

  default void childrenClear()
  {
    final var children =
      List.copyOf(this.node().children());

    for (final var child : children) {
      this.node().childRemove(child);
    }
  }

  /**
   * A convenience function to add a component as a child of this component.
   *
   * @param component The child component
   */

  default void childAdd(
    final SyComponentType component)
  {
    this.node().childAdd(component.node());
  }

  /**
   * A convenience function to remove a component from this component.
   *
   * @param component The child component
   */

  default void childRemove(
    final SyComponentType component)
  {
    this.node().childRemove(component.node());
  }

  /**
   * Find an ancestor component matching the given predicate.
   *
   * @param predicate The predicate
   *
   * @return The ancestor component, if any
   */

  default Optional<SyComponentType> ancestorMatching(
    final Predicate<SyComponentType> predicate)
  {
    Objects.requireNonNull(predicate, "predicate");

    var parentOpt =
      this.node().parent();

    while (parentOpt.isPresent()) {
      final var parent = parentOpt.get();
      if (predicate.test(parent.value())) {
        return Optional.of(parent.value());
      }
      parentOpt = parent.parent();
    }

    return Optional.empty();
  }
}
