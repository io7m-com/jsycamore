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


package com.io7m.jsycamore.tests;

import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyFontException;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.awt.internal.SyAWTFont;
import com.io7m.jsycamore.awt.internal.SyAWTFontDirectoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.io7m.jsycamore.api.text.SyFontStyle.REGULAR;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyAWTFontTest
{
  private SyFontDirectoryServiceType<SyAWTFont> fonts;
  private SyAWTFont font;

  @BeforeEach
  public void setup()
    throws SyFontException
  {
    this.fonts =
      SyAWTFontDirectoryService.createFromServiceLoader();
    this.font =
      this.fonts.get(
        new SyFontDescription("DejaVu Sans", REGULAR, 12)
      );
  }

  @Test
  public void testLayout()
    throws Exception
  {
    try (var s = SyAWTFontTest.class.getResourceAsStream(
      "/com/io7m/jsycamore/tests/arctic.txt")) {
      final var text =
        SyText.text(new String(s.readAllBytes(), StandardCharsets.UTF_8));

      final var r =
        new ArrayList<>(this.font.textLayoutMultiple(List.of(text), 0,320));

      assertEquals(26, r.size());
      check(r, "For three hundred years explorers have been ");
      check(r, "active in pushing aside the realms of the unknown ");
      check(r, "towards the north pole; but the equally interesting ");
      check(r, "south pole has, during all this time, been almost ");
      check(r, "wholly neglected. There have been expeditions to ");
      check(r, "the far south, but compared to arctic ventures ");
      check(r, "they have been so few and their work within the ");
      check(r, "polar circle has been so little that the results have ");
      check(r, "been largely forgotten. It is not because valuable ");
      check(r, "results have not been obtained in the antarctic, ");
      check(r, "but because the popular interest in the arctic has ");
      check(r, "completely overshadowed the reports of the ");
      check(r, "antipodes. The search for the North-west and the ");
    }
  }

  private static void check(
    final List<SyTextLineMeasuredType> lines,
    final String expected)
  {
    assertEquals(expected, lines.remove(0).text().value());
  }
}
