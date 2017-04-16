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
import com.io7m.jsycamore.core.components.SyPanelType;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SyPanelContract extends SyComponentContract
{
  @Override
  protected abstract SyPanelType create();

  @Test
  public void testTransparency()
  {
    final SyPanelType c = this.create();

    Assert.assertFalse(c.isPanelTransparent());
    c.setPanelTransparent(true);
    Assert.assertTrue(c.isPanelTransparent());
    c.setPanelTransparent(false);
    Assert.assertFalse(c.isPanelTransparent());
  }

  @Test
  public void testWindowlessTheme()
  {
    final SyPanelType c = this.create();
    Assert.assertFalse(c.window().isPresent());
    Assert.assertFalse(c.theme().isPresent());
  }

  @Test
  public final void testMatch()
  {
    final SyPanelType panel = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    panel.matchComponent(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, b_panel) -> {
        called.set(true);
        return Unit.unit();
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

    Assert.assertTrue(called.get());
  }

  @Test
  public final void testMatchReadable()
  {
    final SyPanelType panel = this.create();
    final AtomicBoolean called = new AtomicBoolean(false);

    panel.matchComponentReadable(
      this,
      (x, button) -> {
        throw new UnreachableCodeException();
      },
      (x, b_panel) -> {
        called.set(true);
        return Unit.unit();
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

    Assert.assertTrue(called.get());
  }
}
