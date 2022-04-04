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

import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.forms.SyForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnSizeType.exact;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnSizeType.flexible;
import static com.io7m.jsycamore.components.standard.forms.SyFormColumnsConfiguration.columns;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SyFormTest extends SyComponentContract<SyForm>
{
  @BeforeEach
  public void formSetup()
  {

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
  protected SyForm newComponent()
  {
    return new SyForm(columns(
      exact(32),
      flexible(),
      exact(16)
    ));
  }
}
