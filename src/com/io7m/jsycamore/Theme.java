package com.io7m.jsycamore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.PropertyUtils;
import com.io7m.jaux.PropertyUtils.ValueIncorrectType;
import com.io7m.jaux.PropertyUtils.ValueNotFound;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;

/**
 * Type specifying the basic color scheme used by windows and components.
 */

public final class Theme
{
  /**
   * Copy the theme referenced by <code>source</code> to the theme referenced
   * by <code>target</code>, replacing the contents entirely.
   * 
   * @param source
   *          The source theme.
   * @param target
   *          The target theme.
   * @throws ConstraintError
   *           Iff <code>source == null || target == null</code>.
   */

  public static void copy(
    final @Nonnull Theme source,
    final @Nonnull Theme target)
    throws ConstraintError
  {
    Constraints.constrainNotNull(source, "Source theme");
    Constraints.constrainNotNull(target, "Target theme");

    target.setBoundsColor(source.getBoundsColor());
    target.setFailsafeColor(source.getFailsafeColor());

    /**
     * Focused.
     */

    target.setFocusedComponentActiveBackgroundColor(source
      .getFocusedComponentActiveBackgroundColor());
    target.setFocusedComponentActiveEdgeColor(source
      .getFocusedComponentActiveEdgeColor());
    target.setFocusedComponentBackgroundColor(source
      .getFocusedComponentBackgroundColor());
    target.setFocusedComponentDisabledBackgroundColor(source
      .getFocusedComponentDisabledBackgroundColor());
    target.setFocusedComponentDisabledEdgeColor(source
      .getFocusedComponentDisabledEdgeColor());
    target
      .setFocusedComponentEdgeColor(source.getFocusedComponentEdgeColor());
    target.setFocusedComponentOverBackgroundColor(source
      .getFocusedComponentOverBackgroundColor());
    target.setFocusedComponentOverEdgeColor(source
      .getFocusedComponentOverEdgeColor());
    target.setFocusedTextAreaBackgroundColor(source
      .getFocusedTextAreaBackgroundColor());
    target.setFocusedTextAreaForegroundColor(source
      .getFocusedTextAreaForegroundColor());
    target.setFocusedWindowEdgeColor(source.getFocusedWindowEdgeColor());
    target.setFocusedWindowTitlebarBackgroundColor(source
      .getFocusedWindowTitlebarBackgroundColor());
    target.setFocusedWindowTitlebarTextColor(source
      .getFocusedWindowTitlebarTextColor());

    /**
     * Unfocused.
     */

    target.setUnfocusedComponentBackgroundColor(source
      .getUnfocusedComponentBackgroundColor());
    target.setUnfocusedComponentDisabledBackgroundColor(source
      .getUnfocusedComponentDisabledBackgroundColor());
    target.setUnfocusedComponentDisabledEdgeColor(source
      .getUnfocusedComponentDisabledEdgeColor());
    target.setUnfocusedComponentEdgeColor(source
      .getUnfocusedComponentEdgeColor());
    target.setUnfocusedTextAreaBackgroundColor(source
      .getUnfocusedTextAreaBackgroundColor());
    target.setUnfocusedTextAreaForegroundColor(source
      .getUnfocusedTextAreaForegroundColor());
    target.setUnfocusedWindowEdgeColor(source.getUnfocusedWindowEdgeColor());
    target.setUnfocusedWindowTitlebarBackgroundColor(source
      .getUnfocusedWindowTitlebarBackgroundColor());
    target.setUnfocusedWindowTitlebarTextColor(source
      .getUnfocusedWindowTitlebarTextColor());

    target.setWindowEdgeWidth(source.getWindowEdgeWidth());
  }

  /**
   * Load the theme named <code>name</code> from the filesystem referenced by
   * <code>fs</code>. The theme will be loaded from the file
   * <code>name.thm</code>, from the directory <code>/sycamore/themes</code>
   * in the filesystem.
   */

