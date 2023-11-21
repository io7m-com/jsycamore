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
 * A mutable builder to construct value sets.
 */

public interface SyThemeValuesBuilderType
{
  /**
   * Create a new named constant with an RGBA color type.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createConstantColor4D(
    String name,
    String description,
    PVector4D<SySpaceRGBAPreType> value)
    throws SyThemeValueException;

  /**
   * Create a new named constant with an integer type.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createConstantInteger(
    String name,
    String description,
    int value)
    throws SyThemeValueException;

  /**
   * Create a new named constant with a font type.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createConstantFont(
    String name,
    String description,
    SyFontDescription value)
    throws SyThemeValueException;

  /**
   * Create a new named constant with a double type.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createConstantDouble(
    String name,
    String description,
    double value)
    throws SyThemeValueException;

  /**
   * Create a new named function that transforms RGBA color types.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param source      The name of the source that will be passed to this
   *                    function
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createFunctionColor4D(
    String name,
    String description,
    String source,
    SyThemeValueFunctionType<PVector4D<SySpaceRGBAPreType>> value)
    throws SyThemeValueException;

  /**
   * Create a new named function that transforms integers.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param source      The name of the source that will be passed to this
   *                    function
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createFunctionInteger(
    String name,
    String description,
    String source,
    SyThemeValueFunctionType<Integer> value)
    throws SyThemeValueException;

  /**
   * Create a new named function that transforms doubles.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param source      The name of the source that will be passed to this
   *                    function
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createFunctionDouble(
    String name,
    String description,
    String source,
    SyThemeValueFunctionType<Double> value)
    throws SyThemeValueException;

  /**
   * Create a new named function that transforms fonts.
   *
   * @param name        The name
   * @param description The humanly-readable description
   * @param source      The name of the source that will be passed to this
   *                    function
   * @param value       The value
   *
   * @return this
   *
   * @throws SyThemeValueException On errors
   */

  SyThemeValuesBuilderType createFunctionFont(
    String name,
    String description,
    String source,
    SyThemeValueFunctionType<SyFontDescription> value)
    throws SyThemeValueException;

  /**
   * @return An value set based on all of the parameters given so far
   */

  SyThemeValuesType create();
}
