package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.ReportType;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements 'show_create_answer' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowCreateAnswerPage implements Command {
//    private static final CommandResponse CREATE_ANSWER_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
//    private static final String CREATOR_ID_PARAMETER_NAME_FOR_ANSWER = "managerId";
//    private static final String CREATOR_ROLE_PARAMETER_NAME_FOR_ANSWER = "managerRole";
//    private static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_ANSWER = "questionId";
//    private static final String QUESTION_TEXT_PARAMETER_NAME = "questionText";
//    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
//    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
//
//    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
//    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
//    private static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
//    private static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";
//    private static final String QUESTION_REPORT_ID_ATTRIBUTE_NAME = "questionId";
//    private static final String QUESTION_TEXT_ATTRIBUTE_NAME = "questionText";
//    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
//    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";
//
//    private static final CommandResponse SHOW_CREATE_ANSWER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";

    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateAnswerPage() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class ShowCreateAnswerPageHolder {
        private final static ShowCreateAnswerPage instance
                = new ShowCreateAnswerPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateAnswerPage getInstance() {
        return ShowCreateAnswerPage.ShowCreateAnswerPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long questionReportId
                = Long.valueOf(request.getParameter(ApplicationConstants.QUESTION_REPORT_ID_PARAMETER_NAME_FOR_ANSWER));
        final Long creatorId
                = Long.valueOf(request.getParameter(ApplicationConstants.CREATOR_ID_PARAMETER_NAME_FOR_ANSWER));
        final String creatorRole
                = request.getParameter(ApplicationConstants.CREATOR_ROLE_PARAMETER_NAME_FOR_ANSWER);
        final Long conferenceId = Long.valueOf(request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(ApplicationConstants.SECTION_ID_PARAMETER_NAME));
        final String questionText = request.getParameter(ApplicationConstants.QUESTION_TEXT_PARAMETER_NAME);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isUserWithIdExistInSystem(creatorId)
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole)
                || !validator.isReportExistInSystem(questionReportId)
                || !validator.isReportWithSuchTextExistInSystem(questionText)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        final List<Conference> conferences = service.findAllConferences();
        String conferenceTitle = null;
        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                conferenceTitle = conference.getConferenceTitle();
            }
        }
        final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
        String sectionName = null;
        for (Section section : sections
        ) {
            if (section.getId().equals(sectionId)) {
                sectionName = section.getSectionName();
            }
        }

        request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(ApplicationConstants.CREATOR_ID_ATTRIBUTE_NAME, creatorId);
        request.setAttribute(ApplicationConstants.CREATOR_ROLE_ATTRIBUTE_NAME, creatorRole);
        request.setAttribute(ApplicationConstants.SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(ApplicationConstants.QUESTION_REPORT_ID_ATTRIBUTE_NAME, questionReportId);
        request.setAttribute(ApplicationConstants.QUESTION_TEXT_ATTRIBUTE_NAME, questionText);
        request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(ApplicationConstants.SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        List<ReportType> allowedReportTypes = new ArrayList<>();
        allowedReportTypes.add(ReportType.ANSWER);
        allowedReportTypes.add(ReportType.QUESTION);
        request.setAttribute(ApplicationConstants.ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME, allowedReportTypes);
        return ApplicationConstants.CREATE_ANSWER_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_CREATE_ANSWER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}