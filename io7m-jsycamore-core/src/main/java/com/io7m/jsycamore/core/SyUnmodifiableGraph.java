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

import com.io7m.jnull.NullCheck;
import net.jcip.annotations.NotThreadSafe;
import org.jgrapht.graph.UnmodifiableDirectedGraph;

/**
 * An unmodifiable view of a {@link SyGraph} value.
 *
 * @param <V> The type of vertices
 * @param <E> The type of edges
 */

@NotThreadSafe
public final class SyUnmodifiableGraph<V, E> extends UnmodifiableDirectedGraph<V, E>
{
  /**
   * Construct a read-only view of the given graph.
   *
   * @param actual The actual graph
   */

  public SyUnmodifiableGraph(final SyGraph<V, E> actual)
  {
    super(NullCheck.notNull(actual));
  }

  /**
   * A convenient function for producing more strongly-typed read-only views of
   * graphs.
   *
   * @param actual The graph
   * @param <VR>   A read-only subtype of {@code V}
   * @param <V>    The original type of vertices
   * @param <ER>   A read-only subtype of {@code E}
   * @param <E>    The original type of edges
   *
   * @return A read-only view of {@code actual} with the type parameters
   * replaced with read-only subtypes
   */

  @SuppressWarnings("unchecked")
  public static <VR, V extends VR, ER, E extends ER>
  SyUnmodifiableGraph<VR, ER> createReadable(
    final SyGraph<V, E> actual)
  {
    NullCheck.notNull(actual);
    return new SyUnmodifiableGraph<>((SyGraph<VR, ER>) actual);
  }
}
