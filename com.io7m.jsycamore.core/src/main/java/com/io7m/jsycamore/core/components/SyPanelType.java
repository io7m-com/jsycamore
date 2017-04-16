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

import java.util.function.BiFunction;

/**
 * The type of panels. These are simple (possibly invisible) containers for
 * other components.
 */

public interface SyPanelType extends SyComponentType, SyPanelReadableType
{
  /**
   * Set the panel as <i>transparent</i>.
   *
   * @param e {@code true} iff this panel is <i>transparent</i>
   *
   * @see SyPanelReadableType#isPanelTransparent()
   */

  void setPanelTransparent(boolean e);

  @Override
  default <A, B> B matchComponent(
    final A context,
    final BiFunction<A, SyButtonType, B> on_button,
    final BiFunction<A, SyPanelType, B> on_panel,
    final BiFunction<A, SyLabelType, B> on_label,
    final BiFunction<A, SyImageType, B> on_image,
    final BiFunction<A, SyMeterType, B> on_meter)
  {
    return NullCheck.notNull(on_panel, "Receiver").apply(context, this);
  }
}
