package com.io7m.jsycamore.components;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jsycamore.Component;
import com.io7m.jsycamore.GUIException;

public interface ButtonListener
{
  void buttonListenerOnClick(
    final @Nonnull Component button)
    throws GUIException,
      ConstraintError;
}
