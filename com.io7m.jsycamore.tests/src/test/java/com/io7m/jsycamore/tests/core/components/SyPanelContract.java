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

import com.io7m.jsycamore.api.components.SyPanelType;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyPanelContract extends SyComponentContract
{
  @Override
  protected abstract SyPanelType create();

  @Test
  public void testTransparency()
  {
    final var c = this.create();

    assertFalse(c.isPanelTransparent());
    c.setPanelTransparent(true);
    assertTrue(c.isPanelTransparent());
    c.setPanelTransparent(false);
    assertFalse(c.isPanelTransparent());
  }

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
    final var panel = this.create();
    final var called = new AtomicBoolean(false);

    panel.matchComponent(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, b_panel) -> {
        called.set(true);
        return Void.class;
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, image) -> {
        throw new UnreachableCodeException();
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final var panel = this.create();
    final var called = new AtomicBoolean(false);

    panel.matchComponentReadable(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, b_panel) -> {
        called.set(true);
        return Void.class;
      },
      (x, label) -> {
        throw new UnreachableCodeException();
      },
      (x, image) -> {
        throw new UnreachableCodeException();
      },
      (x, meter) -> {
        throw new UnreachableCodeException();
      });

    assertTrue(called.get());
  }
}
