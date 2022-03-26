/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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
import com.io7m.jsycamore.api.themes.SyThemeParameterType.SyParameterColorRGBA;

import java.util.Map;
import java.util.Optional;

import static com.io7m.jsycamore.api.themes.SyThemeParameterType.SyParameterString;

/**
 * A theme instance.
 */

public interface SyThemeType extends SyThemeWindowType
{
  /**
   * Find a theme component for the given component.
   *
   * @param component The component
   *
   * @return A theme component
   */

  SyThemeComponentType findForComponent(
    SyThemeableReadableType component);

  /**
   * Set the value of a parameter.
   *
   * @param parameter The parameter
   */

  void setParameter(
    SyThemeParameterType parameter);

  /**
   * @return A read-only view of the theme's declared parameters
   */

  Map<String, SyThemeParameterType> parameters();

  /**
   * Retrieve a parameter by name.
   *
   * @param parameter The parameter
   *
   * @return A parameter, if one exists
   */

  default Optional<SyThemeParameterType> parameterFor(
    final SyThemeParameterType parameter)
  {
    return Optional.ofNullable(this.parameters().get(parameter.name()));
  }

  /**
   * Retrieve a parameter by name.
   *
   * @param parameter The parameter
   *
   * @return A parameter, if one exists
   */

  default Optional<SyParameterString> parameterForString(
    final SyThemeParameterType parameter)
  {
    return Optional.ofNullable(this.parameters().get(parameter.name()))
      .map(SyParameterString.class::cast);
  }

  /**
   * Retrieve a parameter by name.
   *
   * @param parameter The parameter
   *
   * @return A parameter, if one exists
   */

  default Optional<SyParameterColorRGBA> parameterForRGBA(
    final SyThemeParameterType parameter)
  {
    return Optional.ofNullable(this.parameters().get(parameter.name()))
      .map(SyParameterColorRGBA.class::cast);
  }

  /**
   * Retrieve a parameter by name.
   *
   * @param parameter The parameter
   *
   * @return A parameter, if one exists
   */

  default Optional<SyPaintFillType> parameterForFillRGBA(
    final SyThemeParameterType parameter)
  {
    return Optional.ofNullable(this.parameters().get(parameter.name()))
      .map(SyParameterColorRGBA.class::cast)
      .map(x -> new SyPaintFlat(x.value()));
  }

  /**
   * Retrieve a parameter by name.
   *
   * @param parameter The parameter
   *
   * @return A parameter, if one exists
   */

  default Optional<SyPaintEdgeType> parameterForEdgeRGBA(
    final SyThemeParameterType parameter)
  {
    return Optional.ofNullable(this.parameters().get(parameter.name()))
      .map(SyParameterColorRGBA.class::cast)
      .map(x -> new SyPaintFlat(x.value()));
  }
}
