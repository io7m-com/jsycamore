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

import com.io7m.jsycamore.api.text.SyTextLineNumber;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

/**
 * A bidirectional map between line numbers and Y offsets.
 */

final class SyLineYBiMap
{
  private final TreeMap<SyTextLineNumber, Integer> lineToY;
  private final TreeMap<Integer, SyTextLineNumber> yToLine;

  SyLineYBiMap()
  {
    this.lineToY = new TreeMap<>();
    this.yToLine = new TreeMap<>();
  }

  void clear()
  {
    this.lineToY.clear();
    this.yToLine.clear();
  }

  void set(
    final SyTextLineNumber lineNumber,
    final int y)
  {
    Objects.requireNonNull(lineNumber, "lineNumber");

    final var boxY = Integer.valueOf(y);
    this.removeByLine(lineNumber);
    this.removeByY(y);

    this.lineToY.put(lineNumber, boxY);
    this.yToLine.put(boxY, lineNumber);
  }

  Optional<SyTextLineNumber> lineContainingY(
    final int y)
  {
    return Optional.ofNullable(this.yToLine.floorEntry(Integer.valueOf(y)))
      .map(Map.Entry::getValue);
  }

  Optional<Integer> highestY()
  {
    try {
      return Optional.of(this.yToLine.lastKey());
    } catch (final NoSuchElementException e) {
      return Optional.empty();
    }
  }

  Optional<Integer> removeByLine(
    final SyTextLineNumber line)
  {
    Objects.requireNonNull(line, "line");

    final var y = this.lineToY.remove(line);
    if (y != null) {
      this.yToLine.remove(y);
    }
    return Optional.ofNullable(y);
  }

  Optional<SyTextLineNumber> removeByY(
    final int y)
  {
    final var line = this.yToLine.remove(Integer.valueOf(y));
    if (line != null) {
      this.lineToY.remove(line);
    }
    return Optional.ofNullable(line);
  }

  Optional<SyTextLineNumber> line(
    final int y)
  {
    return Optional.ofNullable(this.yToLine.get(Integer.valueOf(y)));
  }

  Optional<Integer> y(
    final SyTextLineNumber lineNumber)
  {
    Objects.requireNonNull(lineNumber, "lineNumber");
    return Optional.ofNullable(this.lineToY.get(lineNumber));
  }
}
