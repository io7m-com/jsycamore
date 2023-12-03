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

import java.text.Bidi;
import java.util.Objects;

import static com.io7m.jsycamore.api.text.SyTextDirection.TEXT_DIRECTION_LEFT_TO_RIGHT;
import static com.io7m.jsycamore.api.text.SyTextDirection.TEXT_DIRECTION_RIGHT_TO_LEFT;

/**
 * A section of text with a specific direction.
 *
 * @param value     The text value
 * @param direction The text direction
 */

public record SyText(
  String value,
  SyTextDirection direction)
{
  private static final SyText EMPTY =
    new SyText("", TEXT_DIRECTION_LEFT_TO_RIGHT);

  /**
   * A section of text with a specific direction.
   *
   * @param value     The text value
   * @param direction The text direction
   */

  public SyText
  {
    Objects.requireNonNull(value, "value");
    Objects.requireNonNull(direction, "direction");
  }

  /**
   * Create a text section from the given string, using {@link Bidi} to infer
   * the text direction.
   *
   * @param text The text
   *
   * @return A text section
   */

  public static SyText text(
    final String text)
  {
    final var chars = text.toCharArray();
    if (Bidi.requiresBidi(chars, 0, chars.length)) {
      final var bidi = new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
      if (bidi.isRightToLeft()) {
        return new SyText(text, TEXT_DIRECTION_RIGHT_TO_LEFT);
      }
    }
    return new SyText(text, TEXT_DIRECTION_LEFT_TO_RIGHT);
  }

  /**
   * Create a text section from the given string and text direction.
   *
   * @param text      The text
   * @param direction The text direction
   *
   * @return A text section
   */

  public static SyText text(
    final String text,
    final SyTextDirection direction)
  {
    return new SyText(text, direction);
  }

  /**
   * @return An empty text section
   */

  public static SyText empty()
  {
    return EMPTY;
  }
}
