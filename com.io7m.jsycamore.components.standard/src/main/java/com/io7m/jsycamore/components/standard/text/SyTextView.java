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

package com.io7m.jsycamore.components.standard.text;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyTextViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.keyboard.SyKeyEventType;
import com.io7m.jsycamore.api.keyboard.SyKeyboardFocusBehavior;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextSingleLineModelType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A simple text view.
 */

public final class SyTextView
  extends SyComponentAbstract implements SyTextViewType
{
  private SyTextSingleLineModelType textModel;
  private AttributeType<SyText> text;
  private AttributeType<Boolean> textSelectable;
  private SyFontType font;

  private SyTextView(
    final SyScreenType inScreen,
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(
      inScreen,
      inThemeClassesExtra,
      SyKeyboardFocusBehavior.IGNORES_FOCUS_AND_STOPS_TRAVERSAL
    );

    final var attributes =
      SyComponentAttributes.get();
    this.textSelectable =
      attributes.create(Boolean.FALSE);
    this.text =
      attributes.create(SyText.empty());
  }

  /**
   * Create a text view.
   *
   * @param inScreen     The screen that owns the component
   * @param themeClasses The extra theme classes in priority order
   * @param text         The text
   *
   * @return A text view
   */

  public static SyTextViewType textView(
    final SyScreenType inScreen,
    final List<SyThemeClassNameType> themeClasses,
    final SyText text)
  {
    Objects.requireNonNull(text, "text");

    final var textView = new SyTextView(inScreen, themeClasses);
    textView.setText(text);
    return textView;
  }

  /**
   * Create a text view.
   *
   * @param inScreen     The screen that owns the component
   * @param themeClasses The extra theme classes in priority order
   *
   * @return A text view
   */

  public static SyTextViewType textView(
    final SyScreenType inScreen,
    final List<SyThemeClassNameType> themeClasses)
  {
    return new SyTextView(inScreen, themeClasses);
  }

  /**
   * Create a text view.
   *
   * @param inScreen The screen that owns the component
   *
   * @return A text view
   */

  public static SyTextViewType textView(
    final SyScreenType inScreen)
  {
    return new SyTextView(inScreen, List.of());
  }

  /**
   * Create a text view.
   *
   * @param inScreen The screen that owns the component
   * @param text     The text
   *
   * @return A text view
   */

  public static SyTextViewType textView(
    final SyScreenType inScreen,
    final SyText text)
  {
    return textView(inScreen, List.of(), text);
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return switch (event) {
      case final SyMouseEventType e -> this.onMouseEvent(e);
      case final SyKeyEventType e -> EVENT_NOT_CONSUMED;
    };
  }

  private SyEventConsumed onMouseEvent(
    final SyMouseEventType event)
  {
    /*
     * The text may be configured to be selectable, or not.
     */

    if (!this.isTextSelectable()) {
      return EVENT_NOT_CONSUMED;
    }

    /*
     * There may or may not be a selection service available.
     */

    final var textSelections =
      this.screen()
        .textSelectionService();

    return switch (event) {
      case final SyMouseEventOnNoLongerOver e -> {
        yield EVENT_NOT_CONSUMED;
      }

      case final SyMouseEventOnOver e -> {
        yield EVENT_NOT_CONSUMED;
      }

      case final SyMouseEventOnPressed e -> {
        final var model = this.textModel;
        if (model == null) {
          yield EVENT_NOT_CONSUMED;
        }

        final var relative =
          this.relativePositionOf(e.mousePosition());

        textSelections.selectionSet(this, model.selectionStart(relative));
        yield EVENT_CONSUMED;
      }

      case final SyMouseEventOnHeld e -> {
        final var model = this.textModel;
        if (model == null) {
          yield EVENT_NOT_CONSUMED;
        }

        final var relative =
          this.relativePositionOf(e.mousePositionNow());

        textSelections.selectionSet(this, model.selectionContinue(relative));
        yield EVENT_CONSUMED;
      }

      case final SyMouseEventOnReleased e -> {
        final var model = this.textModel;
        if (model == null) {
          yield EVENT_NOT_CONSUMED;
        }

        final var relative =
          this.relativePositionOf(e.mousePosition());

        textSelections.selectionSet(this, model.selectionFinish(relative));
        yield EVENT_CONSUMED;
      }
    };
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    this.font = this.findFont(layoutContext);
    this.modelGetOrCreate();

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

  private void modelGetOrCreate()
  {
    final var model = this.textModel;
    if (model == null) {
      this.textModel = SyTextSingleLineModel.create(this.text, this.font);
    }
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> minimumSizeRequired(
    final SyLayoutContextType layoutContext)
  {
    this.font = this.findFont(layoutContext);
    this.modelGetOrCreate();

    final var textLine = this.textModel.lineMeasured();
    final var sizeX = textLine.textWidth();
    final var sizeY = textLine.height();
    return PAreaSizeI.of(sizeX, sizeY);
  }

  private SyFontType findFont(
    final SyLayoutContextType layoutContext)
  {
    return layoutContext.themeCurrent()
      .findForComponent(this)
      .font(layoutContext, this);
  }

  @Override
  public AttributeType<Boolean> textSelectable()
  {
    return this.textSelectable;
  }

  @Override
  public AttributeType<SyText> text()
  {
    return this.text;
  }
}
