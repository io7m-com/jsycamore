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

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jattribute.core.AttributeType;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentQuery;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowType;
import com.io7m.jsycamore.api.windows.SyWindowEventType;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulator;
import com.io7m.jsycamore.api.windows.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Objects;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * An abstract default implementation of the {@link SyWindowType} type.
 */

public final class SyWindow implements SyWindowType
{
  private final SyWindowViewportAccumulatorType viewportAccumulator;
  private final SyWindowRoot root;
  private final SyScreenType screen;
  private final AttributeType<PVector2I<SySpaceViewportType>> positionAttribute;
  private final AttributeType<PAreaSizeI<SySpaceViewportType>> sizeAttribute;
  private final AttributeType<Boolean> maximized;
  private final AttributeType<Boolean> decorated;
  private final AttributeType<String> titleText;
  private final SyWindowID id;
  private PVector2I<SySpaceViewportType> position;
  private PVector2I<SySpaceViewportType> positionMaximized;
  private PAreaSizeI<SySpaceViewportType> size;
  private PAreaSizeI<SySpaceViewportType> sizeMaximized;
  private SyConstraints constraints;

  SyWindow(
    final SyScreenType inScreen,
    final SyWindowID inId,
    final PAreaSizeI<SySpaceViewportType> inSize)
  {
    this.screen =
      Objects.requireNonNull(inScreen, "inGUI");
    this.id =
      Objects.requireNonNull(inId, "inId");
    this.size =
      Objects.requireNonNull(inSize, "inSize");
    this.sizeMaximized =
      Objects.requireNonNull(inSize, "inSize");

    this.position =
      PVectors2I.zero();
    this.positionMaximized =
      PVectors2I.zero();

    this.constraints =
      new SyConstraints(0, 0, this.size.sizeX(), this.size.sizeY());
    this.viewportAccumulator =
      SyWindowViewportAccumulator.create();

    final var attributes = SyWindowAttributes.get();
    this.maximized =
      attributes.create(FALSE);
    this.decorated =
      attributes.create(TRUE);

    this.positionAttribute =
      this.maximized.map(isMaximized -> {
        if (!isMaximized) {
          return this.position;
        }
        return this.positionMaximized;
      });

    this.sizeAttribute =
      this.maximized.map(isMaximized -> {
        if (!isMaximized) {
          return this.size;
        }
        return this.sizeMaximized;
      });

    this.root = new SyWindowRoot();
    this.root.setWindow(Optional.of(this));
    this.setSize(inSize);

    this.titleText = this.root.title().titleText();
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[SyWindow ");
    sb.append(this.id.value());
    sb.append(' ');
    PAreasI.showToBuilder(this.boundingArea(), sb);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public SyWindowID id()
  {
    return this.id;
  }

  @Override
  public JOTreeNodeReadableType<SyComponentReadableType> rootNodeReadable()
  {
    return this.root.nodeReadable();
  }

  @Override
  public AttributeReadableType<Boolean> maximized()
  {
    return this.maximized;
  }

  @Override
  public void setPosition(
    final PVector2I<SySpaceViewportType> newPosition)
  {
    Objects.requireNonNull(newPosition, "position");

    if (this.maximized.get()) {
      this.positionMaximized = newPosition;
    } else {
      this.position = newPosition;
    }
  }

  @Override
  public void setSize(
    final PAreaSizeI<SySpaceViewportType> newSize)
  {
    Objects.requireNonNull(newSize, "size");

    final var clampedSize =
      PAreaSizeI.<SySpaceViewportType>of(
        Math.max(0, newSize.sizeX()),
        Math.max(0, newSize.sizeY())
      );

    if (this.maximized.get()) {
      this.sizeMaximized = clampedSize;
    } else {
      this.size = clampedSize;
    }

    final var sizeX = clampedSize.sizeX();
    final var sizeY = clampedSize.sizeY();
    this.root.size().set(PAreaSizeI.of(sizeX, sizeY));
    this.viewportAccumulator.reset(sizeX, sizeY);
    this.constraints =
      new SyConstraints(0, 0, sizeX, sizeY);
  }

  @Override
  public SyComponentType contentArea()
  {
    return this.root.contentArea();
  }

  @Override
  public AttributeType<String> title()
  {
    return this.titleText;
  }

  @Override
  public SyScreenType screen()
  {
    return this.screen;
  }

  @Override
  public void layout(
    final SyLayoutContextType layoutContext)
  {
    Objects.requireNonNull(layoutContext, "layoutContext");
    this.root.layout(layoutContext, this.constraints);
  }

  @Override
  public void eventSend(
    final SyWindowEventType event)
  {

  }

  private boolean isInBoundsWindowRelative(
    final PVector2I<SySpaceWindowType> windowPosition)
  {
    final var bounds = this.boundingArea();
    final int targetX = Math.addExact(windowPosition.x(), bounds.minimumX());
    final int targetY = Math.addExact(windowPosition.y(), bounds.minimumY());
    return PAreasI.containsPoint(bounds, targetX, targetY);
  }

  @Override
  public PVector2I<SySpaceWindowType> transformViewportRelative(
    final PVector2I<SySpaceViewportType> viewportPosition)
  {
    Objects.requireNonNull(viewportPosition, "Position");

    return PVector2I.of(
      Math.subtractExact(viewportPosition.x(), this.boundingArea().minimumX()),
      Math.subtractExact(viewportPosition.y(), this.boundingArea().minimumY())
    );
  }

  @Override
  public Optional<SyComponentType> componentForViewportPosition(
    final PVector2I<SySpaceViewportType> viewportPosition,
    final SyComponentQuery query)
  {
    Objects.requireNonNull(viewportPosition, "Position");
    Objects.requireNonNull(query, "query");

    return this.componentForWindowPosition(
      this.transformViewportRelative(viewportPosition),
      query
    );
  }

  @Override
  public Optional<SyComponentType> componentForWindowPosition(
    final PVector2I<SySpaceWindowType> windowPosition,
    final SyComponentQuery query)
  {
    Objects.requireNonNull(windowPosition, "windowPosition");
    Objects.requireNonNull(query, "query");

    if (this.isInBoundsWindowRelative(windowPosition)) {
      return this.root.componentForWindowRelative(
        windowPosition,
        this.viewportAccumulator,
        query
      );
    }

    return Optional.empty();
  }

  @Override
  public void setMaximizeToggle(
    final PAreaSizeI<SySpaceViewportType> viewportSize)
  {
    Objects.requireNonNull(viewportSize, "viewportSize");

    if (this.maximized.get()) {
      this.maximized.set(FALSE);
      this.setSize(this.size);
    } else {
      this.maximized.set(TRUE);
      this.setPosition(PVectors2I.zero());
      this.setSize(viewportSize);
    }
  }

  @Override
  public AttributeType<Boolean> decorated()
  {
    return this.decorated;
  }

  @Override
  public AttributeReadableType<PVector2I<SySpaceViewportType>> position()
  {
    return this.positionAttribute;
  }

  @Override
  public AttributeReadableType<PAreaSizeI<SySpaceViewportType>> size()
  {
    return this.sizeAttribute;
  }
}
