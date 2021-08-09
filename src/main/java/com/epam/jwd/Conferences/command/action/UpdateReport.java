package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;
import java.util.Optional;

/**
 * Implements 'update_report' action. The singleton.
 */
public class UpdateReport implements Command {

    private static final String ID_PARAMETER_NAME = "id";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String REPORT_TEXT_PARAMETER_NAME = "reportText";
    private static final String REPORT_TYPE_PARAMETER_NAME = "reportType";
    private static final String APPLICANT_PARAMETER_NAME = "applicant";
    private static final String UPDATER_ID_PARAMETER_NAME = "updaterId";
    private static final String UPDATER_ROLE_PARAMETER_NAME = "updaterRole";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String REPORT_ATTRIBUTE_NAME = "report";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String REPORTS_ATTRIBUTE_NAME = "reports";
    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";

    private static final CommandResponse UPDATE_REPORT_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME = "questionReportId";
    private static final String NO_PERMISSION_TO_UPDATE_REPORT_MSG = "YouHaveNoPermissionToUpdateThisReportMSG";
    private static final String INVALID_REPORT_TEXT_NOT_UTF8_MSG = "ReportTextShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_REPORT_TEXT_MSG = "ReportTextShouldNotBeEmptyMSG";
    private static final CommandResponse UPDATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";

    private final UserService service;
    private final Validator validator;

    private static class UpdateReportHolder {
        private final static UpdateReport instance
                = new UpdateReport();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static UpdateReport getInstance() {
        return UpdateReport.UpdateReportHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private UpdateReport() {
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
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final String sectionName = String.valueOf(request.getParameter(SECTION_NAME_PARAMETER_NAME));
        final String conferenceTitle = String.valueOf(request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME));
        final String reportText = String.valueOf(request.getParameter(REPORT_TEXT_PARAMETER_NAME));
        final String reportType = String.valueOf(request.getParameter(REPORT_TYPE_PARAMETER_NAME));
        final String applicantNickname = String.valueOf(request.getParameter(APPLICANT_PARAMETER_NAME));
        final Long questionReportId = Long.valueOf(request.getParameter(QUESTION_REPORT_ID_PARAMETER_NAME));
        final Long updaterId = Long.valueOf(request.getParameter(UPDATER_ID_PARAMETER_NAME));
        final String updaterRole = String.valueOf(request.getParameter(UPDATER_ROLE_PARAMETER_NAME));
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        final List<User> users = service.findAllUsers();

        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceWithSuchTitleExistInSystem(conferenceTitle)
                || !validator.isSectionWithSuchNameExistInSystem(sectionName)
                || !validator.isUserWithIdExistInSystem(updaterId)
                || !validator.isReportExistInSystem(id)
                || !validator.isReportTypeExistInSystem(reportType)
                || !validator.isUserWithNicknameExistInSystem(applicantNickname)
                || !validator.isRoleWithSuchNameExistInSystem(updaterRole)) {
            return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        //validation of questionReportId
        if (questionReportId != 0) {
            if(!validator.isReportExistInSystem(questionReportId)) {
                return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
            }
        }

        // data preparation section
        Long sectionId = null;
        Long sectionManagerId = null;
        for (Section section: sections
        ) {
            if (section.getSectionName().equals(sectionName)) {
                sectionId = section.getId();
            }
        }

        for (Section section: sections
             ) {
            if (section.getId().equals(sectionId)) {
                sectionManagerId = section.getManagerSect();
            }
        }

        Long conferenceId = null;
        for (Conference conference: conferences
        ) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                conferenceId = conference.getId();
            }
        }

        ReportType reportTypeToUpdate = null;
        for (ReportType reportTypeParameter: ReportType.valuesAsList()
        ) {
            if (reportTypeParameter.getName().equals(reportType)) {
                reportTypeToUpdate = reportTypeParameter;
            }
        }

        Long applicantId = null;
        for (User user: users
        ) {
            if (user.getNickname().equals(applicantNickname)) {
                applicantId = user.getId();
            }
        }

        // validation of permission to update
        if (!updaterId.equals(applicantId) && !updaterRole.equals("ADMIN") && !updaterId.equals(sectionManagerId)) {
            return prepareErrorPage(request, NO_PERMISSION_TO_UPDATE_REPORT_MSG);
        }

        // validation of string
        if (reportText.trim().equals("")) {
            return prepareErrorPage(request, INVALID_REPORT_TEXT_MSG);
        }

        if (!validator.isStringValid(reportText)) {
            return prepareErrorPage(request, INVALID_REPORT_TEXT_NOT_UTF8_MSG);
        }

        Report reportToUpdate = new Report(id, sectionId, conferenceId, reportText, reportTypeToUpdate, applicantId, questionReportId);

        service.updateReport(reportToUpdate);

        // return sectionReports page of the updated report section
        final List<Report> reports = service.findAllReportsBySectionID(sectionId, conferenceId);
        request.setAttribute(REPORTS_ATTRIBUTE_NAME, reports);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(CONFERENCE_TITLE_PARAMETER_NAME, conferenceTitle);
        return SHOW_REPORTS_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPage(CommandRequest request,
                                             String errorMessage) {
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        final List<User> users = service.findAllUsers();
        final Optional<Report> report = service.findReportByID(id);
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(REPORT_ATTRIBUTE_NAME, report);
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);

        return UPDATE_REPORT_ERROR_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return UPDATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}