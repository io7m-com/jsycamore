/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.components;

import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.api.bounded.SyBoundedReadableType;
import com.io7m.jsycamore.api.mouse.SyMouseAcceptingReadableType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeableReadableType;
import com.io7m.jsycamore.api.visibility.SyVisibleReadableType;
import com.io7m.jsycamore.api.windows.SyWindowReadableType;

import java.util.Optional;

/**
 * The type of readable components.
 */

public interface SyComponentReadableType
  extends SyActiveReadableType,
  SyVisibleReadableType,
  SyMouseAcceptingReadableType,
  SyBoundedReadableType<SySpaceParentRelativeType>,
  SyThemeableReadableType
{
  /**
   * @return The window to which this component belongs
   */

  Optional<SyWindowReadableType> windowReadable();

  /**
   * @return The tree node for this component
   */

  JOTreeNodeReadableType<SyComponentReadableType> nodeReadable();

  @Override
  default boolean isVisible()
  {
    /*
     * If a component is set to invisible, it is unconditionally invisible.
     * Otherwise, it is visible if its parent is visible. If there is no
     * parent, the component is visible.
     */

    return switch (this.visibility().get()) {
      case VISIBILITY_INVISIBLE -> false;
      case VISIBILITY_VISIBLE -> {
        final var parentOpt = this.nodeReadable().parentReadable();
        if (parentOpt.isPresent()) {
          final var parent = parentOpt.get().value();
          yield parent.isVisible();
        }
        yield true;
      }
    };
  }

  @Override
  default boolean isActive()
  {
    /*
     * If a component is set to inactive, it is unconditionally inactive.
     * Otherwise, it is active if its parent is active. If there is no
     * parent, the component is active.
     */

    return switch (this.activity().get()) {
      case INACTIVE -> false;
      case ACTIVE -> {
        final var parentOpt = this.nodeReadable().parentReadable();
        if (parentOpt.isPresent()) {
          final var parent = parentOpt.get().value();
          yield parent.isActive();
        }
        yield true;
      }
    };
  }

  /**
   * A convenience method to return the number of children of this component.
   *
   * @return The number of children
   */

  default int childCount()
  {
    return this.nodeReadable().childrenReadable().size();
  }
}
