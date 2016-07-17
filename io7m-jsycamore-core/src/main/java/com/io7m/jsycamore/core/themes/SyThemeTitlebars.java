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

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SyTextMeasurementType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.junreachable.UnimplementedCodeException;
import org.valid4j.Assertive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Functions for handling titlebars.
 */

public final class SyThemeTitlebars
{
  private SyThemeTitlebars()
  {
    throw new UnimplementedCodeException();
  }

  /**
   * Calculate the order in which any titlebar elements will appear.
   */

  private static List<SyThemeTitlebarElement> elementsOrder(
    final SyThemeWindowTitleBarType title_theme)
  {
    final List<SyThemeTitlebarElement> elements =
      new ArrayList<>(SyThemeTitlebarElement.values().length);
    Collections.addAll(elements, SyThemeTitlebarElement.values());
    Collections.sort(elements, title_theme.elementOrder());
    return elements;
  }

  /**
   * Determine how large a button will be given the title theme.
   */

  private static int buttonHeight(
    final SyThemeWindowTitleBarType title_theme)
  {
    /**
     * The height of a button is the titlebar height minus any top and
     * bottom padding.
     */

    final SyThemePaddingType padding = title_theme.buttonPadding();
    final int without_top =
      Math.subtractExact(title_theme.height(), padding.paddingTop());
    final int without_bottom =
      Math.subtractExact(without_top, padding.paddingBottom());
    return Math.max(0, without_bottom);
  }

  /**
   * Determine how much horizontal space is needed to contain all of the
   * non-text elements in the window, including padding.
   */

  private static int nonTextWidthRequired(
    final SyThemeWindowTitleBarType title_theme,
    final List<SyThemeTitlebarElement> elements,
    final boolean is_closeable,
    final boolean is_maximizable)
  {
    final SyThemePaddingType button_padding = title_theme.buttonPadding();
    final int button_height = SyThemeTitlebars.buttonHeight(title_theme);

    int non_text_width = 0;
    final int button_pad_left = button_padding.paddingLeft();
    final int button_pad_right = button_padding.paddingRight();
    for (int index = 0; index < elements.size(); ++index) {
      final SyThemeTitlebarElement element = elements.get(index);
      switch (element) {
        case ELEMENT_CLOSE_BUTTON: {
          if (is_closeable) {
            non_text_width = Math.addExact(non_text_width, button_pad_left);
            non_text_width = Math.addExact(non_text_width, button_height);
            non_text_width = Math.addExact(non_text_width, button_pad_right);
          }
          break;
        }
        case ELEMENT_MAXIMIZE_BUTTON: {
          if (is_maximizable) {
            non_text_width = Math.addExact(non_text_width, button_pad_left);
            non_text_width = Math.addExact(non_text_width, button_height);
            non_text_width = Math.addExact(non_text_width, button_pad_right);
          }
          break;
        }
        case ELEMENT_TITLE: {
          break;
        }
        case ELEMENT_ICON: {
          if (title_theme.showIcon()) {
            non_text_width = Math.addExact(non_text_width, button_pad_left);
            non_text_width = Math.addExact(non_text_width, button_height);
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
   * the content that will appear in the titlebar.
   *
   * @param measurement    A measurement interface
   * @param maximum        The maximum possible area that the titlebar can
   *                       cover
   * @param title_theme    The titlebar theme
   * @param title_text     The titlebar text
   * @param is_closeable   {@code true} iff the titlebar is attached to a window
   *                       that is closeable
   * @param is_maximizable {@code true} iff the titlebar is attached to a window
   *                       that is maximizable
   *
   * @return The minimum width that is required to hold the contents of the
   * titlebar
   */

  public static int minimumWidthRequired(
    final SyTextMeasurementType measurement,
    final SyBoxType<SySpaceParentRelativeType> maximum,
    final SyThemeWindowTitleBarType title_theme,
    final String title_text,
    final boolean is_closeable,
    final boolean is_maximizable)
  {
    NullCheck.notNull(measurement);
    NullCheck.notNull(maximum);
    NullCheck.notNull(title_theme);
    NullCheck.notNull(title_text);

    /*
     * Work out how much space is required for anything that isn't the titlebar
     * text.
     */

    final List<SyThemeTitlebarElement> elements =
      SyThemeTitlebars.elementsOrder(title_theme);
    final int non_text_width =
      SyThemeTitlebars.nonTextWidthRequired(
        title_theme, elements, is_closeable, is_maximizable);

    final int maximum_width = maximum.width();
    final int remaining =
      Math.max(0, Math.subtractExact(maximum_width, non_text_width));

    /*
     * Given the amount of remaining space, work out the smallest amount of
     * that remaining space that can contain the titlebar text.
     */

    final int text_content_width =
      measurement.measureTextWidth(title_theme.textFont(), title_text);

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
}
