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

import java.util.Objects;

/**
 * A text location.
 */

public interface SyTextLocationType
  extends Comparable<SyTextLocationType>
{
  /**
   * @return The line number of the text
   */

  SyTextLineNumber lineNumber();

  /**
   * @return The character at the location
   */

  SyCharacter characterAt();

  /**
   * @return The caret for this location
   */

  SyCaret caret();

  /**
   * A caret.
   *
   * @param area The caret area
   */

  record SyCaret(
    PAreaI<SySpaceParentRelativeType> area)
  {
    /**
     * A caret.
     */

    public SyCaret
    {
      Objects.requireNonNull(area, "area");
    }
  }

  /**
   * Information about a character at a location.
   *
   * @param isLeadingEdge      {@code true} if the location is at the leading edge of a character
   * @param centerCharacter    The character at the center index
   * @param centerIndex        The center index
   * @param insertionCharacter The character at the insertion index
   * @param insertionIndex     The insertion index
   */

  record SyCharacter(
    boolean isLeadingEdge,
    int centerIndex,
    char centerCharacter,
    int insertionIndex,
    char insertionCharacter)
  {

  }
}
