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
import com.io7m.jsycamore.api.components.SyTextMultiLineViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.keyboard.SyKeyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.api.text.SyTextMultiLineModelType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A multi-line text view.
 */

public final class SyTextMultiLineView
  extends SyTextComponentAbstract implements SyTextMultiLineViewType
{
  private final AttributeType<Boolean> textSelectable;
  private SyTextMultiLineModelType textModel;
  private List<SyText> textSectionsDeferred;

  /**
   * A multi-line text view.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The extra theme classes, if any
   */

  public SyTextMultiLineView(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses, () -> true);

    final var components =
      SyComponentAttributes.get();

    this.textSelectable =
      components.create(Boolean.TRUE);
    this.textSectionsDeferred =
      new LinkedList<>();

    /*
     * A text view moving from selectable to not selectable will invalidate
     * its selection.
     */

    this.textSelectable.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue) && !newValue.booleanValue()) {
        this.textSelectionInvalidateIfSelected();
      }
    });
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

    final var textSelections =
      this.screen().textSelectionService();

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

        final var selection =
          model.selectionStart(relative);
        if (selection.isEmpty()) {
          yield EVENT_NOT_CONSUMED;
        }

        textSelections.selectionSet(this, selection.get());
        yield EVENT_CONSUMED;
      }

      case final SyMouseEventOnHeld e -> {
        final var model = this.textModel;
        if (model == null) {
          yield EVENT_NOT_CONSUMED;
        }

        final var relative =
          this.relativePositionOf(e.mousePositionNow());

        final var selection =
          model.selectionContinue(relative);
        if (selection.isEmpty()) {
          yield EVENT_NOT_CONSUMED;
        }

        textSelections.selectionSet(this, selection.get());
        yield EVENT_CONSUMED;
      }

      case final SyMouseEventOnReleased e -> {
        final var model = this.textModel;
        if (model == null) {
          yield EVENT_NOT_CONSUMED;
        }

        final var relative =
          this.relativePositionOf(e.mousePosition());

        final var selection =
          model.selectionFinish(relative);
        if (selection.isEmpty()) {
          yield EVENT_NOT_CONSUMED;
        }

        textSelections.selectionSet(this, selection.get());
        yield EVENT_CONSUMED;
      }
    };
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var oldSize =
      this.size().get();
    final var newSize =
      super.layout(layoutContext, constraints);

    /*
     * The selection for this view is invalidated if it changes size.
     */

    if (!Objects.equals(oldSize, newSize)) {
      this.textSelectionInvalidateIfSelected();
    }

    this.createModelIfRequired(layoutContext);
    this.textModel.setPageWidth(newSize.sizeX());
    return newSize;
  }

  private void createModelIfRequired(
    final SyLayoutContextType layoutContext)
  {
    final var model = this.textModel;
    if (model == null) {
      final var themeComponent =
        layoutContext.themeCurrent()
          .findForComponent(this);

      final var font =
        themeComponent.font(layoutContext, this);
      final var sizeX =
        this.size().get().sizeX();

      this.textModel = SyTextMultiLineModel.create(font, sizeX);
      this.textModel.textSectionsAppend(this.textSectionsDeferred);
      this.textSectionsDeferred.clear();
    }
  }

  @Override
  public SortedMap<Integer, SyTextLineMeasuredType> textsByYOffset()
  {
    final var model = this.textModel;
    if (model == null) {
      return Collections.emptySortedMap();
    } else {
      return model.linesByYCoordinate();
    }
  }

  @Override
  public int minimumSizeYRequired(
    final SyLayoutContextType layoutContext)
  {
    this.createModelIfRequired(layoutContext);
    return this.textModel.minimumSizeYRequired();
  }

  @Override
  public AttributeType<Boolean> textSelectable()
  {
    return this.textSelectable;
  }

  @Override
  public void textSectionsAppend(
    final List<SyText> sections)
  {
    this.textSelectionInvalidateIfSelected();

    final var model = this.textModel;
    if (model == null) {
      this.textSectionsDeferred.addAll(sections);
    } else {
      model.textSectionsAppend(sections);
    }
  }
}
