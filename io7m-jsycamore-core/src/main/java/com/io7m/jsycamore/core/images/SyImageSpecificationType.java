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

package com.io7m.jsycamore.core.images;

import com.io7m.jsycamore.core.SyImmutableStyleType;
import org.immutables.value.Value;

/**
 * An image to be loaded.
 */

@SyImmutableStyleType
@Value.Immutable
public interface SyImageSpecificationType
{
  /**
   * The abstract name of an image. This is expected to be mapped in an
   * implementation-specific manner to an image resource.
   *
   * @return The name of the image
   *
   * @see SyImageCacheResolverType
   */

  @Value.Parameter
  String name();

  /**
   * @return The width of the image
   */

  @Value.Parameter
  int width();

  /**
   * @return The height of the image
   */

  @Value.Parameter
  int height();

  /**
   * @return The format of the image
   */

  @Value.Parameter
  SyImageFormat format();

  /**
   * @return The scaling interpolation, if scaling is required
   */

  @Value.Parameter
  @Value.Default
  default SyImageScaleInterpolation scaleInterpolation()
  {
    return SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR;
  }
}
