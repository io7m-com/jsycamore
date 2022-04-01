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

package com.io7m.jsycamore.api.windows;

import com.io7m.jaffirm.core.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * An immutable window set.
 */

public final class SyWindowSet
{
  private final Map<SyWindowID, SyWindowType> windows;
  private final Set<SyWindowID> windowsVisible;
  private final List<SyWindowID> windowsVisibleOrdered;

  private SyWindowSet(
    final Map<SyWindowID, SyWindowType> inWindows,
    final Set<SyWindowID> inWindowsVisible,
    final List<SyWindowID> inWindowsVisibleOrdered)
  {
    this.windows = inWindows;
    this.windowsVisible = inWindowsVisible;
    this.windowsVisibleOrdered = inWindowsVisibleOrdered;

    for (final var id : this.windowsVisible) {
      Preconditions.checkPrecondition(
        this.windowsVisibleOrdered.contains(id),
        "Window order list must contain all visible windows");
    }

    for (final var id : this.windowsVisibleOrdered) {
      Preconditions.checkPrecondition(
        this.windowsVisible.contains(id),
        "Window visible set must contain all ordered windows");
    }
  }

  /**
   * Create an empty window set.
   *
   * @return The empty set
   */

  public static SyWindowSet empty()
  {
    return new SyWindowSet(Map.of(), Set.of(), List.of());
  }

  /**
   * @return The focused window in the set, if any
   */

