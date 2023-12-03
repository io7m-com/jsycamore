/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.components;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextMultiLineModelType;

import java.util.List;
import java.util.Objects;

/**
 * Write access to multi-line text views.
 */

public interface SyTextMultiLineViewType
  extends SyTextMultiLineViewReadableType, SyComponentType
{
  /**
   * @return An attribute indicating if this text view is selectable
   */

  @Override
  AttributeType<Boolean> textSelectable();

  /**
   * Set whether the text view is selectable.
   *
   * @param selectable {@code true} if the text view is selectable
   */

  default void setTextSelectable(
    final boolean selectable)
  {
    this.textSelectable().set(Boolean.valueOf(selectable));
  }

  /**
   * Append a section of text.
   *
   * @param section The text section
   *
   * @see SyTextMultiLineModelType#textSectionAppend(SyText)
   */

  default void textSectionAppend(
    final SyText section)
  {
    this.textSectionsAppend(
      List.of(Objects.requireNonNull(section, "section"))
    );
  }

  /**
   * Append sections of text.
   *
   * @param sections The text sections
   *
   * @see SyTextMultiLineModelType#textSectionsAppend(List)
   */

  void textSectionsAppend(
    List<SyText> sections);
}
