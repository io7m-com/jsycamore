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

package com.io7m.jsycamore.tests.core.themes;

import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeTitleBarElement;
import com.io7m.jsycamore.themes.bee.SyThemeBee;
import com.io7m.jsycamore.themes.bee.SyThemeBeeSpecification;
import com.io7m.jsycamore.themes.fenestra.SyThemeFenestra;
import com.io7m.jsycamore.themes.fenestra.SyThemeFenestraSpecification;
import com.io7m.jsycamore.themes.motive.SyThemeMotive;
import com.io7m.jsycamore.themes.motive.SyThemeMotiveSpecification;
import com.io7m.jsycamore.themes.stride.SyThemeStride;
import com.io7m.jsycamore.themes.stride.SyThemeStrideSpecification;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public final class SyThemeTest
{
  private static void checkComparator(final Comparator<SyThemeTitleBarElement> order)
  {
    for (final SyThemeTitleBarElement x : SyThemeTitleBarElement.values()) {
      for (final SyThemeTitleBarElement y : SyThemeTitleBarElement.values()) {
        if (x == y) {
          Assert.assertEquals(0L, (long) order.compare(x, y));
          Assert.assertEquals(0L, (long) order.compare(y, x));
        } else {
          final int acmp = order.compare(x, y);
          Assert.assertNotEquals(0L, (long) acmp);
          final int bcmp = order.compare(y, x);
          Assert.assertEquals((long) acmp, (long) (0 - bcmp));
        }
      }
    }
  }

  @Test
  public void testInstantiateMotive()
  {
    final SyTheme t =
      SyThemeMotive.builder().build();
    final SyTheme u =
      SyThemeMotive.builderFrom(SyThemeMotiveSpecification.builder().build()).build();
    Assert.assertEquals(t, u);
  }

  @Test
  public void testSortingMotive()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    SyThemeTest.checkComparator(t.windowTheme().titleBar().elementOrder());
  }

  @Test
  public void testInstantiateFenestra()
  {
    final SyTheme t =
      SyThemeFenestra.builder().build();
    final SyTheme u =
      SyThemeFenestra.builderFrom(SyThemeFenestraSpecification.builder().build()).build();
    Assert.assertEquals(t, u);
  }

  @Test
  public void testSortingFenestra()
  {
    final SyTheme t = SyThemeFenestra.builder().build();
    SyThemeTest.checkComparator(t.windowTheme().titleBar().elementOrder());
  }

  @Test
  public void testInstantiateStride()
  {
    final SyTheme t =
      SyThemeStride.builder().build();
    final SyTheme u =
      SyThemeStride.builderFrom(SyThemeStrideSpecification.builder().build()).build();
    Assert.assertEquals(t, u);
  }

  @Test
  public void testSortingStride()
  {
    final SyTheme t = SyThemeStride.builder().build();
    SyThemeTest.checkComparator(t.windowTheme().titleBar().elementOrder());
  }

  @Test
  public void testInstantiateBee()
  {
    final SyTheme t =
      SyThemeBee.builder().build();
    final SyTheme u =
      SyThemeBee.builderFrom(SyThemeBeeSpecification.builder().build()).build();
    Assert.assertEquals(t, u);
  }

  @Test
  public void testSortingBee()
  {
    final SyTheme t = SyThemeBee.builder().build();
    SyThemeTest.checkComparator(t.windowTheme().titleBar().elementOrder());
  }
}
