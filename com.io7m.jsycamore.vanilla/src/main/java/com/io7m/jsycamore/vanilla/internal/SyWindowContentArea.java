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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.SyThemeType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;

import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_CONTENT_AREA;

public final class SyWindowContentArea extends SyWindowComponent
{
  SyWindowContentArea()
  {
    super(WINDOW_CONTENT_AREA);
  }

  @Override
  protected boolean onEvent(
    final SyEventType event)
  {
    return false;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyThemeType theme,
    final SyConstraints constraints)
  {
    final var newSize =
      theme.sizeForWindowDecorationComponent(
        constraints, this.semantic()
      );

    this.size().set(newSize);
    return newSize;
  }
}
