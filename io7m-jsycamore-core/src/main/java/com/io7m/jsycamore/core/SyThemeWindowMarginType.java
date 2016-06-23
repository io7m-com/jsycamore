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

package com.io7m.jsycamore.core;

import com.io7m.jtensors.VectorI3F;
import org.immutables.value.Value;

import java.util.Optional;

@SyImmutableStyleType
@Value.Immutable
@Value.Modifiable
public interface SyThemeWindowMarginType
{
  @Value.Parameter
  @Value.Default
  default int leftWidth()
  {
    return 16;
  }

  @Value.Parameter
  @Value.Default
  default int rightWidth()
  {
    return 16;
  }

  @Value.Parameter
  @Value.Default
  default int topHeight()
  {
    return 16;
  }

  @Value.Parameter
  @Value.Default
  default int bottomHeight()
  {
    return 16;
  }

  @Value.Parameter
  Optional<SyThemeEmbossType> embossActive();

  @Value.Parameter
  Optional<SyThemeEmbossType> embossInactive();

  @Value.Parameter
  @Value.Default
  default SyThemeWindowMarginCorner bottomLeftStyle()
  {
    return SyThemeWindowMarginCorner.MARGIN_CORNER_NONE;
  }

  @Value.Parameter
  @Value.Default
  default SyThemeWindowMarginCorner bottomRightStyle()
  {
    return SyThemeWindowMarginCorner.MARGIN_CORNER_NONE;
  }

  @Value.Parameter
  @Value.Default
  default SyThemeWindowMarginCorner topLeftStyle()
  {
    return SyThemeWindowMarginCorner.MARGIN_CORNER_NONE;
  }

  @Value.Parameter
  @Value.Default
  default SyThemeWindowMarginCorner topRightStyle()
  {
    return SyThemeWindowMarginCorner.MARGIN_CORNER_NONE;
  }

  @Value.Parameter
  @Value.Default
  default VectorI3F colorActive()
  {
    return new VectorI3F(0.8f, 0.8f, 0.8f);
  }

  @Value.Parameter
  @Value.Default
  default VectorI3F colorInactive()
  {
    return new VectorI3F(0.8f, 0.8f, 0.8f);
  }
}
