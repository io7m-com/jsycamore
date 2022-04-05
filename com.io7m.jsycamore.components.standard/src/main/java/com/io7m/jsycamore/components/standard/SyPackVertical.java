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
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_LEFT;
import static java.lang.Math.min;

/**
 * A simple container that packs child objects vertically with a configurable
 * amount of padding between the objects.
 */

public final class SyPackVertical extends SyLayoutAbstract
{
  private final AttributeType<Integer> paddingBetween;
  private final AttributeType<SyAlignmentHorizontal> alignHorizontal;
  private final AttributeType<SyResizeBehaviour> childSizeXBehaviour;

  /**
   * A simple container that distributes child objects vertically with a
   * configurable amount of padding between the objects.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyPackVertical(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);
    final var attributes = SyComponentAttributes.get();
    this.paddingBetween = attributes.create(0);
    this.alignHorizontal = attributes.create(ALIGN_HORIZONTAL_LEFT);
    this.childSizeXBehaviour = attributes.create(PRESERVE);
  }

  /**
   * A simple container that distributes child objects vertically with a
   * configurable amount of padding between the objects.
   */

  @ConvenienceConstructor
  public SyPackVertical()
  {
    this(List.of());
  }

  /**
   * A simple container that distributes child objects vertically with a
   * configurable amount of padding between the objects.
   *
   * @param padding The padding to insert between objects
   */

  @ConvenienceConstructor
  public SyPackVertical(
    final int padding)
  {
    this(List.of());
    this.paddingBetween.set(padding);
  }

  /**
   * @return The horizontal alignment behaviour for child elements
   */

  public AttributeType<SyAlignmentHorizontal> alignHorizontal()
  {
    return this.alignHorizontal;
  }

  /**
   * @return The padding between objects
   */

  public AttributeType<Integer> paddingBetween()
  {
    return this.paddingBetween;
  }

  /**
   * @return The size X behaviour for child elements
   */

  public AttributeType<SyResizeBehaviour> childSizeXBehaviour()
  {
    return this.childSizeXBehaviour;
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

    final var sizeLimit =
      this.sizeUpperLimit().get();
    final var containerSizeX =
      min(constraints.sizeMaximumX(), sizeLimit.sizeX());
    final var containerSizeY =
      min(constraints.sizeMaximumY(), sizeLimit.sizeY());

    var maximumX = 0;
    var offsetY = 0;
    final var padding = this.paddingBetween.get().intValue();
    final var alignH = this.alignHorizontal.get();

    final var childConstraints =
      new SyConstraints(
        switch (this.childSizeXBehaviour.get()) {
          case PRESERVE -> 0;
          case FILL_SPACE -> containerSizeX;
        },
        0,
        containerSizeX,
        containerSizeY
      );

    for (final var childNode : childrenVisible) {
      final var child = childNode.value();
      final var childSize =
        child.layout(layoutContext, childConstraints);

      final var childSizeX = childSize.sizeX();
      maximumX = Math.max(maximumX, childSizeX);

      final var offsetX = switch (alignH) {
        case ALIGN_HORIZONTAL_LEFT -> 0;
        case ALIGN_HORIZONTAL_RIGHT -> containerSizeX - childSizeX;
        case ALIGN_HORIZONTAL_CENTER -> (containerSizeY / 2) - (childSizeX / 2);
      };

      child.position().set(PVector2I.of(offsetX, offsetY));
      offsetY += childSize.sizeY();
      offsetY += padding;
    }

    final var totalSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(maximumX, offsetY);

    this.setSize(totalSize);
    return totalSize;
  }
}
