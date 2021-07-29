package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
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
    private static final Logger logger = LogManager.getLogger(CreateNewUser.class);

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String MANAGER_CONF_PARAMETER_NAME = "managerConf";
    private static final CommandResponse CREATE_NEW_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    public static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    public static final String USERS_ATTRIBUTE_NAME = "users";
    private static final CommandResponse CONFERENCE_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String DUPLICATE_CONFERENCE_MESSAGE
            = "The conference with such a conference title already exist in the system. Please choose an other conference title.";

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    CreateConference() {
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

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String confTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String managerConf = request.getParameter(MANAGER_CONF_PARAMETER_NAME);
        final String creatorId = String.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
        final List<User> users = service.findAllUsers();

        if (!creatorRole.equals("ADMIN")) {
            return prepareErrorPage(request,
                    "You have no permission to create a new conference. Please DO NOT try again");
        } else if (confTitle == null || confTitle.trim().equals("")) {
            return prepareErrorPage(request,
                    "Conference title should be not empty or contains only spaces. Please try again");
        } else if (!isStringValid(confTitle)) {
            return prepareErrorPage(request,
                    "The entered conference title is not valid. It should contain only latin letters. Please try again");
        }

        Long managerId = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(managerConf)) {
                managerId = user.getId();
            }
        }

        Conference conferenceToCreate = new Conference(1L, confTitle, managerId);
        try {
            service.createConference(conferenceToCreate);
            final List<Conference> updatedConferences = service.findAllConferences();
            request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, updatedConferences);
            final List<User> users1 = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, users1);

            return CONFERENCE_CREATION_SUCCESS_RESPONSE;
        } catch (DuplicateException e) {
            logger.info(DUPLICATE_CONFERENCE_MESSAGE);
            return prepareErrorPage(request, DUPLICATE_CONFERENCE_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return CREATE_NEW_USER_ERROR_RESPONSE;
    }

    private boolean isStringValid(String toValidate) {
        byte[] byteArrray = toValidate.getBytes();
        return isUTF8(byteArrray);
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}
