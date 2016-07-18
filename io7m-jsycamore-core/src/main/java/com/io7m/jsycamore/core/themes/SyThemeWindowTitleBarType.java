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
import org.immutables.value.Value;

import java.util.Comparator;

/**
 * The style applied to a window titlebar.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeWindowTitleBarType
{
  /**
   * @return The width of buttons in the titlebar
   */

  @Value.Parameter
  int buttonWidth();

  /**
   * @return The height of buttons in the titlebar
   */

  @Value.Parameter
  int buttonHeight();

  /**
   * @return The vertical alignment of buttons within the titlebar
   */

  @Value.Parameter
  SyAlignmentVertical buttonAlignment();

  /**
   * @return The theme for buttons in the titlebar
   */

  @Value.Parameter
  SyThemeButtonType buttonTheme();

  /**
   * @return The padding for buttons in the titlebar
   */

  @Value.Parameter
  SyThemePaddingType buttonPadding();

  /**
   * @return A comparator that will decide the order of components within a
   * titlebar
   */

  @Value.Parameter
  Comparator<SyThemeTitlebarElement> elementOrder();

  /**
   * @return The titlebar height
   */

  @Value.Parameter
  int height();

  /**
   * @return {@code true} iff the titlebar should display an icon
   */

  @Value.Parameter
  boolean showIcon();

  /**
   * @return The titlebar panel theme
   */

  @Value.Parameter
  SyThemePanelType panelTheme();

  /**
   * @return The titlebar text theme
   */

  @Value.Parameter
  SyThemeLabelType textTheme();

  /**
   * @return The titlebar title text alignment
   */

  @Value.Parameter
  SyAlignmentHorizontal textAlignment();

  /**
   * @return The padding around the title text
   */

  @Value.Parameter
  SyThemePaddingType textPadding();

}
