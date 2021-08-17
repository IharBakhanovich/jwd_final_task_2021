package com.epam.jwd.Conferences.pool;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
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

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_APP_CONNECTION_POOL; //LogManager.getLogger(AppConnectionPool.class);
//    public static final String DB_MYSQL_PATH = "jdbc:mysql://";
//    public static final String SUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE = "ConnectionPool was successfully initialized. Amount of available connections: ";
//    public static final String DB_MAX_CONNECTIONS_PROPERTY = "DB_MAX_CONNECTIONS";
//    public static final String DB_SERVER_PROPERTY = "DB_SERVER";
//    public static final String DB_PORT_PROPERTY = "DB_PORT";
//    public static final String DB_NAME_PROPERTY = "DB_NAME";
//    public static final String DB_USER_PROPERTY = "DB_USER";
//    public static final String DB_PASSWORD_PROPERTY = "DB_PASSWORD";
//    public static final String FAILED_TO_OPEN_CONNECTION_MESSAGE = "failed to open connection";
//    public static final String SQL_DRIVERS_REGISTRATION_START_MESSAGE = "sql drivers registration start...";
//    public static final String REGISTRATION_SUCCESSFUL_MESSAGE = "registration successful";
//    public static final String REGISTRATION_UNSUCCESSFUL_MESSAGE = "registration unsuccessful";
//    public static final String DRIVER_REGISTRATION_FAILED_MESSAGE = "driver registration failed";
//    public static final String DURING_CROWING_CONNECTION_POOL_MESSAGE = " during crowing connectionPool";
//    public static final String OPENING_DATABASE_CONNECTION_MESSAGE = "Opening Database Connection...";
//    public static final String SQL_DRIVERS_UNREGISTERING_START_MESSAGE = "sql drivers unregistering start...";
//    public static final String UNREGISTERING_DRIVERS_FAILED_MESSAGE = "unregistering drivers failed";
//    private final String UNSUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE = "DB read unsuccessfully. ConnectionPool was NOT initialized";
//
//    private static final int INIT_CONNECTIONS_AMOUNT = 8;
//    private static final int CONNECTIONS_GROW_FACTOR = 4;

    private final int MAX_CONNECTIONS_AMOUNT;
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

    /**
     * Constructs a new AppConnectionPool.
     */
    AppConnectionPool() {
        Configuration configuration = Configuration.getInstance();
        configuration.loadConfig(Configuration.getFilepath());
        Properties properties = configuration.getConfig();

        MAX_CONNECTIONS_AMOUNT = Integer.parseInt(properties.getProperty(ApplicationConstants.DB_MAX_CONNECTIONS_PROPERTY));
        DB_SERVER = properties.getProperty(ApplicationConstants.DB_SERVER_PROPERTY);
        DB_PORT = properties.getProperty(ApplicationConstants.DB_PORT_PROPERTY);
        DB_NAME = properties.getProperty(ApplicationConstants.DB_NAME_PROPERTY);
        DB_USER = properties.getProperty(ApplicationConstants.DB_USER_PROPERTY);
        DB_PASSWORD = properties.getProperty(ApplicationConstants.DB_PASSWORD_PROPERTY);
        DB_PATH = ApplicationConstants.DB_MYSQL_PATH + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME;

        initialized = new AtomicBoolean(false);
        availableConnections = new ArrayBlockingQueue<>(MAX_CONNECTIONS_AMOUNT);
        takenConnections = new ArrayList<>();
        this.lock = new ReentrantLock();
        connectionsOpened = new AtomicInteger(0);
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

    /**
     * {@inheritDoc}
     */
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
                    for (int i = 0; i < ApplicationConstants.INIT_CONNECTIONS_AMOUNT; i++) {
                        final Connection connection = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASSWORD);
                        final ProxyConnection proxyConnection = new ProxyConnection(connection);
                        availableConnections.add(proxyConnection);
                        logger.info(ApplicationConstants.SUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE + availableConnections.size());
                    }
                } catch (SQLException e) {
                    logger.error(ApplicationConstants.UNSUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE);
                    initialized.set(false);
                    throw new CouldNotInitializeConnectionPoolException(ApplicationConstants.FAILED_TO_OPEN_CONNECTION_MESSAGE, e);
                }
                connectionsOpened.set(ApplicationConstants.INIT_CONNECTIONS_AMOUNT);
            }
        } finally {
            lock.unlock();
        }
    }

    private void registerDrivers() throws CouldNotInitializeConnectionPoolException {
        logger.info(ApplicationConstants.SQL_DRIVERS_REGISTRATION_START_MESSAGE);
        try {
            //jdbc:mysql://localhost:3306/conferences
            DriverManager.registerDriver(DriverManager.getDriver(DB_PATH));
            logger.info(ApplicationConstants.REGISTRATION_SUCCESSFUL_MESSAGE);
        } catch (SQLException e) {
            logger.info(ApplicationConstants.REGISTRATION_UNSUCCESSFUL_MESSAGE);
            initialized.set(false); // in this case next thread sees false and initialized pool again
            throw new CouldNotInitializeConnectionPoolException(ApplicationConstants.DRIVER_REGISTRATION_FAILED_MESSAGE, e);
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
                        addConnections(currentOpenedConnections, ApplicationConstants.CONNECTIONS_GROW_FACTOR);
                    } catch (SQLException e) {
                        logger.error(ApplicationConstants.UNSUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE
                                + ApplicationConstants.DURING_CROWING_CONNECTION_POOL_MESSAGE);
                    }
                } else {
                    int amountConnectionsToCreate = MAX_CONNECTIONS_AMOUNT - currentOpenedConnections;
                    try {
                        addConnections(currentOpenedConnections, amountConnectionsToCreate);
                    } catch (SQLException e) {
                        logger.error(ApplicationConstants.UNSUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE
                                + ApplicationConstants.DURING_CROWING_CONNECTION_POOL_MESSAGE);
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
            logger.info(ApplicationConstants.OPENING_DATABASE_CONNECTION_MESSAGE);
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
        logger.info(ApplicationConstants.SQL_DRIVERS_UNREGISTERING_START_MESSAGE);
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                logger.error(ApplicationConstants.UNREGISTERING_DRIVERS_FAILED_MESSAGE);
            }
        }
    }
}