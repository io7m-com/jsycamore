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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.text.SyTextSectionLineType;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
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

  @Override
  public List<SyTextSectionLineType> textLayout(
    final String text,
    final int width)
  {
    Objects.requireNonNull(text, "text");

    if (text.isEmpty()) {
      return List.of(
        new SectionLine(PAreaSizeI.of(0, this.textHeight()), "")
      );
    }

    final var attributedString = new AttributedString(text);
    attributedString.addAttribute(TextAttribute.FONT, this.font);

    final var breaker =
      new LineBreakMeasurer(
        attributedString.getIterator(),
        this.metrics.getFontRenderContext()
      );

    final var fWidth = (float) width;

    var indexThen = 0;

    final var results =
      new LinkedList<SyTextSectionLineType>();

    while (true) {
      final var layout = breaker.nextLayout(fWidth);
      if (layout == null) {
        break;
      }

      final var indexNow =
        breaker.getPosition();
      final var bounds =
        layout.getBounds();

      final var line =
        new SectionLine(
          PAreaSizeI.of((int) Math.ceil(bounds.getWidth()), this.textHeight()),
          text.substring(indexThen, indexNow)
        );

      results.add(line);
      indexThen = indexNow;
    }

    return results;
  }

  private record SectionLine(
    PAreaSizeI<SySpaceParentRelativeType> size,
    String text)
    implements SyTextSectionLineType
  {

  }
}
