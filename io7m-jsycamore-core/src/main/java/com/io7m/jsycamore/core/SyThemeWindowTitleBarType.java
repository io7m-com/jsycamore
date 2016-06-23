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

package com.io7m.jsycamore.core;

import com.io7m.jtensors.VectorI3F;
import org.immutables.value.Value;

import java.util.Optional;

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeWindowTitleBarType
{
  @Value.Parameter
  @Value.Default
  default int height()
  {
    return 16;
  }

  @Value.Parameter
  @Value.Default
  default VectorI3F colorActive()
  {
    return new VectorI3F(0.3f, 0.3f, 0.3f);
  }

  @Value.Parameter
  @Value.Default
  default VectorI3F colorInactive()
  {
    return new VectorI3F(0.3f, 0.3f, 0.3f);
  }

  @Value.Parameter
  Optional<SyThemeEmbossType> embossActive();

  @Value.Parameter
  Optional<SyThemeEmbossType> embossInactive();

  @Value.Parameter
  @Value.Default
  default SyThemeAlignment textAlignment()
  {
    return SyThemeAlignment.ALIGN_CENTER;
  }

  @Value.Parameter
  @Value.Default
  default String textFont()
  {
    return "Monospaced 10";
  }
}
