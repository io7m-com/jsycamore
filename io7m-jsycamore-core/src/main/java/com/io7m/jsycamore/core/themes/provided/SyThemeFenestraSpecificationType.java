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
 * The parameters for the {@link SyThemeFenestra} theme.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeFenestraSpecificationType
{
  /**
   * @return The base color for active titlebars
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarColorActive()
  {
    return new VectorI3F(0.085f, 0.45f, 0.19f);
  }

  /**
   * @return The base color for inactive titlebars
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarColorInactive()
  {
    return new VectorI3F(0.62f, 0.62f, 0.62f);
  }

  /**
   * @return The text color for active window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarTextColorActive()
  {
    return new VectorI3F(1.0f, 1.0f, 1.0f);
  }

  /**
   * @return The text color for inactive window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F titlebarTextColorInactive()
  {
    return new VectorI3F(1.0f, 1.0f, 1.0f);
  }

  /**
   * @return The base color for window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F frameColor()
  {
    return new VectorI3F(0.82f, 0.81f, 0.78f);
  }

  /**
   * @return The general background color used for buttons, panels, etc
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F backgroundColor()
  {
    return new VectorI3F(0.82f, 0.81f, 0.78f);
  }

  /**
   * @return The general foreground color used for text on components, etc
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F foregroundColorActive()
  {
    return new VectorI3F(0.0f, 0.0f, 0.0f);
  }

  /**
   * @return The general foreground color used for text on inactive components,
   * etc
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F foregroundColorInactive()
  {
    return new VectorI3F(0.4f, 0.4f, 0.4f);
  }

  /**
   * @return The factor by which to scale the active color to produce the light
   * shade for embossing
   */

  @Value.Parameter
  @Value.Default
  default float colorLightFactor()
  {
    return 1.2f;
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
