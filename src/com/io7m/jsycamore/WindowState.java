package com.io7m.jsycamore;

/**
 * The state of any given window.
 */

public enum WindowState
{
  /** The window is open and is the frontmost window. */
  WINDOW_FOCUSED,
  /** The window is closed, but will be open before the next frame. */
  WINDOW_WANT_OPEN,
  /** The window is open. */
  WINDOW_OPEN,
  /** The window is open, but will be closed before the next frame. */
  WINDOW_WANT_CLOSE,
  /** The window is closed. */
  WINDOW_CLOSED
}
