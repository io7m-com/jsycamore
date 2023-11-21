/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.components.standard.forms;

/**
 * The base type of column size constraints.
 */

public sealed interface SyFormColumnSizeType
  permits SyFormColumnSizeExact,
  SyFormColumnSizeFlexible
{
  /**
   * A column must be exactly {@code size}.
   *
   * @param size The size
   *
   * @return A size constraint
   */

  static SyFormColumnSizeExact exact(final int size)
  {
    return new SyFormColumnSizeExact(size);
  }

  /**
   * A column is of a flexible size, and will use up any remaining size in the
   * row.
   *
   * @return A size constraint
   */

  static SyFormColumnSizeFlexible flexible()
  {
    return SyFormColumnSizeFlexible.FLEXIBLE;
  }
}
