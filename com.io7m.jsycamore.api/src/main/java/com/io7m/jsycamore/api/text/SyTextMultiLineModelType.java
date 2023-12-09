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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A multi-line text model.
 */

public interface SyTextMultiLineModelType
  extends SyTextMultiLineModelReadableType
{
  /**
   * Set the font.
   *
   * @param font The font
   */

  void setFont(
    SyFontType font);

  /**
   * Set the page width in pixels.
   *
   * @param width The width
   */

  void setPageWidth(
    int width);

  /**
   * Replace an existing text section.
   *
   * @param textID The text ID
   * @param text   The new text section
   */

  void textSectionReplace(
    SyTextID textID,
    SyText text);

  /**
   * Append a section of text at the end of the model.
   *
   * @param section The text
   */

  default void textSectionAppend(
    final SyText section)
  {
    this.textSectionsAppend(
      List.of(Objects.requireNonNull(section, "section"))
    );
  }

  /**
   * Append sections of text at the end of the model.
   *
   * @param sections The text sections
   */

  void textSectionsAppend(
    List<SyText> sections);

  /**
   * Start a selection operation.
   *
   * @param position The parent-relative position
   *
   * @return The initial selection
   */

  Optional<SyTextSelection> selectionStart(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Continue a selection operation.
   *
   * @param position The parent-relative position
   *
   * @return The selection
   */

  Optional<SyTextSelection> selectionContinue(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Finish a selection operation.
   *
   * @param position The parent-relative position
   *
   * @return The selection
   */

  Optional<SyTextSelection> selectionFinish(
    PVector2I<SySpaceParentRelativeType> position);
}
