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

package com.io7m.jsycamore.awt.internal;

public final class SyOverlayComposite extends SyAbstractComposite
{
  private static final SyOverlayComposite INSTANCE =
    new SyOverlayComposite();

  public static SyOverlayComposite get()
  {
    return INSTANCE;
  }

  private SyOverlayComposite()
  {

  }

  @Override
  protected void mix(
    final double[] srcData,
    final double[] dstData,
    final double[] outData)
  {
    for (var index = 0; index < 4; ++index) {
      final var dc = dstData[index];
      final var sc = srcData[index];

      if (dc > 0.5) {
        final var value = (sc - dc) / 0.5;
        final var min = dc - (sc - dc);
        outData[index] = sc * value + min;
      } else {
        final var value = dc / 0.5;
        outData[index] = sc * value;
      }
    }
  }
}
