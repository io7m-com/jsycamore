package com.io7m.jsycamore.tests;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.ApproximatelyEqualFloat;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.GUIException.ErrorCode;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Version;
import com.io7m.jtensors.VectorReadable3F;

public class ThemeTest
{
  private static boolean fequal(
    final float e,
    final float a)
  {
    return ApproximatelyEqualFloat.approximatelyEqual(e, a);
  }

  @Test(expected = GUIException.class) public
    void
    testBadVectorInvalidNumberFormat()
      throws GUIException,
        ConstraintError
  {
    final Properties p = new Properties();
    p.setProperty(
      "jsycamore.theme.version",
      Integer.toString(Version.THEME_VERSION));
    p.setProperty("jsycamore.theme.bounds_color", "x y z");

    try {
      Theme.loadThemeFromProperties(p);
    } catch (final GUIException e) {
      Assert.assertEquals(
        ErrorCode.GUI_THEME_VALUE_INCORRECT_TYPE,
        e.getErrorCode());
      throw e;
    }

    Assert.fail("unreachable");
  }

  @Test(expected = GUIException.class) public
    void
    testBadVectorInvalidNumberRange()
      throws GUIException,
        ConstraintError
  {
    final Properties p = new Properties();
    p.setProperty(
      "jsycamore.theme.version",
      Integer.toString(Version.THEME_VERSION));
    p.setProperty("jsycamore.theme.bounds_color", "2.0 0.0 0.0");

    try {
      Theme.loadThemeFromProperties(p);
    } catch (final GUIException e) {
      Assert.assertEquals(
        ErrorCode.GUI_THEME_VALUE_INCORRECT_TYPE,
        e.getErrorCode());
      throw e;
    }

    Assert.fail("unreachable");
  }

  @Test(expected = GUIException.class) public
    void
    testBadVectorInvalidUtterly()
      throws GUIException,
        ConstraintError
  {
    final Properties p = new Properties();
    p.setProperty(
      "jsycamore.theme.version",
      Integer.toString(Version.THEME_VERSION));
    p.setProperty("jsycamore.theme.bounds_color", "x");

    try {
      Theme.loadThemeFromProperties(p);
    } catch (final GUIException e) {
      Assert.assertEquals(
        ErrorCode.GUI_THEME_VALUE_INCORRECT_TYPE,
        e.getErrorCode());
      throw e;
    }

    Assert.fail("unreachable");
  }

