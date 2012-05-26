package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3F;

/**
 * Type specifying the basic color scheme used by windows and components.
 */

public final class Theme
{
  private int                      window_edge_width;

  private final @Nonnull VectorM3F focused_window_edge_color;
  private final @Nonnull VectorM3F focused_window_pane_background_color;
  private final @Nonnull VectorM3F focused_window_titlebar_background_color;
  private final @Nonnull VectorM3F focused_window_titlebar_text_color;
  private final @Nonnull VectorM3F focused_component_edge_color;
  private final @Nonnull VectorM3F focused_component_background_color;
  private final @Nonnull VectorM3F focused_component_over_background_color;
  private final @Nonnull VectorM3F focused_component_over_edge_color;
  private final @Nonnull VectorM3F focused_component_active_background_color;
  private final @Nonnull VectorM3F focused_component_active_edge_color;
  private final @Nonnull VectorM3F focused_text_area_background_color;
  private final @Nonnull VectorM3F focused_text_area_foreground_color;

  private final @Nonnull VectorM3F unfocused_window_edge_color;
  private final @Nonnull VectorM3F unfocused_window_pane_background_color;
  private final @Nonnull VectorM3F unfocused_window_titlebar_background_color;
  private final @Nonnull VectorM3F unfocused_window_titlebar_text_color;
  private final @Nonnull VectorM3F unfocused_component_edge_color;
  private final @Nonnull VectorM3F unfocused_component_background_color;
  private final @Nonnull VectorM3F unfocused_text_area_background_color;
  private final @Nonnull VectorM3F unfocused_text_area_foreground_color;

  private final @Nonnull VectorM3F failsafe_color;
  private final @Nonnull VectorM3F bounds_color;

  Theme()
  {
    this.window_edge_width = 1;

    this.focused_window_edge_color = new VectorM3F();
    this.focused_window_pane_background_color = new VectorM3F();
    this.focused_window_titlebar_background_color = new VectorM3F();
    this.focused_window_titlebar_text_color = new VectorM3F();
    this.focused_component_background_color = new VectorM3F();
    this.focused_component_edge_color = new VectorM3F();
    this.focused_component_over_background_color = new VectorM3F();
    this.focused_component_over_edge_color = new VectorM3F();
    this.focused_component_active_background_color = new VectorM3F();
    this.focused_component_active_edge_color = new VectorM3F();
    this.focused_text_area_background_color = new VectorM3F();
    this.focused_text_area_foreground_color = new VectorM3F();

    this.unfocused_window_edge_color = new VectorM3F();
    this.unfocused_window_pane_background_color = new VectorM3F();
    this.unfocused_window_titlebar_background_color = new VectorM3F();
    this.unfocused_window_titlebar_text_color = new VectorM3F();
    this.unfocused_component_background_color = new VectorM3F();
    this.unfocused_component_edge_color = new VectorM3F();
    this.unfocused_text_area_background_color = new VectorM3F();
    this.unfocused_text_area_foreground_color = new VectorM3F();

    this.failsafe_color = new VectorM3F();
    this.bounds_color = new VectorM3F();
  }

  public @Nonnull VectorReadable3F getBoundsColor()
  {
    return this.bounds_color;
  }

