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

import com.io7m.jsycamore.api.keyboard.SyKeyCode;
import com.io7m.jsycamore.api.keyboard.SyKeyModifier;
import com.io7m.jsycamore.api.screens.SyScreenKeyEventsType;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * An adapter that converts AWT key events to {@code jsycamore} key calls.
 */

public final class SyAWTKeyCodeAdapter extends KeyAdapter
{
  private final SyAWTKeyCodeMapping mapper;
  private final SyScreenKeyEventsType screen;

  /**
   * An adapter that converts AWT key events to {@code jsycamore} key calls.
   *
   * @param inScreen The event receiver
   */

  public SyAWTKeyCodeAdapter(
    final SyScreenKeyEventsType inScreen)
  {
    this.screen =
      Objects.requireNonNull(inScreen, "events");
    this.mapper =
      new SyAWTKeyCodeMapping();
  }

  private SyKeyCode keyCodeOf(
    final KeyEvent e)
  {
    return this.mapper.toKeyCode(
      new SyAWTKeyContext(e.getKeyLocation()),
      e.getKeyCode()
    );
  }

  @Override
  public void keyPressed(
    final KeyEvent e)
  {
    final var keyCode =
      this.keyCodeOf(e);
    final var modifier =
      SyKeyModifier.ofKey(keyCode);

    if (modifier.isPresent()) {
      this.screen.keyModifierPressed(modifier.get());
      return;
    }
    this.screen.keyPressed(keyCode);
  }

  @Override
  public void keyReleased(
    final KeyEvent e)
  {
    final var keyCode =
      this.keyCodeOf(e);
    final var modifier =
      SyKeyModifier.ofKey(keyCode);

    if (modifier.isPresent()) {
      this.screen.keyModifierReleased(modifier.get());
      return;
    }
    this.screen.keyReleased(keyCode);
  }
}
