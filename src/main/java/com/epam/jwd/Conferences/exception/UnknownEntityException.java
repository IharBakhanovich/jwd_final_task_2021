package com.epam.jwd.Conferences.exception;

public class UnknownEntityException extends RuntimeException {

    private final String entityName;
    private final Object[] args;

    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    public UnknownEntityException(String entityName, Object[] args) {
        super();
        this.entityName = entityName;
        this.args = args;
    }

    @Override
    public String getMessage() {
        // todo
        // you should use entityName, args (if necessary)
        return String.format("There is no such a entity with the name %s", entityName);
    }
}
