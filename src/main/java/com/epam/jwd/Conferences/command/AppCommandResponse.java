package com.epam.jwd.Conferences.command;

public class AppCommandResponse implements CommandResponse {
    private final boolean isRedirect;
    private final String path;

    public AppCommandResponse(boolean isRedirect, String path) {
        this.path = path;
        this.isRedirect = isRedirect;
    }

    @Override
    public boolean isRedirect() {
        return isRedirect;
    }

    @Override
    public String getPath() {
        return path;
    }
}
