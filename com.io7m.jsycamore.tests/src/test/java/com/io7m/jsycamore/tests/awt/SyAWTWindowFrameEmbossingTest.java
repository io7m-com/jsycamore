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

package com.io7m.jsycamore.tests.awt;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.awt.SyAWTWindowFrameEmbossing;
import com.io7m.jsycamore.awt.SyAWTWindowRendererEmbossedFrameParameters;
import com.io7m.jsycamore.themes.motive.SyThemeMotiveProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public final class SyAWTWindowFrameEmbossingTest
{
  @Test
  public void testMotiveEmbossing()
  {
    final SyTheme theme =
      new SyThemeMotiveProvider().theme();

    final PAreaI<SySpaceParentRelativeType> frame_box =
      PAreasI.create(16, 16, 500, 500);

    final SyAWTWindowRendererEmbossedFrameParameters params =
      SyAWTWindowFrameEmbossing.renderFrameEmbossedActualFrameCalculateParameters(
        theme.windowTheme().titleBar(),
        theme.windowTheme().frame(),
        theme.windowTheme().frame().embossActive().get(),
        Optional.empty(),
        frame_box);

    Assert.assertEquals(21L, (long) params.bottomRightArmLength());
    Assert.assertEquals(21L, (long) params.bottomLeftArmLength());
    Assert.assertEquals(21L, (long) params.topRightArmLength());
    Assert.assertEquals(21L, (long) params.topLeftArmLength());
  }
}
