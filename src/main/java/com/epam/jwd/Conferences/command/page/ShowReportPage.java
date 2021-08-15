package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowReportPage implements Command {
//    private static final CommandResponse SHOW_REPORT_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
//    private static final String ID_PARAMETER_NAME = "id";
//    private static final String REPORT_ATTRIBUTE_NAME = "report";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    public static final String ID_OF_MANAGER_OF_REPORTS_SECTION_ATTRIBUTE_NAME = "idOfManagerOfReportsSection";
//    private static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";
//    private static final String MANAGER_ID_PARAMETER_NAME = "managerId";
//    private static final String MANAGER_ROLE_PARAMETER_NAME = "managerRole";
//    public static final String ADMIN_CONSTANT = "ADMIN";
//    private static final CommandResponse SHOW_REPORT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private final UserService service;
    private final Validator validator;

    private static class ShowReportPageHolder {
        private final static ShowReportPage instance
                = new ShowReportPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowReportPage getInstance() {
        return ShowReportPage.ShowReportPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowReportPage() {
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
        String managerRole = request.getParameter(ApplicationConstants.MANAGER_ROLE_PARAMETER_NAME);
        final Optional<Report> report = service.findReportByID(id);
        final List<Section> sections = service.findAllSections();
        // validation of the parameters (whether they exist in the request)
        if (!validator.isReportExistInSystem(id)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        Long sectionManagerId = null;
        for (Section section : sections
        ) {
            if (report.get().getSectionId().equals(section.getId())) {
                sectionManagerId = section.getManagerSect();
            }
        }
        List<ReportType> allowedReportTypes = new ArrayList<>();
        if (!managerRole.equals("") && managerRole != null) {
            Long managerId = Long.valueOf(request.getParameter(ApplicationConstants.MANAGER_ID_PARAMETER_NAME));
            if (!validator.isUserWithIdExistInSystem(managerId)
                    || !validator.isUserIdAndUserRoleFromTheSameUser(String.valueOf(managerId), managerRole)) {
                return prepareErrorPageBackToMainPage(request, ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
            }
            if (managerId.equals(report.get().getApplicant())
                    && !managerRole.equals(ApplicationConstants.ADMIN_CONSTANT)) {
                switch (report.get().getReportType()) {
                    case APPLICATION:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.CANCELLED);
                        break;
                    case APPROVED:
                        allowedReportTypes.add(ReportType.APPROVED);
                        allowedReportTypes.add(ReportType.CANCELLED);
                        break;
                    case REJECTED:
                        allowedReportTypes.add(ReportType.REJECTED);
                        break;
                    case CANCELLED:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.CANCELLED);
                        break;
                    case QUESTION:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.CANCELLED);
                        break;
                    case ANSWER:
                        allowedReportTypes.add(ReportType.ANSWER);
                        break;
                }
            } else if (managerRole.equals(ApplicationConstants.ADMIN_CONSTANT)
                    || managerId.equals(sectionManagerId)) {
                switch (report.get().getReportType()) {
                    case CANCELLED:
                        allowedReportTypes.add(ReportType.CANCELLED);
                        break;
                    case ANSWER:
                        allowedReportTypes.add(ReportType.ANSWER);
                        allowedReportTypes.add(ReportType.QUESTION);
                        break;
                    case REJECTED:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.REJECTED);
                        allowedReportTypes.add(ReportType.APPROVED);
                        break;
                    case APPROVED:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.REJECTED);
                        allowedReportTypes.add(ReportType.APPROVED);
                        break;
                    case QUESTION:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.REJECTED);
                        allowedReportTypes.add(ReportType.APPROVED);
                        break;
                    case APPLICATION:
                        allowedReportTypes.add(ReportType.APPLICATION);
                        allowedReportTypes.add(ReportType.QUESTION);
                        allowedReportTypes.add(ReportType.REJECTED);
                        allowedReportTypes.add(ReportType.APPROVED);
                        break;
                }
            }
        }
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(ApplicationConstants.REPORT_ATTRIBUTE_NAME, report);
        request.setAttribute(ApplicationConstants.ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME, allowedReportTypes);
        request.setAttribute(ApplicationConstants.ID_OF_MANAGER_OF_REPORTS_SECTION_ATTRIBUTE_NAME, sectionManagerId);
        return ApplicationConstants.SHOW_REPORT_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_REPORT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
