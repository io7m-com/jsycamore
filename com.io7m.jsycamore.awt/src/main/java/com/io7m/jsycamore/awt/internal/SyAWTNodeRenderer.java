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


package com.io7m.jsycamore.awt.internal;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.rendering.SyPaintEdgeType;
import com.io7m.jsycamore.api.rendering.SyPaintFillType;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.rendering.SyPaintGradientLinear;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeImage;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapePolygon;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Polygon;
import java.util.Optional;

/**
 * An AWT node renderer.
 */

public final class SyAWTNodeRenderer
{
  private final SyAWTImageLoader imageLoader;

  /**
   * An AWT node renderer.
   */

  public SyAWTNodeRenderer()
  {
    this.imageLoader = new SyAWTImageLoader();
  }

  private static void renderShapeRectangle(
    final Graphics2D graphics,
    final Optional<SyPaintEdgeType> edge,
    final Optional<SyPaintFillType> fill,
    final SyShapeRectangle<SySpaceComponentRelativeType> rectangle)
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

  private static void renderShapePolygon(
    final Graphics2D graphics,
    final Optional<SyPaintEdgeType> edge,
    final Optional<SyPaintFillType> fill,
    final SyShapePolygon<SySpaceComponentRelativeType> polygon)
  {
    final var awtPoly = awtPolygon(polygon);

    fill.ifPresent(fillPaint -> {
      final var area = polygon.boundingArea();
      graphics.setPaint(fillToPaint(area, fillPaint));
      graphics.fillPolygon(awtPoly);
    });

    edge.ifPresent(edgePaint -> {
      final var area = polygon.boundingArea();
      graphics.setPaint(edgeToPaint(area, edgePaint));
      graphics.drawPolygon(awtPoly);
    });
  }

  private static Polygon awtPolygon(
    final SyShapePolygon<SySpaceComponentRelativeType> polygon)
  {
    final var xp = new int[polygon.points().size()];
    final var yp = new int[polygon.points().size()];
    for (int index = 0; index < xp.length; ++index) {
      final var pp =
        polygon.points().get(index);
      xp[index] = pp.x();
      yp[index] = pp.y();
    }
    return new Polygon(xp, yp, xp.length);
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
    final var r = Math.min(1.0, Math.max(0.0, color.x()));
    final var g = Math.min(1.0, Math.max(0.0, color.y()));
    final var b = Math.min(1.0, Math.max(0.0, color.z()));
    final var a = Math.min(1.0, Math.max(0.0, color.w()));
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

  /**
   * Render the given node.
   *
   * @param graphics2D The graphics context
   * @param renderNode The render node
   */

  public void renderNode(
    final Graphics2D graphics2D,
    final SyRenderNodeType renderNode)
  {
    if (renderNode instanceof SyRenderNodeShape shape) {
      renderNodeShape(graphics2D, shape);
      return;
    }
    if (renderNode instanceof SyRenderNodeText text) {
      renderNodeText(graphics2D, text);
      return;
    }
    if (renderNode instanceof SyRenderNodeImage image) {
      this.renderNodeImage(graphics2D, image);
      return;
    }
    if (renderNode instanceof SyRenderNodeComposite composite) {
      for (final var node : composite.nodes()) {
        this.renderNode(graphics2D, node);
      }
      return;
    }
  }

  private void renderNodeImage(
    final Graphics2D g,
    final SyRenderNodeImage image)
  {
    final var request =
      new SyAWTImageRequest(
        image.image(),
        image.size().sizeX(),
        image.size().sizeY()
      );

    final var imageData = this.imageLoader.load(request);
    g.drawImage(imageData, 0, 0, null);
  }

  private static void renderNodeText(
    final Graphics2D g,
    final SyRenderNodeText textNode)
  {
    final var text = textNode.text();
    if (text.isEmpty()) {
      return;
    }

    final var size = textNode.size();
    final var sizeX = size.sizeX();
    final var sizeY = size.sizeY();
    final var area =
      PAreasI.<SySpaceType>create(0, 0, sizeX, sizeY);

    final var font =
      Font.decode(textNode.font().description().identifier());

    final var metrics =
      g.getFontMetrics(font);
    final var width =
      metrics.stringWidth(text);
    final var lineMetrics =
      metrics.getLineMetrics(text, g);

    final var x = (sizeX / 2) - (width / 2);
    final var y = lineMetrics.getHeight() - (lineMetrics.getDescent() / 2.0f);

    g.setFont(font);
    g.setPaint(fillToPaint(area, textNode.fillPaint()));
    g.drawString(text, x, y);
  }

  private static void renderNodeShape(
    final Graphics2D g,
    final SyRenderNodeShape shape)
  {
    if (shape.shape() instanceof SyShapeRectangle<SySpaceComponentRelativeType> rectangle) {
      renderShapeRectangle(g, shape.edgePaint(), shape.fillPaint(), rectangle);
      return;
    }
    if (shape.shape() instanceof SyShapePolygon<SySpaceComponentRelativeType> polygon) {
      renderShapePolygon(g, shape.edgePaint(), shape.fillPaint(), polygon);
      return;
    }
  }
}
