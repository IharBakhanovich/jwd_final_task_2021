package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;

public class ShowUpdateSectionPage implements Command {
    private static final CommandResponse UPDATE_SECTION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String CONFERENCE_MANAGER_ID_PARAMETER_NAME = "conferenceManagerId";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
    private static final String SECTION_MANAGER_ID_ATTRIBUTE_NAME = "sectionManagerId";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final CommandResponse SHOW_UPDATE_SECTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
    private static final String ERROR_ATTRIBUTE_NAME = "error";

    // the AppService, that communicates with the repo
    private final UserService service;
    private final Validator validator;

    /**
     * Creates an ShowUpdateSectionPage object. It is the the private default constructor,
     * to not create the instance of the class with 'new' outside the class
     */
    private ShowUpdateSectionPage() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class ShowUpdateSectionPageHolder {
        private final static ShowUpdateSectionPage instance
                = new ShowUpdateSectionPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowUpdateSectionPage getInstance() {
        return ShowUpdateSectionPage.ShowUpdateSectionPageHolder.instance;
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
        final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
        final String conferenceManagerId = request.getParameter(CONFERENCE_MANAGER_ID_PARAMETER_NAME);
        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(Long.valueOf(conferenceId))
                || !validator.isConferenceWithSuchTitleExistInSystem(conferenceTitle)
                || !validator.isConferenceTitleAndIdFromTheSameConference(Long.valueOf(conferenceId), conferenceTitle)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isUserWithIdExistInSystem(Long.valueOf(conferenceId))) {
            return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        String sectionName = null;
        Long sectionManagerId = null;
        final List<Section> sections = service.findAllSections();
        for (Section section: sections
             ) {
            if (section.getId().equals(sectionId)) {
                sectionName = section.getSectionName();
                sectionManagerId = section.getManagerSect();
            }
        }
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(SECTION_MANAGER_ID_ATTRIBUTE_NAME, sectionManagerId);
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);
        return UPDATE_SECTION_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return SHOW_UPDATE_SECTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}