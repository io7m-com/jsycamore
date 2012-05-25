package com.io7m.jsycamore;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;
import com.io7m.jpismo.TextRenderer;
import com.io7m.jpismo.TextRendererTrivial;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * The current GUI context.
 * 
 * The <code>GUIContext</code> contains all external GUI state such as loaded
 * fonts and data, shaders, OpenGL rendering interface, logging interface,
 * current theme, etc.
 */

public final class GUIContext
{
  private final @Nonnull GUIShader             shader_textured;
  private final @Nonnull GUIShader             shader_flat;
  private final @Nonnull GUIShader             shader_text;
  private @CheckForNull GUIShader              shader_current;

  private final @Nonnull GLInterface           gl;
  private final @Nonnull FilesystemAPI         filesystem;
  private final @Nonnull Log                   log;
  private final @Nonnull Log                   rlog;
  private final @Nonnull Log                   clog;
  private final @Nonnull MatrixM4x4FStack      matrix_projection_stack;
  private final @Nonnull MatrixM4x4FStack      matrix_modelview_stack;
  private final @Nonnull ScissorStack          scissor_stack;
  private final @Nonnull Theme                 theme;
  private final @Nonnull Font                  font_medium;
  private final @Nonnull TextRendererTrivial   text_renderer_medium;
  private final @Nonnull DrawPrimitives        draw_primitives;

  private final @Nonnull Point<ScreenRelative> viewport_position;
  private final @Nonnull VectorM2I             viewport_size;

  public GUIContext(
    final @Nonnull GLInterface gl,
    final @Nonnull FilesystemAPI fs,
    final @Nonnull Log base_log,
    final @Nonnull Point<ScreenRelative> viewport_position,
    final @Nonnull VectorReadable2I viewport_size)
    throws ConstraintError,
      GLCompileException,
      GLException,
      FontFormatException,
      IOException,
      FilesystemError
  {
    this.gl = Constraints.constrainNotNull(gl, "OpenGL interface");
    this.filesystem =
      Constraints.constrainNotNull(fs, "Filesystem interface");
    this.log = Constraints.constrainNotNull(base_log, "Log interface");

    this.viewport_position =
      new Point<ScreenRelative>(Constraints.constrainNotNull(
        viewport_position,
        "Viewport position"));
    this.viewport_size =
      new VectorM2I(Constraints.constrainNotNull(
        viewport_size,
        "Viewport size"));

    this.rlog = new Log(this.log, "renderer");
    this.clog = new Log(this.log, "component");

    this.matrix_projection_stack = new MatrixM4x4FStack();
    this.matrix_projection_stack.push(new MatrixM4x4F());
    this.matrix_modelview_stack = new MatrixM4x4FStack();
    this.matrix_modelview_stack.push(new MatrixM4x4F());
    this.scissor_stack = new ScissorStack();

    this.shader_flat = new GUIShader("shader-flat", base_log);
    this.shader_flat.addVertexShader(new PathVirtual(
      "/sycamore/shaders/flat.v"));
    this.shader_flat.addFragmentShader(new PathVirtual(
      "/sycamore/shaders/flat.f"));
    this.shader_flat.compile(fs, gl);

    this.shader_textured = new GUIShader("shader-textured", base_log);
    this.shader_textured.addVertexShader(new PathVirtual(
      "/sycamore/shaders/textured.v"));
    this.shader_textured.addFragmentShader(new PathVirtual(
      "/sycamore/shaders/textured.f"));
    this.shader_textured.compile(fs, gl);

    this.shader_text = new GUIShader("shader-text", base_log);
    this.shader_text.addVertexShader(new PathVirtual(
      "/sycamore/shaders/text.v"));
    this.shader_text.addFragmentShader(new PathVirtual(
      "/sycamore/shaders/text.f"));
    this.shader_text.compile(fs, gl);

    this.shader_current = null;

    this.theme = new Theme();
    this.theme.setBoundsColor3f(1, 1, 0);
    this.theme.setFailsafeColor3f(1, 0, 1);
    this.theme.setWindowEdgeWidth(1);

    this.theme.setFocusedWindowEdgeColor3f(1, 1, 1);
    this.theme.setFocusedWindowPaneBackgroundColor3f(0.1f, 0.1f, 0.1f);
    this.theme.setFocusedWindowTitlebarBackgroundColor3f(0.8f, 0.0f, 0.0f);
    this.theme.setFocusedWindowTitlebarTextColor3f(1.0f, 1.0f, 1.0f);
    this.theme.setFocusedComponentBackgroundColor3f(0.2f, 0.2f, 0.2f);
    this.theme.setFocusedComponentEdgeColor3f(0.8f, 0.8f, 0.8f);
    this.theme.setFocusedComponentOverBackgroundColor3f(0.5f, 0.5f, 0.5f);
    this.theme.setFocusedComponentOverEdgeColor3f(0.8f, 0.8f, 0.8f);
    this.theme.setFocusedComponentActiveBackgroundColor3f(0.9f, 0.9f, 0.9f);
    this.theme.setFocusedComponentActiveEdgeColor3f(0.8f, 0.8f, 0.8f);

    this.theme.setUnfocusedWindowEdgeColor3f(1, 1, 1);
    this.theme.setUnfocusedWindowPaneBackgroundColor3f(0.2f, 0.2f, 0.2f);
    this.theme.setUnfocusedWindowTitlebarBackgroundColor3f(0.6f, 0.3f, 0.3f);
    this.theme.setUnfocusedWindowTitlebarTextColor3f(0.6f, 0.6f, 0.6f);
    this.theme.setUnfocusedComponentBackgroundColor3f(0.3f, 0.3f, 0.3f);
    this.theme.setUnfocusedComponentEdgeColor3f(0.5f, 0.5f, 0.5f);

    final InputStream fi =
      this.filesystem.openFile("/sycamore/fonts/dejavu-sans.ttf");
    final Font base_font = Font.createFont(Font.TRUETYPE_FONT, fi);
    this.font_medium = base_font.deriveFont(Font.PLAIN, 11);

    this.text_renderer_medium =
      new TextRendererTrivial(gl, this.font_medium, base_log);

    this.draw_primitives = new DrawPrimitives(gl);
  }

