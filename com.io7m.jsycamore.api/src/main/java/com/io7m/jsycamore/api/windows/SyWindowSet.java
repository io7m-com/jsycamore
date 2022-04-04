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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An immutable window set.
 */

public final class SyWindowSet
{
  private final Map<SyWindowID, SyWindowType> windows;
  private final List<SyWindowType> windowsVisibleOrderedUnderlays;
  private final List<SyWindowType> windowsVisibleOrderedNormals;
  private final List<SyWindowType> windowsVisibleOrderedOverlays;

  private SyWindowSet(
    final Map<SyWindowID, SyWindowType> inWindows,
    final List<SyWindowType> inWindowsVisibleUnderlays,
    final List<SyWindowType> inWindowsVisibleNormals,
    final List<SyWindowType> inWindowsVisibleOverlays)
  {
    this.windows =
      Map.copyOf(inWindows);
    this.windowsVisibleOrderedUnderlays =
      List.copyOf(inWindowsVisibleUnderlays);
    this.windowsVisibleOrderedNormals =
      List.copyOf(inWindowsVisibleNormals);
    this.windowsVisibleOrderedOverlays =
      List.copyOf(inWindowsVisibleOverlays);

    this.windowsVisibleOrderedUnderlays.stream()
      .map(SyWindowReadableType::id)
      .forEach(id -> {
        Preconditions.checkPreconditionV(
          this.windows.containsKey(id),
          "Window ID %s not in window map",
          id.value()
        );
      });

    this.windowsVisibleOrderedNormals.stream()
      .map(SyWindowReadableType::id)
      .forEach(id -> {
        Preconditions.checkPreconditionV(
          this.windows.containsKey(id),
          "Window ID %s not in window map",
          id.value()
        );
      });

    this.windowsVisibleOrderedOverlays.stream()
      .map(SyWindowReadableType::id)
      .forEach(id -> {
        Preconditions.checkPreconditionV(
          this.windows.containsKey(id),
          "Window ID %s not in window map",
          id.value()
        );
      });
  }

  /**
   * Create an empty window set.
   *
   * @return The empty set
   */

  public static SyWindowSet empty()
  {
    return new SyWindowSet(
      Map.of(),
      List.of(),
      List.of(),
      List.of()
    );
  }

  /**
   * @param layer The window layer
   *
   * @return The focused window in the set, if any
   */

