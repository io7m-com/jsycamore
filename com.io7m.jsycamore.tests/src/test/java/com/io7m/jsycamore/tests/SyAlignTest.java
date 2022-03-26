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
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.components.standard.SyAlign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.io7m.jsycamore.api.components.SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER;
import static com.io7m.jsycamore.api.components.SyAlignmentHorizontal.ALIGN_HORIZONTAL_LEFT;
import static com.io7m.jsycamore.api.components.SyAlignmentHorizontal.ALIGN_HORIZONTAL_RIGHT;
import static com.io7m.jsycamore.api.components.SyAlignmentVertical.ALIGN_VERTICAL_BOTTOM;
import static com.io7m.jsycamore.api.components.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;
import static com.io7m.jsycamore.api.components.SyAlignmentVertical.ALIGN_VERTICAL_TOP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyAlignTest
{
  private SyLayoutContextType layoutContext;

  @BeforeEach
  public void setup()
  {
    this.layoutContext = Mockito.mock(SyLayoutContextType.class);
  }

  @Test
  public void testLayoutVTopHLeft()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_TOP);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_LEFT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());
  }

  @Test
  public void testLayoutVTopHRight()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_TOP);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_RIGHT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(128 - 32, c.position().get().x());
    assertEquals(0, c.position().get().y());
  }

  @Test
  public void testLayoutVTopHCenter()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_TOP);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_CENTER);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals((128 / 2) - (32 / 2), c.position().get().x());
    assertEquals(0, c.position().get().y());
  }

  @Test
  public void testLayoutVCenterHLeft()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_LEFT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(0, c.position().get().x());
    assertEquals((128 / 2) - (32 / 2), c.position().get().y());
  }

  @Test
  public void testLayoutVCenterHRight()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_RIGHT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(128 - 32, c.position().get().x());
    assertEquals((128 / 2) - (32 / 2), c.position().get().y());
  }

  @Test
  public void testLayoutVCenterHCenter()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_CENTER);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals((128 / 2) - (32 / 2), c.position().get().x());
    assertEquals((128 / 2) - (32 / 2), c.position().get().y());
  }

  @Test
  public void testLayoutVBottomHLeft()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_BOTTOM);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_LEFT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);
    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(0, c.position().get().x());
    assertEquals(128 - 32, c.position().get().y());
  }

  @Test
  public void testLayoutVBottomHRight()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_BOTTOM);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_RIGHT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);

    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(128 - 32, c.position().get().x());
    assertEquals(128 - 32, c.position().get().y());
  }

  @Test
  public void testLayoutVBottomHCenter()
  {
    final var c = new SyBlob();
    c.setPreferredSizeY(32);
    c.setPreferredSizeX(32);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var a = new SyAlign();
    a.alignmentVertical().set(ALIGN_VERTICAL_BOTTOM);
    a.alignmentHorizontal().set(ALIGN_HORIZONTAL_RIGHT);
    a.childAdd(c);

    assertEquals(0, c.position().get().x());
    assertEquals(0, c.position().get().y());

    final var constraints =
      new SyConstraints(0, 0, 128, 128);

    a.layout(this.layoutContext, constraints);

    final var size = c.size().get();
    assertEquals(32, size.sizeX());
    assertEquals(32, size.sizeY());
    assertEquals(128 - 32, c.position().get().x());
    assertEquals(128 - 32, c.position().get().y());
  }
}
