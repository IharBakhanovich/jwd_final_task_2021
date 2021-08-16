package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;

/**
 * Implements 'show_login' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowLoginPage implements Command {

//    private static final CommandResponse LOGIN_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowLoginPage() {
    }

    private static class ShowLoginPageHolder {
        private final static ShowLoginPage instance
                = new ShowLoginPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowLoginPage getInstance() {
        return ShowLoginPage.ShowLoginPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        return ApplicationConstants.LOGIN_PAGE_RESPONSE;
    }
}