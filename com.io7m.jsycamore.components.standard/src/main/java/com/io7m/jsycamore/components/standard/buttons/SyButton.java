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

package com.io7m.jsycamore.components.standard.buttons;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.components.SyButtonWithTextType;
import com.io7m.jsycamore.api.components.SyTextViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.jsycamore.components.standard.SyAlign;
import com.io7m.jsycamore.components.standard.SyAlignmentHorizontal;
import com.io7m.jsycamore.components.standard.SyAlignmentVertical;
import com.io7m.jsycamore.components.standard.SyComponentBuilderAbstract;
import com.io7m.jsycamore.components.standard.SyLayoutMargin;
import com.io7m.jsycamore.components.standard.text.SyTextView;

import java.util.List;
import java.util.Objects;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * A button with a text label.
 */

public final class SyButton
  extends SyButtonAbstract
  implements SyButtonWithTextType
{
  private final SyTextViewType text;
  private final SyAlign align;
  private final SyLayoutMargin margin;

  private SyButton(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    super(screen, themeClasses);
    this.margin = new SyLayoutMargin(screen);
    this.margin.setPaddingAll(8);

    this.align = new SyAlign(screen);
    this.align.alignmentHorizontal()
      .set(SyAlignmentHorizontal.ALIGN_HORIZONTAL_CENTER);
    this.align.alignmentVertical()
      .set(SyAlignmentVertical.ALIGN_VERTICAL_CENTER);

    this.text = SyTextView.textView(screen);

    this.align.childAdd(this.text);
    this.margin.childAdd(this.align);
    this.childAdd(this.margin);

    this.margin.setMouseQueryAccepting(false);
    this.align.setMouseQueryAccepting(false);
    this.text.setMouseQueryAccepting(false);
  }

  /**
   * Create a button with the given extra theme classes.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The theme classes in priority order
   * @param text         The text
   * @param listener     The on-click listener
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses,
    final SyText text,
    final Runnable listener)
  {
    return buttonBuilder(screen)
      .setText(text)
      .setOnClickListener(listener)
      .setExtraThemeClasses(themeClasses)
      .build();
  }

  /**
   * Create a button with the given extra theme classes.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The theme classes in priority order
   * @param text         The text
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses,
    final SyText text)
  {
    return buttonBuilder(screen)
      .setText(text)
      .setExtraThemeClasses(themeClasses)
      .build();
  }

  /**
   * Create a button with the given extra theme classes.
   *
   * @param screen       The screen that owns the component
   * @param themeClasses The theme classes in priority order
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen,
    final List<SyThemeClassNameType> themeClasses)
  {
    return buttonBuilder(screen)
      .setExtraThemeClasses(themeClasses)
      .build();
  }

  /**
   * Create a button with the given text.
   *
   * @param screen The screen that owns the component
   * @param text   The text
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen,
    final SyText text)
  {
    return buttonBuilder(screen)
      .setText(text)
      .build();
  }

  /**
   * Create a button with the given text and click listener.
   *
   * @param screen   The screen that owns the component
   * @param text     The text
   * @param listener The on-click listener
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen,
    final SyText text,
    final Runnable listener)
  {
    return buttonBuilder(screen)
      .setText(text)
      .setOnClickListener(listener)
      .build();
  }

  /**
   * Create a button with the given click listener.
   *
   * @param screen   The screen that owns the component
   * @param listener The on-click listener
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen,
    final Runnable listener)
  {
    return buttonBuilder(screen)
      .setOnClickListener(listener)
      .build();
  }

  /**
   * Create a button.
   *
   * @param screen The screen that owns the component
   *
   * @return A new button
   */

  public static SyButtonWithTextType button(
    final SyScreenType screen)
  {
    return buttonBuilder(screen)
      .build();
  }

  /**
   * Create a new button builder.
   *
   * @param screen The screen that owns the component
   *
   * @return A builder
   */

  public static SyButtonBuilderType buttonBuilder(
    final SyScreenType screen)
  {
    return new SyButtonBuilder(screen);
  }

  @Override
  protected SyEventConsumed onOtherEvent(
    final SyEventType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  protected void onClicked()
  {

  }

  @Override
  public AttributeType<SyText> text()
  {
    return this.text.text();
  }

  @Override
  public void setText(
    final SyText newText)
  {
    this.text.setText(newText);
  }

  private static final class SyButtonBuilder
    extends SyComponentBuilderAbstract<SyButtonBuilderType>
    implements SyButtonBuilderType
  {
    private List<SyThemeClassNameType> themeClasses;
    private SyText text;
    private Runnable onClick;

    SyButtonBuilder(
      final SyScreenType inScreen)
    {
      super(inScreen);

      this.themeClasses = List.of();
      this.text = SyText.empty();
      this.onClick = () -> {

      };
    }

    @Override
    public SyButtonBuilderType setExtraThemeClasses(
      final List<SyThemeClassNameType> classes)
    {
      this.themeClasses =
        List.copyOf(Objects.requireNonNull(classes, "classes"));
      return this;
    }

    @Override
    public SyButtonBuilderType setText(
      final SyText newText)
    {
      this.text = Objects.requireNonNull(newText, "text");
      return this;
    }

    @Override
    public SyButtonBuilderType setOnClickListener(
      final Runnable onClickListener)
    {
      this.onClick = Objects.requireNonNull(onClickListener, "runnable");
      return this;
    }

    @Override
    public SyButtonWithTextType build()
    {
      final var button =
        new SyButton(
          this.screen(),
          this.themeClasses
        );

      button.setText(this.text);
      button.setOnClickListener(this.onClick);
      return button;
    }
  }
}
