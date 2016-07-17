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

import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The type of components.
 */

public interface SyComponentType extends SyComponentParentEventsType,
  SyComponentMouseEventsType,
  SyComponentReadableType,
  SyMouseListenerType<SyComponentType>,
  SyResizeListenerType
{
  /**
   * Set the parent resize behavior with regards to width.
   *
   * @param b The resize behavior
   *
   * @see #resizeBehaviorWidth()
   */

  void setResizeBehaviorWidth(SyParentResizeBehavior b);

  /**
   * Set the parent resize behavior with regards to height.
   *
   * @param b The resize behavior
   *
   * @see #resizeBehaviorHeight()
   */

  void setResizeBehaviorHeight(SyParentResizeBehavior b);

  /**
   * Set the component's position and bounds.
   *
   * @param box The box representing the new position and bounds
   */

  void setBox(SyBoxType<SySpaceParentRelativeType> box);

  /**
   * @return The window to which the most distant ancestor of this component is
   * attached
   */

  Optional<SyWindowType> window();

  /**
   * @return The component tree node to which this component is attached
   */

  JOTreeNodeType<SyComponentType> node();

  /**
   * Determine the topmost component at the given window-relative position.
   *
   * @param w_position A window-relative position
   * @param context    An accumulator for calculating positions and viewports
   *
   * @return The component, if any
   */

  Optional<SyComponentType> componentForWindowRelative(
    PVectorReadable2IType<SySpaceWindowRelativeType> w_position,
    SyWindowViewportAccumulatorType context);

  /**
   * Match on the type of component.
   *
   * @param context   A context value passed through to the given functions
   * @param on_button A function evaluated if this component is a button
   * @param on_panel  A function evaluated if this component is a panel
   * @param on_label  A function evaluated if this component is a label
   * @param on_image  A function evaluated if this component is an image
   * @param <A>       The type of opaque context values
   * @param <B>       The type of returned values
   *
   * @return The value returned by whichever one of the given functions is
   * evaluated
   */

  <A, B> B matchComponent(
    A context,
    BiFunction<A, SyButtonType, B> on_button,
    BiFunction<A, SyPanelType, B> on_panel,
    BiFunction<A, SyLabelType, B> on_label,
    BiFunction<A, SyImageType, B> on_image);

  /**
   * Enable/disable this component.
   *
   * @param e {@code true} iff the component should be enabled
   *
   * @see SyComponentReadableType#isEnabled()
   */

  void setEnabled(boolean e);

  /**
   * Set this component's visibility.
   *
   * @param v The component's visibility
   *
   * @see SyComponentReadableType#isVisible()
   */

  void setVisibility(SyVisibility v);
}
