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
 * A row within a form.
 */

public final class SyFormRow extends SyLayoutAbstract
{
  private final SyFormColumnsConfiguration configuration;

  /**
   * A row within a form.
   *
   * @param screen          The screen that owns the component
   * @param themeClasses    The extra theme classes, if any
   * @param inConfiguration The column configuration
   */

  public SyFormRow(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses,
    final SyFormColumnsConfiguration inConfiguration)
  {
    super(screen, themeClasses);
    this.configuration =
      Objects.requireNonNull(inConfiguration, "configuration");
  }

  /**
   * A row within a form.
   *
   * @param screen          The screen that owns the component
   * @param inConfiguration The column configuration
   */

  @ConvenienceConstructor
  public SyFormRow(
    final SyScreenType screen,
    final SyFormColumnsConfiguration inConfiguration)
  {
    this(screen, List.of(), inConfiguration);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    this.configuration.evaluateSizes(constraints.sizeMaximumX());

    final var sizeLimit =
      this.sizeUpperLimit().get();
    final var containerSizeX =
      min(constraints.sizeMaximumX(), sizeLimit.sizeX());
    final var containerSizeY =
      min(constraints.sizeMaximumY(), sizeLimit.sizeY());

    int index = 0;
    var offsetX = 0;
    final var childNodes = this.node().children();
    for (final var childNode : childNodes) {
      final var size = this.configuration.sizeFor(index);

      final var childConstraints =
        new SyConstraints(
          size,
          constraints.sizeMinimumY(),
          size,
          containerSizeY
        );

      final var child = childNode.value();
      child.layout(layoutContext, childConstraints);
      child.setPosition(PVector2I.of(offsetX, 0));

      offsetX += size;
      ++index;
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      PAreaSizeI.of(containerSizeX, containerSizeY);
    this.setSize(newSize);
    return newSize;
  }
}
