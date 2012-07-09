package com.io7m.jsycamore.examples.lwjgl30;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jsycamore.DrawPrimitives;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIContext;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;

public final class SimpleQuads implements Runnable
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
        SimpleQuads.viewport_size.x,
        SimpleQuads.viewport_size.y));
      Display.create();

      final SimpleQuads sw = new SimpleQuads();
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
    } catch (final IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final GUI           gui;
  private final GLInterface   gl;
  private Texture2DRGBAStatic texture;
  private final GUIContext    ctx;
  private final FilesystemAPI fs;

  SimpleQuads()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GUIException,
      IOException
  {
    this.gui =
      SetupGUI.setupGUI(
        SimpleQuads.viewport_position,
        SimpleQuads.viewport_size);
    this.ctx = this.gui.getContext();
    this.gl = this.ctx.contextGetGL();
    this.fs = this.ctx.contextGetFilesystem();

    {
      final InputStream is = this.fs.openFile("/sycamore/images/wheat.png");
      final BufferedImage image = ImageIO.read(is);
      this.texture =
        Texture2DRGBAStatic.loadImage(
          "wheat",
          image,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          this.gl);
    }
  }

  private void render()
    throws GLException,
      ConstraintError
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

    draw.renderRectangleEdge(c, new VectorM2I(32, 32), 2, new VectorM4F(
      1.0f,
      0.0f,
      0.0f,
      1.0f));

    draw.renderRectangleTextured(
      c,
      new VectorM2I(128, 128),
      1.0f,
      this.texture);
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
