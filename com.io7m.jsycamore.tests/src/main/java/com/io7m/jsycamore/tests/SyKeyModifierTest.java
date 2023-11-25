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

import com.io7m.jsycamore.api.keyboard.SyKeyCode;
import com.io7m.jsycamore.api.keyboard.SyKeyModifier;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.io7m.jsycamore.api.keyboard.SyKeyCode.*;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.*;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_ALT_LEFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_ALT_RIGHT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_CONTROL_LEFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_CONTROL_RIGHT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_SHIFT_LEFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_SHIFT_RIGHT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_SUPER_LEFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_SUPER_RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyKeyModifierTest
{
  /**
   * Test properties of the modifier keys.
   */

  @Test
  public void testModifiers()
  {
    assertTrue(SY_MODIFIER_SHIFT_RIGHT.isShift());
    assertFalse(SY_MODIFIER_SHIFT_RIGHT.isControl());
    assertFalse(SY_MODIFIER_SHIFT_RIGHT.isAlt());
    assertFalse(SY_MODIFIER_SHIFT_RIGHT.isSuper());

    assertTrue(SY_MODIFIER_SHIFT_LEFT.isShift());
    assertFalse(SY_MODIFIER_SHIFT_LEFT.isControl());
    assertFalse(SY_MODIFIER_SHIFT_LEFT.isAlt());
    assertFalse(SY_MODIFIER_SHIFT_LEFT.isSuper());

    assertFalse(SY_MODIFIER_CONTROL_RIGHT.isShift());
    assertTrue(SY_MODIFIER_CONTROL_RIGHT.isControl());
    assertFalse(SY_MODIFIER_CONTROL_RIGHT.isAlt());
    assertFalse(SY_MODIFIER_CONTROL_RIGHT.isSuper());

    assertFalse(SY_MODIFIER_CONTROL_LEFT.isShift());
    assertTrue(SY_MODIFIER_CONTROL_LEFT.isControl());
    assertFalse(SY_MODIFIER_CONTROL_LEFT.isAlt());
    assertFalse(SY_MODIFIER_CONTROL_LEFT.isSuper());

    assertFalse(SY_MODIFIER_ALT_RIGHT.isShift());
    assertFalse(SY_MODIFIER_ALT_RIGHT.isControl());
    assertTrue(SY_MODIFIER_ALT_RIGHT.isAlt());
    assertFalse(SY_MODIFIER_ALT_RIGHT.isSuper());

    assertFalse(SY_MODIFIER_ALT_LEFT.isShift());
    assertFalse(SY_MODIFIER_ALT_LEFT.isControl());
    assertTrue(SY_MODIFIER_ALT_LEFT.isAlt());
    assertFalse(SY_MODIFIER_ALT_LEFT.isSuper());

    assertFalse(SY_MODIFIER_SUPER_RIGHT.isShift());
    assertFalse(SY_MODIFIER_SUPER_RIGHT.isControl());
    assertFalse(SY_MODIFIER_SUPER_RIGHT.isAlt());
    assertTrue(SY_MODIFIER_SUPER_RIGHT.isSuper());

    assertFalse(SY_MODIFIER_SUPER_LEFT.isShift());
    assertFalse(SY_MODIFIER_SUPER_LEFT.isControl());
    assertFalse(SY_MODIFIER_SUPER_LEFT.isAlt());
    assertTrue(SY_MODIFIER_SUPER_LEFT.isSuper());
  }

  /**
   * Check that keys map to modifiers.
   */

  @Test
  public void testModifierOfKey()
  {
    assertEquals(
      Optional.of(SY_MODIFIER_ALT_LEFT),
      ofKey(SY_KEY_LEFT_ALT));
    assertEquals(
      Optional.of(SY_MODIFIER_SHIFT_LEFT),
      ofKey(SY_KEY_LEFT_SHIFT));
    assertEquals(
      Optional.of(SY_MODIFIER_CONTROL_LEFT),
      ofKey(SY_KEY_LEFT_CONTROL));
    assertEquals(
      Optional.of(SY_MODIFIER_SUPER_LEFT),
      ofKey(SY_KEY_LEFT_SUPER));
    assertEquals(
      Optional.of(SY_MODIFIER_ALT_RIGHT),
      ofKey(SY_KEY_RIGHT_ALT));
    assertEquals(
      Optional.of(SY_MODIFIER_SHIFT_RIGHT),
      ofKey(SY_KEY_RIGHT_SHIFT));
    assertEquals(
      Optional.of(SY_MODIFIER_CONTROL_RIGHT),
      ofKey(SY_KEY_RIGHT_CONTROL));
    assertEquals(
      Optional.of(SY_MODIFIER_SUPER_RIGHT),
      ofKey(SY_KEY_RIGHT_SUPER));

    for (final var key : SyKeyCode.values()) {
      switch (key) {
        case SY_KEY_LEFT_ALT,
          SY_KEY_LEFT_SHIFT,
          SY_KEY_LEFT_CONTROL,
          SY_KEY_LEFT_SUPER,
          SY_KEY_RIGHT_ALT,
          SY_KEY_RIGHT_SHIFT,
          SY_KEY_RIGHT_CONTROL,
          SY_KEY_RIGHT_SUPER -> {
          break;
        }
        default -> {
          assertEquals(Optional.empty(), ofKey(key));
        }
      }
    }
  }
}