  public static @Nonnull Theme loadThemeFromFilesystem(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull String name)
    throws ConstraintError,
      GUIException
  {
    final StringBuilder path = new StringBuilder();
    path.append("/sycamore/themes/");
    path.append(name);
    path.append(".thm");

    InputStream theme_stream = null;
    try {
      theme_stream = fs.openFile(path.toString());
      final Properties props = new Properties();
      props.load(theme_stream);
      return Theme.loadThemeFromProperties(props);

    } catch (final FilesystemError e) {
      throw new GUIException(e);
    } catch (final IOException e) {
      throw new GUIException(e);
    } finally {
      try {
        if (theme_stream != null) {
          theme_stream.close();
        }
      } catch (final IOException e) {
        throw new GUIException(e);
      }
    }
  }

  /**
   * Load the theme contained within <code>p</code>.
   * 
   * @param p
   *          Properties containing a theme specification.
   * @throws ConstraintError
   *           Iff <code>p == null</code>.
   * @throws GUIException
   *           If a theme is in some way invalid.
   */

  public static @Nonnull Theme loadThemeFromProperties(
    final @Nonnull Properties p)
    throws ConstraintError,
      GUIException
  {
    try {
      Constraints.constrainNotNull(p, "Properties");

      final long v = PropertyUtils.getInteger(p, "jsycamore.theme.version");
      if (v != Version.THEME_VERSION) {
        throw GUIException.themeBadVersion(v, Version.THEME_VERSION);
      }

      final Theme t = new Theme();

      /**
       * Focused.
       */

      t.setBoundsColor(Theme.parseV3F(p, "bounds_color"));
      t.setFailsafeColor(Theme.parseV3F(p, "failsafe_color"));
      t.setFocusedComponentActiveBackgroundColor(Theme.parseV3F(
        p,
        "focused.component.active_background_color"));
      t.setFocusedComponentActiveEdgeColor(Theme.parseV3F(
        p,
        "focused.component.active_edge_color"));
      t.setFocusedComponentBackgroundColor(Theme.parseV3F(
        p,
        "focused.component.background_color"));
      t.setFocusedComponentEdgeColor(Theme.parseV3F(
        p,
        "focused.component.edge_color"));
      t.setFocusedComponentDisabledBackgroundColor(Theme.parseV3F(
        p,
        "focused.component.disabled_background_color"));
      t.setFocusedComponentDisabledEdgeColor(Theme.parseV3F(
        p,
        "focused.component.disabled_edge_color"));
      t.setFocusedWindowEdgeColor(Theme.parseV3F(
        p,
        "focused.window.edge_color"));
      t.setFocusedWindowTitlebarBackgroundColor(Theme.parseV3F(
        p,
        "focused.window.titlebar.background_color"));
      t.setFocusedWindowTitlebarTextColor(Theme.parseV3F(
        p,
        "focused.window.titlebar.text_color"));
      t.setFocusedComponentOverBackgroundColor(Theme.parseV3F(
        p,
        "focused.component.over_background_color"));
      t.setFocusedComponentOverEdgeColor(Theme.parseV3F(
        p,
        "focused.component.over_edge_color"));
      t.setFocusedTextAreaForegroundColor(Theme.parseV3F(
        p,
        "focused.text_area.foreground_color"));
      t.setFocusedTextAreaBackgroundColor(Theme.parseV3F(
        p,
        "focused.text_area.background_color"));
      t.setFocusedComponentEdgeColor(Theme.parseV3F(
        p,
        "focused.component.edge_color"));

      /**
       * Unfocused.
       */

      t.setUnfocusedComponentBackgroundColor(Theme.parseV3F(
        p,
        "unfocused.component.background_color"));
      t.setUnfocusedComponentEdgeColor(Theme.parseV3F(
        p,
        "unfocused.component.edge_color"));
      t.setUnfocusedComponentDisabledBackgroundColor(Theme.parseV3F(
        p,
        "unfocused.component.disabled_background_color"));
      t.setUnfocusedComponentDisabledEdgeColor(Theme.parseV3F(
        p,
        "unfocused.component.disabled_edge_color"));
      t.setUnfocusedWindowEdgeColor(Theme.parseV3F(
        p,
        "unfocused.window.edge_color"));
      t.setUnfocusedWindowTitlebarBackgroundColor(Theme.parseV3F(
        p,
        "unfocused.window.titlebar.background_color"));
      t.setUnfocusedWindowTitlebarTextColor(Theme.parseV3F(
        p,
        "unfocused.window.titlebar.text_color"));
      t.setUnfocusedTextAreaForegroundColor(Theme.parseV3F(
        p,
        "unfocused.text_area.foreground_color"));
      t.setUnfocusedTextAreaBackgroundColor(Theme.parseV3F(
        p,
        "unfocused.text_area.background_color"));
      t.setUnfocusedComponentEdgeColor(Theme.parseV3F(
        p,
        "unfocused.component.edge_color"));

      t.setWindowEdgeWidth((int) Theme.parseInt(p, "window.edge_width"));

      return t;
    } catch (final ValueNotFound e) {
      throw new GUIException(e);
    } catch (final ValueIncorrectType e) {
      throw new GUIException(e);
    }
  }

