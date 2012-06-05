package com.io7m.jsycamore.components;

import javax.annotation.Nonnull;

import com.io7m.jsycamore.Component;

public interface ButtonListener
{
  void buttonListenerOnClick(
    final @Nonnull Component button);
}
