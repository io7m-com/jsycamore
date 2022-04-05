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


package com.io7m.jsycamore.awt.internal;

import com.io7m.jsycamore.api.keyboard.SyKeyCode;
import com.io7m.jsycamore.api.keyboard.SyKeyCodeMappingType;

import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_0;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_1;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_2;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_3;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_4;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_5;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_6;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_7;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_8;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_9;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_A;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_APOSTROPHE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_B;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_BACKSLASH;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_BACKSPACE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_C;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_CAPS_LOCK;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_COMMA;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_D;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_DELETE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_DOWN;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_E;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_END;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_ENTER;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_EQUAL;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_ESCAPE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F1;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F10;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F11;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F12;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F13;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F14;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F15;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F16;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F17;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F18;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F19;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F2;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F20;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F21;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F22;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F23;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F24;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F3;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F4;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F5;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F6;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F7;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F8;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_F9;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_G;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_GRAVE_ACCENT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_H;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_HOME;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_I;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_INSERT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_J;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_K;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_0;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_1;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_2;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_3;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_4;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_5;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_6;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_7;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_8;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_9;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_ADD;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_DECIMAL;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_DIVIDE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_ENTER;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_EQUAL;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_MULTIPLY;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_KP_SUBTRACT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_L;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_LEFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_LEFT_ALT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_LEFT_BRACKET;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_LEFT_CONTROL;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_LEFT_SHIFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_M;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_MINUS;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_N;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_NUM_LOCK;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_O;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_P;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_PAGE_DOWN;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_PAGE_UP;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_PAUSE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_PERIOD;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_PRINT_SCREEN;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_Q;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_R;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_RIGHT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_RIGHT_ALT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_RIGHT_BRACKET;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_RIGHT_CONTROL;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_RIGHT_SHIFT;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_S;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_SCROLL_LOCK;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_SEMICOLON;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_SLASH;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_SPACE;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_T;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_TAB;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_U;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_UNKNOWN;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_UP;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_V;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_W;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_X;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_Y;
import static com.io7m.jsycamore.api.keyboard.SyKeyCode.SY_KEY_Z;
import static java.awt.event.KeyEvent.KEY_LOCATION_NUMPAD;
import static java.awt.event.KeyEvent.KEY_LOCATION_RIGHT;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_ADD;
import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CAPS_LOCK;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DECIMAL;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_DIVIDE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_END;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_F1;
import static java.awt.event.KeyEvent.VK_F10;
import static java.awt.event.KeyEvent.VK_F11;
import static java.awt.event.KeyEvent.VK_F12;
import static java.awt.event.KeyEvent.VK_F13;
import static java.awt.event.KeyEvent.VK_F14;
import static java.awt.event.KeyEvent.VK_F15;
import static java.awt.event.KeyEvent.VK_F16;
import static java.awt.event.KeyEvent.VK_F17;
import static java.awt.event.KeyEvent.VK_F18;
import static java.awt.event.KeyEvent.VK_F19;
import static java.awt.event.KeyEvent.VK_F2;
import static java.awt.event.KeyEvent.VK_F20;
import static java.awt.event.KeyEvent.VK_F21;
import static java.awt.event.KeyEvent.VK_F22;
import static java.awt.event.KeyEvent.VK_F23;
import static java.awt.event.KeyEvent.VK_F24;
import static java.awt.event.KeyEvent.VK_F3;
import static java.awt.event.KeyEvent.VK_F4;
import static java.awt.event.KeyEvent.VK_F5;
import static java.awt.event.KeyEvent.VK_F6;
import static java.awt.event.KeyEvent.VK_F7;
import static java.awt.event.KeyEvent.VK_F8;
import static java.awt.event.KeyEvent.VK_F9;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_HOME;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_INSERT;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_MULTIPLY;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_NUMPAD0;
import static java.awt.event.KeyEvent.VK_NUMPAD1;
import static java.awt.event.KeyEvent.VK_NUMPAD2;
import static java.awt.event.KeyEvent.VK_NUMPAD3;
import static java.awt.event.KeyEvent.VK_NUMPAD4;
import static java.awt.event.KeyEvent.VK_NUMPAD5;
import static java.awt.event.KeyEvent.VK_NUMPAD6;
import static java.awt.event.KeyEvent.VK_NUMPAD7;
import static java.awt.event.KeyEvent.VK_NUMPAD8;
import static java.awt.event.KeyEvent.VK_NUMPAD9;
import static java.awt.event.KeyEvent.VK_NUM_LOCK;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_PAUSE;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_PRINTSCREEN;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SCROLL_LOCK;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_SUBTRACT;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_TAB;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

