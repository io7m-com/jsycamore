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
import com.io7m.jsycamore.components.standard.SyPackVertical;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.components.SyResizeBehaviour.FILL_SPACE;
import static com.io7m.jsycamore.api.components.SyResizeBehaviour.PRESERVE;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_LEFT;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyPackVerticalTest extends SyComponentContract<SyPackVertical>
{
  @BeforeEach
  public void vzSetup()
  {

  }

  /**
   * A simple vertical layout works.
   */

  @Test
  public void testSimpleLayout()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(0);

    final var b0 = new SyBlob(this.screen(), 300, 100);
    final var b1 = new SyBlob(this.screen(), 300, 100);
    final var b2 = new SyBlob(this.screen(), 300, 100);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(0, b0.position().get().x());
    assertEquals(0, b0.position().get().y());
    assertEquals(300, b0.size().get().sizeX());
    assertEquals(100, b0.size().get().sizeY());

    assertEquals(0, b1.position().get().x());
    assertEquals(100, b1.position().get().y());
    assertEquals(300, b1.size().get().sizeX());
    assertEquals(100, b1.size().get().sizeY());

    assertEquals(0, b2.position().get().x());
    assertEquals(200, b2.position().get().y());
    assertEquals(300, b2.size().get().sizeX());
    assertEquals(100, b2.size().get().sizeY());
  }

  /**
   * Centering works.
   */

  @Test
  public void testCenter()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(0);
    c.childSizeXBehaviour().set(PRESERVE);
    c.alignHorizontal().set(ALIGN_HORIZONTAL_CENTER);

    final var b0 = new SyBlob(this.screen(), 32, 32);
    final var b1 = new SyBlob(this.screen(), 32, 32);
    final var b2 = new SyBlob(this.screen(), 32, 32);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals((300 / 2) - (32 / 2), b0.position().get().x());
    assertEquals((300 / 2) - (32 / 2), b1.position().get().x());
    assertEquals((300 / 2) - (32 / 2), b2.position().get().x());

    assertEquals(0, b0.position().get().y());
    assertEquals(32, b1.position().get().y());
    assertEquals(64, b2.position().get().y());

    assertEquals(32, b0.size().get().sizeX());
    assertEquals(32, b1.size().get().sizeX());
    assertEquals(32, b2.size().get().sizeX());

    assertEquals(32, b0.size().get().sizeY());
    assertEquals(32, b1.size().get().sizeY());
    assertEquals(32, b2.size().get().sizeY());

    assertEquals(32 * 3, c.size().get().sizeY());
    assertEquals(32, c.size().get().sizeX());
  }

  /**
   * Left aligning works.
   */

  @Test
  public void testLeft()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(0);
    c.childSizeXBehaviour().set(PRESERVE);
    c.alignHorizontal().set(ALIGN_HORIZONTAL_LEFT);

    final var b0 = new SyBlob(this.screen(), 32, 32);
    final var b1 = new SyBlob(this.screen(), 32, 32);
    final var b2 = new SyBlob(this.screen(), 32, 32);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(0, b0.position().get().x());
    assertEquals(0, b1.position().get().x());
    assertEquals(0, b2.position().get().x());

    assertEquals(0, b0.position().get().y());
    assertEquals(32, b1.position().get().y());
    assertEquals(64, b2.position().get().y());

    assertEquals(32, b0.size().get().sizeX());
    assertEquals(32, b1.size().get().sizeX());
    assertEquals(32, b2.size().get().sizeX());

    assertEquals(32, b0.size().get().sizeY());
    assertEquals(32, b1.size().get().sizeY());
    assertEquals(32, b2.size().get().sizeY());

    assertEquals(32 * 3, c.size().get().sizeY());
    assertEquals(32, c.size().get().sizeX());
  }

  /**
   * Right aligning works.
   */

  @Test
  public void testRight()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(0);
    c.childSizeXBehaviour().set(PRESERVE);
    c.alignHorizontal().set(ALIGN_HORIZONTAL_RIGHT);

    final var b0 = new SyBlob(this.screen(), 32, 32);
    final var b1 = new SyBlob(this.screen(), 32, 32);
    final var b2 = new SyBlob(this.screen(), 32, 32);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(300 - 32, b0.position().get().x());
    assertEquals(300 - 32, b1.position().get().x());
    assertEquals(300 - 32, b2.position().get().x());

    assertEquals(0, b0.position().get().y());
    assertEquals(32, b1.position().get().y());
    assertEquals(64, b2.position().get().y());

    assertEquals(32, b0.size().get().sizeX());
    assertEquals(32, b1.size().get().sizeX());
    assertEquals(32, b2.size().get().sizeX());

    assertEquals(32, b0.size().get().sizeY());
    assertEquals(32, b1.size().get().sizeY());
    assertEquals(32, b2.size().get().sizeY());

    assertEquals(32 * 3, c.size().get().sizeY());
    assertEquals(32, c.size().get().sizeX());
  }

  /**
   * Filling horizontal space works.
   */

  @Test
  public void testFillSpace()
  {
    final var c = this.newComponent();
    c.paddingBetween().set(0);
    c.childSizeXBehaviour().set(FILL_SPACE);
    c.alignHorizontal().set(ALIGN_HORIZONTAL_LEFT);

    final var b0 = new SyBlob(this.screen(), 32, 32);
    final var b1 = new SyBlob(this.screen(), 32, 32);
    final var b2 = new SyBlob(this.screen(), 32, 32);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(0, b0.position().get().x());
    assertEquals(0, b1.position().get().x());
    assertEquals(0, b2.position().get().x());

    assertEquals(0, b0.position().get().y());
    assertEquals(32, b1.position().get().y());
    assertEquals(64, b2.position().get().y());

    assertEquals(300, b0.size().get().sizeX());
    assertEquals(300, b1.size().get().sizeX());
    assertEquals(300, b2.size().get().sizeX());

    assertEquals(32, b0.size().get().sizeY());
    assertEquals(32, b1.size().get().sizeY());
    assertEquals(32, b2.size().get().sizeY());

    assertEquals(32 * 3, c.size().get().sizeY());
    assertEquals(300, c.size().get().sizeX());
  }

  @Override
  protected SyPackVertical newComponent()
  {
    return new SyPackVertical(this.screen());
  }
}
