package com.masmovil.rxjooq.dslUtils;

import org.jooq.*;

import java.sql.Connection;
import java.util.concurrent.Callable;

public interface DslContextAdapter {
    Callable<DSLContext> fromConfiguration(Configuration configuration);

    Callable<DSLContext> fromConnection(Connection connection);

    Callable<DSLContext> fromConnection(Connection connection, SQLDialect dialect);
}
