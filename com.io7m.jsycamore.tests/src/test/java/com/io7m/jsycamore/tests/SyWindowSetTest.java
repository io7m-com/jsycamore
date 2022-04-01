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

import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowCreated;
import com.io7m.jsycamore.api.windows.SyWindowFocusGained;
import com.io7m.jsycamore.api.windows.SyWindowFocusLost;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.api.windows.SyWindowSet;
import com.io7m.jsycamore.api.windows.SyWindowType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyWindowSetTest
{
  /**
   * The empty window set is empty.
   */

  @Test
  public void testEmpty()
  {
    final var ws = SyWindowSet.empty();
    assertEquals(0, ws.windows().size());
    assertEquals(0, ws.windowsVisible().size());
    assertEquals(0, ws.windowsVisibleOrdered().size());
    assertEquals(Optional.empty(), ws.windowFocused());
  }

  /**
   * Creating and closing windows publishes the expected events.
   */

  @Test
  public void testWindowCreatedClosed()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    final var wsc0 = ws0.windowCreate(w0);
    assertEquals(new SyWindowCreated(w0.id()), wsc0.changes().get(0));
    assertEquals(1, wsc0.changes().size());
    assertEquals(List.of(), wsc0.newSet().windowsVisibleOrdered());
    assertEquals(Map.of(w0.id(), w0), wsc0.newSet().windows());
    assertEquals(Set.of(), wsc0.newSet().windowsVisible());
    assertFalse(wsc0.newSet().windowIsVisible(w0));

    final var wsc1 = wsc0.newSet().windowClose(w0);
    assertEquals(new SyWindowFocusLost(w0.id()), wsc1.changes().get(0));
    assertEquals(new SyWindowClosed(w0.id()), wsc1.changes().get(1));
    assertEquals(2, wsc1.changes().size());
    assertEquals(List.of(), wsc1.newSet().windowsVisibleOrdered());
    assertEquals(Map.of(w0.id(), w0), wsc1.newSet().windows());
    assertEquals(Set.of(), wsc1.newSet().windowsVisible());
    assertFalse(wsc1.newSet().windowIsVisible(w0));
  }

  /**
   * Creating a window twice, fails.
   */

  @Test
  public void testWindowCreatedTwice()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    final var wsc0 = ws0.windowCreate(w0);

    assertThrows(IllegalStateException.class, () -> {
      wsc0.newSet().windowCreate(w0);
    });
  }

  /**
   * Trying to close an unrecognized window fails.
   */

  @Test
  public void testWindowClosedUnknown()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    assertThrows(IllegalStateException.class, () -> ws0.windowClose(w0));
  }

  /**
   * Creating multiple windows publishes the expected events.
   */

  @Test
  public void testWindowCreatedMultipleFocus()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var w1 = Mockito.mock(SyWindowType.class);
    final var w2 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w1.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w2.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    final var r0 =
      ws0.windowCreate(w0)
        .then(s -> s.windowShow(w0))
        .then(s -> s.windowCreate(w1))
        .then(s -> s.windowShow(w1))
        .then(s -> s.windowCreate(w2))
        .then(s -> s.windowShow(w2));

    assertTrue(r0.newSet().windowIsVisible(w0));
    assertTrue(r0.newSet().windowIsVisible(w1));
    assertTrue(r0.newSet().windowIsVisible(w2));
    assertEquals(Optional.of(w2), r0.newSet().windowFocused());

    final var r1 =
      r0.then(s -> s.windowFocus(w0));

    assertEquals(Optional.of(w0), r1.newSet().windowFocused());
    assertEquals(new SyWindowFocusGained(w0.id()), r1.changes().get(0));
    assertEquals(new SyWindowFocusLost(w2.id()), r1.changes().get(1));
    assertEquals(2, r1.changes().size());

    final var r2 =
      r1.then(s -> s.windowFocus(w1));

    assertEquals(Optional.of(w1), r2.newSet().windowFocused());
    assertEquals(new SyWindowFocusGained(w1.id()), r2.changes().get(0));
    assertEquals(new SyWindowFocusLost(w0.id()), r2.changes().get(1));
    assertEquals(2, r2.changes().size());

    final var r3 =
      r2.then(s -> s.windowFocus(w1));

    assertEquals(Optional.of(w1), r3.newSet().windowFocused());
    assertEquals(0, r3.changes().size());

    final var r4 =
      r3.then(s -> s.windowClose(w1));

    assertEquals(Optional.of(w0), r4.newSet().windowFocused());
    assertEquals(new SyWindowFocusLost(w1.id()), r4.changes().get(0));
    assertEquals(new SyWindowClosed(w1.id()), r4.changes().get(1));
    assertEquals(new SyWindowFocusGained(w0.id()), r4.changes().get(2));
    assertEquals(3, r4.changes().size());
  }
}
