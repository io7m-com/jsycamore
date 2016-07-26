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

package com.io7m.jsycamore.tests.core.boxes;

import com.io7m.jsycamore.core.SySpaceType;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SyBoxGenerator<S extends SySpaceType> implements Generator<SyBoxType<S>>
{
  private final Generator<Integer> int_gen;

  SyBoxGenerator()
  {
    this.int_gen = PrimitiveGenerators.integers(-100, 100);
  }

  public SyBoxType<S> next()
  {
    final List<Integer> order = new ArrayList<Integer>(2);
    order.add(this.int_gen.next());
    order.add(this.int_gen.next());
    Collections.sort(order);

    final int x_min = order.get(0).intValue();
    final int x_max = order.get(1).intValue();

    order.clear();
    order.add(this.int_gen.next());
    order.add(this.int_gen.next());
    Collections.sort(order);

    final int y_min = order.get(0).intValue();
    final int y_max = order.get(1).intValue();

    return SyBox.of(x_min, x_max, y_min, y_max);
  }
}
