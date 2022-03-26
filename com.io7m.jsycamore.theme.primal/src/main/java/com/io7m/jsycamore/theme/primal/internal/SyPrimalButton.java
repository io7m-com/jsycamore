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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.rendering.SyPaintFillType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BACKGROUND_DISABLED_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BACKGROUND_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BACKGROUND_OVER_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BUTTON_PRESSED_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.EDGE;

/**
 * A theme component for buttons.
 */

public final class SyPrimalButton extends SyPrimalAbstract
{
  /**
   * A theme component for buttons.
   *
   * @param inTheme The theme
   */

  public SyPrimalButton(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
  }

  private static Optional<SyPaintFillType> renderForButton(
    final SyThemePrimal theme,
    final SyButtonReadableType button)
  {
    if (button.isPressed()) {
      return theme.parameterForFillRGBA(BUTTON_PRESSED_FILL);
    }
    return theme.parameterForFillRGBA(BACKGROUND_OVER_FILL);
  }

  @Override
  public SyRenderNodeType render(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    final var area =
      component.boundingArea();
    final var rectAll =
      new SyShapeRectangle<SySpaceComponentRelativeType>(
        PAreasI.cast(PAreasI.moveToOrigin(area)));

    final Optional<SyPaintFillType> fill;
    final var theme = this.theme();
    if (component.isActive()) {
      if (component.isMouseOver()) {
        if (component instanceof SyButtonReadableType button) {
          fill = renderForButton(theme, button);
        } else {
          fill = theme.parameterForFillRGBA(BACKGROUND_OVER_FILL);
        }
      } else {
        fill = theme.parameterForFillRGBA(BACKGROUND_FILL);
      }
    } else {
      fill = theme.parameterForFillRGBA(BACKGROUND_DISABLED_FILL);
    }

    final var edge =
      theme.parameterForEdgeRGBA(EDGE);

    return new SyRenderNodeShape(edge, fill, rectAll);
  }
}
