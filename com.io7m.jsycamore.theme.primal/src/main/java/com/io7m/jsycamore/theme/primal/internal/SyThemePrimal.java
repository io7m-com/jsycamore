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

package com.io7m.jsycamore.theme.primal.internal;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.SyThemeType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

public final class SyThemePrimal implements SyThemeType
{
  public SyThemePrimal()
  {

  }

  private static final int RESIZE_BAR_THICKNESS = 8;
  private static final int RESIZE_BOX_SIZE = 8;
  private static final int BUTTON_SIZE = 24;
  private static final int TITLE_HEIGHT = 24;

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> sizeForWindowDecorationComponent(
    final SyConstraints constraints,
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_ROOT -> {
        yield constraints.sizeMaximum();
      }

      case WINDOW_CONTENT_AREA -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - (RESIZE_BOX_SIZE + RESIZE_BOX_SIZE),
          constraints.sizeMaximumY() - ((RESIZE_BOX_SIZE + RESIZE_BOX_SIZE) + TITLE_HEIGHT)
        );
      }

      case WINDOW_RESIZE_NW, WINDOW_RESIZE_SW, WINDOW_RESIZE_SE, WINDOW_RESIZE_NE -> {
        yield constraints.sizeWithin(
          RESIZE_BOX_SIZE,
          RESIZE_BOX_SIZE
        );
      }

      case WINDOW_RESIZE_N, WINDOW_RESIZE_S -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - (RESIZE_BOX_SIZE + RESIZE_BOX_SIZE),
          RESIZE_BAR_THICKNESS);
      }

      case WINDOW_RESIZE_E, WINDOW_RESIZE_W -> {
        yield constraints.sizeWithin(
          RESIZE_BAR_THICKNESS,
          constraints.sizeMaximumY() - (RESIZE_BOX_SIZE + RESIZE_BOX_SIZE));
      }

      case WINDOW_BUTTON_CLOSE, WINDOW_BUTTON_MENU, WINDOW_BUTTON_MAXIMIZE -> {
        yield constraints.sizeWithin(
          BUTTON_SIZE,
          BUTTON_SIZE
        );
      }

      case WINDOW_TITLE -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - ((RESIZE_BOX_SIZE + RESIZE_BOX_SIZE) + (BUTTON_SIZE + BUTTON_SIZE + BUTTON_SIZE)),
          TITLE_HEIGHT
        );
      }
    };
  }

  @Override
  public PVector2I<SySpaceParentRelativeType> positionForWindowDecorationComponent(
    final PAreaSizeI<SySpaceParentRelativeType> sizeParent,
    final PAreaSizeI<SySpaceParentRelativeType> size,
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_ROOT -> {
        yield PVectors2I.zero();
      }

      case WINDOW_CONTENT_AREA -> {
        yield PVector2I.of(
          RESIZE_BOX_SIZE,
          RESIZE_BOX_SIZE + TITLE_HEIGHT);
      }

      case WINDOW_RESIZE_NW -> {
        yield PVector2I.of(
          0,
          0
        );
      }

      case WINDOW_RESIZE_N -> {
        yield PVector2I.of(
          RESIZE_BOX_SIZE,
          0
        );
      }

      case WINDOW_RESIZE_NE -> {
        yield PVector2I.of(
          sizeParent.sizeX() - size.sizeX(),
          0
        );
      }

      case WINDOW_RESIZE_E -> {
        yield PVector2I.of(
          sizeParent.sizeX() - size.sizeX(),
          RESIZE_BOX_SIZE
        );
      }

      case WINDOW_RESIZE_SE -> {
        yield PVector2I.of(
          sizeParent.sizeX() - size.sizeX(),
          sizeParent.sizeY() - size.sizeY()
        );
      }

      case WINDOW_RESIZE_S -> {
        yield PVector2I.of(
          RESIZE_BOX_SIZE,
          sizeParent.sizeY() - size.sizeY()
        );
      }

      case WINDOW_RESIZE_SW -> {
        yield PVector2I.of(
          0,
          sizeParent.sizeY() - size.sizeY()
        );
      }

      case WINDOW_RESIZE_W -> {
        yield PVector2I.of(
          0,
          RESIZE_BOX_SIZE
        );
      }

      case WINDOW_BUTTON_CLOSE -> {
        yield PVector2I.of(
          RESIZE_BOX_SIZE,
          RESIZE_BOX_SIZE
        );
      }

      case WINDOW_BUTTON_MAXIMIZE -> {
        yield PVector2I.of(
          sizeParent.sizeX() - (RESIZE_BOX_SIZE + BUTTON_SIZE),
          RESIZE_BOX_SIZE
        );
      }

      case WINDOW_BUTTON_MENU -> {
        yield PVector2I.of(
          sizeParent.sizeX() - (RESIZE_BOX_SIZE + BUTTON_SIZE + BUTTON_SIZE),
          RESIZE_BOX_SIZE
        );
      }

      case WINDOW_TITLE -> {
        yield PVector2I.of(
          RESIZE_BOX_SIZE + BUTTON_SIZE,
          RESIZE_BOX_SIZE
        );
      }
    };
  }

  @Override
  public int zOrderForWindowDecorationComponent(
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_BUTTON_CLOSE,
        WINDOW_RESIZE_NW,
        WINDOW_RESIZE_W,
        WINDOW_RESIZE_SW,
        WINDOW_RESIZE_SE,
        WINDOW_RESIZE_S,
        WINDOW_RESIZE_NE,
        WINDOW_RESIZE_N,
        WINDOW_RESIZE_E,
        WINDOW_CONTENT_AREA,
        WINDOW_BUTTON_MENU,
        WINDOW_BUTTON_MAXIMIZE -> 2;
      case WINDOW_ROOT -> 0;
      case WINDOW_TITLE -> 1;
    };
  }
}
