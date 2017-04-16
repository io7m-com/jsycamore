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
import com.io7m.jsycamore.core.themes.SyThemeOutline;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class SyTDOutlineSelector extends JPanel implements SyTDControlsType
{
  private final SyTDColorSelector color_active;
  private final SyTDColorSelector color_inactive;
  private final JCheckBox active;
  private final List<Runnable> listeners;
  private final SyThemeOutline.Builder theme;
  private final RowGroup group;
  private Optional<SyThemeOutlineType> result;

  SyTDOutlineSelector(final String in_title)
  {
    NullCheck.notNull(in_title);
    this.listeners = new ArrayList<>(1);
    this.active = new JCheckBox(in_title);
    this.color_active = new SyTDColorSelector(Vector3D.of(0.0, 0.0, 0.0));
    this.color_inactive = new SyTDColorSelector(Vector3D.of(0.3, 0.3, 0.3));

    this.active.addActionListener(e -> {
      final boolean selected = this.active.isSelected();
      this.color_active.setEnabled(selected);
      this.color_inactive.setEnabled(selected);
      this.updateTheme();
    });
    this.color_active.addListener(this::updateTheme);
    this.color_inactive.addListener(this::updateTheme);
    this.theme = SyThemeOutline.builder();
    this.group = new RowGroup();
    this.result = Optional.empty();
    this.active.doClick();
  }

  Optional<SyThemeOutlineType> result()
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
      this.theme.setColorActive(this.color_active.color());
      this.theme.setColorInactive(this.color_inactive.color());
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
    layout.row().group(this.group).grid(new JLabel("Active color")).add(this.color_active.button());
    layout.row().group(this.group).grid(new JLabel("Inactive color")).add(this.color_inactive.button());
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
