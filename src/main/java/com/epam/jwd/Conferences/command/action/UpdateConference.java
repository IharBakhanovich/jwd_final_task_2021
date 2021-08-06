package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UpdateConference implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateConference.class);

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
    private static final String NO_PERMISSION_TO_UPDATE_CONFERENCE_MSG = "YouHaveNoPermissionToUpdateAConferenceMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_MSG = "ConferenceTitleShouldNotBeEmptyOrContainOnlySpacesMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_NOT_UTF8_MSG = "ConferenceTitleShouldContainOnlyLatinLetters";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_TOO_LONG_MSG = "ConferenceTitleIsTooLong";

    private static final int MAX_LENGTH_OF_CONFERENCE_TITLE_IN_DB = 30;

    private static final CommandResponse CONFERENCE_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final CommandResponse CONFERENCE_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private UpdateConference() {
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

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {

        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String managerConf = request.getParameter(MANAGER_CONF_PARAMETER_NAME);
        final String creatorId = String.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));

        final List<User> users = service.findAllUsers();

        if (!creatorRole.equals("ADMIN")) {
            return prepareErrorPage(request, NO_PERMISSION_TO_UPDATE_CONFERENCE_MSG);
        } else if (conferenceTitle == null || conferenceTitle.trim().equals("")) {
            return prepareErrorPage(request, INVALID_CONFERENCE_TITLE_TEXT_MSG);
        } else if (!isStringValid(conferenceTitle)) {
            return prepareErrorPage(request, INVALID_CONFERENCE_TITLE_TEXT_NOT_UTF8_MSG);
        } else if (conferenceTitle.length() > MAX_LENGTH_OF_CONFERENCE_TITLE_IN_DB) {
            return prepareErrorPage(request, INVALID_CONFERENCE_TITLE_TEXT_TOO_LONG_MSG);
        }

        Long managerId = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(managerConf)) {
                managerId = user.getId();
            }
        }

        // to have conferences and the status of the former conference manager before the conference update
        List<Conference> conferencesBeforeUpdate = service.findAllConferences();
        boolean isTheManagerOfThisConferenceRemainsManager = isTheManagerOfThisConferenceRemainsManager(conferenceId);
        Long formerManagerOrThisConferenceId = null;
        for (Conference conference : conferencesBeforeUpdate
        ) {
            if (conference.getId().equals(conferenceId)) {
                formerManagerOrThisConferenceId = conference.getManagerConf();
            }
        }
        Optional<User> oldConferenceManager = service.findUserByID(formerManagerOrThisConferenceId);
        Role currentRoleOfFormerConferenceManager = oldConferenceManager.get().getRole();

        // to update this conference
        Conference conferencetoUpdate = new Conference(conferenceId, conferenceTitle, managerId);
        service.updateConference(conferencetoUpdate);

        // to update role of the newSectionManager
        Optional<User> newConferenceManager = service.findUserByID(managerId);
        Role currentRoleOfNewConferenceManager = newConferenceManager.get().getRole();
        if (currentRoleOfNewConferenceManager != Role.ADMIN && currentRoleOfNewConferenceManager != Role.MANAGER) {
            service.updateUserRole(managerId, Role.MANAGER.getId());
        }
        // to check if it needed and update the role of the formerSectionManager
        if (!isTheManagerOfThisConferenceRemainsManager && currentRoleOfFormerConferenceManager != Role.USER) {
            service.updateUserRole(formerManagerOrThisConferenceId, Role.USER.getId());
        }

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

    private boolean isTheManagerOfThisConferenceRemainsManager(Long conferenceId) {
        // to prove whether the former Manager of this section is still the Manager of another sections or conferences
        boolean isThisManagerTheManagerOfAnotherConferenceOrSection = false;
        List<Section> sectionsBeforeUpdate = service.findAllSections();
        List<Conference> conferencesBeforeUpdate = service.findAllConferences();

        // to find former section manager
        User formerSectionManager = null;
        Long formerManagerOrThisSectionId = null;
        for (Conference conference : conferencesBeforeUpdate
        ) {
            if (conference.getId().equals(conferenceId)) {
                formerManagerOrThisSectionId = conference.getManagerConf();
            }
        }
        // to prove whether this Manager is the manager of the another section
        for (Section section : sectionsBeforeUpdate
        ) {
            if (section.getManagerSect().equals(formerManagerOrThisSectionId)) {
                isThisManagerTheManagerOfAnotherConferenceOrSection = true;
                break;
            }
        }

        // to prove whether this Manager is the manager of the another conference
        for (Conference conference : conferencesBeforeUpdate
        ) {
            if (conference.getManagerConf().equals(formerManagerOrThisSectionId)
                    && !conference.getId().equals(conferenceId)) {
                isThisManagerTheManagerOfAnotherConferenceOrSection = true;
                break;
            }
        }
        return isThisManagerTheManagerOfAnotherConferenceOrSection;
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
