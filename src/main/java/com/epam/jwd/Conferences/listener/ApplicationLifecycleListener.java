package com.epam.jwd.Conferences.listener;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.pool.AppConnectionPool;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class is a web listener that starts the system whenever the servlet
 * context gets initialized and stops the system whenever the servlet context
 * gets destroyed. The subscriber. The publicher is in the TomCat (in this case),
 * which creates ServletContext and subscribes this listener to the publisher.
 * Events come to this listener thanks to the annotation @WebListener
 * and the interface ServletContextListener. There is no code to subscribe - all the work is done by TomCat.
 */
@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    private final UserService userService;

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_APPLICATION_LIFECYCLE_LISTENER;

    /**
     * Constructs a new ApplicationLifecycleListener.
     */
    public ApplicationLifecycleListener() {
        userService = UserService.retrieve();
    }

    /**
     * Starts the system whenever the servlet context get initialized.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // comes the event, which has getServletContext() method, that returns the corresponding
        // initialized ServletContext. The initializing of the ConnectionPool is in this method.
        try {
            logger.info(ApplicationConstants.APP_CONNECTION_POOL_IS_INITIALISED_MESSAGE);
            ConnectionPool.retrieve().init();
            logger.info(ApplicationConstants.APP_CONNECTION_POOL_WAS_SUCCESSFULLY_INITIALISED_MESSAGE);
        } catch (CouldNotInitializeConnectionPoolException e) {
            logger.error(ApplicationConstants.APP_CONNECTION_POOL_WAS_NOT_INITIALISED_MESSAGE);
            logger.error(e.getStackTrace());
        }
    }

    /**
     * Stops the system whenever the servlet context gets destroyed.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info(ApplicationConstants.SHUTTING_DOWN_CONNECTION_POOL_MESSAGE);
        AppConnectionPool.getInstance().destroy();
    }
}
