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

package com.io7m.jsycamore.core;

import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

/**
 * The type of readable components.
 */

public interface SyComponentReadableType
{
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

  PVectorReadable2IType<SySpaceParentRelativeType> positionParentRelative();

  /**
   * @return The position of the component relative to its parent window
   */

  PVectorReadable2IType<SySpaceWindowRelativeType> positionWindowRelative();

  /**
   * @return The size of the component
   */

  VectorReadable2IType size();

  /**
   * @return The window to which this component belongs
   */

  SyWindowReadableType window();

  /**
   * @param position A window-relative position
   *
   * @return {@code true} iff {@code position} is within the bounds of this
   * component
   */

  boolean containsWindowRelative(
    PVectorReadable2IType<SySpaceWindowRelativeType> position);
}
