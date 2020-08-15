package com.masmovil.rxjooq;

import io.reactivex.*;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.reactivex.core.Vertx;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.jooq.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class VertxRxJooqRunnerImpl implements RxJooqRunner {

  protected final Callable<DSLContext> dslContextSupplier;
  protected final PgConnectOptions pgConnectOptions;
  protected Executor executor = null;
  protected final Vertx vertx;

  public VertxRxJooqRunnerImpl(Callable<DSLContext> dslContextSupplier, Executor executor, PgConnectOptions pgConnectOptions, Vertx vertx) {
    this.dslContextSupplier = dslContextSupplier;
    this.executor = executor;
    this.pgConnectOptions = pgConnectOptions;
    this.vertx = vertx;
  }

  @Override
  public <T extends Record> Maybe<T> queryFirstResultIfAny(
      Function<DSLContext, ResultQuery<T>> asyncProducer) {
    return Maybe.using(
        dslContextSupplier,
        dslContext -> {
          ResultQuery<T> preFetch = asyncProducer.apply(dslContext);
          String sql = preFetch.getSQL();
          PoolOptions poolOptions = new PoolOptions()
              .setMaxSize(5);

          return Maybe.using(
              () -> PgPool.pool(vertx.getDelegate(), pgConnectOptions, poolOptions),
              pgPool -> Maybe.create(maybeEmitter -> pgPool.query(sql).execute(rowSetAsyncResult -> {
                if (rowSetAsyncResult.failed()) {
                  maybeEmitter.onError(rowSetAsyncResult.cause());
                }
                RowSet<Row> result = rowSetAsyncResult.result();
                if (result.rowCount() == 0) {
                  maybeEmitter.onComplete();
                } else {
                  for (Row row : result) {
                    //row.getValues()
                  }
                }
              })),
              PgPool::close
          );
        },
        DSLContext::close);
  }

  @Override
  public <T extends Record> Flowable<T> querySeveralResults(
      Function<DSLContext, ResultQuery<T>> producer) {
    return null;
  }

  @Override
  public Completable execute(Function<DSLContext, Query> asyncQuery) {
    return null;
  }
}
