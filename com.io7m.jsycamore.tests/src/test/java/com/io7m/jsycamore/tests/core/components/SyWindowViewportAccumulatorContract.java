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

package com.io7m.jsycamore.tests.core.components;

import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.components.SyWindowViewportAccumulatorType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyWindowViewportAccumulatorContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyWindowViewportAccumulatorContract.class);
  }

  protected abstract SyWindowViewportAccumulatorType create();

  @Test
  public final void testEmpty()
  {
    final var c = this.create();

    assertEquals(0L, (long) c.minimumX());
    assertEquals(0L, (long) c.minimumY());
    assertEquals(0L, (long) c.maximumX());
    assertEquals(0L, (long) c.maximumY());
  }

  @Test
  public final void testAccumulateAlwaysSmaller()
  {
    final var c = this.create();
    c.reset(Integer.MAX_VALUE, Integer.MAX_VALUE);

    var last_x0 = c.minimumX();
    var last_y0 = c.minimumY();
    var last_x1 = c.maximumX();
    var last_y1 = c.maximumY();

    final var bound = (int) StrictMath.pow(2.0, 28.0);
    final var rand = new Random(0L);
    for (var index = 0; index < 1000; ++index) {
      final var x = rand.nextInt(1000);
      final var y = rand.nextInt(1000);
      final var w = rand.nextInt(bound);
      final var h = rand.nextInt(bound);

      c.accumulate(PAreasI.create(x, y, w, h));

      final var new_x0 = c.minimumX();
      final var new_y0 = c.minimumY();
      final var new_x1 = c.maximumX();
      final var new_y1 = c.maximumY();

      LOG.debug(
        "index: {}", Integer.valueOf(index));
      LOG.debug(
        "x0: {} >= {}", Integer.valueOf(new_x0), Integer.valueOf(last_x0));
      LOG.debug(
        "y0: {} >= {}", Integer.valueOf(new_y0), Integer.valueOf(last_y0));
      LOG.debug(
        "x1: {} <= {}", Integer.valueOf(new_x1), Integer.valueOf(last_x1));
      LOG.debug(
        "y1: {} <= {}", Integer.valueOf(new_y1), Integer.valueOf(last_y1));

      assertTrue(new_x0 >= last_x0);
      assertTrue(new_y0 >= last_y0);
      assertTrue(new_x1 <= last_x1);
      assertTrue(new_y1 <= last_y1);

      last_x0 = new_x0;
      last_y0 = new_y0;
      last_x1 = new_x1;
      last_y1 = new_y1;
    }
  }

  @Test
  public final void testRestoreEmpty()
  {
    final var c = this.create();
    c.reset(1000, 500);
    assertEquals(0L, (long) c.minimumX());
    assertEquals(0L, (long) c.minimumY());
    assertEquals(1000L, (long) c.maximumX());
    assertEquals(500L, (long) c.maximumY());

    c.restore();
    assertEquals(0L, (long) c.minimumX());
    assertEquals(0L, (long) c.minimumY());
    assertEquals(1000L, (long) c.maximumX());
    assertEquals(500L, (long) c.maximumY());
  }

  @Test
  public final void testAccumulate()
  {
    final var c = this.create();
    c.reset(1000, 500);
    final var previous = c.toString();

    {
      final var width = (long) (c.maximumX() - c.minimumX());
      final var height = (long) (c.maximumY() - c.minimumY());
      assertEquals(0L, (long) c.minimumX());
      assertEquals(0L, (long) c.minimumY());
      assertEquals(1000L, (long) c.maximumX());
      assertEquals(500L, (long) c.maximumY());
      assertEquals(1000L, width);
      assertEquals(500L, height);
      assertEquals(previous, c.toString());
    }

    {
      c.accumulate(PAreasI.create(10, 15, 20, 30));
      final var width = (long) (c.maximumX() - c.minimumX());
      final var height = (long) (c.maximumY() - c.minimumY());
      assertEquals(10L, (long) c.minimumX());
      assertEquals(15L, (long) c.minimumY());
      assertEquals(30L, (long) c.maximumX());
      assertEquals(45L, (long) c.maximumY());
      assertEquals(20L, width);
      assertEquals(30L, height);
      assertNotEquals(previous, c.toString());
    }

    {
      c.accumulate(PAreasI.create(0, 0, 10, 20));
      final var width = (long) (c.maximumX() - c.minimumX());
      final var height = (long) (c.maximumY() - c.minimumY());
      assertEquals(10L, (long) c.minimumX());
      assertEquals(15L, (long) c.minimumY());
      assertEquals(20L, (long) c.maximumX());
      assertEquals(35L, (long) c.maximumY());
      assertEquals(10L, width);
      assertEquals(20L, height);
      assertNotEquals(previous, c.toString());
    }

    {
      c.restore();
      final var width = (long) (c.maximumX() - c.minimumX());
      final var height = (long) (c.maximumY() - c.minimumY());
      assertEquals(10L, (long) c.minimumX());
      assertEquals(15L, (long) c.minimumY());
      assertEquals(30L, (long) c.maximumX());
      assertEquals(45L, (long) c.maximumY());
      assertEquals(20L, width);
      assertEquals(30L, height);
      assertNotEquals(previous, c.toString());
    }

    {
      c.restore();
      final var width = (long) (c.maximumX() - c.minimumX());
      final var height = (long) (c.maximumY() - c.minimumY());
      assertEquals(0L, (long) c.minimumX());
      assertEquals(0L, (long) c.minimumY());
      assertEquals(1000L, (long) c.maximumX());
      assertEquals(500L, (long) c.maximumY());
      assertEquals(1000L, width);
      assertEquals(500L, height);
      assertEquals(previous, c.toString());
    }
  }
}
