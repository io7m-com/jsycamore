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

package com.io7m.jsycamore.api.windows;

import com.io7m.jaffirm.core.Postconditions;
import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jorchard.core.JOTreeNodeType;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.SyParentResizeBehavior;
import com.io7m.jsycamore.api.components.SyActive;
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.components.SyButtonRepeatingAbstract;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyImage;
import com.io7m.jsycamore.api.components.SyImageAbstract;
import com.io7m.jsycamore.api.components.SyImageType;
import com.io7m.jsycamore.api.components.SyLabelAbstract;
import com.io7m.jsycamore.api.components.SyLabelReadableType;
import com.io7m.jsycamore.api.components.SyPanelAbstract;
import com.io7m.jsycamore.api.components.SyPanelReadableType;
import com.io7m.jsycamore.api.components.SyPanelType;
import com.io7m.jsycamore.api.components.SyVisibility;
import com.io7m.jsycamore.api.components.SyWindowViewportAccumulator;
import com.io7m.jsycamore.api.components.SyWindowViewportAccumulatorType;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.spaces.SySpaceWindowRelativeType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeButtonRepeating;
import com.io7m.jsycamore.api.themes.SyThemeImage;
import com.io7m.jsycamore.api.themes.SyThemeLabel;
import com.io7m.jsycamore.api.themes.SyThemePanel;
import com.io7m.jsycamore.api.themes.SyThemeTitleBars;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangementFunctionType;
import com.io7m.jsycamore.api.themes.SyThemeWindowArrangementType;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBarArrangementType;
import com.io7m.jsycamore.api.themes.SyThemeWindowTitleBarType;
import com.io7m.jsycamore.api.themes.SyThemeWindowType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import com.io7m.junreachable.UnreachableCodeException;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * An abstract default implementation of the {@link SyWindowType} type.
 */

@NotThreadSafe
public abstract class SyWindowAbstract implements SyWindowType
{
  private static final Logger LOG;
  private static final Logger LOG_FOCUS;

  static {
    LOG = LoggerFactory.getLogger(SyWindowAbstract.class);
    LOG_FOCUS = LoggerFactory.getLogger(SyWindowAbstract.LOG.getName() + ".focus");
  }

  private final SyGUIType gui;
  private final WindowRoot root;
  private final SyWindowViewportAccumulatorType transform_context;
  private PAreaI<SySpaceViewportType> box;
  private Optional<SyTheme> theme_override;
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

    this.gui = Objects.requireNonNull(in_gui, "GUI");
    this.box = PAreasI.create(0, 0, width, height);

    this.root = new WindowRoot(in_text);
    this.transform_context = SyWindowViewportAccumulator.create();
    this.theme_override = Optional.empty();
    this.closeable = true;
    this.maximizable = true;

