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

import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jsycamore.core.SyWindowReadableType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The type of readable components.
 */

public interface SyComponentReadableType
{
  /**
   * Components may be enabled and disabled. A component that is disabled
   * will not receive input events, and will typically be rendered as "greyed"
   * out by renderers.
   *
   * @return {@code true} iff this component is enabled
   */

  boolean isEnabled();

  /**
   * @return This component's width resize behavior.
   */

  SyParentResizeBehavior resizeBehaviorWidth();

  /**
   * @return This component's height resize behavior.
   */

  SyParentResizeBehavior resizeBehaviourHeight();

  /**
   * @return The position of the component relative to its parent
   */

  PVectorReadable2IType<SySpaceParentRelativeType> position();

  /**
   * @return The size of the component
   */

  VectorReadable2IType size();

  /**
   * @return The window to which this component belongs
   */

  Optional<SyWindowReadableType> windowReadable();

  /**
   * Match on the type of component.
   *
   * @param context   A context value passed through to the given functions
   * @param on_button A function evaluated if this component is a button
   * @param on_panel  A function evaluated if this component is a panel
   * @param <A>       The type of opaque context values
   * @param <B>       The type of returned values
   *
   * @return The value returned by whichever one of the given functions is
   * evaluated
   */

  <A, B> B matchComponentReadable(
    A context,
    BiFunction<A, SyButtonReadableType, B> on_button,
    BiFunction<A, SyPanelReadableType, B> on_panel);

  /**
   * Determine the topmost component at the given window-relative position.
   *
   * @param w_position A window-relative position
   * @param context    An accumulator for calculating positions and viewports
   *
   * @return The component, if any
   */

  Optional<SyComponentReadableType> componentReadableForWindowRelative(
    PVectorReadable2IType<SySpaceWindowRelativeType> w_position,
    SyWindowViewportAccumulatorType context);

  /**
   * @return The component tree node to which this component is attached
   */

  JOTreeNodeReadableType<SyComponentReadableType> nodeReadable();
}
