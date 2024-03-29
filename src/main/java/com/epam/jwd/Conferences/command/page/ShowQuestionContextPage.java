package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;

/**
 * Implements 'show_question_context' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowQuestionContextPage implements Command {

//    private static final String QUESTION_ID_PARAMETER_NAME_FOR_CONTEXT = "questionIdForContext";
//    private static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_CONTEXT = "questionReportIdForContext";
//    private static final String REPORTS_ATTRIBUTE_NAME = "reports";
//    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    // to choose which variant of the reports.jsp page to show
//    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
//    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
//    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
//    private static final String QUESTION_TOKEN_NAME = "question";
//    private static final String APPLICANT_QUESTIONS_TOKEN_NAME = "applicantQuestions";
//    public static final String MANAGER_ID_PARAMETER_NAME = "managerId";
//    public static final String APPLICANT_QUESTIONS_APPLICATION_TOKEN_VALUE = "applicantQuestions";
//    public static final String USER_QUESTIONS_APPLICATION_TOKEN_VALUE = "";
//    private static final CommandResponse SHOW_QUESTION_CONTEXT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private final UserService service;
    private final Validator validator;

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
        this.validator = Validator.retrieve();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String sectionName = request.getParameter(ApplicationConstants.SECTION_NAME_PARAMETER_NAME);
        final Long questionId = Long.valueOf(request.getParameter(ApplicationConstants.QUESTION_ID_PARAMETER_NAME_FOR_CONTEXT));
        final Long questionReportId = Long.valueOf(request.getParameter(ApplicationConstants.QUESTION_REPORT_ID_PARAMETER_NAME_FOR_CONTEXT));
        final Long managerId = Long.valueOf(request.getParameter(ApplicationConstants.MANAGER_ID_PARAMETER_NAME));
        // validation of the parameters (whether they exist in the request)
        if (!validator.isReportExistInSystem(questionId)
                || !validator.isUserWithIdExistInSystem(managerId)
                || !(sectionName.equals(ApplicationConstants.APPLICANT_QUESTIONS_APPLICATION_TOKEN_VALUE)
                || sectionName.equals(ApplicationConstants.USER_QUESTIONS_APPLICATION_TOKEN_VALUE))) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }
        List<Report> questionContextReports;
        if (questionReportId != 0) {
            if (!validator.isReportExistInSystem(questionReportId)) {
                return prepareErrorPageBackToMainPage(request,
                        ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
            }
            questionContextReports = service.findAllReportsByQuestionId(questionReportId);
        } else {
            questionContextReports = service.findAllReportsByQuestionId(questionId);
        }
        final List<Conference> conferences = service.findAllConferences();
        final List<Section> sections = service.findAllSections();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(ApplicationConstants.REPORTS_ATTRIBUTE_NAME, questionContextReports);
        request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME,
                ApplicationConstants.QUESTION_TOKEN_NAME);
        request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        return ApplicationConstants.SHOW_REPORTS_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_QUESTION_CONTEXT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}