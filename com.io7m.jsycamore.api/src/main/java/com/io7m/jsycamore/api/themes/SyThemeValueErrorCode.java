/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.api.themes;

/**
 * The possible value error codes.
 */

public enum SyThemeValueErrorCode
{
  /**
   * An attempt was made to create an value that already exists.
   */

  VALUE_ALREADY_EXISTS,

  /**
   * An attempt was made to retrieve an value that does not exist.
   */

  VALUE_DOES_NOT_EXIST,

  /**
   * An attempt was made to treat an value as if it had a different type.
   */

  VALUE_TYPE_INCORRECT,

  /**
   * The value graph would be made cyclic by this operation.
   */

  VALUE_CYCLE
}
