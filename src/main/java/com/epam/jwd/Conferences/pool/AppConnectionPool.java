package com.epam.jwd.Conferences.pool;

import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DatabaseException;
import com.epam.jwd.Conferences.system.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Contains and maintains connections. The Singleton.
 */
public class AppConnectionPool implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(AppConnectionPool.class);
    public static final String DB_MYSQL_PATH = "jdbc:mysql://";
    private final String DB_READ_UNSUCCESSFULLY = "DB read unsuccessfully";

    private static final int INIT_CONNECTIONS_AMOUNT = 8;
    private final int MAX_CONNECTIONS_AMOUNT;
    private static final int CONNECTIONS_GROW_FACTOR = 4;

    private final String DB_SERVER;
    private final String DB_PORT;
    private final String DB_NAME;
    private final String DB_USER;
    private final String DB_PASSWORD;
    private final String DB_PATH;

    private final AtomicBoolean initialized;
    private final ArrayBlockingQueue<ProxyConnection> availableConnections;
    private final List<ProxyConnection> takenConnections;
    private AtomicInteger connectionsOpened;
    private final Lock lock;

    AppConnectionPool() {
        Configuration configuration = Configuration.getInstance();
        configuration.loadConfig(Configuration.getFilepath());
        Properties properties = configuration.getConfig();

        MAX_CONNECTIONS_AMOUNT = Integer.parseInt(properties.getProperty("DB_MAX_CONNECTIONS"));
        DB_SERVER = properties.getProperty("DB_SERVER");
        DB_PORT = properties.getProperty("DB_PORT");
        DB_NAME = properties.getProperty("DB_NAME");
        DB_USER = properties.getProperty("DB_USER");
        DB_PASSWORD = properties.getProperty("DB_PASSWORD");
        DB_PATH = DB_MYSQL_PATH + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME;

        initialized = new AtomicBoolean(false);
        availableConnections = new ArrayBlockingQueue<>(MAX_CONNECTIONS_AMOUNT);
        takenConnections = new ArrayList<>();
        this.lock = new ReentrantLock();
        connectionsOpened = new AtomicInteger(0);

        //TODO посмотреть как используется init() и где

//        try {
//            init();
//        } catch (CouldNotInitializeConnectionPoolException e) {
//            logger.error(e.getMessage());
//            throw new DatabaseException(e.getMessage());
//        }
    }

    private static class AppConnectionPoolHolder {
        private final static AppConnectionPool instance
                = new AppConnectionPool();
    }

    @Override
    public void init() throws CouldNotInitializeConnectionPoolException {
        // lock is put to prevent the entrance of another thread and to initialising pool again
        // чтобы второй поток не смог зайти после того как initialized.set(false) сработала,
        //  а эксепшн еще не брошен и в if зайдет другой поток и вычитает initialized как false,
        //  то начнет тоже инициализировать connectionPool
        lock.lock();
        try {
            if (initialized.compareAndSet(false, true)) {
                registerDrivers();
                try {
                    for (int i = 0; i < INIT_CONNECTIONS_AMOUNT; i++) {
                        final Connection connection = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASSWORD);
                        final ProxyConnection proxyConnection = new ProxyConnection(connection);
                        availableConnections.add(proxyConnection);
                        //initialize connection
                    }
                } catch (SQLException e) {
                    logger.error(DB_READ_UNSUCCESSFULLY);
                    initialized.set(false);
                    throw new CouldNotInitializeConnectionPoolException("failed to open connection", e);
                }
                connectionsOpened.set(INIT_CONNECTIONS_AMOUNT);
            }
        } finally {
            lock.unlock();
        }
    }

    private void registerDrivers() throws CouldNotInitializeConnectionPoolException {
        logger.info("sql drivers registration start...");
        try {
            //jdbc:mysql://localhost:3306/conferences
            DriverManager.registerDriver(DriverManager.getDriver(DB_PATH));
            logger.info("registration successful");
        } catch (SQLException e) {
            logger.info("registration unsuccessful");
            initialized.set(false); // in this case next thread sees false and initialized pool again
            throw new CouldNotInitializeConnectionPoolException("driver registration failed", e);
        }
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppConnectionPool getInstance() {
        return AppConnectionPoolHolder.instance;
    }

    /**
     * Returns a Connection.
     *
     * @return Object of this class.
     */
    @Override
    public Connection takeConnection() throws InterruptedException {

        Connection connection = null;
        try {
            connection = availableConnections.take();
        } catch (InterruptedException e) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + e.getMessage());
            Thread.currentThread().interrupt();
            throw new InterruptedException("The thread "
                    + Thread.currentThread().getName()
                    + " inside the takeConnection() method of "
                    + AppConnectionPool.class
                    + " was interrupted");
        }
        lock.lock();
        try {
            final int currentOpenedConnections = connectionsOpened.get();
            if (availableConnections.size() <= currentOpenedConnections * 0.25
                    && currentOpenedConnections < MAX_CONNECTIONS_AMOUNT) {
                if (MAX_CONNECTIONS_AMOUNT - currentOpenedConnections <= 4) {
                    try {
                        addConnections(currentOpenedConnections, CONNECTIONS_GROW_FACTOR);
                    } catch (SQLException e) {
                        logger.error(DB_READ_UNSUCCESSFULLY + " during crowing connectionPool");
                    }
                } else {
                    int amountConnectionsToCreate = MAX_CONNECTIONS_AMOUNT - currentOpenedConnections;
                    try {
                        addConnections(currentOpenedConnections, amountConnectionsToCreate);
                    } catch (SQLException e) {
                        logger.error(DB_READ_UNSUCCESSFULLY + " during crowing connectionPool");
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        takenConnections.add((ProxyConnection) connection);
        return connection;
    }

    private void addConnections(int currentOpenedConnections, int amountConnectionsToCreate) throws SQLException {
        int amountOpenedConnections = currentOpenedConnections;
        for (int i = 0; i < amountConnectionsToCreate; i++) {
            logger.info("Opening Database Connection...");
            try {
                final Connection addedConnection = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASSWORD);
                final ProxyConnection proxyConnection = new ProxyConnection(addedConnection);
                availableConnections.add(proxyConnection);
                amountOpenedConnections = amountOpenedConnections + 1;
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new DatabaseException(e.getSQLState());
            }
        }
        connectionsOpened.set(amountOpenedConnections);
    }

    /**
     * Releases a no longer needed connection.
     *
     * @param connection The connection to pass back.
     */
    @Override
    public void releaseConnection(Connection connection) {
        // TODO уменьшать connectionPool, если заметили, что свободных коннекшенов стало больше, чем надо.
        //  И это по-хорошему нужно делать в потоке демоне, который будет осматривать connectionPool
        //  и периодически проверять что там происходит (сигнализировать ему будет availableConnection),
        //  а в этом месте давать сигнал, чтобы он начал сокраать connectionPool
        if (connection != null) {
            if (connection instanceof ProxyConnection) {
                try {
                    // to return released connection to availableConnections
                    availableConnections.put((ProxyConnection) connection);
                    // to delete released connection from takenConnections
                    takenConnections.remove(connection);
                } catch (InterruptedException e) {
                    logger.error("The thread "
                            + Thread.currentThread().getName()
                            + " is interrupted. "
                            + e.getMessage());
                }
            }
        }
    }

    /**
     * Shuts the connection pool down.
     */
    @Override
    public void destroy() {
        // it can be destroyed only if it is initialized
        if (initialized.compareAndSet(true, false)) {
            // destroying of available connections
            for (ProxyConnection connection : availableConnections) {
                try {
                    logger.info("connection " + connection + " is trying to close ...");
                    connection.realClose();
                } catch (SQLException e) {
                    logger.error("Connection " + connection + " was not closed");
                }
            }
            // destroying of taken connections
            for (ProxyConnection connection : takenConnections) {
                try {
                    logger.info("connection " + connection + " is trying to close ...");
                    connection.realClose();
                } catch (SQLException e) {
                    logger.error("Connection " + connection + " was not closed");
                }
            }
            deregisterDrivers();
        }
    }

    private void deregisterDrivers() {
        logger.info("sql drivers unregistering start...");
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                logger.error("unregistering drivers failed");
            }
        }
    }
}