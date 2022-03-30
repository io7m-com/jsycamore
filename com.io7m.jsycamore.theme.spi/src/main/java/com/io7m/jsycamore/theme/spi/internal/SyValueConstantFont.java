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


package com.io7m.jsycamore.theme.spi.internal;

import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyConstantType.SyConstantFontType;

import java.util.Objects;

import static com.io7m.jsycamore.theme.spi.internal.SyValueType.TYPE_FONT;

/**
 * An font constant node.
 */

public final class SyValueConstantFont extends SyValueAbstract
  implements SyValueNodeType, SyConstantFontType
{
  private final SyFontDescription valueDefault;
  private SyFontDescription valueNow;

  /**
   * Construct a constant node.
   *
   * @param inName        The name
   * @param inDescription The description
   * @param inValue       The value
   */

  public SyValueConstantFont(
    final String inName,
    final String inDescription,
    final SyFontDescription inValue)
  {
    super(inName, inDescription, TYPE_FONT);

    this.valueDefault =
      Objects.requireNonNull(inValue, "inValue");
    this.valueNow =
      this.valueDefault;
  }

  @Override
  public Object evaluated()
  {
    return this.valueNow;
  }

  @Override
  public void reset()
  {
    this.valueNow = this.valueDefault;
  }

  /**
   * Set the node value.
   *
   * @param value The value
   */

  public void set(
    final SyFontDescription value)
  {
    this.valueNow = value;
  }

  @Override
  public SyFontDescription valueDefault()
  {
    return this.valueDefault;
  }

  @Override
  public SyFontDescription valueNow()
  {
    return this.valueNow;
  }
}
