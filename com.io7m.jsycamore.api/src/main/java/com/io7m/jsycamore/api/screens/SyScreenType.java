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

package com.io7m.jsycamore.api.screens;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.menus.SyMenuServiceType;
import com.io7m.jsycamore.api.services.SyServiceDirectoryReadableType;
import com.io7m.jsycamore.api.sized.SySizedType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowServiceType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.util.Optional;
import java.util.concurrent.Flow.Publisher;

/**
 * The type of user interfaces.
 */

public interface SyScreenType
  extends SyScreenMouseEventsType,
  SyScreenKeyEventsType,
  SySizedType<SySpaceViewportType>,
  AutoCloseable
{
  /**
   * @return Access to the services for the screen
   */

  SyServiceDirectoryReadableType services();

  /**
   * @return Access to the (mandatory) window service
   */

  default SyWindowServiceType windowService()
  {
    return this.services().requireService(SyWindowServiceType.class);
  }

  /**
   * @return Access to the (mandatory) menu service
   */

  default SyMenuServiceType menuService()
  {
    return this.services().requireService(SyMenuServiceType.class);
  }

  /**
   * @return Access to the (mandatory) text selection service
   */

  default SyTextSelectionServiceType textSelectionService()
  {
    return this.services().requireService(SyTextSelectionServiceType.class);
  }

  /**
   * @return The theme context
   */

  SyThemeContextType themeContext();

  /**
   * @return The current theme used by the screen
   */

  SyThemeType theme();

  /**
   * @return The component that is currently underneath the mouse pointer, if
   * any
   */

  Optional<SyComponentType> componentOver();

  /**
   * @return The stream of events for the screen
   */

  Publisher<SyEventType> events();

  /**
   * Update the screen, executing a layout pass and updating any animating
   * elements.
   */

  void update();

  /**
   * @return An attribute that exposes the most recently published mouse
   * position
   */

  AttributeReadableType<PVector2I<SySpaceViewportType>> mousePosition();

  @Override
  void close()
    throws RuntimeException;
}
