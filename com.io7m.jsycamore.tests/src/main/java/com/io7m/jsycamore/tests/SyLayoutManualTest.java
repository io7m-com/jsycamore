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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.SyLayoutManual;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyLayoutManualTest extends SyComponentContract<SyLayoutManual>
{
  @BeforeEach
  public void vzSetup()
  {

  }

  /**
   * A simple layout works.
   */

  @Test
  public void testSimpleLayout()
  {
    final var c = this.newComponent();

    final var b0 = new SyBlob(this.screen(), 32, 32);
    final var b1 = new SyBlob(this.screen(), 64, 64);
    final var b2 = new SyBlob(this.screen(), 80, 80);
    c.childAdd(b0);
    c.childAdd(b1);
    c.childAdd(b2);

    b0.setPosition(PVector2I.of(10, 10));
    b1.setPosition(PVector2I.of(20, 20));
    b2.setPosition(PVector2I.of(100, 100));

    b0.setSize(PAreaSizeI.of(5, 5));
    b1.setSize(PAreaSizeI.of(10, 10));
    b2.setSize(PAreaSizeI.of(20, 20));

    final var cs = new SyConstraints(0, 0, 300, 300);
    c.layout(this.layoutContext, cs);

    assertEquals(10, b0.position().get().x());
    assertEquals(10, b0.position().get().y());
    assertEquals(5, b0.size().get().sizeX());
    assertEquals(5, b0.size().get().sizeY());

    assertEquals(20, b1.position().get().x());
    assertEquals(20, b1.position().get().y());
    assertEquals(10, b1.size().get().sizeX());
    assertEquals(10, b1.size().get().sizeY());

    assertEquals(100, b2.position().get().x());
    assertEquals(100, b2.position().get().y());
    assertEquals(20, b2.size().get().sizeX());
    assertEquals(20, b2.size().get().sizeY());
  }

  @Override
  protected SyLayoutManual newComponent()
  {
    return new SyLayoutManual(this.screen());
  }
}
