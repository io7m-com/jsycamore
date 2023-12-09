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
import com.io7m.jsycamore.api.text.SyTextLinePositioned;
import com.io7m.jsycamore.api.text.SyTextLocationType;
import com.io7m.jsycamore.api.text.SyTextMultiLineModelType;
import com.io7m.jsycamore.api.text.SyTextSelection;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Collections;
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
  private final TreeMap<SyTextID, SyTextFormatted> textSectionsFormatted;
  private final TreeMap<SyTextLineNumber, SyTextFormatted> textSectionsFormattedByLine;
  private final TreeMap<Integer, SyTextFormatted> textSectionsFormattedByY;
  private final AttributeType<SyFontType> font;
  private final SortedMap<SyTextID, SyText> textSectionsReadable;
  private SySelectionState selectionState;

  private static final class SyTextFormatted
  {
    private final SyTextID textID;
    private SyTextLineNumber lineNumber;
    private final List<SyTextLineMeasuredType> lines;
    private int yOffset;

    SyTextFormatted(
      final SyTextID inTextID,
      final SyTextLineNumber inLineNumber,
      final List<SyTextLineMeasuredType> inLines,
      final int inYOffset)
    {
      this.textID =
        Objects.requireNonNull(inTextID, "textID");
      this.lineNumber =
        Objects.requireNonNull(inLineNumber, "lineNumber");
      this.lines =
        Objects.requireNonNull(inLines, "inLines");
      this.yOffset =
        inYOffset;
    }

    public int height()
    {
      return this.lines.stream()
        .mapToInt(SyTextLineMeasuredType::height)
        .sum();
    }

    public Optional<SyTextLinePositioned> lineAt(
      final SyTextLineNumber targetLineNumber)
    {
      var y = this.yOffset;
      final var lineOffset = targetLineNumber.value() - this.lineNumber.value();
      for (int index = 0; index <= lineOffset; ++index) {
        final var line = this.lines.get(index);
        if (index == lineOffset) {
          return Optional.of(
            new SyTextLinePositioned(
              y,
              this.lineNumber.adjust(index),
              this.lines.get(index)
            )
          );
        }
        y += line.height();
      }
      return Optional.empty();
    }
  }

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
    this.textSectionsFormatted =
      new TreeMap<>();
    this.textSectionsFormattedByLine =
      new TreeMap<>();
    this.textSectionsFormattedByY =
      new TreeMap<>();

    /*
     * Changing the page width or the font will require re-measuring all
     * text sections.
     */

    this.pageWidth.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue)) {
        this.edit(SyEditOpRegenerateAll.SY_EDIT_OP_REGENERATE);
      }
    });
    this.font.subscribe((oldValue, newValue) -> {
      if (!Objects.equals(oldValue, newValue)) {
        this.edit(SyEditOpRegenerateAll.SY_EDIT_OP_REGENERATE);
      }
    });
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

  private void edit(
    final SyEditOpType edit)
  {
    switch (edit) {
      case final SyEditOpRegenerateAll op -> {
        this.editRegenerateAll();
      }
      case final SyEditOpAppend op -> {
        this.editAppend(op);
      }
      case final SyEditOpReplace op -> {
        this.editReplace(op);
      }
    }
  }

  private void editReplace(
    final SyEditOpReplace op)
  {
    /*
     * Replacing a section means removing the lines associated with the
     * existing section, and then renumbering all lines that followed those
     * removed lines.
     */

    if (!this.textSections.containsKey(op.textID)) {
      throw new NoSuchElementException(
        "No text present with ID %s".formatted(op.textID)
      );
    }

    final var fontNow =
      this.font.get();
    final var wrapNow =
      this.pageWidth.get().intValue();

    final var newLines =
      fontNow.textLayout(op.textID, op.text, wrapNow);
    final var existingFormatted =
      this.textSectionsFormatted.get(op.textID);

    final var newFormatted =
      new SyTextFormatted(
        existingFormatted.textID,
        existingFormatted.lineNumber,
        newLines,
        existingFormatted.yOffset
      );

    /*
     * The number of lines that all the other lines need to be shifted
     * is the difference between the number of new lines for this text,
     * and the number of old lines. It's perfectly fine for the delta
     * to be negative (we might be removing lines).
     */

    final var lineDelta =
      newLines.size() - existingFormatted.lines.size();
    final var lowerLineNumber =
      existingFormatted.lineNumber;
    final var lowerY =
      newFormatted.yOffset + newFormatted.height();

    this.shiftLines(lowerLineNumber, lineDelta, lowerY);

    this.textSections.replace(op.textID, op.text);
    this.textSectionsFormatted.put(existingFormatted.textID, newFormatted);
    this.textSectionsFormattedByY.put(newFormatted.yOffset, newFormatted);
    this.textSectionsFormattedByLine.put(newFormatted.lineNumber, newFormatted);
  }

  /**
   * Shift all lines greater than the given lower line number by the given
   * line delta. All Y values will be recalculated assuming that the new
   * starting Y value is {@code yStart}.
   */

  private void shiftLines(
    final SyTextLineNumber lowerLineNumber,
    final int delta,
    final int yStart)
  {
    final var above =
      List.copyOf(
        this.textSectionsFormattedByLine.tailMap(lowerLineNumber, false)
          .values()
      );

    for (final var aboveFormatted : above) {
      this.textSectionsFormattedByLine.remove(
        aboveFormatted.lineNumber
      );
      this.textSectionsFormattedByY.remove(
        Integer.valueOf(aboveFormatted.yOffset)
      );
      aboveFormatted.lineNumber = aboveFormatted.lineNumber.adjust(delta);
    }

    var y = yStart;
    for (final var aboveFormatted : above) {
      this.textSectionsFormattedByLine.put(
        aboveFormatted.lineNumber,
        aboveFormatted
      );
      aboveFormatted.yOffset = y;
      this.textSectionsFormattedByY.put(
        Integer.valueOf(aboveFormatted.yOffset),
        aboveFormatted
      );
      y += aboveFormatted.height();
    }
  }

  private void editAppend(
    final SyEditOpAppend op)
  {
    /*
     * Appending new sections means creating new measured lines for the
     * appended text sections.
     */

    if (op.texts.isEmpty()) {
      return;
    }

    SyTextID nextID;
    try {
      nextID = this.textSections.lastKey().next();
    } catch (final NoSuchElementException e) {
      nextID = SyTextID.first();
    }

    final SyTextID regenerateFrom = nextID;
    for (final var section : op.texts) {
      this.textSections.put(nextID, section);
      nextID = nextID.next();
    }

    final var fontNow =
      this.font.get();
    final var wrapNow =
      this.pageWidth.get().intValue();

    final var newSections =
      this.textSections.tailMap(regenerateFrom, true);

    var lineNumber = this.nextFreeLineNumber();
    var y = this.highestYOffset();

    for (final var entry : newSections.entrySet()) {
      final var textId =
        entry.getKey();
      final var text =
        entry.getValue();
      final var lines =
        fontNow.textLayout(textId, text, wrapNow);

      final var textFormatted =
        new SyTextFormatted(
          textId,
          lineNumber,
          lines,
          y
        );

      this.textSectionsFormatted.put(textId, textFormatted);
      this.textSectionsFormattedByLine.put(lineNumber, textFormatted);
      this.textSectionsFormattedByY.put(Integer.valueOf(y), textFormatted);

      for (final var line : lines) {
        lineNumber = lineNumber.next();
        y += line.height();
      }
    }
  }

  private SyTextLineNumber nextFreeLineNumber()
  {
    final var lastEntry =
      this.textSectionsFormatted.lastEntry();

    if (lastEntry == null) {
      return SyTextLineNumber.first();
    }

    final var formatted = lastEntry.getValue();
    return formatted.lineNumber.adjust(formatted.lines.size());
  }

  private void editRegenerateAll()
  {
    this.textSectionsFormatted.clear();
    this.textSectionsFormattedByLine.clear();
    this.textSectionsFormattedByY.clear();

    final var fontNow =
      this.font.get();
    final var wrapNow =
      this.pageWidth.get().intValue();

    var lineNumber = SyTextLineNumber.first();
    var y = 0;

    for (final var entry : this.textSections.entrySet()) {
      final var textId =
        entry.getKey();
      final var text =
        entry.getValue();
      final var lines =
        fontNow.textLayout(textId, text, wrapNow);

      final var textFormatted =
        new SyTextFormatted(
          textId,
          lineNumber,
          lines,
          y
        );

      this.textSectionsFormatted.put(textId, textFormatted);
      this.textSectionsFormattedByLine.put(lineNumber, textFormatted);
      this.textSectionsFormattedByY.put(Integer.valueOf(y), textFormatted);

      for (final var line : lines) {
        lineNumber = lineNumber.next();
        y += line.height();
      }
    }
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
  public void textSectionReplace(
    final SyTextID textID,
    final SyText text)
  {
    Objects.requireNonNull(textID, "textID");
    Objects.requireNonNull(text, "text");

    this.edit(new SyEditOpReplace(textID, text));
  }

  @Override
  public void textSectionsAppend(
    final List<SyText> sections)
  {
    this.edit(new SyEditOpAppend(sections));
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
        this.textYForLine(lineNumber);

      final var textSizeX =
        line.textWidth();
      final var textSizeY =
        line.height();

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

  private int textYForLine(
    final SyTextLineNumber lineNumber)
  {
    final var entry =
      this.textSectionsFormattedByLine.floorEntry(lineNumber);
    final var formatted =
      entry.getValue();
    final var lineOffset =
      lineNumber.value() - formatted.lineNumber.value();

    var y = formatted.yOffset;
    for (int index = 0; index < lineOffset; ++index) {
      y += formatted.lines.get(index).height();
    }
    return y;
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
        this.textYForLine(lineNumber);
      final var textSizeX =
        line.textWidth();
      final var textSizeY =
        line.height();

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
    final var entry =
      this.textSectionsFormattedByLine.floorEntry(lineNumber);
    final var formatted =
      entry.getValue();
    final var lineOffset =
      lineNumber.value() - formatted.lineNumber.value();
    return formatted.lines.get(lineOffset);
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

  @Override
  public int lineCount()
  {
    return this.textSectionsFormatted.values()
      .stream()
      .mapToInt(f -> f.lines.size())
      .sum();
  }

  @Override
  public Optional<SyTextLinePositioned> lineAt(
    final SyTextLineNumber line)
  {
    final var floor =
      this.textSectionsFormattedByLine.floorEntry(line);

    if (floor == null) {
      return Optional.empty();
    }

    return floor.getValue().lineAt(line);
  }

  private int highestYOffset()
  {
    final var lastText =
      this.textSectionsFormatted.lastEntry();
    if (lastText == null) {
      return 0;
    }

    final var formatted = lastText.getValue();
    return formatted.yOffset + formatted.height();
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
  public Optional<SyTextLocationType> inspectAt(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    final var y =
      Math.max(0, position.y());

    final var entry =
      this.textSectionsFormattedByY.floorEntry(Integer.valueOf(y));

    if (entry == null) {
      return Optional.empty();
    }

    final var formatted = entry.getValue();
    final var yLocal = y - formatted.yOffset;
    var yLine = 0;
    var lineNumber = formatted.lineNumber;
    for (final var line : formatted.lines) {
      final var yNext = yLine + line.height();
      if (yLocal >= yLine && yLocal < yNext) {
        return Optional.of(line.inspectAtParentRelative(lineNumber, position));
      }
      lineNumber = lineNumber.next();
      yLine += line.height();
    }

    return Optional.empty();
  }

  @Override
  public int minimumSizeYRequired()
  {
    final var lastEntry =
      this.textSectionsFormatted.lastEntry();

    if (lastEntry == null) {
      return 0;
    }

    final var formatted = lastEntry.getValue();
    return formatted.yOffset + formatted.height();
  }

  private enum SyEditOpRegenerateAll
    implements SyEditOpType
  {
    SY_EDIT_OP_REGENERATE
  }

  private sealed interface SyEditOpType
  {

  }

  private record SyEditOpAppend(
    List<SyText> texts)
    implements SyEditOpType
  {

  }

  private record SyEditOpReplace(
    SyTextID textID,
    SyText text)
    implements SyEditOpType
  {

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
