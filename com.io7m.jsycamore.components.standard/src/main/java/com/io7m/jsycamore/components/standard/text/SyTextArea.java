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


package com.io7m.jsycamore.components.standard.text;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyScrollBarHorizontalType;
import com.io7m.jsycamore.api.components.SyScrollBarVerticalType;
import com.io7m.jsycamore.api.components.SyScrollPaneType;
import com.io7m.jsycamore.api.components.SyTextAreaType;
import com.io7m.jsycamore.api.components.SyTextMultiLineViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextMultiLineModelType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jsycamore.components.standard.SyLayoutMargin;
import com.io7m.jsycamore.components.standard.SyScrollPanes;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A text area.
 */

public final class SyTextArea
  extends SyComponentAbstract implements SyTextAreaType
{
  private static final int EDGE_PADDING = 8;
  private static final int PADDING = EDGE_PADDING * 2;

  private final AttributeType<List<SyText>> textSections;
  private final SyScrollPaneType textScroller;
  private final SyLayoutMargin textLayoutMargin;
  private final SyTextMultiLineViewType textMultiLine;

  /**
   * A text area.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra theme classes, if any
   */

  public SyTextArea(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);

    final var attributes =
      SyComponentAttributes.get();
    this.textSections =
      attributes.create(List.of());

    this.textMultiLine =
      SyTextMultiLineView.multiLineTextView(screen, List.of());
    this.textLayoutMargin =
      new SyLayoutMargin(screen);
    this.textScroller =
      SyScrollPanes.create(screen, themeClasses);

    this.textLayoutMargin.childAdd(this.textMultiLine);
    this.textLayoutMargin.setPaddingAll(EDGE_PADDING);
    this.textScroller.contentArea().childAdd(this.textLayoutMargin);
    this.childAdd(this.textScroller);
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    /*
     * Perform an initial layout pass.
     * This allows us to get a width for the scroll viewport.
     */

    super.layout(layoutContext, constraints);

    /*
     * Work out the required size of the content area. The width is
     * equal to the viewport. The height is equal to the required height
     * of the text.
     */

    final var contentSizeX =
      this.textScroller.contentViewport()
        .size()
        .get()
        .sizeX();

    final var contentSizeY =
      this.textMultiLine.minimumSizeYRequired(layoutContext) + PADDING;

    this.textScroller.setContentAreaSize(
      PAreaSizeI.of(contentSizeX, contentSizeY)
    );

    return super.layout(layoutContext, constraints);
  }

  @Override
  public SyTextMultiLineModelType model()
  {
    return this.textMultiLine.model();
  }

  @Override
  public SyScrollBarHorizontalType scrollbarHorizontal()
  {
    return this.textScroller.scrollBarHorizontal();
  }

  @Override
  public SyScrollBarVerticalType scrollbarVertical()
  {
    return this.textScroller.scrollBarVertical();
  }
}
