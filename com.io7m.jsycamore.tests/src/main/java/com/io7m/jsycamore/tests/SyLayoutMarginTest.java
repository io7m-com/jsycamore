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


package com.io7m.jsycamore.tests;

import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.SyLayoutMargin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyLayoutMarginTest extends SyComponentContract<SyLayoutMargin>
{
  @BeforeEach
  public void marginSetup()
  {

  }

  /**
   * An 8 pixel margin works.
   */

  @Test
  public void testMargin8()
  {
    final var c = this.newComponent();
    c.setPaddingAll(8);

    final var b = new SyBlob(this.screen());
    b.setPreferredSizeX(128);
    b.setPreferredSizeY(128);
    c.childAdd(b);

    final var cs = new SyConstraints(0, 0, 128, 128);
    c.layout(this.layoutContext, cs);

    assertEquals(8, b.position().get().x());
    assertEquals(8, b.position().get().y());
    assertEquals(128 - (8 * 2), b.size().get().sizeX());
    assertEquals(128 - (8 * 2), b.size().get().sizeY());
  }

  /**
   * A 0 pixel margin works.
   */

  @Test
  public void testMargin0()
  {
    final var c = this.newComponent();
    c.setPaddingAll(0);

    final var b = new SyBlob(this.screen());
    b.setPreferredSizeX(128);
    b.setPreferredSizeY(128);
    c.childAdd(b);

    final var cs = new SyConstraints(0, 0, 128, 128);
    c.layout(this.layoutContext, cs);

    assertEquals(0, b.position().get().x());
    assertEquals(0, b.position().get().y());
    assertEquals(128, b.size().get().sizeX());
    assertEquals(128, b.size().get().sizeY());
  }

  /**
   * A massive pixel margin does not break.
   */

  @Test
  public void testMarginTooLarge()
  {
    final var c = this.newComponent();
    c.setPaddingAll(256);

    final var b = new SyBlob(this.screen());
    b.setPreferredSizeX(128);
    b.setPreferredSizeY(128);
    c.childAdd(b);

    final var cs = new SyConstraints(0, 0, 128, 128);
    c.layout(this.layoutContext, cs);

    assertEquals(256, b.position().get().x());
    assertEquals(256, b.position().get().y());
    assertEquals(0, b.size().get().sizeX());
    assertEquals(0, b.size().get().sizeY());
  }

  @Override
  protected SyLayoutMargin newComponent()
  {
    return new SyLayoutMargin(this.screen());
  }
}
