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

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public abstract class SyAbstractComposite
  implements Composite, CompositeContext
{
  protected SyAbstractComposite()
  {

  }

  @Override
  public final CompositeContext createContext(
    final ColorModel srcColorModel,
    final ColorModel dstColorModel,
    final RenderingHints hints)
  {
    checkColorModel(srcColorModel, "source");
    checkColorModel(dstColorModel, "destination");
    return this;
  }

  private static void checkColorModel(
    final ColorModel colorModel,
    final String source)
  {
    if (!colorModel.isAlphaPremultiplied()) {
      throw new IllegalArgumentException(String.format(
        "The color model for the %s image must have premultiplied alpha.",
        source)
      );
    }
  }

  @Override
  public void dispose()
  {

  }

  @Override
  public final void compose(
    final Raster src,
    final Raster dst,
    final WritableRaster output)
  {
    checkRaster(src);
    checkRaster(dst);
    checkRaster(output);

    final int width =
      Math.min(src.getWidth(), dst.getWidth());
    final int height =
      Math.min(src.getHeight(), dst.getHeight());

    final var srcData = new double[4];
    final var dstData = new double[4];
    final var outData = new double[4];

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        src.getPixel(x, y, srcData);
        dst.getPixel(x, y, dstData);

        srcData[0] /= 255.0;
        srcData[1] /= 255.0;
        srcData[2] /= 255.0;
        srcData[3] /= 255.0;

        dstData[0] /= 255.0;
        dstData[1] /= 255.0;
        dstData[2] /= 255.0;
        dstData[3] /= 255.0;

        this.mix(srcData, dstData, outData);

        outData[0] *= 255.0;
        outData[1] *= 255.0;
        outData[2] *= 255.0;
        outData[3] *= 255.0;

        output.setPixel(x, y, outData);
      }
    }
  }

  protected abstract void mix(
    double[] srcData,
    double[] dstData,
    double[] outData);

  private static void checkRaster(
    final Raster src)
  {
    if (src.getNumBands() != 4) {
      throw new IllegalArgumentException(
        "Compositing only works with RGBA images");
    }
  }
}
