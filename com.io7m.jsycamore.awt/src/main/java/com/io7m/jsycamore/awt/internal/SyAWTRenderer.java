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
import com.io7m.jsycamore.api.SyScreenType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.windows.SyWindowType;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * An AWT renderer.
 */

public final class SyAWTRenderer implements SyRendererType
{
  private final SyFontDirectoryType fonts;
  private final ThemeContext themeContext;
  private final SyAWTNodeRenderer nodeRenderer;

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
    this.nodeRenderer = new SyAWTNodeRenderer();
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

      this.nodeRenderer.renderNode(g, renderNode);

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
