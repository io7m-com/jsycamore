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


package com.io7m.jsycamore.awt.internal;

import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontType;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Objects;

/**
 * A font loaded using AWT.
 */

public final class SyFontAWT implements SyFontType
{
  private final Font font;
  private final FontMetrics metrics;
  private final SyFontDescription description;

  SyFontAWT(
    final FontMetrics inFontMetrics,
    final Font inFont,
    final SyFontDescription inFontDescription)
  {
    this.metrics =
      Objects.requireNonNull(inFontMetrics, "metrics");
    this.font =
      Objects.requireNonNull(inFont, "font");
    this.description =
      Objects.requireNonNull(inFontDescription, "description");
  }

  /**
   * @return The loaded AWT font
   */

  public Font font()
  {
    return this.font;
  }

  @Override
  public int textHeight()
  {
    return this.metrics.getHeight();
  }

  @Override
  public int textWidth(
    final String text)
  {
    return this.metrics.stringWidth(text);
  }

  @Override
  public int textDescent()
  {
    return this.metrics.getDescent();
  }

  @Override
  public SyFontDescription description()
  {
    return this.description;
  }
}
