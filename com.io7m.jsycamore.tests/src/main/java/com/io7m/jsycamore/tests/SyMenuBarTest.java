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
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.SyMenu;
import com.io7m.jsycamore.components.standard.SyMenuBar;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.text.SyText.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SyMenuBarTest extends SyComponentContract<SyMenuBar>
{
  private static final PVector2I<SySpaceViewportType> Z = PVectors2I.zero();

  @BeforeEach
  public void menuSetup()
  {

  }

  /**
   * Menus cannot be made cyclic.
   */

  @Test
  public void testMenuAcyclic()
  {
    final var menu0 = new SyMenu(this.screen());
    final var menu1 = new SyMenu(this.screen());
    final var item1 = menu0.addSubmenu(text("Menu 1"), menu1);
    assertEquals(menu0, item1.menu());
    assertEquals(menu1, item1.submenu());

    assertThrows(JOTreeExceptionCycle.class, () -> {
      menu1.addSubmenu(text("Menu 0"), menu0);
    });
  }

  @Override
  protected SyMenuBar newComponent()
  {
    return new SyMenuBar(this.screen());
  }
}
