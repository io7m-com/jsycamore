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

package com.io7m.jsycamore.core.renderer;

import com.io7m.jnull.NullCheck;
import org.valid4j.Assertive;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Optional;

public final class SyEmbossed
{
  private final Polygon poly;

  public SyEmbossed()
  {
    this.poly = new Polygon();
  }

  /**
   * <p>Render an embossed rectangle.</p>
   *
   * @param graphics A graphics context
   * @param x        The left rectangle edge
   * @param y        The top rectangle edge
   * @param width    The width
   * @param height   The height
   * @param fill     The paint with which to fill the center, if filling should
   *                 occur
   */

  public void rectangle(
    final Graphics2D graphics,
    final int x,
    final int y,
    final int width,
    final int height,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill)
  {
    NullCheck.notNull(graphics);
    NullCheck.notNull(left);
    NullCheck.notNull(right);
    NullCheck.notNull(top);
    NullCheck.notNull(bottom);
    NullCheck.notNull(fill);

    Assertive.require(width > 0, "Width must be positive");
    Assertive.require(height > 0, "Height must be positive");
    Assertive.require(emboss_size > 0, "Emboss area size must be positive");

    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();
    final Paint old_paint = graphics.getPaint();

    try {
      graphics.clipRect(x, y, width, height);
      graphics.translate(x, y);

      /**
       * Light vertical parallelogram on the left.
       */

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(emboss_size, height - emboss_size);
      this.poly.addPoint(0, height);
      graphics.setPaint(left);
      graphics.fill(this.poly);

      /**
       * Light horizontal parallelogram on the top.
       */

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(width - emboss_size, emboss_size);
      this.poly.addPoint(width, 0);
      graphics.setPaint(top);
      graphics.fill(this.poly);

      /**
       * Dark horizontal parallelogram on the bottom.
       */

      this.poly.reset();
      this.poly.addPoint(0, height);
      this.poly.addPoint(emboss_size, height - emboss_size);
      this.poly.addPoint(width - emboss_size, height - emboss_size);
      this.poly.addPoint(width, height);
      graphics.setPaint(bottom);
      graphics.fill(this.poly);

      /**
       * Dark vertical parallelogram on the right.
       */

      this.poly.reset();
      this.poly.addPoint(width, 0);
      this.poly.addPoint(width, height);
      this.poly.addPoint(width - emboss_size, height - emboss_size);
      this.poly.addPoint(width - emboss_size, emboss_size);
      graphics.setPaint(right);
      graphics.fill(this.poly);

      /**
       * Fill the center, if necessary.
       */

      if (fill.isPresent()) {
        final Paint f = fill.get();
        graphics.setPaint(f);
        graphics.fillRect(
          emboss_size,
          emboss_size,
          width - (emboss_size + emboss_size),
          height - (emboss_size + emboss_size));
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
      graphics.setPaint(old_paint);
    }
  }

  /**
   * The type of L shape
   */

  public enum LShape
  {
    /**
     * ┌
     */

    L_SHAPE_NW,

    /**
     * ┐
     */

    L_SHAPE_NE,

    /**
     * └
     */

    L_SHAPE_SW,

    /**
     * ┘
     */

    L_SHAPE_SE
  }

  /**
   * <p>Render an embossed L shape.</p>
   *
   * @param graphics    A graphics context
   * @param shape       The type of L shape
   * @param x           The x coordinate of the top left corner
   * @param y           The y coordinate of the top left corner
   * @param thickness   The thickness of the body of the L
   * @param length      The length of the arms of the L
   * @param emboss_size The size of the embossed regions
   * @param fill        The paint used to fill the body of the L, if filling
   *                    should occur
   * @param caps        {@code true} iff end caps should be rendered
   */

  public void drawEmbossedL(
    final Graphics2D graphics,
    final LShape shape,
    final int x,
    final int y,
    final int thickness,
    final int length,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill,
    final boolean caps)
  {
    NullCheck.notNull(graphics);
    NullCheck.notNull(shape);
    NullCheck.notNull(left);
    NullCheck.notNull(right);
    NullCheck.notNull(top);
    NullCheck.notNull(bottom);
    NullCheck.notNull(fill);

    Assertive.require(thickness > 0, "Thickness must be positive");
    Assertive.require(length > 0, "Length must be positive");
    Assertive.require(emboss_size > 0, "Embossed area size must be positive");

    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();
    final Paint old_paint = graphics.getPaint();

    try {
      graphics.clipRect(x, y, length, length);
      graphics.translate(x, y);

      switch (shape) {
        case L_SHAPE_NW: {
          this.drawEmbossedL_NW(
            graphics,
            thickness,
            length,
            emboss_size,
            left,
            right,
            top,
            bottom,
            fill,
            caps);
          break;
        }
        case L_SHAPE_NE: {
          this.drawEmbossedL_NE(
            graphics,
            thickness,
            length,
            emboss_size,
            left,
            right,
            top,
            bottom,
            fill,
            caps);
          break;
        }
        case L_SHAPE_SW: {
          this.drawEmbossedL_SW(
            graphics,
            thickness,
            length,
            emboss_size,
            left,
            right,
            top,
            bottom,
            fill,
            caps);
          break;
        }
        case L_SHAPE_SE: {
          this.drawEmbossedL_SE(
            graphics,
            thickness,
            length,
            emboss_size,
            left,
            right,
            top,
            bottom,
            fill,
            caps);
          break;
        }
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCapN(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint paint)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(paint);

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(thickness - emboss_size, emboss_size);
      this.poly.addPoint(thickness, 0);
      graphics.fill(this.poly);

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCapS(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint paint)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(paint);

      this.poly.reset();
      this.poly.addPoint(emboss_size, 0);
      this.poly.addPoint(0, emboss_size);
      this.poly.addPoint(thickness, emboss_size);
      this.poly.addPoint(thickness - emboss_size, 0);
      graphics.fill(this.poly);

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCapE(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint paint)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(paint);

      this.poly.reset();
      this.poly.addPoint(0, emboss_size);
      this.poly.addPoint(0, thickness - emboss_size);
      this.poly.addPoint(emboss_size, thickness);
      this.poly.addPoint(emboss_size, 0);
      graphics.fill(this.poly);

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCapW(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint paint)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(paint);

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(emboss_size, thickness - emboss_size);
      this.poly.addPoint(0, thickness);
      graphics.fill(this.poly);

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerNW(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(left);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, thickness);
      this.poly.addPoint(emboss_size, thickness);
      this.poly.addPoint(emboss_size, emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(top);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(thickness, emboss_size);
      this.poly.addPoint(thickness, 0);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(thickness, thickness);
      this.poly.addPoint(thickness, thickness - emboss_size);
      this.poly.addPoint(thickness - emboss_size, thickness - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness, thickness);
      this.poly.addPoint(thickness - emboss_size, thickness - emboss_size);
      this.poly.addPoint(thickness - emboss_size, thickness);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          emboss_size,
          emboss_size,
          thickness - (2 * emboss_size),
          thickness - emboss_size);
        graphics.fillRect(
          thickness - emboss_size,
          emboss_size,
          emboss_size,
          thickness - (2 * emboss_size));
      }

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerNE(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(top);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, emboss_size);
      this.poly.addPoint(thickness - emboss_size, emboss_size);
      this.poly.addPoint(thickness, 0);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness, 0);
      this.poly.addPoint(thickness - emboss_size, emboss_size);
      this.poly.addPoint(thickness - emboss_size, thickness);
      this.poly.addPoint(thickness, thickness);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(0, thickness - emboss_size);
      this.poly.addPoint(0, thickness);
      this.poly.addPoint(emboss_size, thickness - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(left);
      this.poly.reset();
      this.poly.addPoint(0, thickness);
      this.poly.addPoint(emboss_size, thickness);
      this.poly.addPoint(emboss_size, thickness - emboss_size);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          0,
          emboss_size,
          thickness - emboss_size,
          thickness - (2 * emboss_size));
        graphics.fillRect(
          emboss_size,
          thickness - emboss_size,
          thickness - (2 * emboss_size),
          emboss_size);
      }

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerSW(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(left);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, thickness);
      this.poly.addPoint(emboss_size, thickness - emboss_size);
      this.poly.addPoint(emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(0, thickness);
      this.poly.addPoint(thickness, thickness);
      this.poly.addPoint(thickness, thickness - emboss_size);
      this.poly.addPoint(emboss_size, thickness - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness - emboss_size, 0);
      this.poly.addPoint(thickness - emboss_size, emboss_size);
      this.poly.addPoint(thickness, 0);
      graphics.fill(this.poly);

      graphics.setPaint(top);
      this.poly.reset();
      this.poly.addPoint(thickness, 0);
      this.poly.addPoint(thickness - emboss_size, emboss_size);
      this.poly.addPoint(thickness, emboss_size);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          emboss_size,
          0,
          thickness - (2 * emboss_size),
          thickness - emboss_size);
        graphics.fillRect(
          thickness - emboss_size,
          emboss_size,
          emboss_size,
          thickness - (2 * emboss_size));
      }
    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerSE(
    final Graphics2D graphics,
    final int thickness,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill)
  {
    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(top);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, emboss_size);
      this.poly.addPoint(emboss_size, emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(left);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(0, thickness - emboss_size);
      this.poly.addPoint(0, thickness);
      this.poly.addPoint(thickness, thickness);
      this.poly.addPoint(thickness - emboss_size, thickness - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness - emboss_size, 0);
      this.poly.addPoint(thickness - emboss_size, thickness - emboss_size);
      this.poly.addPoint(thickness, thickness);
      this.poly.addPoint(thickness, 0);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          emboss_size,
          0,
          thickness - (2 * emboss_size),
          thickness - emboss_size);
        graphics.fillRect(
          0,
          emboss_size,
          thickness - (2 * emboss_size),
          thickness - (2 * emboss_size));
      }

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_SE(
    final Graphics2D graphics,
    final int thickness,
    final int length,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill,
    final boolean caps)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /**
       * Vertical bars
       */

      graphics.translate(length - thickness, 0);
      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      graphics.translate(length - emboss_size, 0);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(0, length - thickness);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(0, length - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      /**
       * Corner
       */

      graphics.translate(length - thickness, length - thickness);
      this.drawEmbossedCornerSE(
        graphics, thickness, emboss_size, left, right, top, bottom, fill);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(length + emboss_size - thickness, 0);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness - (2 * emboss_size),
          length - thickness);
        graphics.setTransform(old_transform);

        graphics.translate(0, length + emboss_size - thickness);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness,
          thickness - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /**
       * Caps
       */

      if (caps) {
        graphics.translate(length - thickness, 0);
        this.drawEmbossedCapN(graphics, thickness, emboss_size, top);
        graphics.setTransform(old_transform);

        graphics.translate(0, length - thickness);
        this.drawEmbossedCapW(graphics, thickness, emboss_size, left);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_SW(
    final Graphics2D graphics,
    final int thickness,
    final int length,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill,
    final boolean caps)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /**
       * Vertical bars
       */

      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness);

      graphics.translate(thickness - emboss_size, 0);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(thickness, length - thickness);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(thickness, length - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      /**
       * Corner
       */

      graphics.translate(0, length - thickness);
      this.drawEmbossedCornerSW(
        graphics, thickness, emboss_size, left, right, top, bottom, fill);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(emboss_size, 0);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness - (2 * emboss_size),
          length - thickness);
        graphics.setTransform(old_transform);

        graphics.translate(thickness, emboss_size + length - thickness);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness,
          thickness - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /**
       * Caps
       */

      if (caps) {
        this.drawEmbossedCapN(graphics, thickness, emboss_size, top);
        graphics.translate(length - emboss_size, length - thickness);
        this.drawEmbossedCapE(graphics, thickness, emboss_size, right);
        graphics.setTransform(old_transform);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_NE(
    final Graphics2D graphics,
    final int thickness,
    final int length,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill,
    final boolean caps)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /**
       * Vertical bars
       */

      graphics.translate(length - thickness, thickness);
      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      graphics.translate(length - emboss_size, thickness);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(0, 0);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(0, thickness - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(0, emboss_size);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness,
          thickness - (2 * emboss_size));
        graphics.setTransform(old_transform);

        graphics.translate(emboss_size + length - thickness, thickness);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness - (2 * emboss_size),
          length - thickness);
        graphics.setTransform(old_transform);
      }

      /**
       * Corner
       */

      graphics.translate(length - thickness, 0);
      this.drawEmbossedCornerNE(
        graphics, thickness, emboss_size, left, right, top, bottom, fill);
      graphics.setTransform(old_transform);

      /**
       * Caps
       */

      if (caps) {
        graphics.translate(length - thickness, length - emboss_size);
        this.drawEmbossedCapS(graphics, thickness, emboss_size, bottom);
        graphics.setTransform(old_transform);
        this.drawEmbossedCapW(graphics, thickness, emboss_size, left);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_NW(
    final Graphics2D graphics,
    final int thickness,
    final int length,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill,
    final boolean caps)
  {
    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /**
       * Vertical bars
       */

      graphics.translate(0, thickness);
      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      graphics.translate(thickness - emboss_size, thickness);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(thickness, 0);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(thickness, thickness - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness, emboss_size);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(emboss_size, thickness);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness - (2 * emboss_size),
          length - thickness);
        graphics.setTransform(old_transform);

        graphics.translate(thickness, emboss_size);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness,
          thickness - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /**
       * Corner
       */

      this.drawEmbossedCornerNW(
        graphics, thickness, emboss_size, left, right, top, bottom, fill);
      graphics.setTransform(old_transform);

      /**
       * Caps
       */

      if (caps) {
        graphics.translate(length - emboss_size, 0);
        this.drawEmbossedCapE(graphics, thickness, emboss_size, right);
        graphics.setTransform(old_transform);

        graphics.translate(0, length - emboss_size);
        this.drawEmbossedCapS(graphics, thickness, emboss_size, bottom);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

}
