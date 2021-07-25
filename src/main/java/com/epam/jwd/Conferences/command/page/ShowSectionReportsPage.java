package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

/**
 * This is a command for section's reports page. The singleton.
 */
public class ShowSectionReportsPage implements Command {

    public static final String ID_PARAMETER_NAME = "id";
    public static final String REPORTS_ATTRIBUTE_NAME = "reports";
    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    public static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    public static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    public static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    public static final String USERS_ATTRIBUTE_NAME = "users";
    private final UserService service;

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
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long sectionId = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_ATTRIBUTE_NAME));
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        final List<Report> reports = service.findAllReportsBySectionID(sectionId, conferenceId);
        request.setAttribute(REPORTS_ATTRIBUTE_NAME, reports);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return SHOW_REPORTS_PAGE_RESPONSE;
    }
}
