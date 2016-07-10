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

package com.io7m.jsycamore.themedesigner;

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyMouseButton;
import com.io7m.jsycamore.core.SyParentResizeBehavior;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.components.SyButton;
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.components.SyLabel;
import com.io7m.jsycamore.core.components.SyLabelType;
import com.io7m.jsycamore.core.components.SyPanel;
import com.io7m.jsycamore.core.components.SyPanelType;
import com.io7m.jsycamore.core.renderer.SyComponentRendererAWT;
import com.io7m.jsycamore.core.renderer.SyComponentRendererAWTContextType;
import com.io7m.jsycamore.core.renderer.SyComponentRendererType;
import com.io7m.jsycamore.core.renderer.SyWindowRendererAWT;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButton;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemePanel;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.SyThemeWindow;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameCorner;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarVerticalPlacement;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarWidthBehavior;
import com.io7m.jsycamore.core.themes.provided.SyThemeStride;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import net.java.dev.designgridlayout.DesignGridLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

final class SyTDMainWindow extends JFrame
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyTDMainWindow.class);
  }

  private final Canvas canvas;
  private final Controls controls;
  private final SyGUIType gui;
  private final SyWindowType window0;
  private final SyWindowRendererType<BufferedImage, BufferedImage> window_renderer;
  private final SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage> component_renderer;
  private SyWindowType window1;

  SyTDMainWindow()
  {
    final SyThemeType theme = SyThemeStride.builder().build();

    this.gui = SyGUI.createWithTheme("main", theme);
    this.window0 = this.gui.windowCreate(320, 240, "Files");
    this.window0.setPosition(32, 32);
    this.window1 = this.gui.windowCreate(300, 240, "Other");
    this.window1.setPosition(100, 100);

    {
      final SyComponentType content = this.window0.contentPane();
      final SyPanelType panel = SyPanel.create();
      panel.setBounds(content.size().getXI(), content.size().getYI());
      panel.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
      panel.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
      content.node().childAdd(panel.node());

      final SyButtonType button0 = SyButton.create();
      button0.setBounds(64, 32);
      button0.setPosition(16, 16);
      button0.buttonAddListener(b -> SyTDMainWindow.LOG.debug("click"));
      panel.node().childAdd(button0.node());

      final SyLabelType button0_label = SyLabel.create();
      button0_label.setText("Hello.");
      button0_label.setBounds(64, 32);
      button0_label.setPosition(0, 0);
      button0_label.setResizeBehaviorHeight(
        SyParentResizeBehavior.BEHAVIOR_RESIZE);
      button0_label.setResizeBehaviorWidth(
        SyParentResizeBehavior.BEHAVIOR_RESIZE);
      button0.node().childAdd(button0_label.node());

      final SyButtonType button1 = SyButton.create();
      button1.setBounds(64, 32);
      button1.setPosition(16 + 64 + 16, 16);
      button1.setEnabled(false);
      panel.node().childAdd(button1.node());

      final SyLabelType label = SyLabel.create();
      label.setText("Hello.");
      label.setBounds(64, 16);
      label.setPosition(16, 64);
      panel.node().childAdd(label.node());
    }

    {
      final SyButtonType button0 = SyButton.create();
      button0.setBounds(64, 32);
      button0.setPosition(16, 16);
      button0.buttonAddListener(b -> SyTDMainWindow.LOG.debug("click"));
      this.window1.contentPane().node().childAdd(button0.node());

      final SyButtonType button1 = SyButton.create();
      button1.setBounds(64, 32);
      button1.setPosition(16 + 64 + 16, 16);
      button1.setEnabled(false);
      this.window1.contentPane().node().childAdd(button1.node());

      final SyLabelType label = SyLabel.create();
      label.setText("Hello.");
      label.setBounds(64, 16);
      label.setPosition(16, 64);
      this.window1.contentPane().node().childAdd(label.node());
    }

    this.component_renderer =
      SyComponentRendererAWT.create(this.gui.textMeasurement());
    this.window_renderer =
      SyWindowRendererAWT.create(
        this.gui.textMeasurement(), this.component_renderer);

    this.controls = new Controls();
    final JScrollPane controls_scroll = new JScrollPane(this.controls);

    this.canvas = new Canvas();
    this.canvas.setMinimumSize(new Dimension(320, 240));

    final MouseAdapter mouse_adapter = new CanvasMouseAdapter();
    this.canvas.addMouseMotionListener(mouse_adapter);
    this.canvas.addMouseListener(mouse_adapter);

    final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    split.setDividerLocation(0.75);
    split.setLeftComponent(this.canvas);
    split.setRightComponent(controls_scroll);
    this.getContentPane().add(split);
    this.setJMenuBar(SyTDMainWindow.makeMenu());

    this.controls.updateTheme();
  }

  private static JMenuBar makeMenu()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu menu_file = new JMenu("File");

    final JMenuItem item_quit = new JMenuItem("Quit", (int) 'Q');
    menu_file.add(item_quit);

    mb.add(menu_file);
    return mb;
  }

  private final class Canvas extends JPanel
  {
    Canvas()
    {

    }

    @Override
    public void paintComponent(final Graphics g)
    {
      super.paintComponent(g);

      SyTDMainWindow.LOG.debug("paint");

      final SyTDMainWindow win = SyTDMainWindow.this;

      final Graphics2D g2 = (Graphics2D) g;
      g2.setPaint(Color.WHITE);
      g2.fillRect(0, 0, win.getWidth(), win.getHeight());

      final List<SyWindowType> windows = win.gui.windowsOpenOrdered();
      final int window_count = windows.size();
      for (int index = window_count - 1; index >= 0; --index) {
        final SyWindowType window = windows.get(index);
        final VectorReadable2IType bounds = window.bounds();
        final BufferedImage bi =
          new BufferedImage(
            bounds.getXI(),
            bounds.getYI(),
            BufferedImage.TYPE_4BYTE_ABGR_PRE);

        win.window_renderer.render(bi, window);
        final AffineTransform transform = g2.getTransform();
        try {
          final PVectorReadable2IType<SySpaceViewportType> position =
            window.position();
          g2.translate(position.getXI(), position.getYI());
          g2.drawImage(bi, 0, 0, null);
        } finally {
          g2.setTransform(transform);
        }
      }
    }
  }

  private final class Controls extends JPanel
  {
    private final SyTDIntegerSlider slider_frame_bottom;
    private final SyTDIntegerSlider slider_frame_top;
    private final SyTDIntegerSlider slider_frame_left;
    private final SyTDIntegerSlider slider_frame_right;
    private final SyTheme.Builder theme_builder;
    private final SyThemeWindow.Builder theme_window_builder;
    private final SyThemeWindowFrame.Builder theme_window_frame_builder;
    private final SyThemeWindowTitleBar.Builder theme_window_titlebar_builder;
    private final SyTDColorSelector frame_color_active;
    private final SyTDColorSelector frame_color_inactive;
    private final SyTDEmbossSelector frame_emboss_active;
    private final SyTDEmbossSelector frame_emboss_inactive;
    private final SyTDComboBox<SyThemeWindowFrameCorner> frame_corner_top_left;
    private final SyTDComboBox<SyThemeWindowFrameCorner> frame_corner_top_right;
    private final SyTDComboBox<SyThemeWindowFrameCorner> frame_corner_bottom_left;
    private final SyTDComboBox<SyThemeWindowFrameCorner> frame_corner_bottom_right;
    private final SyTDOutlineSelector outline;
    private final SyTDColorSelector titlebar_color_active;
    private final SyTDColorSelector titlebar_color_inactive;
    private final SyTDColorSelector titlebar_text_color_active;
    private final SyTDColorSelector titlebar_text_color_inactive;
    private final SyTDComboBox<SyThemeWindowTitlebarWidthBehavior> titlebar_width_behaviour;
    private final SyTDEmbossSelector titlebar_emboss_active;
    private final SyTDEmbossSelector titlebar_emboss_inactive;
    private final SyTDIntegerSlider titlebar_height;
    private final SyTDComboBox<SyThemeWindowTitlebarVerticalPlacement> titlebar_vertical_placement;
    private final SyTDComboBox<SyAlignmentHorizontal> titlebar_text_alignment;
    private final SyTDComboBox<SyAlignmentHorizontal> titlebar_alignment;
    private final SyTDFontSelector titlebar_text_font;
    private final SyThemeButton.Builder theme_button_builder;
    private final SyThemePanel.Builder theme_panel_builder;
    private final SyThemeLabel.Builder theme_label_builder;

    Controls()
    {
      final SyTheme base = SyThemeStride.builder().build();

      this.theme_builder = SyTheme.builder();
      this.theme_button_builder = SyThemeButton.builder();
      this.theme_button_builder.from(base.buttonTheme());
      this.theme_panel_builder = SyThemePanel.builder();
      this.theme_panel_builder.from(base.panelTheme());
      this.theme_label_builder = SyThemeLabel.builder();
      this.theme_label_builder.from(base.labelTheme());

      this.theme_window_builder = SyThemeWindow.builder();
      this.theme_window_frame_builder = SyThemeWindowFrame.builder();
      this.theme_window_titlebar_builder = SyThemeWindowTitleBar.builder();

      this.slider_frame_top = new SyTDIntegerSlider("Top thickness", 0, 16);
      this.slider_frame_top.setCurrent(5);
      this.slider_frame_top.setOnChangeListener(e -> this.updateTheme());

      this.slider_frame_bottom = new SyTDIntegerSlider(
        "Bottom thickness", 0, 16);
      this.slider_frame_bottom.setCurrent(5);
      this.slider_frame_bottom.setOnChangeListener(e -> this.updateTheme());

      this.slider_frame_left = new SyTDIntegerSlider("Left thickness", 0, 16);
      this.slider_frame_left.setCurrent(5);
      this.slider_frame_left.setOnChangeListener(e -> this.updateTheme());

      this.slider_frame_right = new SyTDIntegerSlider("Right thickness", 0, 16);
      this.slider_frame_right.setCurrent(5);
      this.slider_frame_right.setOnChangeListener(e -> this.updateTheme());

      this.frame_color_active =
        new SyTDColorSelector(new VectorI3F(0.5f, 0.5f, 0.5f));
      this.frame_color_active.addListener(this::updateTheme);

      this.frame_color_inactive =
        new SyTDColorSelector(new VectorI3F(0.5f, 0.5f, 0.5f));
      this.frame_color_inactive.addListener(this::updateTheme);

      this.frame_emboss_active =
        new SyTDEmbossSelector("Emboss active window frames");
      this.frame_emboss_active.addListener(this::updateTheme);

      this.frame_emboss_inactive =
        new SyTDEmbossSelector("Emboss inactive window frames");
      this.frame_emboss_inactive.addListener(this::updateTheme);

      this.frame_corner_top_left =
        new SyTDComboBox<>(SyThemeWindowFrameCorner.class);
      this.frame_corner_top_left.addActionListener(e -> this.updateTheme());

      this.frame_corner_top_right =
        new SyTDComboBox<>(SyThemeWindowFrameCorner.class);
      this.frame_corner_top_right.addActionListener(e -> this.updateTheme());

      this.frame_corner_bottom_left =
        new SyTDComboBox<>(SyThemeWindowFrameCorner.class);
      this.frame_corner_bottom_left.addActionListener(e -> this.updateTheme());

      this.frame_corner_bottom_right =
        new SyTDComboBox<>(SyThemeWindowFrameCorner.class);
      this.frame_corner_bottom_right.addActionListener(e -> this.updateTheme());

      this.outline = new SyTDOutlineSelector("Draw outlines");
      this.outline.addListener(this::updateTheme);

      this.titlebar_color_active =
        new SyTDColorSelector(new VectorI3F(0.5f, 0.5f, 0.5f));
      this.titlebar_color_active.addListener(this::updateTheme);
      this.titlebar_text_color_active =
        new SyTDColorSelector(new VectorI3F(1.0f, 1.0f, 1.0f));
      this.titlebar_text_color_active.addListener(this::updateTheme);
      this.titlebar_text_color_inactive =
        new SyTDColorSelector(new VectorI3F(1.0f, 1.0f, 1.0f));
      this.titlebar_text_color_inactive.addListener(this::updateTheme);
      this.titlebar_color_inactive =
        new SyTDColorSelector(new VectorI3F(0.5f, 0.5f, 0.5f));
      this.titlebar_color_inactive.addListener(this::updateTheme);
      this.titlebar_width_behaviour =
        new SyTDComboBox<>(SyThemeWindowTitlebarWidthBehavior.class);
      this.titlebar_width_behaviour.setSelectedItem(
        SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME);
      this.titlebar_width_behaviour.addActionListener(e -> this.updateTheme());

      this.titlebar_text_font = new SyTDFontSelector("Text font");
      this.titlebar_text_font.addListener(this::updateTheme);

      this.titlebar_emboss_active =
        new SyTDEmbossSelector("Emboss active titlebars");
      this.titlebar_emboss_active.addListener(this::updateTheme);

      this.titlebar_emboss_inactive =
        new SyTDEmbossSelector("Emboss inactive titlebars");
      this.titlebar_emboss_inactive.addListener(this::updateTheme);

      this.titlebar_height =
        new SyTDIntegerSlider("Height", 1, 32);
      this.titlebar_height.setCurrent(16);
      this.titlebar_height.setOnChangeListener(x -> this.updateTheme());

      this.titlebar_vertical_placement =
        new SyTDComboBox<>(SyThemeWindowTitlebarVerticalPlacement.class);
      this.titlebar_vertical_placement.setSelectedItem(
        SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_OVERLAP_FRAME);
      this.titlebar_vertical_placement.addActionListener(
        x -> this.updateTheme());

      this.titlebar_text_alignment =
        new SyTDComboBox<>(SyAlignmentHorizontal.class);
      this.titlebar_text_alignment.setSelectedItem(
        SyAlignmentHorizontal.ALIGN_CENTER);
      this.titlebar_text_alignment.addActionListener(
        x -> this.updateTheme());

      this.titlebar_alignment =
        new SyTDComboBox<>(SyAlignmentHorizontal.class);
      this.titlebar_alignment.setSelectedItem(
        SyAlignmentHorizontal.ALIGN_CENTER);
      this.titlebar_alignment.addActionListener(
        x -> this.updateTheme());

      final DesignGridLayout dg = new DesignGridLayout(this);
      dg.row().left().add(this.largerLabel("Frames")).fill();
      this.slider_frame_top.controlsAddToLayout(dg);
      this.slider_frame_bottom.controlsAddToLayout(dg);
      this.slider_frame_left.controlsAddToLayout(dg);
      this.slider_frame_right.controlsAddToLayout(dg);
      dg.row().grid(new JLabel("Active color")).add(this.frame_color_active.button());
      dg.row().grid(new JLabel("Inactive color")).add(this.frame_color_inactive.button());
      dg.emptyRow();
      this.frame_emboss_active.controlsAddToLayout(dg);
      dg.emptyRow();
      this.frame_emboss_inactive.controlsAddToLayout(dg);
      dg.emptyRow();
      dg.row().grid(new JLabel("Top left corner style")).add(this.frame_corner_top_left);
      dg.row().grid(new JLabel("Top right corner style")).add(this.frame_corner_top_right);
      dg.row().grid(new JLabel("Bottom left corner style")).add(this.frame_corner_bottom_left);
      dg.row().grid(new JLabel("Bottom right corner style")).add(this.frame_corner_bottom_right);
      dg.emptyRow();
      dg.row().left().add(new JSeparator()).fill();

      dg.row().left().add(this.largerLabel("Outlines")).fill();
      dg.emptyRow();
      this.outline.controlsAddToLayout(dg);
      dg.emptyRow();
      dg.row().left().add(new JSeparator()).fill();

      dg.row().left().add(this.largerLabel("Titlebar")).fill();
      dg.emptyRow();
      this.titlebar_height.controlsAddToLayout(dg);
      dg.row().grid(new JLabel("Width behaviour")).add(this.titlebar_width_behaviour);
      dg.row().grid(new JLabel("Vertical placement")).add(this.titlebar_vertical_placement);
      dg.row().grid(new JLabel("Text alignment")).add(this.titlebar_text_alignment);
      dg.row().grid(new JLabel("Horizontal alignment")).add(this.titlebar_alignment);
      this.titlebar_text_font.controlsAddToLayout(dg);
      dg.row().grid(new JLabel("Active color")).add(this.titlebar_color_active.button());
      dg.row().grid(new JLabel("Active text color")).add(this.titlebar_text_color_active.button());
      dg.row().grid(new JLabel("Inactive color")).add(this.titlebar_color_inactive.button());
      dg.row().grid(new JLabel("Inactive text color")).add(this.titlebar_text_color_inactive.button());
      this.titlebar_emboss_active.controlsAddToLayout(dg);
      this.titlebar_emboss_inactive.controlsAddToLayout(dg);
      dg.emptyRow();
    }

    private JLabel largerLabel(final String text)
    {
      final JLabel label = new JLabel(text);
      final Font f = label.getFont();
      label.setFont(f.deriveFont(f.getSize2D() * 1.5f));
      return label;
    }

    private void updateTheme()
    {
      this.theme_window_frame_builder.setEmbossActive(
        this.frame_emboss_active.result());
      this.theme_window_frame_builder.setEmbossInactive(
        this.frame_emboss_inactive.result());
      this.theme_window_frame_builder.setColorActive(
        new VectorI3F(this.frame_color_active.color()));
      this.theme_window_frame_builder.setColorInactive(
        new VectorI3F(this.frame_color_inactive.color()));
      this.theme_window_frame_builder.setTopHeight(
        this.slider_frame_top.value());
      this.theme_window_frame_builder.setBottomHeight(
        this.slider_frame_bottom.value());
      this.theme_window_frame_builder.setLeftWidth(
        this.slider_frame_left.value());
      this.theme_window_frame_builder.setRightWidth(
        this.slider_frame_right.value());

      this.theme_window_frame_builder.setTopLeftStyle(
        this.frame_corner_top_left.getSelectedItem());
      this.theme_window_frame_builder.setTopRightStyle(
        this.frame_corner_top_right.getSelectedItem());
      this.theme_window_frame_builder.setBottomLeftStyle(
        this.frame_corner_bottom_left.getSelectedItem());
      this.theme_window_frame_builder.setBottomRightStyle(
        this.frame_corner_bottom_right.getSelectedItem());

      this.theme_window_builder.setOutline(
        this.outline.result());

      this.theme_window_titlebar_builder.setColorActive(
        this.titlebar_color_active.color());
      this.theme_window_titlebar_builder.setColorInactive(
        this.titlebar_color_inactive.color());
      this.theme_window_titlebar_builder.setTextColorInactive(
        this.titlebar_text_color_inactive.color());
      this.theme_window_titlebar_builder.setTextColorActive(
        this.titlebar_text_color_active.color());
      this.theme_window_titlebar_builder.setWidthBehavior(
        this.titlebar_width_behaviour.getSelectedItem());
      this.theme_window_titlebar_builder.setEmbossActive(
        this.titlebar_emboss_active.result());
      this.theme_window_titlebar_builder.setEmbossInactive(
        this.titlebar_emboss_inactive.result());
      this.theme_window_titlebar_builder.setHeight(
        this.titlebar_height.value());
      this.theme_window_titlebar_builder.setVerticalPlacement(
        this.titlebar_vertical_placement.getSelectedItem());
      this.theme_window_titlebar_builder.setTextAlignment(
        this.titlebar_text_alignment.getSelectedItem());
      this.theme_window_titlebar_builder.setHorizontalAlignment(
        this.titlebar_alignment.getSelectedItem());
      this.theme_window_titlebar_builder.setTextFont(
        this.titlebar_text_font.fontString());

      this.theme_window_builder.setTitleBar(
        this.theme_window_titlebar_builder.build());
      this.theme_window_builder.setFrame(
        this.theme_window_frame_builder.build());
      this.theme_builder.setWindowTheme(
        this.theme_window_builder.build());
      this.theme_builder.setButtonTheme(
        this.theme_button_builder.build());
      this.theme_builder.setPanelTheme(
        this.theme_panel_builder.build());
      this.theme_builder.setLabelTheme(
        this.theme_label_builder.build());

      final SyTheme theme =
        this.theme_builder.build();

      SyTDMainWindow.this.gui.setTheme(theme);
      SyTDMainWindow.this.canvas.repaint();
    }
  }

  private final class CanvasMouseAdapter extends MouseAdapter
  {
    CanvasMouseAdapter()
    {

    }

    private final PVectorM2I<SySpaceViewportType> position =
      new PVectorM2I<>();

    @Override
    public void mouseMoved(final MouseEvent e)
    {
      this.position.set2I(e.getX(), e.getY());
      SyTDMainWindow.this.gui.onMouseMoved(this.position);
      SyTDMainWindow.this.canvas.repaint();
    }

    @Override
    public void mouseDragged(final MouseEvent e)
    {
      this.position.set2I(e.getX(), e.getY());
      SyTDMainWindow.this.gui.onMouseMoved(this.position);
      SyTDMainWindow.this.canvas.repaint();
    }

    @Override
    public void mouseReleased(final MouseEvent e)
    {
      this.position.set2I(e.getX(), e.getY());
      SyTDMainWindow.this.gui.onMouseUp(
        this.position, SyMouseButton.ofIndex(e.getButton() - 1));
      SyTDMainWindow.this.canvas.repaint();
    }

    @Override
    public void mousePressed(final MouseEvent e)
    {
      this.position.set2I(e.getX(), e.getY());
      SyTDMainWindow.this.gui.onMouseDown(
        this.position, SyMouseButton.ofIndex(e.getButton() - 1));
      SyTDMainWindow.this.canvas.repaint();
    }
  }
}
