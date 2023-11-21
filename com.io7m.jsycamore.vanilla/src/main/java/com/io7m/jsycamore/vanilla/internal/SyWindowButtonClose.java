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

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_CLOSE;

/**
 * A window close button.
 */

public final class SyWindowButtonClose extends SyWindowButtonWithIcon
{
  /**
   * A window close button.
   */

  public SyWindowButtonClose()
  {
    super(WINDOW_BUTTON_CLOSE, "jsycamore:icon:window_close");
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SyThemeClassNameStandard.WINDOW_BUTTON_CLOSE, BUTTON);
  }

  @Override
  protected void onClicked()
  {
    this.window().ifPresent(w -> {
      switch (w.closeButtonBehaviour().get()) {
        case HIDE_ON_CLOSE_BUTTON -> {
          w.screen().windowHide(w);
        }
        case CLOSE_ON_CLOSE_BUTTON -> {
          w.screen().windowClose(w);
        }
      }
    });
  }
}
