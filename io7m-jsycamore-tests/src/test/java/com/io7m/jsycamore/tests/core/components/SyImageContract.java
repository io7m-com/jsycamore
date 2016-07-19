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

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyButtonListenerType;
import com.io7m.jsycamore.core.components.SyButtonState;
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.components.SyImageType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.parameterized.PVectorI2I;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SyImageContract
{
  protected abstract SyImageType create();

  @Test
  public final void testIdentities()
  {
    final SyImageType image = this.create();

    for (final SyAlignmentHorizontal a : SyAlignmentHorizontal.values()) {
      image.setImageAlignmentHorizontal(a);
      Assert.assertEquals(a, image.imageAlignmentHorizontal());
    }

    for (final SyAlignmentVertical a : SyAlignmentVertical.values()) {
      image.setImageAlignmentVertical(a);
      Assert.assertEquals(a, image.imageAlignmentVertical());
    }

    final SyImageSpecification spec = SyImageSpecification.of(
      "x",
      32,
      32,
      SyImageFormat.IMAGE_FORMAT_RGB_565,
      new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    image.setImage(spec);
    Assert.assertEquals(spec, image.image());
  }
}
