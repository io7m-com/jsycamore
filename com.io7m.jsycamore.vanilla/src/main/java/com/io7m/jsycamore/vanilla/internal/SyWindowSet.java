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
import com.io7m.jsycamore.api.windows.SyWindowType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class SyWindowSet
{
  private final Set<SyWindowType> windows;
  private final Set<SyWindowType> windowsOpen;
  private final List<SyWindowType> windowsOpenOrdered;

  private SyWindowSet(
    final Set<SyWindowType> inWindows,
    final Set<SyWindowType> inWindowsOpen,
    final List<SyWindowType> inWindowsOpenOrdered)
  {
    this.windows = inWindows;
    this.windowsOpen = inWindowsOpen;
    this.windowsOpenOrdered = inWindowsOpenOrdered;

    for (final SyWindowType w : this.windowsOpen) {
      Preconditions.checkPrecondition(
        this.windowsOpenOrdered.contains(w),
        "Window order list must contain all open windows");
    }

    for (final SyWindowType w : this.windowsOpenOrdered) {
      Preconditions.checkPrecondition(
        this.windowsOpen.contains(w),
        "Window open set must contain all ordered windows");
    }
  }

  public static SyWindowSet empty()
  {
    return new SyWindowSet(Set.of(), Set.of(), List.of());
  }

  public Optional<SyWindowType> windowFocused()
  {
    if (this.windowsOpenOrdered.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(this.windowsOpenOrdered.get(0));
  }

  public Set<SyWindowType> windows()
  {
    return this.windows;
  }

  public Set<SyWindowType> windowsOpen()
  {
    return this.windowsOpen;
  }

  public List<SyWindowType> windowsOpenOrdered()
  {
    return this.windowsOpenOrdered;
  }

  public SyWindowSetChanged windowOpen(
    final SyWindowType window)
  {
    final var newWindows =
      new HashSet<>(this.windows);
    final var newWindowsOpen =
      new HashSet<>(this.windowsOpen);
    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsOpenOrdered);

    newWindows.add(window);
    newWindowsOpen.add(window);
    newWindowsOpenOrdered.remove(window);
    newWindowsOpenOrdered.addFirst(window);

    final var newWindowSet =
      new SyWindowSet(
        Set.copyOf(newWindows),
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

  public SyWindowSetChanged windowClose(
    final SyWindowType window)
  {
    final var newWindowsOpen =
      new HashSet<>(this.windowsOpen);
    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsOpenOrdered);

    newWindowsOpen.remove(window);
    newWindowsOpenOrdered.remove(window);

    final Optional<SyWindowType> focusGained;
    if (!newWindowsOpenOrdered.isEmpty()) {
      focusGained = Optional.of(newWindowsOpenOrdered.get(0));
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

  public SyWindowSetChanged windowFocus(
    final SyWindowType window)
  {
    final var newWindowsOpenOrdered =
      new LinkedList<>(this.windowsOpenOrdered);

    newWindowsOpenOrdered.remove(window);
    newWindowsOpenOrdered.addFirst(window);

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

  public boolean windowIsOpen(
    final SyWindowType window)
  {
    return this.windowsOpen.contains(window);
  }
}
