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

import com.io7m.junreachable.UnreachableCodeException;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * Functions to log and ignore exceptions.
 */

public final class SyErrors
{
  private SyErrors()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Iff {@code e <: Error}, throw {@code e}. Otherwise, log {@code e} and suppress it.
   *
   * @param log A log
   * @param e   An exception
   */

  public static void ignoreNonErrors(
    final Logger log,
    final Throwable e)
  {
    Objects.requireNonNull(log, "Logger");
    Objects.requireNonNull(e, "Exception");

    if (e instanceof Error) {
      throw (Error) e;
    }

    log.debug("ignored exception: ", e);
  }
}
