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

package com.io7m.jsycamore.tests.core;

import com.io7m.jsycamore.awt.SyAWTComponentRenderer;
import com.io7m.jsycamore.awt.SyAWTComponentRendererContextType;
import com.io7m.jsycamore.awt.SyAWTTextMeasurement;
import com.io7m.jsycamore.awt.SyAWTWindowRenderer;
import com.io7m.jsycamore.caffeine.SyBufferedImageCacheCaffeine;
import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowContentPaneType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyActive;
import com.io7m.jsycamore.core.components.SyButton;
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.components.SyLabel;
import com.io7m.jsycamore.core.components.SyLabelType;
import com.io7m.jsycamore.core.components.SyPanel;
import com.io7m.jsycamore.core.components.SyPanelType;
import com.io7m.jsycamore.core.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.core.images.SyImageCacheResolverType;
import com.io7m.jsycamore.core.images.SyImageCacheType;
import com.io7m.jsycamore.core.images.SyImageFormat;
import com.io7m.jsycamore.core.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.core.images.SyImageSpecification;
import com.io7m.jsycamore.core.renderer.SyComponentRendererType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jsycamore.core.themes.provided.SyThemeFenestra;
import com.io7m.jsycamore.core.themes.provided.SyThemeMotive;
import com.io7m.jsycamore.core.themes.provided.SyThemeStride;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.parameterized.PVectorI2I;
import com.io7m.junreachable.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class WindowDemo
{
  private WindowDemo()
  {
    throw new UnreachableCodeException();
  }

  private static final class Canvas extends JPanel
  {
    private static final Logger LOG;

    static {
      LOG = LoggerFactory.getLogger(Canvas.class);
    }

    private final SyAWTTextMeasurement measurement;
    private final SyGUIType gui;
    private final SyWindowType window0;
    private final SyWindowType window1;
    private final SyImageCacheType<BufferedImage> image_cache;
    private final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> c_renderer;
    private final SyWindowRendererType<BufferedImage, BufferedImage> w_renderer;
    private final List<SyThemeType> themes;
    private final Random random;

    Canvas()
    {
      this.setFocusable(true);

      this.random = new Random(23L);

      this.themes = new ArrayList<>();
      this.themes.add(SyThemeMotive.builder().build());
      this.themes.add(SyThemeFenestra.builder().build());
      this.themes.add(SyThemeBee.builder().build());
      this.themes.add(SyThemeStride.builder().build());

      final SyImageCacheResolverType resolver = specification -> {
        Canvas.LOG.debug("loading: {}", specification.name());

        final InputStream stream = WindowDemo.class.getResourceAsStream(
          specification.name());
        if (stream == null) {
          throw new FileNotFoundException(specification.name());
        }
        return stream;
      };

      final SyImageCacheLoaderType<BufferedImage> loader = (specification, stream) -> {
        final BufferedImage loaded = ImageIO.read(stream);
        if (loaded == null) {
          throw new IOException(
            "Could not parse output_image " + specification.name());
        }
        return loaded;
      };

      final ExecutorService io_executor =
        Executors.newFixedThreadPool(4);
      final ScheduledExecutorService r_executor =
        Executors.newSingleThreadScheduledExecutor();

      final BufferedImage image_default =
        new BufferedImage(4, 4, BufferedImage.TYPE_4BYTE_ABGR);
      final BufferedImage image_error =
        image_default;

      this.image_cache = SyBufferedImageCacheCaffeine.create(
        resolver, loader, io_executor, image_default, image_error, 4_000_000L);

      this.measurement = SyAWTTextMeasurement.create();
      this.gui =
        SyGUI.createWithTheme(this.measurement, "Main", this.themes.get(0));

      this.window0 = this.createWindow("Window 0");
      this.window0.setBox(SyBoxes.create(16, 16, 320, 240));
      this.window1 = this.createWindow("Window 1");
      this.window1.setBox(SyBoxes.create(16, 16 + 240 + 16, 320, 240));

      this.c_renderer = SyAWTComponentRenderer.create(
        this.image_cache, this.measurement, this.measurement);
      this.w_renderer = SyAWTWindowRenderer.create(this.c_renderer);

      final MouseAdapter mouse_adapter = new MouseAdapter()
      {
        @Override
        public void mousePressed(final MouseEvent e)
        {
          Canvas.this.gui.onMouseDown(
            new PVectorI2I<>(e.getX(), e.getY()),
            SyMouseButton.ofIndex(e.getButton() - 1));
          Canvas.this.repaint();
        }

        @Override
        public void mouseDragged(final MouseEvent e)
        {
          Canvas.this.gui.onMouseMoved(new PVectorI2I<>(e.getX(), e.getY()));
          Canvas.this.repaint();
        }

        @Override
        public void mouseReleased(final MouseEvent e)
        {
          Canvas.this.gui.onMouseUp(
            new PVectorI2I<>(e.getX(), e.getY()),
            SyMouseButton.ofIndex(e.getButton() - 1));
          Canvas.this.repaint();
        }

        @Override
        public void mouseMoved(final MouseEvent e)
        {
          Canvas.this.gui.onMouseMoved(new PVectorI2I<>(e.getX(), e.getY()));
          Canvas.this.repaint();
        }
      };

      final KeyAdapter key_adapter = new KeyAdapter()
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

      this.addMouseMotionListener(mouse_adapter);
      this.addMouseListener(mouse_adapter);
      this.addKeyListener(key_adapter);

      r_executor.scheduleAtFixedRate(
        () -> SwingUtilities.invokeLater(() -> {

          Canvas.LOG.debug(
            "cache size: {}/{}",
            Long.valueOf(this.image_cache.size()),
            Long.valueOf(this.image_cache.maximumSize()));

          if (!this.window0.isOpen()) {
            this.gui.windowOpen(this.window0);
          }
          if (!this.window1.isOpen()) {
            this.gui.windowOpen(this.window1);
          }
          this.repaint();
        }), 3L, 3L, TimeUnit.SECONDS);

      r_executor.scheduleAtFixedRate(
        () -> SwingUtilities.invokeLater(() -> {
          Collections.shuffle(this.themes, this.random);
          this.gui.setTheme(this.themes.get(0));
          this.repaint();
        }), 3L, 3L, TimeUnit.SECONDS);
    }

    private SyWindowType createWindow(
      final String title)
    {
      final SyWindowType window = this.gui.windowCreate(320, 240, title);
      window.titleBar().setIcon(Optional.of(SyImageSpecification.of(
        "/com/io7m/jsycamore/tests/awt/paper.png",
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST)));

      final SyWindowContentPaneType content = window.contentPane();

      {
        final SyPanelType panel = SyPanel.create();
        panel.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        panel.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);

        final SyBoxType<SySpaceParentRelativeType> content_box = content.box();
        panel.setBox(SyBoxes.create(
          0,
          0,
          content_box.width(),
          content_box.height()));
        content.node().childAdd(panel.node());

        {
          final SyButtonType button = SyButton.create();
          button.setBox(SyBoxes.create(8, 8, 64, 32));
          panel.node().childAdd(button.node());

          final SyLabelType label = SyLabel.create();
          label.setText("Hello");
          label.setBox(SyBoxes.create(0, 0, 64, 32));
          button.node().childAdd(label.node());
        }

        {
          final SyButtonType button = SyButton.create();
          button.setBox(SyBoxes.create(8 + 64 + 8, 8, 64, 32));
          button.setActive(SyActive.INACTIVE);
          panel.node().childAdd(button.node());

          final SyLabelType label = SyLabel.create();
          label.setText("Hello");
          label.setBox(SyBoxes.create(0, 0, 64, 32));
          button.node().childAdd(label.node());
        }

        {
          final int y_start = 8;
          final int y_end = y_start + (4 * (16 + 2));
          for (int y = y_start; y < y_end; y += 16 + 2) {
            final int x_start = 8 + 64 + 8 + 64 + 8;
            final int x_end = x_start + (4 * (16 + 2));
            for (int x = x_start; x < x_end; x += 16 + 2) {
              final SyButtonType button = SyButton.create();
              button.setBox(SyBoxes.create(x, y, 16, 16));
              panel.node().childAdd(button.node());
            }
          }
        }
      }

      return window;
    }

    @Override
    public void paint(final Graphics g)
    {
      super.paint(g);

      final List<SyWindowType> order = this.gui.windowsOpenOrdered();
      final ListIterator<SyWindowType> iter = order.listIterator(order.size());

      while (iter.hasPrevious()) {
        final SyWindowType w = iter.previous();
        this.renderWindow(g, w);
      }
    }

    private void renderWindow(
      final Graphics g,
      final SyWindowType w)
    {
      final SyBoxType<SySpaceViewportType> box = w.box();
      final BufferedImage image = new BufferedImage(
        box.width(), box.height(), BufferedImage.TYPE_4BYTE_ABGR);
      this.w_renderer.render(image, w);
      g.drawImage(image, box.minimumX(), box.minimumY(), null);
    }
  }

  public static void main(final String[] args)
  {
    SwingUtilities.invokeLater(() -> {
      final JFrame frame = new JFrame("WindowDemo");
      frame.setPreferredSize(new Dimension(800, 600));
      frame.getContentPane().add(new Canvas());
      frame.pack();
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setVisible(true);
    });
  }
}
