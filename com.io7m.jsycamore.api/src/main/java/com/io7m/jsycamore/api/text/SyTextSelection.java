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


package com.io7m.jsycamore.api.text;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;

import java.util.List;
import java.util.Objects;

/**
 * An immutable text selection.
 *
 * @param lowerInclusive The inclusive lower bound of the text selection
 * @param upperInclusive The inclusive upper bound of the text selection
 * @param regions        The selection regions, for rendering purposes
 */

public record SyTextSelection(
  SyTextLocationType lowerInclusive,
  SyTextLocationType upperInclusive,
  List<PAreaI<SySpaceParentRelativeType>> regions)
{
  /**
   * An immutable text selection.
   *
   * @param lowerInclusive The inclusive lower bound of the text selection
   * @param upperInclusive The inclusive upper bound of the text selection
   * @param regions        The selection regions, for rendering purposes
   */

  public SyTextSelection
  {
    Objects.requireNonNull(lowerInclusive, "lowerInclusive");
    Objects.requireNonNull(upperInclusive, "upperInclusive");
    Objects.requireNonNull(regions, "regions");
  }
}