  private static long parseInt(
    final @Nonnull Properties p,
    final @Nonnull String key)
    throws ValueNotFound,
      ConstraintError,
      ValueIncorrectType
  {
    final StringBuilder b = new StringBuilder();
    b.append("jsycamore.theme.");
    b.append(key);
    return Theme.parseIntActual(p, b.toString());
  }

  private static long parseIntActual(
    final @Nonnull Properties p,
    final @Nonnull String key)
    throws ValueNotFound,
      ConstraintError,
      ValueIncorrectType
  {
    return PropertyUtils.getInteger(p, key);
  }

  private static float parseNormalReal(
    final @Nonnull String key,
    final @Nonnull String value)
    throws GUIException
  {
    final float r = Float.parseFloat(value);
    if ((r < 0.0) || (r > 1.0)) {
      throw GUIException.themeFloatNormalRange(key, r);
    }
    return r;
  }

  private static VectorReadable3F parseV3F(
    final @Nonnull Properties p,
    final @Nonnull String key)
    throws GUIException,
      ValueNotFound,
      ConstraintError,
      ValueIncorrectType
  {
    final StringBuilder b = new StringBuilder();
    b.append("jsycamore.theme.");
    b.append(key);
    return Theme.parseV3FActual(p, b.toString());
  }

  private static VectorReadable3F parseV3FActual(
    final @Nonnull Properties p,
    final @Nonnull String key)
    throws ValueNotFound,
      ConstraintError,
      ValueIncorrectType,
      GUIException
  {
    final String value = PropertyUtils.getString(p, key);
    final String[] components = value.split("\\s+");

    if (components.length != 3) {
      throw new ValueIncorrectType(
        key,
        "a three element vector of real values");
    }

    try {
      final float r = Theme.parseNormalReal(key, components[0]);
      final float g = Theme.parseNormalReal(key, components[1]);
      final float b = Theme.parseNormalReal(key, components[2]);
      return new VectorI3F(r, g, b);
    } catch (final NumberFormatException e) {
      throw GUIException.themeWanted3F(key, value);
    }
  }

  private int                      window_edge_width;
  private final @Nonnull VectorM3F focused_window_edge_color;
  private final @Nonnull VectorM3F focused_window_titlebar_background_color;
  private final @Nonnull VectorM3F focused_window_titlebar_text_color;
  private final @Nonnull VectorM3F focused_component_edge_color;
  private final @Nonnull VectorM3F focused_component_background_color;
  private final @Nonnull VectorM3F focused_component_over_background_color;

  private final @Nonnull VectorM3F focused_component_over_edge_color;
  private final @Nonnull VectorM3F focused_component_active_background_color;
  private final @Nonnull VectorM3F focused_component_active_edge_color;
  private final @Nonnull VectorM3F focused_component_disabled_background_color;
  private final @Nonnull VectorM3F focused_component_disabled_edge_color;
  private final @Nonnull VectorM3F focused_text_area_background_color;
  private final @Nonnull VectorM3F focused_text_area_foreground_color;
  private final @Nonnull VectorM3F unfocused_window_edge_color;
  private final @Nonnull VectorM3F unfocused_window_titlebar_background_color;

