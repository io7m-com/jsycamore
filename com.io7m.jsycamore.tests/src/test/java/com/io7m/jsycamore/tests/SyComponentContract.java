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

import com.io7m.jsycamore.api.components.SyComponentType;
import org.junit.jupiter.api.Test;

import static com.io7m.jsycamore.api.components.SyActive.ACTIVE;
import static com.io7m.jsycamore.api.components.SyActive.INACTIVE;
import static com.io7m.jsycamore.api.components.SyVisibility.VISIBILITY_INVISIBLE;
import static com.io7m.jsycamore.api.components.SyVisibility.VISIBILITY_VISIBLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SyComponentContract<T extends SyComponentType>
{
  protected abstract T newComponent();

  @Test
  public final void testComponentActiveOK()
  {
    final var c = this.newComponent();
    assertEquals(ACTIVE, c.activity().get());
    assertTrue(c.isActive());

    c.setActive(INACTIVE);
    assertEquals(INACTIVE, c.activity().get());
    assertFalse(c.isActive());

    c.setActive(ACTIVE);
    assertEquals(ACTIVE, c.activity().get());
    assertTrue(c.isActive());
  }

  @Test
  public final void testComponentVisibleOK()
  {
    final var c = this.newComponent();
    assertEquals(VISIBILITY_VISIBLE, c.visibility().get());
    assertTrue(c.isVisible());

    c.setVisible(VISIBILITY_INVISIBLE);
    assertEquals(VISIBILITY_INVISIBLE, c.visibility().get());
    assertFalse(c.isVisible());

    c.setVisible(VISIBILITY_VISIBLE);
    assertEquals(VISIBILITY_VISIBLE, c.visibility().get());
    assertTrue(c.isVisible());
  }
}
