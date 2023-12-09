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

import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceTextAlignedType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

/**
 * <p>A line of text that has been analyzed/measured.</p>
 * <p>Note that this line of text may have been produced as part of a text
 * formatting operation, and therefore there is an 1:N relationship between
 * the text given by {@link #textOriginal()} and {@link #textAsWrapped()}.
 * All of the methods here work in terms of {@link #textAsWrapped()}.</p>
 */

public interface SyTextLineMeasuredType
{
  /**
   * @return The width of the page within this line resides
   */

  int pageWidth();

  /**
   * @return The height required to contain this text
   */

  int height();

  /**
   * @return The smallest width that can contain the given text.
   */

  int textWidth();

  /**
   * @return The original text that produced this line
   */

  SyTextID textOriginal();

  /**
   * @return The actual text after analysis/wrapping
   */

  SyText textAsWrapped();

  /**
   * Inspect the text at the given position. The information returned includes
   * details such as the index of the character within the string at the given
   * location, information for rendering a caret, etc. The location will be
   * reported to be at line {@code lineNumber}.
   *
   * @param lineNumber The line number
   * @param position   The position
   *
   * @return Information about text at the given position
   */

  SyTextLocationType inspectAt(
    SyTextLineNumber lineNumber,
    PVector2I<SySpaceTextAlignedType> position);

  /**
   * Transform the given parent-relative coordinates to text-space coordinates.
   *
   * @param position The parent-relative position
   *
   * @return The equivalent text-space position
   */

  PVector2I<SySpaceTextAlignedType> transformToTextCoordinates(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Inspect the character at the given parent relative location. The location
   * will be reported to be at line {@code lineNumber}.
   *
   * @param lineNumber The line number
   * @param position   The parent-relative position
   *
   * @return The character at the given location
   */

  default SyTextLocationType inspectAtParentRelative(
    final SyTextLineNumber lineNumber,
    final PVector2I<SySpaceParentRelativeType> position)
  {
    return this.inspectAt(
      lineNumber,
      this.transformToTextCoordinates(position)
    );
  }
}
