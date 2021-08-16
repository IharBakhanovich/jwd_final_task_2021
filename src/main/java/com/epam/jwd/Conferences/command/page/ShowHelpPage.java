package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;

/**
 * Implements 'show_help' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowHelpPage implements Command {

//    private static final CommandResponse SHOW_HELP_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/help.jsp");

    private static class ShowHelpPageHolder {
        private final static ShowHelpPage instance
                = new ShowHelpPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowHelpPage getInstance() {
        return ShowHelpPage.ShowHelpPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowHelpPage() {
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        return ApplicationConstants.SHOW_HELP_PAGE_RESPONSE;
    }
}