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


package com.io7m.jsycamore.api.windows;

import java.math.BigInteger;

/**
 * An identifier for a window layer.
 *
 * @param value The value
 */

public record SyWindowLayerID(BigInteger value)
  implements Comparable<SyWindowLayerID>
{
  private static final SyWindowLayerID FIRST_ID =
    new SyWindowLayerID(BigInteger.ZERO);

  @Override
  public int compareTo(
    final SyWindowLayerID other)
  {
    return this.value.compareTo(other.value);
  }

  /**
   * @return The next layer ID
   */

  public SyWindowLayerID nextHigher()
  {
    return new SyWindowLayerID(this.value.add(BigInteger.ONE));
  }

  /**
   * @return The next layer ID
   */

  public SyWindowLayerID nextLower()
  {
    return new SyWindowLayerID(this.value.subtract(BigInteger.ONE));
  }

  /**
   * @return The default layer ID
   */

  public static SyWindowLayerID defaultLayer()
  {
    return FIRST_ID;
  }
}
