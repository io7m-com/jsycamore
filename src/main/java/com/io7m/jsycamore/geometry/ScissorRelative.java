package com.io7m.jsycamore.geometry;

/**
 * Type used to represent a scissor-relative coordinate system. This type
 * essentially has no value-level representation and is only used for the
 * phantom type parameter of {@link Point}.
 * 
 * @see Point
 */

public final class ScissorRelative
{
  private ScissorRelative()
  {
    throw new AssertionError("unreachable code: report this bug!");
  }
}
