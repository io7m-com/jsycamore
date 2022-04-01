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
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.components.standard.SyLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyLimitTest extends SyComponentContract<SyLimit>
{
  private SyLayoutContextType layoutContext;

  @BeforeEach
  public void setup()
  {
    this.layoutContext = Mockito.mock(SyLayoutContextType.class);
  }

  @Override
  protected SyLimit newComponent()
  {
    return new SyLimit();
  }

  @Test
  public void testLimitSimple()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyLimit();
    a.limitSize().set(PAreaSizeI.of(16, 16));
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(16, size.sizeX());
    assertEquals(16, size.sizeY());
    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());
  }

  @Test
  public void testLimitConvenience0()
  {
    final var a = new SyLimit(PAreaSizeI.of(16, 16));
    final var size = a.limitSize().get();
    assertEquals(16, size.sizeX());
    assertEquals(16, size.sizeY());
  }
}
