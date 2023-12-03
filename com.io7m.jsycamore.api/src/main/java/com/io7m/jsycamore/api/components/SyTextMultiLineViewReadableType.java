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
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;
import java.util.SortedMap;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.TEXT_MULTILINE_VIEW;

/**
 * Read-only access to multi-line text views.
 */

public interface SyTextMultiLineViewReadableType
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
    return List.of(TEXT_MULTILINE_VIEW);
  }

  /**
   * @return A read-only snapshot of the texts by Y offset
   */

  SortedMap<Integer, SyTextLineMeasuredType> textsByYOffset();

  /**
   * Determine the minimum size on the Y axis required to display the
   * contained text fully.
   *
   * @param layoutContext The layout context
   *
   * @return The minimum size on the Y axis
   */

  int minimumSizeYRequired(SyLayoutContextType layoutContext);
}
