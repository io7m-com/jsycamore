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
import com.io7m.jtensors.core.parameterized.vectors.PVector3D;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;

import java.util.Objects;

/**
 * A parameter for a theme.
 */

public sealed interface SyThemeParameterType
{
  /**
   * @return The name of the parameter
   */

  String name();

  /**
   * @return A humanly-readable name describing the type of the parameter
   */

  String type();

  /**
   * @return A humanly-readable description of the parameter
   */

  String description();

  /**
   * A parameter of a signed integer type.
   *
   * @param name        The name of the parameter
   * @param description The humanly-readable name describing the type of the
   *                    parameter
   * @param value       The humanly-readable description of the parameter
   */

  record SyParameterIntegralSigned(
    String name,
    String description,
    int value)
    implements SyThemeParameterType
  {
    /**
     * A parameter of a signed integer type.
     */

    public SyParameterIntegralSigned
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
    }

    @Override
    public String type()
    {
      return "Integer Signed";
    }
  }

  /**
   * A parameter of an unsigned integer type.
   *
   * @param name        The name of the parameter
   * @param description The humanly-readable name describing the type of the
   *                    parameter
   * @param value       The humanly-readable description of the parameter
   */

  record SyParameterIntegralUnsigned(
    String name,
    String description,
    int value)
    implements SyThemeParameterType
  {
    /**
     * A parameter of a signed integer type.
     */

    public SyParameterIntegralUnsigned
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
    }

    @Override
    public String type()
    {
      return "Integer Unsigned";
    }
  }

  /**
   * A parameter of a real type.
   *
   * @param name        The name of the parameter
   * @param description The humanly-readable name describing the type of the
   *                    parameter
   * @param value       The humanly-readable description of the parameter
   */

  record SyParameterReal(
    String name,
    String description,
    double value)
    implements SyThemeParameterType
  {
    /**
     * A parameter of a real type.
     */

    public SyParameterReal
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
    }

    @Override
    public String type()
    {
      return "Real";
    }
  }

  /**
   * A parameter of a string type.
   *
   * @param name        The name of the parameter
   * @param description The humanly-readable name describing the type of the
   *                    parameter
   * @param value       The humanly-readable description of the parameter
   */

  record SyParameterString(
    String name,
    String description,
    String value)
    implements SyThemeParameterType
  {
    /**
     * A parameter of a string type.
     */

    public SyParameterString
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(value, "value");
    }

    @Override
    public String type()
    {
      return "String";
    }
  }

  /**
   * A parameter of an RGB color vector type.
   *
   * @param name        The name of the parameter
   * @param description The humanly-readable name describing the type of the
   *                    parameter
   * @param value       The humanly-readable description of the parameter
   */

  record SyParameterColorRGB(
    String name,
    String description,
    PVector3D<SySpaceRGBAPreType> value)
    implements SyThemeParameterType
  {
    /**
     * A parameter of an RGB color vector type.
     */

    public SyParameterColorRGB
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(value, "value");
    }

    @Override
    public String type()
    {
      return "ColorRGB";
    }
  }

  /**
   * A parameter of an RGBA color vector type.
   *
   * @param name        The name of the parameter
   * @param description The humanly-readable name describing the type of the
   *                    parameter
   * @param value       The humanly-readable description of the parameter
   */

  record SyParameterColorRGBA(
    String name,
    String description,
    PVector4D<SySpaceRGBAPreType> value)
    implements SyThemeParameterType
  {
    /**
     * A parameter of an RGBA color vector type.
     */

    public SyParameterColorRGBA
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(value, "value");
    }

    @Override
    public String type()
    {
      return "ColorRGBA";
    }
  }
}
