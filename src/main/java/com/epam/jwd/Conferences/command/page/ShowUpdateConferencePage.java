package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;

/**
 * Implements 'show_update_conference' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class ShowUpdateConferencePage implements Command {
//    private static final CommandResponse UPDATE_CONFERENCE_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
//    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    private static final String MANAGER_CONFERENCE_ATTRIBUTE_NAME = "managerConf";
//    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
//    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
//
//    private static final CommandResponse SHOW_UPDATE_CONFERENCE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";

    // the AppService, that communicates with the repo
    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowUpdateConferencePage() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class ShowUpdateConferencePageHolder {
        private final static ShowUpdateConferencePage instance
                = new ShowUpdateConferencePage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowUpdateConferencePage getInstance() {
        return ShowUpdateConferencePage.ShowUpdateConferencePageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long conferenceId = Long.valueOf(request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME));
        final String creatorId = request.getParameter(ApplicationConstants.CREATOR_ID_PARAMETER_NAME);
        final String creatorRole = request.getParameter(ApplicationConstants.CREATOR_ROLE_PARAMETER_NAME);
        // validation of the parameters (whether they exist in the request)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isUserWithIdExistInSystem(Long.valueOf(creatorId))
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole)
                || !validator.isUserIdAndUserRoleFromTheSameUser(creatorId, creatorRole)) {
            return prepareErrorPageBackToMainPage(request, ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        final List<Conference> conferences = service.findAllConferences();
        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                request.setAttribute(ApplicationConstants.MANAGER_CONFERENCE_ATTRIBUTE_NAME, conference.getManagerConf());
            }
        }
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        return ApplicationConstants.UPDATE_CONFERENCE_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_UPDATE_CONFERENCE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}