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

package com.io7m.jsycamore.awt;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import net.jcip.annotations.NotThreadSafe;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;

/**
 * The default implementation of the {@link SyTextMeasurementType} interface.
 */

@NotThreadSafe
public final class SyAWTTextMeasurement implements SyTextMeasurementType,
  SyAWTFontCacheType
{
  private final Graphics2D graphics;
  private final WeakHashMap<String, Font> font_cache;

  private SyAWTTextMeasurement()
  {
    final BufferedImage image =
      new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR_PRE);
    this.graphics = image.createGraphics();
    this.font_cache = new WeakHashMap<>(8);
  }

  /**
   * @return A new text measurement instance
   */

  public static SyAWTTextMeasurement create()
  {
    return new SyAWTTextMeasurement();
  }

  @Override
  public int measureTextHeight(final String font)
  {
    final FontMetrics metrics =
      this.graphics.getFontMetrics(this.decodeFont(font));
    return metrics.getHeight();
  }

  @Override
  public int measureTextWidth(
    final String font,
    final String text)
  {
    final FontMetrics metrics =
      this.graphics.getFontMetrics(this.decodeFont(font));
    return metrics.stringWidth(text);
  }

  @Override
  public Font decodeFont(final String font)
  {
    NullCheck.notNull(font);
    return this.font_cache.computeIfAbsent(font, Font::decode);
  }

  @Override
  public int measureTextDescent(final String font)
  {
    NullCheck.notNull(font);
    final FontMetrics metrics =
      this.graphics.getFontMetrics(this.decodeFont(font));
    return metrics.getDescent();
  }
}
