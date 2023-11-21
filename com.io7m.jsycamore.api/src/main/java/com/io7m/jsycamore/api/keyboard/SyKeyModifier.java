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


package com.io7m.jsycamore.api.keyboard;

import java.util.Optional;

/**
 * A modifier key.
 */

public enum SyKeyModifier implements SyKeyModifierType
{
  /**
   * The left shift key.
   */

  SY_MODIFIER_SHIFT_LEFT {
    @Override
    public boolean isShift()
    {
      return true;
    }

    @Override
    public boolean isControl()
    {
      return false;
    }

    @Override
    public boolean isAlt()
    {
      return false;
    }

    @Override
    public boolean isSuper()
    {
      return false;
    }
  },

  /**
   * The right shift key.
   */

  SY_MODIFIER_SHIFT_RIGHT {
    @Override
    public boolean isShift()
    {
      return true;
    }

    @Override
    public boolean isControl()
    {
      return false;
    }

    @Override
    public boolean isAlt()
    {
      return false;
    }

    @Override
    public boolean isSuper()
    {
      return false;
    }
  },

  /**
   * The left control key.
   */

  SY_MODIFIER_CONTROL_LEFT {
    @Override
    public boolean isShift()
    {
      return false;
    }

    @Override
    public boolean isControl()
    {
      return true;
    }

    @Override
    public boolean isAlt()
    {
      return false;
    }

    @Override
    public boolean isSuper()
    {
      return false;
    }
  },

  /**
   * The right control key.
   */

  SY_MODIFIER_CONTROL_RIGHT {
    @Override
    public boolean isShift()
    {
      return false;
    }

    @Override
    public boolean isControl()
    {
      return true;
    }

    @Override
    public boolean isAlt()
    {
      return false;
    }

    @Override
    public boolean isSuper()
    {
      return false;
    }
  },

  /**
   * The left alt key.
   */

  SY_MODIFIER_ALT_LEFT {
    @Override
    public boolean isShift()
    {
      return false;
    }

    @Override
    public boolean isControl()
    {
      return false;
    }

    @Override
    public boolean isAlt()
    {
      return true;
    }

    @Override
    public boolean isSuper()
    {
      return false;
    }
  },

  /**
   * The right alt key.
   */

  SY_MODIFIER_ALT_RIGHT {
    @Override
    public boolean isShift()
    {
      return false;
    }

    @Override
    public boolean isControl()
    {
      return false;
    }

    @Override
    public boolean isAlt()
    {
      return true;
    }

    @Override
    public boolean isSuper()
    {
      return false;
    }
  },

  /**
   * The left super key.
   */

  SY_MODIFIER_SUPER_LEFT {
    @Override
    public boolean isShift()
    {
      return false;
    }

    @Override
    public boolean isControl()
    {
      return false;
    }

    @Override
    public boolean isAlt()
    {
      return false;
    }

    @Override
    public boolean isSuper()
    {
      return true;
    }
  },

  /**
   * The right super key.
   */

  SY_MODIFIER_SUPER_RIGHT {
    @Override
    public boolean isShift()
    {
      return false;
    }

    @Override
    public boolean isControl()
    {
      return false;
    }

    @Override
    public boolean isAlt()
    {
      return false;
    }

    @Override
    public boolean isSuper()
    {
      return true;
    }
  };

  /**
   * @param keyCode The input keycode
   *
   * @return The key modifier associated with the keycode, if any
   */

  public static Optional<SyKeyModifier> ofKey(
    final SyKeyCode keyCode)
  {
    return switch (keyCode) {
      case SY_KEY_LEFT_ALT -> Optional.of(SY_MODIFIER_ALT_LEFT);
      case SY_KEY_LEFT_CONTROL -> Optional.of(SY_MODIFIER_CONTROL_LEFT);
      case SY_KEY_LEFT_SHIFT -> Optional.of(SY_MODIFIER_SHIFT_LEFT);
      case SY_KEY_LEFT_SUPER -> Optional.of(SY_MODIFIER_SUPER_LEFT);
      case SY_KEY_RIGHT_ALT -> Optional.of(SY_MODIFIER_ALT_RIGHT);
      case SY_KEY_RIGHT_CONTROL -> Optional.of(SY_MODIFIER_CONTROL_RIGHT);
      case SY_KEY_RIGHT_SHIFT -> Optional.of(SY_MODIFIER_SHIFT_RIGHT);
      case SY_KEY_RIGHT_SUPER -> Optional.of(SY_MODIFIER_SUPER_RIGHT);
      default -> Optional.empty();
    };
  }
}
