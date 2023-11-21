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

import com.io7m.jsycamore.api.rendering.SyPaintEdgeType;
import com.io7m.jsycamore.api.rendering.SyPaintFillType;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;

import java.util.Map;

/**
 * Values that can be retrieved by a theme.
 */

public interface SyThemeValuesType
{
  /**
   * @return The current values
   */

  Map<String, SyThemeValueType> values();

  /**
   * Reset all values to their defaults.
   */

  void reset();

  /**
   * Check that a value exists with the given name and that it has an RGBA color
   * type, and return the current value.
   *
   * @param name The value name
   *
   * @return The current value
   *
   * @throws SyThemeValueException On errors
   */

  PVector4D<SySpaceRGBAPreType> color4D(
    String name)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has an RGBA color
   * type, and set the current value to {@code color}.
   *
   * @param name  The value name
   * @param value The new value
   *
   * @throws SyThemeValueException On errors
   */

  void setColor4D(
    String name,
    PVector4D<SySpaceRGBAPreType> value)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has an RGBA color
   * function type, and set the current function to {@code transform}.
   *
   * @param name      The value name
   * @param transform The new transform
   *
   * @throws SyThemeValueException On errors
   */

  void setColorTransform(
    String name,
    SyThemeValueFunctionType<PVector4D<SySpaceRGBAPreType>> transform)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has an integer
   * type, and return the current value.
   *
   * @param name The value name
   *
   * @return The current value
   *
   * @throws SyThemeValueException On errors
   */

  int integer(
    String name)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has an integer
   * type, and set the current value to {@code color}.
   *
   * @param name  The value name
   * @param value The new value
   *
   * @throws SyThemeValueException On errors
   */

  void setInteger(
    String name,
    int value)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has an integer
   * function type, and set the current function to {@code transform}.
   *
   * @param name      The value name
   * @param transform The new transform
   *
   * @throws SyThemeValueException On errors
   */

  void setIntegerTransform(
    String name,
    SyThemeValueFunctionType<Integer> transform)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has a font type,
   * and return the current value.
   *
   * @param name The value name
   *
   * @return The current value
   *
   * @throws SyThemeValueException On errors
   */

  SyFontDescription font(
    String name)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has a font type,
   * and set the current value to {@code color}.
   *
   * @param name  The value name
   * @param value The new value
   *
   * @throws SyThemeValueException On errors
   */

  void setFont(
    String name,
    SyFontDescription value)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has a font
   * function type, and set the current function to {@code transform}.
   *
   * @param name      The value name
   * @param transform The new transform
   *
   * @throws SyThemeValueException On errors
   */

  void setFontTransform(
    String name,
    SyThemeValueFunctionType<SyFontDescription> transform)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has a double
   * type, and return the current value.
   *
   * @param name The value name
   *
   * @return The current value
   *
   * @throws SyThemeValueException On errors
   */

  double double_(
    String name)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has a double
   * type, and set the current value to {@code color}.
   *
   * @param name  The value name
   * @param value The new value
   *
   * @throws SyThemeValueException On errors
   */

  void setDouble(
    String name,
    double value)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has a double
   * function type, and set the current function to {@code transform}.
   *
   * @param name      The value name
   * @param transform The new transform
   *
   * @throws SyThemeValueException On errors
   */

  void setDoubleTransform(
    String name,
    SyThemeValueFunctionType<Double> transform)
    throws SyThemeValueException;

  /**
   * Check that a value exists with the given name and that it has an RGBA color
   * type, and return the current value wrapped as a flat fill paint value.
   *
   * @param name The value name
   *
   * @return A flat paint
   *
   * @throws SyThemeValueException On errors
   */

  default SyPaintFillType fillFlat(
    final String name)
    throws SyThemeValueException
  {
    return new SyPaintFlat(this.color4D(name));
  }

  /**
   * Check that a value exists with the given name and that it has an RGBA color
   * type, and return the current value wrapped as a flat edge paint value.
   *
   * @param name The value name
   *
   * @return A flat paint
   *
   * @throws SyThemeValueException On errors
   */

  default SyPaintEdgeType edgeFlat(
    final String name)
    throws SyThemeValueException
  {
    return new SyPaintFlat(this.color4D(name));
  }
}
