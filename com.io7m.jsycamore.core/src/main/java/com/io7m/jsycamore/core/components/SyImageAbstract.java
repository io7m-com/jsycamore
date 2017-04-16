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
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.images.api.SyImageSpecification;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;

/**
 * An abstract implementation of the {@link SyImageType} interface.
 */

@NotThreadSafe
public abstract class SyImageAbstract extends SyComponentAbstract implements
  SyImageType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyImageAbstract.class);
  }

  private SyImageSpecification image;
  private SyAlignmentHorizontal align_h;
  private SyAlignmentVertical align_v;

  protected SyImageAbstract(
    final SyImageSpecification in_image,
    final BooleanSupplier in_detach_check)
  {
    super(in_detach_check);
    this.image = NullCheck.notNull(in_image, "Image");
    this.align_h = SyAlignmentHorizontal.ALIGN_CENTER;
    this.align_v = SyAlignmentVertical.ALIGN_CENTER;
  }

  @Override
  public final SyAlignmentHorizontal imageAlignmentHorizontal()
  {
    return this.align_h;
  }

  @Override
  public final SyAlignmentVertical imageAlignmentVertical()
  {
    return this.align_v;
  }

  @Override
  public final void setImageAlignmentVertical(
    final SyAlignmentVertical v)
  {
    this.align_v = NullCheck.notNull(v, "Alignment");
  }

  @Override
  public final void setImageAlignmentHorizontal(
    final SyAlignmentHorizontal h)
  {
    this.align_h = NullCheck.notNull(h, "Alignment");
  }

  @Override
  public final void setImage(final SyImageSpecification in_image)
  {
    this.image = NullCheck.notNull(in_image, "Image specification");
  }

  @Override
  public final SyImageSpecification image()
  {
    return this.image;
  }
}
