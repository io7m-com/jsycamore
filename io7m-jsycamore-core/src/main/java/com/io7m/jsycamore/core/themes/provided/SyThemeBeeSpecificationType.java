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

package com.io7m.jsycamore.core.themes.provided;

import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jtensors.VectorI3F;
import org.immutables.value.Value;

/**
 * The parameters for the {@link SyThemeBee} theme.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeBeeSpecificationType
{
  /**
   * @return The base color for active titlebars
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarColorActive()
  {
    return new VectorI3F(1.0f, 0.79f, 0.0f);
  }

  /**
   * @return The base color for inactive titlebars
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarColorInactive()
  {
    return new VectorI3F(0.8f, 0.8f, 0.8f);
  }

  /**
   * @return The text color for active window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarTextColorActive()
  {
    return new VectorI3F(0.0f, 0.0f, 0.0f);
  }

  /**
   * @return The text color for inactive window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarTextColorInactive()
  {
    return new VectorI3F(0.3f, 0.3f, 0.3f);
  }

  /**
   * @return The base color for window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F frameColor()
  {
    return new VectorI3F(0.8f, 0.8f, 0.8f);
  }

  /**
   * @return The factor by which to scale the active color to produce the light
   * shade for embossing
   */

  @Value.Parameter
  @Value.Default
  default float colorLightFactor()
  {
    return 1.5f;
  }

  /**
   * @return The factor by which to scale the active color to produce the dark
   * shade for embossing
   */

  @Value.Parameter
  @Value.Default
  default float colorDarkFactor()
  {
    return 0.5f;
  }
}
