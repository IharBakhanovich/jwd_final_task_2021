package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowCreateSectionPage implements Command {
    private static final CommandResponse CREATE_SECTION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    private static final CommandResponse SHOW_CREATE_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";


    // the AppService, that communicates with the repo
    private final UserService userService;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateSectionPage() {
        this.userService = UserService.retrieve();
    }

    private static class ShowCreateSectionPageHolder {
        private final static ShowCreateSectionPage instance
                = new ShowCreateSectionPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateSectionPage getInstance() {
        return ShowCreateSectionPage.ShowCreateSectionPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String conferenceId = request.getParameter(CONFERENCE_ID_PARAMETER_NAME);
        final List<User> users = userService.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        final List<Conference> conferences = userService.findAllConferences();
        Long conferenceManagerId = null;
        for (Conference conference: conferences
             ) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);
        return CREATE_SECTION_PAGE_RESPONSE;
    }
}
