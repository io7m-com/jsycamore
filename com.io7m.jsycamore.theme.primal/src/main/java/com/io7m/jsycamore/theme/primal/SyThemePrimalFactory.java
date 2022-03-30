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

package com.io7m.jsycamore.theme.primal;

import com.io7m.jsycamore.api.themes.SyThemeFactoryType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.theme.primal.internal.SyThemePrimal;
import org.osgi.service.component.annotations.Component;

/**
 * The Primal theme.
 */

@Component(service = SyThemeFactoryType.class)
public final class SyThemePrimalFactory implements SyThemeFactoryType
{
  /**
   * The "Primal" theme.
   */

  public SyThemePrimalFactory()
  {

  }

  @Override
  public SyThemeType create()
  {
    return new SyThemePrimal();
  }
}
