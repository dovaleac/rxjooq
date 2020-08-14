package com.masmovil.rxjooq.dslUtils;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.concurrent.Callable;

public class DslContextAdapterImpl implements DslContextAdapter {

  private static volatile DslContextAdapterImpl mInstance;

  private DslContextAdapterImpl() {
  }

  public static DslContextAdapterImpl getInstance() {
    if (mInstance == null) {
      synchronized (DslContextAdapterImpl.class) {
        if (mInstance == null) {
          mInstance = new DslContextAdapterImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public Callable<DSLContext> fromConfiguration(Configuration configuration) {
    return () -> DSL.using(configuration);
  }

  @Override
  public Callable<DSLContext> fromConnection(Connection connection) {
    return fromConfiguration(new DefaultConfiguration().derive(connection));
  }

  @Override
  public Callable<DSLContext> fromConnection(Connection connection, SQLDialect dialect) {
    return fromConfiguration(new DefaultConfiguration().derive(connection).derive(dialect));
  }
}
