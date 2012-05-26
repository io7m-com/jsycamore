package com.io7m.jsycamore.windows;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

public final class WindowParameters
{
  private boolean         can_close;
  private boolean         can_resize;
  private int             minimum_width;
  private int             minimum_height;
  private @Nonnull String title;

  public WindowParameters()
  {
    this.can_close = true;
    this.can_resize = true;
    this.minimum_width = 64;
    this.minimum_height = 64;
    this.title = "Window";
  }

  public boolean getCanClose()
  {
    return this.can_close;
  }

  public boolean getCanResize()
  {
    return this.can_resize;
  }

  public int getMinimumHeight()
  {
    return this.minimum_height;
  }

  public int getMinimumWidth()
  {
    return this.minimum_width;
  }

  public @Nonnull String getTitle()
  {
    return this.title;
  }

  public void setCanClose(
    final boolean can)
  {
    this.can_close = can;
  }

  public void setCanResize(
    final boolean can)
  {
    this.can_resize = can;
  }

  public void setMinimumHeight(
    final int height)
    throws ConstraintError
  {
    this.minimum_height =
      Constraints.constrainRange(
        height,
        2,
        Integer.MAX_VALUE,
        "Maximum height");
  }

  public void setMinimumWidth(
    final int width)
    throws ConstraintError
  {
    this.minimum_width =
      Constraints
        .constrainRange(width, 2, Integer.MAX_VALUE, "Maximum width");
  }

  public void setTitle(
    final @Nonnull String title)
    throws ConstraintError
  {
    this.title = Constraints.constrainNotNull(title, "Title");
  }
}
