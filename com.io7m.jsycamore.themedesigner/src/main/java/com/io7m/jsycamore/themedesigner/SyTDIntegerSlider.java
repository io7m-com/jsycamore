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
import com.io7m.jnull.Nullable;
import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.util.function.IntConsumer;

final class SyTDIntegerSlider implements SyTDControlsType
{
  private final JTextField field;
  private final RowGroup group;
  private final JLabel label;
  private final int maximum;
  private final int minimum;
  private final JSlider slider;
  private int current;
  private @Nullable IntConsumer on_change;

  SyTDIntegerSlider(
    final String in_label,
    final int in_minimum,
    final int in_maximum)
  {
    this.label = new JLabel(NullCheck.notNull(in_label, "Label"));
    this.group = new RowGroup();

    this.maximum = in_maximum;
    this.minimum = in_minimum;
    this.current = (this.maximum / 2) + this.minimum;

    this.field = new JTextField(Integer.toString(in_minimum));
    this.slider = new JSlider(SwingConstants.HORIZONTAL);
    this.slider.setMinimum(this.minimum);
    this.slider.setMaximum(this.maximum);
    this.slider.setValue(this.current);

    this.slider.addChangeListener(ev -> {
      this.current = this.slider.getValue();
      this.refreshText();
      this.callListener();
    });

    this.field.setEditable(false);

    this.refreshText();
  }

  void setOnChangeListener(
    final IntConsumer p)
  {
    this.on_change = NullCheck.notNull(p, "Procedure");
  }

  @Override
  public void controlsAddToLayout(
    final DesignGridLayout layout)
  {
    layout
      .row()
      .group(this.group)
      .grid(this.label)
      .add(this.slider, 3)
      .add(this.field);
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

  int value()
  {
    return this.current;
  }

  private void refreshText()
  {
    final String ctext = String.format("%d", Integer.valueOf(this.current));
    this.field.setText(ctext);
  }

  void setCurrent(
    final int e)
  {
    this.slider.setValue(e);
    this.current = e;
    this.refreshText();
    this.callListener();
  }

  private void callListener()
  {
    final IntConsumer proc = this.on_change;
    if (proc != null) {
      proc.accept(this.current);
    }
  }

  void setEnabled(final boolean enabled)
  {
    this.slider.setEnabled(enabled);
  }
}
