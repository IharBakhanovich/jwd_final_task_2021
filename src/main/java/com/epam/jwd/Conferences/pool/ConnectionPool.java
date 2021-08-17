package com.epam.jwd.Conferences.pool;

import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;

import java.sql.Connection;

public interface ConnectionPool {

    /**
     * Returns a Connection.
     *
     * @return Object of this class.
     */
    Connection takeConnection() throws InterruptedException;

    /**
     * Releases a no longer needed connection.
     *
     * @param connection The connection to pass back.
     */
    void releaseConnection(Connection connection);

    /**
     * Initializes ConnectionPool.
     *
     * @throws CouldNotInitializeConnectionPoolException if an initializing is not successful.
     */
    void init() throws CouldNotInitializeConnectionPoolException;

    /**
     * Shuts the connection pool down.
     */
    void destroy();

    static ConnectionPool retrieve() {
        return AppConnectionPool.getInstance();
    }
}
