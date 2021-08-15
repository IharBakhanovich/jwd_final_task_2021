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

public class ShowQuestionPage implements Command {

//    private static final CommandResponse SHOW_QUESTIONS_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/questions.jsp");
//
//    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    public static final String QUESTIONS_ATTRIBUTE_NAME = "questions";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    public static final String MANAGER_ID_PARAMETER_NAME = "managerId";
//    public static final String MANAGER_ID_ATTRIBUTE_NAME = "managerId";
//    public static final String MANAGER_ROLE_PARAMETER_NAME = "managerRole";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final CommandResponse SHOW_QUESTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    private final UserService service;
    private final Validator validator;

    private static class ShowQuestionPagePageHolder {
        private final static ShowQuestionPage instance
                = new ShowQuestionPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowQuestionPage getInstance() {
        return ShowQuestionPage.ShowQuestionPagePageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowQuestionPage() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long managerId = Long.valueOf(request.getParameter(ApplicationConstants.MANAGER_ID_PARAMETER_NAME));
        final String managerRole = request.getParameter(ApplicationConstants.MANAGER_ROLE_PARAMETER_NAME);
        // validation of the parameters (whether they exist in the request)
        if (!validator.isUserWithIdExistInSystem(managerId)
                || !validator.isRoleWithSuchNameExistInSystem(managerRole)
                || !validator.isUserIdAndUserRoleFromTheSameUser(String.valueOf(managerId), managerRole)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }
        request.setAttribute(ApplicationConstants.MANAGER_ID_ATTRIBUTE_NAME, managerId);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        final List<Report> questions = service.findAllQuestions(managerId);
        request.setAttribute(ApplicationConstants.QUESTIONS_ATTRIBUTE_NAME, questions);
        final List<Section> sections = service.findAllSections();
        request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        return ApplicationConstants.SHOW_QUESTIONS_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_QUESTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
