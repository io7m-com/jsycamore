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

package com.io7m.jsycamore.core;

/**
 * The type of text measurement interfaces.
 */

public interface SyTextMeasurementType
{
  /**
   * Measure the maximum height of text rendered using the given font.
   *
   * @param font The font
   *
   * @return The height of the text
   */

  int measureTextHeight(String font);

  /**
   * Measure the width of the given text using the given font.
   *
   * @param font The font
   * @param text The text
   *
   * @return The width of the text
   */

  int measureTextWidth(
    String font,
    String text);

  /**
   * Measure the maximum descent of text rendered using the given font.
   *
   * @param font The font
   *
   * @return The descent of the text
   */

  int measureTextDescent(String font);

}
