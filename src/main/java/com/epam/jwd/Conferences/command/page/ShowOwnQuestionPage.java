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
 * Implements 'show_own_questions' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowOwnQuestionPage implements Command {

//    private static final CommandResponse SHOW_QUESTIONS_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/questions.jsp");
//
//    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    public static final String QUESTIONS_ATTRIBUTE_NAME = "questions";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    public static final String MANAGER_ID_PARAMETER_NAME = "managerId";
//    public static final String MANAGER_ID_ATTRIBUTE_NAME = "managerId";
//    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
//    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
//    public static final String MANAGER_ROLE_PARAMETER_NAME = "managerRole";
//    public static final String APPLICANT_QUESTIONS_TOKEN = "applicantQuestions";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final CommandResponse SHOW_OWN_QUESTIONS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    private final UserService service;
    private final Validator validator;

    private static class ShowOwnQuestionPageHolder {
        private final static ShowOwnQuestionPage instance
                = new ShowOwnQuestionPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowOwnQuestionPage getInstance() {
        return ShowOwnQuestionPage.ShowOwnQuestionPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowOwnQuestionPage() {
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
        final String sectionName = request.getParameter(ApplicationConstants.SECTION_NAME_PARAMETER_NAME);
        final Long managerId = Long.valueOf(request.getParameter(ApplicationConstants.MANAGER_ID_PARAMETER_NAME));
        final String managerRole = request.getParameter(ApplicationConstants.MANAGER_ROLE_PARAMETER_NAME);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isUserWithIdExistInSystem(managerId)
                || !validator.isRoleWithSuchNameExistInSystem(managerRole)
                || !validator.isUserIdAndUserRoleFromTheSameUser(String.valueOf(managerId), managerRole)
                || !sectionName.equals(ApplicationConstants.APPLICANT_QUESTIONS_TOKEN)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(ApplicationConstants.MANAGER_ID_ATTRIBUTE_NAME, managerId);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        final List<Report> questions = service.findApplicantQuestions(managerId);
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
        return ApplicationConstants.SHOW_OWN_QUESTIONS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}