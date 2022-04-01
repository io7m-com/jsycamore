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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.events.SyEventConsumer;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.windows.SyWindowCreated;
import com.io7m.jsycamore.api.windows.SyWindowEventType;
import com.io7m.jsycamore.api.windows.SyWindowFocusGained;
import com.io7m.jsycamore.api.windows.SyWindowFocusLost;
import com.io7m.jsycamore.awt.internal.SyFontDirectoryAWT;
import com.io7m.jsycamore.theme.primal.SyThemePrimalFactory;
import com.io7m.jsycamore.vanilla.SyScreenFactory;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_LEFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyScreenTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyScreenTest.class);

  private SyScreenFactory screens;
  private SyScreenType screen;
  private List<SyWindowEventType> events;
  private CountDownLatch eventsLatch;

  @BeforeEach
  public void setup()
  {
    this.screens =
      new SyScreenFactory();
    this.screen =
      this.screens.create(
        new SyThemePrimalFactory().create(),
        SyFontDirectoryAWT.create(),
        PAreaSizeI.of(800, 600)
      );

    this.eventsLatch = new CountDownLatch(1);
    this.events = Collections.synchronizedList(new ArrayList<>());

    SyEventConsumer.subscribe(
      this.screen.windowEvents(),
      event -> {
        LOG.debug("event: {}", event);
        this.events.add(event);
        LOG.debug("event count: {}", this.events.size());
      },
      () -> {
        LOG.debug("event stream closed");
        this.eventsLatch.countDown();
      });
  }

  @AfterEach
  public void tearDown()
  {
    this.screen.close();
  }

  @Test
  public void testWindowFocusCreation()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);
    final var w1 =
      this.screen.windowCreate(240, 120);

    assertFalse(this.screen.windowIsFocused(w0));
    assertTrue(this.screen.windowIsFocused(w1));
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusGained(w0.id()), this.events.remove(0));
    assertEquals(new SyWindowCreated(w1.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusLost(w0.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusGained(w1.id()), this.events.remove(0));
    assertEquals(0, this.events.size());
  }

  @Test
  public void testWindowFocusMouseEvent()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);
    final var w1 =
      this.screen.windowCreate(240, 120);

    w0.setPosition(PVector2I.of(0, 0));
    w1.setPosition(PVector2I.of(250, 150));
    this.screen.update();

    assertFalse(this.screen.windowIsFocused(w0));
    assertTrue(this.screen.windowIsFocused(w1));

    this.screen.mouseDown(PVector2I.of(48, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(48, 20), MOUSE_BUTTON_LEFT);

    assertTrue(this.screen.windowIsFocused(w0));
    assertFalse(this.screen.windowIsFocused(w1));
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusGained(w0.id()), this.events.remove(0));
    assertEquals(new SyWindowCreated(w1.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusLost(w0.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusGained(w1.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusLost(w1.id()), this.events.remove(0));
    assertEquals(new SyWindowFocusGained(w0.id()), this.events.remove(0));
    assertEquals(0, this.events.size());
  }
}
