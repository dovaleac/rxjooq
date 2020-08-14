package com.masmovil.rxjooq;

import com.masmovil.rxjooq.dslUtils.DslContextAdapter;
import com.masmovil.rxjooq.steps.RxJooqRunnerStep1;
import org.jooq.*;

import java.sql.Connection;
import java.util.concurrent.Callable;
import javax.inject.Inject;

public class RxJooqFacadeImpl implements RxJooqFacade {

  private final DslContextAdapter dslContextAdapter;

  @Inject
  public RxJooqFacadeImpl(DslContextAdapter dslContextAdapter) {
    this.dslContextAdapter = dslContextAdapter;
  }

  @Override
  public RxJooqRunnerStep1 withDslContextSupplier(Callable<DSLContext> dslContextSupplier) {
    return new RxJooqRunnerStep1(dslContextSupplier);
  }

  @Override
  public RxJooqRunnerStep1 withConfiguration(Configuration configuration) {
    return new RxJooqRunnerStep1(dslContextAdapter.fromConfiguration(configuration));
  }

  @Override
  public RxJooqRunnerStep1 withConnection(Connection connection) {
    return new RxJooqRunnerStep1(dslContextAdapter.fromConnection(connection));
  }

  @Override
  public RxJooqRunnerStep1 withConnectionAndDialect(Connection connection, SQLDialect dialect) {
    return new RxJooqRunnerStep1(dslContextAdapter.fromConnection(connection, dialect));
  }


}
