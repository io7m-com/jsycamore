package com.io7m.jsycamore.examples.lwjgl30;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jsycamore.Component.ParentResizeBehavior;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.components.AbstractContainer;
import com.io7m.jsycamore.components.ButtonLabelled;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jsycamore.windows.StandardWindow;
import com.io7m.jsycamore.windows.WindowParameters;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;

public final class SimpleButton implements Runnable
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
      Display.setTitle("SimpleButton");
      Display.setDisplayMode(new DisplayMode(
        SimpleButton.viewport_size.x,
        SimpleButton.viewport_size.y));
      Display.create();

      final SimpleButton sw = new SimpleButton();
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
  private final Window                window0;
  private final GUIContext            ctx;

  SimpleButton()
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
        SimpleButton.viewport_position,
        SimpleButton.viewport_size);
    this.ctx = this.gui.getContext();
    this.gl = this.ctx.contextGetGL();

    final WindowParameters wp = new WindowParameters();
    wp.setCanClose(false);
    wp.setCanResize(false);
    wp.setTitle("Window 0");

    this.window0 =
      new StandardWindow(
        this.ctx,
        new Point<ScreenRelative>(64, 64),
        new VectorI2I(300, 200),
        wp);
    this.window0.windowSetAlpha(0.98f);
    this.window0.windowSetMinimumHeight(this.ctx, 96);
    this.window0.windowSetMinimumWidth(this.ctx, 96);

    final AbstractContainer pane = this.window0.windowGetContentPane();
    final ButtonLabelled b =
      new ButtonLabelled(
        this.ctx,
        pane,
        new Point<ParentRelative>(8, 8),
        new VectorI2I(64, 32),
        "Hello");
    ComponentAlignment.setPositionContainerCenter(b);
    b.componentSetMinimumX(8);
    b.componentSetMinimumY(8);
    b.componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);
    b.componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_MOVE);

    this.gui.windowAdd(this.window0);
  }

  private void input()
    throws GUIException,
      ConstraintError
  {
    while (Keyboard.next()) {
      if (Keyboard.getEventKey() == Keyboard.KEY_W) {
        if (Keyboard.getEventKeyState() == false) {
          this.window0.windowSetWantOpen();
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
