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
   * @param graphics  A graphics context
   * @param rectangle The rectangle
   */

  public void rectangle(
    final Graphics2D graphics,
    final SyAWTEmbossedRectangle rectangle)
  {
    Objects.requireNonNull(rectangle, "rectangle");

    final PAreaI<?> box = rectangle.box();
    final int emboss_size = rectangle.embossSize();

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

      /*
       * Light vertical parallelogram on the paintLeft.
       */

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(emboss_size, h - emboss_size);
      this.poly.addPoint(0, h);
      graphics.setPaint(rectangle.paintLeft());
      graphics.fill(this.poly);

      /*
       * Light horizontal parallelogram on the paintTop.
       */

      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(w - emboss_size, emboss_size);
      this.poly.addPoint(w, 0);
      graphics.setPaint(rectangle.paintTop());
      graphics.fill(this.poly);

      /*
       * Dark horizontal parallelogram on the paintBottom.
       */

      this.poly.reset();
      this.poly.addPoint(0, h);
      this.poly.addPoint(emboss_size, h - emboss_size);
      this.poly.addPoint(w - emboss_size, h - emboss_size);
      this.poly.addPoint(w, h);
      graphics.setPaint(rectangle.paintBottom());
      graphics.fill(this.poly);

      /*
       * Dark vertical parallelogram on the paintRight.
       */

      this.poly.reset();
      this.poly.addPoint(w, 0);
      this.poly.addPoint(w, h);
      this.poly.addPoint(w - emboss_size, h - emboss_size);
      this.poly.addPoint(w - emboss_size, emboss_size);
      graphics.setPaint(rectangle.paintRight());
      graphics.fill(this.poly);

      /*
       * Fill the center, if necessary.
       */

      if (rectangle.paintFill().isPresent()) {
        final Paint f = rectangle.paintFill().get();
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
   * @param graphics A graphics context
   * @param corner   The corner that will be rendered
   */

  public void drawEmbossedL(
    final Graphics2D graphics,
    final SyAWTEmbossedCornerL corner)
  {
    Objects.requireNonNull(graphics, "Graphics context");
    Objects.requireNonNull(corner, "Shape");

    final AffineTransform old_transform = graphics.getTransform();
    final Shape old_clip = graphics.getClip();
    final Paint old_paint = graphics.getPaint();

    try {
      graphics.clipRect(corner.x(), corner.y(), corner.armLength(), corner.armLength());
      graphics.translate(corner.x(), corner.y());

      switch (corner.shape()) {
        case L_SHAPE_NW: {
          this.drawEmbossedL_NW(graphics, corner);
          break;
        }
        case L_SHAPE_NE: {
          this.drawEmbossedL_NE(graphics, corner);
          break;
        }
        case L_SHAPE_SW: {
          this.drawEmbossedL_SW(graphics, corner);
          break;
        }
        case L_SHAPE_SE: {
          this.drawEmbossedL_SE(graphics, corner);
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
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_NW,
      s -> "L-Shape must be L_SHAPE_NW");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();

    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(corner.paintLeft());
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintTop());
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical + emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical + emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintBottom());
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintRight());
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(thickness_of_vertical - emboss_size, thickness_of_horizontal);
      graphics.fill(this.poly);

      if (corner.paintFill().isPresent()) {
        graphics.setPaint(corner.paintFill().get());
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
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_NE,
      s -> "L-Shape must be L_SHAPE_NE");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();

    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(corner.paintTop());
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, emboss_size);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical, 0);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintRight());
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, 0);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintBottom());
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintLeft());
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      final Optional<Paint> fill_opt = corner.paintFill();
      fill_opt.ifPresent(fill -> {
        graphics.setPaint(fill);
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
      });
    } finally {
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedCornerSW(
    final Graphics2D graphics,
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_SW,
      s -> "L-Shape must be L_SHAPE_SW");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();

    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(corner.paintLeft());
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintBottom());
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(emboss_size, thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintRight());
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical - emboss_size, 0);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical, 0);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintTop());
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical, 0);
      this.poly.addPoint(thickness_of_vertical - emboss_size, emboss_size);
      this.poly.addPoint(thickness_of_vertical, emboss_size);
      graphics.fill(this.poly);

      if (corner.paintFill().isPresent()) {
        graphics.setPaint(corner.paintFill().get());
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
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_SE,
      s -> "L-Shape must be L_SHAPE_SE");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();

    final Paint old_paint = graphics.getPaint();
    try {
      graphics.setPaint(corner.paintTop());
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(0, emboss_size);
      this.poly.addPoint(emboss_size, emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintLeft());
      this.poly.reset();
      this.poly.addPoint(0, 0);
      this.poly.addPoint(emboss_size, emboss_size);
      this.poly.addPoint(emboss_size, 0);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintBottom());
      this.poly.reset();
      this.poly.addPoint(0, thickness_of_horizontal - emboss_size);
      this.poly.addPoint(0, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      graphics.fill(this.poly);

      graphics.setPaint(corner.paintRight());
      this.poly.reset();
      this.poly.addPoint(thickness_of_vertical - emboss_size, 0);
      this.poly.addPoint(
        thickness_of_vertical - emboss_size,
        thickness_of_horizontal - emboss_size);
      this.poly.addPoint(thickness_of_vertical, thickness_of_horizontal);
      this.poly.addPoint(thickness_of_vertical, 0);
      graphics.fill(this.poly);

      if (corner.paintFill().isPresent()) {
        graphics.setPaint(corner.paintFill().get());
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
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_SE,
      s -> "L-Shape must be L_SHAPE_SE");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();
    final int length = corner.armLength();

    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /*
       * Vertical bars
       */

      graphics.translate(length - thickness_of_vertical, 0);
      graphics.setPaint(corner.paintLeft());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      graphics.translate(length - emboss_size, 0);
      graphics.setPaint(corner.paintRight());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /*
       * Horizontal bars
       */

      graphics.translate(0, length - thickness_of_horizontal);
      graphics.setPaint(corner.paintTop());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(0, length - emboss_size);
      graphics.setPaint(corner.paintBottom());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      /*
       * Corner
       */

      graphics.translate(
        length - thickness_of_vertical,
        length - thickness_of_horizontal);
      this.drawEmbossedCornerSE(graphics, corner);
      graphics.setTransform(old_transform);

      if (corner.paintFill().isPresent()) {
        final Paint fill_paint = corner.paintFill().get();

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

      /*
       * Caps
       */

      if (corner.caps()) {
        graphics.translate(length - thickness_of_vertical, 0);
        this.drawEmbossedCapN(graphics, thickness_of_vertical, emboss_size, corner.paintTop());
        graphics.setTransform(old_transform);

        graphics.translate(0, length - thickness_of_horizontal);
        this.drawEmbossedCapW(graphics, thickness_of_horizontal, emboss_size, corner.paintLeft());
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_SW(
    final Graphics2D graphics,
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_SW,
      s -> "L-Shape must be L_SHAPE_SW");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();
    final int length = corner.armLength();

    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /*
       * Vertical bars
       */

      graphics.setPaint(corner.paintLeft());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);

      graphics.translate(thickness_of_vertical - emboss_size, 0);
      graphics.setPaint(corner.paintRight());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /*
       * Horizontal bars
       */

      graphics.translate(thickness_of_vertical, length - thickness_of_horizontal);
      graphics.setPaint(corner.paintTop());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(thickness_of_vertical, length - emboss_size);
      graphics.setPaint(corner.paintBottom());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      /*
       * Corner
       */

      graphics.translate(0, length - thickness_of_horizontal);
      this.drawEmbossedCornerSW(graphics, corner);
      graphics.setTransform(old_transform);

      if (corner.paintFill().isPresent()) {
        final Paint fill_paint = corner.paintFill().get();

        graphics.translate(emboss_size, 0);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          thickness_of_vertical - (2 * emboss_size),
          length - thickness_of_horizontal);
        graphics.setTransform(old_transform);

        graphics.translate(thickness_of_vertical, emboss_size + length - thickness_of_horizontal);
        graphics.setPaint(fill_paint);
        graphics.fillRect(
          0,
          0,
          length - thickness_of_vertical,
          thickness_of_horizontal - (2 * emboss_size));
        graphics.setTransform(old_transform);
      }

      /*
       * Caps
       */

      if (corner.caps()) {
        this.drawEmbossedCapN(graphics, thickness_of_vertical, emboss_size, corner.paintTop());
        graphics.translate(
          length - emboss_size,
          length - thickness_of_horizontal);
        this.drawEmbossedCapE(graphics, thickness_of_horizontal, emboss_size, corner.paintRight());
        graphics.setTransform(old_transform);
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_NE(
    final Graphics2D graphics,
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_NE,
      s -> "L-Shape must be L_SHAPE_NE");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();
    final int length = corner.armLength();

    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /*
       * Vertical bars
       */

      graphics.translate(length - thickness_of_vertical, thickness_of_horizontal);
      graphics.setPaint(corner.paintLeft());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      graphics.translate(length - emboss_size, thickness_of_horizontal);
      graphics.setPaint(corner.paintRight());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /*
       * Horizontal bars
       */

      graphics.translate(0, 0);
      graphics.setPaint(corner.paintTop());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(0, thickness_of_horizontal - emboss_size);
      graphics.setPaint(corner.paintBottom());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      if (corner.paintFill().isPresent()) {
        final Paint fill_paint = corner.paintFill().get();

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

      /*
       * Corner
       */

      graphics.translate(length - thickness_of_vertical, 0);
      this.drawEmbossedCornerNE(graphics, corner);
      graphics.setTransform(old_transform);

      /*
       * Caps
       */

      if (corner.caps()) {
        graphics.translate(
          length - thickness_of_vertical,
          length - emboss_size);
        this.drawEmbossedCapS(graphics, thickness_of_vertical, emboss_size, corner.paintBottom());
        graphics.setTransform(old_transform);
        this.drawEmbossedCapW(graphics, thickness_of_horizontal, emboss_size, corner.paintLeft());
      }

    } finally {
      graphics.setTransform(old_transform);
      graphics.setPaint(old_paint);
    }
  }

  private void drawEmbossedL_NW(
    final Graphics2D graphics,
    final SyAWTEmbossedCornerL corner)
  {
    Preconditions.checkPrecondition(
      corner.shape(),
      corner.shape() == LShape.L_SHAPE_NW,
      s -> "L-Shape must be L_SHAPE_NW");

    final int thickness_of_horizontal = corner.thicknessOfHorizontal();
    final int thickness_of_vertical = corner.thicknessOfVertical();
    final int emboss_size = corner.embossSize();
    final int length = corner.armLength();

    final AffineTransform old_transform = graphics.getTransform();
    final Paint old_paint = graphics.getPaint();

    try {

      /*
       * Vertical bars
       */

      graphics.translate(0, thickness_of_horizontal);
      graphics.setPaint(corner.paintLeft());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      graphics.translate(thickness_of_vertical - emboss_size, thickness_of_horizontal);
      graphics.setPaint(corner.paintRight());
      graphics.fillRect(0, 0, emboss_size, length - thickness_of_horizontal);
      graphics.setTransform(old_transform);

      /*
       * Horizontal bars
       */

      graphics.translate(thickness_of_vertical, 0);
      graphics.setPaint(corner.paintTop());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      graphics.translate(thickness_of_vertical, thickness_of_horizontal - emboss_size);
      graphics.setPaint(corner.paintBottom());
      graphics.fillRect(0, 0, length - thickness_of_vertical, emboss_size);
      graphics.setTransform(old_transform);

      if (corner.paintFill().isPresent()) {
        final Paint fill_paint = corner.paintFill().get();

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

      /*
       * Corner
       */

      this.drawEmbossedCornerNW(graphics, corner);
      graphics.setTransform(old_transform);

      /*
       * Caps
       */

      if (corner.caps()) {
        graphics.translate(length - emboss_size, 0);
        this.drawEmbossedCapE(graphics, thickness_of_horizontal, emboss_size, corner.paintRight());
        graphics.setTransform(old_transform);

        graphics.translate(0, length - emboss_size);
        this.drawEmbossedCapS(graphics, thickness_of_vertical, emboss_size, corner.paintBottom());
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
