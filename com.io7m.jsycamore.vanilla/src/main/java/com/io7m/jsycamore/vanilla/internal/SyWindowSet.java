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

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jsycamore.api.windows.SyWindowType;

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
  private final Set<SyWindowID> windowsOpen;
  private final List<SyWindowID> windowsOpenOrdered;

  private SyWindowSet(
    final Map<SyWindowID, SyWindowType> inWindows,
    final Set<SyWindowID> inWindowsOpen,
    final List<SyWindowID> inWindowsOpenOrdered)
  {
    this.windows = inWindows;
    this.windowsOpen = inWindowsOpen;
    this.windowsOpenOrdered = inWindowsOpenOrdered;

    for (final SyWindowID w : this.windowsOpen) {
      Preconditions.checkPrecondition(
        this.windowsOpenOrdered.contains(w),
        "Window order list must contain all open windows");
    }

    for (final SyWindowID w : this.windowsOpenOrdered) {
      Preconditions.checkPrecondition(
        this.windowsOpen.contains(w),
        "Window open set must contain all ordered windows");
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
    if (this.windowsOpenOrdered.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(this.windowsOpenOrdered.get(0))
      /**/.flatMap(i -> Optional.ofNullable(this.windows.get(i)));
  }

  /**
   * @return The set of windows
   */

  public Map<SyWindowID, SyWindowType> windows()
  {
    return this.windows;
  }

  /**
   * @return The set of windows that are open
   */

  public Set<SyWindowID> windowsOpen()
  {
    return this.windowsOpen;
  }

  /**
   * @return The set of windows that are open in depth order
   */

  public List<SyWindowType> windowsOpenOrdered()
  {
    return this.windowIdsOpenOrdered()
      .stream()
      .map(this.windows::get)
      .toList();
  }

  /**
   * @return The set of windows that are open in depth order
   */

  public List<SyWindowID> windowIdsOpenOrdered()
  {
    return this.windowsOpenOrdered;
  }

  /**
   * Open the given window. The window is added to the set of open windows.
   *
   * @param window The window
   *
   * @return This set with the given window open
   */

  public SyWindowSetChanged windowOpen(
    final SyWindowType window)
  {
    final var newWindows =
      new HashMap<>(this.windows);
    final var newWindowsOpen =
      new HashSet<>(this.windowsOpen);
    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsOpenOrdered);

    newWindows.put(window.id(), window);
    newWindowsOpen.add(window.id());
    newWindowsOpenOrdered.remove(window.id());
    newWindowsOpenOrdered.addFirst(window.id());

    final var newWindowSet =
      new SyWindowSet(
        Map.copyOf(newWindows),
        Set.copyOf(newWindowsOpen),
        List.copyOf(newWindowsOpenOrdered)
      );

    /*
     * If this window wasn't the window in focus, then indicate that the
     * focus has changed. This is possible because it's possible (although
     * pointless) to call openWindow redundantly on the same window multiple
     * times.
     */

    final var focusLostPotentially = this.windowFocused();
    final var focusGainedPotentially = Optional.of(window);
    final Optional<SyWindowType> focusLostActually;
    final Optional<SyWindowType> focusGainedActually;
    if (!Objects.equals(focusLostPotentially, focusGainedPotentially)) {
      focusLostActually = focusLostPotentially;
      focusGainedActually = focusGainedPotentially;
    } else {
      focusLostActually = Optional.empty();
      focusGainedActually = Optional.empty();
    }

    return new SyWindowSetChanged(
      newWindowSet,
      focusLostActually,
      focusGainedActually
    );
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
    final var newWindowsOpen =
      new HashSet<>(this.windowsOpen);
    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsOpenOrdered);

    newWindowsOpen.remove(window.id());
    newWindowsOpenOrdered.remove(window.id());

    final Optional<SyWindowType> focusGained;
    if (!newWindowsOpenOrdered.isEmpty()) {
      focusGained =
        Optional.of(newWindowsOpenOrdered.get(0))
          .flatMap(i -> Optional.ofNullable(this.windows.get(i)));
    } else {
      focusGained = Optional.empty();
    }

    final var newWindowSet =
      new SyWindowSet(
        this.windows,
        Set.copyOf(newWindowsOpen),
        List.copyOf(newWindowsOpenOrdered)
      );

    return new SyWindowSetChanged(
      newWindowSet,
      Optional.of(window),
      focusGained
    );
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
    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsOpenOrdered);

    newWindowsOpenOrdered.remove(window.id());
    newWindowsOpenOrdered.addFirst(window.id());

    /*
     * If the window wasn't the window in focus, then indicate that the
     * focus has changed.
     */

    final var focusLostPotentially = this.windowFocused();
    final var focusGainedPotentially = Optional.of(window);
    final Optional<SyWindowType> focusLostActually;
    final Optional<SyWindowType> focusGainedActually;
    if (!Objects.equals(focusLostPotentially, focusGainedPotentially)) {
      focusLostActually = focusLostPotentially;
      focusGainedActually = focusGainedPotentially;
    } else {
      focusLostActually = Optional.empty();
      focusGainedActually = Optional.empty();
    }

    final var newWindowSet =
      new SyWindowSet(
        this.windows,
        this.windowsOpen,
        List.copyOf(newWindowsOpenOrdered)
      );

    return new SyWindowSetChanged(
      newWindowSet,
      focusLostActually,
      focusGainedActually
    );
  }

  /**
   * @param window The window
   *
   * @return {@code true} if the given window is open
   */

  public boolean windowIsOpen(
    final SyWindowReadableType window)
  {
    return this.windowsOpen.contains(window.id());
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
