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


package com.io7m.jsycamore.theme.spi.internal;

import com.io7m.jsycamore.api.themes.SyThemeValueFunctionType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyFunctionType.SyFunctionDoubleType;

import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.theme.spi.internal.SyValueType.TYPE_DOUBLE;

/**
 * A double function node.
 */

public final class SyValueFunctionDouble extends SyValueAbstract
  implements SyValueNodeType, SyFunctionDoubleType
{
  private final String source;
  private final SyThemeValueFunctionType<Double> functionDefault;
  private SyThemeValueFunctionType<Double> functionNow;
  private Optional<Double> evaluated;

  /**
   * Construct a function node.
   *
   * @param inName        The name
   * @param inDescription The description
   * @param inSource      The source
   * @param inFunction    The function
   */

  public SyValueFunctionDouble(
    final String inName,
    final String inDescription,
    final String inSource,
    final SyThemeValueFunctionType<Double> inFunction)
  {
    super(inName, inDescription, TYPE_DOUBLE);

    this.source =
      Objects.requireNonNull(inSource, "source");
    this.functionDefault =
      Objects.requireNonNull(inFunction, "function");
    this.functionNow =
      this.functionDefault;
    this.evaluated =
      Optional.empty();
  }

  @Override
  public Object evaluated()
  {
    return this.evaluated.orElseThrow(IllegalStateException::new);
  }

  @Override
  public void reset()
  {
    this.functionNow = this.functionDefault;
  }

  /**
   * Evaluate the function.
   *
   * @param sourceNode The input
   */

  public void evaluate(
    final SyValueNodeType sourceNode)
  {
    this.evaluated = Optional.of(
      this.functionNow.apply((Double) sourceNode.evaluated())
    );
  }

  /**
   * Set the node transform.
   *
   * @param transform The value
   */

  public void set(
    final SyThemeValueFunctionType<Double> transform)
  {
    this.functionNow = Objects.requireNonNull(transform, "transform");
  }

  @Override
  public double valueNow()
  {
    return (int) this.evaluated();
  }

  @Override
  public String source()
  {
    return this.source;
  }
}
