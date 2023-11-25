/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.components.standard.internal.scrollbars;

import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.components.standard.SyAlign;
import com.io7m.jsycamore.components.standard.SyButtonAbstract;
import com.io7m.jsycamore.components.standard.SyImageView;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLBAR_VERTICAL_BUTTON_UP;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLBAR_VERTICAL_BUTTON_UP_ICON;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER;
import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

final class SyScrollBarVButtonUp extends SyButtonAbstract
{
  private final SyImageView image;
  private final SyAlign align;

  SyScrollBarVButtonUp()
  {
    super(List.of(SCROLLBAR_VERTICAL_BUTTON_UP));

    this.image = new SyImageView(List.of(SCROLLBAR_VERTICAL_BUTTON_UP_ICON));
    this.image.setImageURI("jsycamore:icon:scroll_up");

    this.align = new SyAlign();
    this.align.alignmentHorizontal().set(ALIGN_HORIZONTAL_CENTER);
    this.align.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    this.align.childAdd(this.image);
    this.childAdd(this.align);
  }

  @Override
  protected SyEventConsumed onOtherEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  protected void onClicked()
  {

  }
}
