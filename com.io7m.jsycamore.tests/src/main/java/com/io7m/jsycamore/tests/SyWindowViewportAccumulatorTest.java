/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyWindowViewportAccumulatorTest
{
  @Test
  public void testNegativeOffset()
  {
    final var accum =
      SyWindowViewportAccumulator.create();

    accum.reset(300, 300);
    accum.accumulate(PAreasI.create(-100, -100, 200, 200));

    assertEquals(-100, accum.minimumX());
    assertEquals(-100, accum.minimumY());
    assertEquals(100, accum.maximumX());
    assertEquals(100, accum.maximumY());
  }

  @Test
  public void testOffsetSequence()
  {
    final var accum =
      SyWindowViewportAccumulator.create();

    accum.reset(300, 300);

    assertEquals(0, accum.minimumX());
    assertEquals(0, accum.minimumY());
    assertEquals(300, accum.maximumX());
    assertEquals(300, accum.maximumY());

    accum.accumulate(PAreasI.create(0, 0, 200, 200));

    assertEquals(0, accum.minimumX());
    assertEquals(0, accum.minimumY());
    assertEquals(200, accum.maximumX());
    assertEquals(200, accum.maximumY());

    accum.accumulate(PAreasI.create(50, 50, 100, 100));

    assertEquals(50, accum.minimumX());
    assertEquals(50, accum.minimumY());
    assertEquals(150, accum.maximumX());
    assertEquals(150, accum.maximumY());

    accum.restore();

    assertEquals(0, accum.minimumX());
    assertEquals(0, accum.minimumY());
    assertEquals(200, accum.maximumX());
    assertEquals(200, accum.maximumY());

    accum.restore();

    assertEquals(0, accum.minimumX());
    assertEquals(0, accum.minimumY());
    assertEquals(300, accum.maximumX());
    assertEquals(300, accum.maximumY());

    accum.restore();

    assertEquals(0, accum.minimumX());
    assertEquals(0, accum.minimumY());
    assertEquals(300, accum.maximumX());
    assertEquals(300, accum.maximumY());
  }
}
