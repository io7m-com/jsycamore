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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextID;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.api.text.SyTextLineNumber;
import com.io7m.jsycamore.api.text.SyTextLocationType;
import com.io7m.jsycamore.api.text.SyTextSelection;
import com.io7m.jsycamore.api.text.SyTextSingleLineModelType;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;
import java.util.Objects;

/**
 * The single-line text model implementation.
 */

public final class SyTextSingleLineModel implements SyTextSingleLineModelType
{
  private final AttributeType<SyText> text;
  private final AttributeType<SyFontType> font;
  private SyTextLineMeasuredType textMeasured;
  private SySelectionState selectionState;

  private SyTextSingleLineModel(
    final AttributeType<SyText> inText,
    final SyFontType inFont)
  {
    final var attributes =
      SyComponentAttributes.get();
    this.font =
      attributes.create(inFont);
    this.text =
      Objects.requireNonNull(inText, "inText");

    this.text.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue)) {
        this.regenerate();
      }
    });
    this.font.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue)) {
        this.regenerate();
      }
    });
    this.regenerate();
  }

  /**
   * Create a new single-line text model.
   *
   * @param inText The text attribute
   * @param inFont The font
   *
   * @return A text model
   */

  public static SyTextSingleLineModelType create(
    final AttributeType<SyText> inText,
    final SyFontType inFont)
  {
    return new SyTextSingleLineModel(inText, inFont);
  }

  private static PAreaI<SySpaceParentRelativeType> sanitizedArea(
    final int xMinimum,
    final int xMaximum,
    final int textSizeY)
  {
    if (xMaximum < xMinimum) {
      return PAreaI.of(
        xMaximum,
        xMinimum,
        0,
        textSizeY
      );
    }
    return PAreaI.of(
      xMinimum,
      xMaximum,
      0,
      textSizeY
    );
  }

  private void regenerate()
  {
    final var fontNow =
      this.font.get();
    final var textNow =
      this.text.get();
    final var textWidth =
      fontNow.textWidth(textNow.value());
    final var measuredTexts =
      fontNow.textLayout(
        SyTextID.first(),
        textNow,
        textWidth
      );
    this.textMeasured =
      measuredTexts.get(0);
  }

  @Override
  public void setFont(
    final SyFontType newFont)
  {
    this.font.set(newFont);
  }

  @Override
  public AttributeType<SyText> text()
  {
    return this.text;
  }

  private List<PAreaI<SySpaceParentRelativeType>> buildRegions(
    final SyTextLocationType lowerInclusive,
    final SyTextLocationType upperInclusive)
  {
    if (Objects.equals(lowerInclusive, upperInclusive)) {
      return List.of();
    }

    final var textNow =
      this.text.get();
    final var textDirection =
      textNow.direction();

    return List.of(
      switch (textDirection) {
        case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
          final var xMinimum =
            lowerInclusive.caret().area().minimumX();
          final var xMaximum =
            upperInclusive.caret().area().minimumX();

          yield sanitizedArea(
            xMinimum,
            xMaximum,
            this.textMeasured.height()
          );
        }
        case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
          final var xMinimum =
            upperInclusive.caret().area().minimumX();
          final var xMaximum =
            lowerInclusive.caret().area().minimumX();

          yield sanitizedArea(
            xMinimum,
            xMaximum,
            this.textMeasured.height()
          );
        }
      }
    );
  }

  @Override
  public SyTextSelection selectionStart(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var location = this.inspectAt(position);
    this.selectionState =
      new SySelectionState(location, location, location);

    return new SyTextSelection(
      this.selectionState.lowerInclusive,
      this.selectionState.upperInclusive,
      this.buildRegions(
        this.selectionState.lowerInclusive,
        this.selectionState.upperInclusive
      )
    );
  }

  @Override
  public SyTextSelection selectionContinue(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var currentState = this.selectionState;
    if (currentState == null) {
      throw new IllegalStateException("No selection in progress.");
    }

    this.selectionState = currentState.extend(this.inspectAt(position));
    return new SyTextSelection(
      this.selectionState.lowerInclusive,
      this.selectionState.upperInclusive,
      this.buildRegions(
        this.selectionState.lowerInclusive,
        this.selectionState.upperInclusive
      )
    );
  }

  @Override
  public SyTextSelection selectionFinish(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var currentState = this.selectionState;
    if (currentState == null) {
      throw new IllegalStateException("No selection in progress.");
    }

    this.selectionState = currentState.extend(this.inspectAt(position));
    return new SyTextSelection(
      this.selectionState.lowerInclusive,
      this.selectionState.upperInclusive,
      this.buildRegions(
        this.selectionState.lowerInclusive,
        this.selectionState.upperInclusive
      )
    );
  }

  @Override
  public SyFontType font()
  {
    return this.font.get();
  }

  @Override
  public SyTextLineMeasuredType lineMeasured()
  {
    return this.textMeasured;
  }

  @Override
  public SyTextLocationType inspectAt(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");
    return this.textMeasured.inspectAtParentRelative(
      SyTextLineNumber.first(),
      position
    );
  }

  private record SySelectionState(
    SyTextLocationType pivot,
    SyTextLocationType lowerInclusive,
    SyTextLocationType upperInclusive)
  {
    SySelectionState
    {
      Objects.requireNonNull(pivot, "pivot");
      Objects.requireNonNull(lowerInclusive, "lowerInclusive");
      Objects.requireNonNull(upperInclusive, "upperInclusive");

      Preconditions.checkPreconditionV(
        lowerInclusive.compareTo(pivot) <= 0,
        "Lower bound must be <= pivot"
      );
      Preconditions.checkPreconditionV(
        upperInclusive.compareTo(pivot) >= 0,
        "Upper bound must be >= pivot"
      );
    }

    public SySelectionState extend(
      final SyTextLocationType location)
    {
      if (location.compareTo(this.pivot) <= 0) {
        return new SySelectionState(
          this.pivot,
          location,
          this.pivot
        );
      }

      return new SySelectionState(
        this.pivot,
        this.pivot,
        location
      );
    }
  }
}
