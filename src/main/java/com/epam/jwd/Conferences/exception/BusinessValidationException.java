package com.epam.jwd.Conferences.exception;

// тут надо решить is that Exception runtime Exception or not
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }
}
