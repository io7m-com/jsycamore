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

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

/**
 * A single-line text model.
 */

public interface SyTextSingleLineModelType
  extends SyTextSingleLineModelReadableType
{
  /**
   * Set the font.
   *
   * @param font The font
   */

  void setFont(
    SyFontType font);

  /**
   * @return The text
   */

  @Override
  AttributeType<SyText> text();

  /**
   * Set the text value.
   *
   * @param text The text
   */

  default void setText(
    final SyText text)
  {
    this.text().set(text);
  }

  /**
   * Start a selection operation.
   *
   * @param position The parent-relative position
   *
   * @return The initial selection
   */

  SyTextSelection selectionStart(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Continue a selection operation.
   *
   * @param position The parent-relative position
   *
   * @return The selection
   */

  SyTextSelection selectionContinue(
    PVector2I<SySpaceParentRelativeType> position);

  /**
   * Finish a selection operation.
   *
   * @param position The parent-relative position
   *
   * @return The selection
   */

  SyTextSelection selectionFinish(
    PVector2I<SySpaceParentRelativeType> position);
}
