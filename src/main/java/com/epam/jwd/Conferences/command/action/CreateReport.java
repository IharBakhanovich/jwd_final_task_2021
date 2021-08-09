package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.ReportType;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Implements 'create_new_report' action. The singleton.
 */
public class CreateReport implements Command {

    private static final Logger logger = LogManager.getLogger(CreateReport.class);

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    private static final String REPORT_TEXT_PARAMETER_NAME = "reportText";
    private static final String APPLICANT_NICKNAME_PARAMETER_NAME = "applicantNickname";
    private static final String REPORT_TYPE_PARAMETER_NAME = "reportType";
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME = "questionReportId";
    private static final String QUESTION_TEXT_PARAMETER_NAME = "questionText";
    private static final String QUESTION_REPORT_ID_ATTRIBUTE_NAME = "questionId";
    private static final String REPORTS_ATTRIBUTE_NAME = "reports";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
    private static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";
    private static final String QUESTION_TEXT_ATTRIBUTE_NAME = "questionText";
    private static final String INVALID_REPORT_TEXT_MSG = "ReportTextShouldNotBeEmptyMSG";
    private static final String INVALID_REPORT_TEXT_NOT_UTF8_MSG = "ReportTextShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";

    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final CommandResponse CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_REPORT_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");
    private static final CommandResponse REPORT_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    private static final CommandResponse CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_ANSWER_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
    private static final CommandResponse CREATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String DUPLICATE_SECTION_MESSAGE
            = "The section with such an section name title already exist in the system."
            + " Please choose an other section name.";

    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private CreateReport() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class CreateReportHolder {
        private final static CreateReport instance
                = new CreateReport();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static CreateReport getInstance() {
        return CreateReport.CreateReportHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long questionReportId = Long.valueOf(request.getParameter(QUESTION_REPORT_ID_PARAMETER_NAME));
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        final String conferenceTitle = String.valueOf(request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
        final String reportText = String.valueOf(request.getParameter(REPORT_TEXT_PARAMETER_NAME));
        final String applicantNickname = String.valueOf(request.getParameter(APPLICANT_NICKNAME_PARAMETER_NAME));
        final String reportType = String.valueOf(request.getParameter(REPORT_TYPE_PARAMETER_NAME));
        final List<User> users = service.findAllUsers();

        // validation of the parameters (whether they exist in the system)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isReportTypeExistInSystem(reportType)
                || !validator.isUserWithNicknameExistInSystem(applicantNickname)
                || !validator.isSectionWithSuchNameExistInSystem(sectionName)
                || !validator.isConferenceWithSuchTitleExistInSystem(conferenceTitle)
                || !validator.isConferenceTitleAndIdFromTheSameConference(conferenceId, conferenceTitle)
                || !validator.isSectionNameAndIdFromTheSameSection(sectionId, sectionName)) {
            return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        //validation of questionReportId
        if (questionReportId != 0) {
            if(!validator.isReportExistInSystem(questionReportId)) {
                return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
            }
        }

        if (reportText == null || reportText.trim().equals("")) {
            return prepareErrorPage(request, INVALID_REPORT_TEXT_MSG);
        } else if (!validator.isStringValid(reportText)) {
            return prepareErrorPage(request, INVALID_REPORT_TEXT_NOT_UTF8_MSG);
        }

        Long applicantId = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(applicantNickname)) {
                applicantId = user.getId();
            }
        }

        ReportType reportTypeToAdd = ReportType.valueOf(reportType);

        Report reportToCreate
                = new Report(1L, sectionId, conferenceId, reportText,
                reportTypeToAdd, applicantId, questionReportId);

        try {
            service.createReport(reportToCreate);
            final List<Report> reports = service.findAllReportsBySectionID(sectionId, conferenceId);
            request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
            request.setAttribute(REPORTS_ATTRIBUTE_NAME, reports);
            request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
            request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
            request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            request.setAttribute(USERS_ATTRIBUTE_NAME, users);
            return REPORT_CREATION_SUCCESS_RESPONSE;
        } catch (DuplicateException e) {
            logger.info(DUPLICATE_SECTION_MESSAGE);
            return prepareErrorPage(request, DUPLICATE_SECTION_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        final Long questionReportId = Long.valueOf(request.getParameter(QUESTION_REPORT_ID_PARAMETER_NAME));
        final String questionText = String.valueOf(request.getParameter(QUESTION_TEXT_PARAMETER_NAME));

        if (questionReportId == 0) {
            final Long creatorId = Long.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
            final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
            final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
            final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
            final String conferenceTitle = String.valueOf(request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME));
            final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
            request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
            request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            request.setAttribute(CREATOR_ID_ATTRIBUTE_NAME, creatorId);
            request.setAttribute(CREATOR_ROLE_ATTRIBUTE_NAME, creatorRole);
            request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
            request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
            request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
            return CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_REPORT_PAGE;
        } else {
            final Long creatorId = Long.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
            final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
            final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
            final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
            request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            request.setAttribute(CREATOR_ID_ATTRIBUTE_NAME, creatorId);
            request.setAttribute(CREATOR_ROLE_ATTRIBUTE_NAME, creatorRole);
            request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
            request.setAttribute(QUESTION_REPORT_ID_ATTRIBUTE_NAME, questionReportId);
            request.setAttribute(QUESTION_TEXT_ATTRIBUTE_NAME, questionText);
            request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
            return CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_ANSWER_PAGE;
        }
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return CREATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
