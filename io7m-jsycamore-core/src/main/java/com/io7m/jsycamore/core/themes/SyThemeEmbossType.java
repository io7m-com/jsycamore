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
 * Embossing parameters.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeEmbossType
{
  /**
   * @return The color used for the top edges of embossed shapes
   */

  @Value.Parameter
  VectorI3F colorTop();

  /**
   * @return The color used for the bottom edges of embossed shapes
   */

  @Value.Parameter
  VectorI3F colorBottom();

  /**
   * @return The color used for the left edges of embossed shapes
   */

  @Value.Parameter
  VectorI3F colorLeft();

  /**
   * @return The color used for the right edges of embossed shapes
   */

  @Value.Parameter
  VectorI3F colorRight();

  /**
   * @return The size of embossed edges
   */

  @Value.Parameter
  int size();
}
