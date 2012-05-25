package com.io7m.jsycamore.windows;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.Component.ParentResizeBehavior;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;

public final class StandardWindow extends Window
{
  private final @Nonnull ContentPane    content_pane;
  private final @Nonnull Titlebar       titlebar;
  private final @CheckForNull ResizeBox resize_box;

  public StandardWindow(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position,
    final @Nonnull VectorReadable2I size,
    final @Nonnull String title,
    final boolean can_close,
    final boolean can_resize)
    throws ConstraintError,
      GUIException
  {
    super(context, position, size);

    this.windowSetMinimumWidth(64);
    this.windowSetMinimumHeight(64);

    final Component root = this.windowGetRootPane();
    final VectorReadable2I root_size = root.componentGetSize();

    {
      final VectorM2I new_size = new VectorM2I();
      new_size.x = root_size.getXI();
      new_size.y = 16;

      this.titlebar =
        new Titlebar(
          context,
          root,
          PointConstants.PARENT_ORIGIN,
          new_size,
          title,
          can_close);
      this.titlebar
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    }

    {
      final VectorM2I new_size = new VectorM2I();
      new_size.x = root_size.getXI();
      new_size.y =
        (root_size.getYI() - this.titlebar.componentGetSize().getYI()) + 1;

      this.content_pane =
        new ContentPane(root, PointConstants.PARENT_ORIGIN, new_size);

      ComponentAlignment.setPositionRelativeBelowSameX(
        this.content_pane,
        -1,
        this.titlebar);
      this.content_pane
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      this.content_pane
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    }

    if (can_resize) {
      this.resize_box =
        new ResizeBox(
          context,
          this.content_pane,
          PointConstants.PARENT_ORIGIN);
      this.resize_box
        .componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
      this.resize_box
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);

      ComponentAlignment.setPositionContainerBottomRight(this.resize_box, 4);
    } else {
      this.resize_box = null;
    }
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[StandardWindow ");
    builder.append(this.windowGetID());
    builder.append("]");
    return builder.toString();
  }

  @Override public @Nonnull ContentPane windowGetContentPane()
  {
    return this.content_pane;
  }
}
