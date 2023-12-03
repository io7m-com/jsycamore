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

import com.io7m.jsycamore.api.keyboard.SyKeyCode;
import com.io7m.jsycamore.api.keyboard.SyKeyEventPressed;
import com.io7m.jsycamore.api.menus.SyMenuClosed;
import com.io7m.jsycamore.api.menus.SyMenuType;
import com.io7m.jsycamore.api.mouse.SyMouseButton;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.awt.internal.SyAWTFont;
import com.io7m.jsycamore.awt.internal.SyAWTFontDirectoryService;
import com.io7m.jsycamore.awt.internal.SyAWTImageLoader;
import com.io7m.jsycamore.awt.internal.SyAWTRenderer;
import com.io7m.jsycamore.components.standard.text.SyTextMultiLineModel;
import com.io7m.jsycamore.components.standard.text.SyTextMultiLineView;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.text.SyFontStyle.BOLD;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyTextMultiLineViewTest
  extends SyComponentContract<SyTextMultiLineView>
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyTextMultiLineViewTest.class);
  private Path directory;
  private BufferedImage imageReceived;
  private Path imageReceivedFile;
  private SyAWTImageLoader imageLoader;
  private Graphics2D graphics;
  private SyFontDirectoryServiceType<SyAWTFont> fontsAWT;
  private SyAWTRenderer renderer;

  private static List<SyText> textSections(
    final String file)
    throws IOException
  {
    final var path =
      "/com/io7m/jsycamore/tests/%s".formatted(file);

    final var c = SyTextMultiLineViewTest.class;
    try (var stream = c.getResourceAsStream(path)) {
      final var data = stream.readAllBytes();
      return new String(data, StandardCharsets.UTF_8)
        .lines()
        .map(SyText::text)
        .toList();
    }
  }

  @BeforeEach
  public void textViewSetup(
    final @TempDir Path directory)
  {
    this.directory =
      directory;
    this.imageReceived =
      new BufferedImage(768, 768, TYPE_4BYTE_ABGR_PRE);
    this.imageReceivedFile =
      this.directory.resolve("received.png");
    this.imageLoader =
      new SyAWTImageLoader();
    this.fontsAWT =
      SyAWTFontDirectoryService.createFromServiceLoader();

    this.graphics = this.imageReceived.createGraphics();
    this.graphics.setPaint(Color.BLUE);
    this.graphics.fillRect(0, 0, 768, 768);
    this.renderer =
      new SyAWTRenderer(
        this.screen().services(),
        this.fontsAWT,
        this.imageLoader
      );
  }

  /**
   * A text view doesn't accept various events.
   */

  @Test
  public void testIgnoredEvents()
  {
    final var c = this.newComponent();
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyWindowClosed(new SyWindowID(UUID.randomUUID())))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnOver(PVectors2I.zero(), c))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnNoLongerOver())
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyKeyEventPressed(SyKeyCode.SY_KEY_E))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMenuClosed(Mockito.mock(SyMenuType.class)))
    );
  }

  /**
   * A text view doesn't accept events before a layout.
   */

  @Test
  public void testIgnoredEventsBeforeLayout()
  {
    final var c = this.newComponent();
    c.setTextSelectable(true);

    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );
  }

  /**
   * Text is deferred until a layout occurs.
   */

  @Test
  public void testTextDeferred()
  {
    final var c = this.newComponent();
    c.textSectionAppend(SyText.text("Hello!"));

    assertEquals(
      Collections.emptySortedMap(),
      c.textsByYOffset()
    );

    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      SyText.text("Hello!"),
      c.textsByYOffset().get(0).text()
    );

    c.textSectionAppend(SyText.text("Goodbye!"));
    this.window().layout(this.layoutContext);

    assertEquals(
      SyText.text("Hello!"),
      c.textsByYOffset().get(0).text()
    );
    assertEquals(
      SyText.text("Goodbye!"),
      c.textsByYOffset().get(14).text()
    );
  }

  /**
   * A text view that is not selectable, does not accept mouse events.
   */

  @Test
  public void testNotSelectable()
  {
    final var c = this.newComponent();
    c.setTextSelectable(false);

    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );
    assertEquals(
      EVENT_NOT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward0()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(16, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(16, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(0, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(0, chLower.centerIndex());
    assertEquals('L', chLower.centerCharacter());
    assertEquals(2, chUpper.centerIndex());
    assertEquals('r', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(18, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward1()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(16, 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(16, 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(1, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(0, chLower.centerIndex());
    assertEquals('L', chLower.centerCharacter());
    assertEquals(3, chUpper.centerIndex());
    assertEquals('i', chUpper.centerCharacter());

    assertEquals(2, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(465, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(0, selection.regions().get(1).minimumX());
    assertEquals(16, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward2()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(16, 18 * 2),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(16, 18 * 2),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(2, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(0, chLower.centerIndex());
    assertEquals('L', chLower.centerCharacter());
    assertEquals(1, chUpper.centerIndex());
    assertEquals('a', chUpper.centerCharacter());

    assertEquals(3, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(465, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(0, selection.regions().get(1).minimumX());
    assertEquals(498, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());

    assertEquals(0, selection.regions().get(2).minimumX());
    assertEquals(18, selection.regions().get(2).maximumX());
    assertEquals(28, selection.regions().get(2).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward0()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(485, 0),
        PVector2I.of(300, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(300, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(0, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(4, chLower.centerIndex());
    assertEquals('ל', chLower.centerCharacter());
    assertEquals(36, chUpper.centerIndex());
    assertEquals('ח', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(301, selection.regions().get(0).minimumX());
    assertEquals(486, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward1()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(485, 0),
        PVector2I.of(300, 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(300, 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(1, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(4, chLower.centerIndex());
    assertEquals('ל', chLower.centerCharacter());
    assertEquals(36, chUpper.centerIndex());
    assertEquals('ח', chUpper.centerCharacter());

    assertEquals(2, selection.regions().size());
    assertEquals(27, selection.regions().get(0).minimumX());
    assertEquals(486, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(2, selection.regions().size());
    assertEquals(298, selection.regions().get(1).minimumX());
    assertEquals(512, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward2()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(485, 0),
        PVector2I.of(300, 2 * 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(300, 2 * 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(2, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(4, chLower.centerIndex());
    assertEquals('ל', chLower.centerCharacter());
    assertEquals(36, chUpper.centerIndex());
    assertEquals('ה', chUpper.centerCharacter());

    assertEquals(3, selection.regions().size());
    assertEquals(27, selection.regions().get(0).minimumX());
    assertEquals(486, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(3, selection.regions().size());
    assertEquals(-4, selection.regions().get(1).minimumX());
    assertEquals(512, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());

    assertEquals(3, selection.regions().size());
    assertEquals(298, selection.regions().get(2).minimumX());
    assertEquals(512, selection.regions().get(2).maximumX());
    assertEquals(28, selection.regions().get(2).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackward0()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(16, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(16, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(0, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(0, chLower.centerIndex());
    assertEquals('L', chLower.centerCharacter());
    assertEquals(2, chUpper.centerIndex());
    assertEquals('r', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(18, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackward1()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(16, 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(16, 18),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(1, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(0, chLower.centerIndex());
    assertEquals('L', chLower.centerCharacter());
    assertEquals(3, chUpper.centerIndex());
    assertEquals('i', chUpper.centerCharacter());

    assertEquals(2, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(465, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(0, selection.regions().get(1).minimumX());
    assertEquals(16, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackward2()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(16, 18 * 2),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(16, 18 * 2),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(2, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(0, chLower.centerIndex());
    assertEquals('L', chLower.centerCharacter());
    assertEquals(1, chUpper.centerIndex());
    assertEquals('a', chUpper.centerCharacter());

    assertEquals(3, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(465, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(0, selection.regions().get(1).minimumX());
    assertEquals(498, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());

    assertEquals(0, selection.regions().get(2).minimumX());
    assertEquals(18, selection.regions().get(2).maximumX());
    assertEquals(28, selection.regions().get(2).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackward0()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(0, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(4, chLower.centerIndex());
    assertEquals('ל', chLower.centerCharacter());
    assertEquals(85, chUpper.centerIndex());
    assertEquals(' ', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(27, selection.regions().get(0).minimumX());
    assertEquals(486, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackward1()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(16, 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(16, 18),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(1, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(85, chLower.centerIndex());
    assertEquals(' ', chLower.centerCharacter());
    assertEquals(86, chUpper.centerIndex());
    assertEquals('ד', chUpper.centerCharacter());

    assertEquals(2, selection.regions().size());
    assertEquals(27, selection.regions().get(0).minimumX());
    assertEquals(27, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(15, selection.regions().get(1).minimumX());
    assertEquals(512, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackward2()
    throws IOException
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(400, 2 * 18),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(400, 2 * 18),
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(485, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionServiceType.SyTextSelectionIsSelected.class,
        textSelections.isComponentSelected(c)
      );

    final var selection = selectionStatus.selection();
    assertEquals(0, selection.lowerInclusive().lineNumber());
    assertEquals(2, selection.upperInclusive().lineNumber());

    final var chLower =
      selection.lowerInclusive().characterAt();
    final var chUpper =
      selection.upperInclusive().characterAt();

    assertEquals(4, chLower.centerIndex());
    assertEquals('ל', chLower.centerCharacter());
    assertEquals(18, chUpper.centerIndex());
    assertEquals('י', chUpper.centerCharacter());

    assertEquals(3, selection.regions().size());
    assertEquals(27, selection.regions().get(0).minimumX());
    assertEquals(486, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());

    assertEquals(3, selection.regions().size());
    assertEquals(-4, selection.regions().get(1).minimumX());
    assertEquals(512, selection.regions().get(1).maximumX());
    assertEquals(14, selection.regions().get(1).minimumY());

    assertEquals(3, selection.regions().size());
    assertEquals(400, selection.regions().get(2).minimumX());
    assertEquals(512, selection.regions().get(2).maximumX());
    assertEquals(28, selection.regions().get(2).minimumY());
  }

  /**
   * Resizing the model works.
   */

  @Test
  public void testModelResize()
    throws Exception
  {
    final var font0 =
      this.screen()
        .services()
        .requireService(SyFontDirectoryServiceType.class)
        .get(new SyFontDescription("DejaVu Sans", BOLD, 12));

    final var font1 =
      this.screen()
        .services()
        .requireService(SyFontDirectoryServiceType.class)
        .get(new SyFontDescription("DejaVu Sans", BOLD, 24));

    final var model =
      SyTextMultiLineModel.create(font0, 600);

    assertEquals(font0, model.font());
    model.setFont(font1);
    assertEquals(font1, model.font());
    model.setPageWidth(200);
    assertEquals(200, model.pageWidth());
  }

  /**
   * Model selections must be used in the correct order.
   */

  @Test
  public void testModelSelectionBad0()
    throws Exception
  {
    final var font0 =
      this.screen()
        .services()
        .requireService(SyFontDirectoryServiceType.class)
        .get(new SyFontDescription("DejaVu Sans", BOLD, 12));

    final var model =
      SyTextMultiLineModel.create(font0, 600);

    assertEquals(Optional.empty(), model.selectionContinue(PVectors2I.zero()));
    assertEquals(Optional.empty(), model.selectionFinish(PVectors2I.zero()));
  }

  /**
   * Models can be inspected.
   */

  @Test
  public void testModelSelectionInspectAt()
    throws Exception
  {
    final var font0 =
      this.screen()
        .services()
        .requireService(SyFontDirectoryServiceType.class)
        .get(new SyFontDescription("DejaVu Sans", BOLD, 12));

    final var model =
      SyTextMultiLineModel.create(font0, 600);

    assertEquals(Optional.empty(), model.inspectAt(PVectors2I.zero()));
  }

  @Override
  protected SyTextMultiLineView newComponent()
  {
    return new SyTextMultiLineView(this.screen(), List.of());
  }

  private void saveImage()
    throws Exception
  {
    ImageIO.write(
      this.imageReceived,
      "PNG",
      this.imageReceivedFile.toFile()
    );
  }

  private void compareImages(
    final String name)
    throws Exception
  {
    final var expectedPath =
      SyTestDirectories.resourceOf(
        SyAWTNodeRendererTest.class,
        this.directory,
        name
      );

    LOG.debug("-- comparing images");
    LOG.debug("expected {}", expectedPath);
    LOG.debug("received {}", this.imageReceivedFile);

    final var imageExpected =
      ImageIO.read(expectedPath.toFile());

    final var grabR = new PixelGrabber(
      this.imageReceived,
      0,
      0,
      this.imageReceived.getWidth(),
      this.imageReceived.getHeight(),
      false
    );

    final var grabE = new PixelGrabber(
      imageExpected,
      0,
      0,
      imageExpected.getWidth(),
      imageExpected.getHeight(),
      false
    );

    assertTrue(grabR.grabPixels(), "Grabbing pixels must succeed");
    assertTrue(grabE.grabPixels(), "Grabbing pixels must succeed");

    final var pixelsR = (int[]) grabR.getPixels();
    final var pixelsE = (int[]) grabE.getPixels();

    try {
      assertArrayEquals(pixelsE, pixelsR, "Images do not match");
    } catch (final Throwable e) {
      LOG.error("Image mismatch: ", e);
      LOG.error("Run: feh {} {}", expectedPath, this.imageReceivedFile);
      throw e;
    }
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackward0Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(485, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(485, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextRTLBackward0.png");
  }

  private void renderImage()
  {
    final var windows =
      this.screen().windowService().windowsVisibleOrdered();
    for (int index = windows.size() - 1; index >= 0; --index) {
      final var window = windows.get(index);
      window.layout(this.layoutContext);
      this.renderer.render(this.graphics, this.screen(), window);
    }
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackward1Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 1 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 1 * 18),
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextRTLBackward1.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackward2Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 2 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 2 * 18),
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextRTLBackward2.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward0Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(0, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(0, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextRTLForward0.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward1Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(256, 1 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 1 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextRTLForward1.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward2Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("loremHebrew.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(256, 2 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 2 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextRTLForward2.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackward0Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(0, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(0, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextLTRBackward0.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackward1Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 1 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 1 * 18),
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextLTRBackward1.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackward2Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 2 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 2 * 18),
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextLTRBackward2.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward0Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(0, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(0, 0),
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextLTRForward0.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward1Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(256, 1 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 1 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextLTRForward1.png");
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward2Visual()
    throws Exception
  {
    final var c = this.newComponent();

    c.textSectionsAppend(textSections("lorem.txt"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.eventSend(new SyMouseEventOnPressed(
      PVector2I.of(256, 0),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnHeld(
      PVector2I.of(256, 0),
      PVector2I.of(256, 2 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    c.eventSend(new SyMouseEventOnReleased(
      PVector2I.of(256, 2 * 18),
      SyMouseButton.MOUSE_BUTTON_LEFT,
      c
    ));

    this.renderImage();
    this.saveImage();
    this.compareImages("MultiLineTextLTRForward2.png");
  }
}
