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

package com.io7m.jsycamore.tests.demos;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.menus.SyMenuServiceType;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowServiceType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.awt.internal.SyAWTFont;
import com.io7m.jsycamore.awt.internal.SyAWTFontDirectoryService;
import com.io7m.jsycamore.awt.internal.SyAWTImageLoader;
import com.io7m.jsycamore.awt.internal.SyAWTKeyCodeAdapter;
import com.io7m.jsycamore.awt.internal.SyAWTMouseAdapter;
import com.io7m.jsycamore.awt.internal.SyAWTRenderer;
import com.io7m.jsycamore.components.standard.SyLayoutHorizontal;
import com.io7m.jsycamore.components.standard.SyMenu;
import com.io7m.jsycamore.theme.primal.SyThemePrimalFactory;
import com.io7m.jsycamore.vanilla.SyScreenFactory;
import com.io7m.jsycamore.vanilla.internal.SyLayoutContext;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.junreachable.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.Executors;

import static com.io7m.jsycamore.api.active.SyActive.INACTIVE;
import static com.io7m.jsycamore.api.text.SyText.text;
import static com.io7m.jsycamore.api.windows.SyWindowCloseBehaviour.HIDE_ON_CLOSE_BUTTON;
import static com.io7m.jsycamore.components.standard.buttons.SyButton.button;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public final class SyMenuDemo
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyMenuDemo.class);

  private SyMenuDemo()
  {
    throw new UnreachableCodeException();
  }

  public static void main(final String[] args)
  {
    SwingUtilities.invokeLater(() -> {
      try {
        final var frame = new JFrame("WindowDemo");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(new Canvas());
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private static Optional<URI> iconOf(
    final String name)
  {
    try {
      return Optional.of(
        SyMenuDemo.class.getResource(
            "/com/io7m/jsycamore/tests/" + name)
          .toURI()
      );
    } catch (final URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

  private static final class Canvas extends JPanel
  {
    private final SyFontDirectoryServiceType<SyAWTFont> fontDirectory;
    private final SyAWTRenderer renderer;
    private final SyScreenType screen;
    private final SyThemeType theme;
    private final SyWindowType window0;
    private final SyAWTImageLoader imageLoader;
    private final SyWindowServiceType windowService;
    private final SyMenuServiceType menuService;

    Canvas()
      throws Exception
    {
      this.setFocusable(true);

      this.fontDirectory =
        SyAWTFontDirectoryService.createFromServiceLoader();
      this.imageLoader =
        new SyAWTImageLoader();

      this.theme =
        new SyThemePrimalFactory()
          .create();

      this.screen =
        new SyScreenFactory().create(
          this.theme,
          this.fontDirectory,
          PAreaSizeI.of(1024, 768)
        );

      this.windowService =
        this.screen.windowService();
      this.menuService =
        this.screen.menuService();
      this.window0 =
        this.windowService.windowCreate(640, 480);

      this.window0.closeButtonBehaviour()
        .set(HIDE_ON_CLOSE_BUTTON);

      this.renderer = new SyAWTRenderer(
        this.screen.services(),
        this.fontDirectory,
        this.imageLoader);
      this.renderer.nodeRenderer()
        .setDebugBoundsRendering(false);

      final var executor =
        Executors.newSingleThreadScheduledExecutor(runnable -> {
          final var thread = new Thread(runnable);
          thread.setName("SyWindowDemo.scheduler");
          thread.setDaemon(true);
          return thread;
        });

      final var mouseAdapter =
        new SyAWTMouseAdapter(this.screen);
      final var keyAdapter =
        new SyAWTKeyCodeAdapter(this.screen);

      this.addMouseMotionListener(mouseAdapter);
      this.addMouseListener(mouseAdapter);
      this.addKeyListener(keyAdapter);

      this.addComponentListener(new ComponentAdapter()
      {
        @Override
        public void componentResized(final ComponentEvent e)
        {
          SyMenuDemo.Canvas.this.screen.setSize(
            PAreaSizeI.of(
              e.getComponent().getWidth(),
              e.getComponent().getHeight())
          );
        }
      });

      {
        final Runnable action = () -> {
          LOG.debug("action!");
        };

        final var menu = new SyMenu(this.screen);
        menu.addAtom(text("New..."), action)
          .icon().set(iconOf("paper16.png"));

        menu.addAtom(text("Open..."), action);
        menu.addAtom(text("Save"), action).setActive(INACTIVE);
        menu.addSeparator();
        menu.addAtom(text("Quit"), action);

        final var buttonOpen =
          button(this.screen, text("Menu Open"), () -> {
            final var mousePosition =
              this.screen.mousePosition().get();
            menu.position().set(PVector2I.of(
              mousePosition.x(),
              mousePosition.y()
            ));
            this.menuService.menuOpen(menu);
          });

        final var buttonClose =
          button(
            this.screen,
            text("Menu Close"),
            this.menuService::menuClose
          );

        final var layout = new SyLayoutHorizontal(this.screen);
        layout.childAdd(buttonOpen);
        layout.childAdd(buttonClose);
        this.window0.contentArea().childAdd(layout);
      }

      executor.scheduleAtFixedRate(() -> {
        SwingUtilities.invokeLater(() -> {
          if (!this.windowService.windowIsVisible(this.window0)) {
            this.windowService.windowShow(this.window0);
          }
        });
      }, 0L, 2L, SECONDS);

      executor.scheduleAtFixedRate(() -> {
        SwingUtilities.invokeLater(this::repaint);
      }, 0L, 16L, MILLISECONDS);
    }

    @Override
    public void paint(final Graphics g)
    {
      final var g2 = (Graphics2D) g;

      g2.setColor(Color.GRAY);
      g2.fillRect(0, 0, this.getWidth(), this.getHeight());

      g2.setPaint(new Color(144, 144, 144));

      for (int x = 0; x < this.getWidth(); x += 32) {
        g2.drawLine(x, 0, x, this.getHeight());
      }
      for (int y = 0; y < this.getHeight(); y += 32) {
        g2.drawLine(0, y, this.getWidth(), y);
      }

      final var layoutContext =
        new SyLayoutContext(
          this.screen.services(),
          this.fontDirectory,
          this.screen.theme()
        );

      final var windows = this.windowService.windowsVisibleOrdered();
      for (int index = windows.size() - 1; index >= 0; --index) {
        final var window = windows.get(index);
        window.layout(layoutContext);
        this.renderer.render(g2, this.screen, window);
      }
    }
  }
}