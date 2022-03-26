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

package com.io7m.jsycamore.tests;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.rendering.SyPaintGradientLinear;
import com.io7m.jsycamore.api.rendering.SyPaintedGroup;
import com.io7m.jsycamore.api.rendering.SyPaintedGroups;
import com.io7m.jsycamore.api.rendering.SyPaintedShape;
import com.io7m.jsycamore.api.rendering.SyShapePolygon;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.awt.internal.SyAWTShapeRenderer;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;

public final class SyPaintDemo
{
  private SyPaintDemo()
  {

  }

  public static void main(
    final String[] args)
    throws Exception
  {
    final var image =
      new BufferedImage(512, 512, TYPE_4BYTE_ABGR_PRE);

    final var renderer = new SyAWTShapeRenderer();

    final var graphics = image.createGraphics();
    try {
      final PVector4D<SySpaceRGBAPreType> blue =
        PVector4D.of(0.0, 0.0, 1.0, 1.0);
      final PVector4D<SySpaceRGBAPreType> red =
        PVector4D.of(1.0, 0.0, 0.0, 1.0);
      final PVector4D<SySpaceRGBAPreType> green =
        PVector4D.of(0.0, 1.0, 0.0, 1.0);
      final PAreaI<SySpaceComponentRelativeType> base =
        PAreaI.of(0, 31, 0, 31);

      final var texture =
        ImageIO.read(new File("tile06.png"));
      texture.coerceData(true);

      final var gradient0 =
        new SyPaintGradientLinear(
          Vector2D.of(0.0, 0.0),
          Vector2D.of(0.0, 1.0),
          List.of(blue, red, green),
          List.of(0.0, 0.5, 1.0)
        );

      final var groups = new SyPaintedGroups<>(
        List.of(
          new SyPaintedGroup<>(
            Optional.empty(),
            List.of(
              new SyPaintedShape<>(
                Optional.of(new SyPaintFlat(blue)),
                Optional.of(new SyPaintFlat(red)),
                new SyShapeRectangle<>(PAreasI.moveAbsolute(base, 32, 32))
              ),
              new SyPaintedShape<>(
                Optional.of(new SyPaintFlat(blue)),
                Optional.of(gradient0),
                new SyShapeRectangle<>(PAreasI.moveAbsolute(base, 80, 32))
              ),
              new SyPaintedShape<>(
                Optional.empty(),
                Optional.of(gradient0),
                new SyShapeRectangle<>(PAreasI.moveAbsolute(base, 32, 80))
              ),
              new SyPaintedShape<>(
                Optional.of(new SyPaintFlat(blue)),
                Optional.of(gradient0),
                new SyShapePolygon<>(
                  List.of(
                    PVector2I.of(128, 128),
                    PVector2I.of(150, 140),
                    PVector2I.of(130, 160)
                  )
                )
              )
            )
          )
        )
      );

      final var existingComposite = graphics.getComposite();
      try {
        graphics.setPaint(new Color(0.34f, 0.41f, 0.47f));
        graphics.fillRect(32, 32, 256, 256);
        graphics.setPaint(new TexturePaint(
          texture,
          new Rectangle(0, 0, 256, 256)
        ));
        graphics.fillRect(0, 0, 512, 512);

      } finally {
        graphics.setComposite(existingComposite);
      }

      renderer.renderGroups(graphics, groups);
    } finally {
      graphics.dispose();
    }

    ImageIO.write(image, "PNG", new File("/tmp/image.png"));
  }
}
