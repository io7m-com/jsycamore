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

import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowServiceType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.awt.internal.SyAWTFontDirectoryService;
import com.io7m.jsycamore.theme.primal.SyThemePrimalFactory;
import com.io7m.jsycamore.vanilla.SyScreenFactory;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.io7m.jsycamore.api.active.SyActive.ACTIVE;
import static com.io7m.jsycamore.api.active.SyActive.INACTIVE;
import static com.io7m.jsycamore.api.visibility.SyVisibility.VISIBILITY_INVISIBLE;
import static com.io7m.jsycamore.api.visibility.SyVisibility.VISIBILITY_VISIBLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public abstract class SyComponentContract<T extends SyComponentType>
{
  private static final PVector2I<SySpaceViewportType> Z_VIEWPORT =
    PVectors2I.zero();

  protected SyLayoutContextType layoutContext;
  private SyThemeType theme;
  private SyScreenType screen;
  private SyWindowType window;
  private SyFontDirectoryServiceType<? extends SyFontType> fonts;
  private SyWindowServiceType windowService;

  protected abstract T newComponent();

  @BeforeEach
  public void componentSetup()
  {
    this.fonts =
      SyAWTFontDirectoryService.createFromServiceLoader();
    this.theme =
      new SyThemePrimalFactory()
        .create();
    this.screen =
      new SyScreenFactory()
        .create(this.theme, this.fonts, PAreaSizeI.of(1024, 1024));
    this.windowService =
      this.screen.windowService();

    this.window =
      this.windowService.windowCreate(512, 512);
    this.window.decorated()
      .set(false);

    this.layoutContext =
      Mockito.mock(SyLayoutContextType.class);

    Mockito.doAnswer(invocationOnMock -> {
        return invocationOnMock.getArguments()[0];
      }).when(this.layoutContext)
      .deriveThemeConstraints(any(), any());

    Mockito.when(this.layoutContext.themeCurrent())
      .thenReturn(this.theme);
    Mockito.when(this.layoutContext.fonts())
      .thenReturn(this.fonts);
  }

  protected final SyScreenType screen()
  {
    return this.screen;
  }

  protected final SyComponentType windowContentArea()
  {
    return this.window().contentArea();
  }

  protected final SyWindowType window()
  {
    return this.window;
  }

  /**
   * Component activity works.
   */

  @Test
  public final void testComponentActiveOK()
  {
    final var c = this.newComponent();
    assertEquals(ACTIVE, c.activity().get());
    assertTrue(c.isActive());

    c.setActive(INACTIVE);
    assertEquals(INACTIVE, c.activity().get());
    assertFalse(c.isActive());

    c.setActive(ACTIVE);
    assertEquals(ACTIVE, c.activity().get());
    assertTrue(c.isActive());
  }

  /**
   * Component visibility works.
   */

  @Test
  public final void testComponentVisibleOK()
  {
    final var c = this.newComponent();
    assertEquals(VISIBILITY_VISIBLE, c.visibility().get());
    assertTrue(c.isVisible());

    c.setVisible(VISIBILITY_INVISIBLE);
    assertEquals(VISIBILITY_INVISIBLE, c.visibility().get());
    assertFalse(c.isVisible());

    c.setVisible(VISIBILITY_VISIBLE);
    assertEquals(VISIBILITY_VISIBLE, c.visibility().get());
    assertTrue(c.isVisible());
  }

  /**
   * Components respect size limits.
   */

  @Test
  public final void testComponentSizeLimitOK()
  {
    final var c = this.newComponent();

    c.sizeUpperLimit().set(PAreaSizeI.of(64, 32));

    final var size =
      c.layout(
        this.layoutContext,
        new SyConstraints(
          0,
          0,
          128,
          128
        ));

    final var rSize = c.size().get();
    assertTrue(
      rSize.sizeX() <= 64,
      () -> "%d <= 64".formatted(rSize.sizeX()));
    assertTrue(
      rSize.sizeY() <= 32,
      () -> "%d <= 32".formatted(rSize.sizeY()));
  }

  /**
   * Components accepting mouse focus track the "over" status.
   */

  @Test
  public final void testComponentMouseOverOK()
  {
    final var c = this.newComponent();

    if (c.isMouseQueryAccepting()) {
      c.eventSend(new SyMouseEventOnOver(Z_VIEWPORT, c));
      assertTrue(c.isMouseOver());
    }
  }

  /**
   * Components can be attached to windows.
   */

  @Test
  public final void testComponentWindowOK()
  {
    final var c = this.newComponent();

    assertTrue(c.window().isEmpty());
    assertTrue(c.windowReadable().isEmpty());

    this.window.contentArea()
      .childAdd(c);

    assertEquals(Optional.of(this.window), c.window());
    assertEquals(Optional.of(this.window), c.windowReadable());
  }

  /**
   * Component children work.
   */

  @Test
  public final void testComponentChildAddRemoveOK()
  {
    final var p = this.newComponent();
    final var c0 = this.newComponent();
    final var c1 = this.newComponent();
    final var c2 = this.newComponent();

    p.childAdd(c0);
    p.childAdd(c1);
    p.childAdd(c2);

    var children =
      p.node()
        .children()
        .stream()
        .map(JOTreeNodeReadableType::value)
        .toList();

    assertTrue(children.contains(c0));
    assertTrue(children.contains(c1));
    assertTrue(children.contains(c2));

    p.childRemove(c0);
    children =
      p.node()
        .children()
        .stream()
        .map(JOTreeNodeReadableType::value)
        .toList();

    assertFalse(children.contains(c0));
    assertTrue(children.contains(c1));
    assertTrue(children.contains(c2));

    p.childRemove(c1);
    children =
      p.node()
        .children()
        .stream()
        .map(JOTreeNodeReadableType::value)
        .toList();

    assertFalse(children.contains(c0));
    assertFalse(children.contains(c1));
    assertTrue(children.contains(c2));

    p.childRemove(c2);
    children =
      p.node()
        .children()
        .stream()
        .map(JOTreeNodeReadableType::value)
        .toList();

    assertFalse(children.contains(c0));
    assertFalse(children.contains(c1));
    assertFalse(children.contains(c2));
  }

  /**
   * Component children work.
   */

  @Test
  public final void testComponentChildAddClearOK()
  {
    final var p = this.newComponent();
    final var c0 = this.newComponent();
    final var c1 = this.newComponent();
    final var c2 = this.newComponent();

    p.childAdd(c0);
    p.childAdd(c1);
    p.childAdd(c2);

    var children =
      p.node()
        .children()
        .stream()
        .map(JOTreeNodeReadableType::value)
        .toList();

    assertTrue(children.contains(c0));
    assertTrue(children.contains(c1));
    assertTrue(children.contains(c2));

    p.childrenClear();
    children =
      p.node()
        .children()
        .stream()
        .map(JOTreeNodeReadableType::value)
        .toList();

    assertFalse(children.contains(c0));
    assertFalse(children.contains(c1));
    assertFalse(children.contains(c2));
  }
}
