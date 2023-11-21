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

package com.io7m.jsycamore.api.text;

/**
 * A font.
 */

public interface SyFontType
{
  /**
   * Measure the maximum height of text rendered using this font.
   *
   * @return The height of the text
   */

  int textHeight();

  /**
   * Measure the width of the given text using this font.
   *
   * @param text The text
   *
   * @return The width of the text
   */

  int textWidth(String text);

  /**
   * Measure the maximum descent of text rendered using this font.
   *
   * @return The descent of the text
   */

  int textDescent();

  /**
   * The font description.
   *
   * @return The description of the font
   */

  SyFontDescription description();
}
