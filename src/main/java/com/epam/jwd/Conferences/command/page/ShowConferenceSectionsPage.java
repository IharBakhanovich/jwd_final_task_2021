package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;

/**
 * Implements 'show_sections' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowConferenceSectionsPage implements Command {

//    private static final String ID_PARAMETER_NAME = "id";
//    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
//    private static final CommandResponse SHOW_SECTIONS_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
//    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
//    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
//    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_F_SCSP = "conferenceManager";
//    private static final CommandResponse SHOW_CONFERENCE_SECTIONS_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";

    // the AppService, that communicates with the repo
    private final UserService service;
    private final Validator validator;

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
        this.validator = Validator.retrieve();
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(ApplicationConstants.ID_PARAMETER_NAME));
        final String conferenceTitle = request.getParameter(ApplicationConstants.CONFERENCE_TITLE_PARAMETER_NAME);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceWithSuchTitleExistInSystem(conferenceTitle)
                || !validator.isConferenceExistInSystem(id)
                || !validator.isConferenceTitleAndIdFromTheSameConference(id, conferenceTitle)) {
            return prepareErrorPageBackToMainPage(request, ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        final List<Conference> conferences = service.findAllConferences();
        Long conferenceManagerId = null;
        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(id)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        request.setAttribute(ApplicationConstants.CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_F_SCSP, conferenceManagerId);
        final List<Section> sections = service.findAllSectionsByConferenceID(id);
        request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, id);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        return ApplicationConstants.SHOW_SECTIONS_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_CONFERENCE_SECTIONS_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}