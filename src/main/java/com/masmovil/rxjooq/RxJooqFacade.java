package com.masmovil.rxjooq;

import com.masmovil.rxjooq.steps.RxJooqRunnerStep1;
import org.jooq.*;

import java.sql.Connection;
import java.util.concurrent.Callable;

public interface RxJooqFacade {
  RxJooqRunnerStep1 withDslContextSupplier(Callable<DSLContext> dslContextSupplier);

  RxJooqRunnerStep1 withConfiguration(Configuration configuration);

  RxJooqRunnerStep1 withConnection(Connection connection);

  RxJooqRunnerStep1 withConnectionAndDialect(Connection connection, SQLDialect dialect);
}