  /**
   * Retrieve the logging interface used by components.
   */

  public @Nonnull Log contextGetComponentLog()
  {
    return this.clog;
  }

  /**
   * Retrieve the current {@link DrawPrimitives} interface.
   */

  public @Nonnull DrawPrimitives contextGetDrawPrimitives()
  {
    return this.draw_primitives;
  }

  /**
   * Retrieve a reference to the current filesystem.
   */

  public @Nonnull FilesystemAPI contextGetFilesystem()
  {
    return this.filesystem;
  }

  /**
   * Retrieve a reference to the current OpenGL interface.
   */

  public @Nonnull GLInterface contextGetGL()
  {
    return this.gl;
  }

  /**
   * Retrieve a reference to the current base logging interface.
   */

  public @Nonnull Log contextGetLog()
  {
    return this.log;
  }

  /**
   * Retrieve a reference to the modelview matrix on the top of the current
   * matrix stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   * 
   * @see GUIContext#contextPopMatrixModelview()
   * @see GUIContext#contextPushMatrixModelview()
   */

  public @Nonnull MatrixM4x4F contextGetMatrixModelview()
    throws ConstraintError
  {
    return this.matrix_modelview_stack.peek();
  }

  /**
   * Retrieve a reference to the projection matrix on the top of the current
   * matrix stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   * 
   * @see GUIContext#contextPopMatrixProjection()
   * @see GUIContext#contextPushMatrixProjection()
   */

  public @Nonnull MatrixM4x4F contextGetMatrixProjection()
    throws ConstraintError
  {
    return this.matrix_projection_stack.peek();
  }

