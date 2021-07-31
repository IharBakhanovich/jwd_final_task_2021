package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowUpdateConferencePage implements Command {
    private static final CommandResponse UPDATE_CONFERENCE_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String MANAGER_CONFERENCE_ATTRIBUTE_NAME = "managerConf";

    // the AppService, that communicates with the repo
    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowUpdateConferencePage() {
        this.service = UserService.retrieve();
    }

    private static class ShowUpdateConferencePageHolder {
        private final static ShowUpdateConferencePage instance
                = new ShowUpdateConferencePage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowUpdateConferencePage getInstance() {
        return ShowUpdateConferencePage.ShowUpdateConferencePageHolder.instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        final List<Conference> conferences = service.findAllConferences();
        for (Conference conference: conferences
             ) {
            if (conference.getId().equals(conferenceId)) {
                request.setAttribute(MANAGER_CONFERENCE_ATTRIBUTE_NAME, conference.getManagerConf());
            }
        }
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        return UPDATE_CONFERENCE_PAGE_RESPONSE;
    }
}
