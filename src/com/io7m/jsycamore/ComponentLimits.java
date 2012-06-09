package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

public final class ComponentLimits
{
  /**
   * Set the maximum allowed X position of <code>c</code> such that the X
   * position of the right edge of <code>c</code> will always be less than or
   * equal to the X position of the right edge of its parent. The current
   * position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component to adjust
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setMaximumXContainerRight(
    final @Nonnull Component c)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final int parent_w = parent.componentGetWidth();
      final int current_w = c.componentGetWidth();
      c.componentSetMaximumX(parent_w - current_w);
    }
  }

  /**
   * Set the minimum and maximum X and Y positions for <code>c</code> such
   * that the component may not overlap any of the sides of its container. The
   * current position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component to adjust
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setMaximumXYContainer(
    final @Nonnull Component c)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final int parent_w = parent.componentGetWidth();
      final int parent_h = parent.componentGetHeight();
      final int current_h = c.componentGetHeight();
      final int current_w = c.componentGetWidth();
      final int max_y = parent_h - current_h;
      final int max_x = parent_w - current_w;

      c.componentSetMinimumX(0);
      c.componentSetMinimumY(0);

      if (max_y > 0) {
        c.componentSetMaximumY(max_y);
      }
      if (max_x > 0) {
        c.componentSetMaximumX(max_x);
      }
    }
  }

  /**
   * Set the maximum allowed Y position of <code>c</code> such that the Y
   * position of the bottom edge of <code>c</code> will always be less than or
   * equal to the Y position of the bottom edge of its parent. The current
   * position of <code>c</code> is unaffected.
   * 
   * @param c
   *          The component to adjust
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public static void setMaximumYContainerBottom(
    final @Nonnull Component c)
    throws ConstraintError
  {
    Constraints.constrainNotNull(c, "Component");

    final Component parent = c.componentGetParent();
    if (parent != null) {
      final int parent_h = parent.componentGetHeight();
      final int current_h = c.componentGetHeight();
      c.componentSetMaximumY(parent_h - current_h);
    }
  }
}
