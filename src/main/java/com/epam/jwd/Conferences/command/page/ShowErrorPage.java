package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;

public class ShowErrorPage implements Command {

    private static final CommandResponse ERROR_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/error.jsp");

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowErrorPage() {
    }

    private static class ShowErrorPageHolder {
        private final static ShowErrorPage instance
                = new ShowErrorPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowErrorPage getInstance() {
        return ShowErrorPage.ShowErrorPageHolder.instance;
    }


    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        return ApplicationConstants.ERROR_PAGE_RESPONSE;
    }
}