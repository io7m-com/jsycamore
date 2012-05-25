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

  public void putAlpha(
    final @Nonnull GLInterface g,
    final float alpha)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("alpha");
    Constraints.constrainNotNull(u, "Alpha uniform");
    g.programPutUniformFloat(u, alpha);
  }

  public void putColor(
    final @Nonnull GLInterface g,
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("color");
    Constraints.constrainNotNull(u, "Color uniform");
    g.programPutUniformVector4f(u, color);
  }

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

  public void putPointSize(
    final @Nonnull GLInterface g,
    final int edge_width)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("point_size");
    Constraints.constrainNotNull(u, "Point size uniform");
    g.programPutUniformFloat(u, edge_width);
  }

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

  public void putTextureUnit(
    final @Nonnull GLInterface g,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    final ProgramUniform u = this.program.getUniform("texture");
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
