package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowConferenceSectionsPage implements Command {

    private static final String ID_PARAMETER_NAME = "id";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final CommandResponse SHOW_SECTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManager";
    // the AppService, that communicates with the repo
    private final UserService service;

    private static class ShowConferenceSectionsPageHolder {
        private final static ShowConferenceSectionsPage instance
                = new ShowConferenceSectionsPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowConferenceSectionsPage getInstance() {
        return ShowConferenceSectionsPage.ShowConferenceSectionsPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowConferenceSectionsPage() {
        this.service = UserService.retrieve();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final List<Conference> conferences = service.findAllConferences();
        Long conferenceManagerId = null;
        for (Conference conference: conferences
             ) {
            if(conference.getId() == id) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);
        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final List<Section> sections = service.findAllSectionsByConferenceID(id);
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, id);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return SHOW_SECTIONS_PAGE_RESPONSE;
    }
}
