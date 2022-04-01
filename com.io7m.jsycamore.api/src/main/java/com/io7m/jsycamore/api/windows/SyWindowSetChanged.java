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

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * An event indicating that the window set changed.
 *
 * @param newSet  The new window set
 * @param changes The set of changes
 */

public record SyWindowSetChanged(
  SyWindowSet newSet,
  List<SyWindowEventType> changes)
{
  /**
   * An event indicating that the window set changed.
   *
   * @param newSet  The new window set
   * @param changes The set of changes
   */

  public SyWindowSetChanged
  {
    Objects.requireNonNull(newSet, "newSet");
    Objects.requireNonNull(changes, "changes");
  }

  /**
   * Apply a function to the current window set, yielding a new window set
   * change.
   *
   * @param f A function
   *
   * @return The window set change
   */

  public SyWindowSetChanged then(
    final Function<SyWindowSet, SyWindowSetChanged> f)
  {
    return f.apply(this.newSet);
  }
}
