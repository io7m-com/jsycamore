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

package com.io7m.jsycamore.tests.core;

import com.io7m.jsycamore.core.SyThemeAlignment;
import com.io7m.jsycamore.core.SyThemeEmboss;
import com.io7m.jsycamore.core.SyThemeMutable;
import com.io7m.jsycamore.core.SyThemeWindow;
import com.io7m.jsycamore.core.SyThemeWindowMargin;
import com.io7m.jsycamore.core.SyThemeWindowMarginCorner;
import com.io7m.jsycamore.core.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.SyWindow;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererAWT;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jtensors.VectorI3F;
import com.io7m.junreachable.UnreachableCodeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import java.util.OptionalInt;

public final class Demo
{
  private Demo()
  {
    throw new UnreachableCodeException();
  }

  public static void main(final String[] args)
    throws Exception
  {
    final BufferedImage bi = new BufferedImage(
      320 + 32, 240 + 32, BufferedImage.TYPE_4BYTE_ABGR_PRE);

    final SyThemeMutable theme = SyThemeMutable.create();
    theme.setMainBackgroundColor(new VectorI3F(0.8f, 0.8f, 0.8f));

    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(
      new VectorI3F(0.95f, 0.95f, 0.95f));
    theme_titlebar_emboss_active_b.setColorLeft(
      new VectorI3F(0.90f, 0.90f, 0.90f));

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setHeight(18);
    theme_titlebar_b.setColorActive(new VectorI3F(0.8f, 0.8f, 0.8f));
    theme_titlebar_b.setEmbossActive(
      Optional.of(theme_titlebar_emboss_active_b.build()));
    theme_titlebar_b.setTextAlignment(SyThemeAlignment.ALIGN_CENTER);

    final SyThemeEmboss.Builder theme_margin_emboss_active_b =
      SyThemeEmboss.builder();
    theme_margin_emboss_active_b.setSize(1);
    theme_margin_emboss_active_b.setColorTop(
      new VectorI3F(0.95f, 0.95f, 0.95f));
    theme_margin_emboss_active_b.setColorLeft(
      new VectorI3F(0.90f, 0.90f, 0.90f));

    final SyThemeWindowMargin.Builder theme_margin_b =
      SyThemeWindowMargin.builder();
    theme_margin_b.setBottomHeight(5);
    theme_margin_b.setTopHeight(4);
    theme_margin_b.setLeftWidth(4);
    theme_margin_b.setRightWidth(4);

    theme_margin_b.setTopLeftStyle(
      SyThemeWindowMarginCorner.MARGIN_CORNER_NONE);
    theme_margin_b.setTopRightStyle(
      SyThemeWindowMarginCorner.MARGIN_CORNER_NONE);
    theme_margin_b.setBottomLeftStyle(
      SyThemeWindowMarginCorner.MARGIN_CORNER_NONE);
    theme_margin_b.setBottomRightStyle(
      SyThemeWindowMarginCorner.MARGIN_CORNER_NONE);

    theme_margin_b.setEmbossActive(
      Optional.of(theme_margin_emboss_active_b.build()));

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_margin_b.build()));

    final SyWindowType win = SyWindow.create(theme);
    win.setSize(320, 240);
    win.setActive(true);
    win.setText("File Manager - sys$starlet_c");

    final SyWindowRendererType<BufferedImage, BufferedImage> r =
      SyWindowRendererAWT.create();

    r.render(bi, win);
    ImageIO.write(bi, "PNG", new File("/tmp/window.png"));
  }
}
