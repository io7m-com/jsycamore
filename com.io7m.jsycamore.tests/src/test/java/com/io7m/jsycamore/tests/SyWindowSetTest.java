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

import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.vanilla.internal.SyWindowSet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyWindowSetTest
{
  @Test
  public void testEmpty()
  {
    final var ws = SyWindowSet.empty();
    assertEquals(0, ws.windows().size());
    assertEquals(0, ws.windowsOpen().size());
    assertEquals(0, ws.windowsOpenOrdered().size());
    assertEquals(Optional.empty(), ws.windowFocused());
  }

  @Test
  public void testWindowOpenedClosed()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var wsc0 = ws0.windowOpen(w0);
    assertEquals(Optional.of(w0), wsc0.focusGained());
    assertEquals(Optional.empty(), wsc0.focusLost());
    assertEquals(List.of(w0), wsc0.newSet().windowsOpenOrdered());
    assertEquals(Set.of(w0), wsc0.newSet().windows());
    assertEquals(Set.of(w0), wsc0.newSet().windowsOpen());
    assertTrue(wsc0.newSet().windowIsOpen(w0));

    final var wsc1 = wsc0.newSet().windowClose(w0);
    assertEquals(Optional.empty(), wsc1.focusGained());
    assertEquals(Optional.of(w0), wsc1.focusLost());
    assertEquals(List.of(), wsc1.newSet().windowsOpenOrdered());
    assertEquals(Set.of(w0), wsc1.newSet().windows());
    assertEquals(Set.of(), wsc1.newSet().windowsOpen());
    assertFalse(wsc1.newSet().windowIsOpen(w0));
  }

  @Test
  public void testWindowClosedUnknown()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var wsc0 = ws0.windowClose(w0);
    assertEquals(Optional.empty(), wsc0.focusGained());
    assertEquals(Optional.of(w0), wsc0.focusLost());
    assertEquals(List.of(), wsc0.newSet().windowsOpenOrdered());
    assertEquals(Set.of(), wsc0.newSet().windows());
    assertEquals(Set.of(), wsc0.newSet().windowsOpen());
  }

  @Test
  public void testWindowOpenedMultipleFocus()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var w1 = Mockito.mock(SyWindowType.class);
    final var w2 = Mockito.mock(SyWindowType.class);

    final var r0 =
      ws0.windowOpen(w0)
        .then(s -> s.windowOpen(w0))
        .then(s -> s.windowOpen(w1))
        .then(s -> s.windowOpen(w2));

    assertTrue(r0.newSet().windowIsOpen(w0));
    assertTrue(r0.newSet().windowIsOpen(w1));
    assertTrue(r0.newSet().windowIsOpen(w2));
    assertEquals(Optional.of(w2), r0.newSet().windowFocused());

    final var r1 =
      r0.then(s -> s.windowFocus(w0));

    assertEquals(Optional.of(w0), r1.newSet().windowFocused());
    assertEquals(Optional.of(w0), r1.focusGained());
    assertEquals(Optional.of(w2), r1.focusLost());

    final var r2 =
      r1.then(s -> s.windowFocus(w1));

    assertEquals(Optional.of(w1), r2.newSet().windowFocused());
    assertEquals(Optional.of(w1), r2.focusGained());
    assertEquals(Optional.of(w0), r2.focusLost());

    final var r3 =
      r2.then(s -> s.windowFocus(w1));

    assertEquals(Optional.of(w1), r3.newSet().windowFocused());
    assertEquals(Optional.empty(), r3.focusGained());
    assertEquals(Optional.empty(), r3.focusLost());

    final var r4 =
      r3.then(s -> s.windowClose(w1));

    assertEquals(Optional.of(w0), r4.newSet().windowFocused());
    assertEquals(Optional.of(w0), r4.focusGained());
    assertEquals(Optional.of(w1), r4.focusLost());
  }
}
