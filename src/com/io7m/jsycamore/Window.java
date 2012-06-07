package com.io7m.jsycamore;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.components.AbstractContainer;
import com.io7m.jsycamore.components.ContainerThemed;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConstants;
import com.io7m.jsycamore.geometry.PointConversion;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScissorRelative;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jsycamore.geometry.WindowRelative;
import com.io7m.jsycamore.windows.ContentPane;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Abstract class implementing the basic functionality for all windows.
 * 
 * Each {@link Window} contains an n-ary tree of <code>Component</code>s.
 */

public abstract class Window implements Comparable<Window>
{
  private static final @Nonnull AtomicLong     id_pool;

  static {
    id_pool = new AtomicLong(0);
    SHADOW_OFFSET = new VectorI2I(4, 4);
    SHADOW_COLOR = new VectorI4F(0f, 0f, 0f, 0.3f);
  }

  private final @Nonnull Point<ScreenRelative> position;
  private final @Nonnull AbstractContainer     root;
  private final @Nonnull Long                  id;
  private @Nonnull Framebuffer                 framebuffer;
  private @Nonnull Texture2DRGBAStatic         framebuffer_texture;
  private float                                alpha;
  private @Nonnull WindowState                 state;
  private static final @Nonnull VectorI2I      SHADOW_OFFSET;
  private static final @Nonnull VectorI4F      SHADOW_COLOR;

  protected Window(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(context, "GUI context");
    Constraints.constrainNotNull(position, "Position");
    Constraints.constrainNotNull(size, "Size");

    this.id = Long.valueOf(Window.id_pool.incrementAndGet());
    this.root = new ContainerThemed(this, PointConstants.PARENT_ORIGIN, size);
    this.alpha = 1.0f;
    this.state = WindowState.WINDOW_OPEN;
    this.position = new Point<ScreenRelative>(position);

    this.windowSetMinimumWidth(context, 2);
    this.windowSetMinimumHeight(context, 2);
    this.resizeFramebuffer(context.contextGetGL(), this.windowGetSize());
  }

  @Override public final int compareTo(
    final Window other)
  {
    return this.id.compareTo(other.id);
  }

