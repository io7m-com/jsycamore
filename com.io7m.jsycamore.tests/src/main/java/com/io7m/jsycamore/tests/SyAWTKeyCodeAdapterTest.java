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

import com.io7m.jsycamore.api.screens.SyScreenKeyEventsType;
import com.io7m.jsycamore.awt.internal.SyAWTKeyCodeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Component;
import java.awt.event.KeyEvent;

import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_E;
import static com.io7m.jsycamore.api.keyboard.SyKeyModifier.SY_MODIFIER_SHIFT_LEFT;
import static java.awt.event.KeyEvent.KEY_LOCATION_LEFT;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static org.mockito.Mockito.times;

public final class SyAWTKeyCodeAdapterTest
{
  private SyScreenKeyEventsType screen;
  private Component component;

  @BeforeEach
  public void setup()
  {
    this.screen =
      Mockito.mock(SyScreenKeyEventsType.class);
    this.component =
      Mockito.mock(Component.class);
  }

  /**
   * Test that key events are delivered.
   */

  @Test
  public void testKeyEvents()
  {
    final var adapter = new SyAWTKeyCodeAdapter(this.screen);
    adapter.keyPressed(this.keyEventOf(0, VK_E, 'e', 0));

    Mockito.verify(this.screen)
      .keyPressed(SY_KEY_E);

    adapter.keyReleased(this.keyEventOf(0, VK_E, 'e', 0));

    Mockito.verify(this.screen)
      .keyReleased(SY_KEY_E);

    Mockito.verify(this.screen, times(0))
      .keyModifierPressed(Mockito.any());
    Mockito.verify(this.screen, times(0))
      .keyModifierReleased(Mockito.any());
  }

  /**
   * Test that modifier events are delivered.
   */

  @Test
  public void testKeyModifierEvents()
  {
    final var adapter = new SyAWTKeyCodeAdapter(this.screen);
    adapter.keyPressed(this.keyEventOf(VK_SHIFT, VK_SHIFT, 'e', KEY_LOCATION_LEFT));

    Mockito.verify(this.screen)
      .keyModifierPressed(SY_MODIFIER_SHIFT_LEFT);

    adapter.keyReleased(this.keyEventOf(VK_SHIFT, VK_SHIFT, 'e', KEY_LOCATION_LEFT));

    Mockito.verify(this.screen)
      .keyModifierReleased(SY_MODIFIER_SHIFT_LEFT);

    Mockito.verify(this.screen, times(0))
      .keyPressed(Mockito.any());
    Mockito.verify(this.screen, times(0))
      .keyReleased(Mockito.any());
  }

  private KeyEvent keyEventOf(
    final int modifiers,
    final int keyCode,
    final char keyChar,
    final int keyLocation)
  {
    return new KeyEvent(
      this.component,
      0,
      0L,
      modifiers,
      keyCode,
      keyChar,
      keyLocation
    );
  }
}
