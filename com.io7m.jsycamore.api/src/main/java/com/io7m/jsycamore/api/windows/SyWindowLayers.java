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


package com.io7m.jsycamore.api.windows;

/**
 * Conventional values for window layers.
 */

public final class SyWindowLayers
{
  private SyWindowLayers()
  {

  }

  /**
   * @return The layer identifier used for normal windows
   */

  public static int layerForNormalWindows()
  {
    return 0;
  }

  /**
   * @return The layer identifier used for menus
   */

  public static int layerForMenus()
  {
    return layerHighest();
  }

  /**
   * @return The lowest (furthest away from the viewer) layer
   */

  public static int layerLowest()
  {
    return Integer.MIN_VALUE;
  }

  /**
   * @return The highest (closest to the viewer) layer
   */

  public static int layerHighest()
  {
    return Integer.MAX_VALUE;
  }
}
