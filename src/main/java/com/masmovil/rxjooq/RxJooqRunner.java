package com.masmovil.rxjooq;

import io.reactivex.*;
import org.jooq.*;

import java.util.function.Function;

public interface RxJooqRunner {

  <T extends Record> Maybe<T> queryFirstResultIfAny(Function<DSLContext, ResultQuery<T>> asyncProducer);

  default <T extends Record> Single<T> queryFirstResult(
      Function<DSLContext, ResultQuery<T>> asyncProducer) {
    return queryFirstResultIfAny(asyncProducer).toSingle();
  }

  <T extends Record> Flowable<T> querySeveralResults(Function<DSLContext, ResultQuery<T>> producer);

  Completable execute(Function<DSLContext, Query> asyncQuery);
}
