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

package com.io7m.jsycamore.api.images;

import java.util.concurrent.CompletableFuture;

/**
 * <p>The type of image references.</p>
 *
 * <p>An image reference represents an image that may or may not currently be in
 * the process of being loaded.</p>
 *
 * @param <T> The type of images
 */

public interface SyImageReferenceType<T>
{
  /**
   * @return The future representing the image loading operation
   */

  CompletableFuture<T> future();

  /**
   * @return The specification used to construct the image reference
   */

  SyImageSpecificationType description();

  /**
   * Retrieve the current image. If the image is not yet loaded, then a default image will be used.
   * If the image has failed to load, then an image representing an error will be used. Otherwise,
   * the image itself will be returned.
   *
   * @return An image
   */

  T value();
}
