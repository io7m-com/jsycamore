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
import net.jcip.annotations.Immutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * An immutable window set.
 */

@Immutable
public final class SyWindowSet
{
  private final Map<SyWindowID, SyWindowType> windows;
  private final TreeMap<Integer, LinkedList<SyWindowType>> windowsVisibleOrdered;
  private final Set<Integer> windowLayersHidden;

  private SyWindowSet(
    final Map<SyWindowID, SyWindowType> inWindows,
    final TreeMap<Integer, LinkedList<SyWindowType>> inWindowsVisibleOrdered,
    final Set<Integer> inWindowLayersVisible)
  {
    this.windows = Map.copyOf(inWindows);
    this.windowsVisibleOrdered = inWindowsVisibleOrdered;
    this.windowLayersHidden = Set.copyOf(inWindowLayersVisible);

    this.windowsVisibleOrdered.forEach((layer, layerWindows) -> {
      layerWindows.forEach(window -> {
        final var windowId = window.id();
        Preconditions.checkPreconditionV(
          this.windows.containsKey(windowId),
          "Window ID %s not in window map",
          windowId.value()
        );
      });
    });
  }

  /**
   * Create an empty window set.
   *
   * @return The empty set
   */

  public static SyWindowSet empty()
  {
    return new SyWindowSet(Map.of(), new TreeMap<>(), Set.of());
  }

  /**
   * @param layer The window layer
   *
   * @return The focused window in the set, if any
   */

