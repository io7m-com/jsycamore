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

import java.util.function.BiFunction;

/**
 * The type of fills for objects.
 */

public interface SyThemeFillType
{
  /**
   * Match on the type of fill.
   *
   * @param context            A context value passed through to the given
   *                           functions
   * @param on_gradient_linear Evaluated if this fill is a linear gradient
   * @param on_color           Evaluated if this fill is a flat color
   * @param <A>                The type of opaque context values
   * @param <B>                The type of returned values
   *
   * @return The value returned by whichever one of the given functions is
   * evaluated
   */

  <A, B> B matchFill(
    A context,
    BiFunction<A, SyThemeGradientLinearType, B> on_gradient_linear,
    BiFunction<A, SyThemeColorType, B> on_color);
}
