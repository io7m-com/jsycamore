package com.io7m.jsycamore;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Type representing a keyboard event.
 */

public final class KeyEvent
{
  /**
   * Key type representing the keys on a standard US-International keyboard.
   */

  public static enum Key
  {
    KEY_1,
    KEY_2,
    KEY_3,
    KEY_4,
    KEY_5,
    KEY_6,
    KEY_7,
    KEY_8,
    KEY_9,
    KEY_0,

    KEY_MINUS,
    KEY_EQUALS,
    KEY_BACKSPACE,

    KEY_A,
    KEY_B,
    KEY_C,
    KEY_D,
    KEY_E,
    KEY_F,
    KEY_G,
    KEY_H,
    KEY_I,
    KEY_J,
    KEY_K,
    KEY_L,
    KEY_M,
    KEY_N,
    KEY_O,
    KEY_P,
    KEY_Q,
    KEY_R,
    KEY_S,
    KEY_T,
    KEY_U,
    KEY_V,
    KEY_W,
    KEY_X,
    KEY_Y,
    KEY_Z,

    KEY_BRACKET_LEFT,
    KEY_BRACKET_RIGHT,
    KEY_SEMICOLON,
    KEY_QUOTE,
    KEY_HASH,
    KEY_ANGLE_LEFT,
    KEY_ANGLE_RIGHT,
    KEY_SLASH,
    KEY_BACKSLASH,
    KEY_BACKQUOTE,

    KEY_TAB,
    KEY_ESCAPE,
    KEY_F1,
    KEY_F2,
    KEY_F3,
    KEY_F4,
    KEY_F5,
    KEY_F6,
    KEY_F7,
    KEY_F8,
    KEY_F9,
    KEY_F10,
    KEY_F11,
    KEY_F12,

    KEY_INSERT,
    KEY_DELETE,
    KEY_HOME,
    KEY_END,
    KEY_PAGE_UP,
    KEY_PAGE_DOWN,

    KEY_ARROW_UP,
    KEY_ARROW_LEFT,
    KEY_ARROW_DOWN,
    KEY_ARROW_RIGHT,

    KEY_KEYPAD_0,
    KEY_KEYPAD_1,
    KEY_KEYPAD_2,
    KEY_KEYPAD_3,
    KEY_KEYPAD_4,
    KEY_KEYPAD_5,
    KEY_KEYPAD_6,
    KEY_KEYPAD_7,
    KEY_KEYPAD_8,
    KEY_KEYPAD_9,

    KEY_KEYPAD_SLASH,
    KEY_KEYPAD_STAR,
    KEY_KEYPAD_MINUS,
    KEY_KEYPAD_PLUS,
    KEY_KEYPAD_ENTER,
    KEY_KEYPAD_DOT,

    KEY_UNKNOWN
  }

  /**
   * Type representing the modifier keys on a standard US-International
   * keyboard.
   */

  public static enum Modifier
  {
    MODIFIER_LEFT_SHIFT,
    MODIFIER_LEFT_CONTROL,
    MODIFIER_LEFT_ALT,

    MODIFIER_RIGHT_SHIFT,
    MODIFIER_RIGHT_CONTROL,
    MODIFIER_RIGHT_ALT
  }

  private final Key               key;
  private final EnumSet<Modifier> modifiers;
  private final int               character;

  public KeyEvent(
    final @Nonnull Key key,
    final @Nonnull EnumSet<Modifier> modifiers,
    final int character)
    throws ConstraintError
  {
    this.key = Constraints.constrainNotNull(key, "Key");
    this.modifiers = Constraints.constrainNotNull(modifiers, "Modifiers");
    this.character = character;
  }

  public int getCharacter()
  {
    return this.character;
  }

  public @Nonnull Key getKey()
  {
    return this.key;
  }

  public @Nonnull EnumSet<Modifier> getModifiers()
  {
    return this.modifiers;
  }
}
