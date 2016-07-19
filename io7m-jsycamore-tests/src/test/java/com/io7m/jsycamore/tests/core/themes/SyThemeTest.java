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

import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jsycamore.core.themes.provided.SyThemeBeeSpecification;
import com.io7m.jsycamore.core.themes.provided.SyThemeFenestra;
import com.io7m.jsycamore.core.themes.provided.SyThemeFenestraSpecification;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotiveSpecification;
import com.io7m.jsycamore.core.themes.provided.SyThemeStride;
import com.io7m.jsycamore.core.themes.provided.SyThemeStrideSpecification;
import org.junit.Assert;
import org.junit.Test;

public final class SyThemeTest
{
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
  public void testInstantiateFenestra()
  {
    final SyTheme t =
      SyThemeFenestra.builder().build();
    final SyTheme u =
      SyThemeFenestra.builderFrom(SyThemeFenestraSpecification.builder().build()).build();
    Assert.assertEquals(t, u);
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
  public void testInstantiateBee()
  {
    final SyTheme t =
      SyThemeBee.builder().build();
    final SyTheme u =
      SyThemeBee.builderFrom(SyThemeBeeSpecification.builder().build()).build();
    Assert.assertEquals(t, u);
  }
}