    this.recalculateBounds(this.box, true);
  }

  @Override
  public final void setBox(final PAreaI<SySpaceViewportType> in_box)
  {
    this.recalculateBounds(Objects.requireNonNull(in_box, "Box"), false);
  }

  @Override
  public final PAreaI<SySpaceViewportType> box()
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

    this.recalculateBoundsRefresh(true);
  }

  @Override
  public final void setTheme(final Optional<SyTheme> in_theme)
  {
    this.theme_override = Objects.requireNonNull(in_theme, "Theme");
    this.recalculateBoundsRefresh(true);
  }

  private void recalculateBoundsRefresh(
    final boolean theme_changed)
  {
    this.recalculateBounds(this.box, theme_changed);
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
    PAreasI.showToBuilder(this.box(), sb);
    sb.append("]");
    return sb.toString();
  }

  @Override
  public final SyTheme theme()
  {
    if (this.theme_override.isPresent()) {
      return this.theme_override.get();
    }
    return this.gui.theme();
  }

  private void recalculateBounds(
    final PAreaI<? extends SySpaceType> new_box,
    final boolean theme_changed)
  {
    final SyTheme theme = this.theme();
    final SyThemeWindowType window_theme = theme.windowTheme();

    final PAreaI<SySpaceViewportType> window_box =
      PAreasI.create(
        new_box.minimumX(),
        new_box.minimumY(),
        new_box.width(),
        new_box.height());

    final PAreaI<SySpaceParentRelativeType> root_box =
      PAreasI.create(0, 0, new_box.width(), new_box.height());
    final SyThemeWindowArrangementFunctionType arranger =
      window_theme.arranger();
    final SyThemeWindowArrangementType boxes =
      arranger.apply(
        this.gui.textMeasurement(),
        this,
        root_box);

    Preconditions.checkPrecondition(
      PAreasI.contains(root_box, boxes.contentBox()),
      "Root box must contain content box");
    Preconditions.checkPrecondition(
      PAreasI.contains(root_box, boxes.frameBox()),
      "Root box must contain frame box");
    Preconditions.checkPrecondition(
      PAreasI.contains(boxes.frameBox(), boxes.frameExclusionBox()),
      "Frame box must contain frame exclusion box");
    Preconditions.checkPrecondition(
      PAreasI.contains(root_box, boxes.titlebarBox()),
      "Root box must contain titleBar box");

    this.box = window_box;
    this.root.titlebar.text.setTextAlignmentHorizontal(
      theme.windowTheme().titleBar().textAlignment());
    this.root.titlebar.setBox(boxes.titlebarBox());
    this.root.frame.setBox(boxes.frameBox());
    this.root.frame.box_inner = (boxes.frameExclusionBox());
    this.root.content_pane.setBox(boxes.contentBox());
    this.root.setBox(root_box);

    this.root.titlebar.maximize_button.setIcon(
      window_theme.titleBar().buttonMaximizeIcon());
    this.root.titlebar.close_button.setIcon(
      window_theme.titleBar().buttonCloseIcon());

    this.transform_context.reset(window_box.width(), window_box.height());

    if (theme_changed) {
      this.root.content_pane.onThemeChanged();
    }
  }

  @Override
  public final SyWindowTitleBarType titleBar()
  {
    return this.root.titlebar;
  }

  @Override
  public final boolean isFocused()
  {
    return this.gui.windowIsFocused(this);
  }

  @Override
  public final boolean isOpen()
  {
    return this.gui.windowIsOpen(this);
  }

  private boolean isInBoundsWindowRelative(
    final PVector2I<SySpaceWindowRelativeType> w_position)
  {
    final int target_x = Math.addExact(w_position.x(), this.box.minimumX());
    final int target_y = Math.addExact(w_position.y(), this.box.minimumY());
    return PAreasI.containsPoint(this.box, target_x, target_y);
  }

  @Override
  public final PVector2I<SySpaceWindowRelativeType> transformViewportRelative(
    final PVector2I<SySpaceViewportType> v_position)
  {
    Objects.requireNonNull(v_position, "Position");

    return PVector2I.of(
      Math.subtractExact(v_position.x(), this.box.minimumX()),
      Math.subtractExact(v_position.y(), this.box.minimumY()));
  }

  @Override
  public final Optional<SyComponentType> componentForViewportPosition(
    final PVector2I<SySpaceViewportType> v_position)
  {
    Objects.requireNonNull(v_position, "Position");
    return this.componentForWindowPosition(
      this.transformViewportRelative(v_position));
  }

  @Override
  public final Optional<SyComponentType> componentForWindowPosition(
    final PVector2I<SySpaceWindowRelativeType> w_position)
  {
    if (this.isInBoundsWindowRelative(w_position)) {
      return this.root.componentForWindowRelative(
        w_position, this.transform_context);
    }

    return Optional.empty();
  }

  @Override
  public final void onWindowGainsFocus()
  {
    SyWindowAbstract.LOG_FOCUS.trace("focus gained: {}", this);
    this.root.titlebar.setActive(SyActive.ACTIVE);
  }

  @Override
  public final void onWindowLosesFocus()
  {
    SyWindowAbstract.LOG_FOCUS.trace("focus lost: {}", this);
    this.root.titlebar.setActive(SyActive.INACTIVE);
  }

  @Override
  public final void onWindowClosed()
  {
    SyWindowAbstract.LOG_FOCUS.trace("closed: {}", this);
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
      this.root.titlebar.close_button.setVisibility(
        SyVisibility.VISIBILITY_VISIBLE);
    } else {
      this.root.titlebar.close_button.setVisibility(
        SyVisibility.VISIBILITY_INVISIBLE);
    }

    this.recalculateBoundsRefresh(false);
  }

  @Override
  public final boolean isMaximizable()
  {
    return this.maximizable;
  }

  @Override
  public final void setMaximizable(final boolean c)
  {
    this.maximizable = c;

    if (this.isMaximizable()) {
      this.root.titlebar.maximize_button.setVisibility(
        SyVisibility.VISIBILITY_VISIBLE);
    } else {
      this.root.titlebar.maximize_button.setVisibility(
        SyVisibility.VISIBILITY_INVISIBLE);
    }

    this.recalculateBoundsRefresh(false);
  }

  private abstract static class IconButton extends SyButtonRepeatingAbstract
  {
    private Optional<SyImageType> image;

    IconButton(final BooleanSupplier in_detach_check)
    {
      super(in_detach_check);
      this.image = Optional.empty();
    }

    final void setIcon(
      final Optional<SyImageSpecification> in_icon)
    {
      Objects.requireNonNull(in_icon, "Icon");

      if (this.image.isPresent()) {
        final SyImageType i = this.image.get();
        this.node().childRemove(i.node());
      }

      if (in_icon.isPresent()) {
        final SyImageSpecification icon = in_icon.get();
        final SyImageType i = SyImage.create(icon);
        i.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        i.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        i.setBox(PAreasI.create(0, 0, this.box().width(), this.box().height()));
        this.node().childAdd(i.node());
        this.image = Optional.of(i);
      }

      Postconditions.checkPostcondition(
        this.node().children().size() <= 1,
        "Icon button must not leak components");
    }
  }

  private final class TitleBarText extends SyLabelAbstract
  {
    TitleBarText()
    {
      super(() -> {
        SyWindowAbstract.LOG.debug("refusing to detach titleBar text");
        return false;
      });
    }

    @Override
    public String toString()
    {
      return this.toNamedString("TitleBarText");
    }

    @Override
    public Optional<SyThemeLabel> theme()
    {
      return Optional.of(
        SyWindowAbstract.this.theme().windowTheme().titleBar().textTheme());
    }
  }

  private final class TitleBarIcon extends SyPanelAbstract implements
    SyPanelType
  {
    private Optional<TitleBarIconImage> image;

    TitleBarIcon()
    {
      super(
        () -> {
          SyWindowAbstract.LOG.warn("refusing to detach title bar icon");
          return false;
        });

      this.setPanelTransparent(true);
      this.image = Optional.empty();
    }

    @Override
    public String toString()
    {
      return this.toNamedString("TitleBarIcon");
    }

    @Override
    public Optional<SyThemePanel> theme()
    {
      return Optional.of(SyWindowAbstract.this.theme().panelTheme());
    }

    void setIcon(final Optional<SyImageSpecification> in_icon)
    {
      Objects.requireNonNull(in_icon, "Icon");

      if (this.image.isPresent()) {
        final SyImageType i = this.image.get();
        this.node().childRemove(i.node());
      }

      if (in_icon.isPresent()) {
        final SyImageSpecification icon = in_icon.get();
        final TitleBarIconImage i = new TitleBarIconImage(icon);
        i.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        i.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        i.setBox(PAreasI.create(0, 0, this.box().width(), this.box().height()));
        this.node().childAdd(i.node());
        this.image = Optional.of(i);
      }

      Postconditions.checkPostcondition(
        this.node().children().size() <= 1,
        "Titlebar icon must not leak components");
    }

    private final class TitleBarIconImage extends SyImageAbstract implements
      SyImageType
    {
      TitleBarIconImage(
        final SyImageSpecification in_image)
      {
        super(in_image, () -> true);
      }

      @Override
      public Optional<SyThemeImage> theme()
      {
        final SyTheme theme = SyWindowAbstract.this.theme();
        return Optional.of(theme.windowTheme().titleBar().iconTheme());
      }

      @Override
      public String toString()
      {
        return this.toNamedString("TitleBarIconImage");
      }
    }
  }

  private final class TitleBar extends SyPanelAbstract implements
    SyWindowTitleBarType
  {
    private final TitleBarCloseButton close_button;
    private final TitleBarText text;
    private final TitleBarMaximizeButton maximize_button;
    private final TitleBarIcon icon;
    private PVector2I<SySpaceViewportType> window_drag_start;

    TitleBar(final String in_text)
    {
      super(() -> {
        SyWindowAbstract.LOG.warn("refusing to detach title bar");
        return false;
      });

      Objects.requireNonNull(in_text, "Text");

      this.text = new TitleBarText();
      this.text.setText(in_text);
      this.node().childAdd(this.text.node());

      this.icon = new TitleBarIcon();
      this.maximize_button = new TitleBarMaximizeButton();
      this.close_button = new TitleBarCloseButton();
      this.node().childAdd(this.icon.node());
      this.node().childAdd(this.close_button.node());
      this.node().childAdd(this.maximize_button.node());
    }

    @Override
    public void resized(
      final int delta_x,
      final int delta_y)
    {
      final SyTheme theme = SyWindowAbstract.this.theme();
      final SyThemeWindowType theme_window = theme.windowTheme();
      final SyThemeWindowTitleBarType theme_titlebar = theme_window.titleBar();

      final SyThemeWindowTitleBarArrangementType arranged =
        SyThemeTitleBars.arrange(
          super.box(),
          theme_titlebar,
          SyWindowAbstract.this.isCloseable(),
          SyWindowAbstract.this.isMaximizable());

      this.close_button.setBox(arranged.closeButtonBox());
      this.maximize_button.setBox(arranged.maximizeButtonBox());
      this.icon.setBox(arranged.iconBox());
      this.text.setBox(arranged.title());
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("[TitleBar 0x");
      sb.append(Integer.toHexString(this.hashCode()));
      sb.append(" \"");
      sb.append(this.text.text());
      sb.append("\" ");
      PAreasI.showToBuilder(super.box(), sb);
      sb.append("]");
      return sb.toString();
    }

    @Override
    public boolean mouseHeld(
      final PVector2I<SySpaceViewportType> mouse_position_first,
      final PVector2I<SySpaceViewportType> mouse_position_now,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      switch (button) {
        case MOUSE_BUTTON_LEFT: {
          final PAreaI<SySpaceViewportType> window_start_box =
            SyWindowAbstract.this.box();

          final PVector2I<SySpaceViewportType> diff =
            PVectors2I.subtract(mouse_position_now, mouse_position_first);
          final PVector2I<SySpaceViewportType> current =
            PVectors2I.add(this.window_drag_start, diff);

          final PAreaI<SySpaceViewportType> window_new_box =
            PAreasI.moveAbsolute(
              window_start_box, current.x(), current.y());

          Postconditions.checkPostcondition(
            window_start_box.width() == window_new_box.width(),
            "Dragging a title bar must not resize width");
          Postconditions.checkPostcondition(
            window_start_box.height() == window_new_box.height(),
            "Dragging a title bar must not resize height");

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
      final PVector2I<SySpaceViewportType> mouse_position,
      final SyMouseButton button,
      final SyComponentType actual)
    {
      switch (button) {
        case MOUSE_BUTTON_LEFT: {
          this.window_drag_start =
            PVector2I.of(
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
      final PVector2I<SySpaceViewportType> mouse_position,
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
      final PVector2I<SySpaceViewportType> mouse_position,
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
      this.text.setText(Objects.requireNonNull(in_text, "Text"));
      SyWindowAbstract.this.recalculateBoundsRefresh(false);
    }

    @Override
    public void setIcon(final Optional<SyImageSpecification> in_icon)
    {
      this.icon.setIcon(in_icon);
    }

    @Override
    public SyPanelReadableType iconPanel()
    {
      return this.icon;
    }

    @Override
    public SyButtonReadableType closeButton()
    {
      return this.close_button;
    }

    @Override
    public SyLabelReadableType titleLabel()
    {
      return this.text;
    }

    @Override
    public Optional<SyThemePanel> theme()
    {
      final SyTheme theme = SyWindowAbstract.this.theme();
      return Optional.of(theme.windowTheme().titleBar().panelTheme());
    }
  }

  private final class TitleBarCloseButton extends IconButton implements
    SyWindowCloseButtonType
  {
    TitleBarCloseButton()
    {
      super(() -> {
        SyWindowAbstract.LOG.warn("refusing to detach close box");
        return false;
      });
    }

    @Override
    protected void buttonOnClick()
    {
      SyWindowAbstract.this.gui.windowClose(SyWindowAbstract.this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<SyThemeButtonRepeating> theme()
    {
      final SyTheme theme = SyWindowAbstract.this.theme();
      return Optional.of(theme.windowTheme().titleBar().buttonTheme());
    }

    @Override
    public String toString()
    {
      return this.toNamedString("TitleBarCloseButton");
    }
  }

  private final class TitleBarMaximizeButton extends IconButton implements
    SyWindowMaximizeButtonType
  {
    TitleBarMaximizeButton()
    {
      super(() -> {
        SyWindowAbstract.LOG.warn("refusing to detach maximize button");
        return false;
      });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<SyThemeButtonRepeating> theme()
    {
      final SyTheme theme = SyWindowAbstract.this.theme();
      return Optional.of(theme.windowTheme().titleBar().buttonTheme());
    }

    @Override
    public String toString()
    {
      return this.toNamedString("TitleBarMaximizeButton");
    }
  }

  private final class ContentPane extends SyPanelAbstract implements
    SyWindowContentPaneType
  {
    ContentPane()
    {
      super(() -> {
        SyWindowAbstract.LOG.warn("refusing to detach content pane");
        return false;
      });

      this.setPanelTransparent(true);
    }

    @Override
    public String toString()
    {
      return this.toNamedString("ContentPane");
    }

    @Override
    public Optional<SyThemePanel> theme()
    {
      return Optional.of(SyWindowAbstract.this.theme().panelTheme());
    }
  }

  private final class Frame extends SyPanelAbstract implements
    SyWindowFrameType
  {
    private PAreaI<SySpaceParentRelativeType> box_inner;

    Frame()
    {
      super(() -> {
        SyWindowAbstract.LOG.warn("refusing to detach frame");
        return false;
      });

      this.box_inner = PAreasI.create(0, 0, 0, 0);
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
      return PAreasI.containsPoint(this.box_inner, target_x, target_y);
    }

    @Override
    public String toString()
    {
      return this.toNamedString("Frame");
    }

    @Override
    public Optional<SyThemePanel> theme()
    {
      return Optional.of(SyWindowAbstract.this.theme().panelTheme());
    }
  }

  private final class WindowRoot extends SyPanelAbstract
  {
    private final TitleBar titlebar;
    private final Frame frame;
    private final ContentPane content_pane;

    WindowRoot(final String in_text)
    {
      super(() -> {
        SyWindowAbstract.LOG.warn("refusing to detach window root");
        return false;
      });

      this.setWindow(Optional.of(SyWindowAbstract.this));
      this.setSelectable(false);

      this.titlebar = new TitleBar(in_text);
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

    @Override
    public Optional<SyThemePanel> theme()
    {
      return Optional.of(SyWindowAbstract.this.theme().panelTheme());
    }
  }
}
