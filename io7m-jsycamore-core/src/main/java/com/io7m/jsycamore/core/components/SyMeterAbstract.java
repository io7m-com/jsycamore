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

package com.io7m.jsycamore.core.components;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SyOrientation;
import com.io7m.jsycamore.core.SySpaceComponentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SySpaceWindowRelativeType;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import com.io7m.junreachable.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;

/**
 * An abstract implementation of the {@link SyMeterType} interface.
 */

public abstract class SyMeterAbstract extends SyComponentAbstract implements
  SyMeterType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyMeterAbstract.class);
  }

  private double value;
  private SyOrientation orientation;

  protected SyMeterAbstract(
    final BooleanSupplier in_detach_check)
  {
    super(in_detach_check);
    this.value = 0.0;
    this.orientation = SyOrientation.ORIENTATION_HORIZONTAL;
  }

  @Override
  public final SyOrientation orientation()
  {
    return this.orientation;
  }

  @Override
  public final void setOrientation(final SyOrientation o)
  {
    this.orientation = NullCheck.notNull(o, "Orientation");
  }

  @Override
  public final void setValue(final double x)
  {
    this.value = Math.max(Math.min(1.0, x), 0.0);
  }

  @Override
  public final double value()
  {
    return this.value;
  }

  @Override
  public final boolean mouseHeld(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
    final PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position_first);
    NullCheck.notNull(mouse_position_now);
    NullCheck.notNull(button);
    NullCheck.notNull(actual);

    if (SyMeterAbstract.LOG.isTraceEnabled()) {
      SyMeterAbstract.LOG.trace(
        "mouseHeld: {} {} {} {}",
        mouse_position_first,
        mouse_position_now,
        button,
        actual);
    }

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.updateValueForMouse(mouse_position_now);
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  @Override
  public final boolean mousePressed(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position);
    NullCheck.notNull(button);
    NullCheck.notNull(actual);

    if (SyMeterAbstract.LOG.isTraceEnabled()) {
      SyMeterAbstract.LOG.trace(
        "mousePressed: {} {} {}", mouse_position, button, actual);
    }

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.updateValueForMouse(mouse_position);
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  private void updateValueForMouse(
    final PVectorReadable2IType<SySpaceViewportType> mouse_position)
  {
    this.window().ifPresent(window -> {
      final PVectorM2I<SySpaceWindowRelativeType> mouse_pos_window =
        new PVectorM2I<>();
      final PVectorM2I<SySpaceComponentRelativeType> mouse_pos_component =
        new PVectorM2I<>();

      window.transformViewportRelative(mouse_position, mouse_pos_window);
      this.transformWindowRelative(mouse_pos_window, mouse_pos_component);

      switch (this.orientation) {
        case ORIENTATION_HORIZONTAL: {
          final double w = (double) this.box().width();
          final double x = (double) mouse_pos_component.getXI();
          this.setValue(x / w);
          break;
        }
        case ORIENTATION_VERTICAL: {
          final double h = (double) this.box().height();
          final double y = h - (double) mouse_pos_component.getYI();
          this.setValue(y / h);
          break;
        }
      }

      if (SyMeterAbstract.LOG.isTraceEnabled()) {
        SyMeterAbstract.LOG.trace("value: {}", Double.valueOf(this.value()));
      }
    });
  }
}
