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


package com.io7m.jsycamore.api.text;

import java.util.Objects;

/**
 * A mouse interaction with a text view.
 *
 * @param interactionKind The kind of interaction
 * @param locationStart   The location at the start of the interaction
 * @param locationNow     The location now
 */

public record SyTextMouseInteraction(
  Kind interactionKind,
  SyTextLocationType locationStart,
  SyTextLocationType locationNow)
{
  /**
   * A mouse interaction with a text view.
   *
   * @param interactionKind The kind of interaction
   * @param locationStart   The location at the start of the interaction
   * @param locationNow     The location now
   */

  public SyTextMouseInteraction
  {
    Objects.requireNonNull(interactionKind, "interactionKind");
    Objects.requireNonNull(locationStart, "locationStart");
    Objects.requireNonNull(locationNow, "locationNow");
  }

  /**
   * The kind of interaction.
   */

  public enum Kind
  {
    /**
     * The mouse was pressed.
     */

    MOUSE_PRESSED,

    /**
     * The mouse was dragged.
     */

    MOUSE_DRAGGED,

    /**
     * The mouse was released.
     */

    MOUSE_RELEASED
  }
}
