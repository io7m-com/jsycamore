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

package com.io7m.jsycamore.components.standard.forms;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.ConvenienceConstructor;
import com.io7m.jsycamore.components.standard.SyLayoutAbstract;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

/**
 * A vertical form layout consisting of rows of columns.
 */

public final class SyForm extends SyLayoutAbstract
{
  private final SyFormColumnsConfiguration columnConfiguration;

  /**
   * A vertical form layout consisting of rows of columns.
   *
   * @param screen              The screen that owns the component
   * @param themeClasses   The extra theme classes, if any
   * @param inColumnConfiguration The column configuration
   */

  public SyForm(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses,
    final SyFormColumnsConfiguration inColumnConfiguration)
  {
    super(screen, themeClasses);
    this.columnConfiguration =
      Objects.requireNonNull(inColumnConfiguration, "inColumnConfiguration");
  }

  /**
   * A vertical form layout consisting of rows of columns.
   *
   * @param screen              The screen that owns the component
   * @param inColumnConfiguration The column configuration
   */

  @ConvenienceConstructor
  public SyForm(
    final SyScreenType screen,
    final SyFormColumnsConfiguration inColumnConfiguration)
  {
    this(screen, List.of(), inColumnConfiguration);
  }

  /**
   * Add a new row to the form.
   *
   * @return The row
   */

  public SyFormRow addRow()
  {
    final var row = new SyFormRow(this.screen(), this.columnConfiguration);
    this.childAdd(row);
    return row;
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

    final var childConstraints =
      new SyConstraints(
        containerSizeX,
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

      child.position().set(PVector2I.of(0, offsetY));
      offsetY += childSize.sizeY();
    }

    final var totalSize =
      constraints.<SySpaceParentRelativeType>sizeWithin(maximumX, offsetY);

    this.setSize(totalSize);
    return totalSize;
  }
}
