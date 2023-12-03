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


package com.io7m.jsycamore.api.menus;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.MENU_ITEM;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.MENU_ITEM_ATOM;

/**
 * An "atom" menu item. An atom is a simple, named menu item that executes an
 * action when the user clicks it.
 */

public non-sealed interface SyMenuItemAtomType extends SyMenuItemType
{
  @Override
  default List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(MENU_ITEM_ATOM, MENU_ITEM);
  }

  /**
   * @return The icon that appears to the left of the menu item text
   */

  AttributeType<Optional<URI>> icon();

  /**
   * @return The menu item text
   */

  AttributeType<SyText> text();
}
