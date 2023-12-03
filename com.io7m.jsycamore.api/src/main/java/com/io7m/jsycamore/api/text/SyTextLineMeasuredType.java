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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceTextType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

/**
 * A line of text that has been analyzed/measured.
 */

public interface SyTextLineMeasuredType
{
  /**
   * The bounds in terms of a line on a page: The bounds with a height equal
   * to the text, and the width equal to the page width.
   *
   * @return The size of the page line
   */

  PAreaSizeI<SySpaceParentRelativeType> pageLineBounds();

  /**
   * The smallest bounds that can contain the given text. An area of this
   * size will be positioned somewhere within an area of size {@link #pageLineBounds()}
   * for rendering and mouse interactions.
   *
   * @return The size of the text line
   */

  PAreaSizeI<SySpaceParentRelativeType> textBounds();

  /**
   * @return The actual text
   */

  SyText text();

  /**
   * Inspect the text at the given position. The information returned includes
   * details such as the index of the character within the string at the given
   * location, information for rendering a caret, etc.
   *
   * @param position The position
   *
   * @return Information about text at the given position
   */

  SyTextLocationType inspectAt(
    PVector2I<SySpaceTextType> position);

  /**
   * Transform the given parent-relative coordinates to text-space coordinates.
   *
   * @param position The parent-relative position
   *
   * @return The equivalent text-space position
   */

  PVector2I<SySpaceTextType> transformToTextCoordinates(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Inspect the character at the given parent relative location.
   *
   * @param position The parent-relative position
   *
   * @return The character at the given location
   */

  default SyTextLocationType inspectAtParentRelative(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    return this.inspectAt(
      this.transformToTextCoordinates(position)
    );
  }

  /**
   * @return The line number
   */

  int lineNumber();
}
