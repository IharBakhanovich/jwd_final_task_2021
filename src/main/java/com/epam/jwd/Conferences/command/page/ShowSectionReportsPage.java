package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;

/**
 * This is a command for section's reports page. The singleton.
 */
public class ShowSectionReportsPage implements Command {

//    private static final String ID_PARAMETER_NAME = "id";
//    private static final String REPORTS_ATTRIBUTE_NAME = "reports";
//    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
//    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
//    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
//    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
//    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
//    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
//    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
//    private static final CommandResponse SHOW_SECTION_REPORTS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private final UserService service;
    private final Validator validator;

    private static class ShowSectionReportsPagePageHolder {
        private final static ShowSectionReportsPage instance
                = new ShowSectionReportsPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowSectionReportsPage getInstance() {
        return ShowSectionReportsPage.ShowSectionReportsPagePageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowSectionReportsPage() {
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
        final Long sectionId = Long.valueOf(request.getParameter(ApplicationConstants.ID_PARAMETER_NAME));
        final Long conferenceId = Long.valueOf(request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME));
        final String sectionName = request.getParameter(ApplicationConstants.SECTION_NAME_PARAMETER_NAME);
        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isSectionWithSuchNameExistInSystem(sectionName)
                || !validator.isSectionNameAndIdFromTheSameSection(sectionId, sectionName)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }
        final List<Report> reports = service.findAllReportsBySectionID(sectionId, conferenceId);
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        String conferenceTitle = null;

        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                conferenceTitle = conference.getConferenceTitle();
            }
        }
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(ApplicationConstants.REPORTS_ATTRIBUTE_NAME, reports);
        request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(ApplicationConstants.SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        return ApplicationConstants.SHOW_REPORTS_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_SECTION_REPORTS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
