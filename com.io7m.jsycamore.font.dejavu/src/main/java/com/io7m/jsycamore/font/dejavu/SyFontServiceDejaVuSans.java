/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.font.dejavu;

import com.io7m.jsycamore.api.services.SyServiceAbstract;
import com.io7m.jsycamore.api.text.SyFontServiceType;
import com.io7m.jsycamore.api.text.SyFontStyle;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.io.InputStream;

import static com.io7m.jsycamore.api.text.SyFontStyle.REGULAR;

/**
 * The DejaVu Sans font.
 */

@Component
public final class SyFontServiceDejaVuSans
  extends SyServiceAbstract
  implements SyFontServiceType
{
  /**
   * The DejaVu Sans font.
   */

  public SyFontServiceDejaVuSans()
  {

  }

  @Override
  public String family()
  {
    return "DejaVu Sans";
  }

  @Override
  public SyFontStyle style()
  {
    return REGULAR;
  }

  @Override
  public InputStream openStream()
    throws IOException
  {
    final var url = SyFontServiceDejaVuSans.class.getResource(
      "/com/io7m/jsycamore/font/dejavu/DejaVuSans.ttf");
    if (url == null) {
      throw new IOException("Unable to open font file!");
    }
    return url.openStream();
  }

  @Override
  public String description()
  {
    return "DejaVu Sans font service.";
  }
}
