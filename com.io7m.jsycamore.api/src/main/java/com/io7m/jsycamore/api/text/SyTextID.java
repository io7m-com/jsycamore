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

import java.math.BigInteger;

/**
 * An identifier for a section of text.
 *
 * @param value The value
 */

public record SyTextID(BigInteger value)
  implements Comparable<SyTextID>
{
  private static final SyTextID FIRST_ID =
    new SyTextID(BigInteger.ZERO);

  /**
   * @return The first text ID
   */

  public static SyTextID first()
  {
    return FIRST_ID;
  }

  @Override
  public int compareTo(
    final SyTextID other)
  {
    return this.value.compareTo(other.value);
  }

  @Override
  public String toString()
  {
    return this.value.toString();
  }

  /**
   * @return The next text ID
   */

  public SyTextID next()
  {
    return new SyTextID(this.value.add(BigInteger.ONE));
  }

  /**
   * @return The previous text ID
   */

  public SyTextID previous()
  {
    return new SyTextID(this.value.subtract(BigInteger.ONE));
  }
}
