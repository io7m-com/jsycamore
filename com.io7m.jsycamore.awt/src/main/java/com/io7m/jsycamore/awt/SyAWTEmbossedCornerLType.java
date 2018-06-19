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

package com.io7m.jsycamore.awt;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jaffirm.core.Preconditions;
import org.immutables.value.Value;

import java.awt.Paint;
import java.util.Optional;

import static com.io7m.jsycamore.awt.SyAWTEmbossed.LShape;

/**
 * Parameters for drawing embossed L-shaped corners.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyAWTEmbossedCornerLType
{
  /**
   * @return The corner shape
   */

  @Value.Parameter
  LShape shape();

  /**
   * @return The X component of paintTop paintLeft corner of the bounding box
   */

  @Value.Parameter
  int x();

  /**
   * @return The Y component of paintTop paintLeft corner of the bounding box
   */

  @Value.Parameter
  int y();

  /**
   * @return The thickness of horizontal sections
   */

  @Value.Parameter
  int thicknessOfHorizontal();

  /**
   * @return The thickness of vertical sections
   */

  @Value.Parameter
  int thicknessOfVertical();

  /**
   * @return The length of the arms of the corner
   */

  @Value.Parameter
  int armLength();

  /**
   * @return The size of the embossed region
   */

  @Value.Parameter
  int embossSize();

  /**
   * @return The paint used for the paintLeft emboss
   */

  @Value.Parameter
  Paint paintLeft();

  /**
   * @return The paint used for the paintRight emboss
   */

  @Value.Parameter
  Paint paintRight();

  /**
   * @return The paint used for the paintTop emboss
   */

  @Value.Parameter
  Paint paintTop();

  /**
   * @return The paint used for the paintBottom emboss
   */

  @Value.Parameter
  Paint paintBottom();

  /**
   * @return The paint used to paintFill the corner, if filling is required
   */

  @Value.Parameter
  Optional<Paint> paintFill();

  /**
   * @return {@code true} iff end caps should be rendered
   */

  @Value.Parameter
  boolean caps();

  /**
   * Check preconditions for the type.
   */

  @Value.Check
  default void checkPreconditions()
  {
    Preconditions.checkPreconditionI(
      this.thicknessOfHorizontal(),
      this.thicknessOfHorizontal() > 0,
      i -> "Thickness of horizontal sections must be positive");

    Preconditions.checkPreconditionI(
      this.thicknessOfVertical(),
      this.thicknessOfVertical() > 0,
      i -> "Thickness of vertical sections must be positive");

    Preconditions.checkPreconditionI(
      this.armLength(),
      this.armLength() > 0,
      i -> "Length must be positive");

    Preconditions.checkPreconditionI(
      this.embossSize(),
      this.embossSize() > 0,
      i -> "Embossed area size must be positive");
  }
}
