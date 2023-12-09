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
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Optional;
import java.util.SortedMap;

/**
 * A readable multi-line text model.
 */

public interface SyTextMultiLineModelReadableType
{
  /**
   * @return The font used for the model
   */

  SyFontType font();

  /**
   * @return The page width for the model
   */

  int pageWidth();

  /**
   * @return The text sections present in the model
   */

  SortedMap<SyTextID, SyText> textSections();

  /**
   * Inspect the text at the given position. The information returned includes
   * details such as the index of the character within the string at the given
   * location, information for rendering a caret, etc.
   *
   * @param position The position
   *
   * @return Information about text at the given position
   */

  Optional<SyTextLocationType> inspectAt(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Determine the minimum size required on the Y axis to display all the
   * text at the current page width. This is effectively the number of lines
   * multiplied by the height of each line.
   *
   * @return The minimum size required on the Y axis
   */

  int minimumSizeYRequired();

  /**
   * @return The current number of lines
   */

  int lineCount();

  /**
   * @param line The line number
   *
   * @return The line at the given number
   */

  Optional<SyTextLinePositioned> lineAt(
    SyTextLineNumber line);
}
