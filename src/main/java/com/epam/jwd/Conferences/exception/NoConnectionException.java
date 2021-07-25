package com.epam.jwd.Conferences.exception;

/**
 * Exception: Thrown if there is no connection to database.
 */
public class NoConnectionException extends RuntimeException {
    private static final long serialVersionUID = -6110980221261600030L;

    /**
     * Constructs a new NoConnectionException.
     *
     * @param errorMessage The error-message of the exception.
     */
    public NoConnectionException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Constructs a new NoConnectionException.
     */
    public NoConnectionException() {
        super();
    }
}
