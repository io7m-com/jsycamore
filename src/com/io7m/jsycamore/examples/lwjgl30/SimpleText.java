package com.io7m.jsycamore.examples.lwjgl30;

import java.util.ArrayList;
import java.util.Properties;

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
import com.io7m.jpismo.CompiledText;
import com.io7m.jpismo.TextCacheException;
import com.io7m.jpismo.TextRenderer;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;

public final class SimpleText implements Runnable
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
      Display.setTitle("SimpleText");
      Display.setDisplayMode(new DisplayMode(
        SimpleText.viewport_size.x,
        SimpleText.viewport_size.y));
      Display.create();

      final SimpleText sw = new SimpleText();
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

  private final GUI         gui;
  private final Log         log;
  private final GLInterface gl;
  private final Filesystem  fs;

  SimpleText()
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
        SimpleText.viewport_position,
        SimpleText.viewport_size,
        this.gl,
        this.fs,
        this.log);
  }

  private void render()
    throws GLException,
      ConstraintError,
      TextCacheException
  {
    final GUIContext c = this.gui.getContext();
    final DrawPrimitives draw = c.contextGetDrawPrimitives();

    this.gl.colorBufferClear3f(0.25f, 0.25f, 0.25f);
    this.gl.blendingEnable(
      BlendFunction.BLEND_SOURCE_ALPHA,
      BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA);

    final MatrixM4x4F mv = c.contextGetMatrixModelview();
    final MatrixM4x4F mp = c.contextGetMatrixProjection();

    MatrixM4x4F.setIdentity(mp);
    ProjectionMatrix.makeOrthographic(mp, 0, 640, 480, 0, 1, 100);
    MatrixM4x4F.setIdentity(mv);

    final TextRenderer tr = c.contextGetTextRendererMedium();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add("Text");
    final CompiledText compiled_text = tr.textCompile(lines);
    tr.textCacheUpload();

    final VectorReadable4F color = new VectorM4F(0.0f, 0.0f, 1.0f, 1.0f);
    draw.renderCompiledText(c, compiled_text, color);
    compiled_text.resourceDelete(this.gl);
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
    } catch (final TextCacheException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
