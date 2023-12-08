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

import com.io7m.jsycamore.api.themes.SyThemeClassNameCustom;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SyThemeClassNamesTest
{
  private static DynamicTest validClassNameOf(
    final String text)
  {
    return DynamicTest.dynamicTest("testValid_" + text, () -> {
      assertEquals(text, new SyThemeClassNameCustom(text, "A").className());
      assertEquals(text, new SyThemeClassNameCustom(text, "A").toString());
    });
  }

  private static DynamicTest invalidClassNameOf(
    final String text)
  {
    return DynamicTest.dynamicTest("testInvalid_" + text, () -> {
      assertThrows(IllegalArgumentException.class, () -> {
        new SyThemeClassNameCustom(text, "A");
      });
    });
  }

  @Test
  public void testClassNamesStandard()
  {
    final var names = new HashSet<SyThemeClassNameStandard>();

    for (final var name : SyThemeClassNameStandard.values()) {
      assertEquals(name.className(), name.toString());
      names.add(name);
    }

    assertEquals(names.size(), SyThemeClassNameStandard.values().length);
  }

  @TestFactory
  public Stream<DynamicTest> testClassNameCustomInvalid()
  {
    return Stream.of(" ", "@", "#", "a", "A_", "9")
      .map(SyThemeClassNamesTest::invalidClassNameOf);
  }

  @TestFactory
  public Stream<DynamicTest> testClassNameCustomValid()
  {
    return Stream.of("Aa", "B", "C90")
      .map(SyThemeClassNamesTest::validClassNameOf);
  }
}
