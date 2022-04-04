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


package com.io7m.jsycamore.components.standard;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.CONTAINER;

/**
 * An empty space element.
 */

public final class SySpace extends SyComponentAbstract
{
  private final AttributeType<PAreaSizeI<SySpaceParentRelativeType>> preferredSize;

  /**
   * An empty space element.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SySpace(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);
    this.setMouseQueryAccepting(false);

    this.preferredSize =
      SyComponentAttributes.get()
        .create(PAreaSizeI.of(0, 0));
  }

  /**
   * An empty space element.
   */

  @ConvenienceConstructor
  public SySpace()
  {
    this(List.of());
  }

  /**
   * @return The preferred size of the spacer element
   */

  public AttributeType<PAreaSizeI<SySpaceParentRelativeType>> preferredSize()
  {
    return this.preferredSize;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var prefSize =
      this.preferredSize.get();
    final var newSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(
        prefSize.sizeX(),
        prefSize.sizeY()
      );
    this.setSize(newSize);
    return newSize;
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(CONTAINER);
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }
}
