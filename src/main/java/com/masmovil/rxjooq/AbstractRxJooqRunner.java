package com.masmovil.rxjooq;

import io.reactivex.*;
import org.jooq.*;

import java.util.concurrent.*;
import java.util.function.Function;

public abstract class AbstractRxJooqRunner implements RxJooqRunner {

  protected final Callable<DSLContext> dslContextSupplier;
  protected Executor executor = null;

  public AbstractRxJooqRunner(Callable<DSLContext> dslContextSupplier) {
    this.dslContextSupplier = dslContextSupplier;
  }

  public AbstractRxJooqRunner(Callable<DSLContext> dslContextSupplier, Executor executor) {
    this.dslContextSupplier = dslContextSupplier;
    this.executor = executor;
  }

  @Override
  public <T extends Record> Flowable<T> querySeveralResults(
      Function<DSLContext, ResultQuery<T>> producer) {
    return Flowable.using(
        dslContextSupplier,
        dslContext ->
            Flowable.create(
                flowableEmitter -> {
                  try {
                    ResultQuery<T> preFetch = producer.apply(dslContext);
                    CompletionStage<Result<T>> fetched =
                        executor == null ? preFetch.fetchAsync() : preFetch.fetchAsync(executor);
                    fetched.thenAccept(
                        t -> {
                            t.forEach(flowableEmitter::onNext);
                            flowableEmitter.onComplete();
                        });
                  } catch (Exception e) {
                    flowableEmitter.onError(e);
                  }
                },
                BackpressureStrategy.BUFFER),
        DSLContext::close);
  }
  //  @Override
  //  public <T extends Record> Flowable<T> querySeveralResults(
  //      Function<DSLContext, ResultQuery<T>> producer) {
  //    return Flowable.using(
  //        dslContextSupplier,
  //        dslContext ->
  //            Flowable.create(
  //                flowableEmitter -> {
  //                  try {
  //                    producer.apply(dslContext).forEach(flowableEmitter::onNext);
  //                    flowableEmitter.onComplete();
  //                  } catch (Exception e) {
  //                    flowableEmitter.onError(e);
  //                  }
  //                },
  //                BackpressureStrategy.BUFFER),
  //        DSLContext::close);
  //  }

  @Override
  public <RT extends Record> Maybe<RT> queryFirstResultIfAny(
      Function<DSLContext, ResultQuery<RT>> asyncProducer) {
    return Maybe.using(
        dslContextSupplier,
        dslContext ->
            Maybe.create(
                (MaybeEmitter<RT> maybeEmitter) -> {
                  try {
                    ResultQuery<RT> preFetch = asyncProducer.apply(dslContext);
                    CompletionStage<Result<RT>> fetched =
                        executor == null ? preFetch.fetchAsync() : preFetch.fetchAsync(executor);
                    fetched.thenAccept(
                        t -> {
                          if (t.isEmpty()) {
                            maybeEmitter.onComplete();
                          } else {
                            maybeEmitter.onSuccess(t.get(0));
                          }
                        });
                  } catch (Exception e) {
                    maybeEmitter.onError(e);
                  }
                }),
        DSLContext::close);
  }

  @Override
  public Completable execute(Function<DSLContext, Query> asyncQuery) {
    return Completable.using(
        dslContextSupplier,
        dslContext ->
            Completable.create(
                (CompletableEmitter completableEmitter) -> {
                  try {
                    Query preFetch = asyncQuery.apply(dslContext);
                    CompletionStage<Integer> fetched =
                        executor == null
                            ? preFetch.executeAsync()
                            : preFetch.executeAsync(executor);
                    fetched.thenAccept(i -> completableEmitter.onComplete());
                  } catch (Exception e) {
                    completableEmitter.onError(e);
                  }
                }),
        DSLContext::close);
  }
}
