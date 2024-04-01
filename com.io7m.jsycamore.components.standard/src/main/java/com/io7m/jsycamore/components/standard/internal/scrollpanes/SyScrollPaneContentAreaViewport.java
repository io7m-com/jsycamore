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


package com.io7m.jsycamore.components.standard.internal.scrollpanes;

import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.keyboard.SyKeyboardFocusBehavior;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLPANE_CONTENT_AREA_VIEWPORT;

final class SyScrollPaneContentAreaViewport extends SyComponentAbstract
{
  SyScrollPaneContentAreaViewport(
    final SyScreenType inScreen)
  {
    super(
      inScreen,
      List.of(),
      SyKeyboardFocusBehavior.IGNORES_FOCUS_AND_CONTINUES_TRAVERSAL
    );
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SCROLLPANE_CONTENT_AREA_VIEWPORT);
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
  }
}
