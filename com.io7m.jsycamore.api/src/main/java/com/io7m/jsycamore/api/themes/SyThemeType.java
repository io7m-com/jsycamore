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

/**
 * The type of themes.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyThemeType
{
  /**
   * @return The theme used for windows
   */

  @Value.Parameter
  SyThemeWindow windowTheme();

  /**
   * @return The theme used for repeating buttons
   */

  @Value.Parameter
  SyThemeButtonRepeating buttonRepeatingTheme();

  /**
   * @return The theme used for checkbox buttons
   */

  @Value.Parameter
  SyThemeButtonCheckbox buttonCheckboxTheme();

  /**
   * @return The theme used for meters
   */

  @Value.Parameter
  SyThemeMeter meterTheme();

  /**
   * @return The theme used for panels
   */

  @Value.Parameter
  SyThemePanel panelTheme();

  /**
   * @return The theme used for labels
   */

  @Value.Parameter
  SyThemeLabel labelTheme();

  /**
   * @return The theme used for images
   */

  @Value.Parameter
  SyThemeImage imageTheme();
}
