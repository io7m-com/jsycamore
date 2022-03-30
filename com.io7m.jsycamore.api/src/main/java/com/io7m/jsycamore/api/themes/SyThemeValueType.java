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

import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;

/**
 * The base type of theme values.
 */

public sealed interface SyThemeValueType
  permits SyThemeValueType.SyConstantType,
  SyThemeValueType.SyFunctionType
{
  /**
   * @return The unique name of the value
   */

  String name();

  /**
   * @return A humanly-readable string describing the value
   */

  String description();

  /**
   * The kind of values that represent constants.
   */

  sealed interface SyConstantType extends SyThemeValueType
  {
    /**
     * The type of integer constants.
     */

    non-sealed interface SyConstantIntegerType extends SyConstantType
    {
      /**
       * @return The default value
       */

      int valueDefault();

      /**
       * @return The explicitly set value
       */

      int valueNow();
    }

    /**
     * The type of double constants.
     */

    non-sealed interface SyConstantDoubleType extends SyConstantType
    {
      /**
       * @return The default value
       */

      double valueDefault();

      /**
       * @return The explicitly set value
       */

      double valueNow();
    }

    /**
     * The type of RGBA color constants.
     */

    non-sealed interface SyConstantColor4DType extends SyConstantType
    {
      /**
       * @return The default value
       */

      PVector4D<SySpaceRGBAPreType> valueDefault();

      /**
       * @return The explicitly set value
       */

      PVector4D<SySpaceRGBAPreType> valueNow();
    }

    /**
     * The type of font constants.
     */

    non-sealed interface SyConstantFontType extends SyConstantType
    {
      /**
       * @return The default value
       */

      SyFontDescription valueDefault();

      /**
       * @return The explicitly set value
       */

      SyFontDescription valueNow();
    }
  }

  /**
   * The kind of values that represent functions.
   */

  sealed interface SyFunctionType extends SyThemeValueType
  {
    /**
     * @return The name of the source value passed to the function
     */

    String source();

    /**
     * The type of functions that take and return integers.
     */

    non-sealed interface SyFunctionIntegerType extends SyFunctionType
    {
      /**
       * @return The evaluated value
       */

      int valueNow();
    }

    /**
     * The type of functions that take and return doubles.
     */

    non-sealed interface SyFunctionDoubleType extends SyFunctionType
    {
      /**
       * @return The evaluated value
       */

      double valueNow();
    }

    /**
     * The type of functions that take and return RGBA colors.
     */

    non-sealed interface SyFunctionColor4DType extends SyFunctionType
    {
      /**
       * @return The evaluated value
       */

      PVector4D<SySpaceRGBAPreType> valueNow();
    }

    /**
     * The type of functions that take and return fonts.
     */

    non-sealed interface SyFunctionFontType extends SyFunctionType
    {
      /**
       * @return The evaluated value
       */

      SyFontDescription valueNow();
    }
  }
}
