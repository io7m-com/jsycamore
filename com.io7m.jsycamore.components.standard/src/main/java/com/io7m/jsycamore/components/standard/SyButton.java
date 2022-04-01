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
import com.io7m.jsycamore.api.components.SyButtonWithTextType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A button with a text label.
 */

public final class SyButton
  extends SyButtonAbstract
  implements SyButtonWithTextType
{
  private final SyTextView text;
  private final SyAlign align;
  private final SyLayoutMargin margin;

  /**
   * A button with a text label.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyButton(
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inThemeClassesExtra);
    this.margin = new SyLayoutMargin();
    this.margin.setPaddingAll(8);

    this.align = new SyAlign();
    this.align.alignmentHorizontal()
      .set(SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER);
    this.align.alignmentVertical()
      .set(SyAlignmentVertical.ALIGN_VERTICAL_CENTER);

    this.text = new SyTextView();

    this.align.childAdd(this.text);
    this.margin.childAdd(this.align);
    this.childAdd(this.margin);

    this.margin.setMouseQueryAccepting(false);
    this.align.setMouseQueryAccepting(false);
    this.text.setMouseQueryAccepting(false);
  }

  /**
   * A button with a text label.
   *
   * @param inThemeClassesExtra The extra theme classes, if any
   * @param initialText         The initial text
   */

  public SyButton(
    final List<SyThemeClassNameType> inThemeClassesExtra,
    final String initialText)
  {
    this(inThemeClassesExtra);
    this.setText(initialText);
  }

  /**
   * A button with a text label.
   */

  public SyButton()
  {
    this(List.of());
  }

  /**
   * A button with a text label.
   *
   * @param initialText The initial text
   */

  public SyButton(
    final String initialText)
  {
    this(List.of());
    this.setText(initialText);
  }

  @Override
  protected SyEventConsumed onOtherEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  protected void onClicked()
  {

  }

  @Override
  public AttributeType<String> text()
  {
    return this.text.text();
  }

  @Override
  public void setText(
    final String newText)
  {
    this.text.setText(newText);
  }
}