  public Optional<SyWindowType> windowFocused()
  {
    if (this.windowsVisibleOrdered.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(this.windowsVisibleOrdered.get(0))
      .flatMap(i -> Optional.ofNullable(this.windows.get(i)));
  }

  /**
   * @return The set of windows
   */

  public Map<SyWindowID, SyWindowType> windows()
  {
    return this.windows;
  }

  /**
   * @return The set of windows that are visible
   */

  public Set<SyWindowID> windowsVisible()
  {
    return this.windowsVisible;
  }

  /**
   * @return The set of windows that are visible in depth order
   */

  public List<SyWindowType> windowsVisibleOrdered()
  {
    return this.windowIdsVisibleOrdered()
      .stream()
      .map(this.windows::get)
      .toList();
  }

  /**
   * @return The set of windows that are visible in depth order
   */

  public List<SyWindowID> windowIdsVisibleOrdered()
  {
    return this.windowsVisibleOrdered;
  }

  /**
   * Create the given window. The window is added to the set of known windows.
   *
   * @param window The window
   *
   * @return This set with the given window
   */

  public SyWindowSetChanged windowCreate(
    final SyWindowType window)
  {
    if (this.windows.containsKey(window.id())) {
      throw new IllegalStateException(
        "Window %s already exists in this window set"
          .formatted(window.id().value())
      );
    }

    final var newWindows = new HashMap<>(this.windows);
    newWindows.put(window.id(), window);

    return new SyWindowSetChanged(
      new SyWindowSet(
        Map.copyOf(newWindows),
        this.windowsVisible,
        this.windowsVisibleOrdered
      ),
      List.of(new SyWindowCreated(window.id()))
    );
  }

  /**
   * Make the given window visible. The window is added to the set of visible
   * windows.
   *
   * @param window The window
   *
   * @return This set with the given window visible
   */

  public SyWindowSetChanged windowShow(
    final SyWindowType window)
  {
    this.checkKnownWindow(window);

    final var newWindows =
      new HashMap<>(this.windows);
    final var newWindowsVisible =
      new HashSet<>(this.windowsVisible);
    final var newWindowsVisibleOrdered =
      new LinkedList<>(this.windowsVisibleOrdered);

    newWindows.put(window.id(), window);
    newWindowsVisible.add(window.id());
    newWindowsVisibleOrdered.remove(window.id());
    newWindowsVisibleOrdered.addFirst(window.id());

    final var newWindowSet =
      new SyWindowSet(
        Map.copyOf(newWindows),
        Set.copyOf(newWindowsVisible),
        List.copyOf(newWindowsVisibleOrdered)
      );

    /*
     * If this window wasn't the window in focus, then indicate that the
     * focus has changed. This is possible because it's possible (although
     * pointless) to call openWindow redundantly on the same window multiple
     * times.
     */

    final var focusLostPotentially =
      this.windowFocused();
    final var focusGainedPotentially =
      Optional.of(window);
    final var events =
      new ArrayList<SyWindowEventType>(2);

    if (!Objects.equals(focusLostPotentially, focusGainedPotentially)) {
      focusGainedPotentially.ifPresent(w -> {
        events.add(new SyWindowFocusGained(w.id()));
      });
      focusLostPotentially.ifPresent(w -> {
        events.add(new SyWindowFocusLost(w.id()));
      });
    }

    return new SyWindowSetChanged(
      newWindowSet,
      List.copyOf(events)
    );
  }

  private void checkKnownWindow(
    final SyWindowReadableType window)
  {
    if (!this.windows.containsKey(window.id())) {
      throw new IllegalStateException(
        "Window %s is not recognized by this window set."
          .formatted(window.id().value())
      );
    }
  }

  /**
   * Hide the given window.
   *
   * @param window The window
   *
   * @return This set with the given window hidden
   */

  public SyWindowSetChanged windowHide(
    final SyWindowType window)
  {
    this.checkKnownWindow(window);

    final var newWindowsVisible =
      new HashSet<>(this.windowsVisible);
    final var newWindowsVisibleOrdered =
      new LinkedList<>(this.windowsVisibleOrdered);

    newWindowsVisible.remove(window.id());
    newWindowsVisibleOrdered.remove(window.id());

    final var events = new ArrayList<SyWindowEventType>(2);
    events.add(new SyWindowFocusLost(window.id()));
    if (!newWindowsVisibleOrdered.isEmpty()) {
      events.add(new SyWindowFocusGained(newWindowsVisibleOrdered.get(0)));
    }

    final var newWindowSet =
      new SyWindowSet(
        this.windows,
        Set.copyOf(newWindowsVisible),
        List.copyOf(newWindowsVisibleOrdered)
      );

    return new SyWindowSetChanged(newWindowSet, events);
  }

  /**
   * Close the given window.
   *
   * @param window The window
   *
   * @return This set with the given window closed
   */

  public SyWindowSetChanged windowClose(
    final SyWindowType window)
  {
    this.checkKnownWindow(window);

    final var newWindowsVisible =
      new HashSet<>(this.windowsVisible);
    final var newWindowsVisibleOrdered =
      new LinkedList<>(this.windowsVisibleOrdered);

    newWindowsVisible.remove(window.id());
    newWindowsVisibleOrdered.remove(window.id());

    final var events = new ArrayList<SyWindowEventType>(3);
    events.add(new SyWindowFocusLost(window.id()));
    events.add(new SyWindowClosed(window.id()));
    if (!newWindowsVisibleOrdered.isEmpty()) {
      events.add(new SyWindowFocusGained(newWindowsVisibleOrdered.get(0)));
    }

    final var newWindowSet =
      new SyWindowSet(
        this.windows,
        Set.copyOf(newWindowsVisible),
        List.copyOf(newWindowsVisibleOrdered)
      );

    return new SyWindowSetChanged(newWindowSet, events);
  }

  /**
   * Focus the given window.
   *
   * @param window The window
   *
   * @return This set with the given window focused
   */

  public SyWindowSetChanged windowFocus(
    final SyWindowType window)
  {
    this.checkKnownWindow(window);

    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsVisibleOrdered);

    newWindowsOpenOrdered.remove(window.id());
    newWindowsOpenOrdered.addFirst(window.id());

    /*
     * If the window wasn't the window in focus, then indicate that the
     * focus has changed.
     */

    final var focusLostPotentially =
      this.windowFocused();
    final var focusGainedPotentially =
      Optional.of(window);

    final var events = new ArrayList<SyWindowEventType>(2);
    if (!Objects.equals(focusLostPotentially, focusGainedPotentially)) {
      focusGainedPotentially.ifPresent(w -> {
        events.add(new SyWindowFocusGained(w.id()));
      });
      focusLostPotentially.ifPresent(w -> {
        events.add(new SyWindowFocusLost(w.id()));
      });
    }

    final var newWindowSet =
      new SyWindowSet(
        this.windows,
        this.windowsVisible,
        List.copyOf(newWindowsOpenOrdered)
      );

    return new SyWindowSetChanged(newWindowSet, events);
  }

  /**
   * @param window The window
   *
   * @return {@code true} if the given window is visible
   */

  public boolean windowIsVisible(
    final SyWindowReadableType window)
  {
    this.checkKnownWindow(window);
    return this.windowsVisible.contains(window.id());
  }

  /**
   * @return A window ID that is not present in this set
   */

  public SyWindowID windowFreshId()
  {
    while (true) {
      final var id = new SyWindowID(UUID.randomUUID());
      if (!this.windows.containsKey(id)) {
        return id;
      }
    }
  }
}
