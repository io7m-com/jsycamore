/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.components.standard.internal.scrollbars;

import com.io7m.jinterp.InterpolationD;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyScrollBarDrag;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.function.Consumer;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.layout.SySnapping.snapDouble;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLBAR_VERTICAL_TRACK;

final class SyScrollBarVTrack extends SyComponentAbstract
{
  private final SyScrollBarVButtonThumb thumb;
  private double scrollAmount;
  private double scrollPosition;
  private double scrollPositionSnap;

  SyScrollBarVTrack()
  {
    super(List.of());

    this.thumb = new SyScrollBarVButtonThumb(this);
    this.childAdd(this.thumb);
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SCROLLBAR_VERTICAL_TRACK);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var size =
      super.layout(layoutContext, constraints);

    /*
     * The minimum size on the Y axis that would yield a square is the value
     * of the X axis. The smallest the thumb can be is a square. The largest
     * it can be is "fill the entire track".
     */

    final var thumbHeightMinimum =
      size.sizeX();
    final var thumbHeightMaximum =
      size.sizeY();

    final var thumbHeight =
      (int) InterpolationD.interpolateLinear(
        thumbHeightMinimum,
        thumbHeightMaximum,
        this.scrollAmount
      );

    final var limitedConstraints =
      new SyConstraints(
        constraints.sizeMinimumX(),
        Math.max(constraints.sizeMinimumY(), thumbHeight),
        constraints.sizeMaximumX(),
        Math.min(constraints.sizeMaximumY(), thumbHeight)
      );

    this.thumb.layout(layoutContext, limitedConstraints);

    final var thumbPosition =
      this.scrollPosition * (size.sizeY() - this.thumb.size().get().sizeY());

    this.thumb.setPosition(PVector2I.of(0, (int) thumbPosition));
    return size;
  }

  void setOnThumbDragListener(
    final Consumer<SyScrollBarDrag> listener)
  {
    this.thumb.setOnThumbDragListener(listener);
  }

  void removeOnThumbDragListener()
  {
    this.thumb.removeOnThumbDragListener();
  }

  void setScrollPositionSnap(
    final double fraction)
  {
    this.scrollPositionSnap = Math.clamp(fraction, 0.0, 1.0);
  }

  void setScrollPosition(
    final double position)
  {
    final var clampedPosition =
      Math.clamp(position, 0.0, 1.0);
    this.scrollPosition =
      snapDouble(clampedPosition, this.scrollPositionSnap);
  }

  void setScrollAmountShown(
    final double extent)
  {
    this.scrollAmount = Math.clamp(extent, 0.0, 1.0);
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  double scrollPosition()
  {
    return this.scrollPosition;
  }

  SyComponentReadableType thumb()
  {
    return this.thumb;
  }

  double scrollPositionSnapping()
  {
    return this.scrollPositionSnap;
  }

  double scrollAmountShown()
  {
    return this.scrollAmount;
  }

  public double scrollIncrementSize()
  {
    final var height =
      (double) this.size().get().sizeY();
    final var base =
      1.0 / height;
    return snapDouble(base, this.scrollPositionSnap);
  }
}
