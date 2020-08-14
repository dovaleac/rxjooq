package com.masmovil.rxjooq.steps;

import com.masmovil.rxjooq.AbstractRxJooqRunner;
import org.jooq.DSLContext;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class RxJooqRunnerStep2 extends AbstractRxJooqRunner {

  public RxJooqRunnerStep2(Callable<DSLContext> dslContextSupplier, Executor executor) {
    super(dslContextSupplier, executor);
  }
}
