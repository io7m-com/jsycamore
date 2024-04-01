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


package com.io7m.jsycamore.tests;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.keyboard.SyKeyboardFocusBehavior;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

public final class SyBlob extends SyComponentAbstract
{
  private int preferredSizeX;
  private int preferredSizeY;

  public SyBlob(final SyScreenType screen)
  {
    super(
      screen,
      List.of(),
      SyKeyboardFocusBehavior.IGNORES_FOCUS_AND_STOPS_TRAVERSAL
    );
  }

  public SyBlob(
    final SyScreenType screen,
    final int x,
    final int y)
  {
    this(screen);
    this.setPreferredSizeX(x);
    this.setPreferredSizeY(y);
  }

  public int preferredSizeX()
  {
    return this.preferredSizeX;
  }

  public void setPreferredSizeX(
    final int inPreferredSizeX)
  {
    this.preferredSizeX = inPreferredSizeX;
  }

  public int preferredSizeY()
  {
    return this.preferredSizeY;
  }

  public void setPreferredSizeY(
    final int inPreferredSizeY)
  {
    this.preferredSizeY = inPreferredSizeY;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    Objects.requireNonNull(constraints, "constraints");

    final var innerSize =
      constraints.sizeWithin(this.preferredSizeX, this.preferredSizeY);
    final var innerConstraints =
      new SyConstraints(
        constraints.sizeMinimumX(),
        constraints.sizeMinimumY(),
        innerSize.sizeX(),
        innerSize.sizeY()
      );

    final var childNodes = this.node().children();
    for (final var childNode : childNodes) {
      childNode.value().layout(layoutContext, innerConstraints);
    }

    this.setSize(innerConstraints.sizeMaximum());
    return innerConstraints.sizeMaximum();
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of();
  }
}
