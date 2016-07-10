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

import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jtensors.VectorI3F;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * A button theme specification.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeButtonType
{
  /**
   * @return The base color used for active buttons
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorActive()
  {
    return new VectorI3F(0.8f, 0.8f, 0.8f);
  }

  /**
   * @return The embossing used for active buttons
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossActive();

  /**
   * @return The base color used for active buttons that are currently under the
   * mouse cursor
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorOver()
  {
    return new VectorI3F(0.85f, 0.85f, 0.85f);
  }

  /**
   * @return The embossing used for active buttons that are currently under the
   * mouse cursor
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossOver();

  /**
   * @return The base color used for pressed buttons
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorPressed()
  {
    return new VectorI3F(0.75f, 0.75f, 0.75f);
  }

  /**
   * @return The embossing used for pressed buttons
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossPressed();

  /**
   * @return The base color used for disabled buttons
   */

  @Value.Parameter
  @Value.Default
  default VectorI3F colorDisabled()
  {
    return new VectorI3F(0.8f, 0.8f, 0.8f);
  }

  /**
   * @return The embossing used for disabled buttons
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossDisabled();

  /**
   * @return The outline used for buttons
   */

  @Value.Parameter
  Optional<SyThemeOutlineType> outline();
}
