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
import com.io7m.jsycamore.api.components.SyResizeBehaviour;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.components.SyResizeBehaviour.PRESERVE;
import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

/**
 * A simple container that packs child objects horizontally with a configurable
 * amount of padding between the objects.
 */

public final class SyPackHorizontal extends SyLayoutAbstract
{
  private final AttributeType<Integer> paddingBetween;
  private final AttributeType<SyAlignmentVertical> alignVertical;
  private final AttributeType<SyResizeBehaviour> childSizeYBehaviour;

  /**
   * A simple container that distributes child objects horizontally with a
   * configurable amount of padding between the objects.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyPackHorizontal(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);
    final var attributes = SyComponentAttributes.get();
    this.paddingBetween = attributes.create(0);
    this.alignVertical = attributes.create(ALIGN_VERTICAL_CENTER);
    this.childSizeYBehaviour = attributes.create(PRESERVE);
  }

  /**
   * A simple container that distributes child objects horizontally with a
   * configurable amount of padding between the objects.
   */

  @ConvenienceConstructor
  public SyPackHorizontal()
  {
    this(List.of());
  }

  /**
   * A simple container that distributes child objects horizontally with a
   * configurable amount of padding between the objects.
   *
   * @param padding The padding to insert between objects
   */

  @ConvenienceConstructor
  public SyPackHorizontal(
    final int padding)
  {
    this(List.of());
    this.paddingBetween.set(padding);
  }

  /**
   * @return The padding between objects
   */

  public AttributeType<Integer> paddingBetween()
  {
    return this.paddingBetween;
  }

  /**
   * @return The vertical alignment for child elements
   */

  public AttributeType<SyAlignmentVertical> alignVertical()
  {
    return this.alignVertical;
  }

  /**
   * @return The size Y behaviour for child elements
   */

  public AttributeType<SyResizeBehaviour> childSizeYBehaviour()
  {
    return this.childSizeYBehaviour;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    final var childNodes =
      this.node().children();

    final var childrenVisible =
      childNodes.stream()
        .filter(n -> n.value().isVisible())
        .toList();

    final var limitX =
      this.limitSizeX().get().intValue();
    final var limitY =
      this.limitSizeY().get().intValue();
    final var containerSizeX =
      Math.min(constraints.sizeMaximumX(), limitX);
    final var containerSizeY =
      Math.min(constraints.sizeMaximumY(), limitY);

    var maximumY = 0;
    var offsetX = 0;
    final var padding = this.paddingBetween.get().intValue();
    final var alignV = this.alignVertical.get();

    final var childConstraints =
      new SyConstraints(
        0,
        switch (this.childSizeYBehaviour.get()) {
          case PRESERVE -> 0;
          case FILL_SPACE -> containerSizeY;
        },
        containerSizeX,
        containerSizeY
      );

    for (final var childNode : childrenVisible) {
      final var child = childNode.value();
      child.layout(layoutContext, childConstraints);

      final var childSize = child.size().get();
      final var childSizeY = childSize.sizeY();
      maximumY = Math.max(maximumY, childSizeY);

      final var offsetY = switch (alignV) {
        case ALIGN_VERTICAL_TOP -> 0;
        case ALIGN_VERTICAL_BOTTOM -> containerSizeY - childSizeY;
        case ALIGN_VERTICAL_CENTER -> (containerSizeY / 2) - (childSizeY / 2);
      };

      child.position().set(PVector2I.of(offsetX, offsetY));
      offsetX += childSize.sizeX();
      offsetX += padding;
    }

    final var totalSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(offsetX, maximumY);

    this.setSize(totalSize);
    return totalSize;
  }
}
