package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
    public static final String REPORT_ATTRIBUTE_NAME = "report";
    public static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    public static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    public static final String USERS_ATTRIBUTE_NAME = "users";
    public static final String REPORTS_ATTRIBUTE_NAME = "reports";
    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    public static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    public static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";

    private static final CommandResponse SHOW_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");

    private final UserService service;

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
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final String sectionName = String.valueOf(request.getParameter(SECTION_NAME_PARAMETER_NAME));
        final String conferenceTitle = String.valueOf(request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME));
        final String reportText = String.valueOf(request.getParameter(REPORT_TEXT_PARAMETER_NAME));
        final String reportType = String.valueOf(request.getParameter(REPORT_TYPE_PARAMETER_NAME));
        final String applicantNickname = String.valueOf(request.getParameter(APPLICANT_PARAMETER_NAME));
        final String updaterId = String.valueOf(request.getParameter(UPDATER_ID_PARAMETER_NAME));
        final String updaterRole = String.valueOf(request.getParameter(UPDATER_ROLE_PARAMETER_NAME));
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        final List<User> users = service.findAllUsers();

        // data preparation section
        Long sectionId = null;
        for (Section section: sections
        ) {
            if (section.getSectionName().equals(sectionName)) {
                sectionId = section.getId();
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
        if (!updaterId.equals(String.valueOf(applicantId)) && !updaterRole.equals("ADMIN")) {
            return prepareErrorPage(request,
                    "You has no permission to update this report. Please DO NOT try again"
            );
        }

        // validation of string
        if (!reportText.trim().equals("")) {
            if (!isStringValid(reportText)) {
                final Optional<Report> report = service.findReportByID(id);

                request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);

                request.setAttribute(USERS_ATTRIBUTE_NAME, users);

                request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
                request.setAttribute(REPORT_ATTRIBUTE_NAME, report);

                return prepareErrorPage(request,
                        "Report text is not valid. Please try again"
                );
            }
        }


        Report reportToUpdate = new Report(id, sectionId, conferenceId, reportText, reportTypeToUpdate, applicantId);

        service.updateReport(reportToUpdate);

        // return sectionReports page of the updated report section
        final List<Report> reports = service.findAllReportsBySectionID(sectionId, conferenceId);
        request.setAttribute(REPORTS_ATTRIBUTE_NAME, reports);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return SHOW_REPORTS_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPage(CommandRequest request,
                                             String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);

        return SHOW_REPORT_PAGE_RESPONSE;
    }

    private boolean isStringValid(String toValidate) {
        byte[] byteArrray = toValidate.getBytes();
        return isUTF8(byteArrray);
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}