  @Override public final boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Window other = (Window) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override public final int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  private final void resizeFramebuffer(
    final @Nonnull GLInterface g,
    final @Nonnull VectorReadable2I size)
    throws GUIException,
      ConstraintError
  {
    try {
      if (this.framebuffer != null) {
        final int this_w = this.framebuffer_texture.getWidth();
        final int this_h = this.framebuffer_texture.getHeight();
        final int new_w = size.getXI();
        final int new_h = size.getYI();
        if ((this_w == new_w) && (this_h == new_h)) {
          return;
        }
      }

      final Framebuffer new_fbo = g.framebufferAllocate();
      final Texture2DRGBAStatic new_fbt =
        g.texture2DRGBAStaticAllocate(
          "window-" + this.id + "-texture",
          size.getXI(),
          size.getYI(),
          TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);

      g.framebufferAttachStorage(
        new_fbo,
        new FramebufferAttachment[] { new ColorAttachment(new_fbt, 0) });

      final Framebuffer old_fbo = this.framebuffer;
      final Texture2DRGBAStatic old_fbt = this.framebuffer_texture;
      this.framebuffer = new_fbo;
      this.framebuffer_texture = new_fbt;

      if (old_fbo != null) {
        g.framebufferDelete(old_fbo);
      }
      if (old_fbt != null) {
        g.texture2DRGBAStaticDelete(old_fbt);
      }
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Window ");
    builder.append(this.id);
    builder.append("]");
    return builder.toString();
  }

  /**
   * Return <code>true</code> iff the screen-relative point given by
   * <code>point</code> is in the window.
   * 
   * @param point
   * @throws ConstraintError
   */

  public final boolean windowContainsScreenRelativePosition(
    final @Nonnull Point<ScreenRelative> point)
    throws ConstraintError
  {
    final Point<WindowRelative> local =
      PointConversion.screenToWindow(this.windowGetPosition(), point);
    final VectorReadable2I size = this.root.componentGetSize();

    final boolean in_x =
      (local.getXI() >= 0) && (local.getXI() < size.getXI());
    final boolean in_y =
      (local.getYI() >= 0) && (local.getYI() < size.getYI());
    return in_x && in_y;
  }

  /**
   * Retrieve the opacity of the window, in the range
   * <code>[0.0 .. 1.0]</code> inclusive.
   */

  public final float windowGetAlpha()
  {
    return this.alpha;
  }

  /**
   * Retrieve the component that contains the screen-relative point
   * <code>point</code>, or <code>null</code> iff none of them do.
   * 
   * @param point
   *          The screen-relative point.
   * @throws ConstraintError
   *           Iff <code>point == null</code>.
   */

  public @CheckForNull Component windowGetComponentByScreenRelativePosition(
    final @Nonnull Point<ScreenRelative> point)
    throws ConstraintError
  {
    final Point<WindowRelative> local =
      PointConversion.screenToWindow(this.windowGetPosition(), point);
    return this.windowGetComponentByWindowRelativePosition(local);
  }

  private @CheckForNull Component windowGetComponentByWindowRelativePosition(
    final @Nonnull Point<WindowRelative> wr)
  {
    return this.root.componentGetByWindowRelativePosition(wr);
  }

  /**
   * Retrieve the content pane of the window.
   */

  public abstract @Nonnull ContentPane windowGetContentPane();

  /**
   * Retrieve the unique ID of the window.
   */

  public final @Nonnull Long windowGetID()
  {
    return this.id;
  }

  /**
   * Retrieve the screen-relative position of the top-left corner of the
   * window.
   */

  public final @Nonnull Point<ScreenRelative> windowGetPosition()
  {
    return this.position;
  }

  protected final @Nonnull AbstractContainer windowGetRootPane()
  {
    return this.root;
  }

  /**
   * Retrieve the current size of the window.
   */

  public final @Nonnull VectorReadable2I windowGetSize()
  {
    return this.root.componentGetSize();
  }

  final @Nonnull WindowState windowGetState()
  {
    return this.state;
  }

  /**
   * Returns <code>true</code> iff the window is open and is the front-most
   * window.
   */

  public boolean windowIsFocused()
  {
    return this.state == WindowState.WINDOW_FOCUSED;
  }

  final void windowRenderActual(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException
  {
    try {
      final DrawPrimitives draw = context.contextGetDrawPrimitives();
      final Theme theme = context.contextGetTheme();
      final Point<ScreenRelative> window_pos = this.windowGetPosition();
      final Log log = context.contextGetRendererLog();
      log.debug("actual " + this);

      context.contextPushMatrixModelview();
      try {
        final MatrixM4x4F mv = context.contextGetMatrixModelview();
        MatrixM4x4F.setIdentity(mv);
        MatrixM4x4F.translateByVector2IInPlace(mv, window_pos);
        MatrixM4x4F.translateByVector2IInPlace(mv, Window.SHADOW_OFFSET);

        draw.renderRectangleFill(
          context,
          this.windowGetSize(),
          Window.SHADOW_COLOR);
      } finally {
        context.contextPopMatrixModelview();
      }

      context.contextPushMatrixModelview();
      try {
        final MatrixM4x4F mv = context.contextGetMatrixModelview();
        MatrixM4x4F.setIdentity(mv);
        MatrixM4x4F.translateByVector2IInPlace(mv, window_pos);

        draw.renderRectangleTextured(
          context,
          this.windowGetSize(),
          this.alpha,
          this.framebuffer_texture);

        final VectorM2I size = new VectorM2I(this.windowGetSize());
        size.y = size.y + 1;

        if (this.windowIsFocused()) {
          draw.renderRectangleEdge(
            context,
            size,
            theme.getWindowEdgeWidth(),
            theme.getFocusedWindowEdgeColor());
        } else {
          draw.renderRectangleEdge(
            context,
            size,
            theme.getWindowEdgeWidth(),
            theme.getUnfocusedWindowEdgeColor());
        }

      } finally {
        context.contextPopMatrixModelview();
      }
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  private void windowRenderComponents(
    final @Nonnull GUIContext context)
    throws GUIException,
      ConstraintError,
      GLException
  {
    this.windowRenderComponentsInner(context, this.root);
  }

  private void windowRenderComponentsInner(
    final @Nonnull GUIContext context,
    final @Nonnull Component c)
    throws GUIException,
      ConstraintError,
      GLException
  {
    final VectorReadable2I component_siz = c.componentGetSize();

    final PointReadable<ParentRelative> component_ppos =
      c.componentGetPositionParentRelative();
    final PointReadable<WindowRelative> component_wpos =
      c.componentGetPositionWindowRelative();

    final Point<ScissorRelative> sp =
      PointConversion.windowToScissor(
        this.windowGetSize(),
        new Point<WindowRelative>(component_wpos.getXI(), component_wpos
          .getYI() + component_siz.getYI()));

    final Scissor scissor =
      context.contextPushScissor(new Scissor(sp, component_siz));

    context.contextPushMatrixModelview();
    try {
      final GLInterface g = context.contextGetGL();

      final MatrixM4x4F mv = context.contextGetMatrixModelview();
      MatrixM4x4F.translateByVector2IInPlace(mv, component_ppos);

      g.scissorEnable(scissor.getPosition(), scissor.getSize());
      c.componentRenderPreDescendants(context);

      for (final Component child : c.componentGetChildren()) {
        this.windowRenderComponentsInner(context, child);
      }

      g.scissorEnable(scissor.getPosition(), scissor.getSize());
      c.componentRenderPostDescendants(context);

      g.scissorDisable();
    } finally {
      context.contextPopScissor();
      context.contextPopMatrixModelview();
    }
  }

  final void windowRenderContents(
    final @Nonnull GUIContext context)
    throws GUIException,
      ConstraintError
  {
    try {
      final GLInterface g = context.contextGetGL();
      final Theme theme = context.contextGetTheme();
      final Log log = context.contextGetRendererLog();
      log.debug("contents " + this);

      g.framebufferBind(this.framebuffer);
      try {
        g.colorBufferClearV3f(theme.getFailsafeColor());
        g.viewportSet(VectorI2I.ZERO, this.windowGetSize());

        context.contextPushMatrixProjection();
        try {
          final MatrixM4x4F mp = context.contextGetMatrixProjection();
          final double left = 0.0;
          final double right = this.windowGetSize().getXI();
          final double bottom = this.windowGetSize().getYI();
          final double top = 0.0;
          ProjectionMatrix.makeOrthographic(
            mp,
            left,
            right,
            bottom,
            top,
            1,
            100);

          final MatrixM4x4F mv = context.contextGetMatrixModelview();
          MatrixM4x4F.setIdentity(mv);

          this.windowRenderComponents(context);
        } finally {
          context.contextPopMatrixProjection();
        }
      } finally {
        g.framebufferUnbind();
      }
    } catch (final GLException e) {
      throw new GUIException(e);
    }
  }

  /**
   * Set the opacity of the window to <code>new_alpha</code>.
   * 
   * @param new_alpha
   *          The opacity value, clamped to the range
   *          <code>[0.0 .. 1.0]</code> inclusive.
   */

  public final void windowSetAlpha(
    final float new_alpha)
  {
    this.alpha = Math.min(1.0f, Math.max(0.0f, new_alpha));
  }

  /**
   * Set the minimum height of the window.
   * 
   * @param height
   *          The minimum height.
   * @throws ConstraintError
   *           Iff <code>2 <= height <= Integer.MAX_VALUE == false</code>.
   */

  public final void windowSetMinimumHeight(
    final @Nonnull GUIContext context,
    final int height)
    throws ConstraintError
  {
    this.root.componentSetMinimumHeight(
      context,
      Constraints.constrainRange(height, 2, Integer.MAX_VALUE, "Height"));
  }

  /**
   * Set the minimum size of the window.
   * 
   * @param min_size
   *          The minimum size.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>min_size == null</code></li>
   *           <li>
   *           Iff
   *           <code>2 <= min_size.getXI() <= Integer.MAX_VALUE == false</code>
   *           .</li>
   *           <li>
   *           Iff
   *           <code>2 <= min_size.getYI() <= Integer.MAX_VALUE == false</code>
   *           .</li>
   *           </ul>
   */

  public final void windowSetMinimumSize(
    final @Nonnull GUIContext context,
    final VectorReadable2I min_size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(min_size, "Minimum size");
    Constraints.constrainRange(
      min_size.getXI(),
      2,
      Integer.MAX_VALUE,
      "Minimum width");
    Constraints.constrainRange(
      min_size.getYI(),
      2,
      Integer.MAX_VALUE,
      "Minimum height");
    this.root.componentSetMinimumSize(context, min_size);
  }

  /**
   * Set the minimum width of the window.
   * 
   * @param width
   *          The minimum width.
   * @throws ConstraintError
   *           Iff <code>2 <= width <= Integer.MAX_VALUE == false</code>.
   */

  public final void windowSetMinimumWidth(
    final @Nonnull GUIContext context,
    final int width)
    throws ConstraintError
  {
    this.root.componentSetMinimumWidth(
      context,
      Constraints.constrainRange(width, 2, Integer.MAX_VALUE, "Width"));
  }

  /**
   * Set the position of the window.
   * 
   * @param new_position
   *          The new position.
   * @throws ConstraintError
   *           Iff <code>new_position == null</code>.
   */

  public final void windowSetPosition(
    final @Nonnull Point<ScreenRelative> new_position)
    throws ConstraintError
  {
    Constraints.constrainNotNull(new_position, "Position");
    this.position.setXI(new_position.getXI());
    this.position.setYI(new_position.getYI());
  }

  /**
   * Set the size of the window.
   * 
   * @param context
   *          The GUI context.
   * @param size
   *          The new window size.
   * @throws ConstraintError
   *           Iff <code>size == null</code> or <code>size</code> describes a
   *           rectangle smaller than <code>(2,2)</code>.
   * @throws GUIException
   */

  public final void windowSetSize(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError,
      GUIException
  {
    Constraints.constrainNotNull(context, "GUI context");
    this.root.componentSetSize(context, size);

    final Log log = context.contextGetComponentLog();
    log.debug("size " + this.windowGetSize());

    this.resizeFramebuffer(context.contextGetGL(), this.windowGetSize());
  }

  final void windowSetState(
    final @Nonnull WindowState next_state)
  {
    this.state = next_state;
  }

  /**
   * Inform the window that it should close. The GUI will close the window
   * before the next frame is rendered.
   */

  public void windowSetWantClose()
  {
    this.windowSetState(WindowState.WINDOW_WANT_CLOSE);
  }

  /**
   * Inform the window that it should open. The GUI will open the window
   * before the next frame is rendered.
   */

  public void windowSetWantOpen()
  {
    this.windowSetState(WindowState.WINDOW_WANT_OPEN);
  }
}
