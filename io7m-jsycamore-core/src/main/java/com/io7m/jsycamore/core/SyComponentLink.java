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

public final class SyComponentLink
{
  private final SyComponentType source;
  private final SyComponentType target;

  public SyComponentLink(
    final SyComponentType in_source,
    final SyComponentType in_target)
  {
    this.source = NullCheck.notNull(in_source);
    this.target = NullCheck.notNull(in_target);
  }

  public SyComponentType source()
  {
    return this.source;
  }

  public SyComponentType target()
  {
    return this.target;
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final SyComponentLink that = (SyComponentLink) o;
    return this.source.equals(that.source) && this.target.equals(that.target);
  }

  @Override
  public int hashCode()
  {
    int result = this.source.hashCode();
    result = 31 * result + this.target.hashCode();
    return result;
  }
}