  private final @Nonnull VectorM3F unfocused_window_titlebar_text_color;
  private final @Nonnull VectorM3F unfocused_component_edge_color;
  private final @Nonnull VectorM3F unfocused_component_background_color;
  private final @Nonnull VectorM3F unfocused_component_disabled_edge_color;
  private final @Nonnull VectorM3F unfocused_component_disabled_background_color;
  private final @Nonnull VectorM3F unfocused_text_area_background_color;

  private final @Nonnull VectorM3F unfocused_text_area_foreground_color;
  private final @Nonnull VectorM3F failsafe_color;

  private final @Nonnull VectorM3F bounds_color;

  Theme()
  {
    this.window_edge_width = 1;

    this.focused_window_edge_color = new VectorM3F();
    this.focused_window_titlebar_background_color = new VectorM3F();
    this.focused_window_titlebar_text_color = new VectorM3F();
    this.focused_component_background_color = new VectorM3F();
    this.focused_component_edge_color = new VectorM3F();
    this.focused_component_over_background_color = new VectorM3F();
    this.focused_component_over_edge_color = new VectorM3F();
    this.focused_component_active_background_color = new VectorM3F();
    this.focused_component_active_edge_color = new VectorM3F();
    this.focused_component_disabled_background_color = new VectorM3F();
    this.focused_component_disabled_edge_color = new VectorM3F();
    this.focused_text_area_background_color = new VectorM3F();
    this.focused_text_area_foreground_color = new VectorM3F();

    this.unfocused_window_edge_color = new VectorM3F();
    this.unfocused_window_titlebar_background_color = new VectorM3F();
    this.unfocused_window_titlebar_text_color = new VectorM3F();
    this.unfocused_component_background_color = new VectorM3F();
    this.unfocused_component_edge_color = new VectorM3F();
    this.unfocused_component_disabled_background_color = new VectorM3F();
    this.unfocused_component_disabled_edge_color = new VectorM3F();
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

  public @Nonnull
    VectorReadable3F
    getFocusedComponentDisabledBackgroundColor()
  {
    return this.focused_component_disabled_background_color;
  }

  public @Nonnull VectorReadable3F getFocusedComponentDisabledEdgeColor()
  {
    return this.focused_component_disabled_edge_color;
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

  public @Nonnull
    VectorReadable3F
    getUnfocusedComponentDisabledBackgroundColor()
  {
    return this.unfocused_component_disabled_background_color;
  }

  public @Nonnull VectorReadable3F getUnfocusedComponentDisabledEdgeColor()
  {
    return this.unfocused_component_disabled_edge_color;
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

  public void setFocusedComponentDisabledBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_disabled_background_color);
  }

  public void setFocusedComponentDisabledBackgroundColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_disabled_background_color.x = r;
    this.focused_component_disabled_background_color.y = g;
    this.focused_component_disabled_background_color.z = b;
  }

  public void setFocusedComponentDisabledEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_component_disabled_edge_color);
  }

  public void setFocusedComponentDisabledEdgeColor3f(
    final float r,
    final float g,
    final float b)
  {
    this.focused_component_disabled_edge_color.x = r;
    this.focused_component_disabled_edge_color.y = g;
    this.focused_component_disabled_edge_color.z = b;
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

  public void setFocusedTextAreaBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_text_area_background_color);
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

  public void setFocusedTextAreaForegroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.focused_text_area_foreground_color);
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

  public void setUnfocusedComponentDisabledBackgroundColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_component_disabled_background_color);
  }

  public void setUnfocusedComponentDisabledEdgeColor(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError
  {
    Constraints.constrainNotNull(color, "Color");
    VectorM3F.copy(color, this.unfocused_component_disabled_edge_color);
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
