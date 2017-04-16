/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jsycamore.core.themes;

import com.io7m.jfunctional.Pair;
import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.junreachable.UnimplementedCodeException;
import com.io7m.junreachable.UnreachableCodeException;
import org.valid4j.Assertive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Functions for handling title bars.
 */

public final class SyThemeTitleBars
{
  private SyThemeTitleBars()
  {
    throw new UnimplementedCodeException();
  }

  /**
   * Calculate the order in which any title bar elements will appear. The
   * returned list will contain the order in which title bar elements appear.
   * There will be entries for all possible element types, even if that element
   * would not actually appear in the title bar.
   *
   * @param title_theme The title bar theme
   *
   * @return A list indicating the order of elements
   */

  public static List<SyThemeTitleBarElement> elementsOrder(
    final SyThemeWindowTitleBarType title_theme)
  {
    NullCheck.notNull(title_theme, "Theme");

    final List<SyThemeTitleBarElement> elements =
      new ArrayList<>(SyThemeTitleBarElement.values().length);
    Collections.addAll(elements, SyThemeTitleBarElement.values());
    Collections.sort(elements, title_theme.elementOrder());
    return elements;
  }

  /**
   * Determine how much horizontal space is needed to contain all of the
   * non-text elements in the window, including padding.
   */

  private static int nonTextWidthRequired(
    final SyThemeWindowTitleBarType title_theme,
    final List<SyThemeTitleBarElement> elements,
    final boolean is_closeable,
    final boolean is_maximizable)
  {
    NullCheck.notNull(title_theme, "Theme");
    NullCheck.notNull(elements, "Elements");

    final SyThemePaddingType button_padding = title_theme.buttonPadding();

    int non_text_width = 0;
    final int button_pad_left = button_padding.paddingLeft();
    final int button_pad_right = button_padding.paddingRight();
    for (int index = 0; index < elements.size(); ++index) {
      final SyThemeTitleBarElement element = elements.get(index);
      switch (element) {
        case ELEMENT_CLOSE_BUTTON: {
          if (is_closeable) {
            non_text_width = Math.addExact(non_text_width, button_pad_left);
            non_text_width = Math.addExact(
              non_text_width,
              title_theme.buttonWidth());
            non_text_width = Math.addExact(non_text_width, button_pad_right);
          }
          break;
        }
        case ELEMENT_MAXIMIZE_BUTTON: {
          if (is_maximizable) {
            non_text_width = Math.addExact(non_text_width, button_pad_left);
            non_text_width = Math.addExact(
              non_text_width,
              title_theme.buttonWidth());
            non_text_width = Math.addExact(non_text_width, button_pad_right);
          }
          break;
        }
        case ELEMENT_TITLE: {
          break;
        }
        case ELEMENT_ICON: {
          if (title_theme.iconPresent()) {
            non_text_width = Math.addExact(non_text_width, button_pad_left);
            non_text_width = Math.addExact(
              non_text_width,
              title_theme.buttonWidth());
            non_text_width = Math.addExact(non_text_width, button_pad_right);
          }
          break;
        }
      }
    }

    return non_text_width;
  }

  /**
   * Calculate the minimum amount of horizontal space needed to contain all of
   * the content that will appear in the title bar.
   *
   * @param measurement    A measurement interface
   * @param maximum        The maximum possible area that the title bar can
   *                       cover
   * @param title_theme    The title bar theme
   * @param title_text     The title bar text
   * @param is_closeable   {@code true} iff the title bar is attached to a
   *                       window that is closeable
   * @param is_maximizable {@code true} iff the title bar is attached to a
   *                       window that is maximizable
   *
   * @return The minimum width that is required to hold the contents of the
   * title bar
   */

  public static int minimumWidthRequired(
    final SyTextMeasurementType measurement,
    final SyBoxType<SySpaceParentRelativeType> maximum,
    final SyThemeWindowTitleBarType title_theme,
    final String title_text,
    final boolean is_closeable,
    final boolean is_maximizable)
  {
    NullCheck.notNull(measurement, "Text measurement");
    NullCheck.notNull(maximum, "Maximum");
    NullCheck.notNull(title_theme, "Title theme");
    NullCheck.notNull(title_text, "Title text");

    /*
     * Work out how much space is required for anything that isn't the titleBar
     * text.
     */

    final List<SyThemeTitleBarElement> elements =
      SyThemeTitleBars.elementsOrder(title_theme);
    final int non_text_width =
      SyThemeTitleBars.nonTextWidthRequired(
        title_theme, elements, is_closeable, is_maximizable);

    final int maximum_width = maximum.width();
    final int remaining =
      Math.max(0, Math.subtractExact(maximum_width, non_text_width));

    /*
     * Given the amount of remaining space, work out the smallest amount of
     * that remaining space that can contain the title bar text.
     */

    final int text_content_width =
      measurement.measureTextWidth(
        title_theme.textTheme().textFont(), title_text);

    final SyThemePaddingType text_padding =
      title_theme.textPadding();
    final int text_full_width =
      text_content_width
        + text_padding.paddingLeft()
        + text_padding.paddingRight();
    final int text_limited_width =
      Math.min(remaining, text_full_width);

    final int full_width = non_text_width + text_limited_width;
    Assertive.ensure(
      full_width <= maximum_width,
      "Maximum width calculation is correct");
    return full_width;
  }

