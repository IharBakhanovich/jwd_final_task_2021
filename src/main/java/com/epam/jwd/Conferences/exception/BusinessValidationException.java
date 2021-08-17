package com.epam.jwd.Conferences.exception;

/**
 * Creates the Exception by the BusinessValidation.
 */
public class BusinessValidationException extends RuntimeException {

    /**
     * Constructs the BusinessValidationException.
     *
     * @param message of the Exception.
     */
    public BusinessValidationException(String message) {
        super(message);
    }
}
