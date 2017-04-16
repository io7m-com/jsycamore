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

/**
 * An image format specification.
 */

public enum SyImageFormat
{
  /**
   * 8-bit greyscale.
   */

  IMAGE_FORMAT_GREY_8,

  /**
   * 24-bit, 8-bit per pixel RGB.
   */

  IMAGE_FORMAT_RGB_888,

  /**
   * 32-bit, 8-bit per pixel RGBA.
   */

  IMAGE_FORMAT_RGBA_8888,

  /**
   * 16-bit, 5-bit red, 6-bit green, 5-bit blue RGB.
   */

  IMAGE_FORMAT_RGB_565,

  /**
   * 16-bit, 4-bit red, 4-bit green, 4-bit blue, 4-bit alpha RGBA.
   */

  IMAGE_FORMAT_RGBA_4444
}
