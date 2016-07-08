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

package com.io7m.jsycamore.core.renderer;

import com.io7m.jfunctional.Unit;
import com.io7m.jnull.NullCheck;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.core.components.SyButtonReadableType;
import com.io7m.jsycamore.core.components.SyComponentReadableType;
import com.io7m.jsycamore.core.components.SyPanelReadableType;
import com.io7m.jsycamore.core.components.SyWindowViewportAccumulatorType;
import com.io7m.jtensors.VectorReadable2IType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;

public final class SyComponentRendererAWT implements
  SyComponentRendererType<SyComponentRendererAWTContextType, BufferedImage>
{
  private SyComponentRendererAWT()
  {

  }

  public static SyComponentRendererType<
    SyComponentRendererAWTContextType, BufferedImage> create()
  {
    return new SyComponentRendererAWT();
  }

  @Override
  public BufferedImage render(
    final SyComponentRendererAWTContextType context,
    final SyComponentReadableType object)
  {
    NullCheck.notNull(context);
    NullCheck.notNull(object);

    final BufferedImage image = context.image();
    final Graphics2D graphics = image.createGraphics();
    this.renderActual(context, graphics, object);
    return image;
  }

  private void renderActual(
    final SyComponentRendererAWTContextType context,
    final Graphics2D graphics,
    final SyComponentReadableType object)
  {
    final SyWindowViewportAccumulatorType viewport = context.viewport();
    final AffineTransform saved_transform = graphics.getTransform();
    final Shape saved_clip = graphics.getClip();

    try {
      viewport.accumulate(object.position(), object.size());

      final int max_x = viewport.maximumX();
      final int min_x = viewport.minimumX();
      final int max_y = viewport.maximumY();
      final int min_y = viewport.minimumY();
      final int width = max_x - min_x;
      final int height = max_y - min_y;
      graphics.clipRect(min_x, min_y, width, height);
      graphics.translate(min_x, min_y);

      object.matchComponentReadable(this, (r, button) -> {
        SyComponentRendererAWT.renderButton(context, graphics, button);
        return Unit.unit();
      }, (r, panel) -> {
        SyComponentRendererAWT.renderPanel(context, graphics, panel);
        return Unit.unit();
      });

      final JOTreeNodeReadableType<SyComponentReadableType> node =
        object.nodeReadable();
      final Collection<JOTreeNodeReadableType<SyComponentReadableType>> child_nodes =
        node.childrenReadable();

      for (final JOTreeNodeReadableType<SyComponentReadableType> child_node : child_nodes) {
        this.renderActual(context, graphics, child_node.value());
      }

    } finally {
      viewport.restore();
      graphics.setTransform(saved_transform);
      graphics.setClip(saved_clip);
    }
  }

  private static void renderPanel(
    final SyComponentRendererAWTContextType context,
    final Graphics2D graphics,
    final SyPanelReadableType panel)
  {
    final VectorReadable2IType size = panel.size();
    graphics.setPaint(Color.MAGENTA);
    graphics.fillRect(0, 0, size.getXI(), size.getYI());
  }

  private static void renderButton(
    final SyComponentRendererAWTContextType context,
    final Graphics2D graphics,
    final SyButtonReadableType button)
  {
    final VectorReadable2IType size = button.size();
    graphics.setPaint(Color.GREEN);
    graphics.fillRect(0, 0, size.getXI(), size.getYI());
  }
}
