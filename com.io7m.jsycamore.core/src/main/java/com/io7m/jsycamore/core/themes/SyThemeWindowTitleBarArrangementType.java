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

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.core.SyImmutableStyleType;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import org.immutables.value.Value;

/**
 * The boxes for components that appear in window title bars.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyThemeWindowTitleBarArrangementType
{
  /**
   * @return The box that will be used for the close button
   */

  @Value.Parameter(order = 0)
  PAreaI<SySpaceParentRelativeType> closeButtonBox();

  /**
   * @return The box that will be used for the maximize button
   */

  @Value.Parameter(order = 1)
  PAreaI<SySpaceParentRelativeType> maximizeButtonBox();

  /**
   * @return The box that will be used for the icon
   */

  @Value.Parameter(order = 2)
  PAreaI<SySpaceParentRelativeType> iconBox();

  /**
   * @return The box that will be used for the title
   */

  @Value.Parameter(order = 3)
  PAreaI<SySpaceParentRelativeType> title();
}
