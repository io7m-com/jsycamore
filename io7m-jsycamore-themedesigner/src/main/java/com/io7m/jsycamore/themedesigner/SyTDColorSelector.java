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

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3FType;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

final class SyTDColorSelector extends JPanel
{
  private final JButton select;
  private final VectorM3F color_vector;
  private Color color;
  private float[] color_data;
  private List<Runnable> listeners;

  SyTDColorSelector(final VectorReadable3FType initial_color)
  {
    this.listeners = new ArrayList<>(1);

    this.color_data = new float[4];
    this.color_vector = new VectorM3F();

    this.select = new JButton("Select...");
    this.select.setIcon(new ColorIcon());

    this.select.addActionListener(e -> {
      final Color selected_color = JColorChooser.showDialog(
        this,
        "Select color_vector",
        new Color(
          this.color_vector.getXF(),
          this.color_vector.getYF(),
          this.color_vector.getZF()));
      if (selected_color != null) {
        this.updateColor(selected_color);
      }
    });

    this.updateColor(new Color(
      initial_color.getXF(),
      initial_color.getYF(),
      initial_color.getZF()));
  }

  @Override
  public void setEnabled(final boolean enabled)
  {
    super.setEnabled(enabled);
    this.select.setEnabled(enabled);
  }

  JButton button()
  {
    return this.select;
  }

  private void updateColor(final Color new_color)
  {
    new_color.getRGBColorComponents(this.color_data);
    this.color_vector.set3F(
      this.color_data[0],
      this.color_data[1],
      this.color_data[2]);
    this.color = new_color;

    for (final Runnable r : this.listeners) {
      r.run();
    }
  }

  VectorI3F color()
  {
    return new VectorI3F(this.color_vector);
  }

  void addListener(final Runnable on_update)
  {
    this.listeners.add(NullCheck.notNull(on_update));
  }

  private final class ColorIcon implements Icon
  {
    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;

    ColorIcon()
    {

    }

    @Override
    public void paintIcon(
      final Component c,
      final Graphics g,
      final int x,
      final int y)
    {
      final Graphics2D g2d = (Graphics2D) g.create();
      g2d.setPaint(SyTDColorSelector.this.color);
      g2d.fillRect(x, y, ColorIcon.ICON_WIDTH, ColorIcon.ICON_HEIGHT);
      g2d.setPaint(Color.BLACK);
      g2d.drawRect(x, y, ColorIcon.ICON_WIDTH, ColorIcon.ICON_HEIGHT);
      g2d.dispose();
    }

    @Override
    public int getIconWidth()
    {
      return ColorIcon.ICON_WIDTH;
    }

    @Override
    public int getIconHeight()
    {
      return ColorIcon.ICON_HEIGHT;
    }
  }
}
