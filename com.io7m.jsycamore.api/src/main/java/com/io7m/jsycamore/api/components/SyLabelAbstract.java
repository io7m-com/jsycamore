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

package com.io7m.jsycamore.api.components;

import com.io7m.jsycamore.api.themes.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.themes.SyAlignmentVertical;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * An abstract implementation of the {@link SyLabelType} interface.
 */

@NotThreadSafe
public abstract class SyLabelAbstract extends SyComponentAbstract implements
  SyLabelType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyLabelAbstract.class);
  }

  private SyAlignmentHorizontal align_h;
  private SyAlignmentVertical align_v;
  private String text;

  protected SyLabelAbstract(
    final BooleanSupplier in_detach_check)
  {
    super(in_detach_check);
    this.text = "";
    this.align_h = SyAlignmentHorizontal.ALIGN_CENTER;
    this.align_v = SyAlignmentVertical.ALIGN_CENTER;
  }

  @Override
  public final SyAlignmentHorizontal textAlignmentHorizontal()
  {
    return this.align_h;
  }

  @Override
  public final SyAlignmentVertical textAlignmentVertical()
  {
    return this.align_v;
  }

  @Override
  public final void setTextAlignmentVertical(
    final SyAlignmentVertical v)
  {
    this.align_v = Objects.requireNonNull(v, "Alignment");
  }

  @Override
  public final void setTextAlignmentHorizontal(
    final SyAlignmentHorizontal h)
  {
    this.align_h = Objects.requireNonNull(h, "Alignment");
  }

  @Override
  public final void setText(final String in_text)
  {
    this.text = Objects.requireNonNull(in_text, "Text");
  }

  @Override
  public final String text()
  {
    return this.text;
  }
}
