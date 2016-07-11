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

package com.io7m.jsycamore.tests.core;

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SySpaceParentRelativeType;
import com.io7m.jsycamore.core.SySpaceViewportType;
import com.io7m.jsycamore.core.SyWindowContentPaneType;
import com.io7m.jsycamore.core.SyWindowFrameType;
import com.io7m.jsycamore.core.SyWindowTitlebarType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.components.SyComponentType;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeButton;
import com.io7m.jsycamore.core.themes.SyThemeLabel;
import com.io7m.jsycamore.core.themes.SyThemePanel;
import com.io7m.jsycamore.core.themes.SyThemeWindow;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarVerticalPlacement;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarWidthBehavior;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.parameterized.PVectorReadable2IType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public abstract class SyWindowContract
{
  private static final long FRAME_RIGHT_WIDTH = 4L;
  private static final long FRAME_LEFT_WIDTH = 2L;
  private static final long FRAME_TOP_HEIGHT = 5L;
  private static final long FRAME_BOTTOM_HEIGHT = 3L;
  private static final long TITLEBAR_HEIGHT = 16L;

  private static SyTheme createStandardTheme(
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical,
    final SyThemeWindowTitlebarWidthBehavior titlebar_width,
    final SyAlignmentHorizontal titlebar_horizontal)
  {
    final SyThemeWindowTitleBar.Builder theme_win_title = SyThemeWindowTitleBar.builder();
    theme_win_title.setVerticalPlacement(titlebar_vertical);
    theme_win_title.setWidthBehavior(titlebar_width);
    theme_win_title.setHorizontalAlignment(titlebar_horizontal);
    theme_win_title.setHeight((int) SyWindowContract.TITLEBAR_HEIGHT);

    final SyThemeWindowFrame.Builder theme_win_frame = SyThemeWindowFrame.builder();
    theme_win_frame.setRightWidth(
      (int) SyWindowContract.FRAME_RIGHT_WIDTH);
    theme_win_frame.setLeftWidth(
      (int) SyWindowContract.FRAME_LEFT_WIDTH);
    theme_win_frame.setTopHeight(
      (int) SyWindowContract.FRAME_TOP_HEIGHT);
    theme_win_frame.setBottomHeight(
      (int) SyWindowContract.FRAME_BOTTOM_HEIGHT);

    final SyThemeWindow.Builder theme_win = SyThemeWindow.builder();
    theme_win.setTitleBar(theme_win_title.build());
    theme_win.setFrame(theme_win_frame.build());
    theme_win.setOutline(Optional.empty());

    final SyTheme.Builder theme_b = SyTheme.builder();
    theme_b.setWindowTheme(theme_win.build());
    theme_b.setPanelTheme(SyThemePanel.builder().build());
    theme_b.setButtonTheme(SyThemeButton.builder().build());
    theme_b.setLabelTheme(SyThemeLabel.builder().build());
    return theme_b.build();
  }

  protected abstract SyWindowType create(
    int width,
    int height,
    String title);

  @Test
  public final void testCreate()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");
    final VectorReadable2IType bounds = w.bounds();
    Assert.assertEquals(640L, (long) bounds.getXI());
    Assert.assertEquals(480L, (long) bounds.getYI());
    final PVectorReadable2IType<SySpaceViewportType> position = w.position();
    Assert.assertEquals(0L, (long) position.getXI());
    Assert.assertEquals(0L, (long) position.getYI());

    final SyWindowTitlebarType titlebar = w.titlebar();
    Assert.assertEquals("Main 0", titlebar.text());

    Assert.assertTrue(w.focused());
  }

  @Test
  public final void testBoundsTitleAboveFrameWidthWindowCenter()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_CENTER;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        480L - SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleAboveFrameWidthWindowLeft()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_LEFT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        480L - SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleAboveFrameWidthWindowRight()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_RIGHT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        480L - SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleAboveFrameWidthInsideFrameLeft()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_LEFT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(
        480L - SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleAboveFrameWidthInsideFrameRight()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_RIGHT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      long expected_x = 640L;
      expected_x -= 640L;
      expected_x = expected_x + SyWindowContract.FRAME_LEFT_WIDTH;
      expected_x = expected_x + SyWindowContract.FRAME_RIGHT_WIDTH;

      Assert.assertEquals(
        expected_x,
        (long) position.getXI());
      Assert.assertEquals(
        0L,
        (long) position.getYI());
      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(
        0L,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L,
        (long) size.getXI());
      Assert.assertEquals(
        480L - SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleAboveFrameWidthInsideFrameCenter()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_ABOVE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_CENTER;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      final long expected_x = (640L / 2L) - ((long) size.getXI() / 2L);

      Assert.assertEquals(
        expected_x,
        (long) position.getXI());
      Assert.assertEquals(
        0L,
        (long) position.getYI());
      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(
        0L,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L,
        (long) size.getXI());
      Assert.assertEquals(
        480L - SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleInsideFrameWidthInsideFrameCenter()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_CENTER;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleInsideFrameWidthInsideFrameLeft()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_LEFT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleInsideFrameWidthInsideFrameRight()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_RIGHT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleInsideFrameWidthWindowLeft()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_LEFT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(
        0,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L,
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleInsideFrameWidthWindowCenter()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_CENTER;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(
        0,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L,
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testBoundsTitleInsideFrameWidthWindowRight()
  {
    final SyThemeWindowTitlebarVerticalPlacement titlebar_vertical =
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME;
    final SyThemeWindowTitlebarWidthBehavior titlebar_width =
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW;
    final SyAlignmentHorizontal titlebar_horizontal =
      SyAlignmentHorizontal.ALIGN_RIGHT;

    final SyTheme theme = SyWindowContract.createStandardTheme(
      titlebar_vertical, titlebar_width, titlebar_horizontal);

    final SyWindowType w = this.create(640, 480, "Main 0");
    w.setTheme(Optional.of(theme));

    {
      final VectorReadable2IType size = w.bounds();
      final PVectorReadable2IType<SySpaceViewportType> position = w.position();
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
    }

    {
      final SyWindowTitlebarType titlebar = w.titlebar();
      Assert.assertEquals("Main 0", titlebar.text());

      final VectorReadable2IType size = titlebar.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = titlebar.position();

      Assert.assertEquals(
        0,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT,
        (long) position.getYI());
      Assert.assertEquals(
        640L,
        (long) size.getXI());
      Assert.assertEquals(
        SyWindowContract.TITLEBAR_HEIGHT,
        (long) size.getYI());
    }

    {
      final SyWindowFrameType frame = w.frame();
      final VectorReadable2IType size = frame.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = frame.position();

      Assert.assertEquals(0L, (long) position.getXI());
      Assert.assertEquals(0L, (long) position.getYI());
      Assert.assertEquals(640L, (long) size.getXI());
      Assert.assertEquals(480L, (long) size.getYI());
    }

    {
      final SyComponentType content = w.contentPane();
      final VectorReadable2IType size = content.size();
      final PVectorReadable2IType<SySpaceParentRelativeType> position = content.position();

      Assert.assertEquals(
        640L - (SyWindowContract.FRAME_LEFT_WIDTH + SyWindowContract.FRAME_RIGHT_WIDTH),
        (long) size.getXI());
      Assert.assertEquals(
        480L - (SyWindowContract.FRAME_BOTTOM_HEIGHT + SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT),
        (long) size.getYI());
      Assert.assertEquals(
        SyWindowContract.FRAME_LEFT_WIDTH,
        (long) position.getXI());
      Assert.assertEquals(
        SyWindowContract.FRAME_TOP_HEIGHT + SyWindowContract.TITLEBAR_HEIGHT,
        (long) position.getYI());
    }
  }

  @Test
  public final void testSetTitle()
  {
    final SyWindowType w = this.create(640, 480, "Main 0");

    final SyWindowTitlebarType titlebar = w.titlebar();
    Assert.assertEquals("Main 0", titlebar.text());

    titlebar.setText("Main 1");
    Assert.assertEquals("Main 1", titlebar.text());
  }
}
