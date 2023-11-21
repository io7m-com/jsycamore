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


package com.io7m.jsycamore.api.events;

import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

/**
 * A simple implementation of the {@link Flow.Subscriber} interface that exposes
 * a lambda-based API.
 *
 * @param <T> The type of events
 */

public final class SyEventConsumer<T> implements Flow.Subscriber<T>
{
  private final Consumer<T> consumer;
  private final Runnable onComplete;
  private Flow.Subscription subscription;

  private SyEventConsumer(
    final Consumer<T> inConsumer,
    final Runnable inOnComplete)
  {
    this.consumer =
      Objects.requireNonNull(inConsumer, "consumer");
    this.onComplete =
      Objects.requireNonNull(inOnComplete, "inOnComplete");
  }

  /**
   * Subscribe to the given publisher, delivering events to {@code consumer}
   * (automatically requesting new events each time one is published), and
   * calling {@code onComplete} when the publisher is closed.
   *
   * @param publisher  The publisher
   * @param consumer   An event consumer
   * @param onComplete A function called on closing the publisher
   * @param <T>        The type of events
   *
   * @return A consumer
   */

  public static <T> SyEventConsumer<T> subscribe(
    final Flow.Publisher<T> publisher,
    final Consumer<T> consumer,
    final Runnable onComplete)
  {
    Objects.requireNonNull(publisher, "publisher");
    Objects.requireNonNull(consumer, "consumer");

    final var r = new SyEventConsumer<T>(consumer, onComplete);
    publisher.subscribe(r);
    return r;
  }

  /**
   * Subscribe to the given publisher, delivering events to {@code consumer}
   * (automatically requesting new events each time one is published), and
   * calling {@code onComplete} when the publisher is closed.
   *
   * @param publisher The publisher
   * @param consumer  An event consumer
   * @param <T>       The type of events
   *
   * @return A consumer
   */

  public static <T> SyEventConsumer<T> subscribe(
    final Flow.Publisher<T> publisher,
    final Consumer<T> consumer)
  {
    return subscribe(publisher, consumer, () -> {
    });
  }

  @Override
  public void onSubscribe(
    final Flow.Subscription newSubscription)
  {
    this.subscription =
      Objects.requireNonNull(newSubscription, "newSubscription");
    this.subscription.request(1L);
  }

  @Override
  public void onNext(
    final T item)
  {
    try {
      this.consumer.accept(item);
    } finally {
      this.subscription.request(1L);
    }
  }

  @Override
  public void onError(
    final Throwable throwable)
  {

  }

  @Override
  public void onComplete()
  {
    this.onComplete.run();
  }
}
