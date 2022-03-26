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

import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyScreenType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.rendering.SyPaintEdgeType;
import com.io7m.jsycamore.api.rendering.SyPaintFillType;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.rendering.SyPaintGradientLinear;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.Optional;

/**
 * An AWT renderer.
 */

public final class SyAWTRenderer implements SyRendererType
{
  private final SyFontDirectoryType fonts;
  private final ThemeContext themeContext;

  /**
   * An AWT renderer.
   *
   * @param inFonts A font directory
   */

  public SyAWTRenderer(
    final SyFontDirectoryType inFonts)
  {
    this.fonts = Objects.requireNonNull(inFonts, "fonts");
    this.themeContext = new ThemeContext(this.fonts);
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

  @Override
  public void render(
    final Graphics2D g,
    final SyScreenType screen,
    final SyWindowType window)
  {
    final var oldTransform = g.getTransform();
    final var oldClip = g.getClip();

    try {
      final var bounds = window.boundingArea();
      g.setTransform(
        AffineTransform.getTranslateInstance(
          bounds.minimumX(),
          bounds.minimumY())
      );
      g.setClip(0, 0, bounds.sizeX(), bounds.sizeY());

      final var root = window.rootNodeReadable();
      this.renderComponent(g, screen, root);
    } finally {
      g.setTransform(oldTransform);
      g.setClip(oldClip);
    }
  }

  private void renderComponent(
    final Graphics2D g,
    final SyScreenType gui,
    final JOTreeNodeReadableType<SyComponentReadableType> node)
  {
    final var component = node.value();
    if (!component.isVisible()) {
      return;
    }

    final var oldTransform = g.getTransform();
    final var oldClip = g.getClip();

    try {
      final var bounds = component.boundingArea();
      g.translate(bounds.minimumX(), bounds.minimumY());
      g.clipRect(0, 0, bounds.sizeX() + 1, bounds.sizeY() + 1);

      final var componentTheme =
        gui.theme().findForComponent(component);
      final var renderNode =
        componentTheme.render(this.themeContext, component);

      this.renderNode(g, renderNode);

      final var children =
        node.childrenReadable();

      for (final var child : children) {
        this.renderComponent(g, gui, child);
      }
    } finally {
      g.setTransform(oldTransform);
      g.setClip(oldClip);
    }
  }

  private void renderNode(
    final Graphics2D g,
    final SyRenderNodeType renderNode)
  {
    if (renderNode instanceof SyRenderNodeShape shape) {
      this.renderNodeShape(g, shape);
      return;
    }
    if (renderNode instanceof SyRenderNodeText text) {
      this.renderNodeText(g, text);
      return;
    }
  }

  private void renderNodeText(
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
      Font.decode(textNode.font().identifier());

    final var metrics =
      g.getFontMetrics(font);
    final var width =
      metrics.stringWidth(text);
    final var lineMetrics =
      metrics.getLineMetrics(text, g);

    final var x = (sizeX / 2) - (width / 2);
    final var y = (sizeY / 2) + (lineMetrics.getHeight() / 2);

    g.setFont(font);
    g.setPaint(fillToPaint(area, textNode.fillPaint()));
    g.drawString(text, x, y);
  }

  private void renderNodeShape(
    final Graphics2D g,
    final SyRenderNodeShape shape)
  {
    if (shape.shape() instanceof SyShapeRectangle<SySpaceComponentRelativeType> rectangle) {
      renderShapeRectangle(g, shape.edgePaint(), shape.fillPaint(), rectangle);
      return;
    }
  }

  private static final class ThemeContext implements SyThemeContextType
  {
    private SyFontDirectoryType fonts;

    ThemeContext(
      final SyFontDirectoryType inFonts)
    {
      this.fonts = Objects.requireNonNull(inFonts, "fonts");
    }

    @Override
    public SyFontDirectoryType fonts()
    {
      return this.fonts;
    }
  }
}
