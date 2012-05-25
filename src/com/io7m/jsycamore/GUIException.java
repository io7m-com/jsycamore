/*
 * Copyright © 2012 http://io7m.com
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

package com.io7m.jsycamore;

import java.awt.FontFormatException;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jpismo.TextCacheException;
import com.io7m.jvvfs.FilesystemError;

/**
 * <p>
 * Standard exception type.
 * </p>
 * <p>
 * The exception contains an error code enumeration, {@link ErrorCode},
 * allowing code that uses the library to statically guarantee that it has
 * handled all possible errors raised by the GUI.
 * </p>
 */

public final class GUIException extends Exception
{
  public static enum ErrorCode
  {
    /** An error occurred in the OpenGL implementation. */
    GUI_OPENGL_ERROR,

    /** An error occurred whilst compiling an OpenGL shader. */
    GUI_OPENGL_COMPILE_ERROR,

    /** An error occurred whilst accessing the filesystem. */
    GUI_FILESYSTEM_ERROR,

    /** Unreachable code reached. This is a bug! */
    GUI_UNREACHABLE_CODE,

    /** I/O exception */
    GUI_IO_ERROR,

    /** Text cache exception */
    GUI_TEXT_CACHE_ERROR,

    /** Font format error */
    GUI_FONT_FORMAT_ERROR
  }

  private static final long        serialVersionUID = 8167795456664986779L;
  private final @Nonnull ErrorCode code;

  private final @Nonnull Throwable exception;

  private GUIException(
    final @Nonnull ErrorCode code,
    final @Nonnull Throwable e)
  {
    super(e.getMessage());
    this.code = code;
    this.exception = e;
  }

  public GUIException(
    final FilesystemError e)
  {
    this(ErrorCode.GUI_FILESYSTEM_ERROR, e);
  }

  public GUIException(
    final FontFormatException e)
  {
    this(ErrorCode.GUI_FONT_FORMAT_ERROR, e);
  }

  public GUIException(
    final GLCompileException e)
  {
    this(ErrorCode.GUI_OPENGL_COMPILE_ERROR, e);
  }

  public GUIException(
    final GLException e)
  {
    this(ErrorCode.GUI_OPENGL_ERROR, e);
  }

  public GUIException(
    final IOException e)
  {
    this(ErrorCode.GUI_IO_ERROR, e);
  }

  public GUIException(
    final TextCacheException e)
  {
    this(ErrorCode.GUI_TEXT_CACHE_ERROR, e);
  }

  public @Nonnull ErrorCode getErrorCode()
  {
    return this.code;
  }

  public @Nonnull Throwable getException()
  {
    return this.exception;
  }
}
