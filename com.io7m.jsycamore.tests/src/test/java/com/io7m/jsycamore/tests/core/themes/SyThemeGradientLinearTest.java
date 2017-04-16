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

import com.io7m.jaffirm.core.PreconditionViolationException;
import com.io7m.jfunctional.Unit;
import com.io7m.jsycamore.api.themes.SyThemeGradientLinear;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SyThemeGradientLinearTest
{
  @Rule public ExpectedException expected = ExpectedException.none();

  @Test
  public void testPreconditionWrongDistributions()
  {
    final SyThemeGradientLinear.Builder b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    this.expected.expect(PreconditionViolationException.class);
    b.build();
  }

  @Test
  public void testPreconditionTooFewColors()
  {
    final SyThemeGradientLinear.Builder b = SyThemeGradientLinear.builder();
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    this.expected.expect(PreconditionViolationException.class);
    b.build();
  }

  @Test
  public void testPreconditionDisorderedDistributions()
  {
    final SyThemeGradientLinear.Builder b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.addColors(Vector3D.of(1.0, 1.0, 1.0));
    b.addDistributions(1.0);
    b.addDistributions(0.0);
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    this.expected.expect(PreconditionViolationException.class);
    b.build();
  }

  @Test
  public void testOK()
  {
    final SyThemeGradientLinear.Builder b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.addColors(Vector3D.of(1.0, 1.0, 1.0));
    b.addDistributions(0.0);
    b.addDistributions(1.0);
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    final SyThemeGradientLinear r = b.build();
    Assert.assertEquals(2L, (long) r.colors().size());
    Assert.assertEquals(Vector3D.of(0.0, 0.0, 0.0), r.colors().get(0));
    Assert.assertEquals(Vector3D.of(1.0, 1.0, 1.0), r.colors().get(1));

    Assert.assertEquals(2L, (long) r.distributions().size());
    Assert.assertEquals(0.0, r.distributions().get(0).floatValue(), 0.0);
    Assert.assertEquals(1.0, r.distributions().get(1).floatValue(), 0.0);

    Assert.assertEquals(Vector2D.of(0.0, 0.0), r.point0());
    Assert.assertEquals(Vector2D.of(1.0, 1.0), r.point1());
  }

  @Test
  public void testMatch()
  {
    final SyThemeGradientLinear.Builder b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.addColors(Vector3D.of(1.0, 1.0, 1.0));
    b.addDistributions(0.0);
    b.addDistributions(1.0);
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    final SyThemeGradientLinear r = b.build();
    final AtomicBoolean called = new AtomicBoolean(false);
    r.matchFill(
      this,
      (gt, gradient) -> {
        called.set(true);
        return Unit.unit();
      },
      (gt, color) -> {
        throw new UnreachableCodeException();
      });

    Assert.assertTrue(called.get());
  }
}