  /**
   * Retrieve the logging interface used by the renderer.
   */

  public @Nonnull Log contextGetRendererLog()
  {
    return this.rlog;
  }

  /**
   * Retrieve the shading program used to draw flat-shaded primitives.
   */

  public @Nonnull GUIShader contextGetShaderFlat()
  {
    return this.shader_flat;
  }

  /**
   * Retrieve the shading program used to draw text.
   */

  public @Nonnull GUIShader contextGetShaderText()
  {
    return this.shader_text;
  }

  /**
   * Retrieve the shading program used to draw textured primitives.
   */

  public @Nonnull GUIShader contextGetShaderTextured()
  {
    return this.shader_textured;
  }

  /**
   * Retrieve the text renderer used to compile medium-sized text.
   */

  public @Nonnull TextRenderer contextGetTextRendererMedium()
  {
    return this.text_renderer_medium;
  }

  /**
   * Retrieve the current theme.
   */

  public @Nonnull Theme contextGetTheme()
  {
    return this.theme;
  }

  /**
   * Retrieve the current viewport position.
   */

  public @Nonnull Point<ScreenRelative> contextGetViewportPosition()
  {
    return this.viewport_position;
  }

  /**
   * Retrieve the current viewport size.
   */

  public @Nonnull VectorReadable2I contextGetViewportSize()
  {
    return this.viewport_size;
  }

  /**
   * Pop a matrix from the current modelview stack.
   * 
   * @see GUIContext#contextPushMatrixModelview()
   */

  public void contextPopMatrixModelview()
    throws ConstraintError
  {
    this.matrix_modelview_stack.pop();
  }

  /**
   * Pop a matrix from the current projection stack.
   * 
   * @see GUIContext#contextPushMatrixProjection()
   */

  public void contextPopMatrixProjection()
    throws ConstraintError
  {
    this.matrix_projection_stack.pop();
  }

  public @Nonnull Scissor contextPopScissor()
    throws ConstraintError
  {
    return this.scissor_stack.pop();
  }

  /**
   * Copy the matrix at the top of the modelview matrix and push the copy onto
   * the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   * 
   * @see GUIContext#contextPopMatrixModelview()
   */

  public void contextPushMatrixModelview()
    throws ConstraintError
  {
    this.matrix_modelview_stack.pushCopy();
  }

  /**
   * Copy the matrix at the top of the projection matrix and push the copy
   * onto the stack.
   * 
   * @throws ConstraintError
   *           Iff the stack is empty.
   * 
   * @see GUIContext#contextPopMatrixProjection()
   */

  public void contextPushMatrixProjection()
    throws ConstraintError
  {
    this.matrix_projection_stack.pushCopy();
  }

  public @Nonnull Scissor contextPushScissor(
    final @Nonnull Scissor next)
  {
    return this.scissor_stack.push(next);
  }

  /**
   * Activate the shader <code>shader</code>. A record is kept of the current
   * shader so that subsequent calls to <code>contextShaderActivate</code>
   * with the same value for <code>shader</code> do not incur the penalty of a
   * useless OpenGL state change.
   * 
   * @param shader
   *          The current shader.
   * @throws ConstraintError
   *           Iff <code>shader == null</code>.
   * @throws GLException
   */

  public void contextShaderActivate(
    final @Nonnull GUIShader shader)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(shader, "Shader");

    if (this.shader_current != shader) {
      this.shader_current = shader;
      this.rlog.debug("shader bind: " + this.shader_current);
      this.shader_current.activate(this.gl);
    }
  }

  /**
   * Deactivate the current shader.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  public void contextShaderDeactivate()
    throws ConstraintError,
      GLException
  {
    if (this.shader_current != null) {
      this.rlog.debug("shader unbind: " + this.shader_current);
      this.shader_current.deactivate(this.gl);
    }
  }
}
