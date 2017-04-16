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

package com.io7m.jsycamore.core.boxes;

import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jsycamore.core.SySpaceType;
import org.immutables.value.Value;
import org.valid4j.Assertive;

/**
 * The type of boxes expressed as half-closed ranges on the X and Y axis.
 *
 * @param <S> A phantom type parameter indicating the coordinate space of the
 *            box
 */

@SyImmutableStyleType
@Value.Immutable
@Value.Modifiable
public interface SyBoxType<S extends SySpaceType>
{
  /**
   * Check the preconditions for the parameters.
   */

  @Value.Check
  default void checkPreconditions()
  {
    Assertive.require(
      this.maximumX() >= this.minimumX(), "X maximum >= X minimum");
    Assertive.require(
      this.maximumY() >= this.minimumY(), "Y maximum >= Y minimum");
  }

  /**
   * @return The value on the X axis of the left edge of the box (inclusive)
   */

  @Value.Parameter(order = 0)
  int minimumX();

  /**
   * @return The value on the X axis of the right edge of the box (exclusive)
   */

  @Value.Parameter(order = 1)
  int maximumX();

  /**
   * @return The value on the Y axis of the top edge of the box (inclusive)
   */

  @Value.Parameter(order = 2)
  int minimumY();

  /**
   * @return The value on the Y axis of the bottom edge of the box (exclusive)
   */

  @Value.Parameter(order = 3)
  int maximumY();

  /**
   * @return The width of the box
   */

  default int width()
  {
    return Math.subtractExact(this.maximumX(), this.minimumX());
  }

  /**
   * @return The height of the box
   */

  default int height()
  {
    return Math.subtractExact(this.maximumY(), this.minimumY());
  }
}
