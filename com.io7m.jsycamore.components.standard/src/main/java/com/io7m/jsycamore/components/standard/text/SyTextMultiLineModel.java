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

import com.io7m.jaffirm.core.Invariants;
import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextID;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.api.text.SyTextLineNumber;
import com.io7m.jsycamore.api.text.SyTextLocationType;
import com.io7m.jsycamore.api.text.SyTextMultiLineModelType;
import com.io7m.jsycamore.api.text.SyTextSelection;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The multi-line text model implementation.
 */

public final class SyTextMultiLineModel implements SyTextMultiLineModelType
{
  private final AttributeType<Integer> pageWidth;
  private final TreeMap<SyTextID, SyText> textSections;
  private final TreeMap<Integer, SyTextLineMeasuredType> textMeasuredLinesByY;
  private final TreeMap<SyTextLineNumber, Integer> textLineNumberToY;
  private final AttributeType<SyFontType> font;
  private final HashMap<SyTextID, List<SyTextLineMeasuredType>> textSectionsToMeasuredLines;
  private final SortedMap<SyTextID, SyText> textSectionsReadable;
  private SySelectionState selectionState;

  private SyTextMultiLineModel(
    final SyFontType inFont,
    final int inPageWidth)
  {
    final var attributes =
      SyComponentAttributes.get();
    this.pageWidth =
      attributes.create(Integer.valueOf(inPageWidth));
    this.font =
      attributes.create(inFont);

    this.textSections =
      new TreeMap<>();
    this.textSectionsReadable =
      Collections.unmodifiableSortedMap(this.textSections);
    this.textSectionsToMeasuredLines =
      new HashMap<>();
    this.textMeasuredLinesByY =
      new TreeMap<>();
    this.textLineNumberToY =
      new TreeMap<>();

    /*
     * Changing the page width or the font will require re-measuring all
     * text sections.
     */

    this.pageWidth.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue)) {
        this.regenerate(SyRegenerateAll.REGENERATE_ALL);
      }
    });
    this.font.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue)) {
        this.regenerate(SyRegenerateAll.REGENERATE_ALL);
      }
    });
  }

  private sealed interface SyRegenerateType
  {

  }

  private enum SyRegenerateAll
    implements SyRegenerateType
  {
    REGENERATE_ALL
  }

  private record SyRegenerateFrom(
    SyTextID textSectionIndex)
    implements SyRegenerateType
  {

  }

  /**
   * Create a new multi-line text model.
   *
   * @param inFont      The font
   * @param inPageWidth The page width
   *
   * @return A text model
   */

  public static SyTextMultiLineModelType create(
    final SyFontType inFont,
    final int inPageWidth)
  {
    return new SyTextMultiLineModel(inFont, inPageWidth);
  }

  private static PAreaI<SySpaceParentRelativeType> sanitizedArea(
    final int xMinimum,
    final int xMaximum,
    final int y,
    final int textSizeY)
  {
    if (xMaximum < xMinimum) {
      return PAreaI.of(
        xMaximum,
        xMinimum,
        y,
        y + textSizeY
      );
    }
    return PAreaI.of(
      xMinimum,
      xMaximum,
      y,
      y + textSizeY
    );
  }

  private void regenerate(
    final SyRegenerateType regenerate)
  {
    final var fontNow =
      this.font.get();
    final var wrapNow =
      this.pageWidth.get().intValue();

    switch (regenerate) {
      case final SyRegenerateAll all -> {
        this.regenerateEverything(fontNow, wrapNow);
      }
      case final SyRegenerateFrom from -> {
        this.regenerateFrom(from, fontNow, wrapNow);
      }
    }
  }

  private void regenerateFrom(
    final SyRegenerateFrom from,
    final SyFontType fontNow,
    final int wrapNow)
  {
    if (Objects.equals(from.textSectionIndex, SyTextID.first())) {
      this.regenerateEverything(fontNow, wrapNow);
      return;
    }

    final var textSectionsToRedo =
      this.textSections.tailMap(
        from.textSectionIndex,
        true
      );

    /*
     * Remove all the data associated with the text sections that will
     * be re-measured.
     */

    for (final var entry : textSectionsToRedo.entrySet()) {
      final var textID =
        entry.getKey();
      final var lines =
        this.textSectionsToMeasuredLines.get(textID);

      if (lines == null) {
        continue;
      }

      for (final var line : lines) {
        final var lineNumber = line.lineNumber();
        final var y = this.textLineNumberToY.get(lineNumber);
        if (y == null) {
          this.textMeasuredLinesByY.remove(y);
        }
        this.textLineNumberToY.remove(lineNumber);
      }
    }

    /*
     * Determine what the new starting line number will be.
     */

    SyTextLineNumber startingLineNumber;
    try {
      startingLineNumber =
        this.textLineNumberToY.lastKey();
    } catch (final NoSuchElementException e) {
      startingLineNumber =
        new SyTextLineNumber(0);
    }

    final var newMeasured =
      fontNow.textLayoutMultiple(
        textSectionsToRedo,
        startingLineNumber,
        wrapNow
      );

    var y = this.highestYOffset();
    for (final var measured : newMeasured) {
      final var boxY = Integer.valueOf(y);
      this.recordMeasuredLineForText(measured);
      this.textMeasuredLinesByY.put(boxY, measured);
      this.textLineNumberToY.put(measured.lineNumber(), boxY);
      final var textSize = measured.textBounds();
      y += textSize.sizeY();
    }
  }

  private void regenerateEverything(
    final SyFontType fontNow,
    final int wrapNow)
  {
    this.textMeasuredLinesByY.clear();
    this.textSectionsToMeasuredLines.clear();
    this.textLineNumberToY.clear();

    final var newMeasured =
      fontNow.textLayoutMultiple(
        this.textSections,
        SyTextLineNumber.first(),
        wrapNow
      );

    var y = 0;
    for (final var measured : newMeasured) {
      final var boxY = Integer.valueOf(y);
      this.recordMeasuredLineForText(measured);
      this.textMeasuredLinesByY.put(boxY, measured);
      this.textLineNumberToY.put(measured.lineNumber(), boxY);
      final var textSize = measured.textBounds();
      y += textSize.sizeY();
    }
  }

  /**
   * Record the relationship between the original text and a
   * measured line that it produced.
   */

  private void recordMeasuredLineForText(
    final SyTextLineMeasuredType measured)
  {
    var linesForText =
      this.textSectionsToMeasuredLines.get(measured.textOriginal());
    if (linesForText == null) {
      linesForText = new LinkedList<>();
    }
    linesForText.add(measured);
    this.textSectionsToMeasuredLines.put(
      measured.textOriginal(),
      linesForText
    );
  }

  @Override
  public void setFont(
    final SyFontType newFont)
  {
    this.font.set(newFont);
  }

  @Override
  public void setPageWidth(
    final int newWidth)
  {
    this.pageWidth.set(Integer.valueOf(Math.max(1, newWidth)));
  }

  @Override
  public void textSectionsAppend(
    final List<SyText> sections)
  {
    if (sections.isEmpty()) {
      return;
    }

    SyTextID nextID;
    try {
      nextID = this.textSections.lastKey().next();
    } catch (final NoSuchElementException e) {
      nextID = SyTextID.first();
    }


    final SyTextID regenerateFrom = nextID;
    for (final var section : sections) {
      this.textSections.put(nextID, section);
      nextID = nextID.next();
    }

    this.regenerate(new SyRegenerateFrom(regenerateFrom));
  }

  private List<PAreaI<SySpaceParentRelativeType>> buildRegions(
    final SyTextLocationType pivot,
    final SyTextLocationType lowerInclusive,
    final SyTextLocationType upperInclusive)
  {
    if (Objects.equals(lowerInclusive, upperInclusive)) {
      return List.of();
    }

    final var currentPageWidth =
      this.pageWidth.get().intValue();
    final var isSelectingForwards =
      pivot.compareTo(upperInclusive) < 0;

    if (isSelectingForwards) {
      return this.buildRegionsForSelectionForward(
        lowerInclusive,
        upperInclusive,
        currentPageWidth
      );
    }

    return this.buildRegionsForSelectionBackwards(
      lowerInclusive,
      upperInclusive,
      currentPageWidth
    );
  }

  private List<PAreaI<SySpaceParentRelativeType>>
  buildRegionsForSelectionBackwards(
    final SyTextLocationType lowerInclusive,
    final SyTextLocationType upperInclusive,
    final int currentPageWidth)
  {
    final LinkedList<PAreaI<SySpaceParentRelativeType>> results =
      new LinkedList<>();

    final var lowerLine =
      lowerInclusive.lineNumber();
    final var upperLine =
      upperInclusive.lineNumber();

    for (var lineNumber = lowerLine;
         lineNumber.compareTo(upperLine) <= 0;
         lineNumber = lineNumber.next()) {

      final var line =
        this.textForLine(lineNumber);
      final var textDirection =
        line.textAsWrapped().direction();
      final var y =
        this.textLineNumberToY.get(lineNumber)
          .intValue();
      final var textSizeX =
        line.textBounds().sizeX();
      final var textSizeY =
        line.textBounds().sizeY();

      /*
       * Lines that are not the first or last line are fully selected.
       */

      if (lineNumber.compareTo(lowerLine) > 0 && lineNumber.compareTo(upperLine) < 0) {
        results.add(
          switch (textDirection) {

            /*
             * Left-aligned, full text width.
             */

            case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
              yield PAreasI.create(
                0,
                y,
                textSizeX,
                textSizeY
              );
            }

            /*
             * Right-aligned, full text width.
             */

            case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
              final var alignmentDelta =
                currentPageWidth - textSizeX;

              yield sanitizedArea(
                alignmentDelta,
                currentPageWidth,
                y,
                textSizeY);
            }
          }
        );
        continue;
      }

      if (lineNumber.equals(upperLine)) {
        results.add(
          switch (textDirection) {

            /*
             * For backwards selection, in left-to-right text, the last line
             * selection extends from the caret of the upper location
             * (which will be rightmost), and may either extend to the start
             * of the line (which will be leftmost), or to the caret of the
             * lower location if it happens to be on the same line.
             */

            case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
              final var xMaximum =
                upperInclusive.caret().area().minimumX();

              final int xMinimum;
              if (lineNumber.equals(lowerLine)) {
                xMinimum = lowerInclusive.caret().area().minimumX();
              } else {
                xMinimum = 0;
              }

              yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
            }

            /*
             * For backwards selection, in right-to-left text, the last line
             * selection extends from the caret of the upper location (which
             * will be leftmost), and may either extend to the start of the line
             * (which will be rightmost), or to the caret of the lower location
             * if it happens to be on the same line.
             *
             * Note that we have to take into account the alignment delta, as
             * text will be rendered right-aligned.
             */

            case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
              final var alignmentDelta =
                currentPageWidth - textSizeX;
              final int xMinimum =
                upperInclusive.caret().area().minimumX() + alignmentDelta;

              final int xMaximum;
              if (lineNumber.equals(lowerLine)) {
                xMaximum = lowerInclusive.caret().area().minimumX() + alignmentDelta;
              } else {
                xMaximum = currentPageWidth;
              }

              yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
            }
          }
        );
        continue;
      }

      Invariants.checkInvariantV(
        lineNumber.equals(lowerLine),
        "Line number must equal lower bound."
      );
      Invariants.checkInvariantV(
        !lineNumber.equals(upperLine),
        "Line number must not equal upper bound."
      );

      results.add(
        switch (textDirection) {

          /*
           * For backwards selection in left-to-right text, the first line
           * selection extends from the end of the line (which will be
           * rightmost), to the caret of the lower location.
           *
           * Note that the caret of the upper location is _not_ involved,
           * because if the lower and upper location are on the same line,
           * then this is already handled in the code for the last line
           * selection above.
           */

          case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
            final var xMinimum =
              lowerInclusive.caret().area().minimumX();
            final var xMaximum =
              textSizeX;

            yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
          }

          /*
           * For backwards selection in right-to-left text, the selection
           * extends from the end of the line (which will be leftmost), to
           * the caret of the lower position.
           *
           * Note that the caret of the upper location is _not_ involved,
           * because if the lower and upper location are on the same line,
           * then this is already handled in the code for the last line
           * selection above.
           *
           * Note that we have to take into account the alignment delta, as
           * text will be rendered right-aligned.
           */

          case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
            final var alignmentDelta =
              currentPageWidth - textSizeX;
            final var xMinimum =
              currentPageWidth - textSizeX;
            final var xMaximum =
              lowerInclusive.caret().area().minimumX() + alignmentDelta;

            yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
          }
        }
      );
    }

    return List.copyOf(results);
  }

  private List<PAreaI<SySpaceParentRelativeType>>
  buildRegionsForSelectionForward(
    final SyTextLocationType lowerInclusive,
    final SyTextLocationType upperInclusive,
    final int currentPageWidth)
  {
    final LinkedList<PAreaI<SySpaceParentRelativeType>> results =
      new LinkedList<>();

    final var lowerLine =
      lowerInclusive.lineNumber();
    final var upperLine =
      upperInclusive.lineNumber();

    for (var lineNumber = lowerLine;
         lineNumber.compareTo(upperLine) <= 0;
         lineNumber = lineNumber.next()) {

      final var line =
        this.textForLine(lineNumber);
      final var textDirection =
        line.textAsWrapped().direction();
      final var y =
        this.textLineNumberToY.get(lineNumber)
          .intValue();
      final var textSizeX =
        line.textBounds().sizeX();
      final var textSizeY =
        line.textBounds().sizeY();

      /*
       * Lines that are not the first or last line are fully selected.
       */

      if (lineNumber.compareTo(lowerLine) > 0 && lineNumber.compareTo(upperLine) < 0) {
        results.add(
          switch (textDirection) {

            /*
             * Left-aligned, full text width.
             */

            case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
              yield PAreasI.create(
                0,
                y,
                textSizeX,
                textSizeY
              );
            }

            /*
             * Right-aligned, full text width.
             */

            case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
              final var alignmentDelta =
                currentPageWidth - textSizeX;

              yield sanitizedArea(
                alignmentDelta,
                currentPageWidth,
                y,
                textSizeY);
            }
          }
        );
        continue;
      }

      if (lineNumber.equals(lowerLine)) {
        results.add(
          switch (textDirection) {

            /*
             * For forward selection, in left-to-right text, the first line
             * selection extends from the caret of the lower location (which
             * will be leftmost), and may either extend to the end of the line
             * (which will be rightmost), or to the caret of the upper location
             * if it happens to be on the same line.
             */

            case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
              final var xMinimum =
                lowerInclusive.caret().area().minimumX();

              final int xMaximum;
              if (lineNumber.equals(upperLine)) {
                xMaximum = upperInclusive.caret().area().minimumX();
              } else {
                xMaximum = textSizeX;
              }

              yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
            }

            /*
             * For forward selection, in right-to-left text, the first line
             * selection extends from the caret of the lower location (which
             * will be rightmost), and may extend either to the end of the
             * line (which will be leftmost), or to the caret of the upper
             * location if it happens to be on the same line.
             *
             * Note that we have to take into account the alignment delta, as
             * text will be rendered right-aligned.
             */

            case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
              final var alignmentDelta =
                currentPageWidth - textSizeX;
              final int xMaximum =
                lowerInclusive.caret().area().minimumX() + alignmentDelta;

              final int xMinimum;
              if (lineNumber.equals(upperLine)) {
                xMinimum = upperInclusive.caret().area().minimumX() + alignmentDelta;
              } else {
                xMinimum = alignmentDelta;
              }

              yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
            }
          }
        );
        continue;
      }

      Invariants.checkInvariantV(
        lineNumber.equals(upperLine),
        "Line number must equal upper bound."
      );
      Invariants.checkInvariantV(
        !lineNumber.equals(lowerLine),
        "Line number must not equal lower bound."
      );

      results.add(
        switch (textDirection) {

          /*
           * For forward selection, in left-to-right text, the last line
           * selection extends from the start of the line (which is leftmost)
           * to the caret of the upper location (rightmost).
           *
           * Note that the caret of the lower location is _not_ involved,
           * because if the lower and upper location are on the same line,
           * then this is already handled in the code for the first line
           * selection above.
           */

          case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
            final var xMaximum =
              upperInclusive.caret().area().minimumX();
            final int xMinimum =
              0;

            yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
          }

          /*
           * For forward selection, in right-to-left text, the last line
           * selection extends from the start of the line (which is rightmost)
           * to the caret of the upper selection (leftmost).
           *
           * Note that the caret of the lower location is _not_ involved,
           * because if the lower and upper location are on the same line,
           * then this is already handled in the code for the first line
           * selection above.
           *
           * Note that we have to take into account the alignment delta, as
           * text will be rendered right-aligned.
           */

          case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
            final var delta =
              currentPageWidth - textSizeX;
            final var xMaximum =
              currentPageWidth;
            final var xMinimum =
              upperInclusive.caret().area().minimumX() + delta;

            yield sanitizedArea(xMinimum, xMaximum, y, textSizeY);
          }
        }
      );
    }

    return List.copyOf(results);
  }

  private SyTextLineMeasuredType textForLine(
    final SyTextLineNumber lineNumber)
  {
    return this.textMeasuredLinesByY.get(
      this.textLineNumberToY.get(lineNumber)
    );
  }

  @Override
  public Optional<SyTextSelection> selectionStart(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var locationOpt =
      this.inspectAt(position);

    if (locationOpt.isEmpty()) {
      this.selectionState = null;
      return Optional.empty();
    }

    final var location =
      locationOpt.get();
    this.selectionState =
      new SySelectionState(location, location, location);

    return Optional.of(new SyTextSelection(
      this.selectionState.lowerInclusive,
      this.selectionState.upperInclusive,
      this.buildRegions(
        this.selectionState.pivot,
        this.selectionState.lowerInclusive,
        this.selectionState.upperInclusive
      )
    ));
  }

  @Override
  public Optional<SyTextSelection> selectionContinue(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var currentState = this.selectionState;
    if (currentState == null) {
      return Optional.empty();
    }

    final var location = this.inspectAt(position);
    if (location.isEmpty()) {
      this.selectionState = null;
      return Optional.empty();
    }

    this.selectionState = currentState.extend(location.get());
    return Optional.of(new SyTextSelection(
      this.selectionState.lowerInclusive,
      this.selectionState.upperInclusive,
      this.buildRegions(
        this.selectionState.pivot,
        this.selectionState.lowerInclusive,
        this.selectionState.upperInclusive
      )
    ));
  }

  @Override
  public Optional<SyTextSelection> selectionFinish(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var currentState = this.selectionState;
    if (currentState == null) {
      return Optional.empty();
    }

    final var location = this.inspectAt(position);
    if (location.isEmpty()) {
      this.selectionState = null;
      return Optional.empty();
    }

    this.selectionState = currentState.extend(location.get());
    return Optional.of(new SyTextSelection(
      this.selectionState.lowerInclusive,
      this.selectionState.upperInclusive,
      this.buildRegions(
        this.selectionState.pivot,
        this.selectionState.lowerInclusive,
        this.selectionState.upperInclusive
      )
    ));
  }

  private int highestYOffset()
  {
    final var entry =
      this.textMeasuredLinesByY.lastEntry();

    if (entry == null) {
      return 0;
    }

    return entry.getKey().intValue() + entry.getValue().textBounds().sizeY();
  }

  @Override
  public SyFontType font()
  {
    return this.font.get();
  }

  @Override
  public int pageWidth()
  {
    return this.pageWidth.get().intValue();
  }

  @Override
  public SortedMap<SyTextID, SyText> textSections()
  {
    return this.textSectionsReadable;
  }

  @Override
  public Optional<SyText> textSectionContainingLine(
    final SyTextLineNumber lineNumber)
  {
    Objects.requireNonNull(lineNumber, "lineNumber");

    final var y = this.textLineNumberToY.get(lineNumber);
    if (y == null) {
      return Optional.empty();
    }
    final var line = this.textMeasuredLinesByY.get(y);
    if (line == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(this.textSections.get(line.textOriginal()));
  }

  @Override
  public SortedMap<Integer, SyTextLineMeasuredType> linesByYCoordinate()
  {
    return new TreeMap<>(this.textMeasuredLinesByY);
  }

  @Override
  public Optional<SyTextLocationType> inspectAt(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var y =
      Math.max(0, position.y());
    final var entry =
      this.textMeasuredLinesByY.floorEntry(Integer.valueOf(y));

    if (entry == null) {
      return Optional.empty();
    }

    return Optional.of(entry.getValue().inspectAtParentRelative(position));
  }

  @Override
  public int minimumSizeYRequired()
  {
    final var fontNow =
      this.font.get();

    var sum = fontNow.textHeight();
    for (final var value : this.textMeasuredLinesByY.values()) {
      sum += value.pageLineBounds().sizeY();
    }
    return sum;
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
