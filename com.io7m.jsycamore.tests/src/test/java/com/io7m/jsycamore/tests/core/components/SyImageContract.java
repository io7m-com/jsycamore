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

import com.io7m.jsycamore.api.components.SyImageType;
import com.io7m.jsycamore.api.images.SyImageFormat;
import com.io7m.jsycamore.api.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.api.themes.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.themes.SyAlignmentVertical;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyImageContract extends SyComponentContract
{
  @Override
  protected abstract SyImageType create();

  @Test
  public void testWindowlessTheme()
  {
    final var c = this.create();
    assertFalse(c.window().isPresent());
    assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final var image = this.create();
    final var called = new AtomicBoolean(false);

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
        return Void.class;
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final var image = this.create();
    final var called = new AtomicBoolean(false);

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
        return Void.class;
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    assertTrue(called.get());
  }

  @Test
  public final void testIdentities()
  {
    final var image = this.create();

    for (final var a : SyAlignmentHorizontal.values()) {
      image.setImageAlignmentHorizontal(a);
      assertEquals(a, image.imageAlignmentHorizontal());
    }

    for (final var a : SyAlignmentVertical.values()) {
      image.setImageAlignmentVertical(a);
      assertEquals(a, image.imageAlignmentVertical());
    }

    final var spec = SyImageSpecification.of(
      URI.create("x"),
      32,
      32,
      SyImageFormat.IMAGE_FORMAT_RGB_565,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    image.setImage(spec);
    assertEquals(spec, image.image());
  }
}
