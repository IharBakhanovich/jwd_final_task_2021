package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Implements 'create_new_conference' action. The singleton.
 */
public class CreateConference implements Command {
    private static final Logger logger = LogManager.getLogger(CreateConference.class);

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String MANAGER_CONF_PARAMETER_NAME = "managerConf";
    private static final CommandResponse CREATE_NEW_CONFERENCE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final CommandResponse CONFERENCE_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String DUPLICATE_CONFERENCE_MESSAGE
            = "The conference with such a conference title already exist in the system. Please choose an other conference title.";
    private static final int MAX_LENGTH_OF_CONFERENCE_TITLE_IN_DB = 30;
    private static final String NO_PERMISSION_TO_CREATE_CONFERENCE_MSG = "YouHaveNoPermissionToCreateAConferenceMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_MSG = "ConferenceTitleShouldNotBeEmptyOrContainOnlySpacesMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_NOT_UTF8_MSG = "ConferenceTitleShouldContainOnlyLatinLettersMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_TOO_LONG_MSG = "ConferenceTitleIsTooLongMSG";

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private CreateConference() {
        service = UserService.retrieve();
    }

    private static class CreateConferenceHolder {
        private final static CreateConference instance
                = new CreateConference();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static CreateConference getInstance() {
        return CreateConference.CreateConferenceHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final String confTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String managerConf = request.getParameter(MANAGER_CONF_PARAMETER_NAME);
        final String creatorId = String.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
        final List<User> users = service.findAllUsers();

        if (!creatorRole.equals("ADMIN")) {
            return prepareErrorPage(request, NO_PERMISSION_TO_CREATE_CONFERENCE_MSG);
        } else if (confTitle == null || confTitle.trim().equals("")) {
            return prepareErrorPage(request, INVALID_CONFERENCE_TITLE_TEXT_MSG);
        } else if (!isStringValid(confTitle)) {
            return prepareErrorPage(request, INVALID_CONFERENCE_TITLE_TEXT_NOT_UTF8_MSG);
        } else if (confTitle.length() > MAX_LENGTH_OF_CONFERENCE_TITLE_IN_DB) {
            return prepareErrorPage(request, INVALID_CONFERENCE_TITLE_TEXT_TOO_LONG_MSG);
        }

        Long managerId = null;
        Role managerRole = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(managerConf)) {
                managerId = user.getId();
                managerRole = user.getRole();
            }
        }

        Conference conferenceToCreate = new Conference(1L, confTitle, managerId);
        try {
            service.createConference(conferenceToCreate);
            final List<Conference> updatedConferences = service.findAllConferences();
            request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, updatedConferences);
            final List<User> users1 = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, users1);
            // sets a role 'MANAGER' to the manager of a new conference if is not the Role.Manager
            if (managerRole != Role.ADMIN && managerRole != Role.MANAGER) {
                service.updateUserRole(managerId, Role.MANAGER.getId());
            }

            return CONFERENCE_CREATION_SUCCESS_RESPONSE;
        } catch (DuplicateException e) {
            logger.info(DUPLICATE_CONFERENCE_MESSAGE);
            return prepareErrorPage(request, DUPLICATE_CONFERENCE_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return CREATE_NEW_CONFERENCE_ERROR_RESPONSE;
    }

    private boolean isStringValid(String toValidate) {
        byte[] byteArray = toValidate.getBytes();
        return isUTF8(byteArray);
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}
