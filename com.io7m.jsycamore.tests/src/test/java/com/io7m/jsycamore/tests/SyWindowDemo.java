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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.api.text.SyFontStyle;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.awt.internal.SyAWTImageLoader;
import com.io7m.jsycamore.awt.internal.SyAWTRenderer;
import com.io7m.jsycamore.awt.internal.SyFontAWT;
import com.io7m.jsycamore.awt.internal.SyFontDirectoryAWT;
import com.io7m.jsycamore.awt.internal.SyRendererType;
import com.io7m.jsycamore.components.standard.SyButton;
import com.io7m.jsycamore.components.standard.SyImageView;
import com.io7m.jsycamore.components.standard.SyLayoutHorizontal;
import com.io7m.jsycamore.components.standard.SyLayoutMargin;
import com.io7m.jsycamore.components.standard.SyLayoutVertical;
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
import java.util.Optional;
import java.util.concurrent.Executors;

import static com.io7m.jsycamore.api.active.SyActive.INACTIVE;
import static com.io7m.jsycamore.api.visibility.SyVisibility.VISIBILITY_INVISIBLE;
import static com.io7m.jsycamore.api.windows.SyWindowCloseBehaviour.HIDE_ON_CLOSE_BUTTON;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.TimeUnit.SECONDS;

public final class SyWindowDemo
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyWindowDemo.class);

  private SyWindowDemo()
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

  private static final class Canvas extends JPanel
  {
    private final SyFontDirectoryType<SyFontAWT> fontDirectory;
    private final SyAWTRenderer renderer;
    private final SyScreenType screen;
    private final SyThemeType theme;
    private final SyWindowType window0;
    private final SyWindowType window1;
    private final SyAWTImageLoader imageLoader;

    Canvas()
      throws Exception
    {
      this.setFocusable(true);

      this.fontDirectory =
        SyFontDirectoryAWT.createFromServiceLoader();
      this.imageLoader =
        new SyAWTImageLoader();

      this.theme =
        new SyThemePrimalFactory()
          .create();

      this.theme.values()
        .setFont("text_font", new SyFontDescription(
          "York Sans",
          SyFontStyle.REGULAR,
          12
        ));

      this.theme.values()
        .setFont("window_title_text_font", new SyFontDescription(
          "York Sans",
          SyFontStyle.REGULAR,
          12
        ));

      this.screen =
        new SyScreenFactory().create(
          this.theme,
          this.fontDirectory,
          PAreaSizeI.of(1024, 768)
        );

      this.window0 =
        this.screen.windowCreate(640, 480);
      this.window1 =
        this.screen.windowCreate(240, 120);

      this.window0.closeButtonBehaviour()
        .set(HIDE_ON_CLOSE_BUTTON);
      this.window1.closeButtonBehaviour()
        .set(HIDE_ON_CLOSE_BUTTON);

      this.renderer = new SyAWTRenderer(this.fontDirectory, this.imageLoader);
      this.renderer.nodeRenderer()
        .setTextAntialiasing(false);

      final var executor =
        Executors.newSingleThreadScheduledExecutor(runnable -> {
          final var thread = new Thread(runnable);
          thread.setName("SyWindowDemo.scheduler");
          thread.setDaemon(true);
          return thread;
        });

      final var mouseAdapter = new MouseAdapter()
      {
        @Override
        public void mousePressed(
          final MouseEvent e)
        {
          Canvas.this.screen.mouseDown(
            PVector2I.of(e.getX(), e.getY()),
            SyMouseButton.ofIndex(e.getButton() - 1));
          Canvas.this.repaint();
        }

        @Override
        public void mouseDragged(
          final MouseEvent e)
        {
          Canvas.this.screen.mouseMoved(PVector2I.of(e.getX(), e.getY()));
          Canvas.this.repaint();
        }

        @Override
        public void mouseReleased(
          final MouseEvent e)
        {
          Canvas.this.screen.mouseUp(
            PVector2I.of(e.getX(), e.getY()),
            SyMouseButton.ofIndex(e.getButton() - 1));
          Canvas.this.repaint();
        }

        @Override
        public void mouseMoved(
          final MouseEvent e)
        {
          final PVector2I<SySpaceViewportType> position =
            PVector2I.of(e.getX(), e.getY());
          Canvas.this.screen.mouseMoved(position);
          Canvas.this.repaint();
        }
      };

      final var keyAdapter = new KeyAdapter()
      {
        @Override
        public void keyPressed(final KeyEvent e)
        {
          System.out.println(e);
        }

        @Override
        public void keyReleased(final KeyEvent e)
        {
          System.out.println(e);
        }
      };

      this.addMouseMotionListener(mouseAdapter);
      this.addMouseListener(mouseAdapter);
      this.addKeyListener(keyAdapter);

      this.addComponentListener(new ComponentAdapter()
      {
        @Override
        public void componentResized(final ComponentEvent e)
        {
          Canvas.this.screen.setSize(
            PAreaSizeI.of(
              e.getComponent().getWidth(),
              e.getComponent().getHeight())
          );
        }
      });

      {
        final var margin = new SyLayoutMargin();
        margin.setPaddingAll(8);

        final var horizontal = new SyLayoutHorizontal();
        horizontal.paddingBetween().set(8);
        margin.childAdd(horizontal);

        final var button0 = new SyButton("Button 0");
        button0.setOnClickListener(() -> LOG.debug("click 0"));
        final var button1 = new SyButton("Button 1");
        button1.setOnClickListener(() -> LOG.debug("click 1"));
        button1.setActive(INACTIVE);

        final var vz = new SyLayoutVertical();
        vz.childAdd(new SyButton("B0", () -> LOG.debug("B0")));
        vz.childAdd(new SyButton("B1", () -> LOG.debug("B1")));
        vz.childAdd(new SyButton("B2", () -> LOG.debug("B2")));

        final var image = new SyImageView();
        image.imageURI().set(Optional.of(
          SyWindowDemo.class.getResource("/com/io7m/jsycamore/tests/fruit.jpg").toURI()
        ));

        horizontal.childAdd(button0);
        horizontal.childAdd(button1);
        horizontal.childAdd(vz);
        horizontal.childAdd(image);

        this.window0.decorated().set(TRUE);
        this.window0.contentArea().childAdd(margin);
        this.window0.title().set("Window Title");
        this.window0.positionSnapping().set(16);
        this.window0.sizeSnapping().set(16);
      }

      {
        this.window1.closeButtonVisibility().set(VISIBILITY_INVISIBLE);
        this.window1.menuButtonVisibility().set(VISIBILITY_INVISIBLE);
        this.window1.maximizeButtonVisibility().set(VISIBILITY_INVISIBLE);
        this.window1.title().set("ひらがな");
      }

      executor.scheduleAtFixedRate(() -> {
        SwingUtilities.invokeLater(() -> {
          if (!this.screen.windowIsVisible(this.window0)) {
            this.screen.windowShow(this.window0);
          }
          if (!this.screen.windowIsVisible(this.window1)) {
            this.screen.windowShow(this.window1);
          }
        });
      }, 0L, 2L, SECONDS);
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
          this.fontDirectory,
          this.screen.theme()
        );

      final var windows = this.screen.windowsVisibleOrdered();
      for (int index = windows.size() - 1; index >= 0; --index) {
        final var window = windows.get(index);
        window.layout(layoutContext);
        this.renderer.render(g2, this.screen, window);
      }
    }
  }
}