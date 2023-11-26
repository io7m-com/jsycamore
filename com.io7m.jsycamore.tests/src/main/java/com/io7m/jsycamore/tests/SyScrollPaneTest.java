/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.tests;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyScrollPaneType;
import com.io7m.jsycamore.components.standard.SyScrollPanes;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.Test;

public final class SyScrollPaneTest extends SyComponentContract<SyScrollPaneType>
{
  @Override
  protected SyScrollPaneType newComponent()
  {
    return SyScrollPanes.create();
  }

  @Test
  public void testTooSmall()
  {
    final var c = this.newComponent();
    c.setSizeUpperLimit(PAreaSizeI.of(0, 0));
    c.setContentAreaSize(PAreaSizeI.of(0, 0));

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(PVectors2I.zero());

    c.position();
  }
}
