/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.components.standard;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.SyThemeType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.components.SyTextViewType;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;

public final class SyTextView
  extends SyComponentAbstract implements SyTextViewType
{
  private final AttributeType<String> text;

  public SyTextView()
  {
    this.text = SyComponentAttributes.get().create("");
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyThemeType theme,
    final SyConstraints constraints)
  {
    return constraints.sizeMaximum();
  }

  @Override
  protected boolean onEvent(
    final SyEventType event)
  {
    return false;
  }

  @Override
  public AttributeType<String> text()
  {
    return this.text;
  }
}
