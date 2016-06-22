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

package com.io7m.jsycamore.core;

import org.jgrapht.EdgeFactory;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

/**
 * The type of directed acyclic graphs where each vertex may have at most one
 * incoming edge. This turns the graph into a tree.
 *
 * @param <V> The type of vertices
 * @param <E> The type of edges
 */

public final class SyGraph<V, E> extends DirectedAcyclicGraph<V, E>
{
  /**
   * Construct a new graph.
   *
   * @param c The type of edges
   */

  public SyGraph(final Class<? extends E> c)
  {
    super(c);
  }

  /**
   * Construct a new graph.
   *
   * @param edges An edge factory
   */

  public SyGraph(final EdgeFactory<V, E> edges)
  {
    super(edges);
  }

  @Override
  public E addDagEdge(
    final V from,
    final V to)
    throws CycleFoundException
  {
    this.checkParentInvariant(to);
    return super.addDagEdge(from, to);
  }

  private void checkParentInvariant(final V to)
  {
    if (this.inDegreeOf(to) >= 1) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Graph vertex already has a parent.");
      sb.append(System.lineSeparator());
      sb.append("  Vertex: ");
      sb.append(to);
      sb.append(System.lineSeparator());
      sb.append("  Parent: ");
      sb.append(this.incomingEdgesOf(to));
      sb.append(System.lineSeparator());
      throw new IllegalStateException(sb.toString());
    }
  }

  @Override
  public E addEdge(
    final V source,
    final V target)
  {
    this.checkParentInvariant(target);
    return super.addEdge(source, target);
  }

  @Override
  public boolean addDagEdge(
    final V from,
    final V to,
    final E e)
    throws CycleFoundException
  {
    this.checkParentInvariant(to);
    return super.addDagEdge(from, to, e);
  }

  @Override
  public boolean addEdge(
    final V source,
    final V target,
    final E edge)
  {
    this.checkParentInvariant(target);
    return super.addEdge(source, target, edge);
  }
}
