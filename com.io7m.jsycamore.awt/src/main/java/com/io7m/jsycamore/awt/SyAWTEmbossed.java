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

package com.io7m.jsycamore.awt;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jregions.core.parameterized.areas.PAreaI;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.Optional;

/**
 * Functions to render embossed shapes.
 */

public final class SyAWTEmbossed
{
  private final Polygon poly;

  /**
   * Construct an embossed renderer.
   */

  public SyAWTEmbossed()
  {
    this.poly = new Polygon();
  }

  /**
   * <p>Render an embossed rectangle.</p>
   *
   * @param graphics    A graphics context
   * @param box         The box
   * @param emboss_size The size of the embossed region
   * @param left        The paint used for the left emboss regions
   * @param right       The paint used for the right emboss regions
   * @param top         The paint used for the top emboss regions
   * @param bottom      The paint used for the bottom emboss regions
   * @param fill        The paint used for the fill, if any
   */

  public void rectangle(
    final Graphics2D graphics,
    final PAreaI<?> box,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill)
  {
    Objects.requireNonNull(graphics, "Graphics");
    Objects.requireNonNull(box, "Box");
    Objects.requireNonNull(left, "Left paint");
    Objects.requireNonNull(right, "Right paint");
    Objects.requireNonNull(top, "Top paint");
    Objects.requireNonNull(bottom, "Bottom paint");
    Objects.requireNonNull(fill, "Fill paint");

    Preconditions.checkPreconditionI(
      emboss_size,
      emboss_size > 0,
      i -> "Emboss area size must be positive");

    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();
    final Paint old_paint = graphics.getPaint();

    try {
      final int x = box.minimumX();
      final int y = box.minimumY();
      final int w = box.sizeX();
      final int h = box.sizeY();
      graphics.clipRect(x, y, w, h);
      graphics.translate(x, y);

      /**
       * Light vertical parallelogram on the left.
       */

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(emboss_size, h - emboss_size);
      this.poly.addPoint(0, h);
      graphics.setPaint(left);
      graphics.fill(this.poly);

      /**
       * Light horizontal parallelogram on the top.
       */

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(w - emboss_size, emboss_size);
      this.poly.addPoint(w, 0);
      graphics.setPaint(top);
      graphics.fill(this.poly);

      /**
       * Dark horizontal parallelogram on the bottom.
       */

      this.poly.reset();
      this.poly.addPoint(0, h);
      this.poly.addPoint(emboss_size, h - emboss_size);
      this.poly.addPoint(w - emboss_size, h - emboss_size);
      this.poly.addPoint(w, h);
      graphics.setPaint(bottom);
      graphics.fill(this.poly);

      /**
       * Dark vertical parallelogram on the right.
       */

      this.poly.reset();
      this.poly.addPoint(w, 0);
      this.poly.addPoint(w, h);
      this.poly.addPoint(w - emboss_size, h - emboss_size);
      this.poly.addPoint(w - emboss_size, emboss_size);
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
          w - (emboss_size + emboss_size),
          h - (emboss_size + emboss_size));
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setClip(old_clip);
      graphics.setPaint(old_paint);
    }
  }

  /**
   * <p>Render an embossed L shape.</p>
   *
   * @param graphics                A graphics context
   * @param shape                   The shape that will be rendered
   * @param x                       The leftmost edge
   * @param y                       The topmost edge
   * @param thickness_of_horizontal The thickness of horizontal sections
   * @param thickness_of_vertical   The thickness of vertical sections
   * @param length                  The length of the arms
   * @param emboss_size             The size of the embossed region
   * @param left                    The paint used for the left emboss regions
   * @param right                   The paint used for the right emboss regions
   * @param top                     The paint used for the top emboss regions
   * @param bottom                  The paint used for the bottom emboss regions
   * @param fill                    The paint used for the fill, if any
   * @param caps                    {@code true} iff end caps should be rendered
   */

  public void drawEmbossedL(
    final Graphics2D graphics,
    final LShape shape,
    final int x,
    final int y,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
    final int length,
    final int emboss_size,
    final Paint left,
    final Paint right,
    final Paint top,
    final Paint bottom,
    final Optional<Paint> fill,
    final boolean caps)
  {
    Objects.requireNonNull(graphics, "Graphics context");
    Objects.requireNonNull(shape, "Shape");
    Objects.requireNonNull(left, "Left paint");
    Objects.requireNonNull(right, "Right paint");
    Objects.requireNonNull(top, "Top paint");
    Objects.requireNonNull(bottom, "Bottom paint");
    Objects.requireNonNull(fill, "Fill paint");

    Preconditions.checkPreconditionI(
      thickness_of_horizontal,
      thickness_of_horizontal > 0,
      i -> "Thickness of horizontal sections must be positive");
    Preconditions.checkPreconditionI(
      thickness_of_vertical,
      thickness_of_vertical > 0,
      i -> "Thickness of vertical sections must be positive");
    Preconditions.checkPreconditionI(
      length,
      length > 0,
      i -> "Length must be positive");
    Preconditions.checkPreconditionI(
      emboss_size,
      emboss_size > 0,
      i -> "Embossed area size must be positive");

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
            thickness_of_horizontal,
            thickness_of_vertical,
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
            thickness_of_horizontal,
            thickness_of_vertical,
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
            thickness_of_horizontal,
            thickness_of_vertical,
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
            thickness_of_horizontal,
            thickness_of_vertical,
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
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(top);
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical + emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical + emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          emboss_size,
          emboss_size,
          thickness_of_vertical - (2 * emboss_size),
          thickness_of_horizontal - emboss_size);
        graphics.fillRect(
          thickness_of_vertical - emboss_size,
          emboss_size,
          emboss_size,
          thickness_of_horizontal - (2 * emboss_size));
      }
    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerNE(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical, 0);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, 0);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(left);
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          0,
          emboss_size,
          thickness_of_vertical - emboss_size,
          thickness_of_horizontal - (2 * emboss_size));
        graphics.fillRect(
          emboss_size,
          thickness_of_horizontal - emboss_size,
          thickness_of_vertical - (2 * emboss_size),
          emboss_size);
      }

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerSW(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(bottom);
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical - emboss_size, 0);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical, 0);
      graphics.fill(this.poly);

      graphics.setPaint(top);
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, 0);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical, emboss_size);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          emboss_size,
          0,
          thickness_of_vertical - (2 * emboss_size),
          thickness_of_horizontal - emboss_size);
        graphics.fillRect(
          thickness_of_vertical - emboss_size,
          emboss_size,
          emboss_size,
          thickness_of_horizontal - (2 * emboss_size));
      }
    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerSE(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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
      this.poly.addPoint(0, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(right);
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical - emboss_size, 0);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, 0);
      graphics.fill(this.poly);

      if (fill.isPresent()) {
        graphics.setPaint(fill.get());
        graphics.fillRect(
          emboss_size,
          0,
          thickness_of_vertical - (2 * emboss_size),
          thickness_of_horizontal - emboss_size);
        graphics.fillRect(
          0,
          emboss_size,
          thickness_of_vertical - (2 * emboss_size),
          thickness_of_horizontal - (2 * emboss_size));
      }

    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_SE(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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

      graphics.translate(length - thickness_of_vertical, 0);
      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      graphics.translate(length - emboss_size, 0);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(0, length - thickness_of_horizontal);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(0, length - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      /**
       * Corner
       */

      graphics.translate(
        length - thickness_of_vertical,
        length - thickness_of_horizontal);
      this.drawEmbossedCornerSE(
        graphics,
        thickness_of_horizontal,
        thickness_of_vertical,
        emboss_size,
        left,
        right,
        top,
        bottom,
        fill);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(length + emboss_size - thickness_of_vertical, 0);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness_of_vertical - (2 * emboss_size),
          length - thickness_of_horizontal);
        graphics.setTransform(old_transform);

        graphics.translate(0, length + emboss_size - thickness_of_horizontal);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness_of_vertical,
          thickness_of_horizontal - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /**
       * Caps
       */

      if (caps) {
        graphics.translate(length - thickness_of_vertical, 0);
        this.drawEmbossedCapN(
          graphics,
          thickness_of_vertical,
          emboss_size,
          top);
        graphics.setTransform(old_transform);

        graphics.translate(0, length - thickness_of_horizontal);
        this.drawEmbossedCapW(
          graphics,
          thickness_of_horizontal,
          emboss_size,
          left);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_SW(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);

      graphics.translate(thickness_of_vertical - emboss_size, 0);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(
        thickness_of_vertical,
        length - thickness_of_horizontal);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(thickness_of_vertical, length - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      /**
       * Corner
       */

      graphics.translate(0, length - thickness_of_horizontal);
      this.drawEmbossedCornerSW(
        graphics,
        thickness_of_horizontal,
        thickness_of_vertical,
        emboss_size,
        left,
        right,
        top,
        bottom,
        fill);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(emboss_size, 0);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness_of_vertical - (2 * emboss_size),
          length - thickness_of_horizontal);
        graphics.setTransform(old_transform);

        graphics.translate(
          thickness_of_vertical,
          emboss_size + length - thickness_of_horizontal);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness_of_vertical,
          thickness_of_horizontal - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /**
       * Caps
       */

      if (caps) {
        this.drawEmbossedCapN(
          graphics,
          thickness_of_vertical,
          emboss_size,
          top);
        graphics.translate(
          length - emboss_size,
          length - thickness_of_horizontal);
        this.drawEmbossedCapE(
          graphics,
          thickness_of_horizontal,
          emboss_size,
          right);
        graphics.setTransform(old_transform);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_NE(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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

      graphics.translate(
        length - thickness_of_vertical,
        thickness_of_horizontal);
      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      graphics.translate(length - emboss_size, thickness_of_horizontal);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(0, 0);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(0, thickness_of_horizontal - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(0, emboss_size);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness_of_vertical,
          thickness_of_horizontal - (2 * emboss_size));
        graphics.setTransform(old_transform);

        graphics.translate(
          emboss_size + length - thickness_of_vertical,
          thickness_of_horizontal);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness_of_vertical - (2 * emboss_size),
          length - thickness_of_horizontal);
        graphics.setTransform(old_transform);
      }

      /**
       * Corner
       */

      graphics.translate(length - thickness_of_vertical, 0);
      this.drawEmbossedCornerNE(
        graphics,
        thickness_of_horizontal,
        thickness_of_vertical,
        emboss_size,
        left,
        right,
        top,
        bottom,
        fill);
      graphics.setTransform(old_transform);

      /**
       * Caps
       */

      if (caps) {
        graphics.translate(
          length - thickness_of_vertical,
          length - emboss_size);
        this.drawEmbossedCapS(
          graphics,
          thickness_of_vertical,
          emboss_size,
          bottom);
        graphics.setTransform(old_transform);
        this.drawEmbossedCapW(
          graphics,
          thickness_of_horizontal,
          emboss_size,
          left);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_NW(
    final Graphics2D graphics,
    final int thickness_of_horizontal,
    final int thickness_of_vertical,
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

      graphics.translate(0, thickness_of_horizontal);
      graphics.setPaint(left);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      graphics.translate(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal);
      graphics.setPaint(right);
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /**
       * Horizontal bars
       */

      graphics.translate(thickness_of_vertical, 0);
      graphics.setPaint(top);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(
        thickness_of_vertical,
        thickness_of_horizontal - emboss_size);
      graphics.setPaint(bottom);
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      if (fill.isPresent()) {
        final Paint fill_paint = fill.get();

        graphics.translate(emboss_size, thickness_of_horizontal);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness_of_vertical - (2 * emboss_size),
          length - thickness_of_horizontal);
        graphics.setTransform(old_transform);

        graphics.translate(thickness_of_vertical, emboss_size);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness_of_vertical,
          thickness_of_horizontal - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /**
       * Corner
       */

      this.drawEmbossedCornerNW(
        graphics,
        thickness_of_horizontal,
        thickness_of_vertical,
        emboss_size,
        left,
        right,
        top,
        bottom,
        fill);
      graphics.setTransform(old_transform);

      /**
       * Caps
       */

      if (caps) {
        graphics.translate(length - emboss_size, 0);
        this.drawEmbossedCapE(
          graphics,
          thickness_of_horizontal,
          emboss_size,
          right);
        graphics.setTransform(old_transform);

        graphics.translate(0, length - emboss_size);
        this.drawEmbossedCapS(
          graphics,
          thickness_of_vertical,
          emboss_size,
          bottom);
      }

    } finally {
      graphics.setTransform(old_transform);
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
}
