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

package com.io7m.jsycamore.awt;

import com.io7m.jsycamore.api.text.SyTextMeasurementType;
import com.io7m.jsycamore.api.themes.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.themes.SyAlignmentVertical;

import java.awt.Graphics2D;

final class SyAWTTextRenderer
{
  private SyAWTTextRenderer()
  {

  }

  static void renderText(
    final SyTextMeasurementType measurement,
    final Graphics2D graphics,
    final String font,
    final int label_width,
    final int label_height,
    final SyAlignmentHorizontal align_h,
    final SyAlignmentVertical align_v,
    final String text)
  {
    final int text_width =
      measurement.measureTextWidth(font, text);
    final int text_height =
      measurement.measureTextHeight(font);
    final int text_descent =
      measurement.measureTextDescent(font);

    final int label_half_width = label_width / 2;
    final int label_half_height = label_height / 2;
    final int text_half_width = text_width / 2;
    final int text_half_height = text_height / 2;

    int x = 0;
    switch (align_h) {
      case ALIGN_LEFT: {
        x = 0;
        break;
      }
      case ALIGN_RIGHT: {
        x = Math.max(0, label_width - text_width);
        break;
      }
      case ALIGN_CENTER: {
        x = Math.max(0, label_half_width - text_half_width);
        break;
      }
    }

    int y = 0;
    switch (align_v) {
      case ALIGN_TOP: {
        y = text_height;
        break;
      }
      case ALIGN_BOTTOM: {
        y = label_height;
        break;
      }
      case ALIGN_CENTER: {
        y = Math.max(0, label_half_height + (text_half_height - text_descent));
        break;
      }
    }

    graphics.drawString(text, x, y);
  }
}
