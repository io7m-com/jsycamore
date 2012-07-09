package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.geometry.ParentRelative;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Utility functions for positioning components.
 */

public final class ComponentAlignment
{
  /**
   * Set the vertical position of <code>c</code> such that the bottom edge of
   * <code>c</code> is <code>distance</code> from the bottom of its parent
   * container. The horizontal position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerBottom(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final PointReadable<ParentRelative> position =
        c.componentGetPositionParentRelative();

      final VectorReadable2I parent_size = parent.componentGetSize();
      final VectorReadable2I size = c.componentGetSize();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(position.getXI());
      new_pos.setYI(parent_size.getYI() - (size.getYI() + distance));
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical position of <code>c</code> such that the bottom edge of
   * <code>c</code> is <code>distance</code> from the bottom of its parent
   * container, and set the horizontal position of <code>c</code> such that
   * the center of <code>c</code> is equal to that of its parent container.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerBottomCenter(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    ComponentAlignment.setPositionContainerCenter(c);
    ComponentAlignment.setPositionContainerBottom(c, distance);
  }

  /**
   * Set the vertical position of <code>c</code> such that the bottom edge of
   * <code>c</code> is <code>distance</code> from the bottom of its parent
   * container, and set the horizontal position of <code>c</code> such that
   * the left edge of <code>c</code> is <code>distance</code> from the left
   * edge of its parent container.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerBottomLeft(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final VectorReadable2I parent_size = parent.componentGetSize();
      final VectorReadable2I size = c.componentGetSize();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(distance);
      new_pos.setYI(parent_size.getYI() - (size.getYI() + distance));
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical position of <code>c</code> such that the bottom edge of
   * <code>c</code> is <code>distance</code> from the bottom of its parent
   * container, and set the horizontal position of <code>c</code> such that
   * the right edge of <code>c</code> is <code>distance</code> from the right
   * edge of its parent container.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerBottomRight(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final VectorReadable2I parent_size = parent.componentGetSize();
      final VectorReadable2I size = c.componentGetSize();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(parent_size.getXI() - (size.getXI() + distance));
      new_pos.setYI(parent_size.getYI() - (size.getYI() + distance));
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical and horizontal position of <code>c</code> such that the
   * center of <code>c</code> is equal to that of its parent container.
   * 
   * @param c
   *          The component.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerCenter(
    final @Nonnull Component c)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final VectorReadable2I size = c.componentGetSize();
      final int this_w2 = size.getXI() / 2;
      final int this_h2 = size.getYI() / 2;

      final VectorReadable2I parent_size = parent.componentGetSize();
      final int parent_w2 = parent_size.getXI() / 2;
      final int parent_h2 = parent_size.getYI() / 2;

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(parent_w2 - this_w2);
      new_pos.setYI(parent_h2 - this_h2);
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the horizontal position of <code>c</code> such that the left edge of
   * <code>c</code> is <code>distance</code> from the left edge of its parent
   * container. The vertical position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerLeft(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final PointReadable<ParentRelative> position =
        c.componentGetPositionParentRelative();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(distance);
      new_pos.setYI(position.getYI());
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the horizontal position of <code>c</code> such that the right edge of
   * <code>c</code> is <code>distance</code> from the right edge of its parent
   * container. The vertical position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerRight(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final PointReadable<ParentRelative> position =
        c.componentGetPositionParentRelative();
      final VectorReadable2I parent_size = parent.componentGetSize();
      final VectorReadable2I size = c.componentGetSize();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(parent_size.getXI() - (size.getXI() + distance));
      new_pos.setYI(position.getYI());
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical position of <code>c</code> such that the top edge of
   * <code>c</code> is <code>distance</code> from the top edge of its parent
   * container. The horizontal position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerTop(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final PointReadable<ParentRelative> position =
        c.componentGetPositionParentRelative();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(position.getXI());
      new_pos.setYI(distance);
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical position of <code>c</code> such that the top edge of
   * <code>c</code> is <code>distance</code> from the top edge of its parent
   * container, and set the horizontal position of <code>c</code> such that
   * the center of <code>c</code> is equal to that of its parent container.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerTopCenter(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    ComponentAlignment.setPositionContainerCenter(c);
    ComponentAlignment.setPositionContainerTop(c, distance);
  }

  /**
   * Set the vertical position of <code>c</code> such that the top edge of
   * <code>c</code> is <code>distance</code> from the top edge of its parent
   * container, and set the horizontal position of <code>c</code> such that
   * the left edge of <code>c</code> is <code>distance</code> from the left
   * edge of its parent container.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerTopLeft(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(distance);
      new_pos.setYI(distance);
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical position of <code>c</code> such that the top edge of
   * <code>c</code> is <code>distance</code> from the top edge of its parent
   * container, and set the horizontal position of <code>c</code> such that
   * the right edge of <code>c</code> is <code>distance</code> from the right
   * edge of its parent container.
   * 
   * @param c
   *          The component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setPositionContainerTopRight(
    final @Nonnull Component c,
    final int distance)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final VectorReadable2I parent_size = parent.componentGetSize();
      final VectorReadable2I size = c.componentGetSize();

      final Point<ParentRelative> new_pos = new Point<ParentRelative>();
      new_pos.setXI(parent_size.getXI() - (size.getXI() + distance));
      new_pos.setYI(distance);
      c.componentSetPositionParentRelative(new_pos);
    }
  }

  /**
   * Set the vertical position of <code>c0</code> such that the bottom edge of
   * <code>c0</code> is <code>distance</code> from the top edge of
   * <code>c1</code>. The horizontal position of <code>c0</code> is
   * unaffected.
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeAbove(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final PointReadable<ParentRelative> c0_pos =
      c0.componentGetPositionParentRelative();
    final VectorReadable2I c0_siz = c0.componentGetSize();
    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c0_pos.getXI());
    new_pos.setYI((c1_pos.getYI() - c0_siz.getYI()) - distance);

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the vertical position of <code>c0</code> such that the bottom edge of
   * <code>c0</code> is <code>distance</code> from the top edge of
   * <code>c1</code>, and set the horizontal position of <code>c0</code> such
   * that the left edge of <code>c0</code> is equal to that of <code>c1</code>
   * .
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeAboveSameX(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final VectorReadable2I c0_siz = c0.componentGetSize();
    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c1_pos.getXI());
    new_pos.setYI((c1_pos.getYI() - c0_siz.getYI()) - distance);

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the vertical position of <code>c0</code> such that the top edge of
   * <code>c0</code> is <code>distance</code> from the bottom edge of
   * <code>c1</code>. The horizontal position of <code>c0</code> is
   * unaffected.
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeBelow(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final PointReadable<ParentRelative> c0_pos =
      c0.componentGetPositionParentRelative();
    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();
    final VectorReadable2I c1_siz = c1.componentGetSize();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c0_pos.getXI());
    new_pos.setYI(c1_pos.getYI() + c1_siz.getYI() + distance);

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the vertical position of <code>c0</code> such that the top edge of
   * <code>c0</code> is <code>distance</code> from the bottom edge of
   * <code>c1</code>, and set the horizontal position of <code>c0</code> such
   * that the left edge of <code>c0</code> is equal to that of <code>c1</code>
   * .
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeBelowSameX(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();
    final VectorReadable2I c1_siz = c1.componentGetSize();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c1_pos.getXI());
    new_pos.setYI(c1_pos.getYI() + c1_siz.getYI() + distance);

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the horizontal position of <code>c0</code> such that the right edge
   * of <code>c0</code> is <code>distance</code> from the left edge of
   * <code>c1</code>. The vertical position of <code>c0</code> is unaffected.
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeLeftOf(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final PointReadable<ParentRelative> c0_pos =
      c0.componentGetPositionParentRelative();
    final VectorReadable2I c0_siz = c0.componentGetSize();
    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c1_pos.getXI() - (c0_siz.getXI() + distance));
    new_pos.setYI(c0_pos.getYI());

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the horizontal position of <code>c0</code> such that the right edge
   * of <code>c0</code> is <code>distance</code> from the left edge of
   * <code>c1</code>, and set the vertical position of <code>c0</code> such
   * that the top edge of <code>c0</code> is equal to that of <code>c1</code>.
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeLeftOfSameY(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final VectorReadable2I c0_siz = c0.componentGetSize();
    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c1_pos.getXI() - (c0_siz.getXI() + distance));
    new_pos.setYI(c1_pos.getYI());

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the horizontal position of <code>c0</code> such that the left edge of
   * <code>c0</code> is <code>distance</code> from the right edge of
   * <code>c1</code>. The vertical position of <code>c0</code> is unaffected.
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeRightOf(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final PointReadable<ParentRelative> c0_pos =
      c0.componentGetPositionParentRelative();
    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();
    final VectorReadable2I c1_siz = c1.componentGetSize();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c1_pos.getXI() + c1_siz.getXI() + distance);
    new_pos.setYI(c0_pos.getYI());

    c0.componentSetPositionParentRelative(new_pos);
  }

  /**
   * Set the horizontal position of <code>c0</code> such that the left edge of
   * <code>c0</code> is <code>distance</code> from the right edge of
   * <code>c1</code>, and set the vertical position of <code>c0</code> such
   * that the top edge of <code>c0</code> is equal to that of <code>c1</code>.
   * 
   * @param c0
   *          The component to position.
   * @param c1
   *          The other component.
   * @param distance
   *          The offset in pixels.
   * @throws ConstraintError
   *           Iff <code>c0 == null || c1 == null</code>.
   */

  public static void setPositionRelativeRightOfSameY(
    final @Nonnull Component c0,
    final int distance,
    final @Nonnull Component c1)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c0, "Component 0");
    Constraints.constrainNotNull(c1, "Component 1");

    final PointReadable<ParentRelative> c1_pos =
      c1.componentGetPositionParentRelative();
    final VectorReadable2I c1_siz = c1.componentGetSize();

    final Point<ParentRelative> new_pos = new Point<ParentRelative>();
    new_pos.setXI(c1_pos.getXI() + c1_siz.getXI() + distance);
    new_pos.setYI(c1_pos.getYI());

    c0.componentSetPositionParentRelative(new_pos);
  }

  private ComponentAlignment()
  {
    throw new AssertionError("unreachable code: report this bug!");
  }
}
