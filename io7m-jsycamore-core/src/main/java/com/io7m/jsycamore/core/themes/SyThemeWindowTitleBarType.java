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
import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jtensors.VectorI3F;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * The style applied to a window titlebar.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeWindowTitleBarType
{
  /**
   * @return The titlebar height
   */

  @Value.Parameter
  @Value.Default
  default int height()
  {
    return 16;
  }

  /**
   * @return The titlebar width behavior
   */

  @Value.Parameter
  @Value.Default
  default SyThemeWindowTitlebarWidthBehavior widthBehavior()
  {
    return SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
  }

  /**
   * @return The titlebar vertical placement
   */

  @Value.Parameter
  @Value.Default
  default SyThemeWindowTitlebarVerticalPlacement verticalPlacement()
  {
    return SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
  }

  /**
   * @return The titlebar horizontal alignment
   */

  @Value.Parameter
  @Value.Default
  default SyAlignmentHorizontal horizontalAlignment()
  {
    return SyAlignmentHorizontal.ALIGN_CENTER;
  }

  /**
   * @return The titlebar text color when the window is active
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F textColorActive()
  {
    return new VectorI3F(0.0f, 0.0f, 0.0f);
  }

  /**
   * @return The titlebar text color when the window is not active
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F textColorInactive()
  {
    return new VectorI3F(0.2f, 0.2f, 0.2f);
  }

  /**
   * @return The titlebar color when the window is active
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorActive()
  {
    return new VectorI3F(0.3f, 0.3f, 0.3f);
  }

  /**
   * @return The titlebar color when the window is inactive
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorInactive()
  {
    return new VectorI3F(0.3f, 0.3f, 0.3f);
  }

  /**
   * @return The titlebar emboss style that will be used when the window is
   * active
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossActive();

  /**
   * @return The titlebar emboss style that will be used when the window is
   * inactive
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossInactive();

  /**
   * @return The titlebar title text alignment
   */

  @Value.Parameter
  @Value.Default
  default SyAlignmentHorizontal textAlignment()
  {
    return SyAlignmentHorizontal.ALIGN_CENTER;
  }

  /**
   * @return The titlebar text font
   */

  @Value.Parameter
  @Value.Default
  default String textFont()
  {
    return "Monospaced-plain-10";
  }
}
