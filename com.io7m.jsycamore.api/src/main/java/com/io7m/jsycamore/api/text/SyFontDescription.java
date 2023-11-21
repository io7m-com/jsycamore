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

import java.util.Objects;

/**
 * A font description.
 *
 * @param family The font family, such as "IBM Plex Sans Mono"
 * @param style  The font style
 * @param size   The font size
 */

public record SyFontDescription(
  String family,
  SyFontStyle style,
  int size)
{
  /**
   * A font description.
   *
   * @param family The font family, such as "IBM Plex Sans Mono"
   * @param style  The font style
   * @param size   The font size
   */

  public SyFontDescription
  {
    Objects.requireNonNull(family, "family");
    Objects.requireNonNull(style, "style");
  }

  /**
   * Produce an identifier for this font, such as "IBM Plex Sans Mono Bold 16".
   *
   * @return An identifier for this font
   */

  public String identifier()
  {
    return new StringBuilder(32)
      .append(this.family)
      .append(this.showStyle())
      .append(' ')
      .append(this.size)
      .toString();
  }

  private String showStyle()
  {
    return switch (this.style) {
      case BOLD -> " Bold";
      case ITALIC -> " Italic";
      case REGULAR -> "";
      case BOLD_ITALIC -> " Bold Italic";
    };
  }
}
