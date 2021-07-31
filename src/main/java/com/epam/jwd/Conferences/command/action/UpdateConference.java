package com.epam.jwd.Conferences.command.action;

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

public class UpdateConference implements Command {

    private static final Logger logger = LogManager.getLogger(CreateNewUser.class);

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String MANAGER_CONF_PARAMETER_NAME = "managerConf";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String MANAGER_CONFERENCE_ATTRIBUTE_NAME = "managerConf";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";

    private static final CommandResponse CONFERENCE_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final CommandResponse CONFERENCE_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    UpdateConference() {
        service = UserService.retrieve();
    }

    private static class UpdateConferenceHolder {
        private final static UpdateConference instance
                = new UpdateConference();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static UpdateConference getInstance() {
        return UpdateConference.UpdateConferenceHolder.instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {

        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String managerConf = request.getParameter(MANAGER_CONF_PARAMETER_NAME);
        final String creatorId = String.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));

        final List<User> users = service.findAllUsers();

        if (!creatorRole.equals("ADMIN")) {
            return prepareErrorPage(request,
                    "You have no permission to update a new conference. Please DO NOT try again");
        } else if (conferenceTitle == null || conferenceTitle.trim().equals("")) {
            return prepareErrorPage(request,
                    "Conference title should not be empty or contains only spaces. Please try again");
        } else if (!isStringValid(conferenceTitle)) {
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

        Conference conferencetoUpdate = new Conference(conferenceId, conferenceTitle, managerId);
        service.updateConference(conferencetoUpdate);
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);

        return CONFERENCE_UPDATE_SUCCESS_RESPONSE;
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        final List<Conference> conferences = service.findAllConferences();
        for (Conference conference: conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                request.setAttribute(MANAGER_CONFERENCE_ATTRIBUTE_NAME, conference.getManagerConf());
            }
        }
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        return CONFERENCE_UPDATE_ERROR_RESPONSE;
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
