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

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;

import java.util.List;
import java.util.Objects;

import static java.lang.Boolean.FALSE;

/**
 * A window decoration component.
 */

public abstract class SyWindowComponent extends SyComponentAbstract
{
  private final SyWindowDecorationComponent semantic;

  protected SyWindowComponent(
    final SyScreenType screen,
    final SyWindowDecorationComponent inSemantic,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses, () -> false);

    this.semantic =
      Objects.requireNonNull(inSemantic, "semantic");
  }

  final SyWindowDecorationComponent semantic()
  {
    return this.semantic;
  }

  @Override
  public final boolean isVisible()
  {
    return switch (this.visibility().get()) {
      case VISIBILITY_INVISIBLE -> false;
      case VISIBILITY_VISIBLE -> {

        /*
         * If this component is a decoration, then it should not be visible
         * if the window is not showing decorations.
         */

        if (this.semantic.isDecoration() && !this.isWindowDecorated()) {
          yield false;
        }

        /*
         * Otherwise, this component is visible if the parent is visible.
         */

        final var parentOpt =
          this.nodeReadable().parentReadable();
        if (parentOpt.isPresent()) {
          final var parent = parentOpt.get().value();
          yield parent.isVisible();
        }

        yield true;
      }
    };
  }

  protected final boolean isWindowDecorated()
  {
    return this.windowReadable()
      .map(SyWindowReadableType::decorated)
      .map(AttributeReadableType::get)
      .orElse(FALSE)
      .booleanValue();
  }
}