  @Test public void testValidComplete()
    throws GUIException,
      ConstraintError
  {
    final Properties p = new Properties();
    p.setProperty(
      "jsycamore.theme.version",
      Integer.toString(Version.THEME_VERSION));
    p.setProperty("jsycamore.theme.bounds_color", "1.0 1.0 0.0");
    p.setProperty("jsycamore.theme.failsafe_color", "1.0 0.0 1.0");
    p.setProperty("jsycamore.theme.window.edge_width", "3");

    /**
     * Focused.
     */

    p.setProperty("jsycamore.theme.focused.window.edge_color", "1.0 1.0 1.0");
    p.setProperty(
      "jsycamore.theme.focused.component.active_background_color",
      "0.9 0.9 0.9");
    p.setProperty(
      "jsycamore.theme.focused.component.active_edge_color",
      "0.8 0.8 0.8");
    p.setProperty(
      "jsycamore.theme.focused.component.background_color",
      "0.2 0.2 0.2");
    p.setProperty(
      "jsycamore.theme.focused.component.edge_color",
      "0.8 0.8 0.8");
    p.setProperty(
      "jsycamore.theme.focused.component.disabled_background_color",
      "0.3 0.3 0.3");
    p.setProperty(
      "jsycamore.theme.focused.component.disabled_edge_color",
      "0.5 0.5 0.5");
    p.setProperty(
      "jsycamore.theme.focused.component.over_edge_color",
      "0.8 0.8 0.8");
    p.setProperty(
      "jsycamore.theme.focused.component.over_background_color",
      "0.5 0.5 0.5");
    p.setProperty(
      "jsycamore.theme.focused.text_area.foreground_color",
      "0.1 0.1 0.1");
    p.setProperty(
      "jsycamore.theme.focused.text_area.background_color",
      "0.7 0.7 0.7");
    p.setProperty(
      "jsycamore.theme.focused.window.titlebar.background_color",
      "0.8 0.0 0.0");
    p.setProperty(
      "jsycamore.theme.focused.window.titlebar.text_color",
      "1.0 1.0 1.0");

    /**
     * Unfocused.
     */

    p.setProperty(
      "jsycamore.theme.unfocused.window.edge_color",
      "0.8 0.8 0.8");
    p.setProperty(
      "jsycamore.theme.unfocused.component.background_color",
      "0.3 0.3 0.3");
    p.setProperty(
      "jsycamore.theme.unfocused.component.edge_color",
      "0.5 0.5 0.5");
    p.setProperty(
      "jsycamore.theme.unfocused.text_area.foreground_color",
      "0.5 0.5 0.5");
    p.setProperty(
      "jsycamore.theme.unfocused.text_area.background_color",
      "0.7 0.7 0.7");
    p.setProperty(
      "jsycamore.theme.unfocused.window.titlebar.background_color",
      "0.6 0.3 0.3");
    p.setProperty(
      "jsycamore.theme.unfocused.window.titlebar.text_color",
      "0.6 0.6 0.6");
    p.setProperty(
      "jsycamore.theme.unfocused.component.disabled_background_color",
      "0.3 0.3 0.3");
    p.setProperty(
      "jsycamore.theme.unfocused.component.disabled_edge_color",
      "0.5 0.5 0.5");

    final Theme t = Theme.loadThemeFromProperties(p);

    /**
     * Focused.
     */

    {
      final VectorReadable3F v = t.getBoundsColor();
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.0f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFailsafeColor();
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.0f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentActiveBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.9f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.9f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.9f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentActiveEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.2f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.2f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.2f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedTextAreaBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.7f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.7f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.7f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedTextAreaForegroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.1f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.1f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.1f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentOverEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentOverBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getZF()));
    }

    {
      final VectorReadable3F v =
        t.getFocusedComponentDisabledBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedComponentDisabledEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedWindowEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedWindowTitlebarBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.0f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.0f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getFocusedWindowTitlebarTextColor();
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(1.0f, v.getZF()));
    }

    /**
     * Unfocused.
     */

    {
      final VectorReadable3F v = t.getUnfocusedComponentBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getUnfocusedComponentEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getZF()));
    }

    {
      final VectorReadable3F v =
        t.getUnfocusedComponentDisabledBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getUnfocusedComponentDisabledEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getUnfocusedTextAreaBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.7f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.7f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.7f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getUnfocusedTextAreaForegroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.5f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getUnfocusedWindowEdgeColor();
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.8f, v.getZF()));
    }

    {
      final VectorReadable3F v =
        t.getUnfocusedWindowTitlebarBackgroundColor();
      Assert.assertTrue(ThemeTest.fequal(0.6f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.3f, v.getZF()));
    }

    {
      final VectorReadable3F v = t.getUnfocusedWindowTitlebarTextColor();
      Assert.assertTrue(ThemeTest.fequal(0.6f, v.getXF()));
      Assert.assertTrue(ThemeTest.fequal(0.6f, v.getYF()));
      Assert.assertTrue(ThemeTest.fequal(0.6f, v.getZF()));
    }

    {
      final int i = t.getWindowEdgeWidth();
      Assert.assertEquals(3, i);
    }
  }

  @Test(expected = GUIException.class) public void testVersionMissing()
    throws GUIException,
      ConstraintError
  {
    final Properties p = new Properties();

    try {
      Theme.loadThemeFromProperties(p);
    } catch (final GUIException e) {
      Assert.assertEquals(
        ErrorCode.GUI_THEME_VALUE_NOT_FOUND,
        e.getErrorCode());
      throw e;
    }

    Assert.fail("unreachable");
  }

  @Test(expected = GUIException.class) public void testVersionWrong()
    throws GUIException,
      ConstraintError
  {
    final Properties p = new Properties();
    p.setProperty(
      "jsycamore.theme.version",
      Integer.toString(Version.THEME_VERSION + 1));

    try {
      Theme.loadThemeFromProperties(p);
    } catch (final GUIException e) {
      Assert.assertEquals(ErrorCode.GUI_THEME_BAD_VERSION, e.getErrorCode());
      throw e;
    }

    Assert.fail("unreachable");
  }
}
