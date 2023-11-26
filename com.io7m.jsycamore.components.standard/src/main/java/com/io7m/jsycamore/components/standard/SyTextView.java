/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyTextViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A simple text view.
 */

public final class SyTextView
  extends SyComponentAbstract implements SyTextViewType
{
  private final AttributeType<String> text;

  /**
   * A simple text view.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyTextView(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);
    final var attributes = SyComponentAttributes.get();
    this.text = attributes.create("");
  }

  /**
   * A simple text view.
   */

  @ConvenienceConstructor
  public SyTextView()
  {
    this(List.of());
  }

  @Override
  protected SyEventConsumed onEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    /*
     * Note that text views are a special case when it comes to layout
     * sizes. Text views must be dynamically sized according to their
     * actual text content, and so cannot realistically be sized at the
     * whim of whatever component is passing in constraints. They can, as
     * a last resort, be hard clipped by setting a maximum size limit.
     */

    final var requiredSize =
      this.minimumSizeRequired(layoutContext);

    final var limitSize =
      this.sizeUpperLimit().get();

    final var newSize =
      PAreaSizeI.<SySpaceParentRelativeType>of(
        Math.min(requiredSize.sizeX(), limitSize.sizeX()),
        Math.min(requiredSize.sizeY(), limitSize.sizeY())
      );

    this.setSize(newSize);
    return newSize;
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> minimumSizeRequired(
    final SyLayoutContextType layoutContext)
  {
    final var font =
      layoutContext.themeCurrent()
        .findForComponent(this)
        .font(layoutContext, this);

    final var textNow = this.text.get();
    final var sizeX = font.textWidth(textNow);
    final var sizeY = font.textHeight();
    return PAreaSizeI.of(sizeX, sizeY);
  }

  @Override
  public AttributeType<String> text()
  {
    return this.text;
  }
}
