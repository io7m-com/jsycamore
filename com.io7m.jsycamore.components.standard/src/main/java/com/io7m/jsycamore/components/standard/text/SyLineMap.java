/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.components.standard.text;

import com.io7m.jsycamore.api.text.SyTextID;
import com.io7m.jsycamore.api.text.SyTextLineNumber;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A multimap from text IDs to sets of line numbers.
 */

final class SyLineMap
{
  private final TreeMap<SyTextID, SortedSet<SyTextLineNumber>> items;

  SyLineMap()
  {
    this.items = new TreeMap<>();
  }

  SortedSet<SyTextLineNumber> linesForText(
    final SyTextID id)
  {
    return Optional.ofNullable(this.items.get(id))
      .orElse(Collections.emptySortedSet());
  }

  void clear()
  {
    this.items.clear();
  }

  void lineAssociate(
    final SyTextID id,
    final SyTextLineNumber lineNumber)
  {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(lineNumber, "lineNumber");

    var lines = this.items.get(id);
    if (lines == null) {
      lines = new TreeSet<>();
    }
    lines.add(lineNumber);
    this.items.put(id, lines);
  }

  void lineDisassociate(
    final SyTextID id,
    final SyTextLineNumber lineNumber)
  {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(lineNumber, "lineNumber");

    final var lines = this.items.get(id);
    if (lines == null) {
      return;
    }
    lines.remove(lineNumber);
    this.items.put(id, lines);
  }

  SortedSet<SyTextLineNumber> lineDisassociateAll(
    final SyTextID id)
  {
    Objects.requireNonNull(id, "id");
    return Optional.ofNullable(this.items.remove(id))
      .orElse(Collections.emptySortedSet());
  }
}
