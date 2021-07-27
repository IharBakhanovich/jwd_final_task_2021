package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowReportPage implements Command {
    private static final CommandResponse SHOW_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    public static final String ID_PARAMETER_NAME = "id";
    public static final String REPORT_ATTRIBUTE_NAME = "report";
    public static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    public static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    public static final String USERS_ATTRIBUTE_NAME = "users";
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
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final Optional<Report> report = service.findReportByID(id);
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        final List<Section> sections = service.findAllSections();
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(REPORT_ATTRIBUTE_NAME, report);
        return SHOW_REPORT_PAGE_RESPONSE;
    }
}
