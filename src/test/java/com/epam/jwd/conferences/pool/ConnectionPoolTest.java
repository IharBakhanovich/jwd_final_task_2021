package com.epam.jwd.conferences.pool;

import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

/**
 * Tests for ConnectionPool
 */
public class ConnectionPoolTest {

    @BeforeEach
    public void init() {
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_takeConnection() throws InterruptedException {
        Assertions.assertNotNull(ConnectionPool.retrieve().takeConnection());
    }

    // this test takes a lot of time because there are always connections
//    @Test
//    public void test_InterruptedException() {
//        Assertions.assertThrows(InterruptedException.class, () -> {
//            while (true) {
//                ConnectionPool.retrieve().takeConnection();
//            }
//        });
//    }

    @Test
    public void test_releaseConnection() {
        Connection connection = null;
        try {
            connection = ConnectionPool.retrieve().takeConnection();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        final Connection finalConnection = connection;
        Assertions.assertDoesNotThrow(() ->
                ConnectionPool.retrieve().releaseConnection(finalConnection));
    }

    @AfterEach
    public void shutdown() {
        ConnectionPool.retrieve().destroy();
    }
}