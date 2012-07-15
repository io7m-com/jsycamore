package com.io7m.jsycamore;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLResource;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointConversion;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jsycamore.geometry.WindowRelative;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Abstract class implementing the basic functionality for all components.
 * 
 * Each {@link Window} contains an n-ary tree of <code>Component</code>s.
 */

public abstract class Component implements
  Comparable<Component>,
  GLResource,
  MouseListener<Component>,
  ResizeListener<Component>
{
  /**
   * Type representing how a component behaves when its parent resizes.
   */

  public static enum ParentResizeBehavior
  {
    /** The component does nothing when its parent resizes. */
    BEHAVIOR_FIXED,

    /**
     * The component resizes by <code>n</code> pixels when its parent changes
     * size by <code>n</code> pixels.
     */
    BEHAVIOR_RESIZE,

    /**
     * The component moves down/right by <code>n</code> pixels when its parent
     * changes size by <code>n</code> pixels.
     */
    BEHAVIOR_MOVE,
  }

  private static final @Nonnull AtomicLong     id_pool;

  static {
    id_pool = new AtomicLong(0);
  }

  private final @Nonnull Long                  id;

  private final @Nonnull TreeSet<Component>    children;
  private final @Nonnull Point<ParentRelative> position_unrestricted;
  private final @Nonnull Point<ParentRelative> position_current;

  private final @Nonnull Point<ParentRelative> position_minimum;
  private final @Nonnull Point<ParentRelative> position_maximum;
  private final @Nonnull VectorM2I             size_unrestricted;
  private final @Nonnull VectorM2I             size_current;

  private final @Nonnull VectorM2I             size_minimum;
  private final @Nonnull VectorM2I             size_maximum;
  private final @CheckForNull Window           window;
  private @CheckForNull Component              parent;

  private @Nonnull ParentResizeBehavior        resize_width_behavior;
  private @Nonnull ParentResizeBehavior        resize_height_behavior;
  private boolean                              enabled = true;

  protected Component(
    final @Nonnull Component parent,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(position, "Position");
    Constraints.constrainNotNull(size, "Size");
    Constraints.constrainRange(size.getXI(), 1, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(size.getYI(), 1, Integer.MAX_VALUE, "Height");
    Constraints.constrainNotNull(parent, "Parent");

    this.id = Long.valueOf(Component.id_pool.incrementAndGet());
    this.parent = parent;
    this.window = null;

    this.position_current =
      new Point<ParentRelative>(Constraints.constrainNotNull(
        position,
        "Position"));
    this.position_unrestricted =
      new Point<ParentRelative>(this.position_current);
    this.position_minimum =
      new Point<ParentRelative>(Integer.MIN_VALUE, Integer.MIN_VALUE);
    this.position_maximum =
      new Point<ParentRelative>(Integer.MAX_VALUE, Integer.MAX_VALUE);

    this.size_current = new VectorM2I(size);
    this.size_unrestricted = new VectorM2I(this.size_current);
    this.size_minimum = new VectorM2I(1, 1);
    this.size_maximum = new VectorM2I(Integer.MAX_VALUE, Integer.MAX_VALUE);

    this.children = new TreeSet<Component>();
    this.resize_width_behavior = ParentResizeBehavior.BEHAVIOR_FIXED;
    this.resize_height_behavior = ParentResizeBehavior.BEHAVIOR_FIXED;

    parent.children.add(this);
  }

  protected Component(
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(position, "Position");
    Constraints.constrainNotNull(size, "Size");
    Constraints.constrainRange(size.getXI(), 1, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(size.getYI(), 1, Integer.MAX_VALUE, "Height");

    this.id = Long.valueOf(Component.id_pool.incrementAndGet());
    this.parent = null;
    this.window = null;

    this.position_current =
      new Point<ParentRelative>(Constraints.constrainNotNull(
        position,
        "Position"));
    this.position_unrestricted =
      new Point<ParentRelative>(this.position_current);
    this.position_minimum =
      new Point<ParentRelative>(Integer.MIN_VALUE, Integer.MIN_VALUE);
    this.position_maximum =
      new Point<ParentRelative>(Integer.MAX_VALUE, Integer.MAX_VALUE);

    this.size_current = new VectorM2I(size);
    this.size_unrestricted = new VectorM2I(this.size_current);
    this.size_minimum = new VectorM2I(1, 1);
    this.size_maximum = new VectorM2I(Integer.MAX_VALUE, Integer.MAX_VALUE);

    this.children = new TreeSet<Component>();
    this.resize_width_behavior = ParentResizeBehavior.BEHAVIOR_FIXED;
    this.resize_height_behavior = ParentResizeBehavior.BEHAVIOR_FIXED;
  }

  protected Component(
    final @Nonnull Window window,
    final @Nonnull PointReadable<ParentRelative> position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(position, "Position");
    Constraints.constrainNotNull(size, "Size");
    Constraints.constrainRange(size.getXI(), 1, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(size.getYI(), 1, Integer.MAX_VALUE, "Height");
    Constraints.constrainNotNull(window, "Window");

    this.id = Long.valueOf(Component.id_pool.incrementAndGet());
    this.parent = null;
    this.window = Constraints.constrainNotNull(window, "Window");

    this.position_current =
      new Point<ParentRelative>(Constraints.constrainNotNull(
        position,
        "Position"));
    this.position_unrestricted =
      new Point<ParentRelative>(this.position_current);
    this.position_minimum =
      new Point<ParentRelative>(Integer.MIN_VALUE, Integer.MIN_VALUE);
    this.position_maximum =
      new Point<ParentRelative>(Integer.MAX_VALUE, Integer.MAX_VALUE);

    this.size_current = new VectorM2I(size);
    this.size_unrestricted = new VectorM2I(this.size_current);
    this.size_minimum = new VectorM2I(1, 1);
    this.size_maximum = new VectorM2I(Integer.MAX_VALUE, Integer.MAX_VALUE);

    this.children = new TreeSet<Component>();
    this.resize_width_behavior = ParentResizeBehavior.BEHAVIOR_FIXED;
    this.resize_height_behavior = ParentResizeBehavior.BEHAVIOR_FIXED;
  }

  @Override public final int compareTo(
    final Component other)
  {
    return this.id.compareTo(other.id);
  }

  /**
   * Make <code>new_parent</code> the parent of this component.
   * 
   * @param new_parent
   *          The new parent.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>new_parent == null</li>
   *           <li>new_parent == this</li>
   *           </ul>
   */

  public final void componentAttachToParent(
    final @Nonnull Component new_parent)
    throws ConstraintError
  {
    Constraints.constrainNotNull(new_parent, "Parent");
    Constraints.constrainArbitrary(
      this.parent == null,
      "Component already has parent");
    Constraints.constrainArbitrary(
      new_parent != this,
      "Not parenting component to itself");

    this.parent = new_parent;
    this.parent.children.add(this);
  }

  /**
   * Return <code>true</code> iff this component contains the screen-relative
   * point <code>point</code>.
   * 
   * @param point
   *          The screen-relative point.
   * @throws ConstraintError
   *           Iff <code>point == null</code> or an internal constraint error
   *           occurs.
   */

  public final boolean componentContainsScreenRelativePoint(
    final @Nonnull PointReadable<ScreenRelative> point)
    throws ConstraintError
  {
    final Window w = this.componentGetWindow();
    assert w != null;

    final Point<ScreenRelative> wpos = w.windowGetPosition();
    final PointReadable<WindowRelative> rpos =
      PointConversion.screenToWindow(wpos, point);

    return this.componentContainsWindowRelativePoint(rpos);
  }

  /**
   * Return <code>true</code> iff this component contains the window-relative
   * point <code>point</code>.
   * 
   * @param point
   *          The window-relative point.
   * @throws ConstraintError
   *           Iff <code>point == null</code> or an internal constraint error
   *           occurs.
   */

  public final boolean componentContainsWindowRelativePoint(
    final @Nonnull PointReadable<WindowRelative> point)
  {
    final PointReadable<WindowRelative> wr =
      this.componentGetPositionWindowRelative();
    assert wr != null;

    final VectorReadable2I size = this.componentGetSize();

    final int wr_x0 = wr.getXI();
    final int wr_y0 = wr.getYI();
    final int wr_x1 = wr_x0 + size.getXI();
    final int wr_y1 = wr_y0 + size.getYI();
    final boolean in_x = (point.getXI() >= wr_x0) && (point.getXI() < wr_x1);
    final boolean in_y = (point.getYI() >= wr_y0) && (point.getYI() < wr_y1);

    return in_x && in_y;
  }

  /**
   * Detach this component from its parent.
   * 
   * @throws ConstraintError
   *           Iff this component does not have a parent.
   */

  public final void componentDetachFromParent()
    throws ConstraintError
  {
    Constraints.constrainNotNull(this.parent, "Component has parent");
    assert this.parent != null; // Findbugs!

    this.parent.children.remove(this);
    this.parent = null;
  }

  final void componentEventOnMouseClicked(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position,
    final int button,
    final @Nonnull Component actual)
    throws GUIException,
      ConstraintError
  {
    final Log log = context.contextGetComponentLog();
    log.debug("on-mouse-clicked: " + this);

    boolean consumed = true;
    if (this.componentIsEnabled()) {
      consumed =
        this.mouseListenerOnMouseClicked(context, position, button, actual);
    }

    if (consumed == false) {
      if (this.parent != null) {
        this.parent.componentEventOnMouseClicked(
          context,
          position,
          button,
          actual);
      }
    }
  }

  final void componentEventOnMouseHeld(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position_first,
    final @Nonnull PointReadable<ScreenRelative> position_now,
    final int button,
    final @Nonnull Component actual)
    throws GUIException,
      ConstraintError
  {
    final Log log = context.contextGetComponentLog();
    log.debug("on-mouse-held: " + this);

    boolean consumed = true;
    if (this.componentIsEnabled()) {
      consumed =
        this.mouseListenerOnMouseHeld(
          context,
          position_first,
          position_now,
          button,
          actual);
    }

    if (consumed == false) {
      if (this.parent != null) {
        this.parent.componentEventOnMouseHeld(
          context,
          position_first,
          position_now,
          button,
          actual);
      }
    }
  }

  final void componentEventOnMouseNoLongerOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position)
    throws GUIException,
      ConstraintError
  {
    final Log log = context.contextGetComponentLog();
    log.debug("on-mouse-no-longer-over: " + this);

    boolean consumed = true;
    if (this.componentIsEnabled()) {
      consumed = this.mouseListenerOnMouseNoLongerOver(context, position);
    }

    if (consumed == false) {
      if (this.parent != null) {
        this.parent.componentEventOnMouseNoLongerOver(context, position);
      }
    }
  }

  final void componentEventOnMouseOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position,
    final @Nonnull Component actual)
    throws GUIException,
      ConstraintError
  {
    final Log log = context.contextGetComponentLog();
    log.debug("on-mouse-over: " + this);

    boolean consumed = true;
    if (this.componentIsEnabled()) {
      consumed = this.mouseListenerOnMouseOver(context, position, actual);
    }

    if (consumed == false) {
      if (this.parent != null) {
        this.parent.componentEventOnMouseOver(context, position, actual);
      }
    }
  }

  final void componentEventOnMouseReleased(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> position,
    final int button,
    final @Nonnull Component actual)
    throws GUIException,
      ConstraintError
  {
    final Log log = context.contextGetComponentLog();
    log.debug("on-mouse-released: " + this);

    boolean consumed = true;
    if (this.componentIsEnabled()) {
      consumed =
        this.mouseListenerOnMouseReleased(context, position, button, actual);
    }

    if (consumed == false) {
      if (this.parent != null) {
        this.parent.componentEventOnMouseReleased(
          context,
          position,
          button,
          actual);
      }
    }
  }

  /**
   * Retrieve the smallest descendant component of this component that
   * contains the window-relative point <code>point</code>.
   * 
   * @param point
   *          The window-relative point.
   * @return The descendant component, or <code>null</code> iff there is no
   *         component that contains the given point.
   */

  public final @CheckForNull Component componentGetByWindowRelativePosition(
    final @Nonnull PointReadable<WindowRelative> point)
  {
    if (this.componentContainsWindowRelativePoint(point)) {
      for (final Component c : this.children) {
        final Component cg = c.componentGetByWindowRelativePosition(point);
        if (cg != null) {
          return cg;
        }
      }
      return this;
    }

    return null;
  }

  /**
   * Retrieve the set of immediate child components of this component. The set
   * is not modifiable.
   */

  public final @Nonnull Set<Component> componentGetChildren()
  {
    return Collections.unmodifiableSet(this.children);
  }

  /**
   * Retrieve the current height of this component.
   */

  public final int componentGetHeight()
  {
    return this.size_current.y;
  }

  /**
   * Retrieve this component's height resize behaviour.
   * 
   * @see Component#componentSetHeightResizeBehavior(ParentResizeBehavior)
   * @see ParentResizeBehavior
   */

  public final @Nonnull
    ParentResizeBehavior
    componentGetHeightResizeBehavior()
  {
    return this.resize_height_behavior;
  }

  /**
   * Retrieve the unique ID of this component.
   */

  public final @Nonnull Long componentGetID()
  {
    return this.id;
  }

  /**
   * Retrieve the maximum height to which this component can be resized.
   * 
   * @see Component#componentSetMinimumHeight(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final int componentGetMaximumHeight()
  {
    return this.size_maximum.getYI();
  }

  /**
   * Retrieve the maximum position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   */

  public final @Nonnull
    PointReadable<ParentRelative>
    componentGetMaximumPosition()
  {
    return this.position_maximum;
  }

  /**
   * Retrieve the maximum width to which this component can be resized.
   * 
   * @see Component#componentSetMinimumWidth(GUIContext, int)
   * @see Component#componentSetMaximumWidth(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final int componentGetMaximumWidth()
  {
    return this.size_maximum.getXI();
  }

  /**
   * Retrieve the maximum X position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentGetMinimumX()
   * @see Component#componentSetMinimumX(int)
   * @see Component#componentSetMaximumX(int)
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   */

  public final int componentGetMaximumX()
  {
    return this.position_maximum.getXI();
  }

  /**
   * Retrieve the maximum Y position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentGetMinimumY()
   * @see Component#componentSetMinimumY(int)
   * @see Component#componentSetMaximumY(int)
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   */

  public final int componentGetMaximumY()
  {
    return this.position_maximum.getYI();
  }

  /**
   * Retrieve the minimum height to which this component can be resized.
   * 
   * @see Component#componentSetMinimumHeight(GUIContext, int)
   * @see Component#componentSetMaximumHeight(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final int componentGetMinimumHeight()
  {
    return this.size_minimum.getYI();
  }

  /**
   * Retrieve the minimum position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   */

  public final @Nonnull
    PointReadable<ParentRelative>
    componentGetMinimumPosition()
  {
    return this.position_minimum;
  }

  /**
   * Retrieve the minimum width to which this component can be resized.
   * 
   * @see Component#componentSetMinimumWidth(GUIContext, int)
   * @see Component#componentSetMaximumWidth(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final int componentGetMinimumWidth()
  {
    return this.size_minimum.getXI();
  }

  /**
   * Retrieve the minimum X position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentGetMinimumX()
   * @see Component#componentSetMinimumX(int)
   * @see Component#componentSetMaximumX(int)
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   */

  public final int componentGetMinimumX()
  {
    return this.position_minimum.getXI();
  }

  /**
   * Retrieve the minimum Y position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentSetMinimumWidth(GUIContext, int)
   * @see Component#componentSetMaximumWidth(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final int componentGetMinimumY()
  {
    return this.position_minimum.getYI();
  }

  /**
   * Retrieve the parent of this component, iff it has one.
   */

  public final @CheckForNull Component componentGetParent()
  {
    return this.parent;
  }

  /**
   * Retrieve the position of this component relative to its parent.
   */

  public final @Nonnull
    PointReadable<ParentRelative>
    componentGetPositionParentRelative()
  {
    return this.position_current;
  }

  /**
   * Retrieve the position of this component, relative to the containing
   * window. The function returns <code>null</code> iff the component is not
   * currently inside a {@link Window}.
   */

  public final @CheckForNull
    PointReadable<WindowRelative>
    componentGetPositionWindowRelative()
  {
    if (this.componentGetWindow() != null) {
      final Point<WindowRelative> accum = new Point<WindowRelative>();
      return this.componentGetWindowRelativeInner(accum);
    }
    return null;
  }

  /**
   * Retrieve the current size of this component.
   */

  public final @Nonnull VectorReadable2I componentGetSize()
  {
    return this.size_current;
  }

  /**
   * Retrieve the current width of this component.
   */

  public final int componentGetWidth()
  {
    return this.size_current.x;
  }

  /**
   * Retrieve this component's width resize behaviour.
   * 
   * @see Component#componentSetWidthResizeBehavior(ParentResizeBehavior)
   * @see ParentResizeBehavior
   */

  public final @Nonnull
    ParentResizeBehavior
    componentGetWidthResizeBehavior()
  {
    return this.resize_width_behavior;
  }

  /**
   * Retrieve the {@link Window} that contains this component, if any.
   */

  public final @CheckForNull Window componentGetWindow()
  {
    return this.componentGetWindowInner();
  }

  private final @CheckForNull Window componentGetWindowInner()
  {
    if (this.window != null) {
      return this.window;
    }

    if (this.parent != null) {
      return this.parent.componentGetWindowInner();
    }

    return null;
  }

  private final @Nonnull
    PointReadable<WindowRelative>
    componentGetWindowRelativeInner(
      final @Nonnull Point<WindowRelative> accum)
  {
    accum.setXI(accum.getXI() + this.position_current.getXI());
    accum.setYI(accum.getYI() + this.position_current.getYI());

    if (this.parent == null) {
      return accum;
    }

    return this.parent.componentGetWindowRelativeInner(accum);
  }

  /**
   * Return <code>true</code> iff this component has a parent component.
   */

  public final boolean componentHasParent()
  {
    return this.parent != null;
  }

  /**
   * Returns <code>true</code> iff this component is an ancestor of
   * <code>child</code>.
   * 
   * @param child
   *          The possible ancestor.
   * @throws ConstraintError
   *           Iff <code>child == null</code>.
   */

  public final boolean componentIsAncestorOf(
    final @Nonnull Component child)
    throws ConstraintError
  {
    Constraints.constrainNotNull(child, "Child component");
    return this.componentIsAncestorOfInner(child);
  }

  private final boolean componentIsAncestorOfInner(
    final @Nonnull Component child)
    throws ConstraintError
  {
    if (this.componentIsParentOf(child)) {
      return true;
    }
    if (child.parent == null) {
      return false;
    }

    return this.componentIsAncestorOfInner(child.parent);
  }

  /**
   * Returns <code>true</code> iff this component is enabled.
   */

  public final boolean componentIsEnabled()
  {
    return this.enabled;
  }

  @SuppressWarnings("static-method") public final
    boolean
    componentIsFocused()
  {
    return true;
  }

  /**
   * Returns <code>true</code> iff this component is the immediate parent of
   * <code>child</code>.
   * 
   * @param child
   *          The possible child.
   * @throws ConstraintError
   *           Iff <code>child == null</code> or an internal constraint error
   *           occurs.
   */

  public final boolean componentIsParentOf(
    final @Nonnull Component child)
    throws ConstraintError
  {
    Constraints.constrainNotNull(child, "Child component");

    if (child.parent == this) {
      Constraints.constrainArbitrary(
        this.children.contains(child),
        "Child set contains child");
      return true;
    }

    return false;
  }

  /**
   * The rendering function implemented by all components. This function is
   * called after rendering descendants of the component. Most components will
   * not use this function.
   * 
   * @see Component#componentRenderPreDescendants(GUIContext)
   * @param context
   *          The GUI context.
   */

  public abstract void componentRenderPostDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException;

  /**
   * The rendering function implemented by all components. This function is
   * called prior to rendering descendants of the component. Most components
   * will only use this function for rendering.
   * 
   * @see Component#componentRenderPostDescendants(GUIContext)
   * @param context
   *          The GUI context.
   */

  public abstract void componentRenderPreDescendants(
    final @Nonnull GUIContext context)
    throws ConstraintError,
      GUIException;

  /**
   * Enable/disable this component, and all descendants of the component.
   * 
   * @throws ConstraintError
   */

  public final void componentSetEnabled(
    final boolean enabled_now)
    throws ConstraintError
  {
    this.enabled = enabled_now;

    for (final Component child : this.children) {
      child.componentSetEnabled(enabled_now);
    }
  }

  /**
   * Set how this component behaves when the height of its parent changes.
   * 
   * Iff <code>b == BEHAVIOR_RESIZE</code>, then when the parent's height
   * changes by <code>n</code> pixels, the height of this component will also
   * change by <code>n</code> pixels.
   * 
   * Iff <code>b == BEHAVIOR_MOVE</code>, then when the parent's height
   * changes by <code>n</code> pixels, the Y position of this component will
   * also change by <code>n</code> pixels.
   * 
   * Iff <code>b == BEHAVIOR_FIXED</code>, then when the parent's height
   * changes by <code>n</code> pixels, this component does nothing.
   * 
   * @see Component#componentGetHeightResizeBehavior()
   * @param b
   *          The resize behaviour.
   * @throws ConstraintError
   *           Iff <code>b == null</code>.
   */

  public final void componentSetHeightResizeBehavior(
    final @Nonnull ParentResizeBehavior b)
    throws ConstraintError
  {
    this.resize_height_behavior =
      Constraints.constrainNotNull(b, "Height resize behavior");
  }

  /**
   * Set the maximum height to which this component can be resized.
   * 
   * @param height
   * @throws ConstraintError
   *           Iff
   *           <code>componentGetMinimumHeight() < height < Integer.MAX_VALUE == false</code>
   *           .
   */

  public final void componentSetMaximumHeight(
    final @CheckForNull GUIContext context,
    final int height)
    throws ConstraintError
  {
    this.invariantCheckSetMaximumHeight(height);
    this.componentSetSize(context, this.componentGetSize());
  }

  /**
   * Set the maximum position to which this component can be moved.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>max_pos == null</code></li>
   *           <li>
   *           <code>this.componentGetMinimumPosition().getXI() < max_pos.getXI() < Integer.MAX_VALUE == false</code>
   *           </li>
   *           <li>
   *           <code>this.componentGetMinimumPosition().getYI() < max_pos.getYI() < Integer.MAX_VALUE == false</code>
   *           </li>
   *           </ul>
   * 
   */

  public final void componentSetMaximumPosition(
    final @Nonnull PointReadable<ParentRelative> max_pos)
    throws ConstraintError
  {
    Constraints.constrainNotNull(max_pos, "Maximum position");
    this.invariantCheckSetMaximumY(max_pos.getYI());
    this.invariantCheckSetMaximumX(max_pos.getXI());
    this.componentSetPositionParentRelative(this
      .componentGetPositionParentRelative());
  }

  /**
   * Set the maximum size to which this component can be resized.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>max_size == null</code></li>
   *           <li>
   *           <code>this.componentGetMinimumWidth() < max_pos.getXI() < Integer.MAX_VALUE == false</code>
   *           </li>
   *           <li>
   *           <code>this.componentGetMinimumHeight() < max_pos.getYI() < Integer.MAX_VALUE == false</code>
   *           </li>
   *           </ul>
   */

  public final void componentSetMaximumSize(
    final @CheckForNull GUIContext context,
    final @Nonnull VectorReadable2I max_size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(max_size, "Maximum size");
    this.invariantCheckSetMaximumHeight(max_size.getYI());
    this.invariantCheckSetMaximumWidth(max_size.getXI());
    this.componentSetSize(context, this.componentGetSize());
  }

  /**
   * Set the maximum width to which this component can be resized.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>
   *           <code>this.componentGetMinimumWidth() < width < Integer.MAX_VALUE == false</code>
   *           </li>
   *           </ul>
   * 
   * @see Component#componentSetMinimumWidth(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final void componentSetMaximumWidth(
    final @CheckForNull GUIContext context,
    final int width)
    throws ConstraintError
  {
    this.invariantCheckSetMaximumWidth(width);
    this.componentSetSize(context, this.componentGetSize());
  }

  /**
   * Set the maximum X position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentSetMinimumX(int)
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>this.componentGetMinimumX() < x < Integer.MAX_VALUE == false</code>
   *           .
   */

  public final void componentSetMaximumX(
    final int x)
    throws ConstraintError
  {
    this.invariantCheckSetMaximumX(x);
    this.componentSetPositionParentRelative(this
      .componentGetPositionParentRelative());
  }

  /**
   * Set the maximum Y position to which this component can be moved, in
   * parent-relative coordinates.
   * 
   * @see Component#componentSetMinimumY(int)
   * @see Component#componentSetMinimumPosition(PointReadable)
   * @see Component#componentSetMaximumPosition(PointReadable)
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>this.componentGetMinimumY() < y < Integer.MAX_VALUE == false</code>
   *           .
   */

  public final void componentSetMaximumY(
    final int y)
    throws ConstraintError
  {
    this.invariantCheckSetMaximumY(y);
    this.componentSetPositionParentRelative(this
      .componentGetPositionParentRelative());
  }

  /**
   * Set the minimum height to which this component can be resized.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>
   *           <code>0 < height < this.componentGetMaximumHeight() == false</code>
   *           </li>
   *           </ul>
   * 
   * @see Component#componentSetMinimumHeight(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final void componentSetMinimumHeight(
    final @CheckForNull GUIContext context,
    final int height)
    throws ConstraintError
  {
    this.invariantCheckSetMinimumHeight(height);
    this.componentSetSize(context, this.componentGetSize());
  }

  /**
   * Set the minimum position allowed for this component, in parent-relative
   * coordinates.
   * 
   * @throws ConstraintError
   *           If <code>minimum_position == null</code> or any of the
   *           constraints on {@link Component#componentSetMinimumY(int)} or
   *           {@link Component#componentSetMinimumX(int)} are violated.
   */

  public final void componentSetMinimumPosition(
    final @Nonnull PointReadable<ParentRelative> minimum_position)
    throws ConstraintError
  {
    Constraints.constrainNotNull(minimum_position, "Minimum position");
    this.invariantCheckSetMinimumX(minimum_position.getXI());
    this.invariantCheckSetMinimumY(minimum_position.getYI());
    this.componentSetPositionParentRelative(this
      .componentGetPositionParentRelative());
  }

  /**
   * Set the minimum size allowed for this component.
   * 
   * @throws ConstraintError
   *           If <code>minimum_size == null</code> or any of the constraints
   *           on {@link Component#componentSetMinimumHeight(GUIContext, int)}
   *           or {@link Component#componentSetMinimumWidth(GUIContext, int)}
   *           are violated.
   */

  public final void componentSetMinimumSize(
    final @CheckForNull GUIContext context,
    final @Nonnull VectorReadable2I minimum_size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(minimum_size, "Minimum size");
    this.invariantCheckSetMinimumWidth(minimum_size.getXI());
    this.invariantCheckSetMinimumHeight(minimum_size.getYI());
    this.componentSetSize(context, this.componentGetSize());
  }

  /**
   * Set the minimum width to which this component can be resized.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>0 < height <= this.componentGetMaximumWidth() == false</code>
   *           .
   * 
   * @see Component#componentSetMinimumHeight(GUIContext, int)
   * @see Component#componentSetMinimumSize(GUIContext, VectorReadable2I)
   * @see Component#componentSetMaximumSize(GUIContext, VectorReadable2I)
   */

  public final void componentSetMinimumWidth(
    final @CheckForNull GUIContext context,
    final int width)
    throws ConstraintError
  {
    this.invariantCheckSetMinimumWidth(width);
    this.componentSetSize(context, this.componentGetSize());
  }

  /**
   * Set the minimum X position for this component, in parent-relative
   * coordinates.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>Integer.MIN_VALUE <= x <= this.componentGetMaximumX() == false</code>
   *           .
   */

  public final void componentSetMinimumX(
    final int x)
    throws ConstraintError
  {
    this.invariantCheckSetMinimumX(x);
    this.componentSetPositionParentRelative(this
      .componentGetPositionParentRelative());
  }

  /**
   * Set the minimum Y position for this component, in parent-relative
   * coordinates.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>Integer.MIN_VALUE <= y <= this.componentGetMaximumY() == false</code>
   *           .
   */

  public final void componentSetMinimumY(
    final int y)
    throws ConstraintError
  {
    this.invariantCheckSetMinimumY(y);
    this.componentSetPositionParentRelative(this
      .componentGetPositionParentRelative());
  }

  /**
   * Set the parent-relative position of this component to
   * <code>new_position</code>. The given size will be clamped to the range
   * <code>[componentGetMinimumX() .. componentGetMaximumX(), componentGetMinimumY() .. componentGetMaximumY()]</code>
   * inclusive.
   * 
   * @param new_position
   *          The new component position.
   * @throws ConstraintError
   *           Iff <code>new_position == null</code>.
   */

  public final void componentSetPositionParentRelative(
    final @Nonnull PointReadable<ParentRelative> new_position)
    throws ConstraintError
  {
    Constraints.constrainNotNull(new_position, "Position");

    final int result_x =
      Math.min(
        Math.max(new_position.getXI(), this.position_minimum.getXI()),
        this.position_maximum.getXI());
    final int result_y =
      Math.min(
        Math.max(new_position.getYI(), this.position_minimum.getYI()),
        this.position_maximum.getYI());

    this.position_current.setXI(result_x);
    this.position_current.setYI(result_y);
    this.position_unrestricted.setXI(new_position.getXI());
    this.position_unrestricted.setYI(new_position.getYI());
  }

  /**
   * Set the size of this component to <code>new_size</code>. The given size
   * will be clamped to the range
   * <code>[componentGetMinimumWidth() .. componentGetMaximumWidth(), componentGetMinimumHeight() .. componentGetMaximumHeight()]</code>
   * inclusive.
   * 
   * @param new_size
   *          The new component size.
   * @throws ConstraintError
   *           Iff <code>new_size == null</code>.
   */

  public final void componentSetSize(
    final @CheckForNull GUIContext context,
    final @Nonnull VectorReadable2I new_size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(new_size, "Size");

    final int result_x =
      Math.min(
        Math.max(new_size.getXI(), this.size_minimum.x),
        this.size_maximum.x);
    final int result_y =
      Math.min(
        Math.max(new_size.getYI(), this.size_minimum.y),
        this.size_maximum.y);

    final VectorM2I delta = new VectorM2I();
    delta.x = result_x - this.size_current.x;
    delta.y = result_y - this.size_current.y;

    this.size_current.x = result_x;
    this.size_current.y = result_y;
    this.size_unrestricted.x = new_size.getXI();
    this.size_unrestricted.y = new_size.getYI();

    for (final Component child : this.children) {
      child.handleResizeBehaviour(context, delta);
    }
  }

  /**
   * Set how this component behaves when the width of its parent changes.
   * 
   * Iff <code>b == BEHAVIOR_RESIZE</code>, then when the parent's width
   * changes by <code>n</code> pixels, the width of this component will also
   * change by <code>n</code> pixels.
   * 
   * Iff <code>b == BEHAVIOR_MOVE</code>, then when the parent's width changes
   * by <code>n</code> pixels, the X position of this component will also
   * change by <code>n</code> pixels.
   * 
   * Iff <code>b == BEHAVIOR_FIXED</code>, then when the parent's width
   * changes by <code>n</code> pixels, this component does nothing.
   * 
   * @see Component#componentGetWidthResizeBehavior()
   * @param b
   *          The resize behaviour.
   * @throws ConstraintError
   *           Iff <code>b == null</code>.
   */

  public final void componentSetWidthResizeBehavior(
    final @Nonnull ParentResizeBehavior b)
    throws ConstraintError
  {
    this.resize_width_behavior =
      Constraints.constrainNotNull(b, "Width resize behavior");
  }

  protected final void componentSetWindowClosing()
  {
    final Window w = this.componentGetWindow();
    assert w != null;
    w.windowSetState(WindowState.WINDOW_WANT_CLOSE);
  }

  @Override public boolean equals(
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
    final Component other = (Component) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return true;
  }

  private final void handleResizeBehaviour(
    final @CheckForNull GUIContext context,
    final @Nonnull VectorReadable2I delta)
    throws ConstraintError
  {
    final VectorM2I previous = new VectorM2I(this.size_current);

    switch (this.resize_width_behavior) {
      case BEHAVIOR_FIXED:
        break;
      case BEHAVIOR_MOVE:
      {
        final int ux = this.position_unrestricted.getXI() + delta.getXI();
        final int cx =
          Math.max(
            this.position_minimum.getXI(),
            Math.min(this.position_maximum.getXI(), ux));

        this.position_unrestricted.setXI(ux);
        this.position_current.setXI(cx);
        break;
      }
      case BEHAVIOR_RESIZE:
      {
        this.size_unrestricted.x += delta.getXI();

        final int clamp_x =
          Math.max(
            this.size_minimum.x,
            Math.min(this.size_maximum.x, this.size_unrestricted.x));

        this.size_current.x = clamp_x;
        break;
      }
    }

    switch (this.resize_height_behavior) {
      case BEHAVIOR_FIXED:
        break;
      case BEHAVIOR_MOVE:
      {
        final int uy = this.position_unrestricted.getYI() + delta.getYI();
        final int cy =
          Math.max(
            this.position_minimum.getYI(),
            Math.min(this.position_maximum.getYI(), uy));

        this.position_unrestricted.setYI(uy);
        this.position_current.setYI(cy);
        break;
      }
      case BEHAVIOR_RESIZE:
      {
        this.size_unrestricted.y += delta.getYI();

        final int clamp_y =
          Math.max(
            this.size_minimum.y,
            Math.min(this.size_maximum.y, this.size_unrestricted.y));

        this.size_current.y = clamp_y;
        break;
      }
    }

    final VectorM2I new_delta = new VectorM2I();
    new_delta.x = this.size_current.x - previous.x;
    new_delta.y = this.size_current.y - previous.y;
    final boolean resized = (new_delta.x != 0) || (new_delta.y != 0);

    if (resized) {
      this.resizeListenerOnResize(
        context,
        previous,
        this.size_current,
        new_delta,
        this);

      for (final Component child : this.children) {
        child.handleResizeBehaviour(context, new_delta);
      }
    }
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  private final void invariantCheckSetMaximumHeight(
    final int h)
    throws ConstraintError
  {
    Constraints.constrainRange(
      h,
      this.componentGetMinimumHeight(),
      Integer.MAX_VALUE,
      "Maximum height");
    this.size_maximum.y = h;
  }

  private final void invariantCheckSetMaximumWidth(
    final int w)
    throws ConstraintError
  {
    Constraints.constrainRange(
      w,
      this.componentGetMinimumWidth(),
      Integer.MAX_VALUE,
      "Maximum width");
    this.size_maximum.x = w;
  }

  private final void invariantCheckSetMaximumX(
    final int x)
    throws ConstraintError
  {
    Constraints.constrainRange(
      x,
      this.componentGetMinimumX(),
      Integer.MAX_VALUE,
      "Maximum X");
    this.position_maximum.setXI(x);
  }

  private final void invariantCheckSetMaximumY(
    final int y)
    throws ConstraintError
  {
    Constraints.constrainRange(
      y,
      this.componentGetMinimumY(),
      Integer.MAX_VALUE,
      "Maximum Y");
    this.position_maximum.setYI(y);
  }

  private final void invariantCheckSetMinimumHeight(
    final int h)
    throws ConstraintError
  {
    Constraints.constrainRange(
      h,
      1,
      this.componentGetMaximumHeight() - 1,
      "Minimum height");
    this.size_minimum.y = h;
  }

  private final void invariantCheckSetMinimumWidth(
    final int w)
    throws ConstraintError
  {
    Constraints.constrainRange(
      w,
      1,
      this.componentGetMaximumWidth() - 1,
      "Minimum width");
    this.size_minimum.x = w;
  }

  private final void invariantCheckSetMinimumX(
    final int x)
    throws ConstraintError
  {
    Constraints.constrainRange(
      x,
      Integer.MIN_VALUE,
      this.componentGetMaximumX() - 1,
      "Minimum X");
    this.position_minimum.setXI(x);
  }

  private final void invariantCheckSetMinimumY(
    final int y)
    throws ConstraintError
  {
    Constraints.constrainRange(
      y,
      Integer.MIN_VALUE,
      this.componentGetMaximumY() - 1,
      "Minimum Y");
    this.position_minimum.setYI(y);
  }

  @Override public boolean mouseListenerOnMouseClicked(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    return false;
  }

  @Override public boolean mouseListenerOnMouseHeld(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position_initial,
    final @Nonnull PointReadable<ScreenRelative> mouse_position_current,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    return false;
  }

  @Override public boolean mouseListenerOnMouseNoLongerOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position)
    throws ConstraintError,
      GUIException
  {
    return false;
  }

  @Override public boolean mouseListenerOnMouseOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    return false;
  }

  @Override public boolean mouseListenerOnMouseReleased(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull Component actual)
    throws ConstraintError,
      GUIException
  {
    return false;
  }

  @Override public void resizeListenerOnResize(
    final @CheckForNull GUIContext context,
    final @Nonnull VectorReadable2I size_original,
    final @Nonnull VectorReadable2I size_end,
    final @Nonnull VectorReadable2I size_delta,
    final @Nonnull Component actual)
    throws ConstraintError
  {
    Constraints.constrainNotNull(size_original, "Original size");
    Constraints.constrainNotNull(size_end, "Current size");
    Constraints.constrainNotNull(size_delta, "Size delta");
    Constraints.constrainNotNull(actual, "Actual component");
  }
}
