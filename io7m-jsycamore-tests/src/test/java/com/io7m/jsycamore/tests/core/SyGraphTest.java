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

import com.io7m.jsycamore.core.SyGraph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public final class SyGraphTest
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  @Test
  public void testAddEdgeInvariant0()
  {
    final SyGraph<Object, Edge> g = new SyGraph<>(Edge::new);

    final Object o0 = new Object();
    final Object o1 = new Object();
    final Object o2 = new Object();

    g.addVertex(o0);
    g.addVertex(o1);
    g.addVertex(o2);

    g.addEdge(o0, o1);
    this.expected.expect(IllegalStateException.class);
    g.addEdge(o2, o1);
  }

  @Test
  public void testAddEdgeInvariant1()
    throws Exception
  {
    final SyGraph<Object, Edge> g = new SyGraph<>(Edge::new);

    final Object o0 = new Object();
    final Object o1 = new Object();
    final Object o2 = new Object();

    g.addVertex(o0);
    g.addVertex(o1);
    g.addVertex(o2);

    final Edge e0 = new Edge(o0, o1);
    final Edge e1 = new Edge(o2, o1);

    g.addEdge(o0, o1, e0);
    this.expected.expect(IllegalStateException.class);
    g.addEdge(o2, o1, e1);
  }

  @Test
  public void testAddDagEdgeInvariant0()
    throws Exception
  {
    final SyGraph<Object, Edge> g = new SyGraph<>(Edge::new);

    final Object o0 = new Object();
    final Object o1 = new Object();
    final Object o2 = new Object();

    g.addVertex(o0);
    g.addVertex(o1);
    g.addVertex(o2);

    g.addDagEdge(o0, o1);
    this.expected.expect(IllegalStateException.class);
    g.addDagEdge(o2, o1);
  }

  @Test
  public void testAddDagEdgeInvariant1()
    throws Exception
  {
    final SyGraph<Object, Edge> g = new SyGraph<>(Edge::new);

    final Object o0 = new Object();
    final Object o1 = new Object();
    final Object o2 = new Object();

    g.addVertex(o0);
    g.addVertex(o1);
    g.addVertex(o2);

    final Edge e0 = new Edge(o0, o1);
    final Edge e1 = new Edge(o2, o1);

    g.addDagEdge(o0, o1, e0);
    this.expected.expect(IllegalStateException.class);
    g.addDagEdge(o2, o1, e1);
  }

  private static final class Edge
  {
    private final Object from;
    private final Object to;

    Edge(
      final Object in_from,
      final Object in_to)
    {
      this.from = in_from;
      this.to = in_to;
    }
  }
}
