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

public class CreateSection implements Command {
    private static final Logger logger = LogManager.getLogger(CreateSection.class);

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    private static final String MANAGER_SECTION_PARAMETER_NAME = "managerSect";
    private static final CommandResponse CREATE_NEW_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final CommandResponse SECTION_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    private static final String DUPLICATE_SECTION_MESSAGE
            = "The section with such an section name title already exist in the system. Please choose an other section name.";
    private static final String CONFERENCE_MANAGER_ATTRIBUTE_NAME = "conferenceManager";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
    private static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;
    private static final String INVALID_SECTION_NAME_MSG = "SectionNameShouldNotBeEmptyMSG";
    private static final String INVALID_SECTION_NAME_NOT_UTF8_MSG = "SectionNameShouldContainOnlyLatinSignsMSG";
    private static final String NO_PERMISSION_TO_CREATE_SECTION_MSG = "YouHaveNoPermissionToCreateASectionMSG";
    private static final String INVALID_SECTION_NAME_TOO_LONG_MSG = "SectionNameIsTooLongMSG";

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private CreateSection() {
        service = UserService.retrieve();
    }

    private static class CreateSectionHolder {
        private final static CreateSection instance
                = new CreateSection();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static CreateSection getInstance() {
        return CreateSection.CreateSectionHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        final String managerSect = request.getParameter(MANAGER_SECTION_PARAMETER_NAME);
        final Long creatorId = Long.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));

        final List<User> users = service.findAllUsers();
        final List<Conference> conferences = service.findAllConferences();

        Long conferenceManagerId = null;
        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        if (!creatorRole.equals("ADMIN") && !(creatorId.equals(conferenceManagerId))) {
            return prepareErrorPage(request, NO_PERMISSION_TO_CREATE_SECTION_MSG);
        } else if (sectionName == null || sectionName.trim().equals("")) {
            return prepareErrorPage(request, INVALID_SECTION_NAME_MSG);
        } else if (!isStringValid(sectionName)) {
            return prepareErrorPage(request, INVALID_SECTION_NAME_NOT_UTF8_MSG);
        } else if (sectionName.length() > MAX_LENGTH_OF_SECTION_NAME_IN_DB) {
            return prepareErrorPage(request, INVALID_SECTION_NAME_TOO_LONG_MSG);
        }

        Long managerId = null;
        Role sectionManagerRole = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(managerSect)) {
                managerId = user.getId();
                sectionManagerRole = user.getRole();
            }
        }
        Section sectionToCreate = new Section(1L, conferenceId, sectionName, managerId);
        try {
            service.createSection(sectionToCreate);
            final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
            request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
            final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
            request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
            request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            final List<User> users1 = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, users1);
            request.setAttribute(CONFERENCE_MANAGER_ATTRIBUTE_NAME, conferenceManagerId);
            // sets a role 'MANAGER' to the manager of a new section if is not the Role.Manager
            if (sectionManagerRole != Role.ADMIN && sectionManagerRole != Role.MANAGER) {
                service.updateUserRole(managerId, Role.MANAGER.getId());
            }
            return SECTION_CREATION_SUCCESS_RESPONSE;
        } catch (DuplicateException e) {
            logger.info(DUPLICATE_SECTION_MESSAGE);
            return prepareErrorPage(request, DUPLICATE_SECTION_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String conferenceId = request.getParameter(CONFERENCE_ID_PARAMETER_NAME);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        final List<Conference> conferences = service.findAllConferences();
        Long conferenceManagerId = null;
        for (Conference conference: conferences
        ) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);

        return CREATE_NEW_SECTION_ERROR_RESPONSE;
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
