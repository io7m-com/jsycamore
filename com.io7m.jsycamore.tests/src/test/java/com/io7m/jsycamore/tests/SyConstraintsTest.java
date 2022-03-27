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
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyConstraintsTest
{
  /**
   * The minimum size for a constraint is correct.
   */

  @Test
  public void testMinimum()
  {
    final var c = new SyConstraints(32, 64, 128, 256);
    final var smin = c.sizeMinimum();
    assertEquals(32, smin.sizeX());
    assertEquals(64, smin.sizeY());
  }

  /**
   * The maximum size for a constraint is correct.
   */

  @Test
  public void testMaximum()
  {
    final var c = new SyConstraints(32, 64, 128, 256);
    final var smax = c.sizeMaximum();
    assertEquals(128, smax.sizeX());
    assertEquals(256, smax.sizeY());
  }

  /**
   * The satisfiability check is correct.
   */

  @Property
  public void testIsSatisfiedBy(
    @ForAll @IntRange(min = 0, max = 100000) final int a,
    @ForAll @IntRange(min = 0, max = 100000) final int b,
    @ForAll @IntRange(min = 0, max = 100000) final int c,
    @ForAll @IntRange(min = 0, max = 100000) final int d)
  {
    final var nums = new int[]{a, b, c, d};
    Arrays.sort(nums);

    final var cs = new SyConstraints(nums[0], nums[1], nums[2], nums[3]);
    final var s0 = PAreaSizeI.of(nums[2], nums[3]);
    final var s1 = PAreaSizeI.of(nums[0], nums[1]);
    assertTrue(cs.isSatisfiedBy(s0));
    assertTrue(cs.isSatisfiedBy(s1));
  }
}
