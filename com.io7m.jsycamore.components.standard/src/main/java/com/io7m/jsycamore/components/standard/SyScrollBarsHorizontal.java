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


package com.io7m.jsycamore.components.standard;

import com.io7m.jsycamore.api.components.SyScrollBarHorizontalType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.internal.scrollbars.SyScrollBarHorizontalBasic;

import java.util.List;

/**
 * Functions to create horizontal scroll bars.
 */

public final class SyScrollBarsHorizontal
{
  private SyScrollBarsHorizontal()
  {

  }

  /**
   * Create a horizontal scrollbar.
   *
   * @param themeClassesExtra The extra theme classes
   *
   * @return A scrollbar
   */

  public static SyScrollBarHorizontalType create(
    final List<SyThemeClassNameType> themeClassesExtra)
  {
    return new SyScrollBarHorizontalBasic(themeClassesExtra);
  }

  /**
   * Create a horizontal scrollbar.
   *
   * @return A scrollbar
   */

  public static SyScrollBarHorizontalType create()
  {
    return create(List.of());
  }
}
