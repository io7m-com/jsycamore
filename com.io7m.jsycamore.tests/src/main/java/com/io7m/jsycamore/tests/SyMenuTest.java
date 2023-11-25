/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.tests;

import com.io7m.jorchard.core.JOTreeExceptionCycle;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.SyMenu;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_LEFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SyMenuTest extends SyComponentContract<SyMenu>
{
  private static final PVector2I<SySpaceViewportType> Z = PVectors2I.zero();

  @BeforeEach
  public void menuSetup()
  {

  }

  /**
   * Basic menu configuration works.
   */

  @Test
  public void testMenuConfiguration()
  {
    final var clicks = new AtomicInteger(0);

    final var sc = this.newComponent();
    final var si0 =
      sc.addAtom("A", clicks::incrementAndGet);
    final var si1 =
      sc.addAtom("B", clicks::incrementAndGet);

    assertEquals(sc, si0.menu());
    assertEquals(sc, si1.menu());

    final var c = this.newComponent();

    final var i0 =
      c.addAtom("Item 0", clicks::incrementAndGet);
    final var i1 =
      c.addSeparator();
    final var i2 =
      c.addAtom("Item 1", clicks::incrementAndGet);
    final var i3 =
      c.addSubmenu("Item 2", sc);

    assertEquals(c, i0.menu());
    assertEquals(c, i1.menu());
    assertEquals(c, i2.menu());
    assertEquals(c, i3.menu());

    assertEquals(
      EVENT_CONSUMED,
      i0.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, i0))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      i1.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, i1))
    );
    assertEquals(
      EVENT_CONSUMED,
      i2.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, i2))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      i3.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, i3))
    );

    assertEquals(c, c.menuNode().value());
    assertEquals(c, c.menuNodeReadable().value());
    assertEquals(2, clicks.get());
  }

  /**
   * Menus cannot be made cyclic.
   */

  @Test
  public void testMenuAcyclic()
  {
    final var menu0 = new SyMenu();
    final var menu1 = new SyMenu();
    final var item1 = menu0.addSubmenu("Menu 1", menu1);
    assertEquals(menu0, item1.menu());
    assertEquals(menu1, item1.submenu());

    assertThrows(JOTreeExceptionCycle.class, () -> {
      menu1.addSubmenu("Menu 0", menu0);
    });
  }

  /**
   * A menu doesn't accept window events.
   */

  @Test
  public void testWindowEvents()
  {
    final var c = this.newComponent();
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyWindowClosed(new SyWindowID(UUID.randomUUID())))
    );
  }

  @Override
  protected SyMenu newComponent()
  {
    return new SyMenu();
  }
}
