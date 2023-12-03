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


package com.io7m.jsycamore.tests;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jsycamore.api.colors.SyColors;
import com.io7m.jsycamore.api.rendering.SyPaintFlat;
import com.io7m.jsycamore.api.rendering.SyRenderNodeComposite;
import com.io7m.jsycamore.api.rendering.SyRenderNodeShape;
import com.io7m.jsycamore.api.rendering.SyShapeRectangle;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.awt.internal.SyAWTFont;
import com.io7m.jsycamore.awt.internal.SyAWTFontDirectoryService;
import com.io7m.jsycamore.awt.internal.SyAWTImageLoader;
import com.io7m.jsycamore.awt.internal.SyAWTNodeRenderer;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.nio.file.Path;
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyAWTNodeRendererTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyAWTNodeRendererTest.class);

  private Path directory;
  private SyAWTNodeRenderer nodeRenderer;
  private BufferedImage imageReceived;
  private Graphics2D graphics;
  private Path imageReceivedFile;
  private SyAWTImageLoader imageLoader;
  private SyFontDirectoryServiceType<SyAWTFont> fonts;

  @BeforeEach
  public void setup()
    throws Exception
  {
    this.directory =
      SyTestDirectories.createTempDirectory();
    this.imageReceived =
      new BufferedImage(128, 128, TYPE_4BYTE_ABGR_PRE);
    this.imageReceivedFile =
      this.directory.resolve("received.png");
    this.imageLoader =
      new SyAWTImageLoader();
    this.fonts =
      SyAWTFontDirectoryService.createFromServiceLoader();

    this.graphics = this.imageReceived.createGraphics();
    this.graphics.setPaint(Color.BLACK);
    this.graphics.fillRect(0, 0, 128, 128);
    this.nodeRenderer = new SyAWTNodeRenderer(this.imageLoader, this.fonts);
  }

  @AfterEach
  public void tearDown()
    throws Exception
  {
    SyTestDirectories.deleteDirectory(this.directory);
  }

  private void compareImages(
    final String name)
    throws Exception
  {
    final var expectedPath =
      SyTestDirectories.resourceOf(
        SyAWTNodeRendererTest.class,
        this.directory,
        name
      );

    LOG.debug("-- comparing images");
    LOG.debug("expected {}", expectedPath);
    LOG.debug("received {}", this.imageReceivedFile);

    final var imageExpected =
      ImageIO.read(expectedPath.toFile());

    final var grabR = new PixelGrabber(
      this.imageReceived,
      0,
      0,
      this.imageReceived.getWidth(),
      this.imageReceived.getHeight(),
      false
    );

    final var grabE = new PixelGrabber(
      imageExpected,
      0,
      0,
      imageExpected.getWidth(),
      imageExpected.getHeight(),
      false
    );

    assertTrue(grabR.grabPixels(), "Grabbing pixels must succeed");
    assertTrue(grabE.grabPixels(), "Grabbing pixels must succeed");

    final var pixelsR = (int[]) grabR.getPixels();
    final var pixelsE = (int[]) grabE.getPixels();

    try {
      assertArrayEquals(pixelsE, pixelsR, "Images do not match");
    } catch (final Throwable e) {
      LOG.error("Image mismatch: ", e);
      LOG.error("Run: feh {} {}", expectedPath, this.imageReceivedFile);
      throw e;
    }
  }

  private void saveImage()
    throws Exception
  {
    ImageIO.write(
      this.imageReceived,
      "PNG",
      this.imageReceivedFile.toFile()
    );
  }

  @Test
  public void testNodeSimple0()
    throws Exception
  {
    this.nodeRenderer.renderNode(
      this.graphics,
      new SyRenderNodeShape(
        "Simple0",
        PVectors2I.zero(),
        Optional.of(new SyPaintFlat(SyColors.whiteOpaque())),
        Optional.empty(),
        new SyShapeRectangle<>(
          PAreaI.of(2, 32 + 2, 2, 32 + 2))
      )
    );

    this.saveImage();
    this.compareImages("testNodeSimple0.png");
  }

  @Test
  public void testNodeSimple1()
    throws Exception
  {
    this.nodeRenderer.renderNode(
      this.graphics,
      SyRenderNodeComposite.composite(
        "Composite",
        new SyRenderNodeShape(
          "Simple0",
          PVectors2I.zero(),
          Optional.of(new SyPaintFlat(SyColors.whiteOpaque())),
          Optional.empty(),
          new SyShapeRectangle<>(
            PAreaI.of(2, 32 + 2, 2, 32 + 2))
        ),
        new SyRenderNodeShape(
          "Simple1",
          PVectors2I.zero(),
          Optional.of(new SyPaintFlat(SyColors.whiteOpaque())),
          Optional.empty(),
          new SyShapeRectangle<>(
            PAreaI.of(32 + 4, 32 + 4 + 32, 2, 32 + 2))
        )
      )
    );

    this.saveImage();
    this.compareImages("testNodeSimple1.png");
  }
}
