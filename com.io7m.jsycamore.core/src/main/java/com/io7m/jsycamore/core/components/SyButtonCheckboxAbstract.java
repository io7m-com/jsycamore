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
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.themes.SyThemeButtonCheckboxType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * An abstract implementation of the {@link SyButtonType} interface.
 */

@NotThreadSafe
public abstract class SyButtonCheckboxAbstract extends SyComponentAbstract implements
  SyButtonCheckboxType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyButtonCheckboxAbstract.class);
  }

  private final List<SyButtonListenerType> listeners;
  private Optional<SyImageType> check_icon;
  private boolean pressed;
  private boolean over;
  private boolean checked;

  protected SyButtonCheckboxAbstract(
    final BooleanSupplier in_detach_check)
  {
    super(in_detach_check);
    this.listeners = new ArrayList<>(4);
    this.check_icon = Optional.empty();
  }

  protected final boolean isPressed()
  {
    return this.pressed;
  }

  protected final boolean isOver()
  {
    return this.over;
  }

  @Override
  public final void buttonAddListener(final SyButtonListenerType r)
  {
    this.listeners.add(NullCheck.notNull(r, "Button listener"));
  }

  @Override
  public final void buttonRemoveListener(final SyButtonListenerType r)
  {
    this.listeners.remove(NullCheck.notNull(r, "Button listener"));
  }

  @Override
  public final SyButtonState buttonState()
  {
    if (this.pressed) {
      return SyButtonState.BUTTON_PRESSED;
    }

    if (this.over) {
      return SyButtonState.BUTTON_OVER;
    }

    return SyButtonState.BUTTON_ACTIVE;
  }

  @Override
  public final boolean mouseHeld(
    final PVector2I<SySpaceViewportType> mouse_position_first,
    final PVector2I<SySpaceViewportType> mouse_position_now,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position_first, "Mouse first position");
    NullCheck.notNull(mouse_position_now, "Mouse current position");
    NullCheck.notNull(button, "Mouse button");
    NullCheck.notNull(actual, "Component");

    if (SyButtonCheckboxAbstract.LOG.isTraceEnabled()) {
      SyButtonCheckboxAbstract.LOG.trace(
        "mouseHeld: {} {} {} {}",
        mouse_position_first,
        mouse_position_now,
        button,
        actual);
    }

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.window()
          .flatMap(window -> window.componentForViewportPosition(
            mouse_position_now))
          .flatMap(component -> {
            this.over = Objects.equals(component, this);
            return Optional.empty();
          });
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        break;
      }
    }

    return false;
  }

  @Override
  public final boolean mousePressed(
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position, "Mouse position");
    NullCheck.notNull(button, "Mouse button");
    NullCheck.notNull(actual, "Component");

    if (SyButtonCheckboxAbstract.LOG.isTraceEnabled()) {
      SyButtonCheckboxAbstract.LOG.trace(
        "mousePressed: {} {} {}", mouse_position, button, actual);
    }

    this.over = true;
    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        this.pressed = true;
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
  public final boolean isChecked()
  {
    return this.checked;
  }

  @Override
  public final void setChecked(final boolean c)
  {
    this.checked = c;
    this.updateIcon();
  }

  @Override
  public final boolean mouseReleased(
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyMouseButton button,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position, "Mouse position");
    NullCheck.notNull(button, "Mouse button");
    NullCheck.notNull(actual, "Component");

    if (SyButtonCheckboxAbstract.LOG.isTraceEnabled()) {
      SyButtonCheckboxAbstract.LOG.trace(
        "mouseReleased: {} {} {} {}", mouse_position, button, actual);
    }

    switch (button) {
      case MOUSE_BUTTON_LEFT: {
        if (this.pressed && this.over) {
          try {
            this.setChecked(!this.checked);
            this.clicked();
          } finally {

            /*
             * Both "pressed" and "over" are cancelled here because if the
             * button is moved for any reason by the result of pressing the
             * button, there will not be a "mouse no longer" over event
             * delivered to the button. A concrete example of this is when
             * the button represents a window close box: The window will be
             * closed and therefore the button will not receive any subsequent
             * mouse events after the button is pressed.
             */

            this.pressed = false;
            this.over = false;
          }
        }

        this.pressed = false;
        return true;
      }
      case MOUSE_BUTTON_MIDDLE:
      case MOUSE_BUTTON_RIGHT: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  protected void buttonOnClick()
  {

  }

  private void clicked()
  {
    try {
      this.buttonOnClick();
    } catch (final Throwable e) {
      SyErrors.ignoreNonErrors(SyButtonCheckboxAbstract.LOG, e);
    }

    for (final SyButtonListenerType x : this.listeners) {
      try {
        x.buttonClicked(this);
      } catch (final Throwable e) {
        SyErrors.ignoreNonErrors(SyButtonCheckboxAbstract.LOG, e);
      }
    }
  }

  @Override
  public final void themeHasChanged()
  {
    this.updateIcon();
  }

  private void updateIcon()
  {
    if (this.check_icon.isPresent()) {
      final SyImageType icon = this.check_icon.get();
      this.node().childRemove(icon.node());
      this.check_icon = Optional.empty();
    }

    if (this.checked) {
      this.theme()
        .flatMap(SyThemeButtonCheckboxType::checkedIcon)
        .flatMap(image_spec -> {

          final SyBoxType<SySpaceParentRelativeType> box = this.box();
          final SyImageType icon = SyImage.create(image_spec);
          icon.setImageAlignmentHorizontal(SyAlignmentHorizontal.ALIGN_CENTER);
          icon.setImageAlignmentVertical(SyAlignmentVertical.ALIGN_CENTER);
          icon.setBox(SyBoxes.create(0, 0, box.width(), box.height()));
          this.node().childAdd(icon.node());
          this.check_icon = Optional.of(icon);
          return Optional.empty();
        });
    }
  }

  @Override
  public final boolean mouseNoLongerOver()
  {
    SyButtonCheckboxAbstract.LOG.trace("mouseNoLongerOver");
    this.over = false;
    this.pressed = false;
    return true;
  }

  @Override
  public final boolean mouseOver(
    final PVector2I<SySpaceViewportType> mouse_position,
    final SyComponentType actual)
  {
    NullCheck.notNull(mouse_position, "Mouse position");
    NullCheck.notNull(actual, "Component");

    if (SyButtonCheckboxAbstract.LOG.isTraceEnabled()) {
      SyButtonCheckboxAbstract.LOG.trace(
        "mouseOver: {} {}",
        mouse_position,
        actual);
    }

    this.over = true;
    this.pressed = false;
    return true;
  }
}
