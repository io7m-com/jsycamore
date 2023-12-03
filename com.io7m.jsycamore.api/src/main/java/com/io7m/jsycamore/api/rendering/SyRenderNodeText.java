/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.rendering;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Objects;

/**
 * A text render node.
 *
 * @param name      The node name, for debugging purposes
 * @param position  The position
 * @param size      The size
 * @param text      The text
 * @param fillPaint The paint to be used to fill text
 * @param font      The font
 */

public record SyRenderNodeText(
  String name,
  PVector2I<SySpaceComponentRelativeType> position,
  PAreaSizeI<SySpaceComponentRelativeType> size,
  SyPaintFillType fillPaint,
  SyFontType font,
  SyText text)
  implements SyRenderNodePrimitiveType
{
  /**
   * A text render node.
   *
   * @param name      The node name, for debugging purposes
   * @param position  The position
   * @param size      The size
   * @param text      The text
   * @param fillPaint The paint to be used to fill text
   * @param font      The font
   */

  public SyRenderNodeText
  {
    Objects.requireNonNull(fillPaint, "fillPaint");
    Objects.requireNonNull(position, "position");
    Objects.requireNonNull(size, "size");
    Objects.requireNonNull(font, "font");
    Objects.requireNonNull(text, "text");
  }
}
