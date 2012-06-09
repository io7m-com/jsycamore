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
import com.io7m.jsycamore.Component.ParentResizeBehavior;
import com.io7m.jsycamore.ComponentAlignment;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Window;
import com.io7m.jsycamore.components.ButtonLabelled;
import com.io7m.jsycamore.components.ContainerThemed;
import com.io7m.jsycamore.components.Scrollable;
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

public final class SimpleScrollable implements Runnable
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
      Display.setTitle("SimpleScrollable");
      Display.setDisplayMode(new DisplayMode(
        SimpleScrollable.viewport_size.x,
        SimpleScrollable.viewport_size.y));
      Display.create();

      final SimpleScrollable sw = new SimpleScrollable();
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

  SimpleScrollable()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GUIException
  {
    final Properties p = new Properties();
    p.put("com.io7m.jsycamore.level", "LOG_DEBUG");
    p.put("com.io7m.jsycamore.logs.example", "true");
    p.put("com.io7m.jsycamore.logs.example.gl30", "false");
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
        SimpleScrollable.viewport_position,
        SimpleScrollable.viewport_size,
        this.gl,
        this.fs,
        this.log);
    final GUIContext ctx = this.gui.getContext();

    final WindowParameters wp = new WindowParameters();
    wp.setCanClose(false);
    wp.setCanResize(true);
    wp.setTitle("Window 0");

    this.window0 =
      new StandardWindow(
        ctx,
        new Point<ScreenRelative>(64, 64),
        new VectorI2I(500, 300),
        wp);
    this.window0.windowSetAlpha(0.98f);
    this.window0.windowSetMinimumHeight(ctx, 96);
    this.window0.windowSetMinimumWidth(ctx, 96);

    final ContentPane pane = this.window0.windowGetContentPane();

    final ContainerThemed container =
      new ContainerThemed(new Point<ParentRelative>(8, 8), new VectorM2I(
        256,
        256));
    container.setDrawEdge(true);

    final Scrollable s = new Scrollable(ctx, pane, container);
    s.componentSetHeightResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    s.componentSetWidthResizeBehavior(ParentResizeBehavior.BEHAVIOR_RESIZE);
    s.componentSetMinimumWidth(ctx, 64);
    s.componentSetMinimumHeight(ctx, 64);

    final ButtonLabelled b0 =
      new ButtonLabelled(ctx, new Point<ParentRelative>(8, 8), new VectorI2I(
        64,
        32), "b0");
    b0.componentAttachToParent(container);

    final ButtonLabelled b1 =
      new ButtonLabelled(ctx, new Point<ParentRelative>(8, 8), new VectorI2I(
        32,
        32), "b1");
    b1.componentAttachToParent(container);
    ComponentAlignment.setPositionRelativeRightOfSameY(b1, 8, b0);

    final ButtonLabelled b2 =
      new ButtonLabelled(ctx, new Point<ParentRelative>(8, 8), new VectorI2I(
        32,
        64), "b2");
    b2.componentAttachToParent(container);
    ComponentAlignment.setPositionRelativeBelowSameX(b2, 8, b0);

    final ButtonLabelled b3 =
      new ButtonLabelled(ctx, new Point<ParentRelative>(8, 8), new VectorI2I(
        32,
        32), "b3");
    b3.componentAttachToParent(container);
    ComponentAlignment.setPositionRelativeBelowSameX(b3, 8, b1);

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
