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

import com.io7m.jsycamore.api.windows.SyWindowBecameInvisible;
import com.io7m.jsycamore.api.windows.SyWindowBecameVisible;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowCreated;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.api.windows.SyWindowLayerID;
import com.io7m.jsycamore.api.windows.SyWindowSet;
import com.io7m.jsycamore.api.windows.SyWindowType;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.io7m.jsycamore.api.windows.SyWindowDeletionPolicy.WINDOW_MAY_BE_DELETED;
import static com.io7m.jsycamore.api.windows.SyWindowDeletionPolicy.WINDOW_MAY_NOT_BE_DELETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyWindowSetTest
{
  private static void testWindowCreatedMultipleFocusForLayer(
    final SyWindowLayerID layer)
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var w1 = Mockito.mock(SyWindowType.class);
    final var w2 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.toString()).thenReturn("w0");
    Mockito.when(w1.toString()).thenReturn("w1");
    Mockito.when(w2.toString()).thenReturn("w2");

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w1.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w2.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w1.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w2.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);

    Mockito.when(w0.layer()).thenReturn(layer);
    Mockito.when(w1.layer()).thenReturn(layer);
    Mockito.when(w2.layer()).thenReturn(layer);

    var wsc =
      ws0.windowCreate(w0)
        .then(s -> s.windowShow(w0))
        .then(s -> s.windowCreate(w1))
        .then(s -> s.windowShow(w1))
        .then(s -> s.windowCreate(w2))
        .then(s -> s.windowShow(w2));

    assertTrue(wsc.newSet().windowIsVisible(w0));
    assertTrue(wsc.newSet().windowIsVisible(w1));
    assertTrue(wsc.newSet().windowIsVisible(w2));
    assertEquals(
      Optional.of(w2),
      wsc.newSet().windowFocused(layer));

    wsc = wsc.withoutChanges().then(s -> s.windowFocus(w0));
    assertEquals(
      Optional.of(w0),
      wsc.newSet().windowFocused(layer));
    assertEquals(0, wsc.changes().size());

    wsc = wsc.withoutChanges().then(s -> s.windowFocus(w1));
    assertEquals(
      Optional.of(w1),
      wsc.newSet().windowFocused(layer));
    assertEquals(0, wsc.changes().size());

    wsc = wsc.withoutChanges().then(s -> s.windowFocus(w1));
    assertEquals(
      Optional.of(w1),
      wsc.newSet().windowFocused(layer));
    assertEquals(0, wsc.changes().size());

    wsc = wsc.withoutChanges().then(s -> s.windowClose(w1));
    assertEquals(
      Optional.of(w0),
      wsc.newSet().windowFocused(layer));
    assertEquals(new SyWindowClosed(w1.id()), wsc.changes().get(0));
    assertEquals(1, wsc.changes().size());
  }

  private static void testWindowShowRedundantForLayer(
    final SyWindowLayerID layer)
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.toString()).thenReturn("w0");
    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w0.layer()).thenReturn(layer);

    final var wsc =
      ws0.windowCreate(w0)
        .then(k -> k.windowShow(w0))
        .then(k -> k.windowShow(w0));

    assertEquals(
      new SyWindowCreated(w0.id()),
      wsc.changes().get(0)
    );
    assertEquals(
      new SyWindowBecameVisible(w0.id()),
      wsc.changes().get(1)
    );
    assertEquals(2, wsc.changes().size());
  }

  private static void testWindowHideRedundantForLayer(
    final SyWindowLayerID layer)
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.toString()).thenReturn("w0");
    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w0.layer()).thenReturn(layer);

    final var wsc =
      ws0.windowCreate(w0)
        .then(k -> k.windowShow(w0))
        .then(k -> k.windowHide(w0))
        .then(k -> k.windowHide(w0));

    final var changes = wsc.changes();
    assertEquals(
      new SyWindowCreated(w0.id()),
      changes.get(0)
    );
    assertEquals(
      new SyWindowBecameVisible(w0.id()),
      changes.get(1)
    );
    assertEquals(
      new SyWindowBecameInvisible(w0.id()),
      changes.get(2)
    );
    assertEquals(3, changes.size());
  }

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
    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w0.layer()).thenReturn(SyWindowLayerID.defaultLayer());

    var wsc = ws0.windowCreate(w0);
    assertEquals(new SyWindowCreated(w0.id()), wsc.changes().get(0));
    assertEquals(1, wsc.changes().size());
    assertEquals(List.of(), wsc.newSet().windowsVisibleOrdered());
    assertEquals(Map.of(w0.id(), w0), wsc.newSet().windows());
    assertEquals(Set.of(), wsc.newSet().windowsVisible());
    assertFalse(wsc.newSet().windowIsVisible(w0));

    wsc = wsc.then(k -> k.windowClose(w0));
    assertEquals(new SyWindowClosed(w0.id()), wsc.changes().get(1));
    assertEquals(2, wsc.changes().size());
    assertEquals(List.of(), wsc.newSet().windowsVisibleOrdered());
    assertEquals(Map.of(), wsc.newSet().windows());
    assertEquals(Set.of(), wsc.newSet().windowsVisible());
    assertFalse(wsc.newSet().windowIsVisible(w0));
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
   * Trying to close a window that may not be closed, fails.
   */

  @Test
  public void testWindowClosedDisallowed()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.id())
      .thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w0.deletionPolicy())
      .thenReturn(WINDOW_MAY_NOT_BE_DELETED);

    final var wsc = ws0.windowCreate(w0);
    assertThrows(
      IllegalStateException.class,
      () -> wsc.newSet().windowClose(w0));
  }

  /**
   * Creating multiple windows publishes the expected events.
   *
   * @return A test stream
   */

  @TestFactory
  public Stream<DynamicTest> testWindowCreatedMultipleFocus()
  {
    return Stream.of(
        SyWindowLayerID.defaultLayer().nextLower(),
        SyWindowLayerID.defaultLayer(),
        SyWindowLayerID.defaultLayer().nextHigher())
      .map(layer -> {
        return DynamicTest.dynamicTest(
          "testWindowCreatedMultipleFocus" + layer,
          () -> testWindowCreatedMultipleFocusForLayer(layer));
      });
  }

  /**
   * Creating multiple windows publishes the expected events.
   */

  @Test
  public void testWindowUnderlayAlwaysUnderneath()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var w1 = Mockito.mock(SyWindowType.class);
    final var w2 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.toString()).thenReturn("w0");
    Mockito.when(w1.toString()).thenReturn("w1");
    Mockito.when(w2.toString()).thenReturn("w2");

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w1.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w2.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w1.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w2.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);

    Mockito.when(w0.layer()).thenReturn(SyWindowLayerID.defaultLayer().nextLower());
    Mockito.when(w1.layer()).thenReturn(SyWindowLayerID.defaultLayer());
    Mockito.when(w2.layer()).thenReturn(SyWindowLayerID.defaultLayer());

    var wsc =
      ws0.windowCreate(w0)
        .then(k -> k.windowCreate(w1))
        .then(k -> k.windowCreate(w2))
        .then(k -> k.windowShow(w0))
        .then(k -> k.windowShow(w1))
        .then(k -> k.windowShow(w2));

    {
      final var ordered =
        wsc.newSet().windowsVisibleOrdered();
      assertEquals(List.of(w2, w1, w0), ordered);
    }

    wsc = wsc.then(k -> k.windowFocus(w0));

    {
      final var ordered =
        wsc.newSet().windowsVisibleOrdered();
      assertEquals(List.of(w2, w1, w0), ordered);
    }
  }

  /**
   * Showing a window twice doesn't publish redundant events.
   *
   * @return A test stream
   */

  @TestFactory
  public Stream<DynamicTest> testWindowShowRedundant()
  {
    return Stream.of(
        SyWindowLayerID.defaultLayer().nextLower(),
        SyWindowLayerID.defaultLayer(),
        SyWindowLayerID.defaultLayer().nextHigher())
      .map(layer -> {
        return DynamicTest.dynamicTest(
          "testWindowShowRedundant_" + layer,
          () -> testWindowShowRedundantForLayer(layer));
      });
  }

  /**
   * Hiding a window twice doesn't publish redundant events.
   *
   * @return A test stream
   */

  @TestFactory
  public Stream<DynamicTest> testWindowHideRedundant()
  {
    return Stream.of(
        SyWindowLayerID.defaultLayer().nextLower(),
        SyWindowLayerID.defaultLayer(),
        SyWindowLayerID.defaultLayer().nextHigher())
      .map(layer -> {
        return DynamicTest.dynamicTest(
          "testWindowHideRedundant_" + layer,
          () -> testWindowHideRedundantForLayer(layer));
      });
  }

  /**
   * Creating multiple windows publishes the expected events.
   */

  @Test
  public void testWindowSetOrdering()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);
    final var w1 = Mockito.mock(SyWindowType.class);
    final var w2 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.toString()).thenReturn("w0");
    Mockito.when(w1.toString()).thenReturn("w1");
    Mockito.when(w2.toString()).thenReturn("w2");

    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w1.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w2.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w1.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w2.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);

    Mockito.when(w0.layer()).thenReturn(SyWindowLayerID.defaultLayer().nextLower());
    Mockito.when(w1.layer()).thenReturn(SyWindowLayerID.defaultLayer());
    Mockito.when(w2.layer()).thenReturn(SyWindowLayerID.defaultLayer().nextHigher());

    var wsc =
      ws0.windowCreate(w0)
        .then(k -> k.windowCreate(w1))
        .then(k -> k.windowCreate(w2))
        .then(k -> k.windowShow(w0))
        .then(k -> k.windowShow(w1))
        .then(k -> k.windowShow(w2));

    {
      final var ordered =
        wsc.newSet().windowsVisibleOrdered();
      assertEquals(List.of(w2, w1, w0), ordered);
    }

    wsc = wsc.then(k -> k.windowFocus(w0));

    {
      final var ordered =
        wsc.newSet().windowsVisibleOrdered();
      assertEquals(List.of(w2, w1, w0), ordered);
    }
  }

  /**
   * Showing and hiding window layers works.
   */

  @Test
  public void testWindowLayerVisibility()
  {
    final var ws0 = SyWindowSet.empty();
    final var w0 = Mockito.mock(SyWindowType.class);

    Mockito.when(w0.toString()).thenReturn("w0");
    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));
    Mockito.when(w0.deletionPolicy()).thenReturn(WINDOW_MAY_BE_DELETED);
    Mockito.when(w0.layer()).thenReturn(SyWindowLayerID.defaultLayer());

    var wsc =
      ws0.windowCreate(w0)
        .then(k -> k.windowShow(w0));

    assertTrue(wsc.newSet().windowIsVisible(w0));
    assertTrue(wsc.newSet().windowLayerIsShown(SyWindowLayerID.defaultLayer()));

    wsc = wsc.then(k -> k.windowLayerHide(SyWindowLayerID.defaultLayer()));
    assertFalse(wsc.newSet().windowIsVisible(w0));
    assertFalse(wsc.newSet().windowLayerIsShown(SyWindowLayerID.defaultLayer()));

    wsc = wsc.then(k -> k.windowLayerShow(SyWindowLayerID.defaultLayer()));
    assertTrue(wsc.newSet().windowIsVisible(w0));
    assertTrue(wsc.newSet().windowLayerIsShown(SyWindowLayerID.defaultLayer()));
  }
}