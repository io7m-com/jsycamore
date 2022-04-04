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

package com.io7m.jsycamore.components.standard.internal;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.menus.SyMenuItemSeparatorType;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A menu item separator.
 */

public final class SyMenuItemSeparator
  extends SyComponentAbstract
  implements SyMenuItemSeparatorType
{
  private final SyMenuType menu;

  /**
   * A menu item separator.
   *
   * @param inMenu The owning menu
   */

  public SyMenuItemSeparator(
    final SyMenuType inMenu)
  {
    super(List.of());
    this.menu = Objects.requireNonNull(inMenu, "menu");
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var newSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(
        constraints.sizeMaximumX(), 4);
    this.setSize(newSize);
    return newSize;
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> minimumSizeRequired(
    final SyLayoutContextType layoutContext)
  {
    return PAreaSizeI.of(0, 4);
  }

  @Override
  public SyMenuType menu()
  {
    return this.menu;
  }
}
