package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowConferenceSectionsPage implements Command {

    public static final String ID_PARAMETER_NAME = "id";
    public static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final CommandResponse SHOW_SECTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "WEB-INF/jsp/sections.jsp");
    public static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    public static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    public static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceID";
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
        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final List<Section> sections = service.findAllSectionsByConferenceID(id);
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, id);
        return SHOW_SECTIONS_PAGE_RESPONSE;
    }
}
