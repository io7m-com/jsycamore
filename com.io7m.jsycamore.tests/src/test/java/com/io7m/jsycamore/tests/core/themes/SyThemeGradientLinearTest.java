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
import com.io7m.jsycamore.api.themes.SyThemeGradientLinear;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyThemeGradientLinearTest
{
  @Test
  public void testPreconditionWrongDistributions()
  {
    final var b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    assertThrows(PreconditionViolationException.class, b::build);
  }

  @Test
  public void testPreconditionTooFewColors()
  {
    final var b = SyThemeGradientLinear.builder();
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    assertThrows(PreconditionViolationException.class, b::build);
  }

  @Test
  public void testPreconditionDisorderedDistributions()
  {
    final var b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.addColors(Vector3D.of(1.0, 1.0, 1.0));
    b.addDistributions(1.0);
    b.addDistributions(0.0);
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    assertThrows(PreconditionViolationException.class, b::build);
  }

  @Test
  public void testOK()
  {
    final var b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.addColors(Vector3D.of(1.0, 1.0, 1.0));
    b.addDistributions(0.0);
    b.addDistributions(1.0);
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    final var r = b.build();
    assertEquals(2L, (long) r.colors().size());
    assertEquals(Vector3D.of(0.0, 0.0, 0.0), r.colors().get(0));
    assertEquals(Vector3D.of(1.0, 1.0, 1.0), r.colors().get(1));

    assertEquals(2L, (long) r.distributions().size());
    assertEquals(0.0, r.distributions().get(0).floatValue(), 0.0);
    assertEquals(1.0, r.distributions().get(1).floatValue(), 0.0);

    assertEquals(Vector2D.of(0.0, 0.0), r.point0());
    assertEquals(Vector2D.of(1.0, 1.0), r.point1());
  }

  @Test
  public void testMatch()
  {
    final var b = SyThemeGradientLinear.builder();
    b.addColors(Vector3D.of(0.0, 0.0, 0.0));
    b.addColors(Vector3D.of(1.0, 1.0, 1.0));
    b.addDistributions(0.0);
    b.addDistributions(1.0);
    b.setPoint0(Vector2D.of(0.0, 0.0));
    b.setPoint1(Vector2D.of(1.0, 1.0));

    final var r = b.build();
    final var called = new AtomicBoolean(false);
    r.matchFill(
      this,
      (gt, gradient) -> {
        called.set(true);
        return Void.class;
      },
      (gt, color) -> {
        throw new UnreachableCodeException();
      });

    assertTrue(called.get());
  }
}
