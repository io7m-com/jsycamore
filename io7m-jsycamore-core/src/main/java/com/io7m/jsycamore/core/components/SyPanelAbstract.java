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

package com.io7m.jsycamore.core.components;

import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;

/**
 * An abstract implementation of the {@link SyPanelType} interface.
 */

@NotThreadSafe
public abstract class SyPanelAbstract extends SyComponentAbstract implements
  SyPanelType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyPanelAbstract.class);
  }

  private boolean transparent;

  protected SyPanelAbstract(
    final BooleanSupplier in_detach_check)
  {
    super(in_detach_check);
  }

  @Override
  public final void setPanelTransparent(final boolean e)
  {
    this.transparent = e;
  }

  @Override
  public final boolean isPanelTransparent()
  {
    return this.transparent;
  }
}
