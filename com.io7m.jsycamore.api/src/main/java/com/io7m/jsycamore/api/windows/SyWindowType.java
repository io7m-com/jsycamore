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

package com.io7m.jsycamore.api.windows;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.SyScreenType;
import com.io7m.jsycamore.api.components.SyComponentQuery;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Optional;

/**
 * The type of windows.
 */

public interface SyWindowType extends SyWindowReadableType
{
  /**
   * @return The screen to which this window belongs.
   */

  SyScreenType screen();

  /**
   * Execute a layout pass on the window.
   *
   * @param layoutContext The layout context
   */

  void layout(SyLayoutContextType layoutContext);

  /**
   * Send an event to the window.
   *
   * @param event The event
   */

  void eventSend(SyWindowEventType event);

  /**
   * Find a component given a viewport position.
   *
   * @param position The position
   * @param query    The type of position query
   *
   * @return A component, if any
   *
   * @see com.io7m.jsycamore.api.mouse.SyMouseAcceptingReadableType
   */

  Optional<SyComponentType> componentForViewportPosition(
    PVector2I<SySpaceViewportType> position,
    SyComponentQuery query);

  /**
   * Transform the given viewport position to a window-relative position.
   *
   * @param viewportPosition The viewport position
   *
   * @return A window-relative position
   */

  PVector2I<SySpaceWindowType> transformViewportRelative(
    PVector2I<SySpaceViewportType> viewportPosition);

  /**
   * Find the component under the given window-relative position.
   *
   * @param w_position The window-relative position
   * @param query      The type of position query
   *
   * @return The component, if any
   *
   * @see com.io7m.jsycamore.api.mouse.SyMouseAcceptingReadableType
   */

  Optional<SyComponentType> componentForWindowPosition(
    PVector2I<SySpaceWindowType> w_position,
    SyComponentQuery query);

  /**
   * Toggle the "maximized" nature of the window.
   *
   * @param viewportSize The size of the maximized window
   */

  void setMaximizeToggle(
    PAreaSizeI<SySpaceViewportType> viewportSize);

  @Override
  AttributeType<Boolean> decorated();

  /**
   * Set the window position.
   *
   * @param newPosition The new position
   */

  void setPosition(
    PVector2I<SySpaceViewportType> newPosition);

  /**
   * Set the window size.
   *
   * @param newSize The new size
   */

  void setSize(
    PAreaSizeI<SySpaceViewportType> newSize);

  /**
   * @return The content area component
   */

  SyComponentType contentArea();

  @Override
  AttributeType<String> title();
}
