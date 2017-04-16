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

import com.io7m.jsycamore.core.themes.SyThemeMeterType;
import com.io7m.jsycamore.core.themes.SyThemeType;

import java.util.Optional;

/**
 * The default implementation of the {@link SyMeterType} interface.
 */

public final class SyMeter extends SyMeterAbstract
{
  private SyMeter()
  {
    super(() -> true);
  }

  /**
   * @return A new meter
   */

  public static SyMeterType create()
  {
    return new SyMeter();
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[SyMeter 0x");
    sb.append(Integer.toHexString(this.hashCode()));
    sb.append(" ");
    sb.append(this.value());
    sb.append(" ");
    sb.append(this.box().width());
    sb.append("x");
    sb.append(this.box().height());
    sb.append(" ");
    sb.append(this.box().minimumX());
    sb.append("+");
    sb.append(this.box().minimumY());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public Optional<SyThemeMeterType> theme()
  {
    return this.windowTheme().map(SyThemeType::meterTheme);
  }
}
