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
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeEmbossType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class SyTDEmbossSelector extends JPanel implements SyTDControlsType
{
  private final SyTDColorSelector color_top;
  private final SyTDColorSelector color_left;
  private final SyTDColorSelector color_right;
  private final SyTDColorSelector color_bottom;
  private final SyTDIntegerSlider slider_size;
  private final JCheckBox active;
  private final List<Runnable> listeners;
  private final SyThemeEmboss.Builder theme;
  private final RowGroup group;
  private final String title;
  private Optional<SyThemeEmbossType> result;

  SyTDEmbossSelector(final String in_title)
  {
    this.title = NullCheck.notNull(in_title);
    this.listeners = new ArrayList<>(1);
    this.active = new JCheckBox(in_title);
    this.color_top = new SyTDColorSelector(Vector3D.of(0.8, 0.8, 0.8));
    this.color_left = new SyTDColorSelector(Vector3D.of(0.8, 0.8, 0.8));
    this.color_right = new SyTDColorSelector(Vector3D.of(0.2, 0.2, 0.2));
    this.color_bottom = new SyTDColorSelector(Vector3D.of(0.2, 0.2, 0.2));
    this.slider_size = new SyTDIntegerSlider("Emboss size", 1, 16);
    this.slider_size.setCurrent(1);
    this.active.addActionListener(e -> {
      final boolean selected = this.active.isSelected();
      this.color_top.setEnabled(selected);
      this.color_left.setEnabled(selected);
      this.color_right.setEnabled(selected);
      this.color_bottom.setEnabled(selected);
      this.slider_size.setEnabled(selected);
      this.updateTheme();
    });
    this.slider_size.setOnChangeListener(x -> this.updateTheme());
    this.color_top.addListener(this::updateTheme);
    this.color_left.addListener(this::updateTheme);
    this.color_right.addListener(this::updateTheme);
    this.color_bottom.addListener(this::updateTheme);
    this.theme = SyThemeEmboss.builder();
    this.group = new RowGroup();
    this.result = Optional.empty();
    this.active.doClick();
  }

  Optional<SyThemeEmbossType> result()
  {
    return this.result;
  }

  void addListener(final Runnable receiver)
  {
    this.listeners.add(NullCheck.notNull(receiver));
  }

  private void updateTheme()
  {
    if (this.active.isSelected()) {
      this.theme.setColorBottom(this.color_bottom.color());
      this.theme.setColorTop(this.color_top.color());
      this.theme.setColorLeft(this.color_left.color());
      this.theme.setColorRight(this.color_right.color());
      this.theme.setSize(this.slider_size.value());
      this.result = Optional.of(this.theme.build());
    } else {
      this.result = Optional.empty();
    }

    for (final Runnable receiver : this.listeners) {
      receiver.run();
    }
  }

  @Override
  public void controlsAddToLayout(final DesignGridLayout layout)
  {
    layout.row().left().add(this.active).fill();
    layout.row().group(this.group).grid(new JLabel("Emboss top color")).add(this.color_top.button());
    layout.row().group(this.group).grid(new JLabel("Emboss left color")).add(
      this.color_left.button());
    layout.row().group(this.group).grid(new JLabel("Emboss right color")).add(
      this.color_right.button());
    layout.row().group(this.group).grid(new JLabel("Emboss bottom color")).add(
      this.color_bottom.button());
    this.slider_size.controlsAddToLayout(layout);
  }

  @Override
  public void controlsHide()
  {
    this.group.hide();
  }

  @Override
  public void controlsShow()
  {
    this.group.forceShow();
  }
}
