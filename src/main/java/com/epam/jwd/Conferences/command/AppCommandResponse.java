package com.epam.jwd.Conferences.command;

/**
 * The implementation of the {@link CommandResponse}.
 */
public class AppCommandResponse implements CommandResponse {
    private final boolean isRedirect;
    private final String path;

    /**
     * Creates an {@link AppCommandResponse} object.
     *
     * @param path is the value of the path to go.
     * @param isRedirect shows whether the link inside {@code true} or outside the app{@code false}.
     */
    public AppCommandResponse(boolean isRedirect, String path) {
        this.path = path;
        this.isRedirect = isRedirect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRedirect() {
        return isRedirect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return path;
    }
}
