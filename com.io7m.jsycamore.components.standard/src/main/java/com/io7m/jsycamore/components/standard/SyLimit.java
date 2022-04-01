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
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.List;

/**
 * A container that limits child components to the maximum given size.
 */

public final class SyLimit extends SyLayoutAbstract
{
  private final AttributeType<PAreaSizeI<SySpaceParentRelativeType>> limitSize;

  /**
   * A container that limits child components to the maximum given size.
   *
   * @param themeClassesExtra The extra theme classes, if any
   */

  public SyLimit(
    final List<SyThemeClassNameType> themeClassesExtra)
  {
    super(themeClassesExtra);
    final var attributes = SyComponentAttributes.get();
    this.limitSize = attributes.create(PAreaSizeI.of(
      Integer.MAX_VALUE,
      Integer.MAX_VALUE));
  }

  /**
   * A container that limits child components to the maximum given size.
   */

  @ConvenienceConstructor
  public SyLimit()
  {
    this(List.of());
  }

  /**
   * A container that limits child components to the maximum given size.
   *
   * @param size The size limit
   */

  @ConvenienceConstructor
  public SyLimit(
    final PAreaSizeI<SySpaceParentRelativeType> size)
  {
    this(List.of());
    this.limitSize().set(size);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var limitedConstraints =
      new SyConstraints(
        constraints.sizeMinimumX(),
        constraints.sizeMinimumY(),
        Math.min(constraints.sizeMaximumX(), this.limitSize.get().sizeX()),
        Math.min(constraints.sizeMaximumY(), this.limitSize.get().sizeY())
      );

    final var childNodes = this.node().children();
    for (final var childNode : childNodes) {
      final var component = childNode.value();
      component.layout(layoutContext, limitedConstraints);
      component.setPosition(PVectors2I.zero());
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      limitedConstraints.sizeMaximum();
    this.setSize(newSize);
    return newSize;
  }

  /**
   * @return An attribute representing the maximum size limit
   */

  public AttributeType<PAreaSizeI<SySpaceParentRelativeType>> limitSize()
  {
    return this.limitSize;
  }
}
