package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.ReportType;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class ShowCreateAnswerPage implements Command {
    private static final CommandResponse CREATE_ANSWER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
    private static final String CREATOR_ID_PARAMETER_NAME_FOR_ANSWER = "managerId";
    private static final String CREATOR_ROLE_PARAMETER_NAME_FOR_ANSWER = "managerRole";
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_ANSWER = "questionId";
    private static final String QUESTION_TEXT_PARAMETER_NAME = "questionText";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";

    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    private static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
    private static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";
    private static final String QUESTION_REPORT_ID_ATTRIBUTE_NAME = "questionId";
    private static final String QUESTION_TEXT_ATTRIBUTE_NAME = "questionText";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";

    private static final CommandResponse SHOW_CREATE_ANSWER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String ERROR_ATTRIBUTE_NAME = "error";

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

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long questionReportId = Long.valueOf(request.getParameter(QUESTION_REPORT_ID_PARAMETER_NAME_FOR_ANSWER));
        final Long creatorId = Long.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME_FOR_ANSWER));
        final String creatorRole = request.getParameter(CREATOR_ROLE_PARAMETER_NAME_FOR_ANSWER);
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
        final String questionText = request.getParameter(QUESTION_TEXT_PARAMETER_NAME);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isSectionExistInSystem(sectionId)
                || !validator.isUserWithIdExistInSystem(creatorId)
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole)
                || !validator.isReportExistInSystem(questionReportId)
                || !validator.isReportWithSuchTextExistInSystem(questionText)) {
            return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
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

        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CREATOR_ID_ATTRIBUTE_NAME, creatorId);
        request.setAttribute(CREATOR_ROLE_ATTRIBUTE_NAME, creatorRole);
        request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(QUESTION_REPORT_ID_ATTRIBUTE_NAME, questionReportId);
        request.setAttribute(QUESTION_TEXT_ATTRIBUTE_NAME, questionText);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        List<ReportType> allowedReportTypes = new ArrayList<>();
        allowedReportTypes.add(ReportType.ANSWER);
        allowedReportTypes.add(ReportType.QUESTION);
        request.setAttribute(ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME, allowedReportTypes);
        return CREATE_ANSWER_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return SHOW_CREATE_ANSWER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
