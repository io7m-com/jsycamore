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

package com.io7m.jsycamore.awt.internal;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.rendering.SyPaintEdgeType;
import com.io7m.jsycamore.api.rendering.SyPaintFillType;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.rendering.SyPaintGradientLinear;
import com.io7m.jsycamore.api.rendering.SyPaintedGroups;
import com.io7m.jsycamore.api.rendering.SyPaintedShape;
import com.io7m.jsycamore.api.rendering.SyShapeComposite;
import com.io7m.jsycamore.api.rendering.SyShapePolygon;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.rendering.SyShapeType;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

/**
 * An AWT shape renderer.
 */

public final class SyAWTShapeRenderer
{
  /**
   * An AWT shape renderer.
   */

  public SyAWTShapeRenderer()
  {

  }

  private static <T extends SySpaceType> void renderPaintedShape(
    final Graphics2D graphics,
    final SyPaintedShape<T> painted)
  {
    renderShape(
      graphics,
      painted.edgePaint(),
      painted.fillPaint(),
      painted.shape()
    );
  }

  private static <T extends SySpaceType> void renderShape(
    final Graphics2D graphics,
    final Optional<SyPaintEdgeType> edge,
    final Optional<SyPaintFillType> fill,
    final SyShapeType<T> shape)
  {
    if (shape instanceof SyShapeRectangle<T> rectangle) {
      renderShapeRectangle(graphics, edge, fill, rectangle);
      return;
    }

    if (shape instanceof SyShapePolygon<T> polygon) {
      renderShapePolygon(graphics, edge, fill, polygon);
      return;
    }

    if (shape instanceof SyShapeComposite<T> composite) {
      for (final var subShape : composite.shapes()) {
        renderShape(graphics, edge, fill, subShape);
      }
    }
  }

  private static <T extends SySpaceType> void renderShapePolygon(
    final Graphics2D graphics,
    final Optional<SyPaintEdgeType> edge,
    final Optional<SyPaintFillType> fill,
    final SyShapePolygon<T> polygon)
  {
    final var area = polygon.boundingArea();
    final var points = polygon.points();
    final var size = points.size();
    final var xpoints = new int[size];
    final var ypoints = new int[size];
    for (int index = 0; index < size; ++index) {
      xpoints[index] = points.get(index).x();
      ypoints[index] = points.get(index).y();
    }

    final var p =
      new Polygon(xpoints, ypoints, size);

    fill.ifPresent(fillPaint -> {
      graphics.setPaint(fillToPaint(area, fillPaint));
      graphics.fillPolygon(p);
    });

    edge.ifPresent(edgePaint -> {
      graphics.setPaint(edgeToPaint(area, edgePaint));
      graphics.drawPolygon(p);
    });
  }

  private static <T extends SySpaceType> void renderShapeRectangle(
    final Graphics2D graphics,
    final Optional<SyPaintEdgeType> edge,
    final Optional<SyPaintFillType> fill,
    final SyShapeRectangle<T> rectangle)
  {
    fill.ifPresent(fillPaint -> {
      final var area = rectangle.area();
      graphics.setPaint(fillToPaint(area, fillPaint));
      graphics.fillRect(
        area.minimumX(),
        area.minimumY(),
        area.sizeX(),
        area.sizeY()
      );
    });

    edge.ifPresent(edgePaint -> {
      final var area = rectangle.area();
      graphics.setPaint(edgeToPaint(area, edgePaint));
      graphics.drawRect(
        area.minimumX(),
        area.minimumY(),
        area.sizeX(),
        area.sizeY()
      );
    });
  }

  private static <T extends SySpaceType> Paint fillToPaint(
    final PAreaI<T> boundingArea,
    final SyPaintFillType fillPaint)
  {
    if (fillPaint instanceof SyPaintFlat flat) {
      return fillToPaintFlat(flat);
    }

    if (fillPaint instanceof SyPaintGradientLinear linear) {
      return fillToPaintGradientLinear(boundingArea, linear);
    }

    throw new UnreachableCodeException();
  }

  private static Color toColor4(
    final PVector4D<SySpaceRGBAPreType> color)
  {
    final double r = Math.min(1.0, Math.max(0.0, color.x()));
    final double g = Math.min(1.0, Math.max(0.0, color.y()));
    final double b = Math.min(1.0, Math.max(0.0, color.z()));
    final double a = Math.min(1.0, Math.max(0.0, color.w()));
    return new Color((float) r, (float) g, (float) b, (float) a);
  }

  private static <T extends SySpaceType> Paint fillToPaintGradientLinear(
    final PAreaI<T> boundingArea,
    final SyPaintGradientLinear linear)
  {
    final var size = linear.colors().size();
    final var fractions = new float[size];
    final var colors = new Color[size];

    final var distribution = linear.distribution();
    final var colorList = linear.colors();
    for (var index = 0; index < size; ++index) {
      fractions[index] = distribution.get(index).floatValue();
      colors[index] = toColor4(colorList.get(index));
    }

    final var x = (double) boundingArea.minimumX();
    final var y = (double) boundingArea.minimumY();
    final var w = (double) boundingArea.sizeX();
    final var h = (double) boundingArea.sizeY();

    final var p0 = linear.point0();
    final var p1 = linear.point1();

    final var x0 = x + (p0.x() * w);
    final var y0 = y + (p0.y() * h);
    final var x1 = x + (p1.x() * w);
    final var y1 = y + (p1.y() * h);

    return new LinearGradientPaint(
      (float) x0, (float) y0,
      (float) x1, (float) y1,
      fractions, colors);
  }

  private static Paint fillToPaintFlat(
    final SyPaintFlat flat)
  {
    return toColor4(flat.color());
  }

  private static <T extends SySpaceType> Paint edgeToPaint(
    final PAreaI<T> boundingArea,
    final SyPaintEdgeType edgePaint)
  {
    if (edgePaint instanceof SyPaintFlat flat) {
      return edgeToPaintFlat(flat);
    }

    throw new UnreachableCodeException();
  }

  private static Paint edgeToPaintFlat(
    final SyPaintFlat flat)
  {
    return toColor4(flat.color());
  }

  private static <T extends SySpaceType> Shape toShape(
    final SyShapeType<T> shape)
  {
    if (shape instanceof SyShapeRectangle rectangle) {
      final var area = rectangle.boundingArea();
      return new Rectangle2D.Double(
        (double) area.minimumX(),
        (double) area.minimumY(),
        (double) area.sizeX(),
        (double) area.sizeY()
      );
    }

    throw new UnreachableCodeException();
  }

  /**
   * Render a set of painted groups.
   *
   * @param graphics The graphics context
   * @param groups   The painted groups
   * @param <T>      The coordinate system
   */

  public <T extends SySpaceType> void renderGroups(
    final Graphics2D graphics,
    final SyPaintedGroups<T> groups)
  {
    for (final var group : groups.groups()) {
      final var existingClip = graphics.getClip();
      try {
        group.clipShape()
          .map(SyAWTShapeRenderer::toShape)
          .ifPresent(graphics::setClip);

        for (final var shape : group.shapesInOrder()) {
          renderPaintedShape(graphics, shape);
        }
      } finally {
        graphics.setClip(existingClip);
      }
    }
  }
}
