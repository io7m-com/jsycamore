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

package com.io7m.jsycamore.api.bounded;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.sized.SySizedType;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

/**
 * Writable access to information about objects that have bounds.
 *
 * @param <T> The coordinate space
 */

public interface SyBoundedType<T extends SySpaceType>
  extends SyBoundedReadableType<T>, SySizedType<T>
{
  @Override
  AttributeType<PVector2I<T>> position();

  /**
   * Set the position of the object.
   *
   * @param newPosition The new position
   */

  default void setPosition(
    final PVector2I<T> newPosition)
  {
    this.position().set(newPosition);
  }
}
