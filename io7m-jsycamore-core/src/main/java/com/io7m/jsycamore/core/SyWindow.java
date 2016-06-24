/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jsycamore.core;

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVector2IType;
import com.io7m.jtensors.parameterized.PVectorM2I;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import net.jcip.annotations.NotThreadSafe;
import org.valid4j.Assertive;

/**
 * The default implementation of the {@link SyWindowType} type.
 */

@NotThreadSafe
public final class SyWindow implements SyWindowType
{
  private final PVector2IType<SySpaceViewportType> position;
  private final Titlebar titlebar;
  private final String to_string;
  private final PVectorM2I<SySpaceWindowRelativeType> frame_size;
  private final PVectorM2I<SySpaceWindowRelativeType> frame_position;
  private final PVectorM2I<SySpaceWindowRelativeType> titlebar_size;
  private final PVectorM2I<SySpaceWindowRelativeType> titlebar_position;
  private final SyTextMeasurementType measure;
  private SyGraph<SyComponentType, SyComponentLink> components;
  private VectorM2I bounds;
  private SyThemeType theme;
  private boolean active;
  private String text;

  private SyWindow(
    final SyTextMeasurementType in_measure,
    final SyThemeType in_theme)
  {
    NullCheck.notNull(in_theme);

    this.measure = NullCheck.notNull(in_measure);
    this.to_string = "[SyWindow " + this.hashCode() + "]";
    this.position = new PVectorM2I<>();
    this.bounds = new VectorM2I();
    this.frame_size = new PVectorM2I<>();
    this.frame_position = new PVectorM2I<>();
    this.titlebar_size = new PVectorM2I<>();
    this.titlebar_position = new PVectorM2I<>();
    this.components = new SyGraph<>(SyComponentLink::new);
    this.titlebar = new Titlebar();
    this.text = "";
    this.themeReload(in_theme);
  }

  /**
   * Create a new window.
   *
   * @param in_measure A reference to a text measurement interface
   * @param in_theme   A reference to the current theme
   *
   * @return A new window
   */

  public static SyWindowType create(
    final SyTextMeasurementType in_measure,
    final SyThemeType in_theme)
  {
    return new SyWindow(in_measure, in_theme);
  }

  @Override
  public String text()
  {
    return this.text;
  }

  @Override
  public void setText(final String in_text)
  {
    this.text = NullCheck.notNull(in_text);
    this.setBounds(this.bounds.getXI(), this.bounds.getYI());
  }

  @Override
  public boolean active()
  {
    return this.active;
  }

  @Override
  public void setActive(final boolean in_active)
  {
    this.active = in_active;
  }

  @Override
  public String toString()
  {
    return this.to_string;
  }

  private void themeReload(final SyThemeType new_theme)
  {
    this.theme = new_theme;
  }

  @Override
  public SyGraph<SyComponentType, SyComponentLink> components()
  {
    return this.components;
  }

  @Override
  public VectorReadable2IType bounds()
  {
    return this.bounds;
  }

  @Override
  public SyThemeType theme()
  {
    return this.theme;
  }

  @Override
  public void setBounds(
    final int width,
    final int height)
  {
    final int clamp_width = Math.max(width, 2);
    final int clamp_height = Math.max(height, 2);

    final SyThemeWindowType window_theme = this.theme.windowTheme();
    final SyThemeWindowFrameType frame_theme = window_theme.frame();
    final SyThemeWindowTitleBarType title_theme = window_theme.titleBar();

    int title_x = 0;
    int title_y = 0;
    int title_width = clamp_width;
    final int title_height = title_theme.height();

    final int frame_x = 0;
    int frame_y = 0;
    final int frame_width = clamp_width;
    int frame_height = clamp_height;

    final int frame_left = frame_theme.leftWidth();
    final int frame_right = frame_theme.rightWidth();

    final String text_font = title_theme.textFont();
    switch (title_theme.verticalPlacement()) {
      case PLACEMENT_TOP_INSIDE_FRAME: {
        title_y = frame_theme.topHeight();

        switch (title_theme.widthBehavior()) {
          case WIDTH_RESIZE_TO_CONTENT: {
            title_width = this.measureTitleSize(text_font);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }
          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width = frame_width - (frame_left + frame_right);
            title_x = frame_left;
            break;
          }
          case WIDTH_RESIZE_TO_WINDOW: {
            break;
          }
        }
        break;
      }

      case PLACEMENT_TOP_OVERLAP_FRAME: {
        switch (title_theme.widthBehavior()) {
          case WIDTH_RESIZE_TO_CONTENT: {
            title_width = this.measureTitleSize(text_font);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width =
              frame_width - (frame_left + frame_right);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_TO_WINDOW: {
            break;
          }
        }
        break;
      }

      case PLACEMENT_TOP_ABOVE_FRAME: {
        frame_height -= title_height;
        frame_y += title_height;

        switch (title_theme.widthBehavior()) {
          case WIDTH_RESIZE_TO_CONTENT: {
            title_width = this.measureTitleSize(text_font);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_INSIDE_FRAME: {
            title_width =
              frame_width - (frame_left + frame_right);

            switch (title_theme.horizontalAlignment()) {
              case ALIGN_LEFT: {
                break;
              }
              case ALIGN_RIGHT: {
                title_x = clamp_width - title_width;
                break;
              }
              case ALIGN_CENTER: {
                title_x = (clamp_width / 2) - (title_width / 2);
                break;
              }
            }
            break;
          }

          case WIDTH_RESIZE_TO_WINDOW: {
            break;
          }
        }
        break;
      }
    }

    Assertive.ensure(clamp_width >= 2);
    Assertive.ensure(clamp_height >= 2);
    Assertive.ensure(title_width >= 2);
    Assertive.ensure(frame_width >= 2);
    Assertive.ensure(frame_height >= 2);

    final int orig_width = this.bounds.getXI();
    final int orig_height = this.bounds.getYI();
    if (orig_width != clamp_width && orig_height != clamp_height) {
      // XXX: Notify top-level components
    }

    this.bounds.set2I(clamp_width, clamp_height);
    this.titlebar_position.set2I(title_x, title_y);
    this.titlebar_size.set2I(title_width, title_height);
    this.frame_position.set2I(frame_x, frame_y);
    this.frame_size.set2I(frame_width, frame_height);
  }

  private int measureTitleSize(final String text_font)
  {
    final int text_size =
      this.measure.measureText(text_font, this.text);
    final int space_size =
      this.measure.measureText(text_font, " ");
    return (space_size * 2) + text_size;
  }

  @Override
  public PVectorReadable2IType<SySpaceWindowRelativeType> framePosition()
  {
    return this.frame_position;
  }

  @Override
  public PVectorReadable2IType<SySpaceWindowRelativeType> frameBounds()
  {
    return this.frame_size;
  }

  @Override
  public PVectorReadable2IType<SySpaceWindowRelativeType> titlebarPosition()
  {
    return this.titlebar_position;
  }

  @Override
  public PVectorReadable2IType<SySpaceWindowRelativeType> titlebarBounds()
  {
    return this.titlebar_size;
  }

  @Override
  public PVectorReadable2IType<SySpaceViewportType> position()
  {
    return this.position;
  }

  private final class Titlebar
  {
    private int height;
  }
}
