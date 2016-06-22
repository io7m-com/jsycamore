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

import com.io7m.jsycamore.core.SyThemeEmboss;
import com.io7m.jsycamore.core.SyThemeMutable;
import com.io7m.jsycamore.core.SyThemeWindow;
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
      800, 600, BufferedImage.TYPE_4BYTE_ABGR_PRE);

    final SyThemeMutable theme = SyThemeMutable.create();

    theme.setMainBackgroundColor(new VectorI3F(0.3f, 0.3f, 0.3f));
    theme.setWindowTheme(SyThemeWindow.of(SyThemeWindowTitleBar.of(16, VectorI3F.ZERO, VectorI3F.ZERO)));

    final SyWindowType win = SyWindow.create(theme);
    win.setSize(640, 480);

    final SyWindowRendererType<BufferedImage, BufferedImage> r =
      SyWindowRendererAWT.create();

    r.render(bi, win);
    ImageIO.write(bi, "PNG", new File("/tmp/window.png"));
  }
}
