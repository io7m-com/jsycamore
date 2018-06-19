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

package com.io7m.jsycamore.api.themes;

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * Theme values for oriented meters.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyThemeMeterOrientedType
{
  /**
   * @return The base fill used for the containers of active meters
   */

  @Value.Parameter
  SyThemeFillType fillContainerActive();

  /**
   * @return The base fill used for containers of inactive meters
   */

  @Value.Parameter
  SyThemeFillType fillContainerInactive();

  /**
   * @return The embossing used for containers of active meters
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossContainerActive();

  /**
   * @return The embossing used for containers of inactive meters
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossContainerInactive();

  /**
   * @return The base fill used for the indicators of active meters
   */

  @Value.Parameter
  SyThemeFillType fillIndicatorActive();

  /**
   * @return The base fill used for the indicators of inactive meters
   */

  @Value.Parameter
  SyThemeFillType fillIndicatorInactive();

  /**
   * @return The embossing used for the indicators of active meters
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossIndicatorActive();

  /**
   * @return The embossing used for the indicators of inactive meters
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossIndicatorInactive();

  /**
   * @return The outline used for panels
   */

  @Value.Parameter
  Optional<SyThemeOutline> outline();
}
