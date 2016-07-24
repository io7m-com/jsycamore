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

package com.io7m.jsycamore.core.components;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.themes.SyThemeButtonType;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The type of buttons.
 */

public interface SyButtonReadableType extends SyComponentReadableType
{
  /**
   * @param <T> The precise type of theme
   *
   * @return The current theme for the button
   */

  <T extends SyThemeButtonType> Optional<T> theme();

  /**
   * @return The current state of the button
   */

  SyButtonState buttonState();

  @Override
  default <A, B> B matchComponentReadable(
    final A context,
    final BiFunction<A, SyButtonReadableType, B> on_button,
    final BiFunction<A, SyPanelReadableType, B> on_panel,
    final BiFunction<A, SyLabelReadableType, B> on_label,
    final BiFunction<A, SyImageReadableType, B> on_image,
    final BiFunction<A, SyMeterReadableType, B> on_meter)
  {
    return NullCheck.notNull(on_button).apply(context, this);
  }
}
