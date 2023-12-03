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

import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.forms.SyFormRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.components.standard.buttons.SyButton.button;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnSizeType.exact;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnSizeType.flexible;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnsConfiguration.columns;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyFormRowTest extends SyComponentContract<SyFormRow>
{
  @BeforeEach
  public void rowSetup()
  {

  }

  /**
   * A simple fixed and flexible layout gives the expected values.
   */

  @Test
  public void testLayoutSimple()
  {
    final var layout = this.newComponent();

    final var c0 = button(this.screen());
    final var c1 = button(this.screen());
    final var c2 = button(this.screen());

    layout.childAdd(c0);
    layout.childAdd(c1);
    layout.childAdd(c2);
    layout.layout(
      this.layoutContext,
      new SyConstraints(
        0,
        0,
        100 + 32 + 16,
        24
      ));

    assertEquals(100 + 32 + 16, layout.size().get().sizeX());
    assertEquals(24, layout.size().get().sizeY());

    assertEquals(32, c0.size().get().sizeX());
    assertEquals(100, c1.size().get().sizeX());
    assertEquals(16, c2.size().get().sizeX());

    assertEquals(24, c0.size().get().sizeY());
    assertEquals(24, c1.size().get().sizeY());
    assertEquals(24, c2.size().get().sizeY());

    layout.layout(
      this.layoutContext,
      new SyConstraints(
        0,
        0,
        200 + 32 + 16,
        48
      ));

    assertEquals(200 + 32 + 16, layout.size().get().sizeX());
    assertEquals(48, layout.size().get().sizeY());

    assertEquals(32, c0.size().get().sizeX());
    assertEquals(200, c1.size().get().sizeX());
    assertEquals(16, c2.size().get().sizeX());

    assertEquals(48, c0.size().get().sizeY());
    assertEquals(48, c1.size().get().sizeY());
    assertEquals(48, c2.size().get().sizeY());
  }

  /**
   * A layout with entirely fixed columns works.
   */

  @Test
  public void testLayoutAllFixed()
  {
    final var layout =
      new SyFormRow(
        this.screen(),
        columns(
          exact(32),
          exact(100),
          exact(16)
        ));

    final var c0 = button(this.screen());
    final var c1 = button(this.screen());
    final var c2 = button(this.screen());

    layout.childAdd(c0);
    layout.childAdd(c1);
    layout.childAdd(c2);
    layout.layout(
      this.layoutContext,
      new SyConstraints(
        0,
        0,
        100 + 32 + 16,
        24
      ));

    assertEquals(100 + 32 + 16, layout.size().get().sizeX());
    assertEquals(24, layout.size().get().sizeY());

    assertEquals(32, c0.size().get().sizeX());
    assertEquals(100, c1.size().get().sizeX());
    assertEquals(16, c2.size().get().sizeX());

    assertEquals(24, c0.size().get().sizeY());
    assertEquals(24, c1.size().get().sizeY());
    assertEquals(24, c2.size().get().sizeY());

    layout.layout(
      this.layoutContext,
      new SyConstraints(
        0,
        0,
        200 + 32 + 16,
        48
      ));

    assertEquals(200 + 32 + 16, layout.size().get().sizeX());
    assertEquals(48, layout.size().get().sizeY());

    assertEquals(32, c0.size().get().sizeX());
    assertEquals(100, c1.size().get().sizeX());
    assertEquals(16, c2.size().get().sizeX());

    assertEquals(48, c0.size().get().sizeY());
    assertEquals(48, c1.size().get().sizeY());
    assertEquals(48, c2.size().get().sizeY());
  }

  /**
   * A layout with entirely flexible columns works.
   */

  @Test
  public void testLayoutAllFlexible()
  {
    final var layout =
      new SyFormRow(
        this.screen(),
        columns(
          flexible(),
          flexible(),
          flexible()
        ));

    final var c0 = button(this.screen());
    final var c1 = button(this.screen());
    final var c2 = button(this.screen());

    layout.childAdd(c0);
    layout.childAdd(c1);
    layout.childAdd(c2);
    layout.layout(
      this.layoutContext,
      new SyConstraints(
        0,
        0,
        300,
        24
      ));

    assertEquals(300, layout.size().get().sizeX());
    assertEquals(24, layout.size().get().sizeY());

    assertEquals(100, c0.size().get().sizeX());
    assertEquals(100, c1.size().get().sizeX());
    assertEquals(100, c2.size().get().sizeX());

    assertEquals(24, c0.size().get().sizeY());
    assertEquals(24, c1.size().get().sizeY());
    assertEquals(24, c2.size().get().sizeY());

    layout.layout(
      this.layoutContext,
      new SyConstraints(
        0,
        0,
        600,
        48
      ));

    assertEquals(600, layout.size().get().sizeX());
    assertEquals(48, layout.size().get().sizeY());

    assertEquals(200, c0.size().get().sizeX());
    assertEquals(200, c1.size().get().sizeX());
    assertEquals(200, c2.size().get().sizeX());

    assertEquals(48, c0.size().get().sizeY());
    assertEquals(48, c1.size().get().sizeY());
    assertEquals(48, c2.size().get().sizeY());
  }

  /**
   * A layout doesn't accept window events.
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
  protected SyFormRow newComponent()
  {
    return new SyFormRow(
      this.screen(),
      columns(
        exact(32),
        flexible(),
        exact(16)
      ));
  }
}
