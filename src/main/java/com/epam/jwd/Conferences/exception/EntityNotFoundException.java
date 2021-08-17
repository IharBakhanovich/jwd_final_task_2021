package com.epam.jwd.Conferences.exception;

/**
 * Exception: Thrown if an entity was not found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException.
     *
     * @param message The error-message of the exception.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
