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
 * The parameters for the {@link SyThemeMotive} theme.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeMotiveSpecificationType
{
  /**
   * @return The base color for active window frames
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorActive()
  {
    return new VectorI3F(0.34f, 0.42f, 0.48f);
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
