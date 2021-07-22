package com.epam.jwd.Conferences.exception;

// runtimeException
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