  /**
   * Produce an arrangement for a title bar.
   *
   * @param maximum_space  The title bar's box
   * @param title_theme    The title bar theme
   * @param is_closeable   {@code true} iff the parent window is closeable
   * @param is_maximizable {@code true} iff the parent window is maximizable
   *
   * @return An arrangement for components
   */

  public static SyThemeWindowTitleBarArrangementType arrange(
    final SyBoxType<SySpaceParentRelativeType> maximum_space,
    final SyThemeWindowTitleBarType title_theme,
    final boolean is_closeable,
    final boolean is_maximizable)
  {
    final SyBoxType<SySpaceParentRelativeType> maximum_origin =
      SyBoxes.moveToOrigin(maximum_space);

    final List<SyThemeTitleBarElement> elements =
      SyThemeTitleBars.elementsOrder(title_theme);

    final Pair<List<SyThemeTitleBarElement>, List<SyThemeTitleBarElement>> pair =
      SyThemeTitleBars.sortElementsLeftRight(
        title_theme, is_closeable, is_maximizable, elements);

    final List<SyThemeTitleBarElement> left = pair.getLeft();
    final List<SyThemeTitleBarElement> right = pair.getRight();

    final SyThemePaddingType button_pad = title_theme.buttonPadding();
    final int button_pad_left = button_pad.paddingLeft();
    final int button_pad_right = button_pad.paddingRight();

    final SyThemeWindowTitleBarArrangement.Builder arrangement =
      SyThemeWindowTitleBarArrangement.builder();
    arrangement.setCloseButtonBox(SyBoxes.create(0, 0, 0, 0));
    arrangement.setMaximizeButtonBox(SyBoxes.create(0, 0, 0, 0));
    arrangement.setTitle(SyBoxes.create(0, 0, 0, 0));
    arrangement.setIconBox(SyBoxes.create(0, 0, 0, 0));

    int text_min_x = 0;

    final int button_width = title_theme.buttonWidth();

    {
      for (final SyThemeTitleBarElement element : left) {
        switch (element) {
          case ELEMENT_CLOSE_BUTTON: {
            Assertive.require(is_closeable, "Window must be closeable");

            text_min_x = Math.addExact(text_min_x, button_pad_left);
            final SyBoxType<SySpaceParentRelativeType> aligned =
              SyThemeTitleBars.alignedButton(
                maximum_origin,
                title_theme,
                text_min_x);
            arrangement.setCloseButtonBox(aligned);
            text_min_x = Math.addExact(text_min_x, button_width);
            text_min_x = Math.addExact(text_min_x, button_pad_right);
            break;
          }

          case ELEMENT_MAXIMIZE_BUTTON: {
            Assertive.require(is_maximizable, "Window must be maximizable");

            text_min_x = Math.addExact(text_min_x, button_pad_left);
            final SyBoxType<SySpaceParentRelativeType> aligned =
              SyThemeTitleBars.alignedButton(
                maximum_origin,
                title_theme,
                text_min_x);
            arrangement.setMaximizeButtonBox(aligned);
            text_min_x = Math.addExact(text_min_x, button_width);
            text_min_x = Math.addExact(text_min_x, button_pad_right);
            break;
          }

          case ELEMENT_TITLE: {
            throw new UnreachableCodeException();
          }

          case ELEMENT_ICON: {
            Assertive.require(title_theme.iconPresent(), "Icon must be shown");

            arrangement.setIconBox(
              SyThemeTitleBars.alignedIcon(
                maximum_origin,
                title_theme,
                text_min_x));
            text_min_x = Math.addExact(text_min_x, title_theme.iconWidth());
            break;
          }
        }
      }
    }

    int text_max_x = maximum_origin.maximumX();

    {
      for (int index = right.size() - 1; index >= 0; --index) {
        final SyThemeTitleBarElement element = right.get(index);
        switch (element) {
          case ELEMENT_CLOSE_BUTTON: {
            Assertive.require(is_closeable, "Window must be closeable");

            text_max_x = Math.subtractExact(text_max_x, button_pad_right);
            text_max_x = Math.subtractExact(text_max_x, button_width);
            final SyBoxType<SySpaceParentRelativeType> aligned =
              SyThemeTitleBars.alignedButton(
                maximum_origin,
                title_theme,
                text_max_x);
            arrangement.setCloseButtonBox(aligned);
            text_max_x = Math.subtractExact(text_max_x, button_pad_left);
            break;
          }

          case ELEMENT_MAXIMIZE_BUTTON: {
            Assertive.require(is_maximizable, "Window must be maximizable");

            text_max_x = Math.subtractExact(text_max_x, button_pad_right);
            text_max_x = Math.subtractExact(text_max_x, button_width);
            final SyBoxType<SySpaceParentRelativeType> aligned =
              SyThemeTitleBars.alignedButton(
                maximum_origin,
                title_theme,
                text_max_x);
            arrangement.setMaximizeButtonBox(aligned);
            text_max_x = Math.subtractExact(text_max_x, button_pad_left);
            break;
          }

          case ELEMENT_TITLE: {
            throw new UnreachableCodeException();
          }

          case ELEMENT_ICON: {
            Assertive.require(title_theme.iconPresent(), "Icon must be shown");

            arrangement.setIconBox(
              SyThemeTitleBars.alignedIcon(
                maximum_origin,
                title_theme,
                text_max_x));
            text_max_x = Math.subtractExact(
              text_max_x,
              title_theme.iconWidth());
            break;
          }
        }
      }
    }

    final SyThemePaddingType text_pad = title_theme.textPadding();
    final int text_pad_left = text_pad.paddingLeft();
    final int text_pad_right = text_pad.paddingRight();
    text_min_x = Math.max(0, Math.addExact(text_min_x, text_pad_left));
    text_max_x = Math.max(
      text_min_x,
      Math.subtractExact(text_max_x, text_pad_right));

    Assertive.require(
      text_min_x <= text_max_x, "Text minimum X <= Text maximum X");

    arrangement.setTitle(SyBox.of(
      text_min_x,
      text_max_x,
      0,
      maximum_origin.maximumY()));
    return arrangement.build();
  }

