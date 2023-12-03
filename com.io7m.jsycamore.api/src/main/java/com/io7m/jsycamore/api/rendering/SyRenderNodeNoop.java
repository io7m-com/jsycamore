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


package com.io7m.jsycamore.api.rendering;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

/**
 * A no-op render node.
 */

public record SyRenderNodeNoop() implements SyRenderNodePrimitiveType
{
  private static final PAreaSizeI<SySpaceComponentRelativeType> ZERO_SIZE =
    PAreaSizeI.of(0, 0);

  private static final SyRenderNodeNoop NOOP =
    new SyRenderNodeNoop();

  /**
   * @return A no-op render node.
   */

  public static SyRenderNodeNoop noop()
  {
    return NOOP;
  }

  @Override
  public String name()
  {
    return "NoOp";
  }

  @Override
  public PVector2I<SySpaceComponentRelativeType> position()
  {
    return PVectors2I.zero();
  }

  @Override
  public PAreaSizeI<SySpaceComponentRelativeType> size()
  {
    return ZERO_SIZE;
  }
}
