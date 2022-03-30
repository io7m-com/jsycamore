/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.api.themes;

import java.util.List;
import java.util.stream.Stream;

/**
 * The type of readable objects that can have theme information applied to
 * them.
 */

public interface SyThemeableReadableType
{
  /**
   * Obtain the list of theme classes in preference order that are the default
   * classes for a particular type of component.
   *
   * @return The list of theme classes
   */

  List<SyThemeClassNameType> themeClassesDefaultForComponent();

  /**
   * Obtain the list of extra theme classes in preference order.
   *
   * @return The list of theme classes
   */

  List<SyThemeClassNameType> themeClassesExtra();

  /**
   * Obtain the list of theme classes in preference order. When searching for
   * theme information, the theme will be consulted for each class in the list
   * in order, and the theme will return information for the first class it
   * recognizes. Essentially, a list {@code [A, B, C]} allows a component to say
   * "If you have theme rules for a class {@code A}, use those. Otherwise, if
   * you have theme rules for a class {@code B}, use those. Otherwise, use the
   * rules for a class {@code C}".
   *
   * @return The list of theme classes
   */

  default List<SyThemeClassNameType> themeClassesInPreferenceOrder()
  {
    return Stream.concat(
      this.themeClassesExtra().stream(),
      this.themeClassesDefaultForComponent().stream()
    ).toList();
  }
}