/**
 * The mapping from AWT key events to keycodes.
 */

public final class SyAWTKeyCodeMapping
  implements SyKeyCodeMappingType<SyAWTKeyContext>
{
  /**
   * The mapping from AWT key events to keycodes.
   */

  public SyAWTKeyCodeMapping()
  {

  }

  @Override
  public SyKeyCode toKeyCode(
    final SyAWTKeyContext context,
    final int code)
  {
    return switch (code) {
      case VK_0 -> SY_KEY_0;
      case VK_1 -> SY_KEY_1;
      case VK_2 -> SY_KEY_2;
      case VK_3 -> SY_KEY_3;
      case VK_4 -> SY_KEY_4;
      case VK_5 -> SY_KEY_5;
      case VK_6 -> SY_KEY_6;
      case VK_7 -> SY_KEY_7;
      case VK_8 -> SY_KEY_8;
      case VK_9 -> SY_KEY_9;
      case VK_A -> SY_KEY_A;
      case VK_ADD -> SY_KEY_KP_ADD;
      case VK_B -> SY_KEY_B;
      case VK_BACK_SLASH -> SY_KEY_BACKSLASH;
      case VK_BACK_SPACE -> SY_KEY_BACKSPACE;
      case VK_C -> SY_KEY_C;
      case VK_CLOSE_BRACKET -> SY_KEY_RIGHT_BRACKET;
      case VK_D -> SY_KEY_D;
      case VK_DELETE -> SY_KEY_DELETE;
      case VK_DIVIDE -> SY_KEY_KP_DIVIDE;
      case VK_DOWN -> SY_KEY_DOWN;
      case VK_E -> SY_KEY_E;
      case VK_END -> SY_KEY_END;
      case VK_ESCAPE -> SY_KEY_ESCAPE;
      case VK_F -> SY_KEY_F;
      case VK_F1 -> SY_KEY_F1;
      case VK_F10 -> SY_KEY_F10;
      case VK_F11 -> SY_KEY_F11;
      case VK_F12 -> SY_KEY_F12;
      case VK_F13 -> SY_KEY_F13;
      case VK_F14 -> SY_KEY_F14;
      case VK_F15 -> SY_KEY_F15;
      case VK_F16 -> SY_KEY_F16;
      case VK_F17 -> SY_KEY_F17;
      case VK_F18 -> SY_KEY_F18;
      case VK_F19 -> SY_KEY_F19;
      case VK_F2 -> SY_KEY_F2;
      case VK_F20 -> SY_KEY_F20;
      case VK_F21 -> SY_KEY_F21;
      case VK_F22 -> SY_KEY_F22;
      case VK_F23 -> SY_KEY_F23;
      case VK_F24 -> SY_KEY_F24;
      case VK_F3 -> SY_KEY_F3;
      case VK_F4 -> SY_KEY_F4;
      case VK_F5 -> SY_KEY_F5;
      case VK_F6 -> SY_KEY_F6;
      case VK_F7 -> SY_KEY_F7;
      case VK_F8 -> SY_KEY_F8;
      case VK_F9 -> SY_KEY_F9;
      case VK_G -> SY_KEY_G;
      case VK_H -> SY_KEY_H;
      case VK_HOME -> SY_KEY_HOME;
      case VK_I -> SY_KEY_I;
      case VK_INSERT -> SY_KEY_INSERT;
      case VK_J -> SY_KEY_J;
      case VK_K -> SY_KEY_K;
      case VK_L -> SY_KEY_L;
      case VK_LEFT -> SY_KEY_LEFT;
      case VK_M -> SY_KEY_M;
      case VK_MINUS -> SY_KEY_MINUS;
      case VK_MULTIPLY -> SY_KEY_KP_MULTIPLY;
      case VK_N -> SY_KEY_N;
      case VK_O -> SY_KEY_O;
      case VK_OPEN_BRACKET -> SY_KEY_LEFT_BRACKET;
      case VK_P -> SY_KEY_P;
      case VK_PAGE_DOWN -> SY_KEY_PAGE_DOWN;
      case VK_PAGE_UP -> SY_KEY_PAGE_UP;
      case VK_PERIOD -> SY_KEY_PERIOD;
      case VK_PRINTSCREEN -> SY_KEY_PRINT_SCREEN;
      case VK_Q -> SY_KEY_Q;
      case VK_R -> SY_KEY_R;
      case VK_RIGHT -> SY_KEY_RIGHT;
      case VK_S -> SY_KEY_S;
      case VK_SEMICOLON -> SY_KEY_SEMICOLON;
      case VK_SLASH -> SY_KEY_SLASH;
      case VK_SPACE -> SY_KEY_SPACE;
      case VK_SUBTRACT -> SY_KEY_KP_SUBTRACT;
      case VK_T -> SY_KEY_T;
      case VK_TAB -> SY_KEY_TAB;
      case VK_U -> SY_KEY_U;
      case VK_UP -> SY_KEY_UP;
      case VK_V -> SY_KEY_V;
      case VK_W -> SY_KEY_W;
      case VK_X -> SY_KEY_X;
      case VK_Y -> SY_KEY_Y;
      case VK_Z -> SY_KEY_Z;
      case VK_COMMA -> SY_KEY_COMMA;
      case VK_QUOTE -> SY_KEY_APOSTROPHE;
      case VK_BACK_QUOTE -> SY_KEY_GRAVE_ACCENT;
      case VK_PAUSE -> SY_KEY_PAUSE;
      case VK_NUMPAD0 -> SY_KEY_KP_0;
      case VK_NUMPAD1 -> SY_KEY_KP_1;
      case VK_NUMPAD2 -> SY_KEY_KP_2;
      case VK_NUMPAD3 -> SY_KEY_KP_3;
      case VK_NUMPAD4 -> SY_KEY_KP_4;
      case VK_NUMPAD5 -> SY_KEY_KP_5;
      case VK_NUMPAD6 -> SY_KEY_KP_6;
      case VK_NUMPAD7 -> SY_KEY_KP_7;
      case VK_NUMPAD8 -> SY_KEY_KP_8;
      case VK_NUMPAD9 -> SY_KEY_KP_9;
      case VK_DECIMAL -> SY_KEY_KP_DECIMAL;

      case VK_ENTER -> switch (context.keyLocation()) {
        case KEY_LOCATION_NUMPAD -> SY_KEY_KP_ENTER;
        default -> SY_KEY_ENTER;
      };

      case VK_EQUALS -> switch (context.keyLocation()) {
        case KEY_LOCATION_NUMPAD -> SY_KEY_KP_EQUAL;
        default -> SY_KEY_EQUAL;
      };

      case VK_SHIFT -> switch (context.keyLocation()) {
        case KEY_LOCATION_RIGHT -> SY_KEY_RIGHT_SHIFT;
        default -> SY_KEY_LEFT_SHIFT;
      };

      case VK_ALT -> switch (context.keyLocation()) {
        case KEY_LOCATION_RIGHT -> SY_KEY_RIGHT_ALT;
        default -> SY_KEY_LEFT_ALT;
      };

      case VK_CONTROL -> switch (context.keyLocation()) {
        case KEY_LOCATION_RIGHT -> SY_KEY_RIGHT_CONTROL;
        default -> SY_KEY_LEFT_CONTROL;
      };

      case VK_CAPS_LOCK -> SY_KEY_CAPS_LOCK;
      case VK_SCROLL_LOCK -> SY_KEY_SCROLL_LOCK;
      case VK_NUM_LOCK -> SY_KEY_NUM_LOCK;


      default -> SY_KEY_UNKNOWN;
    };
  }
}
