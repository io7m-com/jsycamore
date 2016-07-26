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

package com.io7m.jsycamore.tests.core;

import com.io7m.jsycamore.core.SyMouseButton;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public final class SyMouseButtonTest
{
  @Rule public ExpectedException expected = ExpectedException.none();

  @Test public void testOf()
  {
    Assert.assertEquals(
      SyMouseButton.MOUSE_BUTTON_LEFT, SyMouseButton.ofIndex(0));
    Assert.assertEquals(
      SyMouseButton.MOUSE_BUTTON_MIDDLE, SyMouseButton.ofIndex(1));
    Assert.assertEquals(
      SyMouseButton.MOUSE_BUTTON_RIGHT, SyMouseButton.ofIndex(2));

    for (int index = 0; index < 3; ++index) {
      Assert.assertEquals(
        (long) index,
        (long) SyMouseButton.ofIndex(index).index());
    }
  }

  @Test public void testOfInvalid1()
  {
    this.expected.expect(IllegalArgumentException.class);
    SyMouseButton.ofIndex(-1);
  }

  @Test public void testOfInvalid3()
  {
    this.expected.expect(IllegalArgumentException.class);
    SyMouseButton.ofIndex(3);
  }
}
