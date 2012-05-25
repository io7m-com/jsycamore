package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.geometry.PointReadable;
import com.io7m.jsycamore.geometry.ScreenRelative;

public interface MouseListener<T>
{
  /**
   * Called when a mouse button is clicked.
   * 
   * @param context
   *          The GUI context.
   * @param mouse_position
   *          The current cursor position.
   * @param button
   *          The index of the pressed button.
   * @param actual
   *          The component that received the message originally.
   * @return <code>true</code> if this component has consumed the message.
   * @throws ConstraintError
   * @throws GUIException
   */

  boolean mouseListenerOnMouseClicked(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final T actual)
    throws ConstraintError,
      GUIException;

  /**
   * Called when the mouse button is held.
   * 
   * @param context
   *          The GUI context.
   * @param mouse_position_initial
   *          The initial position of the mouse cursor.
   * @param mouse_position_current
   *          The current position of the mouse cursor.
   * @param button
   *          The index of the pressed button.
   * @param actual
   *          The component that received the message originally.
   * @return <code>true</code> if this component has consumed the message.
   * @throws ConstraintError
   * @throws GUIException
   */

  boolean mouseListenerOnMouseHeld(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position_initial,
    final @Nonnull PointReadable<ScreenRelative> mouse_position_current,
    final int button,
    final @Nonnull T actual)
    throws ConstraintError,
      GUIException;

  /**
   * Called when the mouse cursor is no longer over the component.
   * 
   * @param context
   *          The GUI context.
   * @param mouse_position
   *          The current mouse cursor position.
   * @return <code>true</code> if this component has consumed the message.
   * @throws ConstraintError
   * @throws GUIException
   */

  boolean mouseListenerOnMouseNoLongerOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position)
    throws ConstraintError,
      GUIException;

  /**
   * Called when the mouse cursor moves over this component.
   * 
   * @param context
   *          The GUI context.
   * @param mouse_position
   *          The current mouse cursor position.
   * @param actual
   *          The component that received the message initially.
   * @return <code>true</code> if this component has consumed the message.
   * @throws ConstraintError
   * @throws GUIException
   */

  boolean mouseListenerOnMouseOver(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final @Nonnull T actual)
    throws ConstraintError,
      GUIException;

  /**
   * Called when the mouse button is released, having been previously pressed.
   * 
   * @param context
   *          The GUI context.
   * @param mouse_position
   *          The current mouse cursor position.
   * @param button
   *          The button pressed.
   * @param actual
   *          The component that received the message initially.
   * @return <code>true</code> if this component has consumed the message.
   * @throws ConstraintError
   * @throws GUIException
   */

  boolean mouseListenerOnMouseReleased(
    final @Nonnull GUIContext context,
    final @Nonnull PointReadable<ScreenRelative> mouse_position,
    final int button,
    final @Nonnull T actual)
    throws ConstraintError,
      GUIException;
}