  private static SyBoxType<SySpaceParentRelativeType> alignedIcon(
    final SyBoxType<SySpaceParentRelativeType> container,
    final SyThemeWindowTitleBarType title_theme,
    final int x)
  {
    final SyBoxType<SySpaceParentRelativeType> box_unaligned =
      SyBoxes.create(x, 0, title_theme.iconWidth(), title_theme.iconHeight());

    switch (title_theme.buttonAlignment()) {
      case ALIGN_TOP: {
        return SyBoxes.alignVerticallyTop(container, box_unaligned);
      }
      case ALIGN_BOTTOM: {
        return SyBoxes.alignVerticallyBottom(container, box_unaligned);
      }
      case ALIGN_CENTER: {
        return SyBoxes.alignVerticallyCenter(container, box_unaligned);
      }
    }

    throw new UnreachableCodeException();
  }

  private static SyBoxType<SySpaceParentRelativeType> alignedButton(
    final SyBoxType<SySpaceParentRelativeType> container,
    final SyThemeWindowTitleBarType title_theme,
    final int x)
  {
    final SyBoxType<SySpaceParentRelativeType> box_unaligned = SyBoxes.create(
      x, 0, title_theme.buttonWidth(), title_theme.buttonHeight());

    final SyThemePaddingType padding = title_theme.buttonPadding();
    switch (title_theme.buttonAlignment()) {
      case ALIGN_TOP: {
        return SyBoxes.alignVerticallyTopOffset(
          container, box_unaligned, padding.paddingTop());
      }
      case ALIGN_BOTTOM: {
        return SyBoxes.alignVerticallyBottomOffset(
          container, box_unaligned, padding.paddingBottom());
      }
      case ALIGN_CENTER: {
        return SyBoxes.alignVerticallyCenter(container, box_unaligned);
      }
    }

    throw new UnreachableCodeException();
  }

  private static Pair<List<SyThemeTitleBarElement>, List<SyThemeTitleBarElement>> sortElementsLeftRight(
    final SyThemeWindowTitleBarType title_theme,
    final boolean is_closeable,
    final boolean is_maximizable,
    final List<SyThemeTitleBarElement> elements)
  {
    final List<SyThemeTitleBarElement> elements_left =
      new ArrayList<>(elements.size() / 2);
    final List<SyThemeTitleBarElement> elements_right =
      new ArrayList<>(elements.size() / 2);

    List<SyThemeTitleBarElement> elements_out = elements_left;

    final Iterator<SyThemeTitleBarElement> iter = elements.iterator();
    while (iter.hasNext()) {
      final SyThemeTitleBarElement element = iter.next();
      switch (element) {
        case ELEMENT_CLOSE_BUTTON: {
          if (is_closeable) {
            elements_out.add(element);
          }
          break;
        }

        case ELEMENT_MAXIMIZE_BUTTON: {
          if (is_maximizable) {
            elements_out.add(element);
          }
          break;
        }

        case ELEMENT_TITLE: {
          Assertive.require(Objects.equals(elements_out, elements_left));
          elements_out = elements_right;
          break;
        }

        case ELEMENT_ICON: {
          if (title_theme.iconPresent()) {
            elements_out.add(element);
          }
          break;
        }
      }
    }

    return Pair.pair(elements_left, elements_right);
  }
}
