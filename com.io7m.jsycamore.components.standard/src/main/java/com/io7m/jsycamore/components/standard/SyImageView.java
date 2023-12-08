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


package com.io7m.jsycamore.components.standard;

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jsycamore.api.components.SyImageViewType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;

/**
 * An image view.
 */

public final class SyImageView
  extends SyComponentAbstract implements SyImageViewType
{
  private final AttributeType<Optional<URI>> imageURI;

  /**
   * An image view.
   *
   * @param inScreen            The screen that owns the component
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyImageView(
    final SyScreenType inScreen,
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(inScreen, inThemeClassesExtra);
    final var attributes = SyComponentAttributes.get();
    this.imageURI = attributes.create(Optional.empty());
  }

  /**
   * An image view.
   *
   * @param inScreen The screen that owns the component
   */

  @ConvenienceConstructor
  public SyImageView(final SyScreenType inScreen)
  {
    this(inScreen, List.of());
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return EVENT_NOT_CONSUMED;
  }

  @Override
  public AttributeType<Optional<URI>> imageURI()
  {
    return this.imageURI;
  }
}
