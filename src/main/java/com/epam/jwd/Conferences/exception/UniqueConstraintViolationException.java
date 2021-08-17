package com.epam.jwd.Conferences.exception;

/**
 * Exception: Thrown if a database-insertion violates a UNIQUE-constraint.
 */
public class UniqueConstraintViolationException extends RuntimeException {

    /**
     * Constructs a new UniqueConstraintViolationException.
     *
     * @param message The error-message of the exception.
     */
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
