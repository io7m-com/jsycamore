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

package com.io7m.jsycamore.awt;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import org.immutables.value.Value;

/**
 * The parameters required to render an embossed window frame.
 */

@Value.Immutable
@ImmutablesStyleType
interface SyAWTWindowRendererEmbossedFrameParametersType
{
  /**
   * @return The window frame adjusted for the theme's window outline
   */

  @Value.Parameter
  PAreaI<SySpaceParentRelativeType> frameBoxAdjustedForOutline();

  /**
   * @return The height of the bottom section of the frame
   */

  @Value.Parameter
  int bottomHeight();

  /**
   * @return {@code true} if caps should be rendered for the bottom-left corner
   */

  @Value.Parameter
  boolean bottomLeftCaps();

  /**
   * @return The arm length of the bottom-left corner
   */

  @Value.Parameter
  int bottomLeftArmLength();

  /**
   * @return {@code true} if caps should be rendered for the bottom-right corner
   */

  @Value.Parameter
  boolean bottomRightCaps();

  /**
   * @return The arm length of the bottom-right corner
   */

  @Value.Parameter
  int bottomRightArmLength();

  /**
   * @return The width of the bottom section of the frame
   */

  @Value.Parameter
  int bottomWidth();

  /**
   * @return The x offset of the bottom section of the window frame
   */

  @Value.Parameter
  int bottomX();

  /**
   * @return The y offset of the bottom section of the window frame
   */

  @Value.Parameter
  int bottomY();

  /**
   * @return The size of the emboss
   */

  @Value.Parameter
  int embossSize();

  /**
   * @return The height of the left section of the window frame
   */

  @Value.Parameter
  int leftHeight();

  /**
   * @return The width of the left section of the window frame
   */

  @Value.Parameter
  int leftWidth();

  /**
   * @return The y offset of the left section of the window frame
   */

  @Value.Parameter
  int leftY();

  /**
   * @return The height of the right section of the window frame
   */

  @Value.Parameter
  int rightHeight();

  /**
   * @return The width of the right section of the window frame
   */

  @Value.Parameter
  int rightWidth();

  /**
   * @return The y offset of the right section of the window frame
   */

  @Value.Parameter
  int rightY();

  /**
   * @return The height of the top section of the frame
   */

  @Value.Parameter
  int topHeight();

  /**
   * @return {@code true} if caps should be rendered for the top-left corner
   */

  @Value.Parameter
  boolean topLeftCaps();

  /**
   * @return The arm length of the top-left corner
   */

  @Value.Parameter
  int topLeftArmLength();

  /**
   * @return {@code true} if caps should be rendered for the top-right corner
   */

  @Value.Parameter
  boolean topRightCaps();

  /**
   * @return The arm length of the top-right corner
   */

  @Value.Parameter
  int topRightArmLength();

  /**
   * @return The width of the top section of the frame
   */

  @Value.Parameter
  int topWidth();

  /**
   * @return The x offset of the top section of the window frame
   */

  @Value.Parameter
  int topX();
}
