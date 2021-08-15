package com.epam.jwd.Conferences.listener;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.pool.AppConnectionPool;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class is a web listener that starts the system whenever the servlet
 * context gets initialized and stops the system whenever the servlet context
 * gets destroyed.
 */
// этот класс стал subscriber, а где-то в рамках tomcat зашиты publisher, который создает ServletContext и код,
// который засабскрайбит этот Listener на Publisher, причем за счет аннотации @Weblistener
// и интерфейса ServletContextListener все получается слабо связано, т.е. поставили аннотацию,
// заимплементили интерфейс и к нам сюда уже приходят ивенты. Т.е. нигде не пишем код, отвечающий за подписывание,
// этим всем занимается сам TomCat.
@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    private final UserService userService;

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_APPLICATION_LIFECYCLE_LISTENER; //LogManager.getLogger(ApplicationLifecycleListener.class);
    private static final String CONFIGPATH = "/config/logger.properties";

    public ApplicationLifecycleListener() {
        userService = UserService.retrieve();
    }

    /**
     * Starts the system whenever the servlet context get initialized.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // приходит event, причем такой же ivent придет если context будет уничтожен
        // в ivent есть getServletContext, который возвращает соответствующий ServletContext,
        // который был инициализирован
        // перенесем из контроллера заполнение базы данных, а в проекте будет происходить поднятие ConnectionPool
        //TODO поднять ConnectionPool
        try {
            logger.info("AppConnectionPool is initialised...");
            ConnectionPool.retrieve().init();
            logger.info("AppConnectionPool was successfully initialised.");
        } catch (CouldNotInitializeConnectionPoolException e) {
            logger.error("AppConnectionPool was not initialised");
            logger.error(e.getStackTrace());
        }
    }

    /**
     * Stops the system whenever the servlet context gets destroyed.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down Connection Pool...");
        AppConnectionPool.getInstance().destroy();
    }
}
