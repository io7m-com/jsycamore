package com.io7m.jsycamore;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferCursorWritable2f;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableMap;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jpismo.CompiledPage;
import com.io7m.jpismo.CompiledText;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * Primitive drawing functions.
 */

public final class DrawPrimitives
{
  private static @CheckForNull IndexBuffer            flat_ibo_tri;
  private static @CheckForNull IndexBuffer            flat_ibo_line;
  private static @CheckForNull ArrayBuffer            flat_vbo;
  private static final @Nonnull ArrayBufferDescriptor flat_descriptor;
  private static @CheckForNull IndexBuffer            textured_ibo_tri;
  private static @CheckForNull IndexBuffer            textured_ibo_line;
  private static @CheckForNull ArrayBuffer            textured_vbo;
  private static final @Nonnull ArrayBufferDescriptor textured_descriptor;
  private static final @Nonnull VectorI2I             ONE;

  static {
    try {
      flat_descriptor =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            2) });

      textured_descriptor =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
    new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 2),
    new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2) });

    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    }

    ONE = new VectorI2I(1, 1);
  }

  private static void initializeBuffers(
    final @Nonnull GLInterface g)
    throws GLException,
      ConstraintError
  {
    if (DrawPrimitives.flat_vbo == null) {
      DrawPrimitives.flat_vbo =
        g.arrayBufferAllocate(4, DrawPrimitives.flat_descriptor);

      final ArrayBufferWritableMap map =
        g.arrayBufferMapWrite(DrawPrimitives.flat_vbo);
      try {
        final ArrayBufferCursorWritable2f cp = map.getCursor2f("position");
        cp.put2f(0, 1);
        cp.put2f(0, 0);
        cp.put2f(1, 0);
        cp.put2f(1, 1);
      } finally {
        g.arrayBufferUnmap(DrawPrimitives.flat_vbo);
      }
    }

    if (DrawPrimitives.flat_ibo_tri == null) {
      DrawPrimitives.flat_ibo_tri =
        g.indexBufferAllocate(DrawPrimitives.flat_vbo, 6);

      final IndexBufferWritableMap map =
        g.indexBufferMapWrite(DrawPrimitives.flat_ibo_tri);
      try {
        map.put(0, 0);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 0);
        map.put(4, 2);
        map.put(5, 3);
      } finally {
        g.indexBufferUnmap(DrawPrimitives.flat_ibo_tri);
      }
    }

    if (DrawPrimitives.flat_ibo_line == null) {
      DrawPrimitives.flat_ibo_line =
        g.indexBufferAllocate(DrawPrimitives.flat_vbo, 4);

      final IndexBufferWritableMap map =
        g.indexBufferMapWrite(DrawPrimitives.flat_ibo_line);
      try {
        map.put(0, 0);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
      } finally {
        g.indexBufferUnmap(DrawPrimitives.flat_ibo_line);
      }
    }

    if (DrawPrimitives.textured_vbo == null) {
      DrawPrimitives.textured_vbo =
        g.arrayBufferAllocate(4, DrawPrimitives.textured_descriptor);

      final ArrayBufferWritableMap map =
        g.arrayBufferMapWrite(DrawPrimitives.textured_vbo);
      try {
        final ArrayBufferCursorWritable2f cp = map.getCursor2f("position");
        cp.put2f(0, 1);
        cp.put2f(0, 0);
        cp.put2f(1, 0);
        cp.put2f(1, 1);

        final ArrayBufferCursorWritable2f cu = map.getCursor2f("uv");
        cu.put2f(0, 0);
        cu.put2f(0, 1);
        cu.put2f(1, 1);
        cu.put2f(1, 0);
      } finally {
        g.arrayBufferUnmap(DrawPrimitives.textured_vbo);
      }
    }

    if (DrawPrimitives.textured_ibo_tri == null) {
      DrawPrimitives.textured_ibo_tri =
        g.indexBufferAllocate(DrawPrimitives.textured_vbo, 6);

      final IndexBufferWritableMap map =
        g.indexBufferMapWrite(DrawPrimitives.textured_ibo_tri);
      try {
        map.put(0, 0);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 0);
        map.put(4, 2);
        map.put(5, 3);
      } finally {
        g.indexBufferUnmap(DrawPrimitives.textured_ibo_tri);
      }
    }

    if (DrawPrimitives.textured_ibo_line == null) {
      DrawPrimitives.textured_ibo_line =
        g.indexBufferAllocate(DrawPrimitives.textured_vbo, 4);

      final IndexBufferWritableMap map =
        g.indexBufferMapWrite(DrawPrimitives.textured_ibo_line);
      try {
        map.put(0, 0);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
      } finally {
        g.indexBufferUnmap(DrawPrimitives.textured_ibo_line);
      }
    }
  }

  private final @Nonnull VectorM4F color_cache;

  public DrawPrimitives(
    final @Nonnull GLInterface g)
    throws GLException,
      ConstraintError
  {
    DrawPrimitives.initializeBuffers(g);

    this.color_cache = new VectorM4F();
  }

  /**
   * Render the compiled text <code>compiled_text</code> at the position given
   * by the current modelview matrix in <code>context</code>, using the color
   * <code>color</code>.
   * 
   * @param context
   *          The context.
   * @param compiled_text
   *          The compiled text.
   * @param color
   *          The color.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  @SuppressWarnings("static-method") public void renderCompiledText(
    final @Nonnull GUIContext context,
    final @Nonnull CompiledText compiled_text,
    final @Nonnull VectorReadable4F color)
    throws GLException,
      ConstraintError
  {
    final GLInterface g = context.contextGetGL();
    final MatrixM4x4F mv = context.contextGetMatrixModelview();
    final MatrixM4x4F mp = context.contextGetMatrixProjection();
    final GUIShader p = context.contextGetShaderText();
    final TextureUnit[] units = g.textureGetUnits();

    final int max = compiled_text.maxPages();
    for (int index = 0; index < max; ++index) {
      final CompiledPage page = compiled_text.getPage(index);

      final IndexBuffer ibo = page.getIndexBuffer();
      final ArrayBuffer vbo = page.getVertexBuffer();
      final ArrayBufferDescriptor vbo_desc = vbo.getDescriptor();
      final Texture2DRGBAStatic texture = page.getTexture();

      try {
        g.arrayBufferBind(vbo);
        g.texture2DRGBAStaticBind(units[0], texture);

        context.contextShaderActivate(p);
        p.putModelviewMatrix(g, mv);
        p.putProjectionMatrix(g, mp);
        p.putColor(g, color);
        p.putAlpha(g, color.getWF());
        p.putSize(g, DrawPrimitives.ONE);
        p.putTexture0(g, units[0]);
        p.bindPositionAttribute(g, vbo, vbo_desc.getAttribute("position"));
        p.bindUVAttribute(g, vbo, vbo_desc.getAttribute("uv"));

        g.drawElements(Primitives.PRIMITIVE_TRIANGLES, ibo);
      } finally {
        g.arrayBufferUnbind();
      }
    }
  }

  /**
   * Render the edge of a rectangle of size <code>size</code>, of edge width
   * <code>edge_width</code> and using the color <code>color</code>, at the
   * position given by the current modelview matrix in <code>context</code>.
   * The edge is drawn internal to the rectangle, meaing that a combined edged
   * and filled rectangle can be draw by first calling
   * {@link DrawPrimitives#renderRectangleFill(GUIContext, VectorReadable2I, VectorReadable3F)}
   * and then <code>renderRectangleEdge</code>.
   * 
   * @param size
   *          The width/height of the rectangle.
   * @param edge_width
   *          The thickness of the rectangle edge.
   * @param color
   *          The color.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  public void renderRectangleEdge(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size,
    final int edge_width,
    final @Nonnull VectorReadable3F color)
    throws GLException,
      ConstraintError
  {
    this.color_cache.x = color.getXF();
    this.color_cache.y = color.getYF();
    this.color_cache.z = color.getZF();
    this.color_cache.w = 1.0f;

    this.renderRectangleEdge(context, size, edge_width, this.color_cache);
  }

  /**
   * Render the edge of a rectangle of size <code>size</code>, of edge width
   * <code>edge_width</code> and using the color <code>color</code>, at the
   * position given by the current modelview matrix in <code>context</code>.
   * The edge is drawn internal to the rectangle, meaing that a combined edged
   * and filled rectangle can be draw by first calling
   * {@link DrawPrimitives#renderRectangleFill(GUIContext, VectorReadable2I, VectorReadable4F)}
   * and then <code>renderRectangleEdge</code>.
   * 
   * @param size
   *          The width/height of the rectangle.
   * @param edge_width
   *          The thickness of the rectangle edge.
   * @param color
   *          The color.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  @SuppressWarnings("static-method") public void renderRectangleEdge(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size,
    final int edge_width,
    final @Nonnull VectorReadable4F color)
    throws GLException,
      ConstraintError
  {
    final GLInterface g = context.contextGetGL();

    context.contextPushMatrixModelview();
    final MatrixM4x4F mv = context.contextGetMatrixModelview();
    final MatrixM4x4F mp = context.contextGetMatrixProjection();
    final GUIShader p = context.contextGetShaderFlat();

    try {
      final VectorM2I edge_size = new VectorM2I();
      final VectorM2I edge_offset = new VectorM2I();

      VectorM2I.copy(size, edge_size);
      edge_size.x -= edge_width;
      edge_size.y -= edge_width;
      edge_offset.x = (int) Math.ceil(edge_width * 0.5);
      edge_offset.y = (int) Math.floor(edge_width * 0.5);
      MatrixM4x4F.translateByVector2IInPlace(mv, edge_offset);

      assert DrawPrimitives.flat_vbo != null;
      assert DrawPrimitives.flat_ibo_line != null;

      g.arrayBufferBind(DrawPrimitives.flat_vbo);
      g.lineSetWidth(edge_width);
      g.pointEnableProgramSizeControl();

      context.contextShaderActivate(p);
      p.putModelviewMatrix(g, mv);
      p.putProjectionMatrix(g, mp);
      p.putSize(g, edge_size);
      p.putPointSize(g, edge_width);
      p.putColor(g, color);
      p.bindPositionAttribute(
        g,
        DrawPrimitives.flat_vbo,
        DrawPrimitives.flat_descriptor.getAttribute("position"));

      g.drawElements(
        Primitives.PRIMITIVE_LINE_LOOP,
        DrawPrimitives.flat_ibo_line);
      g.drawElements(
        Primitives.PRIMITIVE_POINTS,
        DrawPrimitives.flat_ibo_line);
    } finally {
      g.arrayBufferUnbind();
      context.contextPopMatrixModelview();
    }
  }

  /**
   * Render a filled rectangle of size <code>size</code>, using the color
   * <code>color</code>, at the position given by the current modelview matrix
   * in <code>context</code>.
   * 
   * @param size
   *          The width/height of the rectangle.
   * @param color
   *          The color.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  public void renderRectangleFill(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size,
    final @Nonnull VectorReadable3F color)
    throws GLException,
      ConstraintError
  {
    this.color_cache.x = color.getXF();
    this.color_cache.y = color.getYF();
    this.color_cache.z = color.getZF();
    this.color_cache.w = 1.0f;

    this.renderRectangleFill(context, size, this.color_cache);
  }

  /**
   * Render a filled rectangle of size <code>size</code>, using the color
   * <code>color</code>, at the position given by the current modelview matrix
   * in <code>context</code>.
   * 
   * @param size
   *          The width/height of the rectangle.
   * @param color
   *          The color.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  @SuppressWarnings("static-method") public void renderRectangleFill(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size,
    final @Nonnull VectorReadable4F color)
    throws GLException,
      ConstraintError
  {
    final GLInterface g = context.contextGetGL();
    final MatrixM4x4F mv = context.contextGetMatrixModelview();
    final MatrixM4x4F mp = context.contextGetMatrixProjection();
    final GUIShader p = context.contextGetShaderFlat();

    try {
      assert DrawPrimitives.flat_vbo != null;
      assert DrawPrimitives.flat_ibo_tri != null;

      g.arrayBufferBind(DrawPrimitives.flat_vbo);

      context.contextShaderActivate(p);
      p.putModelviewMatrix(g, mv);
      p.putProjectionMatrix(g, mp);
      p.putSize(g, size);
      p.putColor(g, color);
      p.bindPositionAttribute(
        g,
        DrawPrimitives.flat_vbo,
        DrawPrimitives.flat_descriptor.getAttribute("position"));

      g.drawElements(
        Primitives.PRIMITIVE_TRIANGLES,
        DrawPrimitives.flat_ibo_tri);
    } finally {
      g.arrayBufferUnbind();
    }
  }

  /**
   * Render a filled rectangle of size <code>size</code>, using the texture
   * <code>texture</code>, of opacity <code>alpha</code>, at the position
   * given by the current modelview matrix in <code>context</code>.
   * 
   * @param size
   *          The width/height of the rectangle.
   * @param alpha
   *          The opacity of the rectangle, in the range
   *          <code>[0.0 .. 1.0]</code> inclusive.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  @SuppressWarnings("static-method") public void renderRectangleTextured(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size,
    final float alpha,
    final @Nonnull Texture2DRGBAStatic texture)
    throws GLException,
      ConstraintError
  {
    final GLInterface g = context.contextGetGL();
    final MatrixM4x4F mv = context.contextGetMatrixModelview();
    final MatrixM4x4F mp = context.contextGetMatrixProjection();
    final GUIShader p = context.contextGetShaderTextured();
    final TextureUnit[] units = g.textureGetUnits();

    try {
      assert DrawPrimitives.textured_vbo != null;
      assert DrawPrimitives.textured_ibo_tri != null;

      g.arrayBufferBind(DrawPrimitives.textured_vbo);
      g.texture2DRGBAStaticBind(units[0], texture);

      context.contextShaderActivate(p);
      p.putModelviewMatrix(g, mv);
      p.putProjectionMatrix(g, mp);
      p.putAlpha(g, alpha);
      p.putSize(g, size);
      p.putTexture0(g, units[0]);
      p.bindPositionAttribute(
        g,
        DrawPrimitives.textured_vbo,
        DrawPrimitives.textured_descriptor.getAttribute("position"));
      p.bindUVAttribute(
        g,
        DrawPrimitives.textured_vbo,
        DrawPrimitives.textured_descriptor.getAttribute("uv"));

      g.drawElements(
        Primitives.PRIMITIVE_TRIANGLES,
        DrawPrimitives.textured_ibo_tri);
    } finally {
      g.arrayBufferUnbind();
    }
  }
}
