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

package com.io7m.jsycamore.awt;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import org.immutables.value.Value;

import java.awt.Paint;
import java.util.Optional;

/**
 * Parameters for drawing embossed rectangles.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyAWTEmbossedRectangleType
{
  /**
   * @return The rectangle area
   */

  @Value.Parameter
  PAreaI<?> box();

  /**
   * @return The thickness of the emboss effect
   */

  @Value.Parameter
  int embossSize();

  /**
   * @return The color of the paintLeft emboss
   */

  @Value.Parameter
  Paint paintLeft();

  /**
   * @return The color of the paintRight emboss
   */

  @Value.Parameter
  Paint paintRight();

  /**
   * @return The color of the paintTop emboss
   */

  @Value.Parameter
  Paint paintTop();

  /**
   * @return The color of the paintBottom emboss
   */

  @Value.Parameter
  Paint paintBottom();

  /**
   * @return The paintFill color for the rectangle, if filling is required
   */

  @Value.Parameter
  Optional<Paint> paintFill();

  /**
   * Check preconditions for the type.
   */

  @Value.Check
  default void checkPreconditions()
  {
    Preconditions.checkPreconditionI(
      this.embossSize(),
      this.embossSize() > 0,
      i -> "Emboss area size must be positive");
  }
}
