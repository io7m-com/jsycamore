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
import com.io7m.jsycamore.api.rendering.SyPaintEdgeType;
import com.io7m.jsycamore.api.rendering.SyPaintFillType;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.rendering.SyPaintGradientLinear;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeImage;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodePrimitiveType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeComposite;
import com.io7m.jsycamore.api.rendering.SyShapePolygon;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyFontException;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.junreachable.UnimplementedCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * An AWT node renderer.
 */

public final class SyAWTNodeRenderer
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyAWTNodeRenderer.class);

  private final SyAWTImageLoader imageLoader;
  private final SyFontDirectoryServiceType<SyAWTFont> fontDirectory;
  private boolean debugBounds;
  private boolean textAntialias;

  /**
   * An AWT node renderer.
   *
   * @param inFontDirectory The font directory
   * @param inImageLoader   The image loader
   */

  public SyAWTNodeRenderer(
    final SyAWTImageLoader inImageLoader,
    final SyFontDirectoryServiceType<SyAWTFont> inFontDirectory)
  {
    this.imageLoader =
      Objects.requireNonNull(inImageLoader, "imageLoader");
    this.fontDirectory =
      Objects.requireNonNull(inFontDirectory, "fontDirectory");
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
    return switch (fillPaint) {
      case final SyPaintFlat flat -> {
        yield fillToPaintFlat(flat);
      }
      case final SyPaintGradientLinear linear -> {
        yield fillToPaintGradientLinear(boundingArea, linear);
      }
    };
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
      fractions,
      colors
    );
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
    return switch (edgePaint) {
      case final SyPaintFlat flat -> {
        yield edgeToPaintFlat(flat);
      }
      case final SyPaintGradientLinear linear -> {
        throw new UnimplementedCodeException();
      }
    };
  }

  private static Paint edgeToPaintFlat(
    final SyPaintFlat flat)
  {
    return toColor4(flat.color());
  }

  private void renderNodeText(
    final Graphics2D g,
    final SyFontDirectoryServiceType<SyAWTFont> fonts,
    final SyRenderNodeText textNode)
  {
    final var text = textNode.text();
    if (text.value().isEmpty()) {
      return;
    }

    final var size = textNode.size();
    final var sizeX = size.sizeX();
    final var sizeY = size.sizeY();

    try {
      final var font =
        fonts.get(textNode.font().description());

      final var width =
        font.textWidth(text.value());

      final int x =
        switch (text.direction()) {
          case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
            yield 0;
          }
          case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
            yield sizeX - width;
          }
        };


      final int y = font.textHeight() - (font.textDescent());

      if (this.textAntialias) {
        g.setRenderingHint(
          RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
      } else {
        g.setRenderingHint(
          RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        );
      }

      g.setFont(font.font());
      g.setPaint(fillToPaint(textNode.boundingArea(), textNode.fillPaint()));
      g.drawString(text.value(), x, y);
    } catch (final SyFontException e) {
      LOG.error("Error rendering text: ", e);
      g.setPaint(Color.RED);
      g.fillRect(0, 0, sizeX, sizeY);
    }
  }

  private static void renderNodeShape(
    final Graphics2D g,
    final SyRenderNodeShape shape)
  {
    switch (shape.shape()) {
      case final SyShapeRectangle<SySpaceComponentRelativeType> r -> {
        renderShapeRectangle(g, shape.edgePaint(), shape.fillPaint(), r);
      }
      case final SyShapePolygon<SySpaceComponentRelativeType> p -> {
        renderShapePolygon(g, shape.edgePaint(), shape.fillPaint(), p);
      }
      case final SyShapeComposite<?> c -> {

      }
    }
  }

  /**
   * Enable/disable debug bounds rendering.
   *
   * @param enabled {@code true} if bounds should be rendered
   */

  public void setDebugBoundsRendering(
    final boolean enabled)
  {
    this.debugBounds = enabled;
  }

  /**
   * Enable/disable debug bounds rendering.
   *
   * @param enabled {@code true} if text should be antialiased
   */

  public void setTextAntialiasing(
    final boolean enabled)
  {
    this.textAntialias = enabled;
  }

  private static void pushTransform(
    final Graphics2D graphics2D,
    final Consumer<Graphics2D> f)
  {
    final var saved = graphics2D.getTransform();
    try {
      f.accept(graphics2D);
    } finally {
      graphics2D.setTransform(saved);
    }
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
    pushTransform(graphics2D, g -> {
      final var nodes =
        flattenNodes(renderNode).toList();

      for (final var node : nodes) {
        pushTransform(g, g2 -> {
          final var position = node.position();
          g2.translate(position.x(), position.y());
          this.renderNodePrimitive(g2, node);
        });
      }
    });
  }

  private void renderNodePrimitive(
    final Graphics2D g,
    final SyRenderNodePrimitiveType node)
  {
    g.setComposite(AlphaComposite.SrcOver);

    switch (node) {
      case final SyRenderNodeImage n -> {
        this.renderNodeImage(g, n);
      }
      case final SyRenderNodeNoop n -> {

      }
      case final SyRenderNodeShape n -> {
        renderNodeShape(g, n);
      }
      case final SyRenderNodeText n -> {
        this.renderNodeText(g, this.fontDirectory, n);
      }
    }

    if (this.debugBounds) {
      g.setColor(Color.RED);
      g.drawRect(
        0, 0, node.size().sizeX(), node.size().sizeY()
      );
    }
  }

  private static Stream<SyRenderNodePrimitiveType> flattenNodes(
    final SyRenderNodeType renderNode)
  {
    return Stream.of(renderNode)
      .flatMap(n -> {
        return switch (n) {
          case final SyRenderNodeComposite m ->
            m.nodes().stream().flatMap(SyAWTNodeRenderer::flattenNodes);
          case final SyRenderNodeImage m -> Stream.of(m);
          case final SyRenderNodeNoop m -> Stream.of(m);
          case final SyRenderNodeShape m -> Stream.of(m);
          case final SyRenderNodeText m -> Stream.of(m);
        };
      });
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
}
