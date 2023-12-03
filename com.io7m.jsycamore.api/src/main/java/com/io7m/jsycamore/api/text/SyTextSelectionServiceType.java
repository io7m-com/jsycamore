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

import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.services.SyServiceType;

import java.util.Objects;

/**
 * <p>The text selection service.</p>
 * <p>The text selection service tracks the selected text on a given screen.</p>
 */

public interface SyTextSelectionServiceType
  extends SyServiceType
{
  /**
   * Clear the screen's current text selection.
   */

  void selectionClear();

  /**
   * Set the screen's current text selection, stating that {@code selection}
   * is owned by the given {@code component}.
   *
   * @param selection The selection
   * @param component The component that owns the selection
   */

  void selectionSet(
    SyComponentReadableType component,
    SyTextSelection selection
  );

  /**
   * The type of component-owned text selections
   */

  sealed interface SyTextSelectionStatusType
  {

  }

  /**
   * A piece of text is selected in the given component.
   *
   * @param component The component
   * @param selection The selection
   */

  record SyTextSelectionIsSelected(
    SyComponentReadableType component,
    SyTextSelection selection)
    implements SyTextSelectionStatusType
  {
    /**
     * A piece of text is selected in the given component.
     */

    public SyTextSelectionIsSelected
    {
      Objects.requireNonNull(component, "component");
      Objects.requireNonNull(selection, "selection");
    }
  }

  /**
   * No text is selected.
   */

  enum SyTextSelectionNotSelected implements SyTextSelectionStatusType
  {
    /**
     * No text is selected.
     */

    NOT_SELECTED
  }

  /**
   * Determine if the given component has text selected.
   *
   * @param component The component
   *
   * @return The selection status
   */

  SyTextSelectionStatusType isComponentSelected(
    SyComponentReadableType component);

  /**
   * Clear the selection if the given component is the component that has
   * text selected.
   *
   * @param component The component
   *
   * @return {@code true} if a selection was cleared
   */

  default boolean selectionClearIfComponentSelected(
    final SyComponentReadableType component)
  {
    return switch (this.isComponentSelected(component)) {
      case final SyTextSelectionIsSelected c -> {
        this.selectionClear();
        yield true;
      }
      case final SyTextSelectionNotSelected c -> {
        yield false;
      }
    };
  }
}
