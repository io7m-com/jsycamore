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
import com.io7m.junreachable.UnreachableCodeException;
import org.immutables.value.Value;

import java.util.Comparator;
import java.util.Optional;

/**
 * The style applied to a window titlebar.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeWindowTitleBarType
{
  /**
   * @return The titlebar outline, if any
   */

  @Value.Parameter
  Optional<SyThemeOutlineType> outline();

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
   * @return The padding around buttons on the titlebar
   */

  @Value.Parameter
  @Value.Default
  default SyThemePaddingType buttonPadding()
  {
    return SyThemePadding.of(0, 0, 0, 0);
  }

  /**
   * @return {@code true} iff the titlebar should display an icon
   */

  @Value.Parameter
  @Value.Default
  default boolean showIcon()
  {
    return false;
  }

  /**
   * @return A function that is used to decide the order of elements appearing
   * in a titlebar
   */

  @Value.Parameter
  @Value.Default
  default Comparator<SyThemeTitlebarElement> elementOrder()
  {
    return (o1, o2) -> {
      switch (o1) {
        case ELEMENT_CLOSE_BUTTON: {
          switch (o2) {
            case ELEMENT_CLOSE_BUTTON:
              return 0;
            case ELEMENT_MAXIMIZE_BUTTON:
            case ELEMENT_TITLE:
            case ELEMENT_ICON:
              return 1;
          }
          throw new UnreachableCodeException();
        }
        case ELEMENT_MAXIMIZE_BUTTON: {
          switch (o2) {
            case ELEMENT_CLOSE_BUTTON:
              return -1;
            case ELEMENT_MAXIMIZE_BUTTON:
              return 0;
            case ELEMENT_TITLE:
            case ELEMENT_ICON:
              return 1;
          }
          throw new UnreachableCodeException();
        }
        case ELEMENT_TITLE: {
          switch (o2) {
            case ELEMENT_CLOSE_BUTTON:
            case ELEMENT_MAXIMIZE_BUTTON:
              return -1;
            case ELEMENT_TITLE:
              return 0;
            case ELEMENT_ICON:
              return 1;
          }
          throw new UnreachableCodeException();
        }
        case ELEMENT_ICON: {
          switch (o2) {
            case ELEMENT_CLOSE_BUTTON:
            case ELEMENT_MAXIMIZE_BUTTON:
            case ELEMENT_TITLE:
              return -1;
            case ELEMENT_ICON:
              return 0;
          }
          throw new UnreachableCodeException();
        }
      }

      throw new UnreachableCodeException();
    };
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
   * @return The padding around the title text
   */

  @Value.Parameter
  @Value.Default
  default SyThemePaddingType textPadding()
  {
    return SyThemePadding.of(16, 16, 0, 0);
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
