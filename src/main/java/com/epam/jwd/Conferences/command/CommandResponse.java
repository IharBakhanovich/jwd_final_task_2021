package com.epam.jwd.Conferences.command;

import com.epam.jwd.Conferences.dao.DBReportDAO;
import com.epam.jwd.Conferences.dao.ReportDAO;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

    /**
     * Returns the implementation of the CommandResponse.
     *
     * @return Object that is the implementation of the CommandResponse.
     */
    static CommandResponse getCommandResponse(boolean isRedirect, String path) {
        return new AppCommandResponse(isRedirect, path);
    }
}
