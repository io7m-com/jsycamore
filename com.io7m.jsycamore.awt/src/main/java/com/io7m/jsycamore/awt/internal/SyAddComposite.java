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

public final class SyAddComposite extends SyAbstractComposite
{
  private static final SyAddComposite INSTANCE =
    new SyAddComposite();

  public static SyAddComposite get()
  {
    return INSTANCE;
  }

  private SyAddComposite()
  {

  }

  @Override
  protected void mix(
    final double[] srcData,
    final double[] dstData,
    final double[] outData)
  {
    outData[0] = Math.min(1.0, srcData[0] + dstData[0]);
    outData[1] = Math.min(1.0, srcData[1] + dstData[1]);
    outData[2] = Math.min(1.0, srcData[2] + dstData[2]);
    outData[3] = Math.min(1.0, srcData[3] + dstData[3]);
  }
}
