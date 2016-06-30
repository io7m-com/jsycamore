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

import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.renderer.SyGUIRendererAWTInefficient;
import com.io7m.jsycamore.core.renderer.SyGUIRendererType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererAWT;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.provided.SyThemeBee;
import com.io7m.jtensors.parameterized.PVectorM2I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public final class WindowDemo implements Runnable
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(WindowDemo.class);
  }

  private final SyGUIType gui;
  private final SyWindowType window0;
  private final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer;
  private final JFrame sw_frame;
  private final BufferedImage output;
  private final SyGUIRendererType<BufferedImage, BufferedImage> gui_renderer;
  private SyWindowType window1;

  private final class Canvas extends JPanel
  {
    @Override
    public void paintComponent(final Graphics g)
    {
      super.paintComponent(g);

      WindowDemo.LOG.debug("paint");

      final WindowDemo wd = WindowDemo.this;
      {
        final Graphics2D gw = wd.output.createGraphics();
        gw.clearRect(0, 0, 1024, 1024);
        wd.gui_renderer.render(wd.output, wd.gui);
      }

      g.drawImage(wd.output, 0, 0, null);
    }
  }

  private WindowDemo()
  {
    final SyThemeType theme = SyThemeBee.builder().build();
    this.gui = SyGUI.createWithTheme("main", theme);
    this.window0 = this.gui.windowCreate(320, 240, "Files");
    this.window1 = this.gui.windowCreate(300, 240, "Other");
    this.window1.setPosition(100, 100);

    this.window_renderer =
      SyWindowRendererAWT.create(this.gui.textMeasurement());
    this.gui_renderer =
      SyGUIRendererAWTInefficient.create(this.window_renderer);
    this.output =
      new BufferedImage(1024, 1024, BufferedImage.TYPE_4BYTE_ABGR_PRE);

    final Canvas canvas = new Canvas();
    this.sw_frame = new JFrame();
    this.sw_frame.setPreferredSize(new Dimension(800, 600));
    this.sw_frame.setMinimumSize(new Dimension(320, 240));
    this.sw_frame.getContentPane().add(canvas);
    this.sw_frame.pack();
    this.sw_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.sw_frame.setVisible(true);

    final MouseAdapter mouse_adapter = new MouseAdapter()
    {
      private final PVectorM2I<SySpaceViewportType> position =
        new PVectorM2I<>();

      @Override
      public void mouseMoved(final MouseEvent e)
      {
        this.position.set2I(e.getX(), e.getY());
        WindowDemo.this.gui.onMouseMoved(this.position);
      }

      @Override
      public void mouseDragged(final MouseEvent e)
      {
        this.position.set2I(e.getX(), e.getY());
        WindowDemo.this.gui.onMouseMoved(this.position);
        canvas.repaint();
      }

      @Override
      public void mouseReleased(final MouseEvent e)
      {
        this.position.set2I(e.getX(), e.getY());
        WindowDemo.this.gui.onMouseUp(
          this.position, SyMouseButton.ofIndex(e.getButton() - 1));
        canvas.repaint();
      }

      @Override
      public void mousePressed(final MouseEvent e)
      {
        this.position.set2I(e.getX(), e.getY());
        WindowDemo.this.gui.onMouseDown(
          this.position, SyMouseButton.ofIndex(e.getButton() - 1));
        canvas.repaint();
      }
    };

    canvas.addMouseMotionListener(mouse_adapter);
    canvas.addMouseListener(mouse_adapter);
  }

  public static void main(final String[] args)
    throws Exception
  {
    SwingUtilities.invokeLater(() -> new WindowDemo().run());
  }

  @Override
  public void run()
  {

  }
}
