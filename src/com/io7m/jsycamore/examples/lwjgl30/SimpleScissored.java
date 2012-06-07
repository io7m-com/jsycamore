package com.io7m.jsycamore.examples.lwjgl30;

import java.util.Properties;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.components.ButtonLabelled;
import com.io7m.jsycamore.components.ContainerThemed;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jsycamore.windows.ContentPane;
import com.io7m.jsycamore.windows.StandardWindow;
import com.io7m.jsycamore.windows.WindowParameters;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;

public final class SimpleScissored implements Runnable
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
      Display.setTitle("SimpleScissored");
      Display.setDisplayMode(new DisplayMode(
        SimpleScissored.viewport_size.x,
        SimpleScissored.viewport_size.y));
      Display.create();

      final SimpleScissored sw = new SimpleScissored();
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
  private final Log                   log;
  private final GLInterface           gl;
  private final Filesystem            fs;
  private final Point<ScreenRelative> mouse_position;
  private final Window                window0;

  SimpleScissored()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GUIException
  {
    final Properties p = new Properties();
    p.put("com.io7m.jsycamore.level", "LOG_DEBUG");
    p.put("com.io7m.jsycamore.logs.example", "true");
    p.put("com.io7m.jsycamore.logs.example.filesystem", "false");
    p.put("com.io7m.jsycamore.logs.example.jsycamore.renderer", "false");

    this.log = new Log(p, "com.io7m.jsycamore", "example");
    this.gl = new GLInterfaceLWJGL30(this.log);

    this.fs = new Filesystem(this.log, new PathReal("."));
    this.fs.createDirectory("/sycamore");
    this.fs.mount("resources", "/sycamore");

    this.mouse_position = new Point<ScreenRelative>();
    this.gui =
      new GUI(
        SimpleScissored.viewport_position,
        SimpleScissored.viewport_size,
        this.gl,
        this.fs,
        this.log);
    final GUIContext ctx = this.gui.getContext();

    final WindowParameters wp = new WindowParameters();
    wp.setCanClose(false);
    wp.setCanResize(false);
    wp.setTitle("Window 0");

    this.window0 =
      new StandardWindow(
        ctx,
        new Point<ScreenRelative>(64, 64),
        new VectorI2I(300, 200),
        wp);
    this.window0.windowSetAlpha(0.98f);

    final ContentPane pane = this.window0.windowGetContentPane();

    final ContainerThemed container =
      new ContainerThemed(
        pane,
        new Point<ParentRelative>(8, 8),
        new VectorI2I(128, 128));
    container.setDrawEdge(true);

    for (int y = -16; y <= 112; y += 32) {
      for (int x = -16; x <= 112; x += 32) {
        final ButtonLabelled b =
          new ButtonLabelled(
            this.gui.getContext(),
            container,
            new Point<ParentRelative>(x, y),
            new VectorI2I(32, 32),
            "X");
        assert b != null;
      }
    }

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
