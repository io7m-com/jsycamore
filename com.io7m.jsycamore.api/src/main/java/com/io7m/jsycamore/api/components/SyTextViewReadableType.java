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

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.TEXT_VIEW;

/**
 * Read-only access to text views.
 */

public interface SyTextViewReadableType
  extends SyComponentReadableType
{
  /**
   * @return An attribute indicating if this text view is selectable
   */

  AttributeReadableType<Boolean> textSelectable();

  /**
   * @return {@code true} if {@link #textSelectable()} is {@code true}
   */

  default boolean isTextSelectable()
  {
    return this.textSelectable().get().booleanValue();
  }

  @Override
  default List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(TEXT_VIEW);
  }

  /**
   * Determine the minimum size required to display the contained text fully.
   *
   * @param layoutContext The layout context
   *
   * @return The minimum size
   */

  PAreaSizeI<SySpaceParentRelativeType> minimumSizeRequired(
    SyLayoutContextType layoutContext);

  /**
   * @return The current text
   */

  AttributeReadableType<SyText> text();
}
