package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.ReportType;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements 'show_create_report' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowCreateReportPage implements Command {
//    private static final CommandResponse CREATE_REPORT_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");
//
//    private static final CommandResponse SHOW_CREATE_REPORT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String ERROR_ATTRIBUTE_NAME = "error";
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//
//    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
//    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
//    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
//    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
//    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
//    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
//
//    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
//    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
//    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
//    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
//    private static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";
//    private static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";

    // the AppService, that communicates with the repo
    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateReportPage() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class ShowCreateReportPageHolder {
        private final static ShowCreateReportPage instance
                = new ShowCreateReportPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateReportPage getInstance() {
        return ShowCreateReportPage.ShowCreateReportPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        List<ReportType> allowedReportTypes = new ArrayList<>();
        allowedReportTypes.add(ReportType.QUESTION);
        allowedReportTypes.add(ReportType.APPLICATION);
        final String conferenceTitle = request.getParameter(ApplicationConstants.CONFERENCE_TITLE_PARAMETER_NAME);
        final String conferenceId = request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME);
        final String creatorId = request.getParameter(ApplicationConstants.CREATOR_ID_PARAMETER_NAME);
        final String creatorRole = request.getParameter(ApplicationConstants.CREATOR_ROLE_PARAMETER_NAME);
        final String sectionId = request.getParameter(ApplicationConstants.SECTION_ID_PARAMETER_NAME);
        final String sectionName = request.getParameter(ApplicationConstants.SECTION_NAME_PARAMETER_NAME);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isUserWithIdExistInSystem(Long.valueOf(creatorId))
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole)
                || !validator.isUserIdAndUserRoleFromTheSameUser(String.valueOf(creatorId), creatorRole)
                || !validator.isSectionExistInSystem(Long.valueOf(sectionId))
                || !validator.isConferenceExistInSystem(Long.valueOf(conferenceId))
                || !validator.isConferenceTitleAndIdFromTheSameConference(Long.valueOf(conferenceId), conferenceTitle)
                || !validator.isConferenceWithSuchTitleExistInSystem(conferenceTitle)
                || !validator.isSectionNameAndIdFromTheSameSection(Long.valueOf(sectionId), sectionName)) {
            return prepareErrorPageBackToMainPage(request, ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }
        request.setAttribute(ApplicationConstants.ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME, allowedReportTypes);
        request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(ApplicationConstants.CREATOR_ID_ATTRIBUTE_NAME, creatorId);
        request.setAttribute(ApplicationConstants.CREATOR_ROLE_ATTRIBUTE_NAME, creatorRole);
        request.setAttribute(ApplicationConstants.SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        return ApplicationConstants.CREATE_REPORT_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_CREATE_REPORT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}