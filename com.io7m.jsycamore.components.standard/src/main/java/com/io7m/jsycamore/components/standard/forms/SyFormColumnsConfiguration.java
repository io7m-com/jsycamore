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

package com.io7m.jsycamore.components.standard.forms;

import com.io7m.junreachable.UnimplementedCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The configuration values for the columns of a form row.
 */

public final class SyFormColumnsConfiguration
{
  private final List<SyFormColumnSizeType> sizes;
  private final ArrayList<Integer> evaluatedSizes;
  private int evaluatedSizeLast;

  /**
   * Create a configuration based on the given size constraints.
   *
   * @param inSizes The size constraints
   */

  public SyFormColumnsConfiguration(
    final List<SyFormColumnSizeType> inSizes)
  {
    this.sizes =
      List.copyOf(Objects.requireNonNull(inSizes, "sizes"));

    this.evaluatedSizes = new ArrayList<>(this.sizes.size());
    for (int index = 0; index < this.sizes.size(); ++index) {
      this.evaluatedSizes.add(0);
    }

    this.evaluatedSizeLast =
      Integer.MIN_VALUE;
  }

  /**
   * Create a configuration based on the given size constraints.
   *
   * @param sizes The size constraints
   *
   * @return A columns configuration
   */

  public static SyFormColumnsConfiguration columns(
    final SyFormColumnSizeType... sizes)
  {
    return new SyFormColumnsConfiguration(List.of(sizes));
  }

  /**
   * Retrieve the evaluated size for a column. Callers must have called {@link
   * #evaluateSizes(int)} first in order to get sensible values back.
   *
   * @param index The column index
   *
   * @return The evaluated size for column {@code index}
   */

  public int sizeFor(
    final int index)
  {
    try {
      return this.evaluatedSizes.get(index);
    } catch (final IndexOutOfBoundsException e) {
      return 0;
    }
  }

  /**
   * Evaluate the column sizes for a row of size {@code sizeX}.
   *
   * @param sizeX The row size on the X axis
   */

  public void evaluateSizes(
    final int sizeX)
  {
    if (this.evaluatedSizeLast == sizeX) {
      return;
    }

    var sizeFixed = 0;
    var countFlexible = 0;
    for (final var size : this.sizes) {
      if (size instanceof SyFormColumnSizeExact exact) {
        sizeFixed += exact.size();
      } else if (size instanceof SyFormColumnSizeFlexible) {
        ++countFlexible;
      } else {
        throw new UnimplementedCodeException();
      }
    }

    final var remaining = sizeX - sizeFixed;
    final int flexible;
    if (countFlexible > 0) {
      flexible = remaining / countFlexible;
    } else {
      flexible = 0;
    }

    for (int index = 0; index < this.sizes.size(); ++index) {
      final var size = this.sizes.get(index);
      if (size instanceof SyFormColumnSizeExact exact) {
        this.evaluatedSizes.set(index, exact.size());
      } else if (size instanceof SyFormColumnSizeFlexible) {
        this.evaluatedSizes.set(index, flexible);
      } else {
        throw new UnimplementedCodeException();
      }
    }

    this.evaluatedSizeLast = sizeX;
  }
}
