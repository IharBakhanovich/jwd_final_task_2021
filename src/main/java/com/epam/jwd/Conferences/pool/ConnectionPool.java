package com.epam.jwd.Conferences.pool;

import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;

import java.sql.Connection;

public interface ConnectionPool {

    Connection takeConnection() throws InterruptedException;

    void releaseConnection(Connection connection);

    void init() throws CouldNotInitializeConnectionPoolException;

    void destroy();

    static ConnectionPool retrieve() {
        return AppConnectionPool.getInstance();
    }
}
