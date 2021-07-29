package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowCreateConferencePage implements Command {

    private static final CommandResponse CREATE_CONFERENCE_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
    public static final String USERS_ATTRIBUTE_NAME = "users";

    // the AppService, that communicates with the repo
    private final UserService userService;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateConferencePage() {
        this.userService = UserService.retrieve();
    }

    private static class ShowCreateConferencePageHolder {
        private final static ShowCreateConferencePage instance
                = new ShowCreateConferencePage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateConferencePage getInstance() {
        return ShowCreateConferencePage.ShowCreateConferencePageHolder.instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<User> users = userService.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return CREATE_CONFERENCE_PAGE_RESPONSE;
    }
}
