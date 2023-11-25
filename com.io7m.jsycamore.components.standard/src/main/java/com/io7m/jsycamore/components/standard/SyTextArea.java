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
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyTextAreaType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.LinkedList;
import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static java.lang.Math.min;

/**
 * A text area.
 */

public final class SyTextArea
  extends SyComponentAbstract implements SyTextAreaType
{
  private static final int INTERNAL_TEXT_PADDING = 2;
  private static final int INTERNAL_TEXT_PADDING_DOUBLE =
    INTERNAL_TEXT_PADDING + INTERNAL_TEXT_PADDING;

  private final AttributeType<List<String>> textSections;
  private final SyPackVertical textContainer;
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

    this.textContainer = new SyPackVertical(inThemeClassesExtra);
    this.textSections.subscribe(this::invalidateViews);
    this.size().subscribe(this::invalidateViews);

    this.childAdd(this.textContainer);
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
    /*
     * Set this text area to the maximum allowed size.
     */

    final var sizeLimit =
      this.sizeUpperLimit().get();

    final var limitedConstraints =
      new SyConstraints(
        constraints.sizeMinimumX(),
        constraints.sizeMinimumY(),
        min(constraints.sizeMaximumX(), sizeLimit.sizeX()),
        min(constraints.sizeMaximumY(), sizeLimit.sizeY())
      );

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      limitedConstraints.sizeMaximum();

    this.setSize(newSize);

    /*
     * The text container is the current text area size, plus a margin.
     */

    final var internalAreaPosition =
      PVector2I.<SySpaceParentRelativeType>of(
        INTERNAL_TEXT_PADDING,
        INTERNAL_TEXT_PADDING
      );

    final var internalArea =
      PAreaSizeI.<SySpaceParentRelativeType>of(
        Math.max(0, newSize.sizeX() - INTERNAL_TEXT_PADDING_DOUBLE),
        Math.max(0, newSize.sizeY() - INTERNAL_TEXT_PADDING_DOUBLE)
      );

    this.textContainer.position()
      .set(internalAreaPosition);
    this.textContainer.size()
      .set(internalArea);

    /*
     * If the internal text views aren't up-to-date (perhaps because the
     * text changed), then remove and create new views, and then tell the
     * text container to execute a layout.
     */

    if (!this.viewsUpToDate) {
      this.textContainer.childrenClear();

      final var themeComponent =
        layoutContext.themeCurrent()
          .findForComponent(this);

      final var font =
        themeComponent.font(layoutContext, this);

      final var lines =
        font.textLayoutMultiple(
          this.textSections.get(),
          internalArea.sizeX()
        );

      for (final var line : lines) {
        final var textView = new SyTextView();
        textView.setSize(line.size());
        textView.setText(line.text());
        textView.setMouseQueryAccepting(false);
        this.textContainer.childAdd(textView);
      }

      this.textContainer.layout(layoutContext, limitedConstraints);
      this.viewsUpToDate = true;
    }

    return newSize;
  }

  @Override
  public void textSectionAppend(
    final String section)
  {
    final var appended = new LinkedList<>(this.textSections.get());
    appended.add(section);
    this.textSections.set(List.copyOf(appended));
  }
}
