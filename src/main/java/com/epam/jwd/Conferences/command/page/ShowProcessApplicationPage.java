package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowProcessApplicationPage implements Command {

    private static final CommandResponse PROCESS_APPLICATION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/processApplication.jsp");
    private static final String MANAGER_ID_PARAMETER_NAME = "managerId";
    private static final String MANAGER_ROLE_PARAMETER_NAME = "managerRole";
    private static final String APPLICATION_ID_PARAMETER_NAME = "applicationId";
    private static final String APPLICATION_TOKEN_PARAMETER_NAME = "applicationToken";

    private static final String APPLICATION_TEXT_PARAMETER_NAME = "applicationText";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";

    private static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String APPLICATION_ID_ATTRIBUTE_NAME = "applicationId";
    private static final String APPLICATION_TOKEN_ATTRIBUTE_NAME = "applicationToken";
    private static final String APPLICATION_TEXT_ATTRIBUTE_NAME = "applicationText";
    private static final String APPLICANT_ATTRIBUTE_NAME = "applicant";
    private static final String QUESTION_REPORT_ID_ATTRIBUTE_NAME = "questionId";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";

    private static final CommandResponse SHOW_PROCESS_APPLICATION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowProcessApplicationPage() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class ShowProcessApplicationPageHolder {
        private final static ShowProcessApplicationPage instance
                = new ShowProcessApplicationPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowProcessApplicationPage getInstance() {
        return ShowProcessApplicationPage.ShowProcessApplicationPageHolder.instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {

        final Long managerId = Long.valueOf(request.getParameter(MANAGER_ID_PARAMETER_NAME));
        final String managerRole = request.getParameter(MANAGER_ROLE_PARAMETER_NAME);
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
        final String applicationText = request.getParameter(APPLICATION_TEXT_PARAMETER_NAME);
        final String applicationToken = request.getParameter(APPLICATION_TOKEN_PARAMETER_NAME);
        final Long applicationId = Long.valueOf(request.getParameter(APPLICATION_ID_PARAMETER_NAME));

        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isUserWithIdExistInSystem(managerId)
                || !validator.isRoleWithSuchNameExistInSystem(managerRole)
                || !validator.isReportExistInSystem(applicationId)) {
            return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        Optional<Report> application = service.findReportByID(applicationId);

        final List<Conference> conferences = service.findAllConferences();
        String conferenceTitle = null;
        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                conferenceTitle = conference.getConferenceTitle();
            }
        }
        final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
        String sectionName = null;
        for (Section section : sections
        ) {
            if (section.getId().equals(sectionId)) {
                sectionName = section.getSectionName();
            }
        }

        final List<User> users = service.findAllUsers();
        String applicant = null;
        for (User user : users
        ) {
            if (user.getId().equals(application.get().getApplicant())) {
                applicant = user.getNickname();
            }
        }

        List<ReportType> allowedReportTypes = new ArrayList<>();


        if (applicationToken.equals("applicantApplication") && application.get().getApplicant().equals(managerId)) {
            allowedReportTypes.add(ReportType.APPLICATION);
            allowedReportTypes.add(ReportType.CANCELLED);
        } else if (applicationToken.equals("userApplication")
                && (managerRole.equals("ADMIN") || managerRole.equals("MANAGER"))
                && application.get().getApplicant().equals(managerId)) {
            allowedReportTypes.add(ReportType.APPLICATION);
            allowedReportTypes.add(ReportType.APPROVED);
            allowedReportTypes.add(ReportType.REJECTED);
            allowedReportTypes.add(ReportType.CANCELLED);
        } else if (applicationToken.equals("userApplication")
                && (managerRole.equals("ADMIN") || managerRole.equals("MANAGER"))) {
            allowedReportTypes.add(ReportType.APPLICATION);
            allowedReportTypes.add(ReportType.APPROVED);
            allowedReportTypes.add(ReportType.REJECTED);
        }
        request.setAttribute(ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME, allowedReportTypes);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(APPLICANT_ATTRIBUTE_NAME, applicant);
        request.setAttribute(APPLICATION_ID_ATTRIBUTE_NAME, applicationId);
        request.setAttribute(APPLICATION_TEXT_ATTRIBUTE_NAME, applicationText);
        request.setAttribute(QUESTION_REPORT_ID_ATTRIBUTE_NAME, application.get().getQuestionReportId());
        request.setAttribute(APPLICATION_TOKEN_ATTRIBUTE_NAME, applicationToken);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);

        return PROCESS_APPLICATION_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return SHOW_PROCESS_APPLICATION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
