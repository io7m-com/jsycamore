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

package com.io7m.jsycamore.themes.fenestra;

import com.io7m.jsycamore.annotations.SyImmutableStyleType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
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
  default Vector3D titlebarColorActive()
  {
    return Vector3D.of(0.085, 0.45, 0.19);
  }

  /**
   * @return The base color for inactive titlebars
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarColorInactive()
  {
    return Vector3D.of(0.62, 0.62, 0.62);
  }

  /**
   * @return The text color for active window frames
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarTextColorActive()
  {
    return Vector3D.of(1.0, 1.0, 1.0);
  }

  /**
   * @return The text color for inactive window frames
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarTextColorInactive()
  {
    return Vector3D.of(1.0, 1.0, 1.0);
  }

  /**
   * @return The base color for window frames
   */

  @Value.Parameter
  @Value.Default
  default Vector3D frameColor()
  {
    return Vector3D.of(0.82, 0.81, 0.78);
  }

  /**
   * @return The general background color used for buttons, panels, etc
   */

  @Value.Parameter
  @Value.Default
  default Vector3D backgroundColor()
  {
    return Vector3D.of(0.82, 0.81, 0.78);
  }

  /**
   * @return The general foreground color used for text on components, etc
   */

  @Value.Parameter
  @Value.Default
  default Vector3D foregroundColorActive()
  {
    return Vector3D.of(0.0, 0.0, 0.0);
  }

  /**
   * @return The general foreground color used for text on inactive components,
   * etc
   */

  @Value.Parameter
  @Value.Default
  default Vector3D foregroundColorInactive()
  {
    return Vector3D.of(0.4, 0.4, 0.4);
  }

  /**
   * @return The factor by which to scale the active color to produce the light
   * shade for embossing
   */

  @Value.Parameter
  @Value.Default
  default double colorLightFactor()
  {
    return 1.2;
  }

  /**
   * @return The factor by which to scale the active color to produce the dark
   * shade for embossing
   */

  @Value.Parameter
  @Value.Default
  default double colorDarkFactor()
  {
    return 0.5;
  }
}
