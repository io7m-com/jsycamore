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

import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.components.standard.SyMenu;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.List;

import static com.io7m.jsycamore.api.text.SyTextDirection.TEXT_DIRECTION_LEFT_TO_RIGHT;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_BUTTON_MENU;

/**
 * A window menu button.
 */

public final class SyWindowButtonMenu extends SyWindowButtonWithIcon
{
  private final SyMenu menu;

  SyWindowButtonMenu(final SyScreenType screen)
  {
    super(screen, WINDOW_BUTTON_MENU, "jsycamore:icon:window_menu");

    this.menu = new SyMenu(screen);
    this.menu.addAtom(
      new SyText("Close", TEXT_DIRECTION_LEFT_TO_RIGHT),
      this::windowClose
    );
    this.menu.addAtom(
      new SyText("Maximize", TEXT_DIRECTION_LEFT_TO_RIGHT),
      this::windowMaximize
    );
  }

  private void windowClose()
  {
    final var windowOpt = this.window();
    if (windowOpt.isEmpty()) {
      return;
    }

    final var window = windowOpt.get();
    switch (window.closeButtonBehaviour().get()) {
      case HIDE_ON_CLOSE_BUTTON -> {
        window.screen()
          .windowService()
          .windowHide(window);
      }
      case CLOSE_ON_CLOSE_BUTTON -> {
        window.screen()
          .windowService()
          .windowClose(window);
      }
    }
  }

  private void windowMaximize()
  {
    this.window().ifPresent(w -> {
      w.screen()
        .windowService()
        .windowMaximize(w);
    });
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SyThemeClassNameStandard.WINDOW_BUTTON_MENU, BUTTON);
  }

  @Override
  protected void onClicked()
  {
    this.window()
      .map(SyWindowType::screen)
      .ifPresent(s -> {
        final var position = s.mousePosition().get();
        this.menu.setPosition(PVector2I.of(position.x(), position.y()));
        s.menuService().menuOpen(this.menu);
      });
  }
}
