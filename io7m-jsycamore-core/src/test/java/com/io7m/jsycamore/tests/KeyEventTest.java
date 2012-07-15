package com.io7m.jsycamore.tests;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.KeyEvent;
import com.io7m.jsycamore.KeyEvent.Modifier;

public class KeyEventTest
{
  @Test public void testKeyIdentity()
    throws ConstraintError
  {
    final EnumSet<Modifier> empty0 = EnumSet.noneOf(Modifier.class);
    final EnumSet<Modifier> empty1 = EnumSet.noneOf(Modifier.class);

    for (final KeyEvent.Key kt : KeyEvent.Key.values()) {
      final KeyEvent k = new KeyEvent(kt, empty0, 0);
      Assert.assertEquals(kt, k.getKey());
      Assert.assertEquals(0, k.getCharacter());
      Assert.assertEquals(empty1, k.getModifiers());
    }
  }

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testKeyNull()
      throws ConstraintError
  {
    final EnumSet<Modifier> empty0 = EnumSet.noneOf(Modifier.class);
    new KeyEvent(null, empty0, 0);
  }

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testModNull()
      throws ConstraintError
  {
    new KeyEvent(KeyEvent.Key.KEY_0, null, 0);
  }
}
