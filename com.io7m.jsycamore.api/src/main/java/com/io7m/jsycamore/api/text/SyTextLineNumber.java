/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.api.text;

/**
 * A line number.
 *
 * @param value The line number
 */

public record SyTextLineNumber(int value)
  implements Comparable<SyTextLineNumber>
{
  private static final SyTextLineNumber FIRST =
    new SyTextLineNumber(0);

  /**
   * @return The first line number
   */

  public static SyTextLineNumber first()
  {
    return FIRST;
  }

  @Override
  public String toString()
  {
    return Integer.toUnsignedString(this.value);
  }

  @Override
  public int compareTo(
    final SyTextLineNumber o)
  {
    return Integer.compareUnsigned(this.value, o.value);
  }

  /**
   * @return The next line number
   */

  public SyTextLineNumber next()
  {
    return new SyTextLineNumber(this.value + 1);
  }

  /**
   * @param delta The delta
   *
   * @return The line number adjusted by the given delta
   */

  public SyTextLineNumber adjust(
    final int delta)
  {
    return new SyTextLineNumber(this.value + delta);
  }
}
