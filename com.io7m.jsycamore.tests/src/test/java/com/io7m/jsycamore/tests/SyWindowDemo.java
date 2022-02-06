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
import com.io7m.jsycamore.api.SyScreenType;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.awt.internal.SyBoundsOnlyRenderer;
import com.io7m.jsycamore.theme.primal.SyThemePrimalFactory;
import com.io7m.jsycamore.vanilla.SyScreenFactory;
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
import java.util.concurrent.Executors;

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
    private final SyScreenType screen;
    private final SyWindowType window0;
    private final SyBoundsOnlyRenderer renderer;

    Canvas()
    {
      this.setFocusable(true);

      final var theme =
        new SyThemePrimalFactory().create();

      this.screen =
        new SyScreenFactory()
          .create(theme, PAreaSizeI.of(800, 600));

      this.window0 =
        this.screen.windowCreate(640, 480);
      this.renderer =
        new SyBoundsOnlyRenderer();

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

      this.window0.decorated().set(TRUE);

      executor.scheduleAtFixedRate(() -> {
        SwingUtilities.invokeLater(() -> {
          this.screen.windowOpen(this.window0);
        });
      }, 0L, 2L, SECONDS);
    }

    @Override
    public void paint(final Graphics g)
    {
      final var g2 = (Graphics2D) g;

      g2.setColor(Color.GRAY);
      g2.fillRect(0, 0, this.getWidth(), this.getHeight());

      this.screen.windowsOpenOrderedNow().forEach(window -> {
        window.layout(this.screen.theme());
        this.renderer.render(g2, this.screen, window);
      });
    }
  }
}