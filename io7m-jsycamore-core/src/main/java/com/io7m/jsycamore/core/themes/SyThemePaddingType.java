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
import org.immutables.value.Value;

/**
 * A padding specification.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemePaddingType
{
  /**
   * @return The size of the left padding
   */

  @Value.Parameter
  @Value.Default
  default int paddingLeft()
  {
    return 0;
  }

  /**
   * @return The size of the right padding
   */

  @Value.Parameter
  @Value.Default
  default int paddingRight()
  {
    return 0;
  }

  /**
   * @return The size of the top padding
   */

  @Value.Parameter
  @Value.Default
  default int paddingTop()
  {
    return 0;
  }

  /**
   * @return The size of the bottom padding
   */

  @Value.Parameter
  @Value.Default
  default int paddingBottom()
  {
    return 0;
  }
}
