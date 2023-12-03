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
 * A trivial container that pads each edge with a configurable amount of empty
 * space.
 */

public final class SyLayoutMargin extends SyLayoutAbstract
{
  private final AttributeType<Integer> paddingTop;
  private final AttributeType<Integer> paddingBottom;
  private final AttributeType<Integer> paddingLeft;
  private final AttributeType<Integer> paddingRight;

  /**
   * A trivial container that pads each edge with a configurable amount of empty
   * space.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra theme classes, if any
   */

  public SyLayoutMargin(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);
    final var attributes = SyComponentAttributes.get();
    this.paddingTop = attributes.create(0);
    this.paddingBottom = attributes.create(0);
    this.paddingLeft = attributes.create(0);
    this.paddingRight = attributes.create(0);
  }

  /**
   * A trivial container that pads each edge with a configurable amount of empty
   * space.
   *
   * @param screen The screen that owns the component
   */

  @ConvenienceConstructor
  public SyLayoutMargin(final SyScreenType screen)
  {
    this(screen, List.of());
  }

  /**
   * Set the padding for all four edges to {@code padding}.
   *
   * @param padding The padding size
   */

  public void setPaddingAll(
    final int padding)
  {
    this.paddingTop().set(padding);
    this.paddingBottom().set(padding);
    this.paddingLeft().set(padding);
    this.paddingRight().set(padding);
  }

  /**
   * @return The top padding
   */

  public AttributeType<Integer> paddingTop()
  {
    return this.paddingTop;
  }

  /**
   * @return The bottom padding
   */

  public AttributeType<Integer> paddingBottom()
  {
    return this.paddingBottom;
  }

  /**
   * @return The left padding
   */

  public AttributeType<Integer> paddingLeft()
  {
    return this.paddingLeft;
  }

  /**
   * @return The right padding
   */

  public AttributeType<Integer> paddingRight()
  {
    return this.paddingRight;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    final var pL = this.paddingLeft.get();
    final var pR = this.paddingRight.get();
    final var pT = this.paddingTop.get();
    final var pB = this.paddingBottom.get();

    final var sizeLimit =
      this.sizeUpperLimit().get();
    final var containerSizeX =
      min(constraints.sizeMaximumX(), sizeLimit.sizeX());
    final var containerSizeY =
      min(constraints.sizeMaximumY(), sizeLimit.sizeY());

    final var newConstraints =
      new SyConstraints(
        0,
        0,
        containerSizeX - (pL + pR),
        containerSizeY - (pT + pB)
      );

    for (final var childNode : this.node().children()) {
      final var child = childNode.value();
      child.layout(layoutContext, newConstraints);
      child.setPosition(PVector2I.of(pL, pT));
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      PAreaSizeI.of(containerSizeX, containerSizeY);
    this.setSize(newSize);
    return newSize;
  }
}
