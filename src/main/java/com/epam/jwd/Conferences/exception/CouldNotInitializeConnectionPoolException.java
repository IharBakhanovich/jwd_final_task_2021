package com.epam.jwd.Conferences.exception;

/**
 * Throws when a ConnectionPool could not be initialized.
 */
public class CouldNotInitializeConnectionPoolException extends Exception {

    /**
     * Constructs the CouldNotInitializeConnectionPoolException.
     *
     * @param message of the Exception.
     */
    public CouldNotInitializeConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
