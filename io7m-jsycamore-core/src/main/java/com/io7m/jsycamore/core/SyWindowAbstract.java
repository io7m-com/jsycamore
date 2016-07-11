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
import net.jcip.annotations.Immutable;
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
  public final SyWindowContentPaneType contentPane()
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
    sb.append(" ");
    sb.append(this.bounds.getXI());
    sb.append("x");
    sb.append(this.bounds.getYI());
    sb.append(" ");
    sb.append(this.position.getXI());
    sb.append("+");
    sb.append(this.position.getYI());
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

  @Immutable
  private static final class Extents
  {
    private final int x_min;
    private final int x_max;
    private final int y_min;
    private final int y_max;

    Extents(
      final int in_x_min,
      final int in_x_max,
      final int in_y_min,
      final int in_y_max)
    {
      Assertive.require(in_x_min >= 0, "X minimum must be >= 0");
      Assertive.require(in_y_min >= 0, "Y minimum must be >= 0");
      Assertive.require(in_x_min <= in_x_max, "X minimum must be <= X maximum");
      Assertive.require(in_y_min <= in_y_max, "Y minimum must be <= Y maximum");

      this.x_min = in_x_min;
      this.x_max = in_x_max;
      this.y_min = in_y_min;
      this.y_max = in_y_max;
    }
  }

  @Immutable
  private static final class WindowExtents
  {
    private final Extents frame;
    private final Extents frame_inner;
    private final Extents titlebar;
    private final Extents content;

    WindowExtents(
      final Extents in_frame,
      final Extents in_frame_inner,
      final Extents in_titlebar,
      final Extents in_content)
    {
      Assertive.require(
        in_frame_inner.x_min >= in_frame.x_min,
        "Frame inner minimum X >= Frame minimum X");
      Assertive.require(
        in_frame_inner.x_max <= in_frame.x_max,
        "Frame inner maximum X <= Frame maximum X");
      Assertive.require(
        in_frame_inner.y_min >= in_frame.y_min,
        "Frame inner minimum Y >= Frame minimum Y");
      Assertive.require(
        in_frame_inner.y_max <= in_frame.y_max,
        "Frame inner maximum Y <= Frame maximum Y");

      Assertive.require(
        in_content.x_min >= in_frame_inner.x_min,
        "Content minimum X >= Frame inner minimum X");
      Assertive.require(
        in_content.x_max <= in_frame_inner.x_max,
        "Content maximum X <= Frame inner maximum X");
      Assertive.require(
        in_content.y_min >= in_frame_inner.y_min,
        "Content minimum Y >= Frame inner minimum Y");
      Assertive.require(
        in_content.y_max <= in_frame_inner.y_max,
        "Content maximum Y <= Frame inner maximum Y");

      this.frame = in_frame;
      this.frame_inner = in_frame_inner;
      this.titlebar = in_titlebar;
      this.content = in_content;
    }
  }

  /**
   * Calculate the various extents when the titlebar is placed inside the
   * frame.
   */

  private WindowExtents calculateWindowExtentsWithTitleInsideFrame(
    final SyThemeWindowFrameType frame_theme,
    final SyThemeWindowTitleBarType title_theme,
    final int root_width,
    final int root_height,
    final int outline_size)
  {
    final Extents ext_frame;
    {
      final int frame_x_min = outline_size;
      final int frame_x_max = root_width - outline_size;
      final int frame_y_min = outline_size;
      final int frame_y_max = root_height - outline_size;
      ext_frame =
        new Extents(frame_x_min, frame_x_max, frame_y_min, frame_y_max);
    }

    final Extents ext_frame_inner =
      SyWindowAbstract.calculateExtentsFrameInner(frame_theme, ext_frame);

    int titlebar_x_min = -1;
    int titlebar_x_max = -1;
    final int titlebar_y_min = ext_frame_inner.y_min;
    final int titlebar_y_max = titlebar_y_min + title_theme.height();

    switch (title_theme.widthBehavior()) {
      case WIDTH_RESIZE_TO_CONTENT: {
        final int title_width = this.measureTitleSize(title_theme.textFont());

        switch (title_theme.horizontalAlignment()) {
          case ALIGN_LEFT: {
            titlebar_x_min = ext_frame_inner.x_min;
            titlebar_x_max = titlebar_x_min + title_width;
            break;
          }
          case ALIGN_RIGHT: {
            titlebar_x_min = ext_frame_inner.x_max - title_width;
            titlebar_x_max = ext_frame_inner.x_max;
            break;
          }
          case ALIGN_CENTER: {
            titlebar_x_min = (root_width / 2) - (title_width / 2);
            titlebar_x_max = titlebar_x_min + title_width;
            break;
          }
        }
        break;
      }

      case WIDTH_RESIZE_INSIDE_FRAME: {
        titlebar_x_min = ext_frame_inner.x_min;
        titlebar_x_max = ext_frame_inner.x_max;
        break;
      }

      case WIDTH_RESIZE_TO_WINDOW: {
        titlebar_x_min = ext_frame.x_min;
        titlebar_x_max = ext_frame.x_max;
        break;
      }
    }

    final Extents ext_titlebar = new Extents(
      titlebar_x_min, titlebar_x_max, titlebar_y_min, titlebar_y_max);

    final Extents ext_content = new Extents(
      ext_frame_inner.x_min,
      ext_frame_inner.x_max,
      titlebar_y_max,
      ext_frame_inner.y_max);

    return new WindowExtents(
      ext_frame, ext_frame_inner, ext_titlebar, ext_content);
  }

  /**
   * Calculate the various extents when the titlebar is overlapping the frame.
   */

  private WindowExtents calculateWindowExtentsWithTitleOverlappingFrame(
    final SyThemeWindowFrameType frame_theme,
    final SyThemeWindowTitleBarType title_theme,
    final int root_width,
    final int root_height,
    final int outline_size)
  {
    final Extents ext_frame;
    {
      final int frame_x_min = outline_size;
      final int frame_x_max = root_width - outline_size;
      final int frame_y_min = outline_size;
      final int frame_y_max = root_height - outline_size;
      ext_frame = new Extents(
        frame_x_min, frame_x_max, frame_y_min, frame_y_max);
    }

    final Extents ext_frame_inner =
      SyWindowAbstract.calculateExtentsFrameInner(frame_theme, ext_frame);

    int titlebar_x_min = -1;
    int titlebar_x_max = -1;
    final int titlebar_y_min = ext_frame.y_min;
    final int titlebar_y_max = titlebar_y_min + title_theme.height();

    switch (title_theme.widthBehavior()) {
      case WIDTH_RESIZE_TO_CONTENT: {
        final int title_width = this.measureTitleSize(title_theme.textFont());

        switch (title_theme.horizontalAlignment()) {
          case ALIGN_LEFT: {
            titlebar_x_min = ext_frame_inner.x_min;
            titlebar_x_max = titlebar_x_min + title_width;
            break;
          }
          case ALIGN_RIGHT: {
            titlebar_x_min = ext_frame_inner.x_max - title_width;
            titlebar_x_max = ext_frame_inner.x_max;
            break;
          }
          case ALIGN_CENTER: {
            titlebar_x_min = (root_width / 2) - (title_width / 2);
            titlebar_x_max = titlebar_x_min + title_width;
            break;
          }
        }
        break;
      }

      case WIDTH_RESIZE_INSIDE_FRAME: {
        titlebar_x_min = ext_frame_inner.x_min;
        titlebar_x_max = ext_frame_inner.x_max;
        break;
      }

      case WIDTH_RESIZE_TO_WINDOW: {
        titlebar_x_min = ext_frame.x_min;
        titlebar_x_max = ext_frame.x_max;
        break;
      }
    }

    final Extents ext_titlebar = new Extents(
      titlebar_x_min, titlebar_x_max, titlebar_y_min, titlebar_y_max);

    final Extents ext_content = new Extents(
      ext_frame_inner.x_min,
      ext_frame_inner.x_max,
      Math.max(ext_frame_inner.y_min, titlebar_y_max),
      ext_frame_inner.y_max);

    return new WindowExtents(
      ext_frame, ext_frame_inner, ext_titlebar, ext_content);
  }

  /**
   * Given a set of frame extents and the given theme, work out the extents of
   * the space inside the frame.
   */

  private static Extents calculateExtentsFrameInner(
    final SyThemeWindowFrameType frame_theme,
    final Extents ext_frame)
  {
    final int frame_size_left = frame_theme.leftWidth();
    final int frame_size_right = frame_theme.rightWidth();
    final int frame_size_top = frame_theme.topHeight();
    final int frame_size_bottom = frame_theme.bottomHeight();

    final int frame_inner_x_min = ext_frame.x_min + frame_size_left;
    final int frame_inner_x_max = ext_frame.x_max - frame_size_right;
    final int frame_inner_y_min = ext_frame.y_min + frame_size_top;
    final int frame_inner_y_max = ext_frame.y_max - frame_size_bottom;

    return new Extents(
      frame_inner_x_min,
      frame_inner_x_max,
      frame_inner_y_min,
      frame_inner_y_max);
  }

  /**
   * Calculate the various extents when the titlebar is placed above the frame.
   */

  private WindowExtents calculateWindowExtentsWithTitleAboveFrame(
    final SyThemeWindowFrameType frame_theme,
    final SyThemeWindowTitleBarType title_theme,
    final int root_width,
    final int root_height,
    final int outline_size)
  {
    int titlebar_x_min = -1;
    int titlebar_x_max = -1;
    final int titlebar_y_min = outline_size;
    final int titlebar_y_max = titlebar_y_min + (title_theme.height() - outline_size);

    final Extents ext_frame;
    {
      final int frame_x_min = outline_size;
      final int frame_x_max = root_width - outline_size;
      final int frame_y_min = titlebar_y_max;
      final int frame_y_max = root_height - outline_size;
      ext_frame =
        new Extents(frame_x_min, frame_x_max, frame_y_min, frame_y_max);
    }

    final Extents ext_frame_inner =
      SyWindowAbstract.calculateExtentsFrameInner(frame_theme, ext_frame);

    switch (title_theme.widthBehavior()) {
      case WIDTH_RESIZE_TO_CONTENT: {
        final int title_width = this.measureTitleSize(title_theme.textFont());

        switch (title_theme.horizontalAlignment()) {
          case ALIGN_LEFT: {
            titlebar_x_min = outline_size;
            titlebar_x_max = (titlebar_x_min + title_width) - outline_size;
            break;
          }
          case ALIGN_RIGHT: {
            titlebar_x_max = root_width - outline_size;
            titlebar_x_min = titlebar_x_max - title_width;
            break;
          }
          case ALIGN_CENTER: {
            titlebar_x_min = (root_width / 2) - (title_width / 2);
            titlebar_x_max = (titlebar_x_min + title_width - outline_size) - outline_size;
            break;
          }
        }
        break;
      }

      case WIDTH_RESIZE_INSIDE_FRAME: {
        final int frame_inner_width =
          ext_frame_inner.x_max - ext_frame_inner.x_min;

        switch (title_theme.horizontalAlignment()) {
          case ALIGN_LEFT: {
            titlebar_x_min = outline_size;
            titlebar_x_max = titlebar_x_min + frame_inner_width;
            break;
          }
          case ALIGN_RIGHT: {
            titlebar_x_max = root_width - outline_size;
            titlebar_x_min = titlebar_x_max - frame_inner_width;
            break;
          }
          case ALIGN_CENTER: {
            titlebar_x_min = (root_width / 2) - (frame_inner_width / 2);
            titlebar_x_max = titlebar_x_min + frame_inner_width;
            break;
          }
        }
        break;
      }

      case WIDTH_RESIZE_TO_WINDOW: {
        titlebar_x_min = outline_size;
        titlebar_x_max = root_width - outline_size;
        break;
      }
    }

    final Extents ext_titlebar = new Extents(
      titlebar_x_min, titlebar_x_max, titlebar_y_min, titlebar_y_max);

    final Extents ext_content = new Extents(
      ext_frame_inner.x_min,
      ext_frame_inner.x_max,
      ext_frame_inner.y_min,
      ext_frame_inner.y_max);

    return new WindowExtents(
      ext_frame, ext_frame_inner, ext_titlebar, ext_content);
  }

  private WindowExtents calculateWindowExtents(
    final SyThemeWindowFrameType frame_theme,
    final SyThemeWindowTitleBarType title_theme,
    final int root_width,
    final int root_height,
    final int outline_size)
  {
    switch (title_theme.verticalPlacement()) {
      case PLACEMENT_TOP_INSIDE_FRAME: {
        return this.calculateWindowExtentsWithTitleInsideFrame(
          frame_theme, title_theme, root_width, root_height, outline_size);
      }
      case PLACEMENT_TOP_OVERLAP_FRAME: {
        return this.calculateWindowExtentsWithTitleOverlappingFrame(
          frame_theme, title_theme, root_width, root_height, outline_size);
      }
      case PLACEMENT_TOP_ABOVE_FRAME: {
        return this.calculateWindowExtentsWithTitleAboveFrame(
          frame_theme, title_theme, root_width, root_height, outline_size);
      }
    }

    throw new UnreachableCodeException();
  }

  private void recalculateBounds(
    final int width,
    final int height)
  {
    final SyThemeType theme = this.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();
    final SyThemeWindowFrameType frame_theme = window_theme.frame();
    final SyThemeWindowTitleBarType title_theme = window_theme.titleBar();

    final int outline_size =
      SyWindowAbstract.calculateOutlineSize(window_theme);
    final int clamp_width = Math.max(width, 2);
    final int clamp_height = Math.max(height, 2);

    final WindowExtents extents = this.calculateWindowExtents(
      frame_theme, title_theme, clamp_width, clamp_height, outline_size);

    this.bounds.set2I(clamp_width, clamp_height);
    this.root.setBounds(clamp_width, clamp_height);

    this.root.frame.setPosition(
      extents.frame.x_min, extents.frame.y_min);
    this.root.frame.setBounds(
      extents.frame.x_max - extents.frame.x_min,
      extents.frame.y_max - extents.frame.y_min);
    this.root.frame.setPositionInnerMinimum(
      extents.frame_inner.x_min, extents.frame_inner.y_min);
    this.root.frame.setPositionInnerMaximum(
      extents.frame_inner.x_max, extents.frame_inner.y_max);

    this.root.content_pane.setPosition(
      extents.content.x_min, extents.content.y_min);
    this.root.content_pane.setBounds(
      extents.content.x_max - extents.content.x_min,
      extents.content.y_max - extents.content.y_min);

    this.root.titlebar.setPosition(
      extents.titlebar.x_min, extents.titlebar.y_min);
    this.root.titlebar.setBounds(
      extents.titlebar.x_max - extents.titlebar.x_min,
      extents.titlebar.y_max - extents.titlebar.y_min);

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
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach titlebar");
        return false;
      });

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
      sb.append("[Titlebar 0x");
      sb.append(Integer.toHexString(this.hashCode()));
      sb.append(" \"");
      sb.append(this.text);
      sb.append("\" ");
      sb.append(this.size().getXI());
      sb.append("x");
      sb.append(this.size().getYI());
      sb.append(" ");
      sb.append(this.position().getXI());
      sb.append("+");
      sb.append(this.position().getYI());
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
      final StringBuilder sb = new StringBuilder(128);
      sb.append("[ContentPane 0x");
      sb.append(Integer.toHexString(this.hashCode()));
      sb.append(" ");
      sb.append(this.size().getXI());
      sb.append("x");
      sb.append(this.size().getYI());
      sb.append(" ");
      sb.append(this.position().getXI());
      sb.append("+");
      sb.append(this.position().getYI());
      sb.append("]");
      return sb.toString();
    }
  }

  private final class Frame extends SyPanelAbstract implements
    SyWindowFrameType
  {
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
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach frame");
        return false;
      });

      this.inner_x_min = 0;
      this.inner_y_min = 0;
      this.inner_x_max = 0;
      this.inner_y_max = 0;
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

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("[Frame 0x");
      sb.append(Integer.toHexString(this.hashCode()));
      sb.append(" ");
      sb.append(this.size().getXI());
      sb.append("x");
      sb.append(this.size().getYI());
      sb.append(" ");
      sb.append(this.position().getXI());
      sb.append("+");
      sb.append(this.position().getYI());
      sb.append(" (inner ");
      sb.append(this.inner_x_max - this.inner_x_min);
      sb.append("x");
      sb.append(this.inner_y_max - this.inner_y_min);
      sb.append(" ");
      sb.append(this.inner_x_min);
      sb.append("+");
      sb.append(this.inner_y_min);
      sb.append(")]");
      return sb.toString();
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
      node.childAdd(this.frame.node());
      node.childAdd(this.titlebar.node());
      node.childAdd(this.content_pane.node());
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("[WindowRoot 0x");
      sb.append(Integer.toHexString(this.hashCode()));
      sb.append(" ");
      sb.append(this.size().getXI());
      sb.append("x");
      sb.append(this.size().getYI());
      sb.append(" ");
      sb.append(this.position().getXI());
      sb.append("+");
      sb.append(this.position().getYI());
      sb.append("]");
      return sb.toString();
    }
  }

}
