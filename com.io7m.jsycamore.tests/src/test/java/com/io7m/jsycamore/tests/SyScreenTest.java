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
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.menus.SyMenuClosed;
import com.io7m.jsycamore.api.menus.SyMenuOpened;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.windows.SyWindowBecameInvisible;
import com.io7m.jsycamore.api.windows.SyWindowBecameVisible;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowCreated;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.api.windows.SyWindowMaximized;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.api.windows.SyWindowUnmaximized;
import com.io7m.jsycamore.awt.internal.SyAWTImageLoader;
import com.io7m.jsycamore.awt.internal.SyAWTRenderer;
import com.io7m.jsycamore.awt.internal.SyFontDirectoryAWT;
import com.io7m.jsycamore.components.standard.SyButton;
import com.io7m.jsycamore.components.standard.SyLayoutVertical;
import com.io7m.jsycamore.components.standard.SyMenu;
import com.io7m.jsycamore.theme.primal.SyThemePrimalFactory;
import com.io7m.jsycamore.vanilla.SyScreenFactory;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_LEFT;
import static com.io7m.jsycamore.api.windows.SyWindowCloseBehaviour.CLOSE_ON_CLOSE_BUTTON;
import static com.io7m.jsycamore.api.windows.SyWindowCloseBehaviour.HIDE_ON_CLOSE_BUTTON;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyScreenTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyScreenTest.class);

  private SyScreenFactory screens;
  private SyScreenType screen;
  private List<SyEventType> events;
  private CountDownLatch eventsLatch;

  private static boolean shouldDumpImages()
  {
    return Objects.equals(
      System.getProperty(
      "com.io7m.jsycamore.tests.dumpImages",
      "false"),
      "true"
    );
  }

  @BeforeEach
  public void setup()
  {
    this.screens =
      new SyScreenFactory();
    this.screen =
      this.screens.create(
        new SyThemePrimalFactory().create(),
        SyFontDirectoryAWT.createFromServiceLoader(),
        PAreaSizeI.of(1024, 1024)
      );

    this.eventsLatch = new CountDownLatch(1);
    this.events = Collections.synchronizedList(new ArrayList<>());

    SyEventConsumer.subscribe(
      this.screen.events(),
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

  /**
   * Creating a window brings that window into focus.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowFocusCreation()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);
    final var w1 =
      this.screen.windowCreate(240, 120);

    assertTrue(this.screen.windowIsVisible(w0));
    assertTrue(this.screen.windowIsVisible(w1));

    assertFalse(this.screen.windowIsFocused(w0));
    assertTrue(this.screen.windowIsFocused(w1));
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowCreated(w1.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w1.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Clicking a window brings that window into focus.
   *
   * @throws Exception On errors
   */

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

    assertTrue(this.screen.windowIsFocused(w0));
    assertFalse(this.screen.windowIsFocused(w1));
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowCreated(w1.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w1.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Focusing a window brings that window into focus.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowFocusExplicit()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);
    final var w1 =
      this.screen.windowCreate(240, 120);

    assertFalse(this.screen.windowIsFocused(w0));
    assertTrue(this.screen.windowIsFocused(w1));

    this.screen.windowFocus(w0);
    assertTrue(this.screen.windowIsFocused(w0));
    assertFalse(this.screen.windowIsFocused(w1));

    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowCreated(w1.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w1.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Closing a window brings another window into focus.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowFocusClose()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);
    final var w1 =
      this.screen.windowCreate(240, 120);

    assertTrue(this.screen.windowIsVisible(w0));
    assertTrue(this.screen.windowIsVisible(w1));
    assertFalse(this.screen.windowIsFocused(w0));
    assertTrue(this.screen.windowIsFocused(w1));

    this.screen.windowHide(w1);

    assertTrue(this.screen.windowIsVisible(w0));
    assertFalse(this.screen.windowIsVisible(w1));
    assertTrue(this.screen.windowIsFocused(w0));
    assertFalse(this.screen.windowIsFocused(w1));

    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowCreated(w1.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w1.id()), this.eventNext());
    assertEquals(new SyWindowBecameInvisible(w1.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Unrecognized windows result in errors.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowUnrecognized()
    throws Exception
  {
    final var w0 = Mockito.mock(SyWindowType.class);
    Mockito.when(w0.id()).thenReturn(new SyWindowID(UUID.randomUUID()));

    assertThrows(IllegalStateException.class, () -> {
      this.screen.windowShow(w0);
    });
  }

  /**
   * Window position snapping works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowPositionSnapping()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    w0.setPosition(PVector2I.of(1, 1));
    assertEquals(PVector2I.of(1, 1), w0.position().get());

    w0.positionSnapping().set(16);
    assertEquals(PVector2I.of(1, 1), w0.position().get());

    w0.setPosition(PVector2I.of(1, 1));
    assertEquals(PVector2I.of(0, 0), w0.position().get());

    w0.setPosition(PVector2I.of(18, 18));
    assertEquals(PVector2I.of(16, 16), w0.position().get());
  }

  /**
   * Window size snapping works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowSizeSnapping()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    w0.setSize(PAreaSizeI.of(1, 1));
    assertEquals(PAreaSizeI.of(1, 1), w0.size().get());

    w0.sizeSnapping().set(16);
    assertEquals(PAreaSizeI.of(1, 1), w0.size().get());

    w0.setSize(PAreaSizeI.of(1, 1));
    assertEquals(PAreaSizeI.of(0, 0), w0.size().get());

    w0.setSize(PAreaSizeI.of(18, 18));
    assertEquals(PAreaSizeI.of(16, 16), w0.size().get());
  }

  /**
   * Window hiding via the close button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowCloseHide()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    this.screen.update();
    w0.closeButtonBehaviour().set(HIDE_ON_CLOSE_BUTTON);

    assertTrue(this.screen.windowIsVisible(w0));
    this.screen.mouseDown(PVector2I.of(20, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(20, 20), MOUSE_BUTTON_LEFT);
    assertFalse(this.screen.windowIsVisible(w0));

    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameInvisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Window closing via the close button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowCloseClose()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    this.screen.update();
    w0.closeButtonBehaviour().set(CLOSE_ON_CLOSE_BUTTON);

    assertTrue(this.screen.windowIsVisible(w0));
    this.screen.mouseDown(PVector2I.of(20, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(20, 20), MOUSE_BUTTON_LEFT);
    assertFalse(this.screen.windowIsVisible(w0));

    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowClosed(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Window maximizing via the maximize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowMaximize()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    this.screen.update();
    this.screen.mouseDown(PVector2I.of(220, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(220, 20), MOUSE_BUTTON_LEFT);
    this.screen.update();
    this.screen.mouseDown(PVector2I.of(1024 - 16, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(1024 - 16, 20), MOUSE_BUTTON_LEFT);
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyWindowMaximized(w0.id()), this.eventNext());
    assertEquals(new SyWindowUnmaximized(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * The window menu button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowMenu()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    this.screen.update();
    this.screen.mouseDown(PVector2I.of(200, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(200, 20), MOUSE_BUTTON_LEFT);
    this.screen.update();
    this.screen.mouseDown(PVector2I.of(64, 64), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(64, 64), MOUSE_BUTTON_LEFT);
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(SyMenuOpened.class, this.eventNext().getClass());
    assertEquals(SyMenuClosed.class, this.eventNext().getClass());
    assertEquals(0, this.events.size());
  }

  /**
   * The window menu button can close the window.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowMenuClose()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(240, 120);

    this.screen.update();
    this.screen.mouseDown(PVector2I.of(200, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(200, 20), MOUSE_BUTTON_LEFT);
    this.screen.mouseDown(PVector2I.of(205, 25), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(205, 25), MOUSE_BUTTON_LEFT);
    this.screen.update();
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(SyMenuOpened.class, this.eventNext().getClass());
    assertEquals(new SyWindowClosed(w0.id()), this.eventNext());
    assertEquals(SyMenuClosed.class, this.eventNext().getClass());
    assertEquals(0, this.events.size());
  }

  /**
   * Opening a menu works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testMenuOpen()
    throws Exception
  {
    final var clicks = new AtomicInteger(0);

    final var menu = new SyMenu();
    menu.addAtom("Item 0", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.addAtom("Item 2", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.addAtom("Item 3", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.position().set(PVector2I.of(0, 0));

    final var button = new SyButton("Open", () -> {
      this.screen.menuOpen(menu);
    });

    final var w0 =
      this.screen.windowCreate(240, 120);

    w0.decorated().set(false);
    w0.contentArea().childAdd(button);

    this.screen.update();

    // Click the button
    this.screen.mouseMoved(PVector2I.of(0, 0));
    this.screen.mouseDown(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);

    // Click the menu item
    this.screen.mouseDown(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);

    assertEquals(1, clicks.get());
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyMenuOpened(menu), this.eventNext());
    assertEquals(new SyMenuClosed(menu), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Opening a menu and then clicking outside of the menu closes the menu.
   *
   * @throws Exception On errors
   */

  @Test
  public void testMenuClickElsewhere()
    throws Exception
  {
    final var clicks = new AtomicInteger(0);

    final var menu = new SyMenu();
    menu.addAtom("Item 0", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.addAtom("Item 2", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.addAtom("Item 3", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.position().set(PVector2I.of(0, 0));

    final var button = new SyButton("Open", () -> {
      this.screen.menuOpen(menu);
    });

    final var w0 =
      this.screen.windowCreate(240, 120);

    w0.decorated().set(false);
    w0.contentArea().childAdd(button);

    this.screen.update();

    // Click the button
    this.screen.mouseMoved(PVector2I.of(0, 0));
    this.screen.mouseDown(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);

    // Click outside the menu.
    this.screen.mouseDown(PVector2I.of(799, 599), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(799, 599), MOUSE_BUTTON_LEFT);

    assertEquals(0, clicks.get());
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyMenuOpened(menu), this.eventNext());
    assertEquals(new SyMenuClosed(menu), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Opening a menu and then clicking something that isn't the menu closes the menu.
   *
   * @throws Exception On errors
   */

  @Test
  public void testMenuClickNotMenu()
    throws Exception
  {
    final var clicks =
      new AtomicInteger(0);
    final var clicksOther =
      new AtomicInteger(0);

    final var menu = new SyMenu();
    menu.addAtom("Item 0", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.addAtom("Item 2", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.addAtom("Item 3", () -> {
      LOG.debug("click");
      clicks.incrementAndGet();
    });
    menu.position().set(PVector2I.of(0, 0));

    final var button = new SyButton("Open", () -> {
      this.screen.menuOpen(menu);
    });
    final var otherButton =
      new SyButton("Otherwise", clicksOther::incrementAndGet);

    final var vertical = new SyLayoutVertical();
    vertical.childAdd(button);
    vertical.childAdd(otherButton);

    final var w0 =
      this.screen.windowCreate(240, 120);

    w0.decorated().set(false);
    w0.contentArea().childAdd(vertical);
    this.screen.update();

    this.dumpScreen();

    // Click the button
    this.screen.mouseMoved(PVector2I.of(0, 0));
    this.screen.mouseDown(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(0, 0), MOUSE_BUTTON_LEFT);
    this.screen.update();

    // Click the other button
    this.screen.mouseDown(PVector2I.of(16, 80), MOUSE_BUTTON_LEFT);
    this.screen.mouseUp(PVector2I.of(16, 80), MOUSE_BUTTON_LEFT);
    assertEquals(1, clicksOther.get());
    assertEquals(0, clicks.get());
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(new SyMenuOpened(menu), this.eventNext());
    assertEquals(new SyMenuClosed(menu), this.eventNext());
    assertEquals(0, this.events.size());
  }

  private void dumpScreen()
  {
    if (shouldDumpImages()) {
      final var size =
        this.screen.size().get();
      final var image =
        new BufferedImage(size.sizeX(), size.sizeY(), TYPE_4BYTE_ABGR_PRE);

      final var imageLoader =
        new SyAWTImageLoader();
      final var fonts =
        SyFontDirectoryAWT.createFromServiceLoader();
      final var renderer =
        new SyAWTRenderer(fonts, imageLoader);
      final var graphics =
        image.createGraphics();

      for (final var w : this.screen.windowsVisibleOrdered()) {
        renderer.render(graphics, this.screen, w);
      }

      try {
        ImageIO.write(image, "PNG", new File("/tmp/screen.png"));
      } catch (final IOException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  /**
   * Closing a menu that isn't open does nothing.
   *
   * @throws Exception On errors
   */

  @Test
  public void testMenuCloseNoop()
    throws Exception
  {
    this.screen.menuClose();
    this.screen.update();
    this.screen.close();

    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the north resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeN()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(512, 384));
    this.screen.mouseDown(PVector2I.of(512, 384), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 320));
    this.screen.mouseUp(PVector2I.of(512, 320), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 320), w0.size().get());
    assertEquals(PVector2I.of(384, 320), w0.position().get());

    this.screen.mouseMoved(PVector2I.of(512, 320));
    this.screen.mouseDown(PVector2I.of(512, 320), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 384));
    this.screen.mouseUp(PVector2I.of(512, 384), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the NW resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeNW()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(384, 384));
    this.screen.mouseDown(PVector2I.of(384, 384), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(320, 320));
    this.screen.mouseUp(PVector2I.of(320, 320), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 320), w0.size().get());
    assertEquals(PVector2I.of(320, 320), w0.position().get());

    this.screen.mouseMoved(PVector2I.of(320, 320));
    this.screen.mouseDown(PVector2I.of(320, 320), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(384, 384));
    this.screen.mouseUp(PVector2I.of(384, 384), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the NE resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeNE()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(640 - 1, 384));
    this.screen.mouseDown(PVector2I.of(640 - 1, 384), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(704 - 1, 320));
    this.screen.mouseUp(PVector2I.of(704 - 1, 320), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 320), w0.size().get());
    assertEquals(PVector2I.of(384, 320), w0.position().get());
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(704 - 1, 320));
    this.screen.mouseDown(PVector2I.of(704 - 1, 320), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(640 - 1, 384));
    this.screen.mouseUp(PVector2I.of(640 - 1, 384), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the S resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeS()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(512, 640 - 1));
    this.screen.mouseDown(PVector2I.of(512, 640 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 704 - 1));
    this.screen.mouseUp(PVector2I.of(512, 704 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 320), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(512, 704 - 1));
    this.screen.mouseDown(PVector2I.of(512, 704 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 640 - 1));
    this.screen.mouseUp(PVector2I.of(512, 640 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());
    this.screen.update();

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the SW resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeSW()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(384, 640 - 1));
    this.screen.mouseDown(PVector2I.of(384, 640 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(320, 704 - 1));
    this.screen.mouseUp(PVector2I.of(320, 704 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 320), w0.size().get());
    assertEquals(PVector2I.of(320, 384), w0.position().get());
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(320, 704 - 1));
    this.screen.mouseDown(PVector2I.of(320, 704 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(384, 640 - 1));
    this.screen.mouseUp(PVector2I.of(384, 640 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());
    this.screen.update();

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the SE resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeSE()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(640 - 1, 640 - 1));
    this.screen.mouseDown(PVector2I.of(640 - 1, 640 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(704 - 1, 704 - 1));
    this.screen.mouseUp(PVector2I.of(704 - 1, 704 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 320), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(704 - 1, 704 - 1));
    this.screen.mouseDown(PVector2I.of(704 - 1, 704 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(640 - 1, 640 - 1));
    this.screen.mouseUp(PVector2I.of(640 - 1, 640 - 1), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());
    this.screen.update();

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the W resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeW()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(384, 512));
    this.screen.mouseDown(PVector2I.of(384, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(320, 512));
    this.screen.mouseUp(PVector2I.of(320, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 256), w0.size().get());
    assertEquals(PVector2I.of(320, 384), w0.position().get());

    this.screen.mouseMoved(PVector2I.of(320, 512));
    this.screen.mouseDown(PVector2I.of(320, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(384, 512));
    this.screen.mouseUp(PVector2I.of(384, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(256, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * Resizing a window via the E resize button works.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeE()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    this.screen.mouseMoved(PVector2I.of(640 - 1, 512));
    this.screen.mouseDown(PVector2I.of(640 - 1, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(704 - 1, 512));
    this.screen.mouseUp(PVector2I.of(704 - 1, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());

    this.screen.mouseMoved(PVector2I.of(704 - 1, 512));
    this.screen.mouseDown(PVector2I.of(704 - 1, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(640 - 1, 512));
    this.screen.mouseUp(PVector2I.of(640 - 1, 512), MOUSE_BUTTON_LEFT);
    this.screen.mouseMoved(PVector2I.of(512, 0));
    assertEquals(PAreaSizeI.of(320, 256), w0.size().get());
    assertEquals(PVector2I.of(384, 384), w0.position().get());

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * The resize buttons ignore anything but the left mouse button.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowResizeButtonsNotLeft()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    w0.setPosition(PVector2I.of(384, 384));
    this.screen.update();

    // North
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(512, 384), button);
      this.screen.mouseMoved(PVector2I.of(512, 320));
      this.screen.mouseUp(PVector2I.of(512, 320), button);
    }

    // South
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(512, 640 - 1), button);
      this.screen.mouseMoved(PVector2I.of(512, 640 - 1));
      this.screen.mouseUp(PVector2I.of(512, 640 - 1), button);
    }

    // West
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(384, 512), button);
      this.screen.mouseMoved(PVector2I.of(384, 512));
      this.screen.mouseUp(PVector2I.of(384, 512), button);
    }

    // East
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(640 - 1, 512), button);
      this.screen.mouseMoved(PVector2I.of(640 - 1, 512));
      this.screen.mouseUp(PVector2I.of(640 - 1, 512), button);
    }

    // North West
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(384, 384), button);
      this.screen.mouseMoved(PVector2I.of(384, 384));
      this.screen.mouseUp(PVector2I.of(384, 384), button);
    }

    // North East
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(384, 640 - 1), button);
      this.screen.mouseMoved(PVector2I.of(384, 640 - 1));
      this.screen.mouseUp(PVector2I.of(384, 640 - 1), button);
    }

    // South East
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(640 - 1, 640 - 1), button);
      this.screen.mouseMoved(PVector2I.of(640 - 1, 640 - 1));
      this.screen.mouseUp(PVector2I.of(640 - 1, 640 - 1), button);
    }

    // South West
    for (final var button : SyMouseButton.values()) {
      if (button == MOUSE_BUTTON_LEFT) {
        continue;
      }
      this.screen.mouseDown(PVector2I.of(640 - 1, 384), button);
      this.screen.mouseMoved(PVector2I.of(640 - 1, 384));
      this.screen.mouseUp(PVector2I.of(640 - 1, 384), button);
    }

    this.screen.close();
    this.eventsLatch.await(3L, TimeUnit.SECONDS);
    assertEquals(new SyWindowCreated(w0.id()), this.eventNext());
    assertEquals(new SyWindowBecameVisible(w0.id()), this.eventNext());
    assertEquals(0, this.events.size());
  }

  /**
   * The window components ignore window events.
   *
   * @throws Exception On errors
   */

  @Test
  public void testWindowComponentsIgnoreWindowEvents()
    throws Exception
  {
    final var w0 =
      this.screen.windowCreate(256, 256);

    var root = w0.contentArea().node();
    while (root.parent().isPresent()) {
      root = root.parent().get();
    }

    final var finalRoot = root;
    root.forEachDepthFirst(Void.class, (input, depth, node) -> {
      final var component =
        finalRoot.value();
      final var result =
        component.eventSend(new SyWindowClosed(w0.id()));
      assertEquals(EVENT_NOT_CONSUMED, result);
    });
  }

  private SyEventType eventNext()
  {
    return this.events.remove(0);
  }
}
