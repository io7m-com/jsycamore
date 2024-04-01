/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.components.standard.internal.scrollbars;

import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyScrollBarDrag;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.keyboard.SyKeyEventType;
import com.io7m.jsycamore.api.keyboard.SyKeyboardFocusBehavior;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyAlign;
import com.io7m.jsycamore.components.standard.SyComponentAbstract;
import com.io7m.jsycamore.components.standard.SyImageView;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.io7m.jsycamore.api.components.SyScrollBarDrag.Kind.DRAG_CONTINUED;
import static com.io7m.jsycamore.api.components.SyScrollBarDrag.Kind.DRAG_ENDED;
import static com.io7m.jsycamore.api.components.SyScrollBarDrag.Kind.DRAG_STARTED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLBAR_HORIZONTAL_BUTTON_THUMB;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.SCROLLBAR_HORIZONTAL_BUTTON_THUMB_ICON;
import static com.io7m.jsycamore.components.standard.SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER;
import static com.io7m.jsycamore.components.standard.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

final class SyScrollBarHButtonThumb
  extends SyComponentAbstract
  implements SyButtonReadableType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyScrollBarHButtonThumb.class);

  private final SyImageView image;
  private final SyAlign align;
  private final SyScrollBarHTrack track;
  private boolean pressed;
  private Consumer<SyScrollBarDrag> onDragListener;
  private double dragScrollStart;
  private double scrollThen;

  SyScrollBarHButtonThumb(
    final SyScreenType screen,
    final SyScrollBarHTrack inTrack)
  {
    super(
      screen,
      List.of(),
      SyKeyboardFocusBehavior.IGNORES_FOCUS_AND_STOPS_TRAVERSAL
    );

    this.track =
      Objects.requireNonNull(inTrack, "track");

    this.image =
      new SyImageView(screen, List.of(SCROLLBAR_HORIZONTAL_BUTTON_THUMB_ICON));
    this.image.setImageURI("jsycamore:icon:scroll_h_thumb");
    this.image.setMouseQueryAccepting(false);

    this.align = new SyAlign(screen);
    this.align.alignmentHorizontal().set(ALIGN_HORIZONTAL_CENTER);
    this.align.alignmentVertical().set(ALIGN_VERTICAL_CENTER);
    this.align.childAdd(this.image);
    this.childAdd(this.align);

    this.onDragListener = (ignored) -> {

    };
  }

  void setOnThumbDragListener(
    final Consumer<SyScrollBarDrag> listener)
  {
    this.onDragListener =
      Objects.requireNonNull(listener, "listener");
  }

  void removeOnThumbDragListener()
  {
    this.onDragListener = (ignored) -> {

    };
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SCROLLBAR_HORIZONTAL_BUTTON_THUMB);
  }

  @Override
  public boolean isPressed()
  {
    return this.pressed;
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return switch (event) {
      case final SyMouseEventType e -> {
        yield this.onMouseEvent(e);
      }
      case final SyKeyEventType e -> EVENT_NOT_CONSUMED;
    };
  }

  private SyEventConsumed onMouseEvent(
    final SyMouseEventType event)
  {
    return switch (event) {
      case final SyMouseEventOnHeld e -> {
        yield switch (e.button()) {
          case MOUSE_BUTTON_LEFT -> {
            yield this.onMouseDragged(e);
          }
          case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
            yield EVENT_NOT_CONSUMED;
          }
        };
      }

      case final SyMouseEventOnNoLongerOver e -> {
        yield EVENT_NOT_CONSUMED;
      }

      case final SyMouseEventOnOver e -> {
        yield EVENT_NOT_CONSUMED;
      }

      case final SyMouseEventOnPressed e -> {
        yield switch (e.button()) {
          case MOUSE_BUTTON_LEFT -> {
            yield this.onMousePressed();
          }
          case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
            yield EVENT_NOT_CONSUMED;
          }
        };
      }

      case final SyMouseEventOnReleased e -> {
        yield switch (e.button()) {
          case MOUSE_BUTTON_LEFT -> {
            yield this.onMouseReleased();
          }
          case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
            yield EVENT_NOT_CONSUMED;
          }
        };
      }
    };
  }

  private SyEventConsumed onMouseReleased()
  {
    this.pressed = false;
    this.publishToDragListener(DRAG_ENDED);
    return EVENT_CONSUMED;
  }

  private SyEventConsumed onMousePressed()
  {
    this.pressed = true;
    this.dragScrollStart = this.track.scrollPosition();
    this.publishToDragListener(DRAG_STARTED);
    return EVENT_CONSUMED;
  }

  private SyEventConsumed onMouseDragged(
    final SyMouseEventOnHeld e)
  {
    this.pressed = true;

    final var mouseThen =
      e.mousePositionFirst();
    final var mouseNow =
      e.mousePositionNow();
    final var mouseDelta =
      PVectors2I.subtract(mouseNow, mouseThen);

    final var trackSize =
      this.track.size().get().sizeX();
    final var trackDelta =
      (double) mouseDelta.x() / (double) trackSize;

    this.track.setScrollPosition(this.dragScrollStart + trackDelta);
    this.publishToDragListener(DRAG_CONTINUED);
    this.scrollThen = this.track.scrollPosition();
    return EVENT_CONSUMED;
  }

  private void publishToDragListener(
    final SyScrollBarDrag.Kind dragKind)
  {
    try {
      final var shouldPublish = switch (dragKind) {
        case DRAG_STARTED -> true;
        case DRAG_CONTINUED -> this.scrollThen != this.track.scrollPosition();
        case DRAG_ENDED -> true;
      };

      if (shouldPublish) {
        this.onDragListener.accept(
          new SyScrollBarDrag(
            dragKind,
            this.dragScrollStart,
            this.track.scrollPosition()
          )
        );
      }
    } catch (final Throwable ex) {
      LOG.debug("Ignored listener exception: ", ex);
    }
  }
}
