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

package com.io7m.jsycamore.core.themes;

import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jtensors.VectorI3F;
import org.immutables.value.Value;

/**
 * An outline specification.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeOutlineType
{
  /**
   * @return {@code true} iff the left edge should have an outline
   */

  @Value.Parameter(order = 0)
  @Value.Default
  default boolean left()
  {
    return true;
  }

  /**
   * @return {@code true} iff the right edge should have an outline
   */

  @Value.Parameter(order = 1)
  @Value.Default
  default boolean right()
  {
    return true;
  }

  /**
   * @return {@code true} iff the top edge should have an outline
   */

  @Value.Parameter(order = 2)
  @Value.Default
  default boolean top()
  {
    return true;
  }

  /**
   * @return {@code true} iff the bottom edge should have an outline
   */

  @Value.Parameter(order = 3)
  @Value.Default
  default boolean bottom()
  {
    return true;
  }

  /**
   * @return The color of the outline when the element is active
   */

  @Value.Parameter(order = 4)
  VectorI3F colorActive();

  /**
   * @return The color of the outline when the element is inactive
   */

  @Value.Parameter(order = 5)
  VectorI3F colorInactive();

  /**
   * @return {@code true} iff corner pixels should be rendered
   */

  @Value.Parameter(order = 6)
  @Value.Default
  default boolean corners()
  {
    return true;
  }
}
