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

package com.io7m.jsycamore.vanilla;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.screens.SyScreenFactoryType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.vanilla.internal.SyScreen;
import com.io7m.jsycamore.vanilla.internal.SyServices;
import com.io7m.jsycamore.vanilla.internal.SyTextSelectionService;

import java.util.Objects;

/**
 * A factory of screens.
 */

public final class SyScreenFactory implements SyScreenFactoryType
{
  /**
   * A factory of screens.
   */

  public SyScreenFactory()
  {

  }

  @Override
  public SyScreenType create(
    final SyThemeType theme,
    final SyFontDirectoryServiceType fonts,
    final PAreaSizeI<SySpaceViewportType> size)
  {
    Objects.requireNonNull(theme, "theme");
    Objects.requireNonNull(fonts, "fonts");
    Objects.requireNonNull(size, "size");

    final var services = new SyServices();
    services.register(
      SyFontDirectoryServiceType.class, fonts);
    services.register(
      SyTextSelectionServiceType.class, new SyTextSelectionService());

    return new SyScreen(services, theme, size);
  }
}
