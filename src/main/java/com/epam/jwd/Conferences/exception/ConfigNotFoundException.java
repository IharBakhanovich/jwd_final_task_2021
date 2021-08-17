package com.epam.jwd.Conferences.exception;

/**
 * RuntimeException: Thrown if loading of the configuration-files
 * (config.properties and logger.properties) at startup fails or the
 * configurtion-data from the database could not be fetched.
 */
public class ConfigNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7994132635228579130L;

    /**
     * Constructs a new ConfigNotFoundException.
     *
     * @param errorMessage The error-message of the exception.
     */
    public ConfigNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Constructs a new ConfigNotFoundException.
     */
    public ConfigNotFoundException() {
        super();
    }

}
