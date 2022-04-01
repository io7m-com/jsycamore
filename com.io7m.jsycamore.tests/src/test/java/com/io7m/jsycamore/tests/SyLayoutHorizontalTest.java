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
import com.io7m.jsycamore.components.standard.SyLayoutHorizontal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyLayoutHorizontalTest extends SyComponentContract<SyLayoutHorizontal>
{
  @BeforeEach
  public void hzSetup()
  {

  }

  /**
   * A simple horizontal layout works.
   */

  @Test
  public void testSimpleLayout()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(0);

    final var b0 = new SyBlob(100, 300);
    final var b1 = new SyBlob(100, 300);
    final var b2 = new SyBlob(100, 300);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(0, b0.position().get().x());
    assertEquals(0, b0.position().get().y());
    assertEquals(100, b0.size().get().sizeX());
    assertEquals(300, b0.size().get().sizeY());

    assertEquals(100, b1.position().get().x());
    assertEquals(0, b1.position().get().y());
    assertEquals(100, b1.size().get().sizeX());
    assertEquals(300, b1.size().get().sizeY());

    assertEquals(200, b2.position().get().x());
    assertEquals(0, b2.position().get().y());
    assertEquals(100, b2.size().get().sizeX());
    assertEquals(300, b2.size().get().sizeY());
  }

  /**
   * A simple padded horizontal layout works.
   */

  @Test
  public void testSimpleLayoutPadded()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(10);

    final var b0 = new SyBlob(100, 300);
    final var b1 = new SyBlob(100, 300);
    final var b2 = new SyBlob(100, 300);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(0, b0.position().get().x());
    assertEquals(0, b0.position().get().y());
    assertEquals(95, b0.size().get().sizeX());
    assertEquals(300, b0.size().get().sizeY());

    assertEquals(105, b1.position().get().x());
    assertEquals(0, b1.position().get().y());
    assertEquals(90, b1.size().get().sizeX());
    assertEquals(300, b1.size().get().sizeY());

    assertEquals(205, b2.position().get().x());
    assertEquals(0, b2.position().get().y());
    assertEquals(95, b2.size().get().sizeX());
    assertEquals(300, b2.size().get().sizeY());
  }

  /**
   * A horizontal layout doesn't accept window events.
   */

  @Test
  public void testWindowEvents()
  {
    final var c = this.newComponent();
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyWindowClosed(new SyWindowID(UUID.randomUUID())))
    );
  }

  @Override
  protected SyLayoutHorizontal newComponent()
  {
    return new SyLayoutHorizontal();
  }
}
