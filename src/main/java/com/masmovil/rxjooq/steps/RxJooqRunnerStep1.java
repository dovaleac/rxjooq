package com.masmovil.rxjooq.steps;

import com.masmovil.rxjooq.AbstractRxJooqRunner;
import org.jooq.DSLContext;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class RxJooqRunnerStep1 extends AbstractRxJooqRunner {

  public RxJooqRunnerStep1(Callable<DSLContext> dslContextSupplier) {
    super(dslContextSupplier);
  }

  public RxJooqRunnerStep2 withExecutor(Executor executor) {
    return new RxJooqRunnerStep2(dslContextSupplier, executor);
  }
}
