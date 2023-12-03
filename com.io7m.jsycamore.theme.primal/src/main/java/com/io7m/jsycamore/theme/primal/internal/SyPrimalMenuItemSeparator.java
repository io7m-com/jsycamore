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

package com.io7m.jsycamore.theme.primal.internal;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.internal.SyPrimalValues.PRIMARY_OVER;

/**
 * A theme component for menus.
 */

public final class SyPrimalMenuItemSeparator extends SyPrimalAbstract
{
  /**
   * A theme component for menus.
   *
   * @param inTheme The theme
   */

  public SyPrimalMenuItemSeparator(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
  }

  @Override
  public SyRenderNodeType render(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    final var area = component.boundingArea();
    final var sizeX = area.sizeX();
    final var sizeY = area.sizeY();
    final var midY = sizeY / 2;

    final var rectangle =
      new SyShapeRectangle<SySpaceComponentRelativeType>(
        PAreaI.of(0, sizeX, midY, midY + 1)
      );

    try {
      final var values = this.theme().values();
      return new SyRenderNodeShape(
        "MenuItemSeparator",
        PVectors2I.zero(),
        Optional.empty(),
        Optional.of(values.fillFlat(PRIMARY_OVER)),
        rectangle
      );
    } catch (final SyThemeValueException e) {
      throw new IllegalStateException(e);
    }
  }
}
