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
 * A meter theme specification.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeMeterType
{
  /**
   * @return The base color used for the containers of active meters
   */

  @Value.Parameter
  VectorI3F colorContainerActive();

  /**
   * @return The base color used for containers of inactive meters
   */

  @Value.Parameter
  VectorI3F colorContainerInactive();

  /**
   * @return The embossing used for containers of active meters
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossContainerActive();

  /**
   * @return The embossing used for containers of inactive meters
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossContainerInactive();

  /**
   * @return The base color used for the fills of active meters
   */

  @Value.Parameter
  VectorI3F colorFillActive();

  /**
   * @return The base color used for fills of inactive meters
   */

  @Value.Parameter
  VectorI3F colorFillInactive();

  /**
   * @return The embossing used for fills of active meters
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossFillActive();

  /**
   * @return The embossing used for fills of inactive meters
   */

  @Value.Parameter
  Optional<SyThemeEmbossType> embossFillInactive();

  /**
   * @return The outline used for panels
   */

  @Value.Parameter
  Optional<SyThemeOutlineType> outline();
}