  public @Nonnull VectorReadable3F getFailsafeColor()
  {
    return this.failsafe_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentActiveBackgroundColor()
  {
    return this.focused_component_active_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentActiveEdgeColor()
  {
    return this.focused_component_active_edge_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentBackgroundColor()
  {
    return this.focused_component_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentEdgeColor()
  {
    return this.focused_component_edge_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentOverBackgroundColor()
  {
    return this.focused_component_over_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentOverEdgeColor()
  {
    return this.focused_component_over_edge_color;
  }

  public @Nonnull VectorReadable3F getFocusedTextAreaBackgroundColor()
  {
    return this.focused_text_area_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedTextAreaForegroundColor()
  {
    return this.focused_text_area_foreground_color;
  }

  public @Nonnull VectorReadable3F getFocusedWindowEdgeColor()
  {
    return this.focused_window_edge_color;
  }

  public @Nonnull VectorReadable3F getFocusedWindowPaneBackgroundColor()
  {
    return this.focused_window_pane_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedWindowTitlebarBackgroundColor()
  {
    return this.focused_window_titlebar_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedWindowTitlebarTextColor()
  {
    return this.focused_window_titlebar_text_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedComponentBackgroundColor()
  {
    return this.unfocused_component_background_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedComponentEdgeColor()
  {
    return this.unfocused_component_edge_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedTextAreaBackgroundColor()
  {
    return this.unfocused_text_area_background_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedTextAreaForegroundColor()
  {
    return this.unfocused_text_area_foreground_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedWindowEdgeColor()
  {
    return this.unfocused_window_edge_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedWindowPaneBackgroundColor()
  {
    return this.unfocused_window_pane_background_color;
  }

  public @Nonnull
    VectorReadable3F
    getUnfocusedWindowTitlebarBackgroundColor()
  {
    return this.unfocused_window_titlebar_background_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedWindowTitlebarTextColor()
  {
    return this.unfocused_window_titlebar_text_color;
  }

  public int getWindowEdgeWidth()
  {
    return this.window_edge_width;
  }

  public void setBoundsColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.bounds_color);
  }

  public void setBoundsColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.bounds_color.x = r;
    this.bounds_color.y = g;
    this.bounds_color.z = b;
  }

  public void setFailsafeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.failsafe_color);
  }

  public void setFailsafeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.failsafe_color.x = r;
    this.failsafe_color.y = g;
    this.failsafe_color.z = b;
  }

  public void setFocusedComponentActiveBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_active_background_color);
  }

  public void setFocusedComponentActiveBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_active_background_color.x = r;
    this.focused_component_active_background_color.y = g;
    this.focused_component_active_background_color.z = b;
  }

  public void setFocusedComponentActiveEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_active_edge_color);
  }

  public void setFocusedComponentActiveEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_active_edge_color.x = r;
    this.focused_component_active_edge_color.y = g;
    this.focused_component_active_edge_color.z = b;
  }

  public void setFocusedComponentBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_background_color);
  }

  public void setFocusedComponentBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_background_color.x = r;
    this.focused_component_background_color.y = g;
    this.focused_component_background_color.z = b;
  }

  public void setFocusedComponentEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_edge_color);
  }

  public void setFocusedComponentEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_edge_color.x = r;
    this.focused_component_edge_color.y = g;
    this.focused_component_edge_color.z = b;
  }

  public void setFocusedComponentOverBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_over_background_color);
  }

  public void setFocusedComponentOverBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_over_background_color.x = r;
    this.focused_component_over_background_color.y = g;
    this.focused_component_over_background_color.z = b;
  }

  public void setFocusedComponentOverEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_over_edge_color);
  }

  public void setFocusedComponentOverEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_over_edge_color.x = r;
    this.focused_component_over_edge_color.y = g;
    this.focused_component_over_edge_color.z = b;
  }

  public void setFocusedTextAreaBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_text_area_background_color.x = r;
    this.focused_text_area_background_color.y = g;
    this.focused_text_area_background_color.z = b;
  }

  public void setFocusedTextAreaBackgroundColor3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_text_area_background_color);
  }

  public void setFocusedTextAreaForegroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_text_area_foreground_color.x = r;
    this.focused_text_area_foreground_color.y = g;
    this.focused_text_area_foreground_color.z = b;
  }

  public void setFocusedTextAreaForegroundColor3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_text_area_foreground_color);
  }

  public void setFocusedWindowEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_window_edge_color);
  }

  public void setFocusedWindowEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_window_edge_color.x = r;
    this.focused_window_edge_color.y = g;
    this.focused_window_edge_color.z = b;
  }

  public void setFocusedWindowPaneBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_window_pane_background_color);
  }

  public void setFocusedWindowPaneBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_window_pane_background_color.x = r;
    this.focused_window_pane_background_color.y = g;
    this.focused_window_pane_background_color.z = b;
  }

  public void setFocusedWindowTitlebarBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_window_titlebar_background_color);
  }

  public void setFocusedWindowTitlebarBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_window_titlebar_background_color.x = r;
    this.focused_window_titlebar_background_color.y = g;
    this.focused_window_titlebar_background_color.z = b;
  }

  public void setFocusedWindowTitlebarTextColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_window_titlebar_text_color);
  }

  public void setFocusedWindowTitlebarTextColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_window_titlebar_text_color.x = r;
    this.focused_window_titlebar_text_color.y = g;
    this.focused_window_titlebar_text_color.z = b;
  }

  public void setUnfocusedComponentBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_component_background_color);
  }

  public void setUnfocusedComponentBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_component_background_color.x = r;
    this.unfocused_component_background_color.y = g;
    this.unfocused_component_background_color.z = b;
  }

  public void setUnfocusedComponentEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_component_edge_color);
  }

  public void setUnfocusedComponentEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_component_edge_color.x = r;
    this.unfocused_component_edge_color.y = g;
    this.unfocused_component_edge_color.z = b;
  }

  public void setUnfocusedTextAreaBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_text_area_background_color);
  }

  public void setUnfocusedTextAreaBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_text_area_background_color.x = r;
    this.unfocused_text_area_background_color.y = g;
    this.unfocused_text_area_background_color.z = b;
  }

  public void setUnfocusedTextAreaForegroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_text_area_foreground_color);
  }

  public void setUnfocusedTextAreaForegroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_text_area_foreground_color.x = r;
    this.unfocused_text_area_foreground_color.y = g;
    this.unfocused_text_area_foreground_color.z = b;
  }

  public void setUnfocusedWindowEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_window_edge_color);
  }

  public void setUnfocusedWindowEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_window_edge_color.x = r;
    this.unfocused_window_edge_color.y = g;
    this.unfocused_window_edge_color.z = b;
  }

  public void setUnfocusedWindowPaneBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_window_pane_background_color);
  }

  public void setUnfocusedWindowPaneBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_window_pane_background_color.x = r;
    this.unfocused_window_pane_background_color.y = g;
    this.unfocused_window_pane_background_color.z = b;
  }

  public void setUnfocusedWindowTitlebarBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_window_titlebar_background_color);
  }

  public void setUnfocusedWindowTitlebarBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_window_titlebar_background_color.x = r;
    this.unfocused_window_titlebar_background_color.y = g;
    this.unfocused_window_titlebar_background_color.z = b;
  }

  public void setUnfocusedWindowTitlebarTextColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_window_titlebar_text_color);
  }

  public void setUnfocusedWindowTitlebarTextColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.unfocused_window_titlebar_text_color.x = r;
    this.unfocused_window_titlebar_text_color.y = g;
    this.unfocused_window_titlebar_text_color.z = b;
  }

  public void setWindowEdgeWidth(
    final int width)
  {
    this.window_edge_width = width;
  }
}
