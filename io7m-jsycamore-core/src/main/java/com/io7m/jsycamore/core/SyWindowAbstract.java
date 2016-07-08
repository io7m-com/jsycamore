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
import com.io7m.jsycamore.core.components.SyButtonType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.components.SyPanelAbstract;
import com.io7m.jsycamore.core.components.SyPanelType;
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
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.util.Optional;
import java.util.function.BiFunction;

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
  private final PVectorM2I<SySpaceWindowRelativeType> frame_size;
  private final PVectorM2I<SySpaceWindowRelativeType> frame_position;
  private final SyGUIType gui;
  private final VectorM2I bounds;
  private final WindowRoot root;
  private final SyWindowViewportAccumulatorType transform_context;
  private SyThemeType theme;

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
    this.frame_size = new PVectorM2I<>();
    this.frame_position = new PVectorM2I<>();
    this.root = new WindowRoot(in_text);
    this.transform_context = SyWindowViewportAccumulator.create();

    this.themeReload(in_gui.theme());
    this.recalculateBounds(width, height);
  }

  @SuppressWarnings("unchecked")
  private static <S extends SySpaceType, T extends S, U extends S>
  PVectorReadable2IType<U> castSpace(final PVectorReadable2IType<T> v)
  {
    return (PVectorReadable2IType<U>) v;
  }

  @Override
  public final SyComponentType contentPane()
  {
    return this.root.content_pane;
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

  private void themeReload(final SyThemeType new_theme)
  {
    this.theme = new_theme;
  }

  @Override
  public final VectorReadable2IType bounds()
  {
    return this.bounds;
  }

  @Override
  public final SyThemeType theme()
  {
    return this.theme;
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
    final SyThemeWindowType window_theme = this.theme.windowTheme();
    final SyThemeWindowFrameType frame_theme = window_theme.frame();
    final SyThemeWindowTitleBarType title_theme = window_theme.titleBar();

    final Optional<SyThemeOutlineType> window_outline = window_theme.outline();
    final int outline_size;
    if (window_outline.isPresent()) {
      outline_size = 1;
    } else {
      outline_size = 0;
    }

    final int clamp_width = Math.max(width, 2);
    final int clamp_height = Math.max(height, 2);

    int title_x = outline_size;
    int title_y = outline_size;
    int title_width = clamp_width - (outline_size * 2);
    final int title_height = title_theme.height();

    final int frame_x = outline_size;
    int frame_y = outline_size;
    final int frame_width = clamp_width - (outline_size * 2);
    int frame_height = clamp_height - (outline_size * 2);

    final int frame_left = frame_theme.leftWidth();
    final int frame_right = frame_theme.rightWidth();

    final String text_font = title_theme.textFont();
    switch (title_theme.verticalPlacement()) {
      case PLACEMENT_TOP_INSIDE_FRAME: {
        title_y = frame_theme.topHeight() + outline_size;

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
                title_x = (clamp_width / 2) - (title_width / 2);
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
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width =
              frame_width - (frame_left + frame_right);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
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
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width =
              frame_width - (frame_left + frame_right);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
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
    this.root.titlebar.setPosition(title_x, title_y);
    this.root.titlebar.setBounds(title_width, title_height);
    this.frame_position.set2I(frame_x, frame_y);
    this.frame_size.set2I(frame_width, frame_height);
    this.transform_context.setSize(clamp_width, clamp_height);
  }

  private int measureTitleSize(final String text_font)
  {
    final SyTextMeasurementType measure = this.gui.textMeasurement();
    final int text_size =
      measure.measureText(text_font, this.root.titlebar.text());
    final int space_size = measure.measureText(text_font, " ");
    return (space_size * 2) + text_size;
  }

  @Override
  public final PVectorReadable2IType<SySpaceWindowRelativeType> framePosition()
  {
    return this.frame_position;
  }

  @Override
  public final PVectorReadable2IType<SySpaceWindowRelativeType> frameBounds()
  {
    return this.frame_size;
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
    private final PVectorM2I<SySpaceWindowRelativeType> size;
    private final PVectorM2I<SySpaceWindowRelativeType> position;
    private final PVectorReadable2IType<SySpaceParentRelativeType> position_parent_view;
    private final PVectorM2I<SySpaceViewportType> window_drag_start;
    private final CloseBox close_box;
    private String text;

    Titlebar(final String in_text)
    {
      this.text = NullCheck.notNull(in_text);
      this.size = new PVectorM2I<>();
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
    public void onMouseHeld(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position_first,
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      switch (button) {
        case MOUSE_BUTTON_LEFT: {
          final PVectorI2I<SySpaceViewportType> diff =
            PVectorI2I.subtract(mouse_position, mouse_position_first);
          final PVectorI2I<SySpaceViewportType> current =
            PVectorI2I.add(this.window_drag_start, diff);
          SyWindowAbstract.this.setPosition(current.getXI(), current.getYI());
          break;
        }
        case MOUSE_BUTTON_MIDDLE:
        case MOUSE_BUTTON_RIGHT: {
          break;
        }
      }
    }

    @Override
    public void onMousePressed(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      switch (button) {
        case MOUSE_BUTTON_LEFT: {
          PVectorM2I.copy(
            SyWindowAbstract.this.position(), this.window_drag_start);
          break;
        }
        case MOUSE_BUTTON_MIDDLE:
        case MOUSE_BUTTON_RIGHT: {
          break;
        }
      }
    }

    @Override
    public void onMouseReleased(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyMouseButton button,
      final SyComponentType actual)
    {

    }

    @Override
    public void onMouseNoLongerOver()
    {

    }

    @Override
    public void onMouseOver(
      final PVectorReadable2IType<SySpaceViewportType> mouse_position,
      final SyComponentType actual)
    {

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

    @Override
    public <A, B> B matchComponent(
      final A context,
      final BiFunction<A, SyButtonType, B> on_button,
      final BiFunction<A, SyPanelType, B> on_panel)
    {
      return NullCheck.notNull(on_panel).apply(context, this);
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

    }
  }

  private final class WindowRoot extends SyPanelAbstract
  {
    private final Titlebar titlebar;
    private final ContentPane content_pane;

    WindowRoot(final String in_text)
    {
      this.setWindow(Optional.of(SyWindowAbstract.this));
      this.setSelectable(false);

      this.titlebar = new Titlebar(in_text);
      this.content_pane = new ContentPane();

      final JOTreeNodeType<SyComponentType> node = this.node();
      node.childAdd(this.content_pane.node());
      node.childAdd(this.titlebar.node());
    }
  }

}
