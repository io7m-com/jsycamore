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


package com.io7m.jsycamore.tests;

import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyFontStyle;
import com.io7m.jsycamore.api.text.SyTextID;
import com.io7m.jsycamore.api.text.SyTextLineNumber;
import com.io7m.jsycamore.api.text.SyTextMultiLineModelType;
import com.io7m.jsycamore.awt.internal.SyAWTFont;
import com.io7m.jsycamore.awt.internal.SyAWTFontDirectoryService;
import com.io7m.jsycamore.components.standard.text.SyTextMultiLineModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.StreamSupport;

import static com.io7m.jsycamore.api.text.SyText.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyTextMultiLineModelTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyTextMultiLineModelTest.class);

  private SyFontDirectoryServiceType<SyAWTFont> fontsAWT;
  private SyAWTFont font;

  @BeforeEach
  public void setup()
    throws Exception
  {
    this.fontsAWT =
      SyAWTFontDirectoryService.createFromServiceLoader();
    this.font =
      this.fontsAWT.get(
        new SyFontDescription("DejaVu Sans", SyFontStyle.REGULAR, 11)
      );
  }

  /**
   * Inserting texts causes renumbering.
   */

  @Test
  public void testInsertRenumbers0()
  {
    final var m =
      SyTextMultiLineModel.create(this.font, 128);

    m.textSectionAppend(text("Hello line A."));
    m.textSectionAppend(text("Hello line B."));
    m.textSectionAppend(text("Hello line C."));

    assertEquals(3, m.lineCount());
    assertEquals("Hello line A.", lineTextOf(m, 0));
    assertEquals("Hello line B.", lineTextOf(m, 1));
    assertEquals("Hello line C.", lineTextOf(m, 2));
    assertEquals(0, lineNumberOf(m, 0));
    assertEquals(1, lineNumberOf(m, 1));
    assertEquals(2, lineNumberOf(m, 2));
    assertEquals(3 * 14, m.minimumSizeYRequired());

    /*
     * The first line is removed, and then replaced with five lines, yielding
     * a total of seven lines.
     */

    m.textSectionReplace(
      SyTextID.first(),
      text("Expressions moisturisers filtrate rumouring apportioned treachery.")
    );

    assertEquals(7, m.lineCount());
    assertEquals("Expressions ", lineTextOf(m, 0));
    assertEquals("moisturisers filtrate ", lineTextOf(m, 1));
    assertEquals("rumouring ", lineTextOf(m, 2));
    assertEquals("apportioned ", lineTextOf(m, 3));
    assertEquals("treachery.", lineTextOf(m, 4));
    assertEquals("Hello line B.", lineTextOf(m, 5));
    assertEquals("Hello line C.", lineTextOf(m, 6));

    assertEquals(0, lineNumberOf(m, 0));
    assertEquals(1, lineNumberOf(m, 1));
    assertEquals(2, lineNumberOf(m, 2));
    assertEquals(3, lineNumberOf(m, 3));
    assertEquals(4, lineNumberOf(m, 4));
    assertEquals(5, lineNumberOf(m, 5));
    assertEquals(6, lineNumberOf(m, 6));
    assertEquals(7 * 14, m.minimumSizeYRequired());
  }

  /**
   * Inserting texts causes renumbering.
   */

  @Test
  public void testInsertRenumbers1()
  {
    final var m =
      SyTextMultiLineModel.create(this.font, 128);

    m.textSectionAppend(text("Expressions moisturisers filtrate rumouring apportioned treachery."));
    m.textSectionAppend(text("Hello line B."));
    m.textSectionAppend(text("Hello line C."));

    assertEquals(7, m.lineCount());
    assertEquals("Expressions ", lineTextOf(m, 0));
    assertEquals("moisturisers filtrate ", lineTextOf(m, 1));
    assertEquals("rumouring ", lineTextOf(m, 2));
    assertEquals("apportioned ", lineTextOf(m, 3));
    assertEquals("treachery.", lineTextOf(m, 4));
    assertEquals("Hello line B.", lineTextOf(m, 5));
    assertEquals("Hello line C.", lineTextOf(m, 6));

    assertEquals(0, lineNumberOf(m, 0));
    assertEquals(1, lineNumberOf(m, 1));
    assertEquals(2, lineNumberOf(m, 2));
    assertEquals(3, lineNumberOf(m, 3));
    assertEquals(4, lineNumberOf(m, 4));
    assertEquals(5, lineNumberOf(m, 5));
    assertEquals(6, lineNumberOf(m, 6));
    assertEquals(7 * 14, m.minimumSizeYRequired());

    /*
     * The first five lines are removed, and then replaced with one lines,
     * yielding a total of 3 lines.
     */

    m.textSectionReplace(
      SyTextID.first(),
      text("Hello line A.")
    );

    assertEquals(3, m.lineCount());
    assertEquals("Hello line A.", lineTextOf(m, 0));
    assertEquals("Hello line B.", lineTextOf(m, 1));
    assertEquals("Hello line C.", lineTextOf(m, 2));
    assertEquals(0, lineNumberOf(m, 0));
    assertEquals(1, lineNumberOf(m, 1));
    assertEquals(2, lineNumberOf(m, 2));
    assertEquals(3 * 14, m.minimumSizeYRequired());
  }

  /**
   * Appending texts preserves numbering.
   */

  @Test
  public void testAppendNumbers()
  {
    final var m =
      SyTextMultiLineModel.create(this.font, 128);

    m.textSectionAppend(text("Hello line A."));
    m.textSectionAppend(text("Hello line B."));
    m.textSectionAppend(text("Hello line C."));

    assertEquals("Hello line A.", lineTextOf(m, 0));
    assertEquals("Hello line B.", lineTextOf(m, 1));
    assertEquals("Hello line C.", lineTextOf(m, 2));
    assertEquals(0, lineNumberOf(m, 0));
    assertEquals(1, lineNumberOf(m, 1));
    assertEquals(2, lineNumberOf(m, 2));
    assertEquals(3 * 14, m.minimumSizeYRequired());

    m.textSectionAppend(text("Hello line D."));

    assertEquals(4, m.lineCount());
    assertEquals("Hello line A.", lineTextOf(m, 0));
    assertEquals("Hello line B.", lineTextOf(m, 1));
    assertEquals("Hello line C.", lineTextOf(m, 2));
    assertEquals("Hello line D.", lineTextOf(m, 3));
    assertEquals(0, lineNumberOf(m, 0));
    assertEquals(1, lineNumberOf(m, 1));
    assertEquals(2, lineNumberOf(m, 2));
    assertEquals(3, lineNumberOf(m, 3));
    assertEquals(4 * 14, m.minimumSizeYRequired());
  }

  private static String lineTextOf(
    final SyTextMultiLineModelType m,
    final int value)
  {
    return m.lineAt(new SyTextLineNumber(value))
      .orElseThrow()
      .textLine()
      .textAsWrapped()
      .value();
  }

  private static int lineNumberOf(
    final SyTextMultiLineModelType m,
    final int value)
  {
    return m.lineAt(new SyTextLineNumber(value))
      .orElseThrow()
      .textLineNumber()
      .value();
  }
}
