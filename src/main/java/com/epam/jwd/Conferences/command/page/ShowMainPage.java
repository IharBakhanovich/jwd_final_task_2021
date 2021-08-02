package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

/**
 * This is a command for main page. The singleton.
 */
public class ShowMainPage implements Command {

    private static final CommandResponse SHOW_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String USERS_ATTRIBUTE_NAME = "users";

    // the AppService, that communicates with the repo
    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowMainPage() {
        this.service = UserService.retrieve();
    }

    private static class ShowMainPageHolder {
        private final static ShowMainPage instance
                = new ShowMainPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowMainPage getInstance() {
        return ShowMainPage.ShowMainPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return SHOW_MAIN_PAGE;
    }
}