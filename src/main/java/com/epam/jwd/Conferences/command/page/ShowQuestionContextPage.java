package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowQuestionContextPage implements Command {

    private static final String QUESTION_ID_PARAMETER_NAME_FOR_CONTEXT = "questionIdForContext";
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_CONTEXT = "questionReportIdForContext";
    private static final String REPORTS_ATTRIBUTE_NAME = "reports";
    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    private static final String USERS_ATTRIBUTE_NAME = "users";
    // to choose which variant of the reports.jsp page to show
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String QUESTION_TOKEN_NAME = "question";


    private final UserService service;

    private static class ShowQuestionContextPageHolder {
        private final static ShowQuestionContextPage instance
                = new ShowQuestionContextPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowQuestionContextPage getInstance() {
        return ShowQuestionContextPage.ShowQuestionContextPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowQuestionContextPage() {
        this.service = UserService.retrieve();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long questionId = Long.valueOf(request.getParameter(QUESTION_ID_PARAMETER_NAME_FOR_CONTEXT));
        final Long questionReportId = Long.valueOf(request.getParameter(QUESTION_REPORT_ID_PARAMETER_NAME_FOR_CONTEXT));
        List<Report> questionContextReports = null;
        if (questionReportId!=0) {
            questionContextReports = service.findAllReportsByQuestionId(questionReportId);
        } else {
            questionContextReports = service.findAllReportsByQuestionId(questionId);
        }
        request.setAttribute(REPORTS_ATTRIBUTE_NAME, questionContextReports);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, QUESTION_TOKEN_NAME);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return SHOW_REPORTS_PAGE_RESPONSE;
    }
}
