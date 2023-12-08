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


package com.io7m.jsycamore.components.standard.internal.scrollpanes;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyScrollBarHorizontalType;
import com.io7m.jsycamore.api.components.SyScrollBarVerticalType;
import com.io7m.jsycamore.api.components.SyScrollPaneType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jsycamore.components.standard.SyScrollBarsHorizontal;
import com.io7m.jsycamore.components.standard.SyScrollBarsVertical;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLPANE;

/**
 * The main scroll pane implementation.
 */

public final class SyScrollPane
  extends SyComponentAbstract implements SyScrollPaneType
{
  private final SyScrollBarHorizontalType scrollH;
  private final SyScrollBarVerticalType scrollV;
  private final SyScrollPaneContentArea contentArea;
  private final SyScrollPaneContentAreaViewport contentAreaViewport;
  private final AttributeType<PAreaSizeI<SySpaceParentRelativeType>> contentAreaSize;

  /**
   * The main scroll pane implementation.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra classes
   */

  public SyScrollPane(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);

    this.scrollH =
      SyScrollBarsHorizontal.create(screen);
    this.scrollV =
      SyScrollBarsVertical.create(screen);
    this.contentArea =
      new SyScrollPaneContentArea(screen);
    this.contentAreaViewport =
      new SyScrollPaneContentAreaViewport(screen);

    final var attributes =
      SyComponentAttributes.get();

    this.contentAreaSize =
      attributes.create(PAreaSizeI.of(1024, 1024));

    this.childAdd(this.scrollH);
    this.childAdd(this.scrollV);
    this.childAdd(this.contentAreaViewport);

    this.scrollH.setOnClickLeftListener(
      this.onScrollClickLeftListener());
    this.scrollH.setOnClickRightListener(
      this.onScrollClickRightListener());
    this.scrollV.setOnClickUpListener(
      this.onScrollClickUpListener());
    this.scrollV.setOnClickDownListener(
      this.onScrollClickDownListener());
  }

  private void updateScrollBars()
  {
    final var contentAreaSizeNow =
      this.contentAreaSize.get();
    final var contentViewportSizeNow =
      this.contentAreaViewport.size().get();

    final var viewportX =
      (double) contentViewportSizeNow.sizeX();
    final var viewportY =
      (double) contentViewportSizeNow.sizeY();

    final var contentX =
      (double) contentAreaSizeNow.sizeX();
    final var contentY =
      (double) contentAreaSizeNow.sizeY();

    /*
     * Clamp the results to [0, 1]. This has the effect of stamping out any
     * NaN values if the content area size is zero.
     */

    final var amountX =
      Math.clamp(viewportX / contentX, 0.0, 1.0);
    final var amountY =
      Math.clamp(viewportY / contentY, 0.0, 1.0);

    this.scrollH.setScrollAmountShown(amountX);
    this.scrollV.setScrollAmountShown(amountY);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    this.updateScrollBars();

    /*
     * Resize this main component to the maximum size allowed by the
     * constraints.
     */

    var limitedConstraints =
      layoutContext.deriveThemeConstraints(constraints, this);

    final var sizeLimit =
      this.sizeUpperLimit().get();

    limitedConstraints =
      limitedConstraints.deriveLimitedBy(sizeLimit);

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      limitedConstraints.sizeMaximum();

    this.setSize(newSize);

    /*
     * Perform an initial layout pass for both scrollbars. This gets them
     * configured to their initial vaguely sensible sizes.
     */

    this.scrollH.layout(layoutContext, limitedConstraints);
    this.scrollV.layout(layoutContext, limitedConstraints);

    /*
     * The width of the horizontal scrollbar must be reduced by the width of
     * the vertical scrollbar. This may be nothing if that scrollbar isn't
     * visible.
     */

    final var scrollHConstraints =
      limitedConstraints.deriveSubtractMaximumWidth(
        this.scrollV.size().get().sizeX()
      );

    /*
     * The height of the scrollbar must be reduced by the height of the
     * horizontal scrollbar. This may be nothing if that scrollbar isn't
     * visible.
     */

    final var scrollVConstraints =
      limitedConstraints.deriveSubtractMaximumHeight(
        this.scrollH.size().get().sizeY()
      );

    this.scrollH.layout(layoutContext, scrollHConstraints);
    this.scrollV.layout(layoutContext, scrollVConstraints);

    /*
     * Laying out the content area is slightly more complex. The size of the
     * content area viewport is derived from the size of the scrollpane as a
     * whole, minus the sizes of the scrollbars. The size of the content area
     * within the viewport is of a fixed size and may be far, far larger than
     * the content viewport. If either of the scrollbars aren't enabled, then
     * the viewport is sized such that it overlaps them.
     *
     * Therefore, to set their sizes independently:
     *
     * 1. Detach the content area from the viewport.
     * 2. Lay out the viewport.
     * 3. Lay out the content area.
     * 4. Re-attach the content area to the viewport.
     * 5. Position the content area so that the correct area is seen through
     *    the viewport.
     */

    final var contentViewportConstraints =
      limitedConstraints.deriveSubtractMaximumSizes(
        this.scrollV.size().get().sizeX(),
        this.scrollH.size().get().sizeY()
      );

    final var contentAreaSizeNow =
      this.contentAreaSize.get();

    final var contentAreaConstraints =
      new SyConstraints(
        0,
        0,
        contentAreaSizeNow.sizeX(),
        contentAreaSizeNow.sizeY()
      );

    this.contentAreaViewport.childRemove(this.contentArea);
    this.contentArea.layout(layoutContext, contentAreaConstraints);

    this.contentAreaViewport.layout(layoutContext, contentViewportConstraints);
    this.contentAreaViewport.childAdd(this.contentArea);
    this.contentAreaViewport.setPosition(PVectors2I.zero());

    final var contentViewportSizeNow =
      this.contentAreaViewport.size().get();

    this.scrollH.setPosition(
      PVector2I.of(0, contentViewportSizeNow.sizeY())
    );
    this.scrollV.setPosition(
      PVector2I.of(contentViewportSizeNow.sizeX(), 0)
    );

    /*
     * The content area is offset by a proportion of the scrollable region
     * within the viewport.
     */

    final var scrollRegionX =
      Math.max(0, contentAreaSizeNow.sizeX() - contentViewportSizeNow.sizeX());
    final var scrollRegionY =
      Math.max(0, contentAreaSizeNow.sizeY() - contentViewportSizeNow.sizeY());

    final var contentAreaX =
      (int) -(this.scrollH.scrollPosition() * scrollRegionX);
    final var contentAreaY =
      (int) -(this.scrollV.scrollPosition() * scrollRegionY);

    this.contentArea.setPosition(PVector2I.of(contentAreaX, contentAreaY));
    return newSize;
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
    return List.of(SCROLLPANE);
  }

  @Override
  public SyComponentType contentArea()
  {
    return this.contentArea;
  }

  @Override
  public SyComponentReadableType contentViewport()
  {
    return this.contentAreaViewport;
  }

  @Override
  public void setContentAreaSize(
    final PAreaSizeI<SySpaceParentRelativeType> size)
  {
    this.contentAreaSize.set(size);
  }

  @Override
  public SyScrollBarHorizontalType scrollBarHorizontal()
  {
    return this.scrollH;
  }

  @Override
  public SyScrollBarVerticalType scrollBarVertical()
  {
    return this.scrollV;
  }

  @Override
  public Runnable onScrollClickLeftListener()
  {
    return this::doScrollClickLeft;
  }

  @Override
  public Runnable onScrollClickRightListener()
  {
    return this::doScrollClickRight;
  }

  @Override
  public Runnable onScrollClickUpListener()
  {
    return this::doScrollClickUp;
  }

  @Override
  public Runnable onScrollClickDownListener()
  {
    return this::doScrollClickDown;
  }

  private void doScrollClickLeft()
  {
    this.scrollH.setScrollPosition(this.scrollH.scrollPosition() - this.scrollH.scrollIncrementSize());
  }

  private void doScrollClickRight()
  {
    this.scrollH.setScrollPosition(this.scrollH.scrollPosition() + this.scrollH.scrollIncrementSize());
  }

  private void doScrollClickUp()
  {
    this.scrollV.setScrollPosition(this.scrollV.scrollPosition() - this.scrollV.scrollIncrementSize());
  }

  private void doScrollClickDown()
  {
    this.scrollV.setScrollPosition(this.scrollV.scrollPosition() + this.scrollV.scrollIncrementSize());
  }
}
