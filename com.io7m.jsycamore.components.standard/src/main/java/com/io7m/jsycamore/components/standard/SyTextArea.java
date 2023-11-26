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


package com.io7m.jsycamore.components.standard;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyScrollBarHorizontalType;
import com.io7m.jsycamore.api.components.SyScrollBarVerticalType;
import com.io7m.jsycamore.api.components.SyScrollPaneType;
import com.io7m.jsycamore.api.components.SyTextAreaType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A text area.
 */

public final class SyTextArea
  extends SyComponentAbstract implements SyTextAreaType
{
  private static final int EDGE_PADDING = 8;
  private static final int PADDING = EDGE_PADDING * 2;

  private final AttributeType<List<String>> textSections;
  private final SyPackVertical textLayout;
  private final SyScrollPaneType textScroller;
  private final SyLayoutMargin textLayoutMargin;
  private boolean viewsUpToDate;

  /**
   * A text area.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyTextArea(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);

    final var attributes =
      SyComponentAttributes.get();
    this.textSections =
      attributes.create(List.of());

    this.textScroller =
      SyScrollPanes.create(inThemeClassesExtra);
    this.textLayoutMargin =
      new SyLayoutMargin();
    this.textLayout =
      new SyPackVertical();

    this.textLayoutMargin.childAdd(this.textLayout);
    this.textLayoutMargin.setPaddingAll(EDGE_PADDING);
    this.textScroller.contentArea().childAdd(this.textLayoutMargin);
    this.childAdd(this.textScroller);

    this.textSections
      .subscribe(this::invalidateViews);
    this.size()
      .subscribe(this::invalidateViews);
  }

  private void invalidateViews(
    final Object ignored0,
    final Object ignored1)
  {
    this.viewsUpToDate = false;
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public AttributeReadableType<List<String>> textSections()
  {
    return this.textSections;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var newSize =
      super.layout(layoutContext, constraints);

    /*
     * If the internal text views aren't up-to-date (perhaps because the
     * text changed), then remove and create new views. This will yield a
     * set of views from which we can determine the total required height
     * of the content.
     */

    if (!this.viewsUpToDate) {
      this.regenerateViews(layoutContext);
    }

    final var textHeightRequired =
      this.textViews()
        .mapToInt(c -> c.size().get().sizeY())
        .sum();

    final var viewportSize =
      this.textScroller.contentViewport()
        .size()
        .get()
        .sizeX();

    this.textScroller.setContentAreaSize(
      PAreaSizeI.of(viewportSize, textHeightRequired)
    );

    this.textScroller.layout(layoutContext, constraints);
    return newSize;
  }

  /**
   * @return The width at which text should be wrapped
   */

  private int textWrapWidth()
  {
    /*
     * Wrap all text such that it is no wider than the viewport size, minus
     * the padding applied to the text area.
     */

    final var viewportSize =
      this.textScroller.contentViewport()
        .size()
        .get()
        .sizeX();

    return Math.max(0, viewportSize - PADDING);
  }

  private Stream<SyComponentReadableType> textViews()
  {
    return this.textLayout
      .nodeReadable()
      .childrenReadable()
      .stream()
      .map(JOTreeNodeReadableType::value);
  }

  private void regenerateViews(
    final SyLayoutContextType layoutContext)
  {
    this.textLayout.childrenClear();

    final var themeComponent =
      layoutContext.themeCurrent()
        .findForComponent(this);

    final var font =
      themeComponent.font(layoutContext, this);

    final var lines =
      font.textLayoutMultiple(
        this.textSections.get(),
        this.textWrapWidth()
      );

    for (final var line : lines) {
      final var textView = new SyTextView();
      textView.setSize(line.size());
      textView.setText(line.text());
      textView.setMouseQueryAccepting(false);
      this.textLayout.childAdd(textView);
    }

    this.viewsUpToDate = true;
  }

  @Override
  public void textSectionAppend(
    final String section)
  {
    final var appended = new LinkedList<>(this.textSections.get());
    appended.add(section);
    this.textSections.set(List.copyOf(appended));
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
