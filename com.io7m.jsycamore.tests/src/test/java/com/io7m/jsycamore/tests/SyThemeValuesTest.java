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

import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyConstantType.SyConstantColor4DType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyConstantType.SyConstantDoubleType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyConstantType.SyConstantFontType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyConstantType.SyConstantIntegerType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyFunctionType.SyFunctionColor4DType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyFunctionType.SyFunctionDoubleType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyFunctionType.SyFunctionFontType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyFunctionType.SyFunctionIntegerType;
import com.io7m.jsycamore.theme.spi.SyThemeValues;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.jtensors.core.parameterized.vectors.PVectors4D;
import org.junit.jupiter.api.Test;

import static com.io7m.jsycamore.api.text.SyFontStyle.REGULAR;
import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_ALREADY_EXISTS;
import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_DOES_NOT_EXIST;
import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_TYPE_INCORRECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SyThemeValuesTest
{
  private static final PVector4D<SySpaceRGBAPreType> ZERO =
    PVector4D.of(0.0, 0.0, 0.0, 0.0);
  private static final PVector4D<SySpaceRGBAPreType> ONE =
    PVector4D.of(1.0, 1.0, 1.0, 1.0);
  private static final PVector4D<SySpaceRGBAPreType> HALF =
    PVector4D.of(0.5, 0.5, 0.5, 0.5);

  @Test
  public void testEmpty()
  {
    final var attributes =
      SyThemeValues.builder()
        .create();

    assertEquals(0, attributes.values().size());
  }

  @Test
  public void testNodeExistsConstantColor4D()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantColor4D("x", "y", ONE);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createConstantColor4D("x", "y", ONE);
      });

    assertEquals(VALUE_ALREADY_EXISTS, ex.errorCode());
  }

  @Test
  public void testNodeNonexistentFunctionColor4D()
    throws Exception
  {
    final var builder = SyThemeValues.builder();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionColor4D("x", "y", "z", x -> ONE);
      });

    assertEquals(VALUE_DOES_NOT_EXIST, ex.errorCode());
  }

  @Test
  public void testNodeTypeFunctionColor4D()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantInteger("x", "y", 1);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionColor4D("a", "y", "x", x -> ONE);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testFunctionColor4D()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantColor4D("a", "D", HALF)
        .createFunctionColor4D("b", "D", "a", x -> PVectors4D.add(x, x))
        .create();

    assertEquals(HALF, attributes.color4D("a"));
    assertEquals(ONE, attributes.color4D("b"));

    attributes.setColorTransform("b", x -> PVectors4D.subtract(x, x));
    assertEquals(HALF, attributes.color4D("a"));
    assertEquals(ZERO, attributes.color4D("b"));

    attributes.reset();
    assertEquals(HALF, attributes.color4D("a"));
    assertEquals(ONE, attributes.color4D("b"));

    assertInstanceOf(SyConstantColor4DType.class, attributes.values().get("a"));
    assertInstanceOf(SyFunctionColor4DType.class, attributes.values().get("b"));
  }

  @Test
  public void testFunctionColor4DNotConstant()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantColor4D("a", "D", HALF)
        .createFunctionColor4D("b", "D", "a", x -> PVectors4D.add(x, x))
        .create();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        attributes.setColor4D("b", HALF);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testConstantColor4D()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantColor4D("a", "D", HALF)
        .create();

    assertEquals(HALF, attributes.color4D("a"));
    attributes.setColor4D("a", ONE);
    assertEquals(ONE, attributes.color4D("a"));
    attributes.reset();
    assertEquals(HALF, attributes.color4D("a"));

    assertInstanceOf(SyConstantColor4DType.class, attributes.values().get("a"));
  }

  @Test
  public void testNodeExistsConstantInteger()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantInteger("x", "y", 1);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createConstantInteger("x", "y", 1);
      });

    assertEquals(VALUE_ALREADY_EXISTS, ex.errorCode());
  }

  @Test
  public void testNodeNonexistentFunctionInteger()
    throws Exception
  {
    final var builder = SyThemeValues.builder();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionInteger("x", "y", "z", x -> 1);
      });

    assertEquals(VALUE_DOES_NOT_EXIST, ex.errorCode());
  }

  @Test
  public void testNodeTypeFunctionInteger()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantColor4D("x", "y", ONE);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionInteger("a", "y", "x", x -> 1);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testFunctionInteger()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantInteger("a", "D", 1)
        .createFunctionInteger("b", "D", "a", x -> x + x)
        .create();

    assertEquals(1, attributes.integer("a"));
    assertEquals(2, attributes.integer("b"));

    attributes.setIntegerTransform("b", x -> x - x);
    assertEquals(1, attributes.integer("a"));
    assertEquals(0, attributes.integer("b"));

    attributes.reset();
    assertEquals(1, attributes.integer("a"));
    assertEquals(2, attributes.integer("b"));

    assertInstanceOf(SyConstantIntegerType.class, attributes.values().get("a"));
    assertInstanceOf(SyFunctionIntegerType.class, attributes.values().get("b"));
  }

  @Test
  public void testFunctionIntegerNotConstant()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantInteger("a", "D", 1)
        .createFunctionInteger("b", "D", "a", x -> x + x)
        .create();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        attributes.setInteger("b", 1);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testConstantInteger()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantInteger("a", "D", 1)
        .create();

    assertEquals(1, attributes.integer("a"));
    attributes.setInteger("a", 2);
    assertEquals(2, attributes.integer("a"));
    attributes.reset();
    assertEquals(1, attributes.integer("a"));

    assertInstanceOf(SyConstantIntegerType.class, attributes.values().get("a"));
  }

  @Test
  public void testNodeExistsConstantDouble()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantDouble("x", "y", 1);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createConstantDouble("x", "y", 1);
      });

    assertEquals(VALUE_ALREADY_EXISTS, ex.errorCode());
  }

  @Test
  public void testNodeNonexistentFunctionDouble()
    throws Exception
  {
    final var builder = SyThemeValues.builder();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionDouble("x", "y", "z", x -> 1.0);
      });

    assertEquals(VALUE_DOES_NOT_EXIST, ex.errorCode());
  }

  @Test
  public void testNodeTypeFunctionDouble()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantColor4D("x", "y", ONE);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionDouble("a", "y", "x", x -> 1.0);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testFunctionDouble()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantDouble("a", "D", 1)
        .createFunctionDouble("b", "D", "a", x -> x + x)
        .create();

    assertEquals(1, attributes.double_("a"));
    assertEquals(2, attributes.double_("b"));

    attributes.setDoubleTransform("b", x -> x - x);
    assertEquals(1, attributes.double_("a"));
    assertEquals(0, attributes.double_("b"));

    attributes.reset();
    assertEquals(1, attributes.double_("a"));
    assertEquals(2, attributes.double_("b"));

    assertInstanceOf(SyConstantDoubleType.class, attributes.values().get("a"));
    assertInstanceOf(SyFunctionDoubleType.class, attributes.values().get("b"));
  }

  @Test
  public void testFunctionDoubleNotConstant()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantDouble("a", "D", 1)
        .createFunctionDouble("b", "D", "a", x -> x + x)
        .create();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        attributes.setDouble("b", 1);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testConstantDouble()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantDouble("a", "D", 1)
        .create();

    assertEquals(1, attributes.double_("a"));
    attributes.setDouble("a", 2);
    assertEquals(2, attributes.double_("a"));
    attributes.reset();
    assertEquals(1, attributes.double_("a"));

    assertInstanceOf(SyConstantDoubleType.class, attributes.values().get("a"));
  }

  private static final SyFontDescription FONT_0 =
    new SyFontDescription("Dialog", REGULAR, 10);
  private static final SyFontDescription FONT_1 =
    new SyFontDescription("Dialog", REGULAR, 12);
  private static final SyFontDescription FONT_2 =
    new SyFontDescription("Dialog", REGULAR, 14);

  @Test
  public void testNodeExistsConstantFont()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantFont("x", "y", FONT_0);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createConstantFont("x", "y", FONT_0);
      });

    assertEquals(VALUE_ALREADY_EXISTS, ex.errorCode());
  }

  @Test
  public void testNodeNonexistentFunctionFont()
    throws Exception
  {
    final var builder = SyThemeValues.builder();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionFont("x", "y", "z", x -> FONT_0);
      });

    assertEquals(VALUE_DOES_NOT_EXIST, ex.errorCode());
  }

  @Test
  public void testNodeTypeFunctionFont()
    throws Exception
  {
    final var builder = SyThemeValues.builder();
    builder.createConstantColor4D("x", "y", ONE);

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        builder.createFunctionFont("a", "y", "x", x -> FONT_0);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testFunctionFont()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantFont("a", "D", FONT_0)
        .createFunctionFont("b", "D", "a", x -> FONT_1)
        .create();

    assertEquals(FONT_0, attributes.font("a"));
    assertEquals(FONT_1, attributes.font("b"));

    attributes.setFontTransform("b", x -> FONT_2);
    assertEquals(FONT_0, attributes.font("a"));
    assertEquals(FONT_2, attributes.font("b"));

    attributes.reset();
    assertEquals(FONT_0, attributes.font("a"));
    assertEquals(FONT_1, attributes.font("b"));

    assertInstanceOf(SyConstantFontType.class, attributes.values().get("a"));
    assertInstanceOf(SyFunctionFontType.class, attributes.values().get("b"));
  }

  @Test
  public void testFunctionFontNotConstant()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantFont("a", "D", FONT_0)
        .createFunctionFont("b", "D", "a", x -> FONT_1)
        .create();

    final var ex =
      assertThrows(SyThemeValueException.class, () -> {
        attributes.setFont("b", FONT_2);
      });

    assertEquals(VALUE_TYPE_INCORRECT, ex.errorCode());
  }

  @Test
  public void testConstantFont()
    throws Exception
  {
    final var attributes =
      SyThemeValues.builder()
        .createConstantFont("a", "D", FONT_0)
        .create();

    assertEquals(FONT_0, attributes.font("a"));
    attributes.setFont("a", FONT_1);
    assertEquals(FONT_1, attributes.font("a"));
    attributes.reset();
    assertEquals(FONT_0, attributes.font("a"));

    assertInstanceOf(SyConstantFontType.class, attributes.values().get("a"));
  }
}
