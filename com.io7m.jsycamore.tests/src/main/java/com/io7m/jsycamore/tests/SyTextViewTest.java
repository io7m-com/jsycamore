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

import com.io7m.jsycamore.api.components.SyTextViewType;
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
import com.io7m.jsycamore.api.text.SyFontException;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextSelectionServiceType.SyTextSelectionIsSelected;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jsycamore.components.standard.SyComponentAttributes;
import com.io7m.jsycamore.components.standard.text.SyTextSingleLineModel;
import com.io7m.jsycamore.components.standard.text.SyTextView;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.text.SyFontStyle.BOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SyTextViewTest extends SyComponentContract<SyTextViewType>
{
  @BeforeEach
  public void textViewSetup()
  {

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
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRForward()
  {
    final var c = this.newComponent();
    c.setText(SyText.text("HELLO!"));
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
      SyTextSelectionIsSelected.class,
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
      SyTextSelectionIsSelected.class,
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
        SyTextSelectionIsSelected.class,
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
    assertEquals('H', chLower.centerCharacter());
    assertEquals(2, chUpper.centerIndex());
    assertEquals('L', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(15, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionLTRBackwards()
  {
    final var c = this.newComponent();
    c.setText(SyText.text("HELLO!"));
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
      SyTextSelectionIsSelected.class,
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
      SyTextSelectionIsSelected.class,
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
        SyTextSelectionIsSelected.class,
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
    assertEquals('H', chLower.centerCharacter());
    assertEquals(2, chUpper.centerIndex());
    assertEquals('L', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(15, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLForward()
  {
    final var c = this.newComponent();
    c.setText(SyText.text("שלום!"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnPressed(
        PVector2I.of(24, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var textSelections =
      this.screen().textSelectionService();

    assertInstanceOf(
      SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(24, 0),
        PVector2I.of(0, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionIsSelected.class,
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
        SyTextSelectionIsSelected.class,
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
    assertEquals('ש', chLower.centerCharacter());
    assertEquals(4, chUpper.centerIndex());
    assertEquals('!', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(20, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testSelectionRTLBackwards()
  {
    final var c = this.newComponent();
    c.setText(SyText.text("שלום!"));
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
      SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnHeld(
        PVector2I.of(0, 0),
        PVector2I.of(24, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    assertInstanceOf(
      SyTextSelectionIsSelected.class,
      textSelections.isComponentSelected(c)
    );

    assertEquals(
      EVENT_CONSUMED,
      c.eventSend(new SyMouseEventOnReleased(
        PVector2I.of(24, 0),
        SyMouseButton.MOUSE_BUTTON_LEFT,
        c
      ))
    );

    final var selectionStatus =
      assertInstanceOf(
        SyTextSelectionIsSelected.class,
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
    assertEquals('ש', chLower.centerCharacter());
    assertEquals(4, chUpper.centerIndex());
    assertEquals('!', chUpper.centerCharacter());

    assertEquals(1, selection.regions().size());
    assertEquals(0, selection.regions().get(0).minimumX());
    assertEquals(20, selection.regions().get(0).maximumX());
    assertEquals(0, selection.regions().get(0).minimumY());
  }

  /**
   * A text view that is selectable allows for selections.
   */

  @Test
  public void testViewResize0()
    throws SyFontException
  {
    final var c = this.newComponent();
    c.setText(SyText.text("שלום!"));
    c.setTextSelectable(true);
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);

    c.setText(SyText.text("שלום!!"));
    this.window().layout(this.layoutContext);
  }

  /**
   * Resizing the model works.
   */

  @Test
  public void testModelResize()
    throws Exception
  {
    final var text =
      SyComponentAttributes.get()
      .create(SyText.text("Hello!"));

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
      SyTextSingleLineModel.create(text, font0);

    assertEquals(font0, model.font());
    model.setText(SyText.text("Goodbye!"));
    model.setFont(font1);
    assertEquals(font1, model.font());
  }

  /**
   * Model selections must be used in the correct order.
   */

  @Test
  public void testModelSelectionBad0()
    throws Exception
  {
    final var text =
      SyComponentAttributes.get()
        .create(SyText.text("Hello!"));

    final var font0 =
      this.screen()
        .services()
        .requireService(SyFontDirectoryServiceType.class)
        .get(new SyFontDescription("DejaVu Sans", BOLD, 12));

    final var model =
      SyTextSingleLineModel.create(text, font0);

    assertThrows(IllegalStateException.class, () -> {
      model.selectionContinue(PVectors2I.zero());
    });
    assertThrows(IllegalStateException.class, () -> {
      model.selectionFinish(PVectors2I.zero());
    });
  }

  @Override
  protected SyTextViewType newComponent()
  {
    return SyTextView.textView(this.screen());
  }
}
