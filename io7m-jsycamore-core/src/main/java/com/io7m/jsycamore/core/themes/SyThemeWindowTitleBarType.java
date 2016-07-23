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

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jsycamore.core.images.SyImageSpecificationType;
import org.immutables.value.Value;

import java.util.Comparator;
import java.util.Optional;

/**
 * The style applied to a window title bar.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeWindowTitleBarType
{
  /**
   * @return The image that will be added to the close button
   */

  Optional<SyImageSpecificationType> buttonCloseIcon();

  /**
   * @return The image that will be added to the maximize button
   */

  Optional<SyImageSpecificationType> buttonMaximizeIcon();

  /**
   * @return The width of buttons in the title bar
   */

  @Value.Parameter
  int buttonWidth();

  /**
   * @return The height of buttons in the title bar
   */

  @Value.Parameter
  int buttonHeight();

  /**
   * @return The vertical alignment of buttons within the title bar
   */

  @Value.Parameter
  SyAlignmentVertical buttonAlignment();

  /**
   * @return The theme for buttons in the title bar
   */

  @Value.Parameter
  SyThemeButtonRepeatingType buttonTheme();

  /**
   * @return The padding for buttons in the title bar
   */

  @Value.Parameter
  SyThemePaddingType buttonPadding();

  /**
   * @return A comparator that will decide the order of components within a
   * title bar
   */

  @Value.Parameter
  Comparator<SyThemeTitleBarElement> elementOrder();

  /**
   * @return The title bar height
   */

  @Value.Parameter
  int height();

  /**
   * @return {@code true} iff the title bar should display an icon
   */

  @Value.Parameter
  boolean iconPresent();

  /**
   * @return The width of icons in the title bar
   */

  @Value.Parameter
  int iconWidth();

  /**
   * @return The height of icons in the title bar
   */

  @Value.Parameter
  int iconHeight();

  /**
   * @return The vertical alignment of icons in the title bar
   */

  @Value.Parameter
  SyAlignmentVertical iconAlignment();

  /**
   * @return The theme for icons in the title bar
   */

  @Value.Parameter
  SyThemeImageType iconTheme();

  /**
   * @return The title bar panel theme
   */

  @Value.Parameter
  SyThemePanelType panelTheme();

  /**
   * @return The title bar text theme
   */

  @Value.Parameter
  SyThemeLabelType textTheme();

  /**
   * @return The title bar title text alignment
   */

  @Value.Parameter
  SyAlignmentHorizontal textAlignment();

  /**
   * @return The padding around the title text
   */

  @Value.Parameter
  SyThemePaddingType textPadding();

}