  public Optional<SyWindowType> windowFocused(
    final int layer)
  {
    final var visible =
      this.windowsVisibleOrdered.get(layer);
    return visible.stream().findFirst();
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
    return this.windowsVisibleOrdered.keySet()
      .stream()
      .filter(layer -> !this.windowLayersHidden.contains(layer))
      .map(this.windowsVisibleOrdered::get)
      .flatMap(Collection::stream)
      .map(SyWindowReadableType::id)
      .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Calculate the complete set of visible windows and return them in depth
   * order: Windows nearer to the viewer are closer to the start of the list.
   *
   * @return The set of windows that are visible in depth order
   */

  public List<SyWindowType> windowsVisibleOrdered()
  {
    return this.windowsVisibleOrdered.keySet()
      .stream()
      .sorted(Comparator.reverseOrder())
      .filter(layer -> !this.windowLayersHidden.contains(layer))
      .map(this.windowsVisibleOrdered::get)
      .flatMap(Collection::stream)
      .toList();
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
    Objects.requireNonNull(window, "window");

    final var windowId = window.id();
    if (this.windows.containsKey(windowId)) {
      throw new IllegalStateException(
        "Window %s already exists in this window set"
          .formatted(windowId.value())
      );
    }

    final var newWindows = new HashMap<>(this.windows);
    newWindows.put(windowId, window);

    return new SyWindowSetChanged(
      new SyWindowSet(
        newWindows,
        this.windowsVisibleOrdered,
        this.windowLayersHidden),
      List.of(new SyWindowCreated(windowId))
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
    Objects.requireNonNull(window, "window");

    this.checkKnownWindow(window);

    final var newMap =
      new TreeMap<>(this.windowsVisibleOrdered);
    final var layerBoxed =
      Integer.valueOf(window.layer());
    final var newVisible =
      new LinkedList<>(newMap.getOrDefault(layerBoxed, new LinkedList<>()));

    final var removed = newVisible.remove(window);
    final List<SyWindowEventType> changes;
    if (removed) {
      changes = List.of();
    } else {
      changes = List.of(new SyWindowBecameVisible(window.id()));
    }
    newVisible.addFirst(window);
    newMap.put(layerBoxed, newVisible);

    return new SyWindowSetChanged(
      new SyWindowSet(this.windows, newMap, this.windowLayersHidden),
      changes
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
    Objects.requireNonNull(window, "window");

    this.checkKnownWindow(window);

    final var newMap =
      new TreeMap<>(this.windowsVisibleOrdered);
    final var layerBoxed =
      Integer.valueOf(window.layer());
    final var newVisible =
      new LinkedList<>(newMap.getOrDefault(layerBoxed, new LinkedList<>()));

    final var removed = newVisible.remove(window);
    final List<SyWindowEventType> changes;
    if (removed) {
      changes = List.of(new SyWindowBecameInvisible(window.id()));
    } else {
      changes = List.of();
    }
    newMap.put(layerBoxed, newVisible);

    return new SyWindowSetChanged(
      new SyWindowSet(this.windows, newMap, this.windowLayersHidden),
      changes
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
    Objects.requireNonNull(window, "window");

    this.checkKnownWindow(window);

    switch (window.deletionPolicy()) {
      case WINDOW_MAY_BE_DELETED -> {
        // OK
      }
      case WINDOW_MAY_NOT_BE_DELETED -> {
        throw new IllegalStateException(
          "Window %s cannot be deleted".formatted(window.id().value())
        );
      }
    }

    final var newWindows =
      new HashMap<>(this.windows);
    final var newMap =
      new TreeMap<>(this.windowsVisibleOrdered);

    final var layerBoxed =
      Integer.valueOf(window.layer());
    final var newVisible =
      new LinkedList<>(newMap.getOrDefault(layerBoxed, new LinkedList<>()));

    newVisible.remove(window);

    final List<SyWindowEventType> changes =
      List.of(new SyWindowClosed(window.id()));

    newMap.put(layerBoxed, newVisible);
    newWindows.remove(window.id());

    return new SyWindowSetChanged(
      new SyWindowSet(newWindows, newMap, this.windowLayersHidden),
      changes
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
    Objects.requireNonNull(window, "window");

    this.checkKnownWindow(window);

    final var newMap =
      new TreeMap<>(this.windowsVisibleOrdered);

    final var layerBoxed =
      Integer.valueOf(window.layer());

    final var existingVisible =
      newMap.getOrDefault(layerBoxed, new LinkedList<>());
    final var newVisible =
      new LinkedList<>(existingVisible);

    newVisible.remove(window);
    newVisible.addFirst(window);
    newMap.put(layerBoxed, newVisible);

    return new SyWindowSetChanged(
      new SyWindowSet(this.windows, newMap, this.windowLayersHidden),
      List.of()
    );
  }

  /**
   * @param window The window
   *
   * @return {@code true} if the given window is visible
   */

  public boolean windowIsVisible(
    final SyWindowReadableType window)
  {
    Objects.requireNonNull(window, "window");

    final var layerBoxed = Integer.valueOf(window.layer());
    if (this.windowLayersHidden.contains(layerBoxed)) {
      return false;
    }

    final var layer =
      this.windowsVisibleOrdered.get(layerBoxed);

    if (layer != null) {
      return layer.contains(window);
    }

    return false;
  }

  /**
   * Hide the given window layer.
   *
   * @param layer The window layer
   *
   * @return This set with the given window layer hidden
   */

  public SyWindowSetChanged windowLayerHide(
    final int layer)
  {
    final var layerBoxed = Integer.valueOf(layer);
    final var newHidden = new HashSet<>(this.windowLayersHidden);
    newHidden.add(layerBoxed);

    return new SyWindowSetChanged(
      new SyWindowSet(
        this.windows,
        this.windowsVisibleOrdered,
        newHidden),
      List.of()
    );
  }

  /**
   * Show the given window layer.
   *
   * @param layer The window layer
   *
   * @return This set with the given window layer made visible
   */

  public SyWindowSetChanged windowLayerShow(
    final int layer)
  {
    final var layerBoxed = Integer.valueOf(layer);
    final var newHidden = new HashSet<>(this.windowLayersHidden);
    newHidden.remove(layerBoxed);

    return new SyWindowSetChanged(
      new SyWindowSet(
        this.windows,
        this.windowsVisibleOrdered,
        newHidden),
      List.of()
    );
  }

  /**
   * @param layer The window layer
   *
   * @return {@code true} if the given window layer is visible
   */

  public boolean windowLayerIsShown(
    final int layer)
  {
    return !this.windowLayersHidden.contains(Integer.valueOf(layer));
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
