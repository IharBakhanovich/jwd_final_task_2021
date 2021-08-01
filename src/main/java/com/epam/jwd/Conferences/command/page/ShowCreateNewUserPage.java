package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;

public class ShowCreateNewUserPage implements Command {

    private static final CommandResponse SHOW_CREATE_NEW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateNewUserPage() {
    }

    private static class ShowCreateNewUserPageHolder {
        private final static ShowCreateNewUserPage instance
                = new ShowCreateNewUserPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateNewUserPage getInstance() {
        return ShowCreateNewUserPage.ShowCreateNewUserPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        return SHOW_CREATE_NEW_USER_PAGE_RESPONSE;
    }
}
