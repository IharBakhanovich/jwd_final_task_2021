package com.epam.jwd.Conferences.exception;

public class UniqueConstraintViolationException extends RuntimeException {

    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
