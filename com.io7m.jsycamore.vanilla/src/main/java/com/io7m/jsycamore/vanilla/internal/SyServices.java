/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jsycamore.api.services.SyServiceDirectoryType;
import com.io7m.repetoir.core.RPServiceDirectory;
import com.io7m.repetoir.core.RPServiceEventType;
import com.io7m.repetoir.core.RPServiceException;
import com.io7m.repetoir.core.RPServiceType;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

/**
 * The main screen service directory.
 */

public final class SyServices implements SyServiceDirectoryType
{
  private final RPServiceDirectory services;

  @Override
  public <T extends RPServiceType> void register(
    final Class<T> clazz,
    final T service)
  {
    this.services.register(clazz, service);
  }

  @Override
  public <T extends RPServiceType> void deregister(
    final Class<T> clazz,
    final T service)
  {
    this.services.deregister(clazz, service);
  }

  @Override
  public <T extends RPServiceType> void deregisterAll(
    final Class<T> clazz)
  {
    this.services.deregisterAll(clazz);
  }

  @Override
  public <T extends RPServiceType> Optional<T> optionalService(
    final Class<T> clazz)
  {
    return this.services.optionalService(clazz);
  }

  @Override
  public <T extends RPServiceType> T requireService(
    final Class<T> clazz)
    throws RPServiceException
  {
    return this.services.requireService(clazz);
  }

  @Override
  public <T extends RPServiceType> List<? extends T> optionalServices(
    final Class<T> clazz)
    throws RPServiceException
  {
    return this.services.optionalServices(clazz);
  }

  @Override
  public List<RPServiceType> services()
  {
    return this.services.services();
  }

  @Override
  public Flow.Publisher<RPServiceEventType> events()
  {
    return this.services.events();
  }

  @Override
  public void close()
    throws IOException
  {
    this.services.close();
  }

  /**
   * The main screen service directory.
   */

  public SyServices()
  {
    this.services = new RPServiceDirectory();
  }
}
