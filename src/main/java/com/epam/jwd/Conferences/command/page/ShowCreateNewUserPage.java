package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.validator.Validator;

public class ShowCreateNewUserPage implements Command {

    private static final CommandResponse SHOW_CREATE_NEW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");
    private static final String USER_NAME_SESSION_ATTRIBUTE = "userName";
    private static final String USER_ROLE_SESSION_ATTRIBUTE = "userRole";
    private static final String USER_ID_SESSION_ATTRIBUTE = "userId";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";
    private static final String NO_PERMISSION_TO_CREATE_CONFERENCE_MSG = "YouHaveNoPermissionToCreateAConferenceMSG";
    private static final CommandResponse SHOW_CREATE_NEW_USER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowCreateNewUserPage() {
        this.validator = Validator.retrieve();
    }

    private static class ShowCreateNewUserPageHolder {
        private final static ShowCreateNewUserPage instance
                = new ShowCreateNewUserPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowCreateNewUserPage getInstance() {
        return ShowCreateNewUserPage.ShowCreateNewUserPageHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {

        Role creatorRole = (Role) request.getCurrentSession().get().getAttribute(USER_ROLE_SESSION_ATTRIBUTE);
        Long creatorId = (Long) request.getCurrentSession().get().getAttribute(USER_ID_SESSION_ATTRIBUTE);

        // validation of the parameters (whether they exist in the request)
        if (!validator.isUserWithIdExistInSystem(creatorId)
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole.getName())
                || !validator.isUserIdAndUserRoleFromTheSameUser(String.valueOf(creatorId), creatorRole.getName())) {
            return prepareErrorPageBackToMainPage(request, INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        // validation whether the really admin want to create Conference
        if (!creatorRole.getName().equals("ADMIN")) {
            return prepareErrorPageBackToMainPage(request, NO_PERMISSION_TO_CREATE_CONFERENCE_MSG);
        }
        return SHOW_CREATE_NEW_USER_PAGE_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return SHOW_CREATE_NEW_USER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
