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
import com.io7m.jsycamore.api.SyBoundedType;
import com.io7m.jsycamore.api.SyThemeType;
import com.io7m.jsycamore.api.events.SyEventReceiverType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Optional;

/**
 * The type of components.
 */

public interface SyComponentType
  extends SyComponentReadableType,
  SyVisibleType,
  SyActiveType,
  SyEventReceiverType,
  SyBoundedType<SySpaceParentRelativeType>
{
  /**
   * @return The tree node for this component
   */

  JOTreeNodeType<SyComponentType> node();

  /**
   * @return The window to which the component is attached, if any
   */

  Optional<SyWindowType> window();

  PAreaSizeI<SySpaceParentRelativeType> layout(
    SyThemeType theme,
    SyConstraints constraints);

  Optional<SyComponentType> componentForWindowRelative(
    PVector2I<SySpaceWindowType> windowPosition,
    SyWindowViewportAccumulatorType context);
}
