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
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

/**
 * A simple container that distributes child objects vertically with a
 * configurable amount of padding between the objects.
 */

public final class SyLayoutVertical extends SyLayoutAbstract
{
  private final AttributeType<Integer> paddingBetween;

  /**
   * A simple container that distributes child objects vertically with a
   * configurable amount of padding between the objects.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra theme classes, if any
   */

  public SyLayoutVertical(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);
    this.paddingBetween = SyComponentAttributes.get().create(0);
  }

  /**
   * A simple container that distributes child objects vertically with a
   * configurable amount of padding between the objects.
   *
   * @param screen The screen that owns the component
   */

  @ConvenienceConstructor
  public SyLayoutVertical(
    final SyScreenType screen)
  {
    this(screen, List.of());
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
        containerSizeY / childCount;
      final var paddingHalf =
        this.paddingBetween.get() / 2;

      var offsetY = 0;
      for (int index = 0; index < childCount; ++index) {
        var spaceT = 0;
        var spaceB = 0;
        if (index == 0) {
          spaceB = paddingHalf;
        } else if (index + 1 == childCount) {
          spaceT = paddingHalf;
        } else {
          spaceT = paddingHalf;
          spaceB = paddingHalf;
        }

        final var child = childrenVisible.get(index).value();
        final var shrunkSize = regionSize - (spaceT + spaceB);
        child.layout(layoutContext, new SyConstraints(
          constraints.sizeMinimumX(),
          0,
          containerSizeX,
          shrunkSize
        ));

        offsetY += spaceT;
        child.position().set(PVector2I.of(0, offsetY));
        offsetY += shrunkSize;
        offsetY += spaceB;
      }
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      PAreaSizeI.of(containerSizeX, containerSizeY);
    this.setSize(newSize);
    return newSize;
  }
}
