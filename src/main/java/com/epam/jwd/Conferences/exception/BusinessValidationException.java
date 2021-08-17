package com.epam.jwd.Conferences.exception;

/**
 * Throws by the validation.
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