package com.io7m.jsycamore.examples.lwjgl30;

import javax.annotation.Nonnull;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.Component.ParentResizeBehavior;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Theme;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.components.AbstractContainer;
import com.io7m.jsycamore.components.ButtonLabelled;
import com.io7m.jsycamore.components.ButtonListener;
import com.io7m.jsycamore.components.ContainerThemed;
import com.io7m.jsycamore.components.TextArea;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jsycamore.windows.StandardWindow;
import com.io7m.jsycamore.windows.WindowParameters;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;

public final class SimpleThemes implements Runnable
{
  private static final Point<ScreenRelative> viewport_position;
  private static final VectorM2I             viewport_size;

  static {
    viewport_position = new Point<ScreenRelative>(0, 0);
    viewport_size = new VectorM2I(640, 480);
  }

  public static void main(
    final String args[])
  {
    try {
      Display.setTitle("SimpleThemes");
      Display.setDisplayMode(new DisplayMode(
        SimpleThemes.viewport_size.x,
        SimpleThemes.viewport_size.y));
      Display.create();

      final SimpleThemes sw = new SimpleThemes();
      sw.run();
    } catch (final LWJGLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    } catch (final GLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    } catch (final FilesystemError e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    } catch (final GUIException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    } catch (final ConstraintError e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final GUI                   gui;
  private final GLInterface           gl;
  private final Point<ScreenRelative> mouse_position;
  private final Window                example_window;
  private final Window                theme_window;
  private final GUIContext            ctx;
  private final FilesystemAPI         fs;

  SimpleThemes()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GUIException
  {
    this.mouse_position = new Point<ScreenRelative>();

    this.gui =
      SetupGUI.setupGUI(
        new PathReal("src/main"),
        "resources",
        SimpleThemes.viewport_position,
        SimpleThemes.viewport_size);
    this.ctx = this.gui.getContext();
    this.gl = this.ctx.contextGetGL();
    this.fs = this.ctx.contextGetFilesystem();

    final WindowParameters wp = new WindowParameters();
    wp.setCanClose(false);
    wp.setCanResize(false);
    wp.setTitle("Window 0");

    {
      /*
       * Component window.
       */

      this.example_window =
        new StandardWindow(
          this.ctx,
          new Point<ScreenRelative>(32, 32),
          new VectorI2I(280, 192),
          wp);
      this.example_window.windowSetAlpha(0.98f);
      this.example_window.windowSetMinimumHeight(this.ctx, 96);
      this.example_window.windowSetMinimumWidth(this.ctx, 96);

      final AbstractContainer pane =
        this.example_window.windowGetContentPane();

      final ContainerThemed container =
        new ContainerThemed(
          pane,
          PointConstants.PARENT_ORIGIN,
          new VectorI2I(156, 88));
      ComponentAlignment.setPositionContainerTopLeft(container, 8);

      final ButtonLabelled b0 =
        new ButtonLabelled(this.ctx, container, new Point<ParentRelative>(
          16,
          16), new VectorI2I(64, 32), "B0");
      ComponentAlignment.setPositionContainerTopLeft(b0, 8);

      final ButtonLabelled b1 =
        new ButtonLabelled(
          this.ctx,
          container,
          PointConstants.PARENT_ORIGIN,
          new VectorI2I(64, 32),
          "B1");
      ComponentAlignment.setPositionRelativeRightOfSameY(b1, 8, b0);

      final ButtonLabelled b2 =
        new ButtonLabelled(
          this.ctx,
          container,
          PointConstants.PARENT_ORIGIN,
          new VectorI2I(64, 32),
          "B2");
      ComponentAlignment.setPositionRelativeBelowSameX(b2, 8, b0);

      final ButtonLabelled b3 =
        new ButtonLabelled(
          this.ctx,
          container,
          PointConstants.PARENT_ORIGIN,
          new VectorI2I(64, 32),
          "B3");
      ComponentAlignment.setPositionRelativeRightOfSameY(b3, 8, b2);

      b0.componentSetEnabled(false);
      b1.componentSetEnabled(true);
      b2.componentSetEnabled(false);
      b3.componentSetEnabled(true);

      final ButtonLabelled toggle =
        new ButtonLabelled(
          this.ctx,
          pane,
          PointConstants.PARENT_ORIGIN,
          new VectorI2I(64, 32),
          "Disable");
      ComponentAlignment
        .setPositionRelativeRightOfSameY(toggle, 8, container);

      toggle.setButtonListener(new ButtonListener() {
        @SuppressWarnings("synthetic-access") @Override public
          void
          buttonListenerOnClick(
            final @Nonnull Component button)
            throws GUIException,
              ConstraintError
        {
          if (container.componentIsEnabled()) {
            container.componentSetEnabled(false);
            toggle.setText(SimpleThemes.this.ctx, "Enable");
          } else {
            container.componentSetEnabled(true);
            toggle.setText(SimpleThemes.this.ctx, "Disable");
          }
        }
      });

      final TextArea t =
        new TextArea(
          this.ctx,
          pane,
          new Point<ParentRelative>(8, 8),
          new VectorI2I(pane.componentGetWidth() - 16, 64));
      t.componentSetMinimumX(8);
      t.componentSetMinimumY(8);
      t
        .componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      t.componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
      t.textAreaAddLine(
        this.gui.getContext(),
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
      t.textAreaAddLine(this.ctx, "Nullam sed ultricies velit.");
      t.textAreaAddLine(
        this.ctx,
        "Aliquam ut risus metus, sit amet dignissim risus.");
      t.textAreaAddLine(
        this.ctx,
        "Nullam urna enim, mollis a dictum eget, pretium nec tortor.");

      ComponentAlignment.setPositionRelativeBelowSameX(t, 8, container);
    }

    {
      /*
       * Theme window.
       */

      wp.setCanClose(false);
      wp.setCanResize(false);
      wp.setTitle("Themes");

      this.theme_window =
        new StandardWindow(
          this.ctx,
          new Point<ScreenRelative>(8, 8),
          new VectorI2I(128, 128),
          wp);
      this.theme_window.windowSetAlpha(0.98f);

      final AbstractContainer container =
        this.theme_window.windowGetContentPane();

      final ButtonLabelled b0 =
        new ButtonLabelled(this.ctx, container, new Point<ParentRelative>(
          8,
          8), new VectorI2I(64, 32), "mars");
      ComponentAlignment.setPositionContainerTopLeft(b0, 8);

      b0.setButtonListener(new ButtonListener() {
        @SuppressWarnings("synthetic-access") @Override public
          void
          buttonListenerOnClick(
            final Component button)
            throws GUIException,
              ConstraintError
        {
          final Theme current = SimpleThemes.this.ctx.contextGetTheme();
          final Theme replace =
            Theme.loadThemeFromFilesystem(SimpleThemes.this.fs, "mars");
          Theme.copy(replace, current);
        }
      });

      final ButtonLabelled b1 =
        new ButtonLabelled(this.ctx, container, new Point<ParentRelative>(
          8,
          8), new VectorI2I(64, 32), "banana");

      b1.setButtonListener(new ButtonListener() {
        @SuppressWarnings("synthetic-access") @Override public
          void
          buttonListenerOnClick(
            final Component button)
            throws GUIException,
              ConstraintError
        {
          final Theme current = SimpleThemes.this.ctx.contextGetTheme();
          final Theme replace =
            Theme.loadThemeFromFilesystem(SimpleThemes.this.fs, "banana");
          Theme.copy(replace, current);
        }
      });

      ComponentAlignment.setPositionRelativeBelowSameX(b1, 8, b0);
    }

    this.gui.windowAdd(this.example_window);
    this.gui.windowAdd(this.theme_window);
  }

  private void input()
    throws GUIException,
      ConstraintError
  {
    while (Keyboard.next()) {
      if (Keyboard.getEventKey() == Keyboard.KEY_W) {
        if (Keyboard.getEventKeyState() == false) {
          this.example_window.windowSetWantOpen();
        }
      }
    }

    while (Mouse.next()) {
      this.mouse_position.setXI(Mouse.getEventX());
      this.mouse_position.setYI(Display.getHeight() - Mouse.getEventY());

      final int button = Mouse.getEventButton();
      if (button >= 0) {
        final boolean down = Mouse.getEventButtonState();
        if (down) {
          this.gui.mouseIsDown(this.mouse_position, button);
        } else {
          this.gui.mouseIsUp(this.mouse_position, button);
        }
      } else {
        this.gui.mouseMoved(this.mouse_position);
      }
    }
  }

  private void render()
    throws GLException,
      ConstraintError,
      GUIException
  {
    this.gl.colorBufferClear3f(0.25f, 0.25f, 0.25f);
    this.gl.blendingEnable(
      BlendFunction.BLEND_SOURCE_ALPHA,
      BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA);

    this.gui.render();
  }

  @Override public void run()
  {
    try {
      while (Display.isCloseRequested() == false) {
        this.input();
        this.render();
        Display.update();
        Display.sync(60);
      }
    } catch (final GLException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final ConstraintError e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final GUIException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
