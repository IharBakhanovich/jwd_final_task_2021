package com.epam.jwd.Conferences.exception;

/**
 * Exception: Thrown if a SQLException occurs that is not a Duplicate, NoData or
 * Connection Exception
 */
public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 6434331489462120773L;

    /**
     * Constructs a new DatabaseException
     *
     * @param msg The error-message of the exception.
     */
    public DatabaseException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new DatabaseException
     */
    public DatabaseException() {
        super();
    }
}
