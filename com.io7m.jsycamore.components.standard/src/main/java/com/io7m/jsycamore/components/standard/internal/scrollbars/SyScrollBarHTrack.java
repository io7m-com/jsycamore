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
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLBAR_HORIZONTAL_TRACK;
import static java.lang.StrictMath.round;

final class SyScrollBarHTrack extends SyComponentAbstract
{
  private final SyScrollBarHButtonThumb thumb;
  private double scrollAmount;
  private double scrollPosition;
  private double scrollPositionSnap;

  SyScrollBarHTrack()
  {
    super(List.of());

    this.thumb = new SyScrollBarHButtonThumb(this);
    this.childAdd(this.thumb);
  }

  /**
   * Snap a real number {@code x} to a snapping value. The value of {@code c}
   * is a fractional value in the range {@code [0, 1]}.
   *
   * @param x The number to be snapped
   * @param c The snapping value
   *
   * @return The snapped value
   */

  private static double snap(
    final double x,
    final double c)
  {
    if (c == 0.0) {
      return x;
    }

    final var p = 1.0 / c;
    var r = x * p;
    r = (double) round(r);
    return r / p;
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SCROLLBAR_HORIZONTAL_TRACK);
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
     * of the X axis.
     */

    final var thumbWidthMinimum =
      size.sizeY();
    final var thumbWidthMaximum =
      size.sizeX();

    final var thumbWidth =
      (int) InterpolationD.interpolateLinear(
        thumbWidthMinimum,
        thumbWidthMaximum,
        this.scrollAmount
      );

    final var limitedConstraints =
      new SyConstraints(
        Math.max(constraints.sizeMinimumX(), thumbWidth),
        constraints.sizeMinimumY(),
        Math.min(constraints.sizeMaximumX(), thumbWidth),
        constraints.sizeMaximumY()
      );

    this.thumb.layout(layoutContext, limitedConstraints);

    final var thumbPosition =
      this.scrollPosition * (size.sizeX() - this.thumb.size().get().sizeX());

    this.thumb.setPosition(PVector2I.of((int) thumbPosition, 0));
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
      snap(clampedPosition, this.scrollPositionSnap);
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
}
