/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package com.io7m.jsycamore.api.keyboard;

/**
 * A specification of how keyboard focus may traverse a component.
 */

public enum SyKeyboardFocusBehavior
{
  /**
   * The component can directly receive focus, and will pass focus to its
   * children if the focus traversal is continued.
   */

  RECEIVES_FOCUS_AND_CONTINUES_TRAVERSAL,

  /**
   * The component can directly receive focus, but will not pass focus to its
   * children if the focus traversal is continued.
   */

  RECEIVES_FOCUS_AND_STOPS_TRAVERSAL,

  /**
   * The component cannot directly receive focus, but will pass focus to its
   * children if the focus traversal is continued.
   */

  IGNORES_FOCUS_AND_CONTINUES_TRAVERSAL,

  /**
   * The component cannot directly receive focus, and will not pass focus to its
   * children if the focus traversal is continued.
   */

  IGNORES_FOCUS_AND_STOPS_TRAVERSAL;

  /**
   * @return {@code true} if this behavior implies that a component will
   * receive focus
   */

  public boolean receivesFocus()
  {
    return switch (this) {
      case RECEIVES_FOCUS_AND_CONTINUES_TRAVERSAL -> true;
      case RECEIVES_FOCUS_AND_STOPS_TRAVERSAL -> true;
      case IGNORES_FOCUS_AND_CONTINUES_TRAVERSAL -> false;
      case IGNORES_FOCUS_AND_STOPS_TRAVERSAL -> false;
    };
  }

  /**
   * @return {@code true} if this behavior implies that a component will pass
   * focus to its children
   */

  public boolean continuesFocusTraversal()
  {
    return switch (this) {
      case RECEIVES_FOCUS_AND_CONTINUES_TRAVERSAL -> true;
      case RECEIVES_FOCUS_AND_STOPS_TRAVERSAL -> false;
      case IGNORES_FOCUS_AND_CONTINUES_TRAVERSAL -> true;
      case IGNORES_FOCUS_AND_STOPS_TRAVERSAL -> false;
    };
  }
}
