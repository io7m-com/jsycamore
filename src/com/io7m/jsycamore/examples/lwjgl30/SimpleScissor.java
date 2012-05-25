package com.io7m.jsycamore.examples.lwjgl30;

import java.util.Properties;
import java.util.Stack;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.Scissor;
import com.io7m.jsycamore.ScissorStack;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScissorRelative;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;

public final class SimpleScissor implements Runnable
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
      Display.setTitle("SimpleQuads");
      Display.setDisplayMode(new DisplayMode(
        SimpleScissor.viewport_size.x,
        SimpleScissor.viewport_size.y));
      Display.create();

      final SimpleScissor sw = new SimpleScissor();
      sw.run();
    } catch (final LWJGLException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final GLException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final FilesystemError e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final GUIException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final ConstraintError e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final GUI              gui;
  private final Log              log;
  private final GLInterface      gl;
  private final Filesystem       fs;
  private final ScissorStack     scissor_stack;
  private final Stack<VectorI3F> color_stack;

  SimpleScissor()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GUIException
  {
    final Properties p = new Properties();
    p.put("com.io7m.jsycamore.level", "LOG_DEBUG");
    p.put("com.io7m.jsycamore.logs.example", "true");
    p.put("com.io7m.jsycamore.logs.example.filesystem", "false");

    this.log = new Log(p, "com.io7m.jsycamore", "example");
    this.gl = new GLInterfaceLWJGL30(this.log);

    this.fs = new Filesystem(this.log, new PathReal("."));
    this.fs.createDirectory("/sycamore");
    this.fs.mount("resources", "/sycamore");

    this.gui =
      new GUI(
        SimpleScissor.viewport_position,
        SimpleScissor.viewport_size,
        this.gl,
        this.fs,
        this.log);

    this.color_stack = new Stack<VectorI3F>();
    this.scissor_stack = new ScissorStack();

    {
      final VectorI2I size = new VectorI2I(300, 200);
      final Point<ScissorRelative> point = new Point<ScissorRelative>(32, 32);

      this.color_stack.push(new VectorI3F(1.0f, 0.0f, 0.0f));
      this.scissor_stack.push(new Scissor(point, size));
    }

    {
      final VectorI2I size = new VectorI2I(300, 200);
      final Point<ScissorRelative> point = new Point<ScissorRelative>(64, 64);

      this.color_stack.push(new VectorI3F(0.0f, 1.0f, 0.0f));
      this.scissor_stack.push(new Scissor(point, size));
    }

    {
      final VectorI2I size = new VectorI2I(150, 150);
      final Point<ScissorRelative> point = new Point<ScissorRelative>(80, 23);

      this.color_stack.push(new VectorI3F(1.0f, 1.0f, 0.0f));
      this.scissor_stack.push(new Scissor(point, size));
    }
  }

  private void render()
    throws GLException,
      ConstraintError
  {
    final GUIContext c = this.gui.getContext();

    this.gl.colorBufferClear3f(0.25f, 0.25f, 0.25f);
    this.gl.blendingEnable(
      BlendFunction.BLEND_SOURCE_ALPHA,
      BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA);

    final MatrixM4x4F mp = c.contextGetMatrixProjection();
    MatrixM4x4F.setIdentity(mp);
    ProjectionMatrix.makeOrthographic(mp, 0, 640, 480, 0, 1, 100);

    int index = 0;
    for (final Scissor s : this.scissor_stack) {
      final PointReadable<ScissorRelative> s_pos = s.getPosition();
      final VectorReadable2I s_siz = s.getSize();

      final VectorI3F color = this.color_stack.get(index);
      this.gl.scissorEnable(s_pos, s_siz);
      this.gl.colorBufferClearV3f(color);
      this.gl.scissorDisable();

      ++index;
    }
  }

  @Override public void run()
  {
    try {
      while (Display.isCloseRequested() == false) {
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
    }
  }
}
