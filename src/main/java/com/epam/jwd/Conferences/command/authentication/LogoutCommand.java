package com.epam.jwd.Conferences.command.authentication;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.service.UserService;

public class LogoutCommand implements Command {
    private static final CommandResponse MAIN_PAGE_REDIRECT
            = CommandResponse.getCommandResponse(true, "index.jsp");

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private LogoutCommand() {
    }

    private static class LogoutCommandHolder {
        private final static LogoutCommand instance
                = new LogoutCommand();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static LogoutCommand getInstance() {
        return LogoutCommand.LogoutCommandHolder.instance;
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        request.invalidateCurrentSession();
        return MAIN_PAGE_REDIRECT;
    }
}
