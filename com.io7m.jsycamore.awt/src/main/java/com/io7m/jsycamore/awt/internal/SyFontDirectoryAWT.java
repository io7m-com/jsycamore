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

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.api.text.SyFontException;
import com.io7m.jsycamore.api.text.SyFontServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.CompletionException;

/**
 * A font directory that loads fonts using AWT.
 */

public final class SyFontDirectoryAWT implements SyFontDirectoryType<SyFontAWT>
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyFontDirectoryAWT.class);

  private final Graphics2D graphics;
  private final List<SyFontServiceType> fonts;
  private final LoadingCache<SyFontDescription, SyFontAWT> fontCache;

  private SyFontDirectoryAWT(
    final List<SyFontServiceType> inFonts)
  {
    this.fonts = Objects.requireNonNull(inFonts, "fonts");

    final BufferedImage image =
      new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR_PRE);
    this.graphics = image.createGraphics();

    this.fontCache =
      Caffeine.newBuilder()
        .maximumSize(32L)
        .build(key -> createFont(key, this.fonts, this.graphics));
  }

  private static SyFontAWT createFont(
    final SyFontDescription request,
    final List<SyFontServiceType> fonts,
    final Graphics2D graphics)
    throws SyFontException
  {
    try {
      for (final var service : fonts) {
        if (Objects.equals(service.family(), request.family())) {
          if (service.style() == request.style()) {
            try (var stream = service.openStream()) {
              final var loadedFonts =
                Font.createFonts(stream);
              final var derivedFont =
                loadedFonts[0].deriveFont((float) request.size());

              LOG.debug("loaded font: {}", request.identifier());
              final var fontMetrics =
                graphics.getFontMetrics(derivedFont);
              return new SyFontAWT(fontMetrics, derivedFont, request);
            }
          }
        }
      }
    } catch (final IOException | FontFormatException e) {
      throw new SyFontException(e.getMessage(), e);
    }

    throw new SyFontException("No such font: " + request.identifier());
  }

  /**
   * Create a new font directory.
   *
   * @param fonts The available font services
   *
   * @return The directory
   */

  public static SyFontDirectoryType<SyFontAWT> create(
    final List<SyFontServiceType> fonts)
  {
    return new SyFontDirectoryAWT(fonts);
  }

  /**
   * Create a new font directory, loading font services from {@link
   * ServiceLoader}.
   *
   * @return The directory
   */

  public static SyFontDirectoryType<SyFontAWT> createFromServiceLoader()
  {
    return create(
      ServiceLoader.load(SyFontServiceType.class)
        .stream()
        .map(ServiceLoader.Provider::get)
        .toList()
    );
  }

  @Override
  public SyFontAWT get(
    final SyFontDescription description)
    throws SyFontException
  {
    try {
      return this.fontCache.get(
        Objects.requireNonNull(description, "description")
      );
    } catch (final CompletionException e) {
      final var cause = e.getCause();
      if (cause instanceof SyFontException fontException) {
        throw fontException;
      }
      throw new SyFontException(cause.getMessage(), cause);
    }
  }
}
