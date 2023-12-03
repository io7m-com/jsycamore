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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyScrollBarDrag;
import com.io7m.jsycamore.api.components.SyScrollBarHideIfDisabled;
import com.io7m.jsycamore.api.components.SyScrollBarHorizontalType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.io7m.jsycamore.api.active.SyActive.ACTIVE;
import static com.io7m.jsycamore.api.active.SyActive.INACTIVE;
import static com.io7m.jsycamore.api.components.SyScrollBarHideIfDisabled.HIDE_IF_DISABLED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A horizontal scrollbar.
 */

public final class SyScrollBarH
  extends SyComponentAbstract
  implements SyScrollBarHorizontalType
{
  private static final PAreaSizeI<SySpaceParentRelativeType> BUTTON_SIZE_DEFAULT =
    PAreaSizeI.of(16, 16);

  private final SyScrollBarHButtonLeft buttonLeft;
  private final SyScrollBarHButtonRight buttonRight;
  private final SyScrollBarHTrack track;
  private SyScrollBarHideIfDisabled hideIfDisabled =
    SyScrollBarHideIfDisabled.SHOW_EVEN_IF_DISABLED;

  /**
   * A horizontal scrollbar.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra theme classes, if any
   */

  public SyScrollBarH(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);

    this.buttonLeft =
      new SyScrollBarHButtonLeft(screen);
    this.buttonRight =
      new SyScrollBarHButtonRight(screen);
    this.track =
      new SyScrollBarHTrack(screen);

    this.childAdd(this.buttonLeft);
    this.childAdd(this.track);
    this.childAdd(this.buttonRight);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    /*
     * Scrollbars are able to hide themselves by collapsing to zero size.
     */

    if (this.hideIfDisabled == HIDE_IF_DISABLED) {
      if (!this.isActive()) {
        final var zero =
          PAreaSizeI.<SySpaceParentRelativeType>of(0, 0);
        this.setSize(zero);
        return zero;
      }
    }

    var limitedConstraints =
      layoutContext.deriveThemeConstraints(constraints, this);

    /*
     * Then, limit the derived constraints further by the component's size
     * limit.
     */

    final var sizeLimit =
      this.sizeUpperLimit().get();

    limitedConstraints = limitedConstraints.deriveLimitedBy(sizeLimit);

    /*
     * Set the desired sizes of the various buttons based on the passed
     * in constraints, and then execute a layout on each of the buttons.
     */

    final var buttonThemeSizeLeft =
      layoutContext.themeCurrent()
        .findForComponent(this.buttonLeft)
        .size(layoutContext, this.buttonLeft)
        .orElse(BUTTON_SIZE_DEFAULT);

    final var buttonSizeLeftConstraints =
      limitedConstraints.deriveLimitedBy(buttonThemeSizeLeft);

    final var buttonThemeSizeRight =
      layoutContext.themeCurrent()
        .findForComponent(this.buttonRight)
        .size(layoutContext, this.buttonRight)
        .orElse(BUTTON_SIZE_DEFAULT);

    final var buttonSizeRightConstraints =
      limitedConstraints.deriveLimitedBy(buttonThemeSizeRight);

    this.buttonLeft.layout(
      layoutContext, buttonSizeLeftConstraints);
    this.buttonRight.layout(
      layoutContext, buttonSizeRightConstraints);

    /*
     * Now that the buttons have their correct sizes, set the size of the
     * entire scrollbar based on the button size and the passed in constraints.
     */

    final PAreaSizeI<SySpaceParentRelativeType> overallSize =
      limitedConstraints.sizeWithin(
        constraints.sizeMaximumX(),
        buttonThemeSizeLeft.sizeY()
      );

    this.setSize(overallSize);

    /*
     * Now, based on all the calculated sizes so far, set the button
     * positions.
     */

    final var buttonLeftSize =
      this.buttonLeft.size().get();
    final var buttonRightSize =
      this.buttonRight.size().get();

    final var buttonLeftAreaStart =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        buttonLeftSize.sizeX(),
        buttonLeftSize.sizeY());
    final var buttonRightAreaStart =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        buttonRightSize.sizeX(),
        buttonRightSize.sizeY());
    final var thisArea =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        overallSize.sizeX(),
        overallSize.sizeY());
    final var buttonLeftArea =
      PAreasI.alignOnXMinX(thisArea, buttonLeftAreaStart);
    final var buttonRightArea =
      PAreasI.alignOnXMaxX(thisArea, buttonRightAreaStart);

    this.buttonLeft.setPosition(
      PVector2I.of(buttonLeftArea.minimumX(), buttonLeftArea.minimumY())
    );
    this.buttonRight.setPosition(
      PVector2I.of(buttonRightArea.minimumX(), buttonRightArea.minimumY())
    );

    /*
     * Now fit the track between the two buttons.
     */

    final var trackArea =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        overallSize.sizeX(),
        overallSize.sizeY());
    final var trackAreaFitted =
      PAreasI.fitBetweenOnX(trackArea, buttonLeftArea, buttonRightArea);

    final var trackConstraints =
      limitedConstraints.deriveLimitedBy(
        PAreaSizeI.of(trackAreaFitted.sizeX(), trackAreaFitted.sizeY())
      );

    this.track.layout(layoutContext, trackConstraints);
    this.track.setPosition(
      PVector2I.of(trackAreaFitted.minimumX(), trackAreaFitted.minimumY())
    );

    return overallSize;
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public void setOnClickLeftListener(
    final Runnable runnable)
  {
    this.buttonLeft.setOnClickListener(runnable);
  }

  @Override
  public void removeOnClickLeftListener()
  {
    this.buttonLeft.removeOnClickListener();
  }

  @Override
  public void setOnClickRightListener(
    final Runnable runnable)
  {
    this.buttonRight.setOnClickListener(runnable);
  }

  @Override
  public void removeOnClickRightListener()
  {
    this.buttonRight.removeOnClickListener();
  }

  @Override
  public void setOnThumbDragListener(
    final Consumer<SyScrollBarDrag> listener)
  {
    this.track.setOnThumbDragListener(listener);
  }

  @Override
  public void removeOnThumbDragListener()
  {
    this.track.removeOnThumbDragListener();
  }

  @Override
  public double scrollIncrementSize()
  {
    return this.track.scrollIncrementSize();
  }

  @Override
  public void setHideIfDisabled(
    final SyScrollBarHideIfDisabled newHideIfDisabled)
  {
    this.hideIfDisabled =
      Objects.requireNonNull(newHideIfDisabled, "newHideIfDisabled");
  }

  @Override
  public void setScrollPosition(
    final double position)
  {
    this.track.setScrollPosition(position);
  }

  @Override
  public void setScrollPositionSnapping(
    final double fraction)
  {
    this.track.setScrollPositionSnap(fraction);
  }

  @Override
  public void setScrollAmountShown(
    final double amount)
  {
    this.track.setScrollAmountShown(amount);
    this.setActive(this.track.scrollAmountShown() >= 1.0 ? INACTIVE : ACTIVE);
  }

  @Override
  public SyButtonReadableType buttonLeft()
  {
    return this.buttonLeft;
  }

  @Override
  public SyButtonReadableType buttonRight()
  {
    return this.buttonRight;
  }

  @Override
  public SyComponentReadableType thumb()
  {
    return this.track.thumb();
  }

  @Override
  public SyComponentReadableType track()
  {
    return this.track;
  }

  @Override
  public double scrollAmountShown()
  {
    return this.track.scrollAmountShown();
  }

  @Override
  public double scrollPosition()
  {
    return this.track.scrollPosition();
  }

  @Override
  public double scrollPositionSnapping()
  {
    return this.track.scrollPositionSnapping();
  }
}
