package com.epam.jwd.Conferences.command;

/**
 * The interface to implement the command response.
 */
public interface CommandResponse {

    boolean isRedirect();

    String getPath();

    /**
     * Returns the implementation of the CommandResponse that uses in the application.
     *
     * @return Object that is the implementation of the CommandResponse.
     */
    static CommandResponse getCommandResponse(boolean isRedirect, String path) {
        return new AppCommandResponse(isRedirect, path);
    }
}
