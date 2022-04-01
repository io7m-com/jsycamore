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


package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jsycamore.components.standard.SyAlign;
import com.io7m.jsycamore.components.standard.SyAlignmentHorizontal;
import com.io7m.jsycamore.components.standard.SyAlignmentVertical;
import com.io7m.jsycamore.components.standard.SyImageView;
import com.io7m.jsycamore.components.standard.SyLayoutMargin;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.WINDOW_BUTTON_CLOSE_ICON;

/**
 * The base type of window buttons that have icons.
 */

public abstract class SyWindowButtonWithIcon extends SyWindowButtonComponent
{
  private final SyLayoutMargin margin;
  private final SyAlign align;
  private final SyImageView image;

  /**
   * A window button with an icon.
   *
   * @param component     The classifier for the component
   * @param imageResource The image resource name
   */

  public SyWindowButtonWithIcon(
    final SyWindowDecorationComponent component,
    final String imageResource)
  {
    super(component, List.of());

    this.margin = new SyLayoutMargin();
    this.margin.setPaddingAll(8);

    this.align = new SyAlign();
    this.align.alignmentHorizontal()
      .set(SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER);
    this.align.alignmentVertical()
      .set(SyAlignmentVertical.ALIGN_VERTICAL_CENTER);

    this.image = new SyImageView(List.of(WINDOW_BUTTON_CLOSE_ICON));
    this.image.imageURI()
      .set(Optional.of(URI.create(imageResource)));

    this.align.childAdd(this.image);
    this.margin.childAdd(this.align);
    this.childAdd(this.margin);

    this.margin.setMouseQueryAccepting(false);
    this.align.setMouseQueryAccepting(false);
    this.image.setMouseQueryAccepting(false);
  }
}
