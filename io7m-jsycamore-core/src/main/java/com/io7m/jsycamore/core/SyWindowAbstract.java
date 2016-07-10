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
import com.io7m.jsycamore.core.components.SyButtonAbstract;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.components.SyPanelAbstract;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulator;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jsycamore.core.themes.SyThemeType;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameType;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.core.themes.SyThemeWindowType;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVector2IType;
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

  private final PVector2IType<SySpaceViewportType> position;
  private final SyGUIType gui;
  private final VectorM2I bounds;
  private final WindowRoot root;
  private final SyWindowViewportAccumulatorType transform_context;
  private Optional<SyThemeType> theme_override;

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
    this.position = new PVectorM2I<>();
    this.bounds = new VectorM2I(width, height);
    this.root = new WindowRoot(in_text);
    this.transform_context = SyWindowViewportAccumulator.create();
    this.theme_override = Optional.empty();

    this.recalculateBounds(width, height);
  }

  @SuppressWarnings("unchecked")
  private static <S extends SySpaceType, T extends S, U extends S>
  PVectorReadable2IType<U> castSpace(final PVectorReadable2IType<T> v)
  {
    return (PVectorReadable2IType<U>) v;
  }

  private static int calculateOutlineSize(final SyThemeWindowType window_theme)
  {
    final Optional<SyThemeOutlineType> window_outline = window_theme.outline();
    final int outline_size;
    if (window_outline.isPresent()) {
      outline_size = 1;
    } else {
      outline_size = 0;
    }
    return outline_size;
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

    final VectorReadable2IType current_bounds = this.bounds();
    this.recalculateBounds(current_bounds.getXI(), current_bounds.getYI());
  }

  @Override
  public final void setTheme(final Optional<SyThemeType> in_theme)
  {
    this.theme_override = NullCheck.notNull(in_theme);
    final VectorReadable2IType current_bounds = this.bounds();
    this.recalculateBounds(current_bounds.getXI(), current_bounds.getYI());
  }

  @Override
  public final SyComponentType contentPane()
  {
    return this.root.frame.content_pane;
  }

  @Override
  public final String toString()
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("[SyWindowAbstract 0x");
    sb.append(Integer.toHexString(this.hashCode()));
    sb.append(" ");
    sb.append(this.root.titlebar.text());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public final VectorReadable2IType bounds()
  {
    return this.bounds;
  }

  @Override
  public final SyThemeType theme()
  {
    if (this.theme_override.isPresent()) {
      return this.theme_override.get();
    }
    return this.gui.theme();
  }

  @Override
  public final void setBounds(
    final int width,
    final int height)
  {
    this.recalculateBounds(width, height);
  }

  @Override
  public final void setPosition(
    final int x,
    final int y)
  {
    this.position.set2I(x, y);
  }

  private void recalculateBounds(
    final int width,
    final int height)
  {
    final SyThemeType theme = this.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowFrameType frame_theme = window_theme.frame();
    final SyThemeWindowTitleBarType title_theme = window_theme.titleBar();

    final int outline_size = SyWindowAbstract.calculateOutlineSize(window_theme);
    final int clamp_width = Math.max(width, 2);
    final int clamp_height = Math.max(height, 2);
    final int clamp_width_half = clamp_width / 2;

    int title_x = outline_size;
    int title_y = outline_size;
    final int outline_m2 = outline_size * 2;
    int title_width = clamp_width - outline_m2;
    final int title_height = title_theme.height();

    final int frame_x = outline_size;
    int frame_y = outline_size;
    final int frame_width = clamp_width - outline_m2;
    int frame_height = clamp_height - outline_m2;

    final int frame_left = frame_theme.leftWidth();
    final int frame_right = frame_theme.rightWidth();
    final int frame_top = frame_theme.topHeight();
    final int frame_bottom = frame_theme.bottomHeight();

    int content_y = 0;
    int content_h = 0;

    final String text_font = title_theme.textFont();

    switch (title_theme.verticalPlacement()) {
      case PLACEMENT_TOP_INSIDE_FRAME: {
        title_y = frame_top + outline_size;

        content_y = title_y + title_height;
        content_h = outline_size + frame_height - (content_y + frame_bottom);

        switch (title_theme.widthBehavior()) {
          case WIDTH_RESIZE_TO_CONTENT: {
            title_width = this.measureTitleSize(text_font);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                title_x = frame_left + outline_size;
                break;
              }
              case ALIGN_RIGHT: {
                title_x = (clamp_width - title_width) - (frame_right + outline_size);
                break;
              }
              case ALIGN_CENTER: {
                title_x = clamp_width_half - (title_width / 2);
                break;
              }
            }
            break;
          }
          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width = frame_width - (frame_left + frame_right);
            title_x += frame_left;
            break;
          }
          case WIDTH_RESIZE_TO_WINDOW: {
            break;
          }
        }
        break;
      }

      case PLACEMENT_TOP_OVERLAP_FRAME: {
        content_y = Math.max(title_y + title_height, frame_top + outline_size);
        content_h = outline_size + frame_height - (content_y + frame_bottom);

        switch (title_theme.widthBehavior()) {
          case WIDTH_RESIZE_TO_CONTENT: {
            title_width = this.measureTitleSize(text_font);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = clamp_width_half - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width = frame_width - (frame_left + frame_right);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = clamp_width_half - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_TO_WINDOW: {
            break;
          }
        }
        break;
      }

      case PLACEMENT_TOP_ABOVE_FRAME: {
        frame_height -= title_height;
        frame_y += title_height;

        content_y = frame_y + frame_top;
        content_h = frame_height - (frame_bottom + frame_top);

        switch (title_theme.widthBehavior()) {
          case WIDTH_RESIZE_TO_CONTENT: {
            title_width = this.measureTitleSize(text_font);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width - outline_size;
                break;
              }
              case ALIGN_CENTER: {
                title_x = clamp_width_half - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width = frame_width - (frame_left + frame_right);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = clamp_width_half - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_TO_WINDOW: {
            break;
          }
        }
        break;
      }
    }

    final int content_x = frame_x + frame_left;
    final int content_w = frame_width - (content_x + frame_right - outline_size);

    final int frame_inner_x_min = content_x;
    final int frame_inner_y_min = frame_y + frame_top;
    final int frame_inner_x_max = frame_width - frame_right;
    final int frame_inner_y_max = frame_height - frame_bottom;

    Assertive.ensure(clamp_width >= 2);
    Assertive.ensure(clamp_height >= 2);
    Assertive.ensure(title_width >= 2);
    Assertive.ensure(frame_width >= 2);
    Assertive.ensure(frame_height >= 2);

    final int orig_width = this.bounds.getXI();
    final int orig_height = this.bounds.getYI();
    if (orig_width != clamp_width && orig_height != clamp_height) {
      // XXX: Notify top-level components
    }

    this.bounds.set2I(clamp_width, clamp_height);
    this.root.setBounds(clamp_width, clamp_height);

    this.root.frame.setPosition(frame_x, frame_y);
    this.root.frame.setBounds(frame_width, frame_height);
    this.root.frame.setPositionInnerMinimum(
      frame_inner_x_min, frame_inner_y_min);
    this.root.frame.setPositionInnerMaximum(
      frame_inner_x_max, frame_inner_y_max);

    this.root.frame.content_pane.setBounds(content_w, content_h);
    this.root.frame.content_pane.setPosition(content_x, content_y);
    this.root.titlebar.setPosition(title_x, title_y);
    this.root.titlebar.setBounds(title_width, title_height);

    this.transform_context.reset(clamp_width, clamp_height);
  }

  private int measureTitleSize(final String text_font)
  {
    final SyTextMeasurementType measure = this.gui.textMeasurement();
    final int text_size =
      measure.measureTextWidth(text_font, this.root.titlebar.text());
    final int space_size = measure.measureTextWidth(text_font, " ");
    return (space_size * 2) + text_size;
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
    final int target_x = w_position.getXI();
    final int target_y = w_position.getYI();

    if (target_x >= 0 && target_x <= this.bounds.getXI()) {
      if (target_y >= 0 && target_y <= this.bounds.getYI()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final void transformViewportRelative(
    final PVectorReadable2IType<SySpaceViewportType> v_position,
    final PVectorWritable2IType<SySpaceWindowRelativeType> w_position)
  {
    NullCheck.notNull(v_position);
    NullCheck.notNull(w_position);
    VectorM2I.subtract(v_position, this.position, w_position);
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
  public final PVectorReadable2IType<SySpaceViewportType> position()
  {
    return this.position;
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

  private final class Titlebar extends SyPanelAbstract implements
    SyWindowTitlebarType
  {
    private final PVectorM2I<SySpaceWindowRelativeType> position;
    private final PVectorReadable2IType<SySpaceParentRelativeType> position_parent_view;
    private final PVectorM2I<SySpaceViewportType> window_drag_start;
    private final CloseBox close_box;
    private String text;

    Titlebar(final String in_text)
    {
      this.text = NullCheck.notNull(in_text);
      this.position = new PVectorM2I<>();
      this.position_parent_view = SyWindowAbstract.castSpace(this.position);
      this.window_drag_start = new PVectorM2I<>();
      this.close_box = new CloseBox();
      this.node().childAdd(this.close_box.node());
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("[Titlebar \"");
      sb.append(this.text);
      sb.append("\"]");
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
          final PVectorI2I<SySpaceViewportType> diff =
            PVectorI2I.subtract(mouse_position_now, mouse_position_first);
          final PVectorI2I<SySpaceViewportType> current =
            PVectorI2I.add(this.window_drag_start, diff);
          SyWindowAbstract.this.setPosition(current.getXI(), current.getYI());
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
          PVectorM2I.copy(
            SyWindowAbstract.this.position(), this.window_drag_start);
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
      return this.text;
    }

    @Override
    public void setText(final String in_text)
    {
      this.text = NullCheck.notNull(in_text);
      SyWindowAbstract.this.setBounds(
        SyWindowAbstract.this.bounds.getXI(),
        SyWindowAbstract.this.bounds.getYI());
    }
  }

  private final class CloseBox extends SyButtonAbstract implements
    SyWindowCloseBoxType
  {
    CloseBox()
    {

    }
  }

  private final class ContentPane extends SyPanelAbstract implements
    SyWindowContentPaneType
  {
    ContentPane()
    {
      this.setPanelTransparent(true);
    }
  }

  private final class Frame extends SyPanelAbstract implements
    SyWindowFrameType
  {
    private final ContentPane content_pane;
    private int inner_x_min;
    private int inner_y_min;
    private int inner_x_max;
    private int inner_y_max;

    @Override
    protected boolean isOverlappingExcludedArea(
      final int viewport_min_x,
      final int viewport_min_y,
      final int viewport_max_x,
      final int viewport_max_y,
      final int target_x,
      final int target_y)
    {
      final boolean inside_x =
        target_x >= this.inner_x_min && target_x <= this.inner_x_max;
      final boolean inside_y =
        target_y >= this.inner_y_min && target_y <= this.inner_y_max;
      return inside_x && inside_y;
    }

    Frame()
    {
      this.content_pane = new ContentPane();
      final JOTreeNodeType<SyComponentType> node = this.node();
      node.childAdd(this.content_pane.node());
    }

    void setPositionInnerMinimum(
      final int x_min,
      final int y_min)
    {
      this.inner_x_min = x_min;
      this.inner_y_min = y_min;
    }

    void setPositionInnerMaximum(
      final int x_max,
      final int y_max)
    {
      this.inner_x_max = x_max;
      this.inner_y_max = y_max;
    }
  }

  private final class WindowRoot extends SyPanelAbstract
  {
    private final Titlebar titlebar;
    private final Frame frame;

    WindowRoot(final String in_text)
    {
      this.setWindow(Optional.of(SyWindowAbstract.this));
      this.setSelectable(false);

      this.titlebar = new Titlebar(in_text);
      this.frame = new Frame();

      final JOTreeNodeType<SyComponentType> node = this.node();
      node.childAdd(this.frame.node());
      node.childAdd(this.titlebar.node());
    }
  }

}
