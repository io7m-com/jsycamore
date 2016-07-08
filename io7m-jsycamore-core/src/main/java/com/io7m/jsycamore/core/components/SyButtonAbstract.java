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

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract implementation of the {@link SyButtonType} interface.
 */

public abstract class SyButtonAbstract extends SyComponentAbstract implements
  SyButtonType
{
  private final List<Runnable> listeners;

  protected SyButtonAbstract()
  {
    this.listeners = new ArrayList<>(4);
  }

  @Override
  public final void buttonAddListener(final Runnable r)
  {
    this.listeners.add(NullCheck.notNull(r));
  }

  @Override
  public final void buttonRemoveListener(final Runnable r)
  {
    this.listeners.remove(NullCheck.notNull(r));
  }

  @Override
  public void onMouseHeld(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {

  }

  @Override
  public void onMouseNoLongerOver()
  {

  }

  @Override
  public void onMouseOver(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {

  }
}
