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

import com.connectina.swing.FontChooserDialog;
import com.io7m.jnull.NullCheck;
import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

final class SyTDFontSelector extends JPanel implements SyTDControlsType
{
  private final JButton select;
  private final JTextField field;
  private final RowGroup group;
  private final String title;
  private Font font;
  private List<Runnable> listeners;

  SyTDFontSelector(final String in_title)
  {
    this.title = NullCheck.notNull(in_title, "Title");
    this.listeners = new ArrayList<>(1);
    this.field = new JTextField();
    this.field.setEditable(false);
    this.font = Font.decode("Monospaced-plain-10");
    this.field.setText(this.fontString());
    this.group = new RowGroup();

    this.select = new JButton("Select...");
    this.select.addActionListener(e -> {
      final FontChooserDialog dialog =
        new FontChooserDialog((JFrame) null, "Select a font", true);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
      if (!dialog.isCancelSelected()) {
        this.font = dialog.getSelectedFont();
        this.field.setText(this.fontString());
        this.update();
      }
    });

    this.update();
  }

  @Override
  public void setEnabled(final boolean enabled)
  {
    super.setEnabled(enabled);
    this.select.setEnabled(enabled);
  }

  private void update()
  {
    for (final Runnable r : this.listeners) {
      r.run();
    }
  }

  public Font font()
  {
    return this.font;
  }

  void addListener(final Runnable on_update)
  {
    this.listeners.add(NullCheck.notNull(on_update, "Listener"));
  }

  @Override
  public void controlsAddToLayout(final DesignGridLayout layout)
  {
    layout.row().group(this.group).grid(new JLabel(this.title)).add(
      this.field,
      2).add(this.select);
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

  String fontString()
  {
    final String style;
    if (this.font.isBold()) {
      style = this.font.isItalic() ? "bolditalic" : "bold";
    } else {
      style = this.font.isItalic() ? "italic" : "plain";
    }

    return String.format(
      "%s-%s-%d",
      this.font.getFamily(),
      style,
      Integer.valueOf(this.font.getSize()));
  }
}
