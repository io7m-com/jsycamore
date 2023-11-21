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

package com.io7m.jsycamore.api.themes;

import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;

import java.util.Map;

/**
 * Theme information for windows.
 */

public interface SyThemeWindowType
{
  /**
   * Obtain the Z order for a window component. Components with higher Z values
   * are rendered over the top of components with lower Z values.
   *
   * @param component The window component
   *
   * @return A Z order value
   */

  int zOrderForWindowDecorationComponent(
    SyWindowDecorationComponent component);

  /**
   * Set the positions and sizes of all window components, and execute a layout
   * pass on each of them to allow them to lay out their own descendants.
   *
   * @param layoutContext     The layout context
   * @param windowConstraints The window constraints
   * @param windowComponents  The set of window components
   */

  void layoutWindowComponents(
    SyLayoutContextType layoutContext,
    SyConstraints windowConstraints,
    Map<SyWindowDecorationComponent, SyComponentType> windowComponents);
}
