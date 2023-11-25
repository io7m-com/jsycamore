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

package com.io7m.jsycamore.api.layout;

import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeType;

import java.util.Objects;

/**
 * The context of a layout operation.
 */

public interface SyLayoutContextType extends SyThemeContextType
{
  /**
   * @return A reference to the current theme
   */

  SyThemeType themeCurrent();

  @Override
  SyFontDirectoryType fonts();

  /**
   * Derive size constraints based on the current theme, if any exist.
   *
   * @param constraints The initial constraints
   * @param component   The component
   *
   * @return The possibly-limited constraints
   */

  default SyConstraints deriveThemeConstraints(
    final SyConstraints constraints,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(constraints, "constraints");
    Objects.requireNonNull(component, "component");

    /*
     * Consult the theme to see if there are size constraints specified
     * for this component. Derive a new set of constraints that try to
     * satisfy the theme whilst also satisfying the passed in constraints.
     */

    final var themeSize =
      this.themeCurrent()
        .findForComponent(component)
        .size(this, component);

    return themeSize.map(constraints::deriveLimitedBy)
      .orElse(constraints);
  }
}
