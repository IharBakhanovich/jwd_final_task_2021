package com.epam.jwd.Conferences.exception;

/**
 * Exception: Thrown by the resolveReportTypeById(Long id) method
 * of the {@link com.epam.jwd.Conferences.dto.ReportType} if such id does not exist.
 */
public class UnknownEntityException extends RuntimeException {

    private final String entityName;
    private final Object[] args;

    /**
     * Constructs a new UnknownEntityException.
     *
     * @param entityName the name of the entity.
     */
    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    /**
     * Constructs a new UnknownEntityException.
     *
     * @param entityName the name of the entity.
     * @param args       are arguments.
     */
    public UnknownEntityException(String entityName, Object[] args) {
        super();
        this.entityName = entityName;
        this.args = args;
    }

    /**
     * The getter of the error-message.
     *
     * @return {@link String} is an error-message of the Exception.
     */
    @Override
    public String getMessage() {
        // you should use entityName, args (if necessary)
        return String.format("There is no such a entity with the name %s", entityName);
    }
}
