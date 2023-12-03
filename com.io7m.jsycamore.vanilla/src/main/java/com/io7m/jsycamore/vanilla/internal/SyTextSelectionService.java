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


package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.services.SyServiceAbstract;
import com.io7m.jsycamore.api.text.SyTextSelection;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.io7m.jsycamore.api.text.SyTextSelectionServiceType.SyTextSelectionNotSelected.NOT_SELECTED;

/**
 * The text selection service.
 */

public final class SyTextSelectionService
  extends SyServiceAbstract
  implements SyTextSelectionServiceType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyTextSelectionService.class);

  private SyTextSelectionStatusType selection;

  /**
   * The text selection service.
   */

  public SyTextSelectionService()
  {

  }

  @Override
  public void selectionClear()
  {
    this.selection = null;
  }

  @Override
  public void selectionSet(
    final SyComponentReadableType newComponent,
    final SyTextSelection newSelection)
  {
    Objects.requireNonNull(newComponent, "component");
    Objects.requireNonNull(newSelection, "selection");

    this.selection = new SyTextSelectionIsSelected(
      newComponent,
      newSelection
    );
  }

  @Override
  public SyTextSelectionStatusType isComponentSelected(
    final SyComponentReadableType component)
  {
    return switch (this.selection) {
      case final SyTextSelectionIsSelected isSelected -> {
        if (Objects.equals(isSelected.component(), component)) {
          yield isSelected;
        } else {
          yield NOT_SELECTED;
        }
      }
      case final SyTextSelectionNotSelected not -> {
        yield NOT_SELECTED;
      }
      case null -> {
        yield NOT_SELECTED;
      }
    };
  }

  @Override
  public String description()
  {
    return "The text selection service.";
  }
}
