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
import com.io7m.jsycamore.api.components.SyScrollBarVerticalType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
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
 * A vertical scrollbar.
 */

public final class SyScrollBarV
  extends SyComponentAbstract
  implements SyScrollBarVerticalType
{
  private static final PAreaSizeI<SySpaceParentRelativeType> BUTTON_SIZE_DEFAULT =
    PAreaSizeI.of(16, 16);

  private final SyScrollBarVButtonUp buttonUp;
  private final SyScrollBarVButtonDown buttonDown;
  private final SyScrollBarVTrack track;
  private SyScrollBarHideIfDisabled hideIfDisabled =
    SyScrollBarHideIfDisabled.SHOW_EVEN_IF_DISABLED;

  /**
   * A vertical scrollbar.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra theme classes, if any
   */

  public SyScrollBarV(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);

    this.buttonUp =
      new SyScrollBarVButtonUp(screen);
    this.buttonDown =
      new SyScrollBarVButtonDown(screen);
    this.track =
      new SyScrollBarVTrack(screen);

    this.childAdd(this.buttonUp);
    this.childAdd(this.track);
    this.childAdd(this.buttonDown);
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

    final var buttonThemeSizeUp =
      layoutContext.themeCurrent()
        .findForComponent(this.buttonUp)
        .size(layoutContext, this.buttonUp)
        .orElse(BUTTON_SIZE_DEFAULT);

    final var buttonSizeUpConstraints =
      limitedConstraints.deriveLimitedBy(buttonThemeSizeUp);

    final var buttonThemeSizeDown =
      layoutContext.themeCurrent()
        .findForComponent(this.buttonDown)
        .size(layoutContext, this.buttonDown)
        .orElse(BUTTON_SIZE_DEFAULT);

    final var buttonSizeDownConstraints =
      limitedConstraints.deriveLimitedBy(buttonThemeSizeDown);

    this.buttonUp.layout(
      layoutContext, buttonSizeUpConstraints);
    this.buttonDown.layout(
      layoutContext, buttonSizeDownConstraints);

    /*
     * Now that the buttons have their correct sizes, set the size of the
     * entire scrollbar based on the button size and the passed in constraints.
     */

    final PAreaSizeI<SySpaceParentRelativeType> overallSize =
      limitedConstraints.sizeWithin(
        buttonThemeSizeUp.sizeX(),
        constraints.sizeMaximumY()
      );

    this.setSize(overallSize);

    /*
     * Now, based on all the calculated sizes so far, set the button
     * positions.
     */

    final var buttonUpSize =
      this.buttonUp.size().get();
    final var buttonDownSize =
      this.buttonDown.size().get();

    final var buttonUpAreaStart =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        buttonUpSize.sizeX(),
        buttonUpSize.sizeY());
    final var buttonDownAreaStart =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        buttonDownSize.sizeX(),
        buttonDownSize.sizeY());
    final var thisArea =
      PAreasI.<SySpaceParentRelativeType>create(
        0,
        0,
        overallSize.sizeX(),
        overallSize.sizeY());
    final var buttonUpArea =
      PAreasI.alignOnYMinY(thisArea, buttonUpAreaStart);
    final var buttonDownArea =
      PAreasI.alignOnYMaxY(thisArea, buttonDownAreaStart);

    this.buttonUp.setPosition(
      PVector2I.of(buttonUpArea.minimumX(), buttonUpArea.minimumY())
    );
    this.buttonDown.setPosition(
      PVector2I.of(buttonDownArea.minimumX(), buttonDownArea.minimumY())
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
      PAreasI.fitBetweenOnY(trackArea, buttonUpArea, buttonDownArea);

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
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
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
  public SyButtonReadableType buttonUp()
  {
    return this.buttonUp;
  }

  @Override
  public SyButtonReadableType buttonDown()
  {
    return this.buttonDown;
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

  @Override
  public double scrollIncrementSize()
  {
    return this.track.scrollIncrementSize();
  }

  @Override
  public void setOnClickUpListener(
    final Runnable runnable)
  {
    this.buttonUp.setOnClickListener(runnable);
  }

  @Override
  public void removeOnClickUpListener()
  {
    this.buttonUp.removeOnClickListener();
  }

  @Override
  public void setOnClickDownListener(
    final Runnable runnable)
  {
    this.buttonDown.setOnClickListener(runnable);
  }

  @Override
  public void removeOnClickDownListener()
  {
    this.buttonDown.removeOnClickListener();
  }

  @Override
  public void setHideIfDisabled(
    final SyScrollBarHideIfDisabled newHideIfDisabled)
  {
    this.hideIfDisabled =
      Objects.requireNonNull(newHideIfDisabled, "newHideIfDisabled");
  }
}