  public Optional<SyWindowType> windowFocused(
    final SyWindowLayer layer)
  {
    return switch (layer) {
      case WINDOW_LAYER_NORMAL -> {
        yield this.windowsVisibleOrderedNormals.stream()
          .findFirst();
      }
      case WINDOW_LAYER_OVERLAY -> {
        yield this.windowsVisibleOrderedOverlays.stream()
          .findFirst();
      }
      case WINDOW_LAYER_UNDERLAY -> {
        yield this.windowsVisibleOrderedUnderlays.stream()
          .findFirst();
      }
    };
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
    final var underlays =
      this.windowsVisibleOrderedUnderlays.stream()
        .map(SyWindowReadableType::id);
    final var normals =
      this.windowsVisibleOrderedNormals.stream()
        .map(SyWindowReadableType::id);
    final var overlays =
      this.windowsVisibleOrderedOverlays.stream()
        .map(SyWindowReadableType::id);

    return Stream.concat(overlays, Stream.concat(normals, underlays))
      .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * @return The set of windows that are visible in depth order
   */

  public List<SyWindowType> windowsVisibleOrdered()
  {
    final var visible = new ArrayList<SyWindowType>(
      this.windowsVisibleOrderedOverlays.size()
        + this.windowsVisibleOrderedNormals.size()
        + this.windowsVisibleOrderedUnderlays.size()
    );

    visible.addAll(this.windowsVisibleOrderedOverlays);
    visible.addAll(this.windowsVisibleOrderedNormals);
    visible.addAll(this.windowsVisibleOrderedUnderlays);
    return visible;
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
        this.windowsVisibleOrderedUnderlays,
        this.windowsVisibleOrderedNormals,
        this.windowsVisibleOrderedOverlays
      ),
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

    return switch (window.layer()) {
      case WINDOW_LAYER_UNDERLAY -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedUnderlays);

        final var removed = newVisible.remove(window);
        final List<SyWindowEventType> changes;
        if (removed) {
          changes = List.of();
        } else {
          changes = List.of(new SyWindowBecameVisible(window.id()));
        }
        newVisible.addFirst(window);

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            newVisible,
            this.windowsVisibleOrderedNormals,
            this.windowsVisibleOrderedOverlays
          ),
          changes
        );
      }

      case WINDOW_LAYER_NORMAL -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedNormals);

        final var removed = newVisible.remove(window);
        final List<SyWindowEventType> changes;
        if (removed) {
          changes = List.of();
        } else {
          changes = List.of(new SyWindowBecameVisible(window.id()));
        }
        newVisible.addFirst(window);

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            this.windowsVisibleOrderedUnderlays,
            newVisible,
            this.windowsVisibleOrderedOverlays
          ),
          changes
        );
      }

      case WINDOW_LAYER_OVERLAY -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedOverlays);

        final var removed = newVisible.remove(window);
        final List<SyWindowEventType> changes;
        if (removed) {
          changes = List.of();
        } else {
          changes = List.of(new SyWindowBecameVisible(window.id()));
        }
        newVisible.addFirst(window);

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            this.windowsVisibleOrderedUnderlays,
            this.windowsVisibleOrderedNormals,
            newVisible
          ),
          changes
        );
      }
    };
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

    return switch (window.layer()) {
      case WINDOW_LAYER_NORMAL -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedNormals);

        final var removed = newVisible.remove(window);
        final List<SyWindowEventType> changes;
        if (removed) {
          changes = List.of(new SyWindowBecameInvisible(window.id()));
        } else {
          changes = List.of();
        }

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            this.windowsVisibleOrderedUnderlays,
            newVisible,
            this.windowsVisibleOrderedOverlays
          ),
          changes
        );
      }

      case WINDOW_LAYER_OVERLAY -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedOverlays);

        final var removed = newVisible.remove(window);
        final List<SyWindowEventType> changes;
        if (removed) {
          changes = List.of(new SyWindowBecameInvisible(window.id()));
        } else {
          changes = List.of();
        }

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            this.windowsVisibleOrderedUnderlays,
            this.windowsVisibleOrderedNormals,
            newVisible
          ),
          changes
        );
      }

      case WINDOW_LAYER_UNDERLAY -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedUnderlays);

        final var removed = newVisible.remove(window);
        final List<SyWindowEventType> changes;
        if (removed) {
          changes = List.of(new SyWindowBecameInvisible(window.id()));
        } else {
          changes = List.of();
        }

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            newVisible,
            this.windowsVisibleOrderedNormals,
            this.windowsVisibleOrderedOverlays
          ),
          changes
        );
      }
    };
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

    return switch (window.layer()) {
      case WINDOW_LAYER_UNDERLAY -> {
        final var newVisible =
          new ArrayList<>(this.windowsVisibleOrderedUnderlays);
        newVisible.remove(window);

        final var newWindows = new HashMap<>(this.windows);
        newWindows.remove(window.id());

        yield new SyWindowSetChanged(
          new SyWindowSet(
            newWindows,
            newVisible,
            this.windowsVisibleOrderedNormals,
            this.windowsVisibleOrderedOverlays
          ),
          List.of(new SyWindowClosed(window.id()))
        );
      }

      case WINDOW_LAYER_NORMAL -> {
        final var newVisible =
          new ArrayList<>(this.windowsVisibleOrderedNormals);
        newVisible.remove(window);

        final var newWindows = new HashMap<>(this.windows);
        newWindows.remove(window.id());

        yield new SyWindowSetChanged(
          new SyWindowSet(
            newWindows,
            this.windowsVisibleOrderedUnderlays,
            newVisible,
            this.windowsVisibleOrderedOverlays
          ),
          List.of(new SyWindowClosed(window.id()))
        );
      }

      case WINDOW_LAYER_OVERLAY -> {
        final var newVisible =
          new ArrayList<>(this.windowsVisibleOrderedOverlays);
        newVisible.remove(window);

        final var newWindows = new HashMap<>(this.windows);
        newWindows.remove(window.id());

        yield new SyWindowSetChanged(
          new SyWindowSet(
            newWindows,
            this.windowsVisibleOrderedUnderlays,
            this.windowsVisibleOrderedNormals,
            newVisible
          ),
          List.of(new SyWindowClosed(window.id()))
        );
      }
    };
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

    return switch (window.layer()) {
      case WINDOW_LAYER_UNDERLAY -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedUnderlays);

        newVisible.remove(window);
        newVisible.addFirst(window);

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            newVisible,
            this.windowsVisibleOrderedNormals,
            this.windowsVisibleOrderedOverlays
          ),
          List.of()
        );
      }

      case WINDOW_LAYER_NORMAL -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedNormals);

        newVisible.remove(window);
        newVisible.addFirst(window);

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            this.windowsVisibleOrderedUnderlays,
            newVisible,
            this.windowsVisibleOrderedOverlays
          ),
          List.of()
        );
      }

      case WINDOW_LAYER_OVERLAY -> {
        final var newVisible =
          new LinkedList<>(this.windowsVisibleOrderedOverlays);

        newVisible.remove(window);
        newVisible.addFirst(window);

        yield new SyWindowSetChanged(
          new SyWindowSet(
            this.windows,
            this.windowsVisibleOrderedUnderlays,
            this.windowsVisibleOrderedNormals,
            newVisible
          ),
          List.of()
        );
      }
    };
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

    if (this.windows.containsKey(window.id())) {
      return switch (window.layer()) {
        case WINDOW_LAYER_UNDERLAY -> {
          yield this.windowsVisibleOrderedUnderlays.stream()
            .anyMatch(w -> Objects.equals(w.id(), window.id()));
        }
        case WINDOW_LAYER_NORMAL -> {
          yield this.windowsVisibleOrderedNormals.stream()
            .anyMatch(w -> Objects.equals(w.id(), window.id()));
        }
        case WINDOW_LAYER_OVERLAY -> {
          yield this.windowsVisibleOrderedOverlays.stream()
            .anyMatch(w -> Objects.equals(w.id(), window.id()));
        }
      };
    }

    return false;
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
