package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;
import java.util.Optional;

/**
 * Implements 'delete_report' action. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class DeleteReport implements Command {
    private final UserService service;
    private final Validator validator;

    private static class DeleteReportHolder {
        private final static DeleteReport instance
                = new DeleteReport();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static DeleteReport getInstance() {
        return DeleteReport.DeleteReportHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private DeleteReport() {
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
        final Long reportId = Long.valueOf(request.getParameter(ApplicationConstants.REPORT_ID_PARAMETER_NAME));
        final Long deleterId
                = Long.valueOf(request.getParameter(ApplicationConstants.DELETER_ID_PARAMETER_NAME));
        final String deleterRole
                = String.valueOf(request.getParameter(ApplicationConstants.DELETER_ROLE_PARAMETER_NAME));
        final String applicationToken
                = String.valueOf(request.getParameter(ApplicationConstants.APPLICATION_TOKEN_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(ApplicationConstants.SECTION_ID_PARAMETER_NAME));
        final Long conferenceId = Long.valueOf(request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME));
        final Long applicantId = Long.valueOf(request.getParameter(ApplicationConstants.APPLICANT_PARAMETER_NAME));
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        final List<User> users = service.findAllUsers();

        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isUserWithIdExistInSystem(deleterId)
                || !validator.isReportExistInSystem(reportId)
                || !validator.isUserWithIdExistInSystem(applicantId)
                || !validator.isRoleWithSuchNameExistInSystem(deleterRole)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        // data preparation section
        Long sectionManagerId = null;
        String sectionName = null;
        for (Section section : sections
        ) {
            if (section.getId().equals(sectionId)) {
                sectionManagerId = section.getManagerSect();
                sectionName = section.getSectionName();
            }
        }

        String conferenceTitle = null;
        for (Conference conference: conferences
             ) {
            if(conference.getId().equals(conferenceId)) {
                conferenceTitle = conference.getConferenceTitle();
            }
        }

        // validation of permission to delete the report
        if (!deleterId.equals(applicantId)
                && !deleterRole.equals(ApplicationConstants.ADMIN_CONSTANT)
                && !deleterId.equals(sectionManagerId)) {
            return prepareErrorPage(request, ApplicationConstants.NO_PERMISSION_TO_UPDATE_REPORT_MSG);
        }

        service.deleteReport(reportId);

        if (applicationToken.equals(ApplicationConstants.APPLICANT_APPLICATION_APPLICATION_TOKEN_VALUE)) {
            final Long managerId = Long.valueOf(request.getParameter(ApplicationConstants.MANAGER_ID_PARAMETER_NAME));
            request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME,
                    ApplicationConstants.SECTION_NAME_VALUE_FOR_APPLICATIONS_PAGE);
            request.setAttribute(ApplicationConstants.MANAGER_ID_ATTRIBUTE_NAME, managerId);
            request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
            final List<Report> applications = service.findApplicantApplications(managerId);
            request.setAttribute(ApplicationConstants.APPLICATIONS_ATTRIBUTE_NAME, applications);
            request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
            request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
            return ApplicationConstants.SHOW_APPLICATIONS_PAGE_RESPONSE;
        } else if (applicationToken.equals(ApplicationConstants.USER_APPLICATION_APPLICATION_TOKEN_VALUE)) {
            final Long managerId = Long.valueOf(request.getParameter(ApplicationConstants.MANAGER_ID_PARAMETER_NAME));
            request.setAttribute(ApplicationConstants.MANAGER_ID_ATTRIBUTE_NAME, managerId);
            request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
            final List<Report> applications = service.findAllApplications(managerId);
            request.setAttribute(ApplicationConstants.APPLICATIONS_ATTRIBUTE_NAME, applications);
            request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
            request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
            return ApplicationConstants.SHOW_APPLICATIONS_PAGE_RESPONSE;
        } else {
            // return sectionReports page of the updated report section
            final List<Report> reports = service.findAllReportsBySectionID(sectionId, conferenceId);
            request.setAttribute(ApplicationConstants.REPORTS_ATTRIBUTE_NAME, reports);
            request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME, sectionName);
            request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
            request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_PARAMETER_NAME, conferenceTitle);
            request.setAttribute(ApplicationConstants.SECTION_ID_ATTRIBUTE_NAME, sectionId);
            request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
            request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
            return ApplicationConstants.SHOW_REPORTS_PAGE_RESPONSE;
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request,
                                             String errorMessage) {
        final Long id = Long.valueOf(request.getParameter(ApplicationConstants.ID_PARAMETER_NAME));
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        final List<User> users = service.findAllUsers();
        final Optional<Report> report = service.findReportByID(id);
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(ApplicationConstants.REPORT_ATTRIBUTE_NAME, report);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);

        return ApplicationConstants.UPDATE_REPORT_ERROR_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.UPDATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
