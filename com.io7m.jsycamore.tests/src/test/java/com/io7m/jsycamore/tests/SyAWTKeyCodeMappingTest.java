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

import com.io7m.jsycamore.awt.internal.SyAWTKeyCodeMapping;
import com.io7m.jsycamore.awt.internal.SyAWTKeyContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F25;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_LEFT_SUPER;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_MENU;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_RIGHT_SUPER;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.values;
import static java.awt.event.KeyEvent.KEY_LOCATION_LEFT;
import static java.awt.event.KeyEvent.KEY_LOCATION_NUMPAD;
import static java.awt.event.KeyEvent.KEY_LOCATION_RIGHT;
import static java.awt.event.KeyEvent.KEY_LOCATION_STANDARD;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyAWTKeyCodeMappingTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyAWTKeyCodeMappingTest.class);

  /**
   * Test that all AWT key codes map to something.
   *
   * @throws Exception On errors
   */

  @Test
  public void testKeycodesUsed()
    throws Exception
  {
    final var mapping = new SyAWTKeyCodeMapping();

    final var fields =
      Stream.of(KeyEvent.class.getFields())
        .filter(field -> field.getName().startsWith("VK_"))
        .filter(field -> field.getType().equals(int.class))
        .filter(field -> (field.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
        .toList();

    final var remaining =
      new HashSet<>(List.of(values()));

    final var locations =
      List.of(
        KEY_LOCATION_NUMPAD,
        KEY_LOCATION_LEFT,
        KEY_LOCATION_RIGHT,
        KEY_LOCATION_STANDARD
      );

    for (final var field : fields) {
      final var value = field.getInt(KeyEvent.class);
      final var name = field.getName();

      for (var location : locations) {
        final var keyCode =
          mapping.toKeyCode(new SyAWTKeyContext(location), value);

        LOG.debug(
          "{}: 0x{} {} -> {}",
          name,
          Integer.toUnsignedString(location, 16),
          value,
          keyCode
        );
        remaining.remove(keyCode);
      }
    }

    assertEquals(Set.of(
      SY_KEY_MENU,
      SY_KEY_RIGHT_SUPER,
      SY_KEY_LEFT_SUPER,
      SY_KEY_F25
    ), remaining);
  }
}
