/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.screens;

import com.io7m.jsycamore.api.keyboard.SyKeyCode;
import com.io7m.jsycamore.api.keyboard.SyKeyModifier;

/**
 * The type of key event interface exposed by screens.
 */

public interface SyScreenKeyEventsType
{
  /**
   * Notify the screen that a key was pressed.
   *
   * @param keyCode The keycode
   */

  void keyPressed(SyKeyCode keyCode);

  /**
   * Notify the screen that a key was released.
   *
   * @param keyCode The keycode
   */

  void keyReleased(SyKeyCode keyCode);

  /**
   * Notify the screen that a key modifier was pressed.
   *
   * @param keyModifier The key modifier
   */

  void keyModifierPressed(SyKeyModifier keyModifier);

  /**
   * Notify the screen that a key modifier was released.
   *
   * @param keyModifier The key modifier
   */

  void keyModifierReleased(SyKeyModifier keyModifier);
}
