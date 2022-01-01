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

import com.io7m.jsycamore.api.themes.SyThemeColor;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyThemeColorTest
{
  @Test
  public void testMatch()
  {
    final var b = SyThemeColor.builder();
    b.setColor(Vector3D.of(0.0, 0.0, 0.0));
    final var r = b.build();
    assertEquals(Vector3D.of(0.0, 0.0, 0.0), r.color());

    final var called = new AtomicBoolean(false);
    r.matchFill(
      this,
      (gt, gradient) -> {
        throw new UnreachableCodeException();
      },
      (gt, color) -> {
        called.set(true);
        return Void.class;
      });

    assertTrue(called.get());
  }
}
