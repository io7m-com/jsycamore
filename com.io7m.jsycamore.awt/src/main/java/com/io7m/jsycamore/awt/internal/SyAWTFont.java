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
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextID;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.api.text.SyTextLineNumber;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

/**
 * A font loaded using AWT.
 */

public final class SyAWTFont implements SyFontType
{
  private final Font font;
  private final FontMetrics metrics;
  private final SyFontDescription description;

  SyAWTFont(
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
  public List<SyTextLineMeasuredType> textLayoutMultiple(
    final SortedMap<SyTextID, SyText> texts,
    final SyTextLineNumber firstLineNumber,
    final int pageWidth)
  {
    Objects.requireNonNull(texts, "texts");

    if (texts.isEmpty()) {
      return List.of();
    }

    final var results =
      new LinkedList<SyTextLineMeasuredType>();

    var lineNumber = firstLineNumber;

    for (final var textEntry : texts.entrySet()) {
      final var textID =
        textEntry.getKey();
      final var text =
        textEntry.getValue();

      if (text.value().isEmpty()) {
        results.add(this.emptySectionLine(pageWidth, textID, lineNumber));
        lineNumber = lineNumber.next();
        continue;
      }

      final var attributedString = new AttributedString(text.value());
      attributedString.addAttribute(
        TextAttribute.FONT,
        this.font
      );
      attributedString.addAttribute(
        TextAttribute.RUN_DIRECTION,
        switch (text.direction()) {
          case TEXT_DIRECTION_LEFT_TO_RIGHT -> TextAttribute.RUN_DIRECTION_LTR;
          case TEXT_DIRECTION_RIGHT_TO_LEFT -> TextAttribute.RUN_DIRECTION_RTL;
        }
      );

      final var breaker =
        new LineBreakMeasurer(
          attributedString.getIterator(),
          this.metrics.getFontRenderContext()
        );

      final var fWidth = (float) pageWidth;

      var indexThen = 0;

      while (true) {
        final var layout = breaker.nextLayout(fWidth);
        if (layout == null) {
          break;
        }

        final var indexNow =
          breaker.getPosition();
        final var brokenText =
          text.value().substring(indexThen, indexNow);

        final var textWidth =
          this.textWidth(brokenText);

        final var line =
          new SyAWTTextAnalyzed(
            pageWidth,
            PAreaSizeI.of(textWidth, this.textHeight()),
            lineNumber,
            layout,
            textID,
            new SyText(brokenText, text.direction())
          );

        results.add(line);
        indexThen = indexNow;
        lineNumber = lineNumber.next();
      }
    }

    return results;
  }

  private SyTextLineMeasuredType emptySectionLine(
    final int pageWidth,
    final SyTextID textID,
    final SyTextLineNumber lineNumber)
  {
      return new SyAWTTextAnalyzed(
        pageWidth,
        PAreaSizeI.of(0, this.textHeight()),
        lineNumber,
        new TextLayout(" ", this.font, this.metrics.getFontRenderContext()),
        textID,
        SyText.empty()
      );
  }
}
