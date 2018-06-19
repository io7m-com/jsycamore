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

package com.io7m.jsycamore.api.themes;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * The type of window frame theme parameters.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyThemeWindowFrameType
{
  /**
   * Check the preconditions for the parameters.
   */

  @Value.Check
  default void checkPreconditions()
  {
    if (this.embossActive().isPresent()) {
      this.checkTopLeft();
      this.checkTopRight();
      this.checkBottomLeft();
      this.checkBottomRight();
    }
  }

  /**
   * Check the bottom right style.
   */

  private void checkBottomRight()
  {
    switch (this.bottomRightStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        this.checkEmbossedBottomRightL();
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }
  }

  /**
   * Check the bottom left style.
   */

  private void checkBottomLeft()
  {
    switch (this.bottomLeftStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        this.checkEmbossedBottomLeftL();
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }
  }

  /**
   * Check the top right style.
   */

  private void checkTopRight()
  {
    switch (this.topRightStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        this.checkEmbossedTopRightL();
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }
  }

  /**
   * Check the top left style.
   */

  private void checkTopLeft()
  {
    switch (this.topLeftStyle()) {
      case FRAME_CORNER_NONE: {
        break;
      }
      case FRAME_CORNER_L_PIECE: {
        this.checkEmbossedTopLeftL();
        break;
      }
      case FRAME_CORNER_BOX: {
        break;
      }
    }
  }

  /**
   * Check the embossed bottom left L corner.
   */

  private void checkEmbossedBottomLeftL()
  {
    Preconditions.checkPreconditionI(
      this.bottomHeight(),
      this.bottomHeight() > 0,
      i -> "An embossed bottom-left L corner requires a non-zero bottom frame");
    Preconditions.checkPreconditionI(
      this.leftWidth(),
      this.leftWidth() > 0,
      i -> "An embossed bottom-left L corner requires a non-zero left frame");
  }

  /**
   * Check the embossed top right L corner.
   */

  private void checkEmbossedTopRightL()
  {
    Preconditions.checkPreconditionI(
      this.topHeight(),
      this.topHeight() > 0,
      i -> "An embossed top-right L corner requires a non-zero top frame");
    Preconditions.checkPreconditionI(
      this.rightWidth(),
      this.rightWidth() > 0,
      i -> "An embossed top-right L corner requires a non-zero right frame");
  }

  /**
   * Check the embossed bottom right L corner.
   */

  private void checkEmbossedBottomRightL()
  {
    Preconditions.checkPreconditionI(
      this.bottomHeight(),
      this.bottomHeight() > 0,
      i -> "An embossed bottom-right L corner requires a non-zero bottom frame");
    Preconditions.checkPreconditionI(
      this.rightWidth(),
      this.rightWidth() > 0,
      i -> "An embossed bottom-right L corner requires a non-zero right frame");
  }

  /**
   * Check the embossed top left L corner.
   */

  private void checkEmbossedTopLeftL()
  {
    Preconditions.checkPreconditionI(
      this.topHeight(),
      this.topHeight() > 0,
      i -> "An embossed top-left L corner requires a non-zero top frame");
    Preconditions.checkPreconditionI(
      this.leftWidth(),
      this.leftWidth() > 0,
      i -> "An embossed top-left L corner requires a non-zero left frame");
  }

  /**
   * @return The width of the left edge of window frames
   */

  @Value.Parameter
  int leftWidth();

  /**
   * @return The width of the right edge of window frames
   */

  @Value.Parameter
  int rightWidth();

  /**
   * @return The width of the top edge of window frames
   */

  @Value.Parameter
  int topHeight();

  /**
   * @return The width of the bottom edge of window frames
   */

  @Value.Parameter
  int bottomHeight();

  /**
   * @return The embossing used when windows are active
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossActive();

  /**
   * @return The embossing used when windows are inactive
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossInactive();

  /**
   * @return The style used for the bottom left frame corner
   */

  @Value.Parameter
  SyThemeWindowFrameCorner bottomLeftStyle();

  /**
   * @return The style used for the bottom right frame corner
   */

  @Value.Parameter
  SyThemeWindowFrameCorner bottomRightStyle();

  /**
   * @return The style used for the top left frame corner
   */

  @Value.Parameter
  SyThemeWindowFrameCorner topLeftStyle();

  /**
   * @return The style used for the top right frame corner
   */

  @Value.Parameter
  SyThemeWindowFrameCorner topRightStyle();

  /**
   * @return The base color used for active window frames
   */

  @Value.Parameter
  Vector3D colorActive();

  /**
   * @return The base color used for inactive window frames
   */

  @Value.Parameter
  Vector3D colorInactive();

  /**
   * @return The frame outline, if any
   */

  @Value.Parameter
  Optional<SyThemeOutline> outline();
}
