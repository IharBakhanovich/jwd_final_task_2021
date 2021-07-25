package com.epam.jwd.Conferences.exception;
/**
 * Exception: Thrown if a database-insertion violates a UNIQUE-constraint.
 */
public class DuplicateException extends Exception {

    private static final long serialVersionUID = -2705066427684237329L;

    /**
     * Constructs a new DuplicateException.
     *
     * @param errorMessage The error-message of the exception.
     */
    public DuplicateException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Constructs a new DuplicateException.
     */
    public DuplicateException() {
        super();
    }
}
