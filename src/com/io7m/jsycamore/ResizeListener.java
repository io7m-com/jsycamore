package com.io7m.jsycamore;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Interface describing an object that can receive "resize" events.
 * 
 * @param <T>
 *          The type of object that received the initial event (not
 *          necessarily the same type of object as the one receiving the event
 *          now).
 */

public interface ResizeListener<T>
{
  /**
   * Called when the component is resized.
   * 
   * @param context
   *          The GUI context.
   * @param size_original
   *          The original size of the object.
   * @param size_end
   *          The current size of the object.
   * @param size_delta
   *          The difference between the current and initial size.
   * @param actual
   *          The component that received the message originally.
   * @throws ConstraintError
   * @throws GUIException
   */

  void resizeListenerOnResize(
    final @Nonnull GUIContext context,
    final @Nonnull VectorReadable2I size_original,
    final @Nonnull VectorReadable2I size_end,
    final @Nonnull VectorReadable2I size_delta,
    final @Nonnull T actual)
    throws ConstraintError,
      GUIException;
}
