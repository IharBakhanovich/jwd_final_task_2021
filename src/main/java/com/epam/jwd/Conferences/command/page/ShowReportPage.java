package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowReportPage implements Command {
    private static final CommandResponse SHOW_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    private static final String ID_PARAMETER_NAME = "id";
    private static final String REPORT_ATTRIBUTE_NAME = "report";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    public static final String ID_OF_MANAGER_OF_REPORTS_SECTION_ATTRIBUTE_NAME = "idOfManagerOfReportsSection";
    private static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";
    private static final String MANAGER_ID_PARAMETER_NAME = "managerId";
    private static final String MANAGER_ROLE_PARAMETER_NAME = "managerRole";
    public static final String ADMIN_CONSTANT = "ADMIN";

    private final UserService service;

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
        String managerRole = request.getParameter(MANAGER_ROLE_PARAMETER_NAME);
        final Optional<Report> report = service.findReportByID(id);
        final List<Section> sections = service.findAllSections();
        Long sectionManagerId = null;
        for (Section section : sections
        ) {
            if (report.get().getSectionId().equals(section.getId())) {
                sectionManagerId = section.getManagerSect();
            }
        }
        List<ReportType> allowedReportTypes = new ArrayList<>();
        if (!managerRole.equals("") && managerRole != null) {
            Long managerId = Long.valueOf(request.getParameter(MANAGER_ID_PARAMETER_NAME));
            if (managerId.equals(report.get().getApplicant()) && !managerRole.equals(ADMIN_CONSTANT)) {
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
            } else if (managerRole.equals(ADMIN_CONSTANT) || managerId.equals(sectionManagerId)) {
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
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(REPORT_ATTRIBUTE_NAME, report);
        request.setAttribute(ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME, allowedReportTypes);
        request.setAttribute(ID_OF_MANAGER_OF_REPORTS_SECTION_ATTRIBUTE_NAME, sectionManagerId);
        return SHOW_REPORT_PAGE_RESPONSE;
    }
}
