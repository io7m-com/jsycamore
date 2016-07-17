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

package com.io7m.jsycamore.core;

import com.io7m.jnull.NullCheck;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.io7m.jsycamore.core.boxes.SyBox;
import com.io7m.jsycamore.core.boxes.SyBoxMutable;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.jsycamore.core.components.SyButtonAbstract;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.components.SyLabelAbstract;
import com.io7m.jsycamore.core.components.SyPanelAbstract;
import com.io7m.jsycamore.core.components.SyVisibility;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulator;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.SyThemeWindowArrangementType;
import com.io7m.jsycamore.core.themes.SyThemeWindowType;
import com.io7m.jtensors.parameterized.PVectorI2I;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorWritable2IType;
import com.io7m.junreachable.UnreachableCodeException;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.util.Optional;

/**
 * An abstract default implementation of the {@link SyWindowType} type.
 */

@NotThreadSafe
public abstract class SyWindowAbstract implements SyWindowType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SyWindowAbstract.class);
  }

  private final SyGUIType gui;
  private final WindowRoot root;
  private final SyWindowViewportAccumulatorType transform_context;
  private final SyBoxMutable<SySpaceViewportType> box;
  private Optional<SyThemeType> theme_override;
  private boolean closeable;
  private boolean maximizable;

  protected SyWindowAbstract(
    final SyGUIType in_gui,
    final int width,
    final int height,
    final String in_text)
  {
    RangeCheck.checkIncludedInInteger(
      width,
      "Window width",
      Ranges.NATURAL_INTEGER,
      "Valid window widths");
    RangeCheck.checkIncludedInInteger(
      height,
      "Window height",
      Ranges.NATURAL_INTEGER,
      "Valid window heights");

    this.gui = NullCheck.notNull(in_gui);
    this.box = SyBoxMutable.create(0, 0, 0, 0);
    this.box.from(SyBoxes.create(0, 0, width, height));

    this.root = new WindowRoot(in_text);
    this.transform_context = SyWindowViewportAccumulator.create();
    this.theme_override = Optional.empty();
    this.closeable = true;
    this.maximizable = false;

    this.recalculateBounds(this.box);
  }

  @Override
  public final void setBox(final SyBoxType<SySpaceViewportType> in_box)
  {
    this.recalculateBounds(NullCheck.notNull(in_box));
  }

  @Override
  public final SyBoxType<SySpaceViewportType> box()
  {
    return this.box;
  }

  @Override
  public final SyWindowFrameType frame()
  {
    return this.root.frame;
  }

  @Override
  public final SyWindowViewportAccumulatorType viewportAccumulator()
  {
    return this.transform_context;
  }

  @Override
  public final void onWindowGUIThemeChanged()
  {
    if (this.theme_override.isPresent()) {
      return;
    }

    this.recalculateBoundsRefresh();
  }

  @Override
  public final void setTheme(final Optional<SyThemeType> in_theme)
  {
    this.theme_override = NullCheck.notNull(in_theme);
    this.recalculateBoundsRefresh();
  }

  private void recalculateBoundsRefresh()
  {
    this.recalculateBounds(SyBox.copyOf(this.box));
  }

  @Override
  public final SyWindowContentPaneType contentPane()
  {
    return this.root.content_pane;
  }

  @Override
  public final String toString()
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[SyWindow 0x");
    sb.append(Integer.toHexString(this.hashCode()));
    sb.append(" \"");
    sb.append(this.root.titlebar.text());
    sb.append("\" ");
    SyBoxes.showToBuilder(this.box(), sb);
    sb.append("]");
    return sb.toString();
  }

  @Override
  public final SyThemeType theme()
  {
    if (this.theme_override.isPresent()) {
      return this.theme_override.get();
    }
    return this.gui.theme();
  }

  private void recalculateBounds(
    final SyBoxType<? extends SySpaceType> new_box)
  {
    final SyThemeType theme = this.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();

    final SyBoxType<SySpaceViewportType> window_box =
      SyBoxes.create(
        new_box.minimumX(),
        new_box.minimumY(),
        new_box.width(),
        new_box.height());

    final SyBoxType<SySpaceParentRelativeType> root_box =
      SyBoxes.create(0, 0, new_box.width(), new_box.height());
    final SyThemeWindowArrangementType boxes =
      window_theme.arranger().apply(
        this.gui.textMeasurement(),
        this,
        root_box);

    this.box.from(window_box);
    this.root.setBox(root_box);
    this.root.frame.box_inner.from(boxes.frameExclusionBox());
    this.root.frame.setBox(boxes.frameBox());
    this.root.titlebar.setBox(boxes.titlebarBox());
    this.root.content_pane.setBox(boxes.contentBox());

    this.transform_context.reset(window_box.width(), window_box.height());
  }

  @Override
  public final SyWindowTitlebarType titlebar()
  {
    return this.root.titlebar;
  }

  @Override
  public final boolean focused()
  {
    return this.gui.windowIsFocused(this);
  }

  private boolean isInBoundsWindowRelative(
    final PVectorReadable2IType<SySpaceWindowRelativeType> w_position)
  {
    final int target_x = Math.addExact(w_position.getXI(), this.box.minimumX());
    final int target_y = Math.addExact(w_position.getYI(), this.box.minimumY());
    return SyBoxes.containsPoint(this.box, target_x, target_y);
  }

  @Override
  public final void transformViewportRelative(
    final PVectorReadable2IType<SySpaceViewportType> v_position,
    final PVectorWritable2IType<SySpaceWindowRelativeType> w_position)
  {
    NullCheck.notNull(v_position);
    NullCheck.notNull(w_position);

    w_position.set2I(
      Math.subtractExact(v_position.getXI(), this.box.minimumX()),
      Math.subtractExact(v_position.getYI(), this.box.minimumY()));
  }

  @Override
  public final Optional<SyComponentType> componentForViewportPosition(
    final PVectorReadable2IType<SySpaceViewportType> v_position)
  {
    NullCheck.notNull(v_position);

    final PVectorM2I<SySpaceWindowRelativeType> w_position = new PVectorM2I<>();
    this.transformViewportRelative(v_position, w_position);
    return this.componentForWindowPosition(w_position);
  }

  @Override
  public final Optional<SyComponentType> componentForWindowPosition(
    final PVectorReadable2IType<SySpaceWindowRelativeType> w_position)
  {
    if (this.isInBoundsWindowRelative(w_position)) {
      return this.root.componentForWindowRelative(
        w_position, this.transform_context);
    }

    return Optional.empty();
  }

  @Override
  public void onWindowGainsFocus()
  {
    // XXX: Nothing yet
  }

  @Override
  public void onWindowLosesFocus()
  {
    // XXX: Nothing yet
  }

  @Override
  public final SyGUIType gui()
  {
    return this.gui;
  }

  @Override
  public final boolean isCloseable()
  {
    return this.closeable;
  }

  @Override
  public final void setCloseable(final boolean c)
  {
    this.closeable = c;

    if (this.isCloseable()) {
      this.root.titlebar.close_box.setVisibility(
        SyVisibility.VISIBILITY_VISIBLE);
    } else {
      this.root.titlebar.close_box.setVisibility(
        SyVisibility.VISIBILITY_INVISIBLE);
    }

    this.recalculateBoundsRefresh();
  }

  @Override
  public final boolean isMaximizable()
  {
    return this.maximizable;
  }

  private final class TitlebarText extends SyLabelAbstract
  {
    TitlebarText()
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach titlebar text");
        return false;
      });
    }
  }

  private final class Titlebar extends SyPanelAbstract implements
    SyWindowTitlebarType
  {
    private final PVectorM2I<SySpaceViewportType> window_drag_start;
    private final TitlebarCloseButton close_box;
    private final TitlebarText text;

    Titlebar(final String in_text)
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach titlebar");
        return false;
      });

      NullCheck.notNull(in_text);
      this.setPanelTransparent(true);

      this.text = new TitlebarText();
      this.text.setText(in_text);
      this.node().childAdd(this.text.node());

      this.close_box = new TitlebarCloseButton();
      this.node().childAdd(this.close_box.node());

      this.window_drag_start = new PVectorM2I<>();
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("[Titlebar 0x");
      sb.append(Integer.toHexString(this.hashCode()));
      sb.append(" \"");
      sb.append(this.text.text());
      sb.append("\" ");
      SyBoxes.showToBuilder(this.box(), sb);
      sb.append("]");
      return sb.toString();
    }

    @Override
    public boolean mouseHeld(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
      final PVectorReadable2IType<SySpaceViewportType> mouse_position_now,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      switch (button) {
        case MOUSE_BUTTON_LEFT: {
          final SyBoxType<SySpaceViewportType> window_start_box =
            SyWindowAbstract.this.box();

          final PVectorI2I<SySpaceViewportType> diff =
            PVectorI2I.subtract(mouse_position_now, mouse_position_first);
          final PVectorI2I<SySpaceViewportType> current =
            PVectorI2I.add(this.window_drag_start, diff);

          final SyBoxType<SySpaceViewportType> window_new_box =
            SyBoxes.moveAbsolute(
              window_start_box, current.getXI(), current.getYI());

          Assertive.ensure(
            window_start_box.width() == window_new_box.width(),
            "Dragging a titlebar must not resize width");
          Assertive.ensure(
            window_start_box.height() == window_new_box.height(),
            "Dragging a titlebar must not resize height");

          SyWindowAbstract.this.setBox(window_new_box);
          return true;
        }
        case MOUSE_BUTTON_MIDDLE:
        case MOUSE_BUTTON_RIGHT: {
          return false;
        }
      }

      throw new UnreachableCodeException();
    }

    @Override
    public boolean mousePressed(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      switch (button) {
        case MOUSE_BUTTON_LEFT: {
          this.window_drag_start.set2I(
            SyWindowAbstract.this.box.minimumX(),
            SyWindowAbstract.this.box.minimumY());
          return true;
        }
        case MOUSE_BUTTON_MIDDLE:
        case MOUSE_BUTTON_RIGHT: {
          return false;
        }
      }

      throw new UnreachableCodeException();
    }

    @Override
    public boolean mouseReleased(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      return false;
    }

    @Override
    public boolean mouseNoLongerOver()
    {
      return false;
    }

    @Override
    public boolean mouseOver(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyComponentType actual)
    {
      return false;
    }

    @Override
    public String text()
    {
      return this.text.text();
    }

    @Override
    public void setText(final String in_text)
    {
      this.text.setText(NullCheck.notNull(in_text));
      SyWindowAbstract.this.recalculateBoundsRefresh();
    }
  }

  private final class TitlebarCloseButton extends SyButtonAbstract implements
    SyWindowCloseBoxType
  {
    TitlebarCloseButton()
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach close box");
        return false;
      });
    }
  }

  private final class ContentPane extends SyPanelAbstract implements
    SyWindowContentPaneType
  {
    ContentPane()
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach content pane");
        return false;
      });

      this.setPanelTransparent(true);
    }

    @Override
    public String toString()
    {
      return this.toNamedString("ContentPane");
    }
  }

  private final class Frame extends SyPanelAbstract implements
    SyWindowFrameType
  {
    private final SyBoxMutable<SySpaceParentRelativeType> box_inner;

    Frame()
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach frame");
        return false;
      });

      this.box_inner = SyBoxMutable.create();
    }

    @Override
    protected boolean isOverlappingExcludedArea(
      final int viewport_min_x,
      final int viewport_min_y,
      final int viewport_max_x,
      final int viewport_max_y,
      final int target_x,
      final int target_y)
    {
      return SyBoxes.containsPoint(this.box_inner, target_x, target_y);
    }

    @Override
    public String toString()
    {
      return this.toNamedString("Frame");
    }
  }

  private final class WindowRoot extends SyPanelAbstract
  {
    private final Titlebar titlebar;
    private final Frame frame;
    private final ContentPane content_pane;

    WindowRoot(final String in_text)
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach window root");
        return false;
      });

      this.setWindow(Optional.of(SyWindowAbstract.this));
      this.setSelectable(false);

      this.titlebar = new Titlebar(in_text);
      this.frame = new Frame();
      this.content_pane = new ContentPane();

      final JOTreeNodeType<SyComponentType> node = this.node();
      node.childAdd(this.titlebar.node());
      node.childAdd(this.frame.node());
      node.childAdd(this.content_pane.node());
    }

    @Override
    public String toString()
    {
      return this.toNamedString("WindowRoot");
    }
  }

}
