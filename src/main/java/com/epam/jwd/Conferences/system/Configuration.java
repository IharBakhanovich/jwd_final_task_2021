package com.epam.jwd.Conferences.system;

import com.epam.jwd.Conferences.exception.ConfigNotFoundException;
import com.epam.jwd.Conferences.pool.AppConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is going to read to configuration file and store it in a
 * key-value-map, thereby proving the information of said file to the entire
 * system, if needed. This class uses the Singleton-Pattern since there is only
 * one configuration necessary.
 */
public class Configuration {

    private static final Logger logger = LogManager.getLogger(Configuration.class);
    private static final String FILEPATH
            = "C:\\Studium\\EPAM\\FinalTask_WebProject\\src\\main\\resources\\application.properties";
    private Properties config = new Properties();

    Configuration() {
    }

    private static class ConfigurationHolder {
        private final static Configuration instance
                = new Configuration();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static Configuration getInstance() {
        return ConfigurationHolder.instance;
    }

    /**
     * A Getter for acquiring the key-value-map of the configuration.
     *
     * @return The key-value-map.
     */
    public Properties getConfig() {
        return config;
    }

    /**
     * Re-loads the Configuration from the disk.
     *
     * @param filepath the absolute file-path of the properties-file;
     */
    public boolean loadConfig(String filepath) {
        InputStream inputStream = null;
        try {
            config = new Properties();
            inputStream = new FileInputStream(filepath);
            config.load(inputStream);
            logger.info("Sucessfully read config.");
        } catch (FileNotFoundException e1) {
            logger.error("Config file could not be found.");
            throw new ConfigNotFoundException("");
        } catch (IOException e2) {
            logger.error("Config file could not be loaded.");
            throw new ConfigNotFoundException("");
        }
        return true;
    }

    /**
     * Getter for the FILEPATH
     *
     * @return the FILEPATH for the config
     */
    public static String getFilepath() {
        return FILEPATH;
    }
}
