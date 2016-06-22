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
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVector2IType;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;

import java.util.Optional;

/**
 * The default implementation of the {@link SyWindowType} type.
 */

public final class SyWindow implements SyWindowType
{
  private final PVector2IType<SySpaceViewportType> position;
  private final Titlebar titlebar;
  private final String to_string;
  private SyGraph<SyComponentType, SyComponentLink> components;
  private VectorM2I size;
  private SyThemeType theme;

  private final class Titlebar
  {
    private int height;
  }

  private SyWindow(final SyThemeType in_theme)
  {
    NullCheck.notNull(in_theme);

    this.to_string = "[SyWindow " + this.hashCode() + "]";
    this.position = new PVectorM2I<>();
    this.size = new VectorM2I();
    this.components = new SyGraph<>(SyComponentLink::new);
    this.titlebar = new Titlebar();
    this.themeReload(in_theme);
  }

  @Override
  public String toString()
  {
    return this.to_string;
  }

  private void themeReload(final SyThemeType new_theme)
  {
    this.theme = new_theme;
  }

  /**
   * Create a new window.
   *
   * @param in_theme A reference to the current theme
   *
   * @return A new window
   */

  public static SyWindowType create(final SyThemeType in_theme)
  {
    return new SyWindow(in_theme);
  }

  @Override
  public SyGraph<SyComponentType, SyComponentLink> components()
  {
    return this.components;
  }

  @Override
  public VectorReadable2IType size()
  {
    return this.size;
  }

  @Override
  public SyThemeType theme()
  {
    return this.theme;
  }

  @Override
  public void setSize(
    final int width,
    final int height)
  {
    this.size.set2I(Math.max(2, width), Math.max(2, height));
    // XXX: Notify top-level components
  }

  @Override
  public PVectorReadable2IType<SySpaceViewportType> position()
  {
    return this.position;
  }
}
