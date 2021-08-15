package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;

public class ShowCreateConferencePage implements Command {

//    private static final CommandResponse CREATE_CONFERENCE_PAGE_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
//    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
//    private static final String NO_PERMISSION_TO_CREATE_CONFERENCE_MSG = "YouHaveNoPermissionToCreateAConferenceMSG";
//    private static final CommandResponse SHOW_CREATE_CONFERENCE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    public static final String ADMIN_CONSTANT = "ADMIN";

    // the AppService, that communicates with the repo
    private final UserService userService;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateConferencePage() {
        this.userService = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class ShowCreateConferencePageHolder {
        private final static ShowCreateConferencePage instance
                = new ShowCreateConferencePage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateConferencePage getInstance() {
        return ShowCreateConferencePage.ShowCreateConferencePageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long creatorId = Long.valueOf(request.getParameter(ApplicationConstants.CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = request.getParameter(ApplicationConstants.CREATOR_ROLE_PARAMETER_NAME);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isUserWithIdExistInSystem(creatorId)
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole)
                || !validator.isUserIdAndUserRoleFromTheSameUser(String.valueOf(creatorId), creatorRole)) {
            return prepareErrorPageBackToMainPage(request, ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        // validation whether the really admin want to create Conference
        if (!creatorRole.equals(ApplicationConstants.ADMIN_CONSTANT)) {
            return prepareErrorPageBackToMainPage(request, ApplicationConstants.NO_PERMISSION_TO_CREATE_CONFERENCE_MSG);
        }

        final List<User> users = userService.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        return ApplicationConstants.CREATE_CONFERENCE_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.SHOW_CREATE_CONFERENCE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
