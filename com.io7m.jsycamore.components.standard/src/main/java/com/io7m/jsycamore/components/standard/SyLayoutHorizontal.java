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
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;
import static java.lang.Math.min;

/**
 * A simple container that evenly distributes child objects horizontally with a
 * configurable amount of padding between the objects.
 */

public final class SyLayoutHorizontal extends SyLayoutAbstract
{
  private final AttributeType<Integer> paddingBetween;
  private final AttributeType<SyAlignmentVertical> alignVertical;

  /**
   * A simple container that distributes child objects horizontally with a
   * configurable amount of padding between the objects.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyLayoutHorizontal(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);
    final var attributes = SyComponentAttributes.get();
    this.paddingBetween = attributes.create(0);
    this.alignVertical = attributes.create(ALIGN_VERTICAL_CENTER);
  }

  /**
   * A simple container that distributes child objects horizontally with a
   * configurable amount of padding between the objects.
   */

  @ConvenienceConstructor
  public SyLayoutHorizontal()
  {
    this(List.of());
  }

  /**
   * @return The padding between objects
   */

  public AttributeType<Integer> paddingBetween()
  {
    return this.paddingBetween;
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

    final var childCount =
      childrenVisible.size();

    final var sizeLimit =
      this.sizeUpperLimit().get();
    final var containerSizeX =
      min(constraints.sizeMaximumX(), sizeLimit.sizeX());
    final var containerSizeY =
      min(constraints.sizeMaximumY(), sizeLimit.sizeY());

    if (childCount > 0) {
      final var regionSize =
        containerSizeX / childCount;
      final var paddingHalf =
        this.paddingBetween.get() / 2;

      var offsetX = 0;
      for (int index = 0; index < childCount; ++index) {
        var spaceL = 0;
        var spaceR = 0;
        if (index == 0) {
          spaceR = paddingHalf;
        } else if (index + 1 == childCount) {
          spaceL = paddingHalf;
        } else {
          spaceL = paddingHalf;
          spaceR = paddingHalf;
        }

        final var child = childrenVisible.get(index).value();
        final var shrunkSize = regionSize - (spaceL + spaceR);

        child.layout(layoutContext, new SyConstraints(
          0,
          constraints.sizeMinimumY(),
          shrunkSize,
          containerSizeY
        ));

        final var childSizeY = child.size().get().sizeY();
        final var offsetY = (containerSizeY / 2) - (childSizeY / 2);

        offsetX += spaceL;
        child.position().set(PVector2I.of(offsetX, offsetY));
        offsetX += shrunkSize;
        offsetX += spaceR;
      }
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      PAreaSizeI.of(containerSizeX, containerSizeY);
    this.setSize(newSize);
    return newSize;
  }
}
