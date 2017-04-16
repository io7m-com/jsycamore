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

import com.io7m.jfunctional.Unit;
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.components.SyImageType;
import com.io7m.jsycamore.images.api.SyImageFormat;
import com.io7m.jsycamore.images.api.SyImageScaleInterpolation;
import com.io7m.jsycamore.images.api.SyImageSpecification;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SyImageContract extends SyComponentContract
{
  @Override
  protected abstract SyImageType create();

  @Test
  public void testWindowlessTheme()
  {
    final SyImageType c = this.create();
    Assert.assertFalse(c.window().isPresent());
    Assert.assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final SyImageType image = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    image.matchComponent(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, panel) -> {
        throw new UnreachableCodeException();
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, b_image) -> {
        called.set(true);
        return Unit.unit();
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final SyImageType image = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    image.matchComponentReadable(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, panel) -> {
        throw new UnreachableCodeException();
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, b_image) -> {
        called.set(true);
        return Unit.unit();
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    Assert.assertTrue(called.get());
  }

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
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    image.setImage(spec);
    Assert.assertEquals(spec, image.image());
  }
}
