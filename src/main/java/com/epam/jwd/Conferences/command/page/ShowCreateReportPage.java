package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.service.UserService;

public class ShowCreateReportPage implements Command {
    private static final CommandResponse CREATE_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";

    public static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    public static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    public static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
    private static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";

    // the AppService, that communicates with the repo
    private final UserService userService;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateReportPage() {
        this.userService = UserService.retrieve();
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
        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String conferenceId = request.getParameter(CONFERENCE_ID_PARAMETER_NAME);
        final String creatorId = request.getParameter(CREATOR_ID_PARAMETER_NAME);
        final String creatorRole = request.getParameter(CREATOR_ROLE_PARAMETER_NAME);
        final String sectionId = request.getParameter(SECTION_ID_PARAMETER_NAME);
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CREATOR_ID_ATTRIBUTE_NAME, creatorId);
        request.setAttribute(CREATOR_ROLE_ATTRIBUTE_NAME, creatorRole);
        request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        return CREATE_REPORT_PAGE_RESPONSE;
    }
}
