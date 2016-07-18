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

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.components.SyLabelType;
import org.junit.Assert;
import org.junit.Test;

public abstract class SyLabelContract
{
  protected abstract SyLabelType create();

  @Test
  public final void testIdentities()
  {
    final SyLabelType label = this.create();

    for (final SyAlignmentHorizontal a : SyAlignmentHorizontal.values()) {
      label.setTextAlignmentHorizontal(a);
      Assert.assertEquals(a, label.textAlignmentHorizontal());
    }

    for (final SyAlignmentVertical a : SyAlignmentVertical.values()) {
      label.setTextAlignmentVertical(a);
      Assert.assertEquals(a, label.textAlignmentVertical());
    }
  }
}
