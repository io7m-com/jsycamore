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

import com.io7m.jsycamore.core.themes.SyThemePanelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of the {@link SyPanelType} interface.
 */

public final class SyPanel extends SyPanelAbstract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyPanel.class);
  }

  private SyPanel()
  {
    super(() -> true);
  }

  /**
   * @return A new panel
   */

  public static SyPanelType create()
  {
    return new SyPanel();
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[SyPanel 0x");
    sb.append(Integer.toHexString(this.hashCode()));
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
  public SyThemePanelType theme()
  {
    return this.windowTheme().panelTheme();
  }
}
