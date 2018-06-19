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

package com.io7m.jsycamore.api.themes;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.immutables.value.Value;

/**
 * An outline specification.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyThemeOutlineType
{
  /**
   * @return {@code true} iff the left edge should have an outline
   */

  @Value.Parameter(order = 0)
  boolean left();

  /**
   * @return {@code true} iff the right edge should have an outline
   */

  @Value.Parameter(order = 1)
  boolean right();

  /**
   * @return {@code true} iff the top edge should have an outline
   */

  @Value.Parameter(order = 2)
  boolean top();

  /**
   * @return {@code true} iff the bottom edge should have an outline
   */

  @Value.Parameter(order = 3)
  boolean bottom();

  /**
   * @return The color of the outline when the element is active
   */

  @Value.Parameter(order = 4)
  Vector3D colorActive();

  /**
   * @return The color of the outline when the element is inactive
   */

  @Value.Parameter(order = 5)
  Vector3D colorInactive();

  /**
   * @return {@code true} iff corner pixels should be rendered
   */

  @Value.Parameter(order = 6)
  boolean corners();
}
