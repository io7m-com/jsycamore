package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.CompilableProgram;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.UsableProgram;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * The standard GUI shader program. This class defines a standard GLSL
 * interface that all shaders called by the GUI are expected to support. Some
 * GLSL parameters are optional; those that are have an accompanying "has"
 * function, eg. {@link GUIShader#hasTexture0()}.
 */

public final class GUIShader implements CompilableProgram, UsableProgram
{
  private final @Nonnull Program program;

  public GUIShader(
    final @Nonnull String name,
    final @Nonnull Log log)
    throws ConstraintError
  {
    this.program = new Program(name, log);
  }

  @Override public void activate(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.program.activate(gl);
  }

  @Override public void addFragmentShader(
    final @Nonnull PathVirtual path)
    throws ConstraintError
  {
    this.program.addFragmentShader(path);
  }

  @Override public void addVertexShader(
    final @Nonnull PathVirtual path)
    throws ConstraintError
  {
    this.program.addVertexShader(path);
  }

  /**
   * Use <code>aba</code> from <code>vbo</code> as the source of data for the
   * "position" program attribute.
   * 
   * @param g
   *          The OpenGL interface.
   * @param vbo
   *          The source array buffer.
   * @param aba
   *          The source array buffer attribute.
   * @throws ConstraintError
   *           Iff the program does not have a "position" attribute.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void bindPositionAttribute(
    final @Nonnull GLInterface g,
    final @Nonnull ArrayBuffer vbo,
    final @Nonnull ArrayBufferAttribute aba)
    throws ConstraintError,
      GLException
  {
    final ProgramAttribute pa = this.program.getAttribute("position");
    Constraints.constrainNotNull(pa, "Position attribute");
    g.arrayBufferBindVertexAttribute(vbo, aba, pa);
  }

  /**
   * Use <code>aba</code> from <code>vbo</code> as the source of data for the
   * "uv" program attribute.
   * 
   * @param g
   *          The OpenGL interface.
   * @param vbo
   *          The source array buffer.
   * @param aba
   *          The source array buffer attribute.
   * @throws ConstraintError
   *           Iff the program does not have a "uv" attribute.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void bindUVAttribute(
    final @Nonnull GLInterface g,
    final @Nonnull ArrayBuffer vbo,
    final @Nonnull ArrayBufferAttribute aba)
    throws ConstraintError,
      GLException
  {
    final ProgramAttribute pa = this.program.getAttribute("uv");
    Constraints.constrainNotNull(pa, "UV attribute");
    g.arrayBufferBindVertexAttribute(vbo, aba, pa);
  }

  @Override public void compile(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLCompileException
  {
    this.program.compile(fs, gl);
  }

  @Override public void deactivate(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.program.deactivate(gl);
  }

  /**
   * Return <code>true</code> iff this shader supports the "alpha" parameter.
   */

  public boolean hasAlpha()
    throws ConstraintError
  {
    return this.program.getUniform("alpha") != null;
  }

  /**
   * Return <code>true</code> iff this shader supports the "color" parameter.
   */

  public boolean hasColor()
    throws ConstraintError
  {
    return this.program.getUniform("color") != null;
  }

  /**
   * Return <code>true</code> iff this shader supports the "point_size"
   * parameter.
   */

  public boolean hasPointSize()
    throws ConstraintError
  {
    return this.program.getUniform("point_size") != null;
  }

  /**
   * Return <code>true</code> iff this shader supports the "texture0"
   * parameter.
   */

  public boolean hasTexture0()
    throws ConstraintError
  {
    return this.program.getUniform("texture0") != null;
  }

  /**
   * Set the opacity used for shading.
   * 
   * @param g
   *          The OpenGL interface.
   * @param alpha
   *          The alpha value to use.
   * @throws ConstraintError
   *           Iff the program does not have an "alpha" uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putAlpha(
    final @Nonnull GLInterface g,
    final float alpha)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainArbitrary(this.hasAlpha(), "Has alpha uniform");
    final ProgramUniform u = this.program.getUniform("alpha");
    g.programPutUniformFloat(u, alpha);
  }

  /**
   * Set the color used for shading.
   * 
   * @param g
   *          The OpenGL interface.
   * @param color
   *          The color value to use.
   * @throws ConstraintError
   *           Iff the program does not have a "color" uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putColor(
    final @Nonnull GLInterface g,
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainArbitrary(this.hasColor(), "Has color uniform");
    final ProgramUniform u = this.program.getUniform("color");
    g.programPutUniformVector4f(u, color);
  }

  /**
   * Set the modelview matrix.
   * 
   * @param g
   *          The OpenGL interface.
   * @param m
   *          The modelview matrix.
   * @throws ConstraintError
   *           Iff the program does not have a "matrix_modelview"
   *           uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putModelviewMatrix(
    final @Nonnull GLInterface g,
    final @Nonnull MatrixM4x4F m)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("matrix_modelview");
    Constraints.constrainNotNull(u, "Matrix modelview uniform");
    g.programPutUniformMatrix4x4f(u, m);
  }

  /**
   * Set the size of points used in shading.
   * 
   * @param g
   *          The OpenGL interface.
   * @param point_size
   *          The point size in pixels.
   * @throws ConstraintError
   *           Iff the program does not have a "point_size" uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putPointSize(
    final @Nonnull GLInterface g,
    final int point_size)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainArbitrary(
      this.hasPointSize(),
      "Program has point_size");

    final ProgramUniform u = this.program.getUniform("point_size");
    g.programPutUniformFloat(u, point_size);
  }

  /**
   * Set the projection matrix.
   * 
   * @param g
   *          The OpenGL interface.
   * @param m
   *          The projection matrix.
   * @throws ConstraintError
   *           Iff the program does not have a "matrix_projection"
   *           uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putProjectionMatrix(
    final @Nonnull GLInterface g,
    final @Nonnull MatrixM4x4F m)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("matrix_projection");
    Constraints.constrainNotNull(u, "Matrix projection uniform");
    g.programPutUniformMatrix4x4f(u, m);
  }

  /**
   * Set the scaling factor.
   * 
   * @param g
   *          The OpenGL interface.
   * @param size
   *          The scaling factor to use when shading.
   * @throws ConstraintError
   *           Iff the program does not have a "matrix_projection"
   *           uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putSize(
    final @Nonnull GLInterface g,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("size");
    Constraints.constrainNotNull(u, "Size uniform");
    g.programPutUniformVector2i(u, size);
  }

  /**
   * Set the texture unit to use for texture <code>0</code> in the shader.
   * 
   * @param g
   *          The OpenGL interface.
   * @param unit
   *          The texture unit.
   * @throws ConstraintError
   *           Iff the program does not have a "texture0" uniform/parameter.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void putTexture0(
    final @Nonnull GLInterface g,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("texture0");
    Constraints.constrainNotNull(u, "Texture uniform");
    g.programPutUniformTextureUnit(u, unit);
  }

  @Override public void removeFragmentShader(
    final @Nonnull PathVirtual path,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.program.removeFragmentShader(path, gl);
  }

  @Override public void removeVertexShader(
    final @Nonnull PathVirtual path,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.program.removeVertexShader(path, gl);
  }

  @Override public boolean requiresCompilation(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLInterface gl)
    throws FilesystemError,
      ConstraintError
  {
    return this.program.requiresCompilation(fs, gl);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[GUIShader ");
    builder.append(this.program);
    builder.append("]");
    return builder.toString();
  }
}